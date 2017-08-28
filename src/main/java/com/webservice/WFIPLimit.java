package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for WFIPLimit complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WFIPLimit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExtensionData" type="{http://tempuri.org/}ExtensionDataObject" minOccurs="0"/>
 *         &lt;element name="BeginIPAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndIPAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WFIPLimit", propOrder = { "extensionData", "beginIPAddress", "endIPAddress", "id",
		"userID" })
public class WFIPLimit {

	@XmlElement(name = "ExtensionData")
	protected ExtensionDataObject extensionData;
	@XmlElement(name = "BeginIPAddress")
	protected String beginIPAddress;
	@XmlElement(name = "EndIPAddress")
	protected String endIPAddress;
	@XmlElement(name = "ID")
	protected long id;
	@XmlElement(name = "UserID")
	protected String userID;

	/**
	 * Gets the value of the extensionData property.
	 * 
	 * @return possible object is {@link ExtensionDataObject }
	 * 
	 */
	public ExtensionDataObject getExtensionData() {
		return extensionData;
	}

	/**
	 * Sets the value of the extensionData property.
	 * 
	 * @param value
	 *            allowed object is {@link ExtensionDataObject }
	 * 
	 */
	public void setExtensionData(ExtensionDataObject value) {
		this.extensionData = value;
	}

	/**
	 * Gets the value of the beginIPAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getBeginIPAddress() {
		return beginIPAddress;
	}

	/**
	 * Sets the value of the beginIPAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setBeginIPAddress(String value) {
		this.beginIPAddress = value;
	}

	/**
	 * Gets the value of the endIPAddress property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEndIPAddress() {
		return endIPAddress;
	}

	/**
	 * Sets the value of the endIPAddress property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEndIPAddress(String value) {
		this.endIPAddress = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 */
	public long getID() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 */
	public void setID(long value) {
		this.id = value;
	}

	/**
	 * Gets the value of the userID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserID() {
		return userID;
	}

	/**
	 * Sets the value of the userID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserID(String value) {
		this.userID = value;
	}

}
