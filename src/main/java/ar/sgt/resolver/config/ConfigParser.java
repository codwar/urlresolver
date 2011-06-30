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
 *   along with UrlResolver. If not, see <http://www.gnu.org/licenses/>.
 */
package ar.sgt.resolver.config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ar.sgt.resolver.processor.ForwardProcessor;
import ar.sgt.resolver.rule.Rule;

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

		NodeList processorList = doc.getElementsByTagName(RuleConstant.NODE_PROCESSOR);

		for (int i = 0; i < processorList.getLength(); i++) {
			Element node = (Element) processorList.item(i);
			String controller = node.getAttribute(RuleConstant.ATT_CLASS);
			processProcessorNode(controller, node);
		}

		NodeList forwardList = doc.getElementsByTagName(RuleConstant.NODE_FORWARDPROCESSOR);

		for (int i = 0; i < forwardList.getLength(); i++) {
			Element node = (Element) forwardList.item(i);
			String controller = ForwardProcessor.class.getName();
			processProcessorNode(controller, node);
		}
		
		return this.config;
	}

	/**
	 * @param item
	 */
	private void processProcessorNode(String controller, Element node) {

		NodeList rules = node.getElementsByTagName(RuleConstant.NODE_RULE);

		for (int i = 0; i < rules.getLength(); i++) {
			Element ruleNode = (Element) rules.item(i);
			Rule rule = this.config.addRule(controller, ruleNode.getAttribute(RuleConstant.ATT_PATTERN),
											ruleNode.hasAttribute(RuleConstant.ATT_NAME) ? ruleNode
											.getAttribute(RuleConstant.ATT_NAME) : null,
											node.hasAttribute(RuleConstant.ATT_REDIRECT) ? node
													.getAttribute(RuleConstant.ATT_REDIRECT) : null);
			processRuleArguments(rule, ruleNode);
		}

	}

	private void processRuleArguments(Rule rule, Element node) {
		
		NodeList args = node.getElementsByTagName(RuleConstant.NODE_ARG);
		
		for (int i = 0; i < args.getLength(); i++) {
			Element argNode = (Element) args.item(i);
			rule.addArgument(argNode.getAttribute(RuleConstant.ATT_NAME), argNode.getAttribute(RuleConstant.ATT_VALUE));
		}
	}

}
