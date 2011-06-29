/**
 *   UrlReverse
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
package ar.sgt.resolver.utils;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.config.Rule;
import ar.sgt.resolver.exception.RuleNotFoundException;
import ar.sgt.resolver.listener.ContextLoader;

public final class UrlReverse {

	private ResolverConfig config;
	
	public UrlReverse(ResolverConfig config) {
		this.config = config;
	}
	
	public UrlReverse(ServletContext context) {
		this.config = (ResolverConfig) context.getAttribute(ContextLoader.RESOLVER_CONFIG);	
	}
	
	public String resolve(String name) throws RuleNotFoundException {
		return resolve(name, Collections.<String,String>emptyMap());
	}

	public String resolve(String name, Map<String, String> params) throws RuleNotFoundException {
		Rule rule = this.config.findByName(name);
		if (rule == null) throw new RuleNotFoundException("Unable to find a rule for name: " + name);
		return reverse(rule.getPattern(), params);
	}
	
	private String reverse(String url, Map<String, String> params) {
		if (params.size() > 0) {
			String finalUrl = RegexpHelper.normalize(url);
			for (Map.Entry<String, String> entry : params.entrySet()) {
				finalUrl = finalUrl.replace("$" + entry.getKey(),
						entry.getValue());
			}
			return finalUrl;
		} else {
			return RegexpHelper.normalize(url, false);
		}
	}
}
