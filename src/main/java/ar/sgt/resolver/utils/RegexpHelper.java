/**
 *   RegexpHelper
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.code.regexp.NamedMatcher;
import com.google.code.regexp.NamedPattern;

/**
 * Functions for reversing a regular expression (used in reverse URL resolving).
 * 
 * This is not, and is not intended to be, a complete reg-exp decompiler. It
 * should be good enough for a large class of URLS, however.
 */
public final class RegexpHelper {

	/*
	 * Mapping of an escape character to a representative of that class. So,
	 * e.g., "\w" is replaced by "x" in a reverse URL. A value of None means to
	 * ignore this sequence. Any missing key is mapped to itself.
	 */
	private static Map<String, String> ESCAPE_MAPPINGS;

	static {
		ESCAPE_MAPPINGS = new HashMap<String, String>();
		ESCAPE_MAPPINGS.put("A", null);
		ESCAPE_MAPPINGS.put("b", null);
		ESCAPE_MAPPINGS.put("B", null);
		ESCAPE_MAPPINGS.put("d", "0");
		ESCAPE_MAPPINGS.put("D", "x");
		ESCAPE_MAPPINGS.put("s", " ");
		ESCAPE_MAPPINGS.put("S", "x");
		ESCAPE_MAPPINGS.put("w", "x");
		ESCAPE_MAPPINGS.put("W", "!");
		ESCAPE_MAPPINGS.put("Z", null);
	}

	/**
	 * Given a reg-exp pattern, normalizes it to a list of forms that suffice
	 * for reverse matching. This does the following:
	 * 
	 * (1) For any repeating sections, keeps the minimum number of occurrences
	 * permitted (this means zero for optional groups).
	 * (2) If an optional group includes parameters, include one occurrence of 
	 * that group (along with the zero occurrence case from step (1)).
	 * (3) Select the first (essentially an arbitrary) element from any character
	 * class. Select an arbitrary character for any unordered class (e.g. '.' or '\w') in the pattern.
	 * (4) Ignore comments and any of the reg-exp flags that won't change what we 
	 * construct ("iLmsu"). "(?x)" is an error, however.
	 * (6) Raise an error on all other non-capturing (?...) forms (e.g. look-ahead and look-behind
	 * matches) and any disjunctive ('|') constructs.
	 */
	public static String normalize(String pattern, Map<String, String> data) {
		NamedPattern p = NamedPattern.compile(pattern);
		List<String> groups = p.groupNames();
		for (String name : groups) {
			String groupMatch = "\\?P\\(" + name + "\\)";
			pattern = pattern.replaceFirst(groupMatch, data.get(name));
		}
		
		if (pattern.startsWith("^")) {
			pattern = pattern.substring(1);
		}
		
		return pattern;
	}
}
