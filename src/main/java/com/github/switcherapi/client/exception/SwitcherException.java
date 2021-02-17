package com.github.switcherapi.client.exception;

/**
 * @author rogerio
 * @since 2019-12-24
 */
public class SwitcherException extends RuntimeException {
	
	private static final long serialVersionUID = -1748896326811044977L;

	public SwitcherException(final String message, final Exception ex) {
		
		super(String.format("Something went wrong: %s", message), ex);
	}

}
