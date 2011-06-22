/**
 *   ResolverConfig
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

public class ResolverConfig {

	public RuleSet rules;
	
	ResolverConfig() {
		rules = new RuleSet();
	}
	
	void addRule(String controller, String path, String type) {
		this.rules.addRule(controller, path, type);
	}

	public Rule findRule(String path) {
		return rules.match(path);
	}
	
	public Rule findByName(String name) {
		return this.rules.findByName(name);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.rules.toString();
	}
}
