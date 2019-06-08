/**
 *   ForwardProcessor
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
package ar.sgt.resolver.processor;

import ar.sgt.resolver.exception.ProcessorException;

/**
 * ForwardProcessor
 * According to config, this processor has a mandatory redirect parameter.
 * It will return null so the redirect its handled by the ResponseProcessor.
 */
public final class ForwardProcessor extends ResponseProcessor {

	@Override
	public String doProcess(ResolverContext context) throws ProcessorException {
		return null;
	}
	
}
