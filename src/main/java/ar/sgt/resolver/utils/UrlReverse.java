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

import hurl.build.QueryBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.exception.ReverseException;
import ar.sgt.resolver.exception.RuleNotFoundException;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.rule.Rule;

import com.google.code.regexp.NamedPattern;

public final class UrlReverse {

	private ResolverConfig config;
	
	public UrlReverse(ResolverConfig config) {
		this.config = config;
	}
	
	public UrlReverse(ServletContext context) {
		this.config = (ResolverConfig) context.getAttribute(ContextLoader.RESOLVER_CONFIG);	
	}
	
	public String resolve(String name) throws RuleNotFoundException, ReverseException {
		return resolve(name, Collections.<String,String>emptyMap());
	}

	public String resolve(String name, Entry<String, String>[] params) throws RuleNotFoundException, ReverseException {
		Map<String, String> m = new HashMap<String, String>();
		for (Entry<String, String> e : params) {
			m.put(e.getKey(), e.getValue());
		}
		return resolve(name, m);
	}
	
	public String resolve(String name, Map<String, String> params) throws RuleNotFoundException, ReverseException {
		Rule rule = this.config.findByName(name);
		if (rule == null) throw new RuleNotFoundException("Unable to find a rule for name: " + name);
		return reverse(rule.getPattern(), params);
	}
	
	private String reverse(String url, Map<String, String> params) throws ReverseException {
		if (params.size() > 0) {
			NamedPattern pt = NamedPattern.compile(url);
			String finalUrl = RegexpHelper.normalize(url);
			// clone the params map, we don't want to modify the original map
			Map<String, String> tempParams = new HashMap<String, String>(params);
			if (pt.groupNames().size() > 0) {
				for (String key : pt.groupNames()) {
					String value = tempParams.remove(key);
					if (value == null) throw new ReverseException("Missing param " + key);
					try {
						finalUrl = finalUrl.replace("$" + key, URLEncoder.encode(value, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						throw new ReverseException(e);
					}
				}
			}
			if (tempParams.size() > 0) {
				// we still have unmatched params. add it as query
				QueryBuilder queryBuilder = QueryBuilder.create();
				for (Entry<String, String> entry : tempParams.entrySet()) {
					queryBuilder.addParam(entry.getKey(), entry.getValue());	
				}
				finalUrl = finalUrl + "?" + queryBuilder.toString();
			}
			return finalUrl;
		} else {
			return RegexpHelper.normalize(url, false);
		}
	}
}
