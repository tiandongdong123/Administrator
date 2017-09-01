package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for AccountIdMapping complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="AccountIdMapping">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="MappingId" type="{http://microsoft.com/wsdl/types/}guid"/>
 *         &lt;element name="Id" type="{http://tempuri.org/}AccountId" minOccurs="0"/>
 *         &lt;element name="RelatedId" type="{http://tempuri.org/}AccountId" minOccurs="0"/>
 *         &lt;element name="BeginTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountIdMapping", propOrder = { "mappingId", "id", "relatedId", "beginTime",
		"endTime" })
public class AccountIdMapping {

	@XmlElement(name = "MappingId", required = true)
	protected String mappingId;
	@XmlElement(name = "Id")
	protected AccountId id;
	@XmlElement(name = "RelatedId")
	protected AccountId relatedId;
	@XmlElement(name = "BeginTime", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar beginTime;
	@XmlElement(name = "EndTime", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar endTime;

	/**
	 * Gets the value of the mappingId property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMappingId() {
		return mappingId;
	}

	/**
	 * Sets the value of the mappingId property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMappingId(String value) {
		this.mappingId = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link AccountId }
	 * 
	 */
	public AccountId getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link AccountId }
	 * 
	 */
	public void setId(AccountId value) {
		this.id = value;
	}

	/**
	 * Gets the value of the relatedId property.
	 * 
	 * @return possible object is {@link AccountId }
	 * 
	 */
	public AccountId getRelatedId() {
		return relatedId;
	}

	/**
	 * Sets the value of the relatedId property.
	 * 
	 * @param value
	 *            allowed object is {@link AccountId }
	 * 
	 */
	public void setRelatedId(AccountId value) {
		this.relatedId = value;
	}

	/**
	 * Gets the value of the beginTime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getBeginTime() {
		return beginTime;
	}

	/**
	 * Sets the value of the beginTime property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setBeginTime(XMLGregorianCalendar value) {
		this.beginTime = value;
	}

	/**
	 * Gets the value of the endTime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getEndTime() {
		return endTime;
	}

	/**
	 * Sets the value of the endTime property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setEndTime(XMLGregorianCalendar value) {
		this.endTime = value;
	}

}
