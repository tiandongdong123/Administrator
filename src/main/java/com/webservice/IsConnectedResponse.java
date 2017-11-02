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
 *         &lt;element name="IsConnectedResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "isConnectedResult" })
@XmlRootElement(name = "IsConnectedResponse")
public class IsConnectedResponse {

	@XmlElement(name = "IsConnectedResult")
	protected boolean isConnectedResult;

	/**
	 * Gets the value of the isConnectedResult property.
	 * 
	 */
	public boolean isIsConnectedResult() {
		return isConnectedResult;
	}

	/**
	 * Sets the value of the isConnectedResult property.
	 * 
	 */
	public void setIsConnectedResult(boolean value) {
		this.isConnectedResult = value;
	}

}
