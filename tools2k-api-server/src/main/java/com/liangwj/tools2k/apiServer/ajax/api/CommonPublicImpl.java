package com.liangwj.tools2k.apiServer.ajax.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.liangwj.tools2k.annotation.api.AApiServerImpl;
import com.liangwj.tools2k.apiServer.ajax.api.beans.CommonAdminUserInfoBean;
import com.liangwj.tools2k.apiServer.ajax.api.beans.CommonAdminUserLoginResponse;
import com.liangwj.tools2k.apiServer.beans.BoolResponse;
import com.liangwj.tools2k.apiServer.beans.CommonSuccessResponse;
import com.liangwj.tools2k.apiServer.beans.manager.ServerInfoBean;
import com.liangwj.tools2k.apiServer.beans.manager.ServerInfoResponse;
import com.liangwj.tools2k.apiServer.debugMode.DebugModeProperties;
import com.liangwj.tools2k.apiServer.loginCheck.ANeedCheckLogin;
import com.liangwj.tools2k.apiServer.security.CommonAdminWebUser;
import com.liangwj.tools2k.apiServer.security.CounterService;
import com.liangwj.tools2k.apiServer.security.IWebUserProvider;
import com.liangwj.tools2k.apiServer.security.LoginContext;
import com.liangwj.tools2k.apiServer.security.MyWebUser;
import com.liangwj.tools2k.apiServer.security.RememberMeUserHandler;
import com.liangwj.tools2k.apiServer.serviceInf.IDictService;
import com.liangwj.tools2k.apiServer.serviceInf.IWsApiServer;
import com.liangwj.tools2k.beans.exceptions.BaseApiException;
import com.liangwj.tools2k.beans.exceptions.InvalidPasswordException;
import com.liangwj.tools2k.beans.form.CreatePasswordForm;
import com.liangwj.tools2k.beans.form.LoginForm;
import com.liangwj.tools2k.utils.other.PasswordEncoder;
import com.liangwj.tools2k.utils.spring.WebContextHolderHelper;

/**
 * <pre>
 * ICommonAdminLogin的实现类
 * </pre>
 *
 * @author rock
 *
 */
@Service
@AApiServerImpl
public class CommonPublicImpl implements ICommonPublic {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(CommonPublicImpl.class);

	/** 是否加载了 Druid */
	public static boolean isHasDruid = false;

	@Autowired
	private RememberMeUserHandler rememberMeUserHandler;

	@Autowired
	private LoginContext loginContext;

	@Autowired
	private DebugModeProperties debugMode;

	@Autowired
	private RememberMeUserHandler adminUserHandler;

	@Autowired
	private CounterService counterService;

	@Autowired(required = false)
	private IDictService dictService;

	@Autowired(required = false)
	private IWsApiServer wsImpl;

	@Override
	public CommonAdminUserLoginResponse login(LoginForm form) throws InvalidPasswordException {
		Assert.notNull(form.getAccount(), "账号不能为空");
		Assert.notNull(form.getPassword(), "密码不能为空");

		log.debug("管理用户 {} 登录后台", form.getAccount());

		// 寻找用户认证供应者
		IWebUserProvider<CommonAdminWebUser> userProvider = this.rememberMeUserHandler
				.getUserProvider(CommonAdminWebUser.class);
		if (userProvider != null) {
			// 如果存在，就尝试获取用户
			CommonAdminWebUser user = userProvider.loadUserByAccount(form.getAccount());
			if (user != null) {
				// 如过能获取用户,就检查密码
				PasswordEncoder.checkPassword(form.getPassword(), user.getEncryptedPassword());

				// 如果密码正确，就返回正常信息
				this.loginContext.onLoginSuccess(user, form.isRememberMe());

				// 封装返回结果
				CommonAdminUserLoginResponse res = new CommonAdminUserLoginResponse();
				WebContextHolderHelper.getRequest().getSession().getId();
				res.setCurUser(new CommonAdminUserInfoBean(user));
				return res;
			}
		}

		throw new InvalidPasswordException();
	}

	/** 获取当前登录用户的信息 */
	@Override
	@ANeedCheckLogin(userClass = CommonAdminWebUser.class)
	public CommonAdminUserLoginResponse getCurUser() throws InvalidPasswordException {
		CommonAdminWebUser user = this.loginContext.getUser(CommonAdminWebUser.class);

		CommonAdminUserLoginResponse res = new CommonAdminUserLoginResponse();
		res.setCurUser(new CommonAdminUserInfoBean(user));
		return res;
	}

	@Override
	public CommonSuccessResponse logout() {
		this.loginContext.onLogout(CommonAdminWebUser.class);
		return CommonSuccessResponse.DEFAULT;
	}

	@Override
	public ServerInfoResponse getServerStatus() throws InvalidPasswordException {
		ServerInfoBean serverInfo = new ServerInfoBean();
		// 调试模式
		serverInfo.setDebugMode(this.debugMode.isDebugMode());

		// 是否有WebSocket Api接口
		serverInfo.setHasWsApiImpl(this.wsImpl != null);

		// 用户是否是在配置文件中定义
		serverInfo.setAdminInProp(adminUserHandler.isAdminInPorp());

		// 是否有druid监控
		serverInfo.setHasDruid(isHasDruid);

		// 是否有发包数据
		serverInfo.setHasSendPacketData(this.counterService.isHasSendPacketData());

		// 封装返回结果
		ServerInfoResponse res = new ServerInfoResponse();
		res.setServerInfo(serverInfo);

		// 框架用户信息
		CommonAdminWebUser user = this.loginContext.getUser(CommonAdminWebUser.class);
		if (user != null) {
			// 如果已经登录
			res.setCurUser(new CommonAdminUserInfoBean(user));
		}

		// 字典信息
		if (this.dictService != null) {
			res.setDict(this.dictService.toJsonMap());
		}

		return res;
	}

	@Override
	public CommonSuccessResponse passwordDemo(CreatePasswordForm form) {
		// 生成加密后的密码
		String encoded = PasswordEncoder.encodePassword(form.getPassword());

		CommonSuccessResponse res = new CommonSuccessResponse();
		res.setMessage(encoded);
		return res;
	}

	@Override
	public BoolResponse updOptTimeOut() throws BaseApiException {
		MyWebUser webUser = this.loginContext.getUser(MyWebUser.class);

		if (webUser != null) {
			if (webUser.overtime()) {
				this.loginContext.onLogout(MyWebUser.class);
			} else {
				webUser.updOptTime();
				return new BoolResponse(true);
			}

		}
		return new BoolResponse(false);
	}
}
