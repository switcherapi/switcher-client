package com.github.petruki.switcher.client.utils;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Paths;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import com.github.petruki.switcher.client.SwitcherFactory;
import com.github.petruki.switcher.client.exception.SwitcherAPIConnectionException;
import com.github.petruki.switcher.client.exception.SwitcherSnapshotLoadException;
import com.github.petruki.switcher.client.exception.SwitcherSnapshotWriteException;
import com.github.petruki.switcher.client.facade.ClientServiceFacade;
import com.github.petruki.switcher.client.model.AuthResponse;
import com.github.petruki.switcher.client.model.Switcher;
import com.github.petruki.switcher.client.model.criteria.Criteria;
import com.github.petruki.switcher.client.model.criteria.Domain;
import com.github.petruki.switcher.client.model.criteria.Snapshot;
import com.github.petruki.switcher.client.service.ClientService;
import com.github.petruki.switcher.client.service.ClientServiceImpl;

@PowerMockIgnore({"javax.management.*", "org.apache.log4j.*", "javax.xml.*", "javax.script.*"})
@RunWith(PowerMockRunner.class)
public class SnapshotLoaderTest {
	
	private static final String SNAPSHOTS_LOCAL = Paths.get(StringUtils.EMPTY).toAbsolutePath().toString() + "/src/test/resources";
	
	private Map<String, Object> properties;
	
	@Before
	public void setupContext() {

		properties = new HashMap<String, Object>();
		properties.put(SwitcherContextParam.URL, "http://localhost:3000");
		properties.put(SwitcherContextParam.APIKEY, "$2b$08$S2Wj/wG/Rfs3ij0xFbtgveDtyUAjML1/TOOhocDg5dhOaU73CEXfK");
		properties.put(SwitcherContextParam.DOMAIN, "switcher-domain");
		properties.put(SwitcherContextParam.COMPONENT, "switcher-client");
		properties.put(SwitcherContextParam.ENVIRONMENT, "generated_default");
		properties.put(SwitcherContextParam.SNAPSHOT_LOCATION, SNAPSHOTS_LOCAL);
		properties.put(SwitcherContextParam.SNAPSHOT_AUTO_LOAD, true);
	}
	
	private void generateLoaderMock(final int executionStatus) throws Exception {
		
		final ClientService mockClientServiceImpl = PowerMockito.mock(ClientService.class);
		final Response mockResponseAuth = PowerMockito.mock(Response.class);
		final Response mockResponseResolveSnapshot = PowerMockito.mock(Response.class);
		
		final AuthResponse authResponse = new AuthResponse();
		authResponse.setExp(SwitcherUtils.addTimeDuration("2s", new Date()).getTime()/1000);
		authResponse.setToken("123lkjsuoi23487skjfh28dskjn29");
		
		PowerMockito.when(mockClientServiceImpl.auth(this.properties)).thenReturn(mockResponseAuth);
		PowerMockito.when(mockResponseAuth.readEntity(AuthResponse.class)).thenReturn(authResponse);
		
		final Snapshot mockedSnapshot = new Snapshot();
		final Criteria criteria = new Criteria();
		criteria.setDomain(SnapshotLoader.loadSnapshot(SNAPSHOTS_LOCAL + "/default.json"));
		mockedSnapshot.setData(criteria);
		
		PowerMockito.when(mockClientServiceImpl.resolveSnapshot(this.properties)).thenReturn(mockResponseResolveSnapshot);
		PowerMockito.when(mockResponseResolveSnapshot.readEntity(Snapshot.class)).thenReturn(mockedSnapshot);
		
		ClientServiceFacade.getInstance().setClientService(mockClientServiceImpl);
	}
	
	private void removeFixture() {
		final File generatedFixture = new File(SNAPSHOTS_LOCAL + "/generated_default.json");
		
		if (generatedFixture.exists()) {
			generatedFixture.delete();
		}
	}
	
	@Test
	public void shouldInvokeResolveSnapshotWithNoErrors() throws Exception {
		ClientServiceImpl clientService = new ClientServiceImpl();
		
		final AuthResponse authResponse = new AuthResponse();
		authResponse.setExp(SwitcherUtils.addTimeDuration("2s", new Date()).getTime()/1000);
		authResponse.setToken("123lkjsuoi23487skjfh28dskjn29");
		properties.put(ClientService.AUTH_RESPONSE, authResponse);
		
		WebTarget webTargetMock = PowerMockito.mock(WebTarget.class);
		Client clientMock = PowerMockito.mock(Client.class);
		Response responseMock = PowerMockito.mock(Response.class);
		Builder builderMock = PowerMockito.mock(Builder.class);

		PowerMockito.when(clientMock.target(String.format(ClientService.SNAPSHOT_URL, properties.get(SwitcherContextParam.URL)))).thenReturn(webTargetMock);
		PowerMockito.when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(builderMock);
		PowerMockito.when(builderMock.header(
				ClientService.HEADER_AUTHORIZATION, String.format(ClientService.TOKEN_TEXT, 
						((AuthResponse) properties.get(ClientService.AUTH_RESPONSE)).getToken()))).thenReturn(builderMock);
		
		final StringBuffer query = new StringBuffer();
		query.append("{\"query\":\"{ domain(name: \\\"%s\\\", environment: \\\"%s\\\") { ");
		query.append("name version description activated ");
		query.append("group { name description activated ");
		query.append("config { key description activated ");
		query.append("strategies { strategy activated operation values } ");
		query.append("components } } } }\"}");
		
		PowerMockito.when(builderMock.post(Entity.json(String.format(query.toString(), "switcher-domain", "generated_default"))))
			.thenReturn(responseMock);
		
		clientService.setClient(clientMock);
		
		clientService.resolveSnapshot(properties);
	}
	
	@Test(expected = SwitcherAPIConnectionException.class)
	public void shouldInvokeResolveSnapshotWithErrors() throws Exception {
		ClientServiceImpl clientService = new ClientServiceImpl();
		
		final AuthResponse authResponse = new AuthResponse();
		authResponse.setExp(SwitcherUtils.addTimeDuration("2s", new Date()).getTime()/1000);
		authResponse.setToken("123lkjsuoi23487skjfh28dskjn29");
		properties.put(ClientService.AUTH_RESPONSE, authResponse);
		
		WebTarget webTargetMock = PowerMockito.mock(WebTarget.class);
		Client clientMock = PowerMockito.mock(Client.class);
		Builder builderMock = PowerMockito.mock(Builder.class);

		PowerMockito.when(clientMock.target(String.format(ClientService.SNAPSHOT_URL, properties.get(SwitcherContextParam.URL)))).thenReturn(webTargetMock);
		PowerMockito.when(webTargetMock.request(MediaType.APPLICATION_JSON)).thenReturn(builderMock);
		PowerMockito.when(builderMock.header(
				ClientService.HEADER_AUTHORIZATION, String.format(ClientService.TOKEN_TEXT, 
						((AuthResponse) properties.get(ClientService.AUTH_RESPONSE)).getToken()))).thenReturn(builderMock);
		
		final StringBuffer query = new StringBuffer();
		query.append("{\"query\":\"{ domain(name: \\\"%s\\\", environment: \\\"%s\\\") { ");
		query.append("name version description activated ");
		query.append("group { name description activated ");
		query.append("config { key description activated ");
		query.append("strategies { strategy activated operation values } ");
		query.append("components } } } }\"}");
		
		PowerMockito.when(builderMock.post(Entity.json(String.format(query.toString(), "switcher-domain", "generated_default"))))
			.thenThrow(ResponseProcessingException.class);
		clientService.setClient(clientMock);
		
		ClientServiceFacade.getInstance().setClientService(clientService);
		
		SwitcherFactory.buildContext(properties, true);
	}
	
	@Test
	public void offlineShouldLoadSnapshotFromAPIBeforeExecuting() throws Exception {
		this.generateLoaderMock(200);
		SwitcherFactory.buildContext(properties, true);
		final Switcher switcher = SwitcherFactory.getSwitcher("USECASE11");
		
		assertTrue(switcher.isItOn());
		this.removeFixture();
	}
	
	@Test
	public void shouldLoadDomainFromSnapshot() throws Exception {
		final Domain domain = SnapshotLoader.loadSnapshot(SNAPSHOTS_LOCAL + "/snapshot_fixture1.json");
		assertNotNull(domain);
		assertNotNull(domain.toString());
	}
	
	@Test(expected = SwitcherSnapshotLoadException.class)
	public void shouldNotLoadDomainFromDefectSnapshot_byFile() throws Exception {
		SnapshotLoader.loadSnapshot(SNAPSHOTS_LOCAL + "/defect_default.json");
	}
	
	@Test(expected = SwitcherSnapshotLoadException.class)
	public void shouldNotLoadDomainFromDefectSnapshot_byEnv() throws Exception {
		SnapshotLoader.loadSnapshot(SNAPSHOTS_LOCAL, "defect_default");
	}

}
