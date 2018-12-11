//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:09:30 PM MST 
//


package services.broker.objects.lexisnexis.prefill.vin.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for conditionCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="conditionCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Excellent"/>
 *     &lt;enumeration value="Good"/>
 *     &lt;enumeration value="Average"/>
 *     &lt;enumeration value="Fair"/>
 *     &lt;enumeration value="Poor"/>
 *     &lt;enumeration value="Not Applicable"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "conditionCodeType")
@XmlEnum
public enum ConditionCodeTypeEnum {

    @XmlEnumValue("Excellent")
    EXCELLENT("Excellent"),
    @XmlEnumValue("Good")
    GOOD("Good"),
    @XmlEnumValue("Average")
    AVERAGE("Average"),
    @XmlEnumValue("Fair")
    FAIR("Fair"),
    @XmlEnumValue("Poor")
    POOR("Poor"),
    @XmlEnumValue("Not Applicable")
    NOT_APPLICABLE("Not Applicable"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    ConditionCodeTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ConditionCodeTypeEnum fromValue(String v) {
        for (ConditionCodeTypeEnum c: ConditionCodeTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
