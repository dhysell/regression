//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ecoa_codes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ecoa_codes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Subject (Individual)"/>
 *     &lt;enumeration value="Joint"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Primary"/>
 *     &lt;enumeration value="Authorized User"/>
 *     &lt;enumeration value="Undesignated"/>
 *     &lt;enumeration value="On behalf of"/>
 *     &lt;enumeration value="Terminated"/>
 *     &lt;enumeration value="Maker"/>
 *     &lt;enumeration value="Co-maker"/>
 *     &lt;enumeration value="Shared"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ecoa_codes")
@XmlEnum
public enum CollectionsEcoaCodeEnum {

    @XmlEnumValue("Subject (Individual)")
    SUBJECT_INDIVIDUAL("Subject (Individual)"),
    @XmlEnumValue("Joint")
    JOINT("Joint"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Primary")
    PRIMARY("Primary"),
    @XmlEnumValue("Authorized User")
    AUTHORIZED_USER("Authorized User"),
    @XmlEnumValue("Undesignated")
    UNDESIGNATED("Undesignated"),
    @XmlEnumValue("On behalf of")
    ON_BEHALF_OF("On behalf of"),
    @XmlEnumValue("Terminated")
    TERMINATED("Terminated"),
    @XmlEnumValue("Maker")
    MAKER("Maker"),
    @XmlEnumValue("Co-maker")
    CO_MAKER("Co-maker"),
    @XmlEnumValue("Shared")
    SHARED("Shared"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    CollectionsEcoaCodeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CollectionsEcoaCodeEnum fromValue(String v) {
        for (CollectionsEcoaCodeEnum c: CollectionsEcoaCodeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}