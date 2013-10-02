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
package de.boksa.rt.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;

import de.boksa.rt.model.RTTicket;

public abstract class RTRESTClient {

	public enum TicketSearchResponseFormat {
		IDONLY("i"), IDANDSUBJECT("s"), MULTILINE("l");

		private String formatString;

		private TicketSearchResponseFormat(String formatString) {
			this.formatString = formatString;
		}

		public String getFormatString() {
			return this.formatString;
		}
	}

	protected static final Pattern PATTERN_RESPONSE_BODY = Pattern.compile("^(.*) (\\d+) (.*)\n((.*\n)*)", Pattern.MULTILINE);
	protected static final String NO_MATCHING_RESULTS = "No matching results";

	private String restInterfaceBaseURL;
	private String username;
	private String password;

	public RTRESTClient(String restInterfaceBaseURL, String username, String password) {
		this.setRestInterfaceBaseURL(restInterfaceBaseURL);
		this.setUsername(username);
		this.setPassword(password);
	}

	public RTRESTResponse login() throws IOException {
		String url = String.format("user/%s", this.getUsername());

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("user", this.getUsername()));
		params.add(new BasicNameValuePair("pass", this.getPassword()));

		return this.getResponse(url, params);
	}

	public RTRESTResponse logout() throws IOException {
		return this.getResponse("logout");
	}

	public RTRESTResponse searchTickets(String query) throws IOException {
		return this.searchTickets(query, null, null);
	}

	public RTRESTResponse searchTickets(String query, String orderby) throws IOException {
		return this.searchTickets(query, orderby, null);
	}

	public RTRESTResponse searchTickets(String query, TicketSearchResponseFormat format) throws IOException {
		return this.searchTickets(query, null, format);
	}

	public RTRESTResponse searchTickets(String query, String orderby, TicketSearchResponseFormat format)
			throws IOException {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("query", query));

		if (orderby != null) {
			params.add(new BasicNameValuePair("orderby", orderby));
		}

		if (format != null) {
			params.add(new BasicNameValuePair("format", format.getFormatString()));
		}

		return this.getResponse("search/ticket", params);
	}

	public RTRESTResponse getTicket(Long ticketId) throws IOException {
		String url = "ticket/" + ticketId;
		return this.getResponse(url);
	}

	public RTRESTResponse getTicket(Long ticketId, String attribute) throws IOException {
		String url = "ticket/" + ticketId + "/" + attribute;

		return this.getResponse(url);
	}

	public RTRESTResponse getTicket(Long ticketId, String attribute, Long attributeId) throws IOException {
		String url = "ticket/" + ticketId + "/" + attribute + "/" + attributeId;

		return this.getResponse(url);
	}

	public RTRESTResponse newTicket(RTTicket ticket, String text) throws IOException {
		String url = "ticket/new";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String content = ticket.getNewTicketParams() + "\ntext: " + text;
		params.add(new BasicNameValuePair("content", content));

		return this.getResponse(url, params);
	}

	public RTRESTResponse commentOnTicket(RTTicket ticket, Map<String, String> parameters) throws IOException {
		String url = "ticket/" + ticket.getId() + "/comment";
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String content = "id: " + ticket.getId() + "\nAction: " + parameters.get("action") + "\nText: "
				+ parameters.get("text") + "\nAttachment: " + ""; // TODO

		if (parameters.get("action").equals("correspond")) {
			content += "\nCc: " + parameters.get("cc") + "\nBcc: " + parameters.get("bcc") + "\nTimeWorked: "
					+ parameters.get("timeworked");
		}
		params.add(new BasicNameValuePair("content", content));

		return this.getResponse(url, params);
	}

	public RTRESTResponse editTicket(RTTicket ticket, Map<String, String> updatedValues) throws IOException {
		String url = "ticket/" + ticket.getId() + "/edit";

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		String content = "";

		for (String key : updatedValues.keySet()) {
			content += key + ": " + updatedValues.get(key);
		}

		params.add(new BasicNameValuePair("content", content));

		return this.getResponse(url, params);
	}

	public RTRESTResponse getUser(String username) throws IOException {
		String url = "user/" + username;
		return this.getResponse(url);
	}

	public RTRESTResponse getUser(Long user_id) throws IOException {
		String url = "user/" + user_id;
		return this.getResponse(url);
	}

	protected Executor getRequestExecutor() {
		return Executor.newInstance();
	}

	protected RTRESTResponse getResponse(String url) throws IOException {
		return this.getResponse(url, new ArrayList<NameValuePair>());
	}

	protected RTRESTResponse getResponse(String url, List<NameValuePair> params) throws IOException {

		Request request = Request.Post(this.getRestInterfaceBaseURL() + url);

		UrlEncodedFormEntity postEntity = new UrlEncodedFormEntity(params, "UTF-8");
		postEntity.setContentType("application/x-www-form-urlencoded");

		request.body(postEntity);

		String responseBody = this.getRequestExecutor().execute(request).returnContent().asString();

		Matcher matcher = PATTERN_RESPONSE_BODY.matcher(responseBody);

		RTRESTResponse response = new RTRESTResponse();
		if (matcher.matches()) {
			String body = matcher.group(4).trim();
			// Check if response is 'No matching results'
			if (body.startsWith(NO_MATCHING_RESULTS)) {
				// Signal upper layers of no records are available by setting
				// response code to -1 and body to actual response from
				// endpoint
				response.setStatusCode(-1l);
				response.setStatusMessage(matcher.group(4).trim());
				return response;
			}
			response.setVersion(matcher.group(1));
			response.setStatusCode(Long.valueOf(matcher.group(2)));
			response.setStatusMessage(matcher.group(3));
			response.setBody(body);
			return response;
		} else {
			// Pattern didn't match - signal upper layers by setting response
			// code to -1
			response.setStatusCode(-1l);
			response.setStatusMessage("Response body contents - no match");
		}
		return response;
	}

	// getter and setter methods...
	public String getRestInterfaceBaseURL() {
		return restInterfaceBaseURL;
	}

	public void setRestInterfaceBaseURL(String restInterfaceBaseURL) {
		this.restInterfaceBaseURL = restInterfaceBaseURL;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
