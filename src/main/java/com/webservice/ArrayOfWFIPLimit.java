package com.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfWFIPLimit complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfWFIPLimit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="WFIPLimit" type="{http://tempuri.org/}WFIPLimit" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfWFIPLimit", propOrder = {
    "wfipLimit"
})
public class ArrayOfWFIPLimit {

    @XmlElement(name = "WFIPLimit", nillable = true)
    protected List<WFIPLimit> wfipLimit;

    /**
     * Gets the value of the wfipLimit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wfipLimit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWFIPLimit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WFIPLimit }
     * 
     * 
     */
    public List<WFIPLimit> getWFIPLimit() {
        if (wfipLimit == null) {
            wfipLimit = new ArrayList<WFIPLimit>();
        }
        return this.wfipLimit;
    }

}
