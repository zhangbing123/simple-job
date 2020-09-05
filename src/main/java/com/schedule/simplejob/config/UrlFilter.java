package com.schedule.simplejob.config;

import lombok.extern.log4j.Log4j2;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * yun.shen
 * @author 
 *
 */
@Log4j2
@WebFilter(filterName = "urlFilter",urlPatterns = {"/*"})
public class UrlFilter implements Filter {


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// Do nothing because of X and Y.
		log.debug("UrlFilter init");
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request1 = (HttpServletRequest) request;

		HttpServletResponse response1 = (HttpServletResponse) response;
		String requestOrigin = request1.getHeader("Origin");
		/**
		 * 防止网页被frame标签劫持
		 */
		response1.addHeader("X-Frame-Options","SAMEORIGIN");
			response1.setHeader("Access-Control-Allow-Origin", requestOrigin);
			response1.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
			response1.setHeader("Access-Control-Allow-Credentials", "true");
			response1.setHeader("Access-Control-Max-Age", "3600");
			response1.setHeader("Access-Control-Allow-Headers", "Accept, Cache-Control,content-type,Content-Type, Content-Encoding, Content-Language,"
					+ " Content-Location, Content-disposition,login_cookie,shopCode,info"
					+ " ,Expires, Last-Modified, Set-Cookie, client-id, client-secret,platform-p");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

		// Do nothing because of X and Y.
	}

}
