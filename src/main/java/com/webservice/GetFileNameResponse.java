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
 *         &lt;element name="GetFileNameResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getFileNameResult" })
@XmlRootElement(name = "GetFileNameResponse")
public class GetFileNameResponse {

	@XmlElement(name = "GetFileNameResult")
	protected String getFileNameResult;

	/**
	 * Gets the value of the getFileNameResult property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getGetFileNameResult() {
		return getFileNameResult;
	}

	/**
	 * Sets the value of the getFileNameResult property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setGetFileNameResult(String value) {
		this.getFileNameResult = value;
	}

}
