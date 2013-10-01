/**
 * 
 */
package de.boksa.rt.rest;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * @author mrhanlon
 *
 */
public class RTRESTBasicAuthClient extends RTRESTClient {

	/**
	 * @param restInterfaceBaseURL
	 * @param username
	 * @param password
	 */
	public RTRESTBasicAuthClient(String restInterfaceBaseURL, String username, String password) {
		super(restInterfaceBaseURL, username, password);
	}
	
	@Override
	protected HttpContext getHttpContext(HttpHost targetHost) {
		BasicHttpContext context =  new BasicHttpContext();
		
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(targetHost, basicAuth);
		context.setAttribute(ClientContext.AUTH_CACHE, authCache);

		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		credsProvider.setCredentials(
				new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials(this.getUsername(), this.getPassword())
			);
		context.setAttribute(ClientContext.CREDS_PROVIDER, credsProvider);
		
		context.setAttribute(ClientPNames.HANDLE_AUTHENTICATION, true);
				
		return context;
	}


}
