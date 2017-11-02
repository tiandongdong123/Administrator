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
 *         &lt;element name="ValidateNonAccountingUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "validateNonAccountingUserResult" })
@XmlRootElement(name = "ValidateNonAccountingUserResponse")
public class ValidateNonAccountingUserResponse {

	@XmlElement(name = "ValidateNonAccountingUserResult")
	protected int validateNonAccountingUserResult;

	/**
	 * Gets the value of the validateNonAccountingUserResult property.
	 * 
	 */
	public int getValidateNonAccountingUserResult() {
		return validateNonAccountingUserResult;
	}

	/**
	 * Sets the value of the validateNonAccountingUserResult property.
	 * 
	 */
	public void setValidateNonAccountingUserResult(int value) {
		this.validateNonAccountingUserResult = value;
	}

}
