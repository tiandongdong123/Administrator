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
 *         &lt;element name="UpdateNonAccountingUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "updateNonAccountingUserResult" })
@XmlRootElement(name = "UpdateNonAccountingUserResponse")
public class UpdateNonAccountingUserResponse {

	@XmlElement(name = "UpdateNonAccountingUserResult")
	protected int updateNonAccountingUserResult;

	/**
	 * Gets the value of the updateNonAccountingUserResult property.
	 * 
	 */
	public int getUpdateNonAccountingUserResult() {
		return updateNonAccountingUserResult;
	}

	/**
	 * Sets the value of the updateNonAccountingUserResult property.
	 * 
	 */
	public void setUpdateNonAccountingUserResult(int value) {
		this.updateNonAccountingUserResult = value;
	}

}
