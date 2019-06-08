/**
 *   RuleConstant
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
package ar.sgt.resolver.config;

public interface RuleConstant {

	public static final String CURRENT_RULE = "current_rule";
	public static final String CURRENT_PATH = "current_path";
	
	public static final String NODE_PROCESSOR = "processor";
	public static final String NODE_FORWARDPROCESSOR = "forward-processor";
	public static final String NODE_RULE = "rule";
	public static final String NODE_ARG = "arg";
	
	public static final String ATT_CLASS = "class";
	public static final String ATT_PATTERN = "pattern";
	public static final String ATT_NAME = "name";
	public static final String ATT_REDIRECT = "redirect";
	public static final String ATT_VALUE = "value";

	public static final String NODE_REDIRECT = "redirect";
	
}
