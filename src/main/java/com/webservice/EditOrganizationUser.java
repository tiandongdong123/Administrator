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
@XmlType(name = "", propOrder = { "user", "hasPerson" })
@XmlRootElement(name = "EditOrganizationUser")
public class EditOrganizationUser {

	protected WFUser user;
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
