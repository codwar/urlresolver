/**
 *   PermanentRedirectProcessor
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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.exception.ProcessorException;
import ar.sgt.resolver.exception.ReverseException;
import ar.sgt.resolver.exception.RuleNotFoundException;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.rule.Rule;
import ar.sgt.resolver.utils.UrlReverse;

/**
 * This processor implementation perform a permanent redirect (301) based on config.
 * Usefull to transform old style uri in new pretty uri.
 * Unlike other Processors, this one cannot be extended.
 */
public final class PermanentRedirectProcessor implements Processor {

	private static final Logger log = LoggerFactory.getLogger(PermanentRedirectProcessor.class);

	@Override
	public final void process(ProcessorContext processorContext,
			ResolverContext context) throws ProcessorException {
		log.debug("Entering processor");
		String ruleName = processorContext.getRedirect();
		Rule current = processorContext.getRule();
		Map<String, String> params = new HashMap<String, String>();
		log.debug("Processing params");
		for (Entry<String, String> args : current.getArguments().entrySet()) {
			params.put(args.getKey(), context.getRequest().getParameter(args.getValue()));
		}
		// find the rule
		ResolverConfig resolver = (ResolverConfig) context.getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
		UrlReverse reverse = new UrlReverse(resolver);
		String url;
		try {
			log.debug("Resolve rule {}", ruleName);
			url = context.getRequest().getContextPath() + reverse.resolve(ruleName, params);
		} catch (RuleNotFoundException e) {
			throw new ProcessorException(e);
		} catch (ReverseException e) {
			throw new ProcessorException(e);
		}
		HttpServletResponse resp = context.getResponse();
		log.debug("Permanent redirect to {}", url);
		resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		resp.setHeader("Location", url);
	}

}
