package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * <p>
 * Java class for WFUser complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="WFUser">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="LoginType" type="{http://tempuri.org/}UserDisposeEnum"/>
 *         &lt;element name="WFIPLimit" type="{http://tempuri.org/}ArrayOfWFIPLimit" minOccurs="0"/>
 *         &lt;element name="AuthPhone" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ExtensionData" type="{http://tempuri.org/}ExtensionDataObject" minOccurs="0"/>
 *         &lt;element name="Address" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Birthday" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="Comment" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CommentSave" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Company" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CompanyType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CountryCode" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="CreationDateTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="EducationLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Email" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IDCardNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="IncomeLevel" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="IsLocked" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="IsMale" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="MobilePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PasswordAnswer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PasswordQuestion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Phone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Region" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserGroup" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UserRealName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VocationType" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WFUser", propOrder = { "loginType", "wfipLimit", "authPhone", "extensionData",
		"address", "birthday", "comment", "commentSave", "company", "companyType", "countryCode",
		"creationDateTime", "educationLevel", "email", "idCardNumber", "incomeLevel", "isLocked",
		"isMale", "mobilePhone", "password", "passwordAnswer", "passwordQuestion", "phone",
		"postCode", "region", "userGroup", "userID", "userRealName", "vocationType" })
public class WFUser {

	@XmlElement(name = "LoginType", required = true, nillable = true)
	protected UserDisposeEnum loginType;
	@XmlElement(name = "WFIPLimit")
	protected ArrayOfWFIPLimit wfipLimit;
	@XmlElement(name = "AuthPhone", required = true, type = Long.class, nillable = true)
	protected Long authPhone;
	@XmlElement(name = "ExtensionData")
	protected ExtensionDataObject extensionData;
	@XmlElement(name = "Address")
	protected String address;
	@XmlElement(name = "Birthday", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar birthday;
	@XmlElement(name = "Comment")
	protected String comment;
	@XmlElement(name = "CommentSave")
	protected String commentSave;
	@XmlElement(name = "Company")
	protected String company;
	@XmlElement(name = "CompanyType", required = true, type = Integer.class, nillable = true)
	protected Integer companyType;
	@XmlElement(name = "CountryCode", required = true, type = Integer.class, nillable = true)
	protected Integer countryCode;
	@XmlElement(name = "CreationDateTime", required = true, nillable = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar creationDateTime;
	@XmlElement(name = "EducationLevel", required = true, type = Integer.class, nillable = true)
	protected Integer educationLevel;
	@XmlElement(name = "Email")
	protected String email;
	@XmlElement(name = "IDCardNumber")
	protected String idCardNumber;
	@XmlElement(name = "IncomeLevel", required = true, type = Integer.class, nillable = true)
	protected Integer incomeLevel;
	@XmlElement(name = "IsLocked", required = true, type = Boolean.class, nillable = true)
	protected Boolean isLocked;
	@XmlElement(name = "IsMale", required = true, type = Boolean.class, nillable = true)
	protected Boolean isMale;
	@XmlElement(name = "MobilePhone")
	protected String mobilePhone;
	@XmlElement(name = "Password")
	protected String password;
	@XmlElement(name = "PasswordAnswer")
	protected String passwordAnswer;
	@XmlElement(name = "PasswordQuestion")
	protected String passwordQuestion;
	@XmlElement(name = "Phone")
	protected String phone;
	@XmlElement(name = "PostCode")
	protected String postCode;
	@XmlElement(name = "Region")
	protected String region;
	@XmlElement(name = "UserGroup")
	protected String userGroup;
	@XmlElement(name = "UserID")
	protected String userID;
	@XmlElement(name = "UserRealName")
	protected String userRealName;
	@XmlElement(name = "VocationType", required = true, type = Integer.class, nillable = true)
	protected Integer vocationType;

	/**
	 * Gets the value of the loginType property.
	 * 
	 * @return possible object is {@link UserDisposeEnum }
	 * 
	 */
	public UserDisposeEnum getLoginType() {
		return loginType;
	}

	/**
	 * Sets the value of the loginType property.
	 * 
	 * @param value
	 *            allowed object is {@link UserDisposeEnum }
	 * 
	 */
	public void setLoginType(UserDisposeEnum value) {
		this.loginType = value;
	}

	/**
	 * Gets the value of the wfipLimit property.
	 * 
	 * @return possible object is {@link ArrayOfWFIPLimit }
	 * 
	 */
	public ArrayOfWFIPLimit getWFIPLimit() {
		return wfipLimit;
	}

	/**
	 * Sets the value of the wfipLimit property.
	 * 
	 * @param value
	 *            allowed object is {@link ArrayOfWFIPLimit }
	 * 
	 */
	public void setWFIPLimit(ArrayOfWFIPLimit value) {
		this.wfipLimit = value;
	}

	/**
	 * Gets the value of the authPhone property.
	 * 
	 * @return possible object is {@link Long }
	 * 
	 */
	public Long getAuthPhone() {
		return authPhone;
	}

	/**
	 * Sets the value of the authPhone property.
	 * 
	 * @param value
	 *            allowed object is {@link Long }
	 * 
	 */
	public void setAuthPhone(Long value) {
		this.authPhone = value;
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
	 * Gets the value of the address property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets the value of the address property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setAddress(String value) {
		this.address = value;
	}

	/**
	 * Gets the value of the birthday property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getBirthday() {
		return birthday;
	}

	/**
	 * Sets the value of the birthday property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setBirthday(XMLGregorianCalendar value) {
		this.birthday = value;
	}

	/**
	 * Gets the value of the comment property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Sets the value of the comment property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setComment(String value) {
		this.comment = value;
	}

	/**
	 * Gets the value of the commentSave property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCommentSave() {
		return commentSave;
	}

	/**
	 * Sets the value of the commentSave property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCommentSave(String value) {
		this.commentSave = value;
	}

	/**
	 * Gets the value of the company property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Sets the value of the company property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setCompany(String value) {
		this.company = value;
	}

	/**
	 * Gets the value of the companyType property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getCompanyType() {
		return companyType;
	}

	/**
	 * Sets the value of the companyType property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setCompanyType(Integer value) {
		this.companyType = value;
	}

	/**
	 * Gets the value of the countryCode property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getCountryCode() {
		return countryCode;
	}

	/**
	 * Sets the value of the countryCode property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setCountryCode(Integer value) {
		this.countryCode = value;
	}

	/**
	 * Gets the value of the creationDateTime property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getCreationDateTime() {
		return creationDateTime;
	}

	/**
	 * Sets the value of the creationDateTime property.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setCreationDateTime(XMLGregorianCalendar value) {
		this.creationDateTime = value;
	}

	/**
	 * Gets the value of the educationLevel property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getEducationLevel() {
		return educationLevel;
	}

	/**
	 * Sets the value of the educationLevel property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setEducationLevel(Integer value) {
		this.educationLevel = value;
	}

	/**
	 * Gets the value of the email property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Sets the value of the email property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setEmail(String value) {
		this.email = value;
	}

	/**
	 * Gets the value of the idCardNumber property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getIDCardNumber() {
		return idCardNumber;
	}

	/**
	 * Sets the value of the idCardNumber property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setIDCardNumber(String value) {
		this.idCardNumber = value;
	}

	/**
	 * Gets the value of the incomeLevel property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getIncomeLevel() {
		return incomeLevel;
	}

	/**
	 * Sets the value of the incomeLevel property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setIncomeLevel(Integer value) {
		this.incomeLevel = value;
	}

	/**
	 * Gets the value of the isLocked property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isIsLocked() {
		return isLocked;
	}

	/**
	 * Sets the value of the isLocked property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setIsLocked(Boolean value) {
		this.isLocked = value;
	}

	/**
	 * Gets the value of the isMale property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isIsMale() {
		return isMale;
	}

	/**
	 * Sets the value of the isMale property.
	 * 
	 * @param value
	 *            allowed object is {@link Boolean }
	 * 
	 */
	public void setIsMale(Boolean value) {
		this.isMale = value;
	}

	/**
	 * Gets the value of the mobilePhone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * Sets the value of the mobilePhone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setMobilePhone(String value) {
		this.mobilePhone = value;
	}

	/**
	 * Gets the value of the password property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the value of the password property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPassword(String value) {
		this.password = value;
	}

	/**
	 * Gets the value of the passwordAnswer property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPasswordAnswer() {
		return passwordAnswer;
	}

	/**
	 * Sets the value of the passwordAnswer property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPasswordAnswer(String value) {
		this.passwordAnswer = value;
	}

	/**
	 * Gets the value of the passwordQuestion property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPasswordQuestion() {
		return passwordQuestion;
	}

	/**
	 * Sets the value of the passwordQuestion property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPasswordQuestion(String value) {
		this.passwordQuestion = value;
	}

	/**
	 * Gets the value of the phone property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Sets the value of the phone property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPhone(String value) {
		this.phone = value;
	}

	/**
	 * Gets the value of the postCode property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPostCode() {
		return postCode;
	}

	/**
	 * Sets the value of the postCode property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPostCode(String value) {
		this.postCode = value;
	}

	/**
	 * Gets the value of the region property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRegion() {
		return region;
	}

	/**
	 * Sets the value of the region property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setRegion(String value) {
		this.region = value;
	}

	/**
	 * Gets the value of the userGroup property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserGroup() {
		return userGroup;
	}

	/**
	 * Sets the value of the userGroup property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserGroup(String value) {
		this.userGroup = value;
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

	/**
	 * Gets the value of the userRealName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUserRealName() {
		return userRealName;
	}

	/**
	 * Sets the value of the userRealName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setUserRealName(String value) {
		this.userRealName = value;
	}

	/**
	 * Gets the value of the vocationType property.
	 * 
	 * @return possible object is {@link Integer }
	 * 
	 */
	public Integer getVocationType() {
		return vocationType;
	}

	/**
	 * Sets the value of the vocationType property.
	 * 
	 * @param value
	 *            allowed object is {@link Integer }
	 * 
	 */
	public void setVocationType(Integer value) {
		this.vocationType = value;
	}

}
