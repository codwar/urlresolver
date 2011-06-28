/**
 *   Processor
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

import java.util.logging.Logger;

import ar.sgt.resolver.exception.ProcessorException;

/**
 * This processor implementation can handle automatic redirection based on
 * config or processor response
 * 
 */
public abstract class ResponseProcessor implements Processor {

	private static final Logger log = Logger.getLogger(ResponseProcessor.class
			.getName());

	@Override
	public final void process(ProcessorContext processorContext,
			ResolverContext context) throws ProcessorException {
		log.fine("Entering processor");
		String resp = doProcess(context);
		String redirect = resp != null ? resp : processorContext.getRedirect();
		if (redirect != null) {
			try {
				log.fine("Fordward to " + redirect);
				context.getServletContext().getRequestDispatcher(redirect).forward(context.getRequest(), context.getResponse());
			} catch (Exception e) {
				log.severe(e.getMessage());
				throw new ProcessorException(e);
			}
		} else {
			log.fine("No redirect. Continue");
		}
	}

	public abstract String doProcess(ResolverContext context)
			throws ProcessorException;

}
