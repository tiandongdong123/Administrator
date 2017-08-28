package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for WFContract complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WFContract">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransferIn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransferOut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExtensionData" type="{http://tempuri.org/}ExtensionDataObject" minOccurs="0"/>
 *         &lt;element name="Terms" type="{http://tempuri.org/}ArrayOfContractTerm" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WFContract", propOrder = { "transferIn", "transferOut", "extensionData", "terms" })
public class WFContract {

	@XmlElement(name = "TransferIn")
	protected String transferIn;
	@XmlElement(name = "TransferOut")
	protected String transferOut;
	@XmlElement(name = "ExtensionData")
	protected ExtensionDataObject extensionData;
	@XmlElement(name = "Terms")
	protected ArrayOfContractTerm terms;

	/**
	 * Gets the value of the transferIn property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransferIn() {
		return transferIn;
	}

	/**
	 * Sets the value of the transferIn property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransferIn(String value) {
		this.transferIn = value;
	}

	/**
	 * Gets the value of the transferOut property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTransferOut() {
		return transferOut;
	}

	/**
	 * Sets the value of the transferOut property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTransferOut(String value) {
		this.transferOut = value;
	}

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
	 * Gets the value of the terms property.
	 * 
	 * @return possible object is {@link ArrayOfContractTerm }
	 * 
	 */
	public ArrayOfContractTerm getTerms() {
		return terms;
	}

	/**
	 * Sets the value of the terms property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfContractTerm }
	 * 
	 */
	public void setTerms(ArrayOfContractTerm value) {
		this.terms = value;
	}

}
