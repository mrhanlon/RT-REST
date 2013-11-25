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
package de.boksa.rt.dao;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import de.boksa.rt.model.RTTicket;
import de.boksa.rt.model.RTTicketAttachment;
import de.boksa.rt.model.RTTicketHistory;
import de.boksa.rt.model.RTTicketUser;
import de.boksa.rt.rest.RTRESTClient;
import de.boksa.rt.rest.RTRESTResponse;
import de.boksa.rt.rest.response.parser.RTParser;
import de.boksa.rt.rest.response.parser.customconverters.StringToDateTimeConverter;

public class RESTRTTicketDAO implements RTTicketDAO {

    private static final Logger logger = Logger.getLogger(RESTRTTicketDAO.class);

    private RTRESTClient client;
    private StringToDateTimeConverter dateConverter;

    protected RESTRTTicketDAO() {
        ConvertUtils.deregister(DateTime.class);
        dateConverter = new StringToDateTimeConverter();
        ConvertUtils.register(dateConverter, DateTime.class);
    }

    @Override
    public boolean createNewTicket(RTTicket ticket, String text) throws Exception {
        Pattern PATTERN_TICKET_CREATED = Pattern.compile("^# Ticket (\\d+) created.$");
        client.login();
        RTRESTResponse response = client.newTicket(ticket, text);
        client.logout();

        Matcher m = PATTERN_TICKET_CREATED.matcher(response.getBody().trim());
        if (response.getStatusCode() == 200l && m.matches()) {
            long ticketId = Long.valueOf(m.group(1));
            ticket.setId(ticketId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public RTTicket getTicket(long ticketId) throws Exception {
        client.login();
        RTRESTResponse response = client.getTicket(ticketId);
        client.logout();

        return getTicket(response);
    }

    private RTTicket getTicket(RTRESTResponse response) throws InvocationTargetException, IllegalAccessException {
        RTParser parser = RTParser.getInstance();
        if (response.getStatusCode() == 200l) {
            List<Map<String, String>> attributes = parser.parseResponse(response);
            RTTicket ticket = new RTTicket();
            if (attributes.size() > 0) {
                Map<String, String> parameters = attributes.get(0);
                // RT returns ticket id as "ticket/#".  Remove the "ticket/" prefix.
                String id = parameters.get("id");
                if (id != null) {
                    parameters.put("id", id.replace("ticket/", ""));
                }
                ticket.populate(parameters);
            }
            return ticket;
        } else {
            return null;
        }
    }

    @Override
    public RTTicketHistory getHistory(long history_id) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<RTTicketHistory> findHistory(RTTicket ticket) throws Exception {
        client.login();
        RTRESTResponse response = client.getTicket(ticket.getId(), "history?format=l");
        client.logout();

        if (response.getStatusCode() == 200l) {
            RTParser parser = RTParser.getInstance();
            List<Map<String, String>> attributes = parser.parseResponse(response);
            List<RTTicketHistory> historyList = new ArrayList<RTTicketHistory>();
            for (Map<String, String> historyAttribute : attributes) {
                RTTicketHistory history = new RTTicketHistory();
                history.populate(historyAttribute);
                historyList.add(history);
            }
            return historyList;
        } else {
            return new ArrayList<RTTicketHistory>();
        }
    }

    @Override
    public List<RTTicketHistory> findHistory(long ticketId) throws Exception {
        client.login();
        RTRESTResponse response = client.getTicket(ticketId, "history?format=l");
        client.logout();

        if (response.getStatusCode() == 200l) {
            RTParser parser = RTParser.getInstance();
            List<Map<String, String>> attributes = parser.parseResponse(response);
            List<RTTicketHistory> historyList = new ArrayList<RTTicketHistory>();
            for (Map<String, String> historyAttribute : attributes) {
                RTTicketHistory history = new RTTicketHistory();
                history.populate(historyAttribute);
                historyList.add(history);
            }
            return historyList;
        } else {
            return new ArrayList<RTTicketHistory>();
        }
    }

    @Override
    public RTTicketAttachment findAttachment(long attachment_id) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public List<RTTicketAttachment> findAttachment(RTTicket ticket) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public RTTicketUser findUser(long user_id) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public RTTicketUser findUser(String username) throws Exception {
        client.login();
        RTRESTResponse response = client.getUser(username);
        client.logout();

        if (response.getStatusCode() == 200l) {
            RTParser parser = RTParser.getInstance();
            RTTicketUser user = new RTTicketUser();
            List<Map<String, String>> attributes = parser.parseResponse(response);
            for (Map<String, String> attribute : attributes) {
                BeanUtils.populate(user, attribute);
            }
            return user;
        } else {
            // No matches were found...just return an empty user instead of
            // bombing out like a dumb
            return new RTTicketUser();
        }
    }

    @Override
    public List<RTTicket> findByQuery(String query) throws Exception {
        return this.findByQuery(query, null);
    }

    @Override
    public List<RTTicket> findByQuery(String query, String orderby) throws IOException {
        client.login();
        List<RTTicket> tickets = new ArrayList<RTTicket>();
        
        try {
            RTRESTResponse response = client.searchTickets(query, orderby);
            if (response.getStatusCode() == 200l) {
                RTParser parser = RTParser.getInstance();
                List<Map<String, String>> parsedResponse = parser.parseResponse(response);
                
                for (Map<String, String> ticketData : parsedResponse) {
                    for (String ticketId : ticketData.keySet()) {
                        RTRESTResponse ticketResp = client.getTicket(Long.valueOf(ticketId));
                        RTTicket ticket = getTicket(ticketResp);
                        if (ticket != null) {
                            tickets.add(ticket);
                        } else {
                            logger.warn("unable to lookup ticket for id: " + ticketId);
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error(e);
        } catch (InvocationTargetException e) {
            logger.error(e);
        } catch (IllegalAccessException e) {
            logger.error(e);
        } finally {
            client.logout();
        }
        return tickets;
    }

    // getter and setter methods...
    public RTRESTClient getClient() {
        return client;
    }

    public void setClient(RTRESTClient client) {
        this.client = client;
    }

    /** This actually probably isn't necessary for our purposes but here it is **/
    @Override
    public boolean editTicket(RTTicket ticket, Map<String, String> parameters) throws Exception {
        client.login();
        RTRESTResponse response = client.editTicket(ticket, parameters);
        client.logout();

        if (response.getStatusCode() == 200l) {
            return true;
        } else {
            // failed!!
            return false;
        }
    }

    @Override
    public boolean commentOnTicket(RTTicket ticket, Map<String, String> parameters) throws Exception {
        client.login();
        RTRESTResponse response = client.commentOnTicket(ticket, parameters);
        client.logout();

        if (response.getStatusCode() == 200l) {
            return true;
        } else {
            // failed!!
            return false;
        }
    }
}
