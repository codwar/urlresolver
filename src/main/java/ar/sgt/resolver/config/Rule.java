/**
 *   Rule
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
package ar.sgt.resolver.config;

import java.util.HashMap;
import java.util.Map;

import com.google.code.regexp.NamedMatcher;
import com.google.code.regexp.NamedPattern;

/**
 * @author gabriel
 *
 */
public final class Rule {

	public String processor;
	public String uri;

	private NamedMatcher matcher;
	
	/**
	 * @param processor
	 * @param path
	 * @param type
	 */
	public Rule(String processor, String uri) {
		this.processor = processor;
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String path) {
		this.uri = path;
	}

	public String getProcessor() {
		return processor;
	}

	public void setProcessor(String processor) {
		this.processor = processor;
	}	

	public boolean match(String path) {
		NamedPattern p = NamedPattern.compile(this.uri);
		this.matcher = p.matcher(path);
		return this.matcher.matches();
	}
	
	public Map<String, String> parseParams() {
		Map<String, String> map = new HashMap<String, String>();
		if (this.matcher == null) return map;
		return this.matcher.namedGroups();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<rule>\n");
		builder.append("<processor>").append(this.processor).append("</processor>\n");
		builder.append("<uri>").append(this.uri).append("</uri>\n");
		builder.append("</rule>\n");
		return builder.toString();
	}

	
}
