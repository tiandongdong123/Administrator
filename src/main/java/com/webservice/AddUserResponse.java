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
 *         &lt;element name="AddUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "addUserResult"
})
@XmlRootElement(name = "AddUserResponse")
public class AddUserResponse {

    @XmlElement(name = "AddUserResult")
    protected int addUserResult;

    /**
     * ��ȡaddUserResult���Ե�ֵ��
     * 
     */
    public int getAddUserResult() {
        return addUserResult;
    }

    /**
     * ����addUserResult���Ե�ֵ��
     * 
     */
    public void setAddUserResult(int value) {
        this.addUserResult = value;
    }

}
