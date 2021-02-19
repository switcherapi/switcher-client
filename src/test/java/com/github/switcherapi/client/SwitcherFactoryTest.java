package com.github.switcherapi.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.switcherapi.Switchers;
import com.github.switcherapi.client.exception.SwitcherFactoryContextException;

public class SwitcherFactoryTest {
	
	final String CONTEXT_ERROR = "Something went wrong: Context has errors - %s not found";
	
	@BeforeEach
	public void resetProperties() {
		Switchers.loadProperties();
	}
	
	@Test
	public void shouldThrowError_noUrl() {
		Switchers.getProperties().setUrl(null);
		
		Exception ex = assertThrows(SwitcherFactoryContextException.class, () -> {
			Switchers.initializeClient();
		});
		
		assertEquals(String.format(
				CONTEXT_ERROR, "SwitcherContextParam.URL"), ex.getMessage());
	}
	
	@Test
	public void shouldThrowError_noApi() {
		Switchers.getProperties().setApiKey(null);
		
		Exception ex = assertThrows(SwitcherFactoryContextException.class, () -> {
			Switchers.initializeClient();
		});
		
		assertEquals(String.format(
				CONTEXT_ERROR, "SwitcherContextParam.APIKEY"), ex.getMessage());
	}
	
	@Test
	public void shouldThrowError_noDomain() {
		Switchers.getProperties().setDomain(null);
		
		Exception ex = assertThrows(SwitcherFactoryContextException.class, () -> {
			Switchers.initializeClient();
		});
		
		assertEquals(String.format(
				CONTEXT_ERROR, "SwitcherContextParam.DOMAIN"), ex.getMessage());
	}
	
	@Test
	public void shouldThrowError_noComponent() throws Exception {
		Switchers.getProperties().setComponent(null);
		
		Exception ex = assertThrows(SwitcherFactoryContextException.class, () -> {
			Switchers.initializeClient();
		});
		
		assertEquals(String.format(
				CONTEXT_ERROR, "SwitcherContextParam.COMPONENT"), ex.getMessage());
	}
	
	@Test
	public void shouldThrowErrorWhenAutoLoad_noLocation() {
		Switchers.getProperties().setSnapshotLocation(null);
		Switchers.getProperties().setSnapshotAutoLoad(true);
		
		Exception ex = assertThrows(SwitcherFactoryContextException.class, () -> {
			Switchers.initializeClient();
		});
		
		assertEquals(String.format(
				CONTEXT_ERROR, "SwitcherContextParam.SNAPSHOT_LOCATION"), ex.getMessage());
	}
	
	@Test
	public void shouldThrowErrorWhenSilentMode_noRetryTimer() {
		Switchers.getProperties().setSilentMode(true);
		Switchers.getProperties().setRetryAfter(null);;
		
		Exception ex = assertThrows(SwitcherFactoryContextException.class, () -> {
			Switchers.initializeClient();
		});
		
		assertEquals(String.format(
				CONTEXT_ERROR, "SwitcherContextParam.RETRY_AFTER"), ex.getMessage());
	}

}
