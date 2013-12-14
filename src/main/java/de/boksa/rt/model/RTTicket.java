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
package de.boksa.rt.model;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/* Ticket fields as per http://requesttracker.wikia.com/wiki/REST#Ticket_Properties:
	id:
	Queue:
	Owner:
	Creator:
	Subject:
	Status:
	Priority:
	InitialPriority:
	FinalPriority:
	Requestors:
	Cc:
	AdminCc:
	Created:
	Starts:
	Started:
	Due:
	Resolved:
	Told:
	LastUpdated:
	TimeEstimated:
	TimeWorked:
	TimeLeft: 
*/

public class RTTicket extends RTTicketAbstractObject implements RTCustomFieldObject {

	private Long id;
	private String queue;
	private String owner;
	private String creator;
	private String subject;
	private String status;
	private Integer priority;
	private Integer initialPriority;
	private Integer finalPriority;
	private String requestors;
	private String cc;
	private String adminCc;
	private DateTime created;
	private DateTime starts;
	private DateTime started;
	private DateTime due;
	private DateTime resolved;
	private DateTime told;
	private DateTime lastUpdated;
	private Long timeWorked;
	private Long timeEstimated;
	private Long timeLeft;
	private Map<String,RTCustomField> customFields;
	
	public RTTicket() {
		this.customFields = new HashMap<String,RTCustomField>();
	}
	
	
	// getter and setter methods...
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getQueue() {
		return queue;
	}
	public void setQueue(String queue) {
		this.queue = queue;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	public Integer getInitialPriority() {
		return initialPriority;
	}
	public void setInitialPriority(Integer initialPriority) {
		this.initialPriority = initialPriority;
	}
	public Integer getFinalPriority() {
		return finalPriority;
	}
	public void setFinalPriority(Integer finalPriority) {
		this.finalPriority = finalPriority;
	}
	public String getRequestors() {
		return requestors;
	}
	public void setRequestors(String requestors) {
		this.requestors = requestors;
	}
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public String getAdminCc() {
		return adminCc;
	}
	public void setAdminCc(String adminCc) {
		this.adminCc = adminCc;
	}
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public DateTime getStarts() {
		return starts;
	}
	public void setStarts(DateTime starts) {
		this.starts = starts;
	}
	public DateTime getStarted() {
		return started;
	}
	public void setStarted(DateTime started) {
		this.started = started;
	}
	public DateTime getDue() {
		return due;
	}
	public void setDue(DateTime due) {
		this.due = due;
	}
	public DateTime getResolved() {
		return resolved;
	}
	public void setResolved(DateTime resolved) {
		this.resolved = resolved;
	}
	public DateTime getTold() {
		return told;
	}
	public void setTold(DateTime told) {
		this.told = told;
	}
	public DateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(DateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Long getTimeEstimated() {
		return timeEstimated;
	}
	public void setTimeEstimated(Long timeEstimated) {
		this.timeEstimated = timeEstimated;
	}
	public Long getTimeWorked() {
		return timeWorked;
	}
	public void setTimeWorked(Long timeWorked) {
		this.timeWorked = timeWorked;
	}
	public Long getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(Long timeLeft) {
		this.timeLeft = timeLeft;
	}
	public Map<String, RTCustomField> getCustomFields() {
		return customFields;
	}
	public void setCustomFields(Map<String, RTCustomField> customFields) {
		this.customFields = customFields;
	}

	// toString...
	@Override
	public String toString() {

		return "RTTicket [id=" + id
			+ ", Queue=" + queue
			+ ", Owner=" + owner
			+ ", Creator=" + creator
			+ ", Subject=" + subject
			+ ", Status=" + status
			+ ", Priority=" + priority
			+ ", InitialPriority=" + initialPriority
			+ ", FinalPriority=" + finalPriority
			+ ", Requestors=" + requestors
			+ ", Cc=" + cc
			+ ", AdminCc=" + adminCc
			+ ", Created=" + created
			+ ", Starts=" + starts
			+ ", Started=" + started
			+ ", Due=" + due
			+ ", Resolved=" + resolved
			+ ", Told=" + told
			+ ", LastUpdated=" + lastUpdated
			+ ", TimeWorked=" + timeWorked
			+ ", TimeEstimated=" + timeEstimated
			+ ", TimeLeft=" + timeLeft
			+ ", CustomFields=" + customFields + "]";
	}
	
	public String getNewTicketParams() {
		
	    StringBuilder params = new StringBuilder("id: ticket/new");
	    if (StringUtils.isNotEmpty(queue)) {
	        params.append("\nQueue: ").append(queue);
	    }
        if (StringUtils.isNotEmpty(requestors)) {
            params.append("\nRequestor: ").append(requestors);
        }
        if (StringUtils.isNotEmpty(subject)) {
            params.append("\nSubject: ").append(subject);
        }
        if (StringUtils.isNotEmpty(cc)) {
            params.append("\nCc: ").append(cc);
        }
        if (StringUtils.isNotEmpty(adminCc)) {
            params.append("\nAdminCc: ").append(adminCc);
        }
        if (StringUtils.isNotEmpty(owner)) {
            params.append("\nOwner: ").append(owner);
        }
        if (priority != null && priority > 0) {
            params.append("\nPriority: ").append(priority);
        }
        if (initialPriority != null && initialPriority > 0) {
            params.append("\nInitialPriority: ").append(initialPriority);
        }
        if (finalPriority != null && finalPriority > 0) {
            params.append("\nFinalPriority: ").append(finalPriority);
        }
        if (timeEstimated != null && timeEstimated > 0) {
            params.append("\nTimeEstimated: ").append(timeEstimated);
        }
        if (starts != null) {
            params.append("\nStarts: ").append(starts);
        }
        if (due != null) {
            params.append("\nDue: ").append(due);
        }
        
        for (String customFieldName : customFields.keySet()) {
            params.append("\n").append(customFieldName).append(": ")
                .append(customFields.get(customFieldName).getValue());
        }
            
        return params.toString();
	}
	
	@Override
	public void populate(Map<String, String> parameters) throws InvocationTargetException, IllegalAccessException {
		BeanUtils.populate(this, parameters);
	}
}
