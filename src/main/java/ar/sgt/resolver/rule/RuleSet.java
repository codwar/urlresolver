/**
 *   RuleSet
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
package ar.sgt.resolver.rule;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author gabriel
 *
 */
public final class RuleSet {

	public Map<String, Rule> rules; 
	
	public RuleSet() {
		this.rules = new LinkedHashMap<String, Rule>();
	}

	public Rule addRule(String processor, String path, String name, String redirect) {
		// if no name is set. use path as name
		Rule rule = new Rule(name, processor, path, redirect);
		this.rules.put(name != null ? name : path, rule);
		return rule;
	}
	
	public Rule match(String path) {
		for (Rule rule : rules.values()) {
			if (rule.match(path)) return rule;
		}
		return null;
	}
	
	public Rule findByName(String name) {
		return this.rules.get(name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<ruleset>\n");
		for (Rule rule : this.rules.values()) {
			builder.append(rule.toString());
		}
		builder.append("</ruleset>");
		return builder.toString();
	}
	
}
