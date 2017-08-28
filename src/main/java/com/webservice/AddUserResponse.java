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
 *         &lt;element name="AddUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "addUserResult" })
@XmlRootElement(name = "AddUserResponse")
public class AddUserResponse {

	@XmlElement(name = "AddUserResult")
	protected int addUserResult;

	/**
	 * Gets the value of the addUserResult property.
	 * 
	 */
	public int getAddUserResult() {
		return addUserResult;
	}

	/**
	 * Sets the value of the addUserResult property.
	 * 
	 */
	public void setAddUserResult(int value) {
		this.addUserResult = value;
	}

}
