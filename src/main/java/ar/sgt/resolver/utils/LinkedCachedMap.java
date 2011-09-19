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

import java.util.LinkedHashMap;

public class LinkedCachedMap<K, V> extends LinkedHashMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5708421843591648380L;

	private int maxEntries;
	
	public LinkedCachedMap(int initialCapacity, int maxCapacity) {
		super(initialCapacity);
		this.maxEntries = maxCapacity;
	}
	
	/* (non-Javadoc)
	 * @see java.util.LinkedHashMap#removeEldestEntry(java.util.Map.Entry)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected boolean removeEldestEntry(java.util.Map.Entry eldest) {
		return size() > maxEntries;
	}
	
}
