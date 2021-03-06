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


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.flow.ForceRedirect;

/**
 * This processor implementation can handle automatic redirection based on
 * config or processor response
 * 
 */
public abstract class ResponseProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(ResponseProcessor.class);

	@Override
	public final void process(ProcessorContext processorContext,
			ResolverContext context) throws ProcessorException {
		log.debug("Entering processor");
		String resp;
		try {
			resp = doProcess(context);
		} catch (ForceRedirect r) {
			log.debug("Redirect to {}", r.getUrl());
			try {
				context.getResponse().sendRedirect(context.getResponse().encodeRedirectURL(r.getUrl()));
			} catch (IOException e) {
				log.error(e.getMessage());
				throw new ProcessorException(e);
			}
			return;
		}
		String redirect = resp != null ? resp : processorContext.getRedirect();
		if (redirect != null) {
			try {
				log.debug("Fordward to {}", redirect);
				context.getServletContext().getRequestDispatcher(redirect).forward(context.getRequest(), context.getResponse());
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
