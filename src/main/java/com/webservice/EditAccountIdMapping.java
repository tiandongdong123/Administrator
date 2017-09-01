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
 *         &lt;element name="mappings" type="{http://tempuri.org/}ArrayOfAccountIdMapping" minOccurs="0"/>
 *         &lt;element name="relatedUsers" type="{http://tempuri.org/}ArrayOfWFUser" minOccurs="0"/>
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
@XmlType(name = "", propOrder = { "mappings", "relatedUsers", "userId" })
@XmlRootElement(name = "EditAccountIdMapping")
public class EditAccountIdMapping {

	protected ArrayOfAccountIdMapping mappings;
	protected ArrayOfWFUser relatedUsers;
	protected String userId;

	/**
	 * Gets the value of the mappings property.
	 * 
	 * @return possible object is {@link ArrayOfAccountIdMapping }
	 * 
	 */
	public ArrayOfAccountIdMapping getMappings() {
		return mappings;
	}

	/**
	 * Sets the value of the mappings property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfAccountIdMapping }
	 * 
	 */
	public void setMappings(ArrayOfAccountIdMapping value) {
		this.mappings = value;
	}

	/**
	 * Gets the value of the relatedUsers property.
	 * 
	 * @return possible object is {@link ArrayOfWFUser }
	 * 
	 */
	public ArrayOfWFUser getRelatedUsers() {
		return relatedUsers;
	}

	/**
	 * Sets the value of the relatedUsers property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfWFUser }
	 * 
	 */
	public void setRelatedUsers(ArrayOfWFUser value) {
		this.relatedUsers = value;
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
