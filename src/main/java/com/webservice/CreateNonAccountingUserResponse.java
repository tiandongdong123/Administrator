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
 *         &lt;element name="CreateNonAccountingUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "createNonAccountingUserResult" })
@XmlRootElement(name = "CreateNonAccountingUserResponse")
public class CreateNonAccountingUserResponse {

	@XmlElement(name = "CreateNonAccountingUserResult")
	protected int createNonAccountingUserResult;

	/**
	 * Gets the value of the createNonAccountingUserResult property.
	 * 
	 */
	public int getCreateNonAccountingUserResult() {
		return createNonAccountingUserResult;
	}

	/**
	 * Sets the value of the createNonAccountingUserResult property.
	 * 
	 */
	public void setCreateNonAccountingUserResult(int value) {
		this.createNonAccountingUserResult = value;
	}

}
