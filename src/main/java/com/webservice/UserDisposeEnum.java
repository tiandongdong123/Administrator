package com.webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for UserDisposeEnum.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="UserDisposeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Pwd"/>
 *     &lt;enumeration value="Ip"/>
 *     &lt;enumeration value="PwdAndIp"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "UserDisposeEnum")
@XmlEnum
public enum UserDisposeEnum {

	@XmlEnumValue("Pwd")
	PWD("Pwd"), @XmlEnumValue("Ip")
	IP("Ip"), @XmlEnumValue("PwdAndIp")
	PWD_AND_IP("PwdAndIp");
	private final String value;

	UserDisposeEnum(String v) {
		value = v;
	}

	public String value() {
		return value;
	}

	public static UserDisposeEnum fromValue(String v) {
		for (UserDisposeEnum c : UserDisposeEnum.values()) {
			if (c.value.equals(v)) {
				return c;
			}
		}
		throw new IllegalArgumentException(v);
	}

}
