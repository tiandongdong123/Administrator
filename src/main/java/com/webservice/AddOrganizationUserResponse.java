package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="AddOrganizationUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addOrganizationUserResult" })
@XmlRootElement(name = "AddOrganizationUserResponse")
public class AddOrganizationUserResponse {

	@XmlElement(name = "AddOrganizationUserResult")
	protected int addOrganizationUserResult;

	/**
	 * Gets the value of the addOrganizationUserResult property.
	 * 
	 */
	public int getAddOrganizationUserResult() {
		return addOrganizationUserResult;
	}

	/**
	 * Sets the value of the addOrganizationUserResult property.
	 * 
	 */
	public void setAddOrganizationUserResult(int value) {
		this.addOrganizationUserResult = value;
	}

}
