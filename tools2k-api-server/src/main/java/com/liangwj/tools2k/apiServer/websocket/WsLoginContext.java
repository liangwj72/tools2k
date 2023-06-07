package com.liangwj.tools2k.apiServer.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.apiServer.beans.BaseSocketPackage;
import com.liangwj.tools2k.apiServer.beans.SocketServerEventPackage;
import com.liangwj.tools2k.apiServer.beans.manager.IConnHandlerInfo;
import com.liangwj.tools2k.apiServer.beans.manager.PayloadCounter;
import com.liangwj.tools2k.apiServer.beans.manager.WsConnectInfoBean;
import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;
import com.liangwj.tools2k.apiServer.security.BaseLoginContext;
import com.liangwj.tools2k.apiServer.security.CounterService;
import com.liangwj.tools2k.apiServer.serviceInf.IWsApiServer;
import com.liangwj.tools2k.apiServer.websocket.beans.SimpleUserLoginEvent;
import com.liangwj.tools2k.beans.others.IFilter;
import com.liangwj.tools2k.utils.other.ConverterUtil;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;
import com.liangwj.tools2k.utils.threadPool.BaseThreadPool;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

/**
 * <pre>
 * 处理用户登录状态，并可查找在线用户
 * 
 * 主要方法:
 * - {@link #onLoginSuccess(IWebUser)} 用户登录时调用该方法，保存用户信息到session
 * - {@link #onLogout(Class)} 用户登出时调用，清楚session中的用户信息
 * - {@link #getUser(Class)} 获取当前线程中的用户信息
 * - {@link #getCurConnHandler() } 获取当前线程中的连接信息
 * - {@link #sendServerEventToAllUser(Object)} 发送服务器事件给所有用户
 * - {@link ConnHandler#sendServerEventNow(Object)} 立刻发送服务器事件
 * - {@link ConnHandler#sendServerEventAfterApiDone(Object)} 发送服务器事件，但推迟到api完成后再发送
 * - {@link ConnHandler#getUser(Class)} 获取当前连接的用户
 * </pre>
 * 
 * @author 梁韦江
 * 
 */
@Service
@ManagedResource(description = "Websocket Api服务")
@ADomainOrder(domainName = CommonMBeanDomainNaming.DOMAIN_API_SERVER, order = CommonMBeanDomainNaming.ORDER)
public class WsLoginContext extends BaseLoginContext implements IWsApiServer {

	/** 保存这个连接的信息 */
	public class ConnHandler implements IConnHandlerInfo {
		private final WebSocketSession session;
		private final String ipAddress;

		// 连接创建的时间
		private final long connectTime = System.currentTimeMillis();

		// 最后一次请求的时间
		private long lastRequestTime;

		private final PayloadCounter upCounter = new PayloadCounter(); // 上行使用情况
		private final PayloadCounter downCounter = new PayloadCounter(); // 下行使用情况

		/** 用户信息 */
		private IWebUser user;

		/** 最近10秒的访问量 */
		private long lastUpCountDiff = 0;
		private long lastUpCount = 0;

		/** 等待发送的服务器事件，服务器事件可选择在执行完接口后才发送 */
		private final List<Object> serverEventQueue = new LinkedList<>();

		public ConnHandler(WebSocketSession session) {
			super();

			Assert.notNull(session, "session 不能为空");
			Assert.notNull(session.getId(), "session id 不能为空");

			this.session = session;
			this.ipAddress = this.buildIpAddress();
		}

		/** 连接时间 */
		@Override
		public long getConnectTime() {
			return connectTime;
		}

		/** 下行流量统计 */
		@Override
		public PayloadCounter getDownCounter() {
			return downCounter;
		}

		@Override
		public String getIpAddress() {
			return ipAddress;
		}

		/** 获取最后一次请求的时间 */
		@Override
		public long getLastRequestTime() {
			return lastRequestTime;
		}

		/** 获取sessionId，用于根据sessionId断线 */
		@Override
		public String getSessionId() {
			if (this.session != null) {
				return this.session.getId();
			} else {
				return null;
			}
		}

		/** 上行流量统计 */
		@Override
		public PayloadCounter getUpCounter() {
			return upCounter;
		}

		@SuppressWarnings("unchecked")
		public <T extends IWebUser> T getUser(Class<T> userClass) {
			if (userClass != null && this.user != null && userClass.isAssignableFrom(this.user.getClass())) {
				return (T) this.user;
			} else {
				return null;
			}
		}

		/** 获取登陆的用户账号 */
		@Override
		public String getUserAccount() {
			if (this.user != null) {
				return user.getAccount();
			} else {
				return null;
			}
		}

		/** 获取用户对象类型，用于前端显示 */
		@Override
		public Class<? extends IWebUser> getUserClass() {
			if (this.user != null) {
				return this.user.getClass();
			} else {
				return null;
			}
		}

		/**
		 * 增加请求次数
		 * 
		 * @param payloadLength
		 */
		public void incrementRequest(int payloadLength, long useTime) {
			// 统计当前连接的流量
			this.upCounter.addPayload(payloadLength, useTime);
			// 更新最后请求发生的时间
			this.lastRequestTime = System.currentTimeMillis();
		}

		/** 发送服务器端事件，在api执行完成之后 */
		public void sendServerEventAfterApiDone(Object event) throws IOException {
			// 对这个队列的操作肯定是在同一个线程，所有没有线程安全问题
			this.serverEventQueue.add(event);
		}

		/** 立刻发送服务器端事件，在api执行完成之前 */
		public void sendServerEventNow(Object event) throws IOException {
			Assert.notNull(event, "要发送的事件不能为空");
			final SocketServerEventPackage pkg = new SocketServerEventPackage(event);
			this.sendMessage(pkg);
		}

		/** 生成bean，给前端使用 */
		public WsConnectInfoBean toWsConnectInfoBean() {
			return new WsConnectInfoBean(this);
		}

		@Override
		public long getLastUpCountDiff() {
			return lastUpCountDiff;
		}

		private String buildIpAddress() {
			final String checkIp = getSessionFromThread().getRemoteAddress().getAddress().toString();
			if (checkIp.startsWith("/")) {
				return checkIp.substring(1);
			}
			return checkIp;
		}

		/** 在api执行完成后，发送队列中的事件 */
		protected void afterApiDone() {
			// 对这个队列的操作肯定是在同一个线程，所有没有线程安全问题
			for (final Object event : this.serverEventQueue) {
				try {
					this.sendServerEventNow(event);
				} catch (final Throwable e) {
					log.debug("发送服务器事件到 连接(id={}) 时，发生了IO错误", this.session.getId());
				}
			}
			this.serverEventQueue.clear();
		}

		protected void onSaveHistroy() {
			final long last = this.upCounter.getCount();
			this.lastUpCountDiff = last - this.lastUpCount;
			this.lastUpCount = last;
		}

		protected void ping() {
			try {
				this.sendPlainText("ping", false);
			} catch (final Throwable e) {
				// ping失败了不用管
				log.warn("ws连接 (sessionId={}) ，发送ping时发生了错误", this.getSessionId());
				// LogUtil.traceError(log, e);
				try {
					// ping失败了就主动断线
					session.close();
				} catch (final IOException e1) {
				}
			}
		}

		private synchronized void sendPlainText(String text, boolean count) throws IOException {
			if (this.session == null || !this.session.isOpen()) {
				return;
			}

			final long start = System.nanoTime();
			// 封装包
			final TextMessage msg = new TextMessage(text);

			// 发送信息
			this.session.sendMessage(msg);

			if (count) {
				final long userTimeNano = System.nanoTime() - start;

				// 增加流量使用统计信息
				final int payload = msg.getPayloadLength();
				this.downCounter.addPayload(payload, userTimeNano); // 统计当前连接
				WsLoginContext.this.couterService.getWsDownCounter().addPayload(payload, userTimeNano); // 统计全局
			}
		}

		/** 发送消息 */
		protected void sendMessage(BaseSocketPackage data) throws IOException {
			if (session != null && data != null && session.isOpen()) {
				final String responseStr = JSON.toJSONString(data, SerializerFeature.PrettyFormat,
						SerializerFeature.SortField);
				this.sendPlainText(responseStr, true);
			}
		}
	}

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WsLoginContext.class);

	public final static String HANDLER_SESSION_NAME = "HANDLER_SESSION_NAME";

	@Autowired
	private CounterService couterService;

	/** 所有在线连接，key是session的id */
	private final Map<String, ConnHandler> onlineMap = new ConcurrentHashMap<>();

	/** 所有已登录连接，key是session id */
	private final Map<String, ConnHandler> userMap = new ConcurrentHashMap<>();

	private final ThreadLocal<WebSocketSession> sessions = new ThreadLocal<>();

	/** 定时获取运行状态的调度器 */
	private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

	/** 断线时调用 */
	public void afterConnectionClosed(WebSocketSession session) {
		if (session == null) {
			return;
		}

		final ConnHandler handler = this.getHandlerFromSession(session);
		if (handler != null && handler.user != null) {
			// 如果这个连接的用户已经登录了，需要将这个用户也删除
			this.userMap.remove(session.getId());
		}

		// 从在线列表中删除
		this.onlineMap.remove(session.getId());
	}

	@ManagedOperation(description = "根据 sessionId关闭session")
	@ManagedOperationParameters({ @ManagedOperationParameter(name = "sessionId", description = "sessionId") })
	public void closeSessionById(String sessionId) {
		if (StringUtils.hasText(sessionId)) {
			final ConnHandler handler = this.onlineMap.get(sessionId);
			if (handler != null) {
				try {
					handler.session.close();

					log.debug("关闭 websocket 成功，sessionId={}", sessionId);

				} catch (final IOException e) {
					log.warn("关闭 websocket session时发生错误, sessionId={}, errorMsg={}", sessionId, e.getMessage());
				}
			} else {
				log.debug("关闭 websocket 失败，找不到该连接，sessionId={}", sessionId);
			}
		}
	}

	@Override
	public List<WsConnectInfoBean> findConnectInfoBeanByFilter(IFilter<IConnHandlerInfo> filter) {
		final List<ConnHandler> connList = this.findConnHandleByFilter(filter);
		return ConverterUtil.convertList(connList, src -> src.toWsConnectInfoBean());
	}

	/** 根据过滤器寻找连接 */
	public List<ConnHandler> findConnHandleByFilter(IFilter<IConnHandlerInfo> filter) {
		final List<ConnHandler> list = new ArrayList<>(this.onlineMap.size());

		// 遍历所有的连接，不管是否有登陆
		final List<ConnHandler> all = new LinkedList<>(this.onlineMap.values());
		for (final ConnHandler handler : all) {
			if (filter == null || filter.isMatch(handler)) {
				// 如果过滤器为空或者命中，就加入到返回结果集中
				list.add(handler);
			}
		}
		// 按连接时间倒序排序
		Collections.sort(list, (o1, o2) -> {
			final long diff = o2.getConnectTime() - o1.getConnectTime();
			if (diff == 0) {
				return 0;
			} else if (diff > 0) {
				return 1;
			} else {
				return -1;
			}
		});
		return list;
	}

	/** 获取所有用户的链接信息 */
	public List<ConnHandler> findUserHandlers() {
		final List<ConnHandler> list = new LinkedList<>(this.userMap.values());
		return list;
	}

	/** 获取当前线程的连接的信息 */
	public String getCurClientIp() {
		final ConnHandler handler = this.getHandlerFromSession(getSessionFromThread());
		return handler.getIpAddress();
	}

	/** 获取当前线程的连接信息 */
	public ConnHandler getCurConnHandler() {
		final ConnHandler handler = this.getHandlerFromSession(getSessionFromThread());
		return handler;
	}

	@Override
	@ManagedAttribute(description = "连接总数")
	public int getTotalConnectCount() {
		return this.onlineMap.size();
	}

	@Override
	@ManagedAttribute(description = "用户总数")
	public int getTotalUserCount() {
		return this.userMap.size();
	}

	@Override
	public <T extends IWebUser> T getUser(Class<T> userClass) {
		Assert.notNull(userClass, "userClass is null");
		// 找到连接信息对象
		final ConnHandler handler = this.getHandlerFromSession(getSessionFromThread());
		// 返回用户信息
		return handler.getUser(userClass);
	}

	@Override
	public void onLoginSuccess(IWebUser user) {
		Assert.notNull(user, "user is null");
		Assert.hasText(user.getAccount(), "用户账号不能为空");

		// 找到连接信息对象
		final ConnHandler handler = this.getHandlerFromSession(getSessionFromThread());

		if (handler.user != null) {
			// 如果原来的已经登录了, 就从已登录账号中删除原来的账号，避免在线用户数错误
			this.userMap.remove(handler.getSessionId());
		}

		// 将用户信息放到handler中
		handler.user = user;

		// 将用户放到在线列表中
		this.userMap.put(handler.getSessionId(), handler);

		try {
			// 通知客户端，用户登录，该事件是api完成后再发送
			handler.sendServerEventAfterApiDone(new SimpleUserLoginEvent(user));
		} catch (final IOException e) {
			log.debug("发送用户登录事件到 连接(id={}) 时，发生了IO错误", handler.session.getId());
		}
	}

	@Override
	/** 登出时 */
	public void onLogout(Class<? extends IWebUser> userClass) {
		Assert.notNull(userClass, "userClass is null");
		// 找到连接信息对象
		final ConnHandler handler = this.getHandlerFromSession(getSessionFromThread());

		if (handler.user != null) {
			// 从在线用户列表中删除该用户
			this.userMap.remove(handler.getSessionId());

			// 将用户信息从handler删除
			handler.user = null;
		}

	}

	/** 移除 */
	public void removeSession() {
		this.sessions.remove();
	}

	@Override
	@ManagedOperation(description = "重置计数器")
	public void resetCounter() {
		this.couterService.getWsUpCounter().reset();
		this.couterService.getWsDownCounter().reset();

		final List<ConnHandler> list = new LinkedList<>(this.onlineMap.values());
		for (final ConnHandler handler : list) {
			handler.getDownCounter().reset();
			handler.getUpCounter().reset();
		}
	}

	/** 根据过滤器，向所有连接发送事件 */
	public void sendServerEvent(Object event, IFilter<IConnHandlerInfo> filter) {
		Assert.notNull(event, "事件不能为空");
		final List<ConnHandler> list = this.findConnHandleByFilter(filter);
		for (final ConnHandler handler : list) {
			try {
				handler.sendServerEventNow(event);
			} catch (final IOException e) {
				// 批量推送时，如果有一条出错，不抛错，但要记录到debug log中，可被查询
				log.debug("推送服务器事件 {} 到连接 sessionId={}, ip={} 时，发生了错误", event.getClass().getName(),
						handler.getSessionId(), handler.getIpAddress());
			}
		}
	}

	/** 从线程中获取session */
	private WebSocketSession getSessionFromThread() {
		return this.sessions.get();
	}

	/** 检查session中是否有用户信息对象，如果没有就添加 */
	protected ConnHandler getHandlerFromSession(WebSocketSession session) {
		Assert.notNull(session, "session不能为空");
		Assert.notNull(session.getId(), "sessionid不能为空");

		ConnHandler handler = (ConnHandler) session.getAttributes().get(HANDLER_SESSION_NAME);
		if (handler == null) {
			// 如果不存在，就新建
			handler = new ConnHandler(session);
			session.getAttributes().put(HANDLER_SESSION_NAME, handler);

			// 新建的同时，放到连接列表中
			this.onlineMap.put(session.getId(), handler);

			log.debug("发现新连接 id:{}", session.getId());
		}
		return handler;
	}

	/** 保存session到当前线程 */
	protected ConnHandler saveSessionToThread(WebSocketSession session) {
		this.sessions.set(session);
		return this.getHandlerFromSession(session);
	}

	@PostConstruct
	protected void init() {
		this.scheduledExecutorService.scheduleAtFixedRate(() -> {
			// 每十秒ping一下所有的连接，检查是否有断线
			this.pingAll();
		}, 0, 10, TimeUnit.SECONDS);
		
		/** 有counterService那边触发定时保存历史任务 */
		this.couterService.setWsApiServerJob(()->{
			this.onSaveHistroy();
		});
	}

	@ManagedOperation(description = "向所有连接发送ping包")
	public void pingAll() {
		final List<ConnHandler> list = this.findConnHandleByFilter(null);
		for (final ConnHandler handler : list) {
			handler.ping();
		}
	}

	@PreDestroy
	protected void shutdown() {
		BaseThreadPool.shutdownExecutorService("Ws连接维护定时任务", this.scheduledExecutorService);
	}

	@Override
	public void onSaveHistroy() {
		final List<ConnHandler> list = this.findUserHandlers();
		for (final ConnHandler handler : list) {
			handler.onSaveHistroy();
		}
	}

}
