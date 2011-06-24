package ar.sgt.resolver.processor;

public final class ProcessorContext {

	private String redirect;
	
	public ProcessorContext(String redirect) {
		this.redirect = redirect;
	}

	public String getRedirect() {
		return redirect;
	}
	
	
}
