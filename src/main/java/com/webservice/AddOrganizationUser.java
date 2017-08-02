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
 *         &lt;element name="contracts" type="{http://tempuri.org/}ArrayOfWFContract" minOccurs="0"/>
 *         &lt;element name="manages" type="{http://tempuri.org/}ArrayOfAccountIdMapping" minOccurs="0"/>
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
    "contracts",
    "manages"
})
@XmlRootElement(name = "AddOrganizationUser")
public class AddOrganizationUser {

    protected WFUser user;
    protected ArrayOfWFContract contracts;
    protected ArrayOfAccountIdMapping manages;

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
     * ��ȡcontracts���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfWFContract }
     *     
     */
    public ArrayOfWFContract getContracts() {
        return contracts;
    }

    /**
     * ����contracts���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfWFContract }
     *     
     */
    public void setContracts(ArrayOfWFContract value) {
        this.contracts = value;
    }

    /**
     * ��ȡmanages���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfAccountIdMapping }
     *     
     */
    public ArrayOfAccountIdMapping getManages() {
        return manages;
    }

    /**
     * ����manages���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfAccountIdMapping }
     *     
     */
    public void setManages(ArrayOfAccountIdMapping value) {
        this.manages = value;
    }

}
