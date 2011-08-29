package ar.sgt.resolver.processor;

import ar.sgt.resolver.rule.Rule;

public final class ProcessorContext {

	private String redirect;
	private Rule rule;
	
	public ProcessorContext(Rule rule, String redirect) {
		this.rule = rule;
		this.redirect = redirect;
	}

	public Rule getRule() {
		return rule;
	}

	public String getRedirect() {
		return redirect;
	}
	
	
}
