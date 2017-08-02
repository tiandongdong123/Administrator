package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>WFContract complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="WFContract">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TransferIn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TransferOut" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ExtensionData" type="{http://tempuri.org/}ExtensionDataObject" minOccurs="0"/>
 *         &lt;element name="Terms" type="{http://tempuri.org/}ArrayOfContractTerm" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WFContract", propOrder = {
    "transferIn",
    "transferOut",
    "extensionData",
    "terms"
})
public class WFContract {

    @XmlElement(name = "TransferIn")
    protected String transferIn;
    @XmlElement(name = "TransferOut")
    protected String transferOut;
    @XmlElement(name = "ExtensionData")
    protected ExtensionDataObject extensionData;
    @XmlElement(name = "Terms")
    protected ArrayOfContractTerm terms;

    /**
     * ��ȡtransferIn���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferIn() {
        return transferIn;
    }

    /**
     * ����transferIn���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferIn(String value) {
        this.transferIn = value;
    }

    /**
     * ��ȡtransferOut���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransferOut() {
        return transferOut;
    }

    /**
     * ����transferOut���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransferOut(String value) {
        this.transferOut = value;
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
     * ��ȡterms���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfContractTerm }
     *     
     */
    public ArrayOfContractTerm getTerms() {
        return terms;
    }

    /**
     * ����terms���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfContractTerm }
     *     
     */
    public void setTerms(ArrayOfContractTerm value) {
        this.terms = value;
    }

}
