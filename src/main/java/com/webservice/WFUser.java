package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>WFUser complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
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
@XmlType(name = "WFUser", propOrder = {
    "loginType",
    "wfipLimit",
    "authPhone",
    "extensionData",
    "address",
    "birthday",
    "comment",
    "commentSave",
    "company",
    "companyType",
    "countryCode",
    "creationDateTime",
    "educationLevel",
    "email",
    "idCardNumber",
    "incomeLevel",
    "isLocked",
    "isMale",
    "mobilePhone",
    "password",
    "passwordAnswer",
    "passwordQuestion",
    "phone",
    "postCode",
    "region",
    "userGroup",
    "userID",
    "userRealName",
    "vocationType"
})
public class WFUser {

    @XmlElement(name = "LoginType", required = true)
    @XmlSchemaType(name = "string")
    protected UserDisposeEnum loginType;
    @XmlElement(name = "WFIPLimit")
    protected ArrayOfWFIPLimit wfipLimit;
    @XmlElement(name = "AuthPhone")
    protected long authPhone;
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
    @XmlElement(name = "CompanyType")
    protected int companyType;
    @XmlElement(name = "CountryCode")
    protected int countryCode;
    @XmlElement(name = "CreationDateTime", required = true, nillable = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar creationDateTime;
    @XmlElement(name = "EducationLevel")
    protected int educationLevel;
    @XmlElement(name = "Email")
    protected String email;
    @XmlElement(name = "IDCardNumber")
    protected String idCardNumber;
    @XmlElement(name = "IncomeLevel")
    protected int incomeLevel;
    @XmlElement(name = "IsLocked")
    protected boolean isLocked;
    @XmlElement(name = "IsMale")
    protected boolean isMale;
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
    @XmlElement(name = "VocationType")
    protected int vocationType;

    /**
     * ��ȡloginType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link UserDisposeEnum }
     *     
     */
    public UserDisposeEnum getLoginType() {
        return loginType;
    }

    /**
     * ����loginType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link UserDisposeEnum }
     *     
     */
    public void setLoginType(UserDisposeEnum value) {
        this.loginType = value;
    }

    /**
     * ��ȡwfipLimit���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWFIPLimit }
     *     
     */
    public ArrayOfWFIPLimit getWFIPLimit() {
        return wfipLimit;
    }

    /**
     * ����wfipLimit���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWFIPLimit }
     *     
     */
    public void setWFIPLimit(ArrayOfWFIPLimit value) {
        this.wfipLimit = value;
    }

    /**
     * ��ȡauthPhone���Ե�ֵ��
     * 
     */
    public long getAuthPhone() {
        return authPhone;
    }

    /**
     * ����authPhone���Ե�ֵ��
     * 
     */
    public void setAuthPhone(long value) {
        this.authPhone = value;
    }

    /**
     * ��ȡextensionData���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ExtensionDataObject }
     *     
     */
    public ExtensionDataObject getExtensionData() {
        return extensionData;
    }

    /**
     * ����extensionData���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ExtensionDataObject }
     *     
     */
    public void setExtensionData(ExtensionDataObject value) {
        this.extensionData = value;
    }

    /**
     * ��ȡaddress���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * ����address���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * ��ȡbirthday���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getBirthday() {
        return birthday;
    }

    /**
     * ����birthday���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setBirthday(XMLGregorianCalendar value) {
        this.birthday = value;
    }

    /**
     * ��ȡcomment���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComment() {
        return comment;
    }

    /**
     * ����comment���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComment(String value) {
        this.comment = value;
    }

    /**
     * ��ȡcommentSave���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommentSave() {
        return commentSave;
    }

    /**
     * ����commentSave���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommentSave(String value) {
        this.commentSave = value;
    }

    /**
     * ��ȡcompany���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCompany() {
        return company;
    }

    /**
     * ����company���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCompany(String value) {
        this.company = value;
    }

    /**
     * ��ȡcompanyType���Ե�ֵ��
     * 
     */
    public int getCompanyType() {
        return companyType;
    }

    /**
     * ����companyType���Ե�ֵ��
     * 
     */
    public void setCompanyType(int value) {
        this.companyType = value;
    }

    /**
     * ��ȡcountryCode���Ե�ֵ��
     * 
     */
    public int getCountryCode() {
        return countryCode;
    }

    /**
     * ����countryCode���Ե�ֵ��
     * 
     */
    public void setCountryCode(int value) {
        this.countryCode = value;
    }

    /**
     * ��ȡcreationDateTime���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * ����creationDateTime���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreationDateTime(XMLGregorianCalendar value) {
        this.creationDateTime = value;
    }

    /**
     * ��ȡeducationLevel���Ե�ֵ��
     * 
     */
    public int getEducationLevel() {
        return educationLevel;
    }

    /**
     * ����educationLevel���Ե�ֵ��
     * 
     */
    public void setEducationLevel(int value) {
        this.educationLevel = value;
    }

    /**
     * ��ȡemail���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmail() {
        return email;
    }

    /**
     * ����email���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmail(String value) {
        this.email = value;
    }

    /**
     * ��ȡidCardNumber���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDCardNumber() {
        return idCardNumber;
    }

    /**
     * ����idCardNumber���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDCardNumber(String value) {
        this.idCardNumber = value;
    }

    /**
     * ��ȡincomeLevel���Ե�ֵ��
     * 
     */
    public int getIncomeLevel() {
        return incomeLevel;
    }

    /**
     * ����incomeLevel���Ե�ֵ��
     * 
     */
    public void setIncomeLevel(int value) {
        this.incomeLevel = value;
    }

    /**
     * ��ȡisLocked���Ե�ֵ��
     * 
     */
    public boolean isIsLocked() {
        return isLocked;
    }

    /**
     * ����isLocked���Ե�ֵ��
     * 
     */
    public void setIsLocked(boolean value) {
        this.isLocked = value;
    }

    /**
     * ��ȡisMale���Ե�ֵ��
     * 
     */
    public boolean isIsMale() {
        return isMale;
    }

    /**
     * ����isMale���Ե�ֵ��
     * 
     */
    public void setIsMale(boolean value) {
        this.isMale = value;
    }

    /**
     * ��ȡmobilePhone���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * ����mobilePhone���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobilePhone(String value) {
        this.mobilePhone = value;
    }

    /**
     * ��ȡpassword���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * ����password���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * ��ȡpasswordAnswer���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordAnswer() {
        return passwordAnswer;
    }

    /**
     * ����passwordAnswer���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordAnswer(String value) {
        this.passwordAnswer = value;
    }

    /**
     * ��ȡpasswordQuestion���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPasswordQuestion() {
        return passwordQuestion;
    }

    /**
     * ����passwordQuestion���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPasswordQuestion(String value) {
        this.passwordQuestion = value;
    }

    /**
     * ��ȡphone���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhone() {
        return phone;
    }

    /**
     * ����phone���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhone(String value) {
        this.phone = value;
    }

    /**
     * ��ȡpostCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * ����postCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostCode(String value) {
        this.postCode = value;
    }

    /**
     * ��ȡregion���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRegion() {
        return region;
    }

    /**
     * ����region���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRegion(String value) {
        this.region = value;
    }

    /**
     * ��ȡuserGroup���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserGroup() {
        return userGroup;
    }

    /**
     * ����userGroup���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserGroup(String value) {
        this.userGroup = value;
    }

    /**
     * ��ȡuserID���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * ����userID���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    /**
     * ��ȡuserRealName���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserRealName() {
        return userRealName;
    }

    /**
     * ����userRealName���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserRealName(String value) {
        this.userRealName = value;
    }

    /**
     * ��ȡvocationType���Ե�ֵ��
     * 
     */
    public int getVocationType() {
        return vocationType;
    }

    /**
     * ����vocationType���Ե�ֵ��
     * 
     */
    public void setVocationType(int value) {
        this.vocationType = value;
    }

}
