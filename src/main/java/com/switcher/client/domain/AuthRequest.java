package com.switcher.client.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class AuthRequest {
	
	private String domain;
	private String component;
	private String environment;
	
	public void setDomain(String domain) {
		
		this.domain = domain;
	}
	
	public void setComponent(String component) {
		
		this.component = component;
	}
	
	public void setEnvironment(String environment) {
		
		this.environment = environment;
	}

	public String getDomain() {
		
		return domain;
	}
	
	public String getComponent() {
		
		return component;
	}

	public String getEnvironment() {
		
		return environment;
	}

	@Override
	public String toString() {
		
		return "AuthRequest [domain=" + domain + ", component=" + component + ", environment=" + environment + "]";
	}

}
