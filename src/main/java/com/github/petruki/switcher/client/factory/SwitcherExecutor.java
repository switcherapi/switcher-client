package com.github.petruki.switcher.client.factory;

import java.util.Map;

import com.github.petruki.switcher.client.exception.SwitcherException;
import com.github.petruki.switcher.client.model.Switcher;

/**
 * @author rogerio
 * @since 2019-12-24
 */
public interface SwitcherExecutor {
	
	public boolean executeCriteria(final Switcher switcher) throws SwitcherException;
	
	public void updateContext(final Map<String, Object> properties) throws SwitcherException;

}
