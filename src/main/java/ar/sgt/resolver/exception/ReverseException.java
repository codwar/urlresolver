/**
 *   ReverseException
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
package ar.sgt.resolver.exception;

public class ReverseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4971131837004333603L;

	public ReverseException() {
		super();
	}
	
	public ReverseException(String m) {
		super(m);
	}
	
	public ReverseException(Throwable t) {
		super(t);
	}
	
}
