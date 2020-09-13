package com.github.switcherapi.client.exception;

/**
 * @author rogerio
 * @since 2019-12-24
 */
public class SwitcherFactoryContextException extends SwitcherException {
	
	private static final long serialVersionUID = -6340224967205872873L;

	public SwitcherFactoryContextException() {
		
		super("Context was not initialized. Call 'buildContext' to set up the factory", null);
	}
	
	public SwitcherFactoryContextException(String error) {
		
		super(String.format("Context has errors - %s", error), null);
	} 

}
