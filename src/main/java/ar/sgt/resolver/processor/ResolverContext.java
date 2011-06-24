/**
 *   ResolverContext
 *   Copyright(c) 2011 Sergio Gabriel Teves
 * 
 *   This file is part of UrlResolver.
 *
 *   UrlResolver is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   UrlResolver is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with UrlResolver. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.processor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.sgt.resolver.listener.Context;

/**
 * @author gabriel
 *
 */
public final class ResolverContext implements Context {
	
	private Map<String, String> parameters;
	private ServletContext servletContext;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private String method;

	public ResolverContext(ServletContext servletContext, HttpServletRequest req, HttpServletResponse resp, String method) {
		this(servletContext, req, resp, new HashMap<String, String>(), method);
	}
	
	public ResolverContext(ServletContext servletContext, HttpServletRequest req, HttpServletResponse resp, Map<String, String> params, String method) {
		this.servletContext = servletContext;
		this.parameters = params;
		this.req = req;
		this.resp = resp;
		this.method = method;
	}
	
	public HttpServletRequest getRequest() {
		return this.req;
	}
	
	public HttpServletResponse getResponse() {
		return this.resp;
	}
	
	public String getParameter(String name) {
		return this.parameters.get(name);
	}

	public boolean isPost() {
		return METHOD_POST.equals(this.method);
	}
	
	public boolean isGet() {
		return METHOD_GET.equals(this.method);
	}

	public ServletContext getServletContext() {
		return servletContext;
	}
	
}
