package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

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
 *         &lt;element name="Username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StartTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EndTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="rdptauth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="onlines" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="copys" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="prints" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="sigprint" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CLS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "username", "startTime", "endTime", "rdptauth", "onlines",
		"copys", "prints", "sigprint", "ip", "cls", "unit" })
@XmlRootElement(name = "ValidateNonAccountingUser")
public class ValidateNonAccountingUser {

	@XmlElement(name = "Username")
	protected String username;
	@XmlElement(name = "StartTime", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar startTime;
	@XmlElement(name = "EndTime", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar endTime;
	protected String rdptauth;
	protected int onlines;
	protected int copys;
	protected int prints;
	protected int sigprint;
	@XmlElement(name = "IP")
	protected String ip;
	@XmlElement(name = "CLS")
	protected String cls;
	@XmlElement(name = "Unit")
	protected String unit;

	/**
	 * Gets the value of the username property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the value of the username property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUsername(String value) {
		this.username = value;
	}

	/**
	 * Gets the value of the startTime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getStartTime() {
		return startTime;
	}

	/**
	 * Sets the value of the startTime property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setStartTime(XMLGregorianCalendar value) {
		this.startTime = value;
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

	/**
	 * Gets the value of the rdptauth property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRdptauth() {
		return rdptauth;
	}

	/**
	 * Sets the value of the rdptauth property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRdptauth(String value) {
		this.rdptauth = value;
	}

	/**
	 * Gets the value of the onlines property.
	 * 
	 */
	public int getOnlines() {
		return onlines;
	}

	/**
	 * Sets the value of the onlines property.
	 * 
	 */
	public void setOnlines(int value) {
		this.onlines = value;
	}

	/**
	 * Gets the value of the copys property.
	 * 
	 */
	public int getCopys() {
		return copys;
	}

	/**
	 * Sets the value of the copys property.
	 * 
	 */
	public void setCopys(int value) {
		this.copys = value;
	}

	/**
	 * Gets the value of the prints property.
	 * 
	 */
	public int getPrints() {
		return prints;
	}

	/**
	 * Sets the value of the prints property.
	 * 
	 */
	public void setPrints(int value) {
		this.prints = value;
	}

	/**
	 * Gets the value of the sigprint property.
	 * 
	 */
	public int getSigprint() {
		return sigprint;
	}

	/**
	 * Sets the value of the sigprint property.
	 * 
	 */
	public void setSigprint(int value) {
		this.sigprint = value;
	}

	/**
	 * Gets the value of the ip property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIP() {
		return ip;
	}

	/**
	 * Sets the value of the ip property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIP(String value) {
		this.ip = value;
	}

	/**
	 * Gets the value of the cls property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCLS() {
		return cls;
	}

	/**
	 * Sets the value of the cls property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCLS(String value) {
		this.cls = value;
	}

	/**
	 * Gets the value of the unit property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the value of the unit property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUnit(String value) {
		this.unit = value;
	}

}
