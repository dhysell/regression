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
 * <p>Java class for cpAddressReferenceTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cpAddressReferenceTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="education"/>
 *     &lt;enumeration value="former"/>
 *     &lt;enumeration value="mailing"/>
 *     &lt;enumeration value="residence"/>
 *     &lt;enumeration value="work"/>
 *     &lt;enumeration value="property"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cpAddressReferenceTypeEnum")
@XmlEnum
public enum CPAddressTypeEnum {

    @XmlEnumValue("education")
    EDUCATION("education"),
    @XmlEnumValue("former")
    FORMER("former"),
    @XmlEnumValue("mailing")
    MAILING("mailing"),
    @XmlEnumValue("residence")
    RESIDENCE("residence"),
    @XmlEnumValue("work")
    WORK("work"),
    @XmlEnumValue("property")
    PROPERTY("property");
    private final String value;

    CPAddressTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CPAddressTypeEnum fromValue(String v) {
        for (CPAddressTypeEnum c: CPAddressTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
