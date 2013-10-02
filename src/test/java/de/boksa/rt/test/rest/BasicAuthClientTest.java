package de.boksa.rt.test.rest;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.http.client.HttpResponseException;
import org.junit.Test;

import de.boksa.rt.rest.BasicAuthClient;
import de.boksa.rt.rest.RTRESTClient;
import de.boksa.rt.rest.RTRESTResponse;

public class BasicAuthClientTest {
	
	public BasicAuthClient getClient() {
		return new BasicAuthClient("http://rt.easter-eggs.org/demos/stable/REST/1.0/", "john.foo", "john.foo");
	}
	
	@Test
	public void testAuthRequired() throws IOException {
		BasicAuthClient client = getClient();
		try {
			client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
		} catch (HttpResponseException e) {
			Assert.assertEquals("Search fails because client not authenticated", 401, e.getStatusCode());
		}
		
	}
	
	@Test
	public void testLoginLogout() throws IOException {
		BasicAuthClient client = getClient();
		
		client.login();
		
		RTRESTResponse response = client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
		Assert.assertEquals("Authenticated call succeeds; client successfully logged in", 200l, response.getStatusCode().longValue());

		client.logout();

		try {
			response = client.searchTickets("Queue = 'Customer Service'", RTRESTClient.TicketSearchResponseFormat.IDONLY);
			Assert.assertEquals("Search fails because client not authenticated", 401l, response.getStatusCode().longValue());
		} catch (HttpResponseException e) {
			Assert.assertEquals("Search fails because client not authenticated", 401, e.getStatusCode());
		}
	}

}
