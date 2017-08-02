package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>anonymous complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="user" type="{http://tempuri.org/}WFUser" minOccurs="0"/>
 *         &lt;element name="ifUupdatePhone" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "user",
    "ifUupdatePhone"
})
@XmlRootElement(name = "EditUser")
public class EditUser {

    protected WFUser user;
    protected boolean ifUupdatePhone;

    /**
     * ��ȡuser���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link WFUser }
     *     
     */
    public WFUser getUser() {
        return user;
    }

    /**
     * ����user���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link WFUser }
     *     
     */
    public void setUser(WFUser value) {
        this.user = value;
    }

    /**
     * ��ȡifUupdatePhone���Ե�ֵ��
     * 
     */
    public boolean isIfUupdatePhone() {
        return ifUupdatePhone;
    }

    /**
     * ����ifUupdatePhone���Ե�ֵ��
     * 
     */
    public void setIfUupdatePhone(boolean value) {
        this.ifUupdatePhone = value;
    }

}
