package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for UserSetting complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="UserSetting">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PropertyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PropertyValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserSetting", propOrder = { "userType", "userId", "propertyName", "propertyValue" })
public class UserSetting {

	@XmlElement(name = "UserType")
	protected String userType;
	@XmlElement(name = "UserId")
	protected String userId;
	@XmlElement(name = "PropertyName")
	protected String propertyName;
	@XmlElement(name = "PropertyValue")
	protected String propertyValue;

	/**
	 * Gets the value of the userType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Sets the value of the userType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserType(String value) {
		this.userType = value;
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

	/**
	 * Gets the value of the propertyName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Sets the value of the propertyName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPropertyName(String value) {
		this.propertyName = value;
	}

	/**
	 * Gets the value of the propertyValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * Sets the value of the propertyValue property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPropertyValue(String value) {
		this.propertyValue = value;
	}

}
