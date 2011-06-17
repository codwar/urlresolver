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

/**
 * @author gabriel
 *
 */
public final class Rule {

	public String path;
	
	public String type;

	/**
	 * @param controller
	 * @param path
	 * @param type
	 */
	public Rule(String path, String type) {
		this.path = path;
		this.type = type;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<rule>");
		builder.append("<uri>").append(this.path).append("</uri>");
		builder.append("<match>").append(this.type).append("</match>");
		builder.append("</rule>");
		return builder.toString();
	}
	
}
