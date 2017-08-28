package com.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ContractTerm complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
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
@XmlType(name = "ContractTerm", propOrder = { "extensionData", "field", "logic", "value",
		"valueType", "verb" })
public class ContractTerm {

	@XmlElement(name = "ExtensionData")
	protected ExtensionDataObject extensionData;
	@XmlElement(name = "Field")
	protected String field;
	@XmlElement(name = "Logic", required = true)
	protected Logic logic;
	@XmlElement(name = "Value")
	protected Object value;
	@XmlElement(name = "ValueType")
	protected String valueType;
	@XmlElement(name = "Verb", required = true)
	protected Verb verb;

	/**
	 * Gets the value of the extensionData property.
	 * 
	 * @return possible object is {@link ExtensionDataObject }
	 * 
	 */
	public ExtensionDataObject getExtensionData() {
		return extensionData;
	}

	/**
	 * Sets the value of the extensionData property.
	 * 
	 * @param value
	 *            allowed object is {@link ExtensionDataObject }
	 * 
	 */
	public void setExtensionData(ExtensionDataObject value) {
		this.extensionData = value;
	}

	/**
	 * Gets the value of the field property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getField() {
		return field;
	}

	/**
	 * Sets the value of the field property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setField(String value) {
		this.field = value;
	}

	/**
	 * Gets the value of the logic property.
	 * 
	 * @return possible object is {@link Logic }
	 * 
	 */
	public Logic getLogic() {
		return logic;
	}

	/**
	 * Sets the value of the logic property.
	 * 
	 * @param value
	 *            allowed object is {@link Logic }
	 * 
	 */
	public void setLogic(Logic value) {
		this.logic = value;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gets the value of the valueType property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValueType() {
		return valueType;
	}

	/**
	 * Sets the value of the valueType property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setValueType(String value) {
		this.valueType = value;
	}

	/**
	 * Gets the value of the verb property.
	 * 
	 * @return possible object is {@link Verb }
	 * 
	 */
	public Verb getVerb() {
		return verb;
	}

	/**
	 * Sets the value of the verb property.
	 * 
	 * @param value
	 *            allowed object is {@link Verb }
	 * 
	 */
	public void setVerb(Verb value) {
		this.verb = value;
	}

}
