/**
 *   UrlTag
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
package ar.sgt.resolver.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.config.Rule;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.utils.RegexpHelper;

public class UrlTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3538312691752045758L;
	
	private Map<String, String> params;
	
	private String name;
	
	@Override
	public int doStartTag() throws JspException {
		this.params = new HashMap<String, String>();
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ResolverConfig config = (ResolverConfig) pageContext.getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
		Rule rule = config.findByName(this.name);
		if (rule == null) throw new JspException("Unable to find a rule for name: " + this.name);
		String html = RegexpHelper.normalize(rule.getPattern(), this.params);
		try {
			pageContext.getOut().write(html);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}
	
	protected void addParam(String name, String value) {
		this.params.put(name, value);
	}

	public void setName(String name) {
		this.name = name;
	}
	

}
