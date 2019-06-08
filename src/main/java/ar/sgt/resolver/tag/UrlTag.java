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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.exception.ReverseException;
import ar.sgt.resolver.exception.RuleNotFoundException;
import ar.sgt.resolver.listener.ContextLoader;
import ar.sgt.resolver.utils.UrlReverse;

public class UrlTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3538312691752045758L;
	
	private static Logger log = LoggerFactory.getLogger(UrlTag.class);
	
	private Map<String, String> params;
	
	private String name;
	private String var;
	
	@Override
	public int doStartTag() throws JspException {
		this.params = new HashMap<String, String>();
		return EVAL_BODY_INCLUDE;
	}

	@Override
	public int doEndTag() throws JspException {
		ResolverConfig config = (ResolverConfig) pageContext.getServletContext().getAttribute(ContextLoader.RESOLVER_CONFIG);
		UrlReverse reverse = new UrlReverse(config);
		try {
			String html = "#";
			try {
				html = ((HttpServletRequest) pageContext.getRequest()).getContextPath() + reverse.resolve(this.name, this.params);
			} catch (RuleNotFoundException e) {
				log.error(e.getMessage());
			} catch (ReverseException e) {
				log.error(e.getMessage());
			}
			if (this.var != null) {
				pageContext.setAttribute(this.var, html);
			} else {
				pageContext.getOut().write(html);	
			}
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

	public String getVar() {
		return var;
	}

	public void setVar(String var) {
		this.var = var;
	}
	

}
