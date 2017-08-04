package com.webservice;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Verb�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * <p>
 * <pre>
 * &lt;simpleType name="Verb">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Equal"/>
 *     &lt;enumeration value="In"/>
 *     &lt;enumeration value="WithIn"/>
 *     &lt;enumeration value="LessThan"/>
 *     &lt;enumeration value="LessThanOrEqualTo"/>
 *     &lt;enumeration value="LargerThan"/>
 *     &lt;enumeration value="LargerThanOrEqualTo"/>
 *     &lt;enumeration value="Not"/>
 *     &lt;enumeration value="NotIn"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Verb")
@XmlEnum
public enum Verb {

    @XmlEnumValue("Equal")
    EQUAL("Equal"),
    @XmlEnumValue("In")
    IN("In"),
    @XmlEnumValue("WithIn")
    WITH_IN("WithIn"),
    @XmlEnumValue("LessThan")
    LESS_THAN("LessThan"),
    @XmlEnumValue("LessThanOrEqualTo")
    LESS_THAN_OR_EQUAL_TO("LessThanOrEqualTo"),
    @XmlEnumValue("LargerThan")
    LARGER_THAN("LargerThan"),
    @XmlEnumValue("LargerThanOrEqualTo")
    LARGER_THAN_OR_EQUAL_TO("LargerThanOrEqualTo"),
    @XmlEnumValue("Not")
    NOT("Not"),
    @XmlEnumValue("NotIn")
    NOT_IN("NotIn");
    private final String value;

    Verb(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Verb fromValue(String v) {
        for (Verb c: Verb.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
