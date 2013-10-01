package de.boksa.rt.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

/**
 * 
 * @author carrie
 * 
 * 
 * id: user/<user-id>
 * Name: <...>
 * Password: ********
 * EmailAddress: <...>
 * RealName: <...>
 * Organization: <...>
 * Privileged: <...>
 * Disabled: <...>
 *
 */

public class RTTicketUser extends RTTicketAbstractObject implements RTCustomFieldObject {
	
	private Long id;
	private String name;
	private String emailAddress;
	private String realName;
	private String organization;
	private boolean privileged;
	private boolean disabled;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public boolean isPriviledged() {
		return privileged;
	}
	public void setPriviledged(boolean priviledged) {
		this.privileged = priviledged;
	}
	public boolean isDisabled() {
		return disabled;
	}
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	@Override
	public Map<String, RTCustomField> getCustomFields() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString() {

		return "RTTicketHistory [id=" + id
			+ ", name=" + name
			+ ", email=" + emailAddress
			+ ", real_name=" + realName
			+ ", organization=" + organization
			+ ", priviledged=" + privileged
			+ ", disabled=" + disabled
			+ "]";
	}
	
	@Override
	public void populate(Map<String, String> parameters) throws InvocationTargetException, IllegalAccessException {
		BeanUtils.populate(this, parameters);
	}
}
