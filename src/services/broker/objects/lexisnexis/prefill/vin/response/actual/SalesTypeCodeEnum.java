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
 * <p>Java class for salesTypeCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="salesTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Assignment Deed"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "salesTypeCode")
@XmlEnum
public enum SalesTypeCodeEnum {

    @XmlEnumValue("Assignment Deed")
    ASSIGNMENT_DEED("Assignment Deed"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    SalesTypeCodeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SalesTypeCodeEnum fromValue(String v) {
        for (SalesTypeCodeEnum c: SalesTypeCodeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
