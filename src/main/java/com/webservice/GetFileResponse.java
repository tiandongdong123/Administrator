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
 *         &lt;element name="GetFileResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "getFileResult", "result" })
@XmlRootElement(name = "GetFileResponse")
public class GetFileResponse {

	@XmlElement(name = "GetFileResult")
	protected boolean getFileResult;
	protected byte[] result;

	/**
	 * Gets the value of the getFileResult property.
	 * 
	 */
	public boolean isGetFileResult() {
		return getFileResult;
	}

	/**
	 * Sets the value of the getFileResult property.
	 * 
	 */
	public void setGetFileResult(boolean value) {
		this.getFileResult = value;
	}

	/**
	 * Gets the value of the result property.
	 * 
	 * @return possible object is byte[]
	 */
	public byte[] getResult() {
		return result;
	}

	/**
	 * Sets the value of the result property.
	 * 
	 * @param value
	 *            allowed object is byte[]
	 */
	public void setResult(byte[] value) {
		this.result = ((byte[]) value);
	}

}
