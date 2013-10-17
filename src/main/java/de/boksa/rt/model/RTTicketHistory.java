package de.boksa.rt.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;

/**
 * @author carrie
 * 
 * id: <history-id>
 * Ticket: <ticket-id>
 * TimeTaken: <...>
 * Type: <...>
 * Field: <...>
 * OldValue: <...>
 * NewValue: <...>
 * Data: <...>
 * Description: <...>
 *
 * Content: <lin1-0>
 *          <line-1>
 *          ...
 *          <line-n>
 *        
 * Creator: <...>
 * Created: <...>
 * Attachments: <...>
 *
 */
public class RTTicketHistory extends RTTicketAbstractObject implements RTCustomFieldObject {
	private Long id;
	private Long ticket;
	private Long timeTaken;
	private String type; // TODO maybe an enum?
	
	// TODO Mapping?
	private String field;
	private String oldValue;
	private String newValue;
	
	private String data;
	private String description;
	private String content;
	
	private String creator;
	
	private DateTime created;
	//TODO : make this thing work somehow
	//private List<RTTicketAttachment> attachments;
	private String attachments;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTicket() {
		return ticket;
	}
	public void setTicket(Long ticket) {
		this.ticket = ticket;
	}
	public Long getTimeTaken() {
		return timeTaken;
	}
	public void setTimeTaken(Long timeTaken) {
		this.timeTaken = timeTaken;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
	@Override
	public Map<String, RTCustomField> getCustomFields() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {

		return "RTTicketHistory [id=" + id
			+ ", ticket=" + ticket
			+ ", timeTaken=" + timeTaken
			+ ", type=" + type
			+ ", field=" + field
			+ ", oldValue=" + oldValue
			+ ", newValue=" + newValue
			+ ", data=" + data
			+ ", description=" + description
			+ ", content=" + content 
			+ ", created=" + created.toString()
			+ ", creator=" + creator
			//+ ", number of attachments=" + attachments.size()
			+ "]";
	}
	
	@Override
	public void populate(Map<String, String> parameters) throws InvocationTargetException, IllegalAccessException {
		BeanUtils.populate(this, parameters);
	}
}
