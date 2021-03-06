/**
 *   ResolverTest
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
package ar.sgt.resolver.test.processor;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

import ar.sgt.resolver.config.ConfigParser;
import ar.sgt.resolver.config.ResolverConfig;
import ar.sgt.resolver.processor.ForwardProcessor;
import ar.sgt.resolver.rule.Rule;

public class ResolverTest extends TestCase {

	private ResolverConfig config;
	
	@Override
	protected void setUp() throws Exception {
		ConfigParser parser = new ConfigParser();
		try {
			this.config = parser.parse(this.getClass().getResourceAsStream("/urlresolver.xml"));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void testConfigLoader() {
		Rule rule1 = config.findRule("aslugcode/detail/");
		assertEquals("TestProcessor1", rule1.getProcessor());
		assertEquals("aslugcode", rule1.parseParams().get("slug"));
		Rule rule2 = config.findRule("detail/");
		assertNull(rule2.parseParams().get("slug"));
		assertEquals("TestProcessor2", rule2.getProcessor());
		assertEquals("page1.jsp", rule2.getRedirect());
		Rule rule3 = config.findByName("rule1");
		assertEquals("TestProcessor1", rule3.getProcessor());
		Rule rule4 = config.findByName("rule4");
		assertNull(rule4);
		Rule rule5 = config.findRule("detail/redirect/");
		assertEquals(ForwardProcessor.class.getName(), rule5.getProcessor());
		assertEquals(2, rule5.getArguments().size());
		assertEquals("value2", rule5.parseParams().get("arg2"));
		Rule rule6 = config.findRule("somepage.jsp?d=valor");
		assertEquals("rule1", rule6.getRedirect());
	}
	
	
}
