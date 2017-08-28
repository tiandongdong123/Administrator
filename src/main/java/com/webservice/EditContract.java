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
 *         &lt;element name="contracts" type="{http://tempuri.org/}ArrayOfWFContract" minOccurs="0"/>
 *         &lt;element name="userId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "contracts", "userId" })
@XmlRootElement(name = "EditContract")
public class EditContract {

	protected ArrayOfWFContract contracts;
	protected String userId;

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
	 * Gets the value of the userId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the value of the userId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserId(String value) {
		this.userId = value;
	}

}
