/**
 *   ResolverFilter
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
package ar.sgt.resolver.filter;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.config.Rule;
import ar.sgt.resolver.exception.HttpError;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.processor.Processor;
import ar.sgt.resolver.processor.ProcessorContext;
import ar.sgt.resolver.processor.ResolverContext;

public class ResolverFilter implements Filter {

	private static final Logger log = Logger.getLogger(ResolverFilter.class.getName());
	
	private FilterConfig filterConfig;
	private ResolverConfig resolverConfig;
	private boolean appendBackSlash;	
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.fine("Initializing servlet");
		this.resolverConfig = (ResolverConfig) filterConfig.getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
		this.appendBackSlash = filterConfig.getInitParameter("append_backslash") != null ? Boolean.parseBoolean(filterConfig.getInitParameter("append_backslash")) : true;
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		String path = req.getRequestURI();
		if (appendBackSlash) {
			if (!path.endsWith("/")) path = path + "/";
		}
		log.fine("Resolve path: " + path);
		Rule rule = resolverConfig.findRule(path);
		if (rule != null) {
			log.fine("Found rule: " + rule.getProcessor());
			ResolverContext context = new ResolverContext(filterConfig.getServletContext(), req, resp, rule.parseParams(), req.getMethod());
			ProcessorContext processorContext = new ProcessorContext(rule.getRedirect());
			Processor processor;
			try {
				processor = loadClass(rule.getProcessor());
				processor.process(processorContext, context);
			} catch (HttpError e) {
				log.fine("Handling HTTP ERROR");
				resp.sendError(e.getHttpErrorCode());
			} catch (Exception e) {
				log.severe(e.getMessage());
				throw new ServletException(e);
			}
		} else {
			log.fine("No matching rule found");
			chain.doFilter(request, response);
		}
	}

	@SuppressWarnings("rawtypes")
	private Processor loadClass(String clasz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class impl = Class.forName(clasz);
		return (Processor) impl.newInstance();
	}
	
	@Override
	public void destroy() {}

}
