package com.github.petruki.switcher.client.service;

import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.github.petruki.switcher.client.model.AuthRequest;
import com.github.petruki.switcher.client.model.AuthResponse;
import com.github.petruki.switcher.client.model.Switcher;
import com.github.petruki.switcher.client.utils.SwitcherContextParam;

/**
 * @author rogerio
 * @since 2019-12-24
 */
public class ClientServiceImpl implements ClientService {
	
	private static final Logger logger = LogManager.getLogger(ClientServiceImpl.class);
	
	private Client client;
	
	public ClientServiceImpl() {
		this.setClient(ClientBuilder.newClient());
	}
	
	@Override
	public Response executeCriteriaService(final Map<String, Object> properties, 
			final Switcher switcher) throws Exception {
		
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("switcher: %s", switcher));
		}
		
		final WebTarget myResource = client.target(String.format(CRITERIA_URL, properties.get(SwitcherContextParam.URL)))
				.queryParam(Switcher.KEY, switcher.getKey())
				.queryParam(Switcher.SHOW_REASON, Boolean.TRUE)
				.queryParam(Switcher.BYPASS_METRIC, properties.containsKey(Switcher.BYPASS_METRIC) ? 
						properties.get(Switcher.BYPASS_METRIC) : false);
		
		final Response response = myResource.request(MediaType.APPLICATION_JSON)
			.header(HEADER_AUTHORIZATION, String.format(TOKEN_TEXT, ((AuthResponse) properties.get(AUTH_RESPONSE)).getToken()))
			.post(Entity.json(switcher.getInputRequest()));
		
		return response;
	}
	
	@Override
	public Response auth(final Map<String, Object> properties) throws Exception {
		
		final AuthRequest authRequest = new AuthRequest();
		authRequest.setDomain((String) properties.get(SwitcherContextParam.DOMAIN));
		authRequest.setComponent((String) properties.get(SwitcherContextParam.COMPONENT));
		authRequest.setEnvironment((String) properties.get(SwitcherContextParam.ENVIRONMENT));

		final WebTarget myResource = client.target(String.format(AUTH_URL, properties.get(SwitcherContextParam.URL)));
		final Response response = myResource.request(MediaType.APPLICATION_JSON)
			.header(HEADER_APIKEY, properties.get(SwitcherContextParam.APIKEY))
			.post(Entity.json(authRequest));	

		return response;
	}
	
	@Override
	public Response resolveSnapshot(Map<String, Object> properties) throws Exception {
		
		final String domain = (String) properties.get(SwitcherContextParam.DOMAIN);
		final String environment = (String) properties.get(SwitcherContextParam.ENVIRONMENT);
		
		final StringBuffer query = new StringBuffer();
		query.append("{\"query\":\"{ domain(name: \\\"%s\\\", environment: \\\"%s\\\") { ");
		query.append("name version description activated ");
		query.append("group { name description activated ");
		query.append("config { key description activated ");
		query.append("strategies { strategy activated operation values } ");
		query.append("components } } } }\"}");
		
		final WebTarget myResource = client.target(String.format(SNAPSHOT_URL, properties.get(SwitcherContextParam.URL)));
		
		final Response response = myResource.request(MediaType.APPLICATION_JSON)
			.header(HEADER_AUTHORIZATION, String.format(TOKEN_TEXT, ((AuthResponse) properties.get(AUTH_RESPONSE)).getToken()))
			.post(Entity.json(String.format(query.toString(), domain, environment)));
		
		return response;
	}

	public void setClient(Client client) {
		
		this.client = client;
	}

}
