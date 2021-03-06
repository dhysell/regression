//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CPfsiType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CPfsiType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Field absent on inquiry and report"/>
 *     &lt;enumeration value="Close match"/>
 *     &lt;enumeration value="Discrepancy"/>
 *     &lt;enumeration value="Field absent on inquiry"/>
 *     &lt;enumeration value="Field absent on report"/>
 *     &lt;enumeration value="Match"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CPfsiType")
@XmlEnum
public enum CPFSIEnum {

    @XmlEnumValue("Field absent on inquiry and report")
    FIELD_ABSENT_ON_INQUIRY_AND_REPORT("Field absent on inquiry and report"),
    @XmlEnumValue("Close match")
    CLOSE_MATCH("Close match"),
    @XmlEnumValue("Discrepancy")
    DISCREPANCY("Discrepancy"),
    @XmlEnumValue("Field absent on inquiry")
    FIELD_ABSENT_ON_INQUIRY("Field absent on inquiry"),
    @XmlEnumValue("Field absent on report")
    FIELD_ABSENT_ON_REPORT("Field absent on report"),
    @XmlEnumValue("Match")
    MATCH("Match");
    private final String value;

    CPFSIEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CPFSIEnum fromValue(String v) {
        for (CPFSIEnum c: CPFSIEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
