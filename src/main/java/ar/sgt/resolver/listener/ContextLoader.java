/**
 *   ContextLoader
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
package ar.sgt.resolver.listener;

import java.io.File;

import javax.servlet.ServletContext;

import ar.sgt.resolver.config.ConfigParser;
import ar.sgt.resolver.config.ResolverConfig;

/**
 * @author gabriel
 * 
 */
public final class ContextLoader {

	public static final String RESOLVER_CONFIG = "RESOLVER_CONFIG";

	public static final String DEFAULT_WEB_CONF_PATH = "/WEB-INF/urlresolver.xml";

	private ResolverConfig resolverConfig;
	
	/**
	 * @param servletContext
	 * @throws Exception 
	 */
	public void initWebContext(ServletContext servletContext) {
		ConfigParser configParser = new ConfigParser();
		try {
			resolverConfig = configParser.parse(new File(DEFAULT_WEB_CONF_PATH));
		} catch (Exception e) {
			e.printStackTrace();
		}
		servletContext.setAttribute(RESOLVER_CONFIG, resolverConfig);
	}

	/**
	 * @param servletContext
	 */
	public void destroyWebContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
	}

}
