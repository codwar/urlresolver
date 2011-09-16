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
 *   along with UrlResolver. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.listener;


import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.config.ConfigParser;
import ar.sgt.resolver.config.ResolverConfig;

/**
 * @author gabriel
 * 
 */
public final class ContextLoader {

	private static final Logger log = LoggerFactory.getLogger(ContextLoader.class);
	
	public static final String RESOLVER_CONFIG = "RESOLVER_CONFIG";
	public static final String APPEND_BACKSLASH = "APPEND_BACKSLASH";
	public static final String CONFIG_LOCATION_PARAM = "resolverConfigLocation";
	public static final String DEFAULT_WEB_CONF_PATH = "/WEB-INF/urlresolver.xml";

	private ResolverConfig resolverConfig;
	
	/**
	 * @param servletContext
	 * @throws Exception 
	 */
	public void initWebContext(ServletContext servletContext) {
		String configFile = servletContext.getInitParameter(CONFIG_LOCATION_PARAM) != null ? servletContext.getInitParameter(CONFIG_LOCATION_PARAM) : DEFAULT_WEB_CONF_PATH;
		log.debug("Loading config from {}", configFile);
		ConfigParser configParser = new ConfigParser();
		try {
			resolverConfig = configParser.parse(servletContext.getResourceAsStream(configFile));
			log.debug("Config loaded");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		servletContext.setAttribute(RESOLVER_CONFIG, resolverConfig);
		servletContext.setAttribute(APPEND_BACKSLASH, servletContext.getInitParameter(APPEND_BACKSLASH) != null ? Boolean.parseBoolean(servletContext.getInitParameter(APPEND_BACKSLASH)) : true);
	}

	/**
	 * @param servletContext
	 */
	public void destroyWebContext(ServletContext servletContext) {
		log.debug("Web context destroyed");
	}

}
