package de.boksa.rt.model;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public abstract class RTTicketAbstractObject {
	Long id;
	Map<String, RTCustomField> customFields;
	
	public Map<String, RTCustomField> getCustomFields() {
		return customFields;
	}
	public void setCustomFields(Map<String, RTCustomField> customFields) {
		this.customFields = customFields;
	}
	
	public abstract void populate(Map<String, String> properties) throws InvocationTargetException, IllegalAccessException;
	
	public abstract String toString();

}
