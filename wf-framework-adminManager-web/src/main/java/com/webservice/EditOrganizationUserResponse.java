package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="EditOrganizationUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "editOrganizationUserResult"
})
@XmlRootElement(name = "EditOrganizationUserResponse")
public class EditOrganizationUserResponse {

    @XmlElement(name = "EditOrganizationUserResult")
    protected int editOrganizationUserResult;

    /**
     * ��ȡeditOrganizationUserResult���Ե�ֵ��
     * 
     */
    public int getEditOrganizationUserResult() {
        return editOrganizationUserResult;
    }

    /**
     * ����editOrganizationUserResult���Ե�ֵ��
     * 
     */
    public void setEditOrganizationUserResult(int value) {
        this.editOrganizationUserResult = value;
    }

}
