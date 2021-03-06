/**
 *   RedirectProcessor
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;

/**
 * This processor implementation can handle automatic redirection based on
 * config or processor response.
 * Unlike ResponseProcessor this is a visible redirect instead of an internal forward.
 * Useful for actions that needs to redirect to another processor.
 * This is a Temporal Redirect (302)
 */
public abstract class RedirectProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(RedirectProcessor.class);
	
	@Override
	public final void process(ProcessorContext processorContext,
			ResolverContext context) throws ProcessorException {
		log.debug("Entering processor");
		String resp = doProcess(context);
		String redirect = resp != null ? resp : processorContext.getRedirect();
		if (redirect != null) {
			try {
				log.debug("Redirect to {}", redirect);
				context.getResponse().sendRedirect(context.getResponse().encodeRedirectURL(redirect));
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new ProcessorException(e);
			}
		} else {
			log.debug("No redirect. Continue");
		}
	}

	public abstract String doProcess(ResolverContext context)
			throws ProcessorException;

}
