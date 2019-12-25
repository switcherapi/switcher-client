package com.switcher.client.factory;

import java.util.Map;

import com.switcher.client.domain.Switcher;
import com.switcher.client.exception.SwitcherException;

/**
 * @author rogerio
 * @since 2019-12-24
 */
public interface SwitcherExecutor {
	
	public boolean executeCriteria(final Switcher switcher) throws SwitcherException;
	
	public void updateContext(final Map<String, Object> properties);

}
