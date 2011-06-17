/**
 *   JIPDBS
 *   Copyright(c) 2011 Martin Schonaker
 *   Copyright(c) 2011 Sergio Gabriel Teves
 * 
 *   This file is part of JIPDBS.
 *
 *   JIPDBS is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   JIPDBS is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with JIPDBS. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.web.dispatcher.config;

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

	private static final String NODE_DISPATCHER = "dispatcher";
	private static final String NODE_RULE = "rule";

	private static final String ATT_CLASS = "class";
	private static final String ATT_URI = "uri";
	private static final String ATT_MATCH_TYPE = "match-type";

	private static final String DEFAULT_TYPE = "regexp";
	private DispatcherConfig config;

	/**
	 * 
	 */
	public ConfigParser() {
		this.config = new DispatcherConfig();
	}

	public DispatcherConfig parse(File file)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(file);
		doc.getDocumentElement().normalize();

		NodeList dispatcherList = doc.getElementsByTagName(NODE_DISPATCHER);

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
		String controller = dispatcher.getAttribute(ATT_CLASS);

		NodeList rules = dispatcher.getElementsByTagName(NODE_RULE);

		RuleSet ruleSet = new RuleSet();
		for (int i = 0; i < rules.getLength(); i++) {
			Element rule = (Element) rules.item(i);
			ruleSet.addRule(
					rule.getAttribute(ATT_URI),
					rule.hasAttribute(ATT_MATCH_TYPE) ? rule
							.getAttribute(ATT_MATCH_TYPE) : DEFAULT_TYPE);
		}

		this.config.addRule(controller, ruleSet);

	}

	public static void main(String[] args) {
		File file = new File(
				"/home/gabriel/workspace/simple-frontcontroller/src/main/java/dispatcher.xml");
		ConfigParser parser = new ConfigParser();
		try {
			DispatcherConfig config = parser.parse(file);
			System.out.println(config.toString());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
