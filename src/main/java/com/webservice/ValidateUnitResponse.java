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
 *         &lt;element name="ValidateUnitResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "validateUnitResult" })
@XmlRootElement(name = "ValidateUnitResponse")
public class ValidateUnitResponse {

	@XmlElement(name = "ValidateUnitResult")
	protected int validateUnitResult;

	/**
	 * Gets the value of the validateUnitResult property.
	 * 
	 */
	public int getValidateUnitResult() {
		return validateUnitResult;
	}

	/**
	 * Sets the value of the validateUnitResult property.
	 * 
	 */
	public void setValidateUnitResult(int value) {
		this.validateUnitResult = value;
	}

}
