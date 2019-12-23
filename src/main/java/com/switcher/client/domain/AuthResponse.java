package com.switcher.client.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class AuthResponse {
	
	private String token;
	private long exp;
	
	public String getToken() {
		
		return this.token;
	}

	public void setToken(String token) {
		
		this.token = token;
	}

	public void setExp(long exp) {
		
		this.exp = exp;
	}
	
	public boolean isExpired() {
		
		return this.exp*1000 < System.currentTimeMillis();
	}
	
}
