package com.liangwj.tools2k.utils.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

public class HttpRegistryBuilder {
	private static HttpRegistryBuilder instance = new HttpRegistryBuilder();

	/**
	 * 用 sock5 的http socket工厂
	 *
	 */
	private class MySocks5PlainConnectionSocketFactory extends PlainConnectionSocketFactory {

		@Override
		public Socket createSocket(final HttpContext context) throws IOException {
			return createSocket5Socket(context);
		}
	}

	/**
	 * 用 sock5 的https socket工厂
	 *
	 */
	private class MySocks5ConnectionSocketFactory extends SSLConnectionSocketFactory {

		public MySocks5ConnectionSocketFactory(final SSLContext sslContext, HostnameVerifier hostnameVerifier) {
			super(sslContext, hostnameVerifier);
		}

		public MySocks5ConnectionSocketFactory(final SSLContext sslContext) {
			super(sslContext);
		}

		@Override
		public Socket createSocket(final HttpContext context) throws IOException {
			return createSocket5Socket(context);
		}
	}

	/**
	 * 从 context中获取代理信息，并创建socket
	 *
	 */
	private Socket createSocket5Socket(final HttpContext context) throws IOException {
		InetSocketAddress socksaddr = (InetSocketAddress) context.getAttribute(HttpUtils2.PROXY_INFO_ATTR_NAME);
		if (socksaddr != null) {
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksaddr);
			return new Socket(proxy);
		} else {
			return new Socket();
		}
	}

	/** 普通http的连接工厂 */
	private final ConnectionSocketFactory csfHttp;

	/** 常规的https连接工厂 */
	private final ConnectionSocketFactory csfHttps;

	/** 信任所有的https连接工厂 */
	private final ConnectionSocketFactory csfHttpsTrustAll;

	private HttpRegistryBuilder() {
		csfHttp = new MySocks5PlainConnectionSocketFactory();
		csfHttps = new MySocks5ConnectionSocketFactory(SSLContexts.createSystemDefault());
		this.csfHttpsTrustAll = createTrustAllConnectionSocketFactory();
	}

	private ConnectionSocketFactory createTrustAllConnectionSocketFactory() {

		SSLContext sslContext;

		try {
			sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					// 信任所有
					return true;
				}
			}).build();
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			sslContext= SSLContexts.createSystemDefault();
		}

		//NoopHostnameVerifier类:  作为主机名验证工具，实质上关闭了主机名验证，它接受任何
        //有效的SSL会话并匹配到目标主机。
        HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
		return new MySocks5ConnectionSocketFactory(sslContext, hostnameVerifier);
	}

	/** 获取连接创建方案 */
	public static Registry<ConnectionSocketFactory> createConnectionSocketFactory(boolean trustInsecure) {
		return instance._createConnectionSocketFactory(trustInsecure);
	}

	private Registry<ConnectionSocketFactory> _createConnectionSocketFactory(boolean trustInsecure) {
		// 设置代理配置
		return RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", this.csfHttp)
				.register("https", trustInsecure ? this.csfHttpsTrustAll : this.csfHttps)
				.build();
	}
}
