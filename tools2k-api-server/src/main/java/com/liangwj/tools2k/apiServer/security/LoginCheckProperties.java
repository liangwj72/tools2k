package com.liangwj.tools2k.apiServer.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.util.StringUtils;

import com.liangwj.tools2k.annotation.api.ADomainOrder;
import com.liangwj.tools2k.apiServer.loginCheck.IWebUser;
import com.liangwj.tools2k.utils.other.PasswordEncoder;
import com.liangwj.tools2k.utils.spring.CommonMBeanDomainNaming;

/**
 * <pre>
 * Debug模式 配置
 * 
 * 默认参数
 * loginCheck.rememberMe.cookieName = remember-me
 * loginCheck.rememberMe.paramName = remember-me 
 * loginCheck.rememberMe.cookieAge = 7*24*60*60 秒，就是7天了 
 * loginCheck.rememberMe.key = 梁韦江是个好人
 * loginCheck.rememberMe.allwaysRememmberMe = false
 * 
 * loginCheck.admin.account = admin
 * loginCheck.admin.password = guotu123
 * </pre>
 * 
 * @author rock 2016年8月26日
 */
@ManagedResource(description = "LoginCheck 配置")
@ConfigurationProperties(prefix = "login-check")
@ADomainOrder(order = CommonMBeanDomainNaming.ORDER, domainName = CommonMBeanDomainNaming.DOMAIN)
public class LoginCheckProperties {

	/**
	 * <pre>
	 * 默认的管理用户账号
	 * </pre>
	 * 
	 * @author rock 2016年11月16日
	 */
	public static class Admin implements IWebUser {
		private static final long serialVersionUID = 1L;

		private String account = "admin";

		/** 默认密码是 guotu123 **/
		private String password = "guotu123";

		@Override
		public String getAccount() {
			return account;
		}

		public String getPassword() {
			return password;
		}

		public boolean isValid() {
			return StringUtils.hasText(account) && StringUtils.hasText(password);
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		@Override
		public boolean checkRights(String optId) {
			return false;
		}

		@Override
		public String getName() {
			return account;
		}

		@Override
		public String getEncryptedPassword() {
			return PasswordEncoder.encodePassword(password);
		}

	}

	/**
	 * <pre>
	 * 默认的 用户数据供应者，用于记录用户信息到cookie
	 * </pre>
	 * 
	 * @author rock 2016年11月16日
	 */
	public class AdminUserProvider implements IWebUserProvider<CommonAdminWebUser> {

		@Override
		public Class<CommonAdminWebUser> getSupportUserClassNames() {
			return CommonAdminWebUser.class;
		}

		@Override
		public CommonAdminWebUser loadUserByAccount(String username) {
			final Admin admin = LoginCheckProperties.this.admin;
			if (admin.isValid()) {
				return new CommonAdminWebUser(admin);
			} else {
				return null;
			}
		}
	}

	public static class RememberMe {

		/** 是否总是 记录登录状态 */
		private boolean allwaysRememmberMe = false;

		/** cookie 有效期，单位：秒 */
		private int cookieAge = 7 * 24 * 60 * 60;

		/** cookie 名字 前缀 */
		private String cookieName = "remember_me";

		/** 生成签名时，附加的key */
		private String key = "Rock是个好人";

		/** remember-me 的参数名 */
		private String paramName = "remember_me";

		public int getCookieAge() {
			return cookieAge;
		}

		public String getCookieName() {
			return cookieName;
		}

		/**
		 * 根据用户对象的类型生成cookie name
		 * 
		 * @param clazz
		 * @return
		 */
		public String getCookieName(Class<? extends IWebUser> clazz) {
			return cookieName + "_" + clazz.getSimpleName();
		}

		public String getKey() {
			return key;
		}

		public String getParamName() {
			return paramName;
		}

		public boolean isAllwaysRememmberMe() {
			return allwaysRememmberMe;
		}

		public void setAllwaysRememmberMe(boolean allwaysRememmberMe) {
			this.allwaysRememmberMe = allwaysRememmberMe;
		}

		public void setCookieAge(int cookieAge) {
			this.cookieAge = cookieAge;
		}

		public void setCookieName(String cookieName) {
			this.cookieName = cookieName;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public void setParamName(String paramName) {
			this.paramName = paramName;
		}
	}

	private final Admin admin = new Admin();
	private final AdminUserProvider adminUserProvider = new AdminUserProvider();

	/**
	 * 登陆页的url,用于权限校验不通过的时候，重定向到登录页
	 */
	private String loginUrl = "/login";

	private RememberMe rememberMe = new RememberMe();

	public Admin getAdmin() {
		return admin;
	}

	public AdminUserProvider getAdminUserProvider() {
		return adminUserProvider;
	}

	@ManagedAttribute(description = "登录url")
	public String getLoginUrl() {
		return loginUrl;
	}

	public RememberMe getRememberMe() {
		return rememberMe;
	}

	@ManagedAttribute(description = "cookie的有效期，秒")
	public int getRememberMeCookieAge() {
		return this.rememberMe.getCookieAge();
	}

	@ManagedAttribute(description = "Cookie名字")
	public String getRememberMeCookieName() {
		return this.rememberMe.getCookieName();
	}

	@ManagedAttribute(description = "签名需要的key")
	public String getRememberMeKey() {
		return this.rememberMe.getKey();
	}

	@ManagedAttribute(description = "引发Remenber的参数名")
	public String getRememberMeParamName() {
		return this.rememberMe.getParamName();
	}

	@ManagedAttribute(description = "是否总是记录登录状态")
	public boolean isAllwaysRememmberMe() {
		return this.rememberMe.isAllwaysRememmberMe();
	}

	@ManagedAttribute
	public void setAllwaysRememmberMe(boolean allwaysRememmberMe) {
		this.rememberMe.setAllwaysRememmberMe(allwaysRememmberMe);
	}

	@ManagedAttribute
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public void setRememberMe(RememberMe rememberMe) {
		this.rememberMe = rememberMe;
	}

	@ManagedAttribute
	public void setRememberMeCookieAge(int cookieAge) {
		this.rememberMe.setCookieAge(cookieAge);
	}

	@ManagedAttribute
	public void setRememberMeCookieName(String cookieName) {
		this.rememberMe.setCookieName(cookieName);
	}

	@ManagedAttribute
	public void setRememberMeKey(String key) {
		this.rememberMe.setKey(key);
	}

	@ManagedAttribute
	public void setRememberMeParamName(String paramName) {
		this.rememberMe.setParamName(paramName);
	}
}
