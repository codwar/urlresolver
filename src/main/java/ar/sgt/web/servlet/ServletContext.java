/**
 *   JIPDBS
 *   Copyright(c) 2011 Martin Schonaker
 *   Copyright(c) 2011 Sergio Gabriel Teves
 * 
 *   This file is part of JIPDBS.
 *
 *   JIPDBS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   JIPDBS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with JIPDBS. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.web.servlet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.sgt.web.listener.Context;

/**
 * @author gabriel
 *
 */
public final class ServletContext implements Context {
	
	private Map<String, String> parameters;
	private HttpServletRequest req;
	private HttpServletResponse resp;
	private String method;

	public ServletContext(HttpServletRequest req, HttpServletResponse resp, String method) {
		this(req, resp, new HashMap<String, String>(), method);
	}
	
	public ServletContext(HttpServletRequest req, HttpServletResponse resp, Map<String, String> params, String method) {
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
	
}
