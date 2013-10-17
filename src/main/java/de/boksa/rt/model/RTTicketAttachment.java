package de.boksa.rt.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.joda.time.DateTime;

/**
 * 
 * @author carrie
 * 
 * id: <attachment-id>
 * Subject:
 * Creator: <user-id>
 * Created: <timestamp>
 * Transaction: <transaction-id>
 * Parent: <parent-id>
 * MessageId:
 * Filename: <filename>
 * ContentType: application/octet-stream
 * ContentEncoding: none
 *
 * Headers: MIME-Version: 1.0
 *        X-Mailer: MIME-tools 5.427 (Entity 5.427)
 *        Content-Type: application/octet-stream;
 *          name="<filename>"
 *        Content-Disposition: inline; filename="<filename>"
 *        Content-Transfer-Encoding: base64
 *        Content-Length: <length in bytes>
 *
 * Content:
 *
 */


public class RTTicketAttachment extends RTTicketAbstractObject implements RTCustomFieldObject {
	
	private Long id;
	private String subject;
	private Long creatorId;
	private DateTime created;
	private Long transactionId;
	private Long parentId;
	private Long messageId;
	private String filename;
	private String contentType;
	private String contentEncoding;
	private String headers;
	private String content;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public DateTime getCreated() {
		return created;
	}
	public void setCreated(DateTime created) {
		this.created = created;
	}
	public Long getTransationId() {
		return transactionId;
	}
	public void setTransationId(Long transationId) {
		this.transactionId = transationId;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getContentEncoding() {
		return contentEncoding;
	}
	public void setContentEncoding(String contentEncoding) {
		this.contentEncoding = contentEncoding;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public Map<String, RTCustomField> getCustomFields() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {

		return "RTTicketAttachment [id=" + id
			+ ", subject=" + subject
			+ ", created=" + created 
			+ ", creatorId=" + creatorId
			+ ", transactionId=" + transactionId
			+ ", parentId=" + parentId
			+ ", messageId=" + messageId
			+ ", filename=" + filename
			+ ", contentType=" + contentType
			+ ", contentEncoding=" + contentEncoding
			+ ", headers=" + headers
			+ ", content=" + content + "]";
	}
	
	@Override
	public void populate(Map<String, String> parameters) throws InvocationTargetException, IllegalAccessException {
		BeanUtils.populate(this, parameters);
	}
}
