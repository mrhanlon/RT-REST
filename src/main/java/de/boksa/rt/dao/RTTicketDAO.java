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

import java.util.List;
import java.util.Map;

import de.boksa.rt.model.RTTicket;
import de.boksa.rt.model.RTTicketAttachment;
import de.boksa.rt.model.RTTicketHistory;
import de.boksa.rt.model.RTTicketUser;

public interface RTTicketDAO {

	public boolean createNewTicket(RTTicket ticket, String text) throws Exception;
	public RTTicket findById(Long id) throws Exception;
	public RTTicketHistory findHistory(Long id) throws Exception;
	public List<RTTicketHistory> findHistory(RTTicket ticket) throws Exception;
	public RTTicketAttachment findAttachment(Long id) throws Exception;
	public List<RTTicketAttachment> findAttachment(RTTicket ticket) throws Exception;
	public RTTicketUser findUser(Long id) throws Exception;
	public RTTicketUser findUser(String username) throws Exception; //TODO testing
	public List<RTTicket> findByQuery(String query) throws Exception;
	public List<RTTicket> findByQuery(String query, String orderby) throws Exception;
	public boolean editTicket(RTTicket ticket, Map<String, String> parameters) throws Exception;
	public boolean commentOnTicket(RTTicket ticket, Map<String, String> parameters) throws Exception;
	
}
