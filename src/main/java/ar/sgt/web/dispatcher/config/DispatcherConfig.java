
package ar.sgt.web.dispatcher.config;

import java.util.HashMap;
import java.util.Map;


public class DispatcherConfig {

	public Map<String, RuleSet> rules;
	
	DispatcherConfig() {
		this.rules = new HashMap<String, RuleSet>();
	}
	
	void addRule(String controller, RuleSet rules) {
		this.rules.put(controller, rules);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("<rules>");
		for (String key : this.rules.keySet()) {
			builder.append("<controller:" + key + ">");
			builder.append(this.rules.get(key).toString());
			builder.append("</controller>");
		}
		builder.append("</rules>");
		return builder.toString();
	}
}
