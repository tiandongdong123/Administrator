package com.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfContractTerm complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfContractTerm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ContractTerm" type="{http://tempuri.org/}ContractTerm" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfContractTerm", propOrder = {
    "contractTerm"
})
public class ArrayOfContractTerm {

    @XmlElement(name = "ContractTerm", nillable = true)
    protected List<ContractTerm> contractTerm;

    /**
     * Gets the value of the contractTerm property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contractTerm property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContractTerm().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContractTerm }
     * 
     * 
     */
    public List<ContractTerm> getContractTerm() {
        if (contractTerm == null) {
            contractTerm = new ArrayList<ContractTerm>();
        }
        return this.contractTerm;
    }

}
