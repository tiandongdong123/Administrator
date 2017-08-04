package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ContractTerm complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ContractTerm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ExtensionData" type="{http://tempuri.org/}ExtensionDataObject" minOccurs="0"/>
 *         &lt;element name="Field" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Logic" type="{http://tempuri.org/}Logic"/>
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="ValueType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Verb" type="{http://tempuri.org/}Verb"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContractTerm", propOrder = {
    "extensionData",
    "field",
    "logic",
    "value",
    "valueType",
    "verb"
})
public class ContractTerm {

    @XmlElement(name = "ExtensionData")
    protected ExtensionDataObject extensionData;
    @XmlElement(name = "Field")
    protected String field;
    @XmlElement(name = "Logic", required = true)
    @XmlSchemaType(name = "string")
    protected Logic logic;
    @XmlElement(name = "Value")
    protected Object value;
    @XmlElement(name = "ValueType")
    protected String valueType;
    @XmlElement(name = "Verb", required = true)
    @XmlSchemaType(name = "string")
    protected Verb verb;

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
     * ��ȡfield���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField() {
        return field;
    }

    /**
     * ����field���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField(String value) {
        this.field = value;
    }

    /**
     * ��ȡlogic���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Logic }
     *     
     */
    public Logic getLogic() {
        return logic;
    }

    /**
     * ����logic���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Logic }
     *     
     */
    public void setLogic(Logic value) {
        this.logic = value;
    }

    /**
     * ��ȡvalue���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getValue() {
        return value;
    }

    /**
     * ����value���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * ��ȡvalueType���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValueType() {
        return valueType;
    }

    /**
     * ����valueType���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValueType(String value) {
        this.valueType = value;
    }

    /**
     * ��ȡverb���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Verb }
     *     
     */
    public Verb getVerb() {
        return verb;
    }

    /**
     * ����verb���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Verb }
     *     
     */
    public void setVerb(Verb value) {
        this.verb = value;
    }

}
