/**
 *   ConfigParser
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
 *   along with JIPDBS. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author gabriel
 * 
 */
public class ConfigParser {

	private ResolverConfig config;

	/**
	 * 
	 */
	public ConfigParser() {
		this.config = new ResolverConfig();
	}

	public ResolverConfig parse(File file)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();

		NodeList dispatcherList = doc.getElementsByTagName(RuleConstant.NODE_DISPATCHER);

		for (int i = 0; i < dispatcherList.getLength(); i++) {
			processDispatcher(dispatcherList.item(i));
		}

		return this.config;
	}

	/**
	 * @param item
	 */
	private void processDispatcher(Node node) {

		Element dispatcher = (Element) node;
		String controller = dispatcher.getAttribute(RuleConstant.ATT_CLASS);

		NodeList rules = dispatcher.getElementsByTagName(RuleConstant.NODE_RULE);

		for (int i = 0; i < rules.getLength(); i++) {
			Element rule = (Element) rules.item(i);
			this.config.addRule(controller, rule.getAttribute(RuleConstant.ATT_URI),
											rule.hasAttribute(RuleConstant.ATT_NAME) ? rule
											.getAttribute(RuleConstant.ATT_NAME) : null);
		}

	}

}
