package com.liangwj.tools2k.apiServer.security;

import java.util.Collections;
import java.util.List;

import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HttpSessionIdResolver;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <pre>
 * ajax跨域调用api时，没有有session，所以我们需要能够从header中获取sessionId
 *
 * 这个类就是为了配合 Spring Session项目，可以先在header中查询sessionId，如果没有，再去cookie中查
 * </pre>
 *
 * @author rock 2020年10月23日
 *
 */
public class HeaderAndCookieHttpSessionIdResolver implements HttpSessionIdResolver {
	public static final String HEADER_NAME = "x-auth-token";

	public static final String ID_ATTR_NAME = HeaderAndCookieHttpSessionIdResolver.class.getName() + ".sessionId";

	private static final org.slf4j.Logger log = org.slf4j.LoggerFactory
			.getLogger(HeaderAndCookieHttpSessionIdResolver.class);

	private final MyCookieSerializer cookieSerializer = new MyCookieSerializer();

	class MyCookieSerializer extends DefaultCookieSerializer {

		/** 因为 CookieValue 是内部类，只好继承一下DefaultCookieSerializer，然后再用 */
		public void writeSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {
			this.writeCookieValue(new CookieValue(request, response, sessionId));
		}
	}

	private String findSessionIdFromRequest(HttpServletRequest request) {
		// 尝试header
		String sessionId = request.getHeader(HEADER_NAME);
		if (StringUtils.hasText(sessionId)) {
			log.debug("在Headere中找到 SessionId:{}", sessionId);
			return sessionId;
		}

		// 尝试cookie
		sessionId = this.getSessionIdFromCookie(request);
		if (StringUtils.hasText(sessionId)) {
			log.debug("在Cookie中找到 SessionId:{}", sessionId);
			return sessionId;
		}

		return null;
	}

	private String getSessionIdFromCookie(HttpServletRequest request) {

		final List<String> cookieValues = this.cookieSerializer.readCookieValues(request);
		return cookieValues.isEmpty() ? "" : cookieValues.get(0);
	}

	@Override
	public List<String> resolveSessionIds(HttpServletRequest request) {
		// 先在Request的属性中找
		String sessionId = (String) request.getAttribute(ID_ATTR_NAME);

		if (!StringUtils.hasText(sessionId)) {
			// 如果找不到，就从header中找
			sessionId = this.findSessionIdFromRequest(request);

			if (StringUtils.hasText(sessionId)) {
				// 如果在header或者cookie中找到了，就保存到request中
				log.debug("找到 sessionId ，保存到request.attribute中");
				request.setAttribute(ID_ATTR_NAME, sessionId);
			}
		}

		return (sessionId != null) ? Collections.singletonList(sessionId) : Collections.emptyList();
	}

	@Override
	public void setSessionId(HttpServletRequest request, HttpServletResponse response, String sessionId) {

		log.debug("onNewSession时，保存SessionId:{} 到 header和 cookie", sessionId);

		response.setHeader("Access-Control-Expose-Headers", HEADER_NAME);
		response.setHeader(HEADER_NAME, sessionId);

		this.cookieSerializer.writeSessionId(request, response, sessionId);

	}

	@Override
	public void expireSession(HttpServletRequest request, HttpServletResponse response) {
		log.debug("onInvalidateSession时，删除SessionId");

		response.setHeader(HEADER_NAME, "");
		this.cookieSerializer.writeSessionId(request, response, "");

	}

}
