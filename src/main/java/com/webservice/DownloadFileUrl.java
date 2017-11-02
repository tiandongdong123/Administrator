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
 *         &lt;element name="Unit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FileID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FileType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="PostDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Token" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "username", "unit", "userType", "fileID", "fileType", "postDate",
		"token" })
@XmlRootElement(name = "DownloadFileUrl")
public class DownloadFileUrl {

	@XmlElement(name = "Username")
	protected String username;
	@XmlElement(name = "Unit")
	protected String unit;
	@XmlElement(name = "UserType")
	protected String userType;
	@XmlElement(name = "FileID")
	protected String fileID;
	@XmlElement(name = "FileType")
	protected int fileType;
	@XmlElement(name = "PostDate", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar postDate;
	@XmlElement(name = "Token")
	protected String token;

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
	 * Gets the value of the fileID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getFileID() {
		return fileID;
	}

	/**
	 * Sets the value of the fileID property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFileID(String value) {
		this.fileID = value;
	}

	/**
	 * Gets the value of the fileType property.
	 * 
	 */
	public int getFileType() {
		return fileType;
	}

	/**
	 * Sets the value of the fileType property.
	 * 
	 */
	public void setFileType(int value) {
		this.fileType = value;
	}

	/**
	 * Gets the value of the postDate property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getPostDate() {
		return postDate;
	}

	/**
	 * Sets the value of the postDate property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setPostDate(XMLGregorianCalendar value) {
		this.postDate = value;
	}

	/**
	 * Gets the value of the token property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the value of the token property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setToken(String value) {
		this.token = value;
	}

}
