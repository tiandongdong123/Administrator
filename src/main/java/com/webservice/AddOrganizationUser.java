package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://tempuri.org/}WFUser" minOccurs="0"/>
 *         &lt;element name="contracts" type="{http://tempuri.org/}ArrayOfWFContract" minOccurs="0"/>
 *         &lt;element name="mappings" type="{http://tempuri.org/}ArrayOfAccountIdMapping" minOccurs="0"/>
 *         &lt;element name="settings" type="{http://tempuri.org/}ArrayOfUserSetting" minOccurs="0"/>
 *         &lt;element name="hasPerson" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "user", "contracts", "mappings", "settings", "hasPerson" })
@XmlRootElement(name = "AddOrganizationUser")
public class AddOrganizationUser {

	protected WFUser user;
	protected ArrayOfWFContract contracts;
	protected ArrayOfAccountIdMapping mappings;
	protected ArrayOfUserSetting settings;
	protected boolean hasPerson;

	/**
	 * Gets the value of the user property.
	 * 
	 * @return possible object is {@link WFUser }
	 * 
	 */
	public WFUser getUser() {
		return user;
	}

	/**
	 * Sets the value of the user property.
	 * 
	 * @param value
	 *            allowed object is {@link WFUser }
	 * 
	 */
	public void setUser(WFUser value) {
		this.user = value;
	}

	/**
	 * Gets the value of the contracts property.
	 * 
	 * @return possible object is {@link ArrayOfWFContract }
	 * 
	 */
	public ArrayOfWFContract getContracts() {
		return contracts;
	}

	/**
	 * Sets the value of the contracts property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfWFContract }
	 * 
	 */
	public void setContracts(ArrayOfWFContract value) {
		this.contracts = value;
	}

	/**
	 * Gets the value of the mappings property.
	 * 
	 * @return possible object is {@link ArrayOfAccountIdMapping }
	 * 
	 */
	public ArrayOfAccountIdMapping getMappings() {
		return mappings;
	}

	/**
	 * Sets the value of the mappings property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfAccountIdMapping }
	 * 
	 */
	public void setMappings(ArrayOfAccountIdMapping value) {
		this.mappings = value;
	}

	/**
	 * Gets the value of the settings property.
	 * 
	 * @return possible object is {@link ArrayOfUserSetting }
	 * 
	 */
	public ArrayOfUserSetting getSettings() {
		return settings;
	}

	/**
	 * Sets the value of the settings property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfUserSetting }
	 * 
	 */
	public void setSettings(ArrayOfUserSetting value) {
		this.settings = value;
	}

	/**
	 * Gets the value of the hasPerson property.
	 * 
	 */
	public boolean isHasPerson() {
		return hasPerson;
	}

	/**
	 * Sets the value of the hasPerson property.
	 * 
	 */
	public void setHasPerson(boolean value) {
		this.hasPerson = value;
	}

}
