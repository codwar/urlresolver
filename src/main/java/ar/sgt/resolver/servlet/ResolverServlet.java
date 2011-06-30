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
 *   along with UrlResolver. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.servlet;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.processor.ResponseProcessor;
import ar.sgt.resolver.processor.ResolverContext;
import ar.sgt.resolver.rule.Rule;

/**
 * @author gabriel
 *
 */
public class ResolverServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3919737317543290653L;

	private static final Logger log = Logger.getLogger(ResolverServlet.class.getName());
	
	private ResolverConfig resolverConfig;
	private boolean appendBackSlash;
	private boolean debug;
	
	@Override
	public void init() throws ServletException {
		log.fine("Initializing servlet");
		resolverConfig = (ResolverConfig) getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
		appendBackSlash = getServletConfig().getInitParameter("append_backslash") != null ? Boolean.parseBoolean(getServletConfig().getInitParameter("append_backslash")) : true;
		debug = getServletConfig().getInitParameter("debug") != null ? Boolean.parseBoolean(getServletConfig().getInitParameter("debug")) : false;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		resolveProcessor(req, resp, ResolverContext.METHOD_GET);
	}

	private void resolveProcessor(HttpServletRequest req, HttpServletResponse resp, String method) throws ServletException {
		String path = req.getRequestURI();
		if (appendBackSlash) {
			if (!path.endsWith("/")) path = path + "/";
		}
		log.fine("Resolve path: " + path);
		Rule rule = resolverConfig.findRule(path);
		if (rule != null) {
			log.fine("Found rule: " + rule.getProcessor());
			ResolverContext context = new ResolverContext(getServletContext(), req, resp, rule.parseParams(), method);
			ResponseProcessor processor;
			try {
				processor = loadClass(rule.getProcessor());
				processor.doProcess(context);
			} catch (Exception e) {
				log.severe(e.getMessage());
				throw new ServletException(e);
			}
		} else {
			log.fine("No matching rule found");
			if (debug) {
				throw new ServletException("No matching rule found for url " + path + "\n" + resolverConfig.toString());
			} else {
				try {
					resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				} catch (IOException e) {
					throw new ServletException(e);
				}
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
	private ResponseProcessor loadClass(String clasz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class impl = Class.forName(clasz);
		return (ResponseProcessor) impl.newInstance();
	}
	
}
