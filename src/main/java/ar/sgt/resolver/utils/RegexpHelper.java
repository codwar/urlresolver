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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import com.google.code.regexp.NamedPattern;

/**
 * Functions for reversing a regular expression (used in reverse URL resolving).
 * 
 * This is not, and is not intended to be, a complete reg-exp decompiler. It
 * should be good enough for a large class of URLS, however.
 */
public final class RegexpHelper {

	/*
	 * If the char is in this list, it should be ignored (used for regexp
	 * patterns, ie: \w )
	 */
	private static List<String> ESCAPE_MAPPINGS = new LinkedList<String>();
	/* this chars are ignored */
	private static List<String> SKIP_MAPPINGS = new LinkedList<String>();

	private static Pattern STRING_RE = Pattern.compile("\\w");

	static {
		ESCAPE_MAPPINGS.add("t");
		ESCAPE_MAPPINGS.add("n");
		ESCAPE_MAPPINGS.add("r");
		ESCAPE_MAPPINGS.add("f");
		ESCAPE_MAPPINGS.add("a");
		ESCAPE_MAPPINGS.add(".");
		ESCAPE_MAPPINGS.add("d");
		ESCAPE_MAPPINGS.add("D");
		ESCAPE_MAPPINGS.add("s");
		ESCAPE_MAPPINGS.add("S");
		ESCAPE_MAPPINGS.add("W");
		ESCAPE_MAPPINGS.add("w");
		ESCAPE_MAPPINGS.add("b");
		ESCAPE_MAPPINGS.add("B");
		ESCAPE_MAPPINGS.add("A");
		ESCAPE_MAPPINGS.add("G");
		ESCAPE_MAPPINGS.add("Z");
		ESCAPE_MAPPINGS.add("Q");
		ESCAPE_MAPPINGS.add("E");

		SKIP_MAPPINGS.add("^");
		SKIP_MAPPINGS.add("|");
		SKIP_MAPPINGS.add("$");
		SKIP_MAPPINGS.add("+");
		SKIP_MAPPINGS.add("*");
	}

	/**
	 * Given a reg-exp pattern, normalizes it to a list of forms that suffice
	 * for reverse matching.
	 * 
	 */
	public static String normalize(String pattern) {
		NamedPattern np = NamedPattern.compile(pattern);
		for (String g : np.groupNames()) {
			pattern = pattern.replaceAll("\\?P\\(" + g + "\\)", "\\$" + g);
		}
		StringBuilder cleanPattern = new StringBuilder();
		CharacterIterator it = new StringCharacterIterator(pattern);
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			if (ch == '\\') {
				ch = it.next();
				if (!ESCAPE_MAPPINGS.contains(String.valueOf(ch)))
					cleanPattern.append(ch);
			} else if (SKIP_MAPPINGS.contains(String.valueOf(ch)))
				continue;
			else if (ch == '[') { // start group
				int nest = 1;
				while (nest > 0) {
					ch = it.next();
					if (ch == '[')
						nest += 1;
					else if (ch == ']')
						nest -= 1;
				}
			} else if (ch == '(') { // start group
				int nest = 1;
				ch = it.next();
				if (ch == '$') { // we found a named group
					cleanPattern.append("$");
					ch = it.next();
					while (STRING_RE.matcher(String.valueOf(ch)).matches()) {
						cleanPattern.append(ch);
						ch = it.next();
					}
				}
				do {
					if (ch == '(')
						nest += 1;
					else if (ch == ')')
						nest -= 1;
					ch = it.next();
				} while (nest > 0);
				ch = it.previous(); // go back one char
			} else
				cleanPattern.append(ch);
		}
		return cleanPattern.toString();
	}

}
