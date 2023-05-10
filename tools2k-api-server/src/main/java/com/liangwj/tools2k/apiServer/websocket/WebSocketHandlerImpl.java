package com.liangwj.tools2k.apiServer.websocket;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSON;
import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.apiServer.beans.BaseSocketResponse;
import com.liangwj.tools2k.apiServer.security.CounterService;
import com.liangwj.tools2k.apiServer.utils.ApiMethodInfo;
import com.liangwj.tools2k.apiServer.websocket.WsLoginContext.ConnHandler;
import com.liangwj.tools2k.apiServer.websocket.beans.BaseResponsePackage;
import com.liangwj.tools2k.apiServer.websocket.beans.CmdBean;
import com.liangwj.tools2k.apiServer.websocket.beans.ErrorResponsePackage;
import com.liangwj.tools2k.apiServer.websocket.beans.SuccessResponsePackage;
import com.liangwj.tools2k.beans.exceptions.ApiNotFoundException;
import com.liangwj.tools2k.utils.other.LogUtil;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;
import com.liangwj.tools2k.utils.threadPool.IMyTask;

/**
 * <pre>
 * WebSocketHandler 实现类， 用于处理web socket事件
 * </pre>
 *
 * @author 梁韦江
 */
@Service
@ManagedResource(description = "Websocket处理器")
@ADomainOrder(domainName = CommonMBeanDomainNaming.DOMAIN_API_SERVER, order = CommonMBeanDomainNaming.ORDER)
public class WebSocketHandlerImpl implements WebSocketHandler {

	@Autowired
	private WsApiMapContainer clientApiContainer;

	@Autowired
	private WsLoginContext loginContext;

	@Autowired(required = false)
	private IWsExtService extSecurityService;

	@Autowired
	private WsThreadPool threadPool;

	@Autowired
	private CounterService couterService;

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(WebSocketHandlerImpl.class);

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.debug("建立了连接 afterConnectionEstablished:id={}", session.getId());
		this.loginContext.saveSessionToThread(session);
	}

	/** 执行任务 */
	private void execTask(WebSocketSession session, WebSocketMessage<?> message) {
		long startNano = System.nanoTime();

		// 执行其他逻辑前，保存一下session到线程变量中
		ConnHandler handler = this.loginContext.saveSessionToThread(session);
		Object param = null;
		CmdBean cmd = null;

		try {
			cmd = this.getCmdBean(message);// 格式转化

			if (cmd != null) {

				ApiMethodInfo<BaseSocketResponse> methodInfo = this.clientApiContainer.findApiMethod(cmd.getUrl());

				BaseResponsePackage res = null;
				try {
					if (methodInfo == null) {
						// 找不到api就直接抛错
						throw new ApiNotFoundException(cmd.getUrl());
					}

					if (methodInfo.hasParam()) {
						// 如果该接口有参数，就绑定参数
						param = cmd.parserForm(methodInfo.getFormClass());
						if (param == null) {
							log.warn("准备调用接口: {}, 但客户端没有传递参数过来。原始数据:{}", cmd.getUrl(), cmd.getOriginStr());
							return;
						}
					}

					// 检查权限
					methodInfo.checkRights(loginContext);

					if (this.extSecurityService != null) {
						// 如果需要额外执行安全检查，就执行
						this.extSecurityService.onBeforeInvokeApi(handler, methodInfo);
					}

					// 执行接口方法
					BaseSocketResponse value = methodInfo.invoke(param);

					// 构造对正常返回结果的封装
					if (value != null) {
						res = new SuccessResponsePackage(cmd, value);
					}

				} catch (Throwable exParam) {

					Throwable e = exParam;
					if (e instanceof InvocationTargetException) {
						// 如果类型是 InvocationTargetException， 里面的类型才是真正的错误
						e = ((InvocationTargetException) exParam).getTargetException();
					}

					// 如果出错了，就构造出错时的返回
					res = new ErrorResponsePackage(cmd, e);

					if (log.isDebugEnabled()) {
						LogUtil.traceError(log, e, "调用接口时出现错误");
					}
				}

				// 发送结果
				if (res != null) {
					handler.sendMessage(res);
				}

				// 暂时关闭这里的log

			}
		} catch (Throwable e) {
			LogUtil.traceError(log, e, "调用接口时出现内部错误");
		}

		// api 调用完成后，发送要延迟发送的服务器事件
		handler.afterApiDone();

		// 执行完成后，将session从线程变量中删除
		this.loginContext.removeSession();

		long useTimeNano = System.nanoTime() - startNano; // 纳秒时间
		long useTimeMs = TimeUnit.NANOSECONDS.toMillis(useTimeNano); // 毫秒时间

		if (log.isDebugEnabled()) {
			if (cmd != null) {
				String url = cmd.getUrl();
				String paramStr = null;
				if (param != null && message.getPayloadLength() < 1024) {
					// 如果上传的内容超过1K，就不输出在debug log中
					paramStr = JSON.toJSONString(param, true);
				}

				log.debug("调用接口: {} 时间消耗 :{}ms \n绑定表单的结果:{}", url, useTimeMs, paramStr);
			}
		}

		// 统计访问次数
		int payLoadLen = message.getPayloadLength();
		handler.incrementRequest(payLoadLen, useTimeNano);

		this.couterService.getWsUpCounter().addPayload(payLoadLen, useTimeNano);
	}

	@Override
	public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
		if (!(message instanceof TextMessage)) {
			log.warn("收到了非字符型的信息，类型为{}, 该类型忽略", message.getClass().getSimpleName());
			return;
		}

		this.threadPool.addNewTask(new WebSocketTask(session, message));
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.warn("传输时发生了错误 handleTransportError, id={}", session.getId());
		session.close();
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

		log.debug("断线时 afterConnectionClosed id={}", session.getId());

		if (this.extSecurityService != null) {
			// 如果有实现了额外服务，就调用额外服务的 afterConnectionClosed 方法
			ConnHandler handler = this.loginContext.getHandlerFromSession(session);
			this.extSecurityService.afterConnectionClosed(handler);
		}

		this.loginContext.afterConnectionClosed(session);
	}

	@Override
	public boolean supportsPartialMessages() {
		return false;
	}

	protected class WebSocketTask implements IMyTask {

		private final WebSocketSession session;

		private final WebSocketMessage<?> message;

		public WebSocketTask(WebSocketSession session, WebSocketMessage<?> message) {
			this.session = session;
			this.message = message;
		}

		@Override
		public void run() {
			WebSocketHandlerImpl.this.execTask(this.session, this.message);
		}

		@Override
		public String getUniqueId() {
			return null;
		}

	}

	private CmdBean getCmdBean(WebSocketMessage<?> message) {

		String jsonStr = ((TextMessage) message).getPayload();

		CmdBean cmd = null;

		try {
			cmd = JSON.parseObject(jsonStr, CmdBean.class);
		} catch (Throwable e) {
		}

		if (cmd == null) {
			log.debug("无法解析收到的内容:{}", jsonStr);
			return null;
		}

		if (!StringUtils.hasText(cmd.getUrl())) {
			log.debug("收到命令非法 {}", jsonStr);
			return null;
		}

		cmd.setOriginStr(jsonStr);

		return cmd;
	}

	@ManagedOperation(description = "重置计数器")
	public void reset() {
		this.couterService.getWsUpCounter().reset();
	}

	@ManagedAttribute(description = "总个数")
	public long getRequestCount() {
		return this.couterService.getWsUpCounter().getCount();
	}

	@ManagedAttribute(description = "平均耗时(微秒)")
	public long getTimeAllAvg() {
		return this.couterService.getWsUpCounter().getTimeAvg() / 1000;
	}

	@ManagedAttribute(description = "平均流量")
	public long getTimeBeforeInvokeAvg() {
		return this.couterService.getWsUpCounter().getPayloadAvg();
	}

}
