/*
 * Copyright (C) 2012  Benjamin Boksa (http://www.boksa.de/)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package de.boksa.rt.test.rest;

import java.io.IOException;
import java.util.ResourceBundle;

import junit.framework.Assert;

import org.junit.Test;

import de.boksa.rt.rest.RTAuthClient;
import de.boksa.rt.rest.RTRESTClient;
import de.boksa.rt.rest.RTRESTResponse;

public class RTAuthClientTest {
	
	public RTAuthClient getClient() {
	    ResourceBundle bundle = ResourceBundle.getBundle("test");
	    
		return new RTAuthClient(bundle.getString("baseUrl"), bundle.getString("user"), bundle.getString("pass"));
	}
	
	@Test
	public void testAuthFailed() throws IOException {
		RTAuthClient client = getClient();
		RTRESTResponse response = client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
		Assert.assertEquals("Search fails because client not authenticated", 401l, response.getStatusCode().longValue());
	}
	
	@Test
	public void testLogin() throws IOException {
		RTAuthClient client = getClient();
		
		RTRESTResponse response = client.login();
		
		Assert.assertEquals("Login succeeded", 200l, response.getStatusCode().longValue());
				
		client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
		client.logout();
		client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
	}
	
	@Test
	public void testLogout() throws IOException {
		RTAuthClient client = getClient();
		
		RTRESTResponse response = client.login();
		Assert.assertEquals("Client successfully logged in", 200l, response.getStatusCode().longValue());
		
		response = client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
		Assert.assertEquals("Authenticated call was made", 200l, response.getStatusCode().longValue());

		response = client.logout();
		Assert.assertEquals("Client successfully logged out", 200l, response.getStatusCode().longValue());
		
		response = client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
		Assert.assertEquals("Search fails because client is logged out", 401l, response.getStatusCode().longValue());
	}

}