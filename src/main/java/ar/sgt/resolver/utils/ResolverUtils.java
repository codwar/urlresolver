/**
 *   Copyright(c) 2010-2011 CodWar Soft
 * 
 *   This file is part of IPDB UrT.
 *
 *   IPDB UrT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This software is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this software. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.utils;

import java.io.UnsupportedEncodingException;

public class ResolverUtils {

	/**
	 * URLDecoder.decode replace %2B with a space.
	 * This function use the corresponding (+) plus sign for %2B.
	 * It use UTF-8 as default.
	 * @throws UnsupportedEncodingException 
	 */
	public static String decodeUrl(String s) throws UnsupportedEncodingException {
		int numChars = s.length();
		StringBuffer sb = new StringBuffer(numChars > 500 ? numChars / 2
				: numChars);
		int i = 0;

		char c;
		byte[] bytes = null;
		while (i < numChars) {
			c = s.charAt(i);
			switch (c) {
			case '%':
				try {

					if (bytes == null)
						bytes = new byte[(numChars - i) / 3];
					int pos = 0;

					while (((i + 2) < numChars) && (c == '%')) {
						bytes[pos++] = (byte) Integer.parseInt(
								s.substring(i + 1, i + 3), 16);
						i += 3;
						if (i < numChars)
							c = s.charAt(i);
					}

					if ((i < numChars) && (c == '%'))
						throw new IllegalArgumentException(
								"URLDecoder: Incomplete trailing escape (%) pattern");

					sb.append(new String(bytes, 0, pos, "UTF-8"));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException(
							"URLDecoder: Illegal hex characters in escape (%) pattern - "
									+ e.getMessage());
				}
				break;
			default:
				sb.append(c);
				i++;
				break;
			}
		}

		return sb.toString();
	}
	
}
