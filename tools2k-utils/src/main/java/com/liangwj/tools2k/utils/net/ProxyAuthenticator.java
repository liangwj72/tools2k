package com.liangwj.tools2k.utils.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.Assert;

import com.liangwj.tools2k.beans.others.ProxyInfo;

/**
 * 根据代理服务器的信息，获取账号和密码
 * 
 */
public class ProxyAuthenticator extends Authenticator {

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ProxyAuthenticator.class);
	private static ProxyAuthenticator instance = new ProxyAuthenticator();

	static {
		Authenticator.setDefault(instance);
	}

	public static ProxyAuthenticator getInstance() {
		return instance;
	}

	private ProxyAuthenticator() {
	}

	/** 保存所有的账号和密码，key是proxy Ip+proxy port */
	private final Map<String, ProxyInfo> passwordMap = new LinkedHashMap<>(10000);

	/** 添加代理信息 */
	public void addProxyInfo(ProxyInfo info) {
		Assert.notNull(info, "into must not be null");
		if (info.isHasPassword()) {
			String key = info.getKey();
			this.passwordMap.put(key, info);

			log.debug("添加socks5代理服务器 {} 的账号密码: {}", info.getKey(), info.getPasswordAuthentication().getUserName());
		}
	}

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		String key = ProxyInfo.calcPasswordKey(this.getRequestingHost(), this.getRequestingPort());
		ProxyInfo info = this.passwordMap.get(key);
		if (info != null) {
			return info.getPasswordAuthentication();
		} else {
			return null;
		}
	}
}
