/**
 *   UrlResolver
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
 *   along with JIPDBS. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.config.Rule;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.processor.Processor;
import ar.sgt.resolver.processor.ResolverContext;

/**
 * @author gabriel
 *
 */
public class UrlResolver extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3919737317543290653L;

	private ResolverConfig resolverConfig;
	
	@Override
	public void init() throws ServletException {
		resolverConfig = (ResolverConfig) getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resolveProcessor(req, resp, ResolverContext.METHOD_GET);
	}

	private void resolveProcessor(HttpServletRequest req, HttpServletResponse resp, String method) {
		String path = req.getRequestURI();
		Rule rule = resolverConfig.findRule(path);
		if (rule != null) {
			ResolverContext context = new ResolverContext(req, resp, rule.parseParams(), method);
			Processor processor;
			try {
				processor = loadClass(rule.getProcessor());
				processor.doProcess(context);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resolveProcessor(req, resp, ResolverContext.METHOD_POST);
	}
	
	@SuppressWarnings("rawtypes")
	private Processor loadClass(String clasz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class impl = Class.forName(clasz);
		return (Processor) impl.newInstance();
	}
	
}
