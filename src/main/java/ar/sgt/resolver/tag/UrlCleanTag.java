/**
 *   UrlCleanTag
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.exception.RuleNotFoundException;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.rule.Rule;
import ar.sgt.resolver.utils.RegexpHelper;

public class UrlCleanTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3538312691752045758L;
	
	private String name;
	private String var;
	
	@Override
	public int doStartTag() throws JspException {
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ResolverConfig config = (ResolverConfig) pageContext.getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
		try {
			Rule rule = config.findByName(this.name);
			if (rule != null) {
				String html = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + RegexpHelper.normalize(rule.getPattern());
				if (this.var != null) {
					pageContext.setAttribute(this.var, html);
				} else {
					pageContext.getOut().write(html);	
				}
			} else {
				throw new RuleNotFoundException("Unable to find a rule for name: " + this.name);
			}
		} catch (IOException e) {
			throw new JspException(e);
		} catch (RuleNotFoundException e) {
			throw new JspException(e);
		}
		return EVAL_PAGE;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	

}
