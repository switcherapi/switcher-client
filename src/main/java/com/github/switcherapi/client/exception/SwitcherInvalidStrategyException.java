package com.github.switcherapi.client.exception;

/**
 * @author Roger Floriano (petruki)
 * @since 2019-12-24
 */
public class SwitcherInvalidStrategyException extends SwitcherException {
	
	private static final long serialVersionUID = -4091584736216245100L;

	public SwitcherInvalidStrategyException(final String strategyName) {
		
		super(String.format("Invalid strategy %s", strategyName), null);
	}

}
