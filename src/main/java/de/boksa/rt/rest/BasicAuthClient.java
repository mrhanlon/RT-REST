/**
 * 
 */
package de.boksa.rt.rest;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Executor;

/**
 * @author mrhanlon
 *
 */
public class BasicAuthClient extends RTRESTClient {

	/**
	 * @param restInterfaceBaseURL
	 * @param username
	 * @param password
	 */
	public BasicAuthClient(String restInterfaceBaseURL, String username, String password) {
		super(restInterfaceBaseURL, username, password);
	}
	
	private Executor executor;
	
	@Override
	protected Executor getRequestExecutor() {
		if (executor == null) {
			executor = Executor.newInstance();
		}
		return executor;
	}
	
	@Override
	public RTRESTResponse login() throws IOException {
		URI uri;
		try {
			uri = new URI(this.getRestInterfaceBaseURL());
		} catch (URISyntaxException e) {
			throw new IOException(e);
		}
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort());
		getRequestExecutor()
			.auth(host, this.getUsername(), this.getPassword())
			.authPreemptive(host);
		
		return super.login();
	}
	
	@Override
	public RTRESTResponse logout() throws IOException {
		RTRESTResponse response = super.logout();
		
		getRequestExecutor().clearAuth().clearCookies();
		this.executor = null;

		return response;
	}


}
