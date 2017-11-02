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
 *         &lt;element name="IsExistedNonAccountingUserResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "isExistedNonAccountingUserResult" })
@XmlRootElement(name = "IsExistedNonAccountingUserResponse")
public class IsExistedNonAccountingUserResponse {

	@XmlElement(name = "IsExistedNonAccountingUserResult")
	protected boolean isExistedNonAccountingUserResult;

	/**
	 * Gets the value of the isExistedNonAccountingUserResult property.
	 * 
	 */
	public boolean isIsExistedNonAccountingUserResult() {
		return isExistedNonAccountingUserResult;
	}

	/**
	 * Sets the value of the isExistedNonAccountingUserResult property.
	 * 
	 */
	public void setIsExistedNonAccountingUserResult(boolean value) {
		this.isExistedNonAccountingUserResult = value;
	}

}
