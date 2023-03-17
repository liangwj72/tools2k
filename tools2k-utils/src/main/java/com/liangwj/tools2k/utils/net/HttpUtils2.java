package com.liangwj.tools2k.utils.net;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import com.liangwj.tools2k.beans.others.ProxyInfo;
import com.liangwj.tools2k.utils.net.IHttpUtil.METHOD;

/**
 * 通过http协议抓起页面的工具类
 *
 * @see HttpParamBuilder
 */
public class HttpUtils2 {
	private final static HttpUtils2 instance = new HttpUtils2(15000, true, null);

	// private static final org.slf4j.Logger log =
	// org.slf4j.LoggerFactory.getLogger(HttpUtils2.class);

	public static final String PROXY_INFO_ATTR_NAME = "socks.address";

	public static HttpUtils2 createInstance(int timeout) {
		return new HttpUtils2(timeout, false, null);
	}

	/**
	 * 创建实例
	 * 
	 * @param timeout
	 *            超时时间（毫秒）
	 * @param trustInsecure
	 *            是否忽略非法证书
	 * @return
	 */
	public static HttpUtils2 createInstance(int timeout, boolean trustInsecure) {
		return new HttpUtils2(timeout, trustInsecure, null);
	}

	/**
	 * 创建实例
	 * 
	 * @param timeout
	 *            超时时间（毫秒）
	 * @param trustInsecure
	 *            是否忽略非法证书
	 * @param proxyInfo
	 *            sock5代理信息
	 * @return
	 */
	public static HttpUtils2 createInstance(int timeout, boolean trustInsecure, ProxyInfo proxyInfo) {
		return new HttpUtils2(timeout, trustInsecure, proxyInfo);
	}

	/** 默认4秒超时，不信任非法证书 */
	public static HttpUtils2 createInstance(ProxyInfo proxyInfo) {
		return new HttpUtils2(4000, false, proxyInfo);
	}

	/** 默认15秒超时，信任非法证书 */
	public static HttpUtils2 getInstance() {
		return instance;
	}

	/** 创建一个本地上下文信息 */
	private final HttpContext localContext = HttpClientContext.create();

	/** 创建一个本地Cookie存储的实例 */
	private final CookieStore cookieStore = new BasicCookieStore();

	private final ProxyInfo proxyInfo;

	/**
	 * HTTP request 的配置，主要是设置各类timeout
	 */
	private final RequestConfig requestConfig;

	private final Registry<ConnectionSocketFactory> socketFactoryRegistry;

	/**
	 * 禁止从外部new 实例
	 */
	private HttpUtils2(int timeout, boolean trustInsecure, ProxyInfo proxyInfo) {

		this.proxyInfo = proxyInfo;

		// 在本地上下文中绑定这个本地存储，用于每次请求
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

		// 初始化 timeout 时间
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(timeout)
				.setConnectTimeout(timeout)
				.setConnectionRequestTimeout(timeout).build();

		// http https 忽略证书https 的连接创建方案
		this.socketFactoryRegistry = HttpRegistryBuilder.createConnectionSocketFactory(trustInsecure);

	}

	/**
	 * 获得一个httpClient， 自动识别是否是https模式
	 *
	 * @param url
	 * @return
	 */
	private CloseableHttpClient createClient() {
		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(this.socketFactoryRegistry);
		CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).build();

		return client;
	}

	/**
	 * 执行http请求
	 *
	 * @param url
	 *            url
	 * @param paramMap
	 *            参数
	 * @param postMethod
	 *            是否post
	 * @param header
	 *            header
	 * @return
	 * @throws HttpUtilException
	 * @throws IOException
	 * @see HttpParamBuilder
	 */
	public String doExecute(IHttpUtil.METHOD methodType, String url, List<NameValuePair> paramList,
			Map<String, String> header) throws HttpUtilException, IOException {

		// 创建客户端
		CloseableHttpClient httpClient = this.createClient();

		if (proxyInfo != null) {
			// 添加代理信息
			ProxyAuthenticator.getInstance().addProxyInfo(proxyInfo);
			// 将代理信息放到context
			localContext.setAttribute(PROXY_INFO_ATTR_NAME, proxyInfo.getInetSocketAddress());
		} else {
			localContext.removeAttribute(PROXY_INFO_ATTR_NAME);
		}

		CloseableHttpResponse response = null;

		// 配置URI
		try {

			HttpRequestBase method;
			if (methodType == METHOD.GET) {

				URIBuilder builder = new URIBuilder(url);
				if (paramList != null) {
					builder.setParameters(paramList);
				}
				method = new HttpGet(builder.build());
			} else {
				HttpPost post = new HttpPost(url);
				if (paramList != null) {
					post.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
				}
				method = post;
			}

			if (header != null) {
				Set<Map.Entry<String, String>> set = header.entrySet();
				for (Entry<String, String> en : set) {
					method.addHeader(en.getKey(), en.getValue());
				}
			}

			// 设置 timeout参数
			method.setConfig(requestConfig);

			// 执行请求
			response = httpClient.execute(method, localContext);

			// 返回结果
			return getResult(response);

		} catch (URISyntaxException e) {
			throw new HttpUtilException(e);
		} catch (UnsupportedEncodingException e) {
			throw new HttpUtilException(e);
		} finally {
			// 关闭连接,释放资源
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		}
	}

	/**
	 * 最简单的执行一个 get 请求
	 * 
	 * @param proxyInfo
	 * @param url
	 * @return
	 * @throws HttpUtilException
	 * @throws IOException
	 */
	public String get(String url) throws HttpUtilException, IOException {
		return this.doExecute(METHOD.GET, url, null, null);
	}

	public String get(String url, List<NameValuePair> paramList,
			Map<String, String> header) throws HttpUtilException, IOException {
		return this.doExecute(METHOD.GET, url, paramList, header);
	}

	/**
	 * 获取结果
	 *
	 * @param response
	 *            响应
	 * @return 响应结果
	 * @throws IOException
	 * @throws HttpUtilException
	 */
	private String getResult(HttpResponse response) throws HttpUtilException, IOException {
		int statusCode = response.getStatusLine().getStatusCode();

		String content;
		try {
			HttpEntity httpEntity = response.getEntity();
			content = EntityUtils.toString(httpEntity, "UTF-8");
		} catch (ParseException e) {
			throw new HttpUtilException(e);
		}

		if (statusCode < 400) {
			return content;
		} else {
			throw new HttpUtilException(response.getStatusLine(), content);
		}
	}

	public String post(String url, List<NameValuePair> paramList,
			Map<String, String> header) throws HttpUtilException, IOException {
		return this.doExecute(METHOD.POST, url, paramList, header);
	}

	/**
	 * 通过httppost传输文件
	 *
	 * @param url
	 *            推送url
	 * @param fieldName
	 *            文件参数名
	 * @param file
	 *            文件
	 * @param paramMap
	 *            其他参数对
	 * @return 服务器返回的内容
	 * @throws IOException
	 * @throws HttpUtilException
	 */

	public String postFile(String url, String fieldName, File file, List<NameValuePair> paramList,
			Map<String, String> header) throws IOException, HttpUtilException {

		// 创建客户端
		CloseableHttpClient httpClient = this.createClient();

		if (proxyInfo != null) {
			// 添加代理信息
			ProxyAuthenticator.getInstance().addProxyInfo(proxyInfo);
			// 将代理信息放到context
			localContext.setAttribute(PROXY_INFO_ATTR_NAME, proxyInfo.getInetSocketAddress());
		}

		CloseableHttpResponse response = null;

		// 配置URI
		try {

			HttpPost method = new HttpPost(url);
			// 增加文件
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.addBinaryBody(fieldName, file, ContentType.DEFAULT_BINARY, file.getName());

			if (paramList != null) {
				for (NameValuePair pair : paramList) {
					builder.addTextBody(pair.getName(), pair.getValue(), ContentType.DEFAULT_BINARY);
				}
			}

			// 设置参数
			method.setEntity(builder.build());

			if (header != null) {
				// 设置header
				Set<Map.Entry<String, String>> set = header.entrySet();
				for (Entry<String, String> en : set) {
					method.addHeader(en.getKey(), en.getValue());
				}
			}

			// 设置 timeout参数
			method.setConfig(requestConfig);

			// 执行请求
			response = httpClient.execute(method, localContext);

			// 返回结果
			return getResult(response);

		} finally {
			// 关闭连接,释放资源
			if (response != null) {
				response.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		}
	}

	/** 生成参数构造器 */
	public static HttpParamBuilder createParamBuilder() {
		return HttpParamBuilder.create();
	}
}
