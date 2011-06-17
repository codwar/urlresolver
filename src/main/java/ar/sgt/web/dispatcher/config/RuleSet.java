/**
 *   JIPDBS
 *   Copyright(c) 2011 Martin Schonaker
 *   Copyright(c) 2011 Sergio Gabriel Teves
 * 
 *   This file is part of JIPDBS.
 *
 *   JIPDBS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   JIPDBS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with JIPDBS. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.web.dispatcher.config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gabriel
 *
 */
public final class RuleSet {

	public List<Rule> rules; 

	public RuleSet() {
		this.rules = new ArrayList<Rule>();
	}

	public void addRule(String path, String type) {
		this.rules.add(new Rule(path, type));
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<ruleset>");
		for (Rule rule : this.rules) {
			builder.append(rule.toString());
		}
		builder.append("</ruleset>");
		return builder.toString();
	}
	
}
