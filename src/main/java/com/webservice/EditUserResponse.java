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
 *         &lt;element name="EditUserResult" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "editUserResult"
})
@XmlRootElement(name = "EditUserResponse")
public class EditUserResponse {

    @XmlElement(name = "EditUserResult")
    protected int editUserResult;

    /**
     * ��ȡeditUserResult���Ե�ֵ��
     * 
     */
    public int getEditUserResult() {
        return editUserResult;
    }

    /**
     * ����editUserResult���Ե�ֵ��
     * 
     */
    public void setEditUserResult(int value) {
        this.editUserResult = value;
    }

}
