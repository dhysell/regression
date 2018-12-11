//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DispositionEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DispositionEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Open"/>
 *     &lt;enumeration value="Closed"/>
 *     &lt;enumeration value="Subrogation"/>
 *     &lt;enumeration value="Under Deductible"/>
 *     &lt;enumeration value="Peril Not Covered"/>
 *     &lt;enumeration value="Withdrawn"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DispositionEnum")
@XmlEnum
public enum DispositionEnum {

    @XmlEnumValue("Open")
    OPEN("Open"),
    @XmlEnumValue("Closed")
    CLOSED("Closed"),
    @XmlEnumValue("Subrogation")
    SUBROGATION("Subrogation"),
    @XmlEnumValue("Under Deductible")
    UNDER_DEDUCTIBLE("Under Deductible"),
    @XmlEnumValue("Peril Not Covered")
    PERIL_NOT_COVERED("Peril Not Covered"),
    @XmlEnumValue("Withdrawn")
    WITHDRAWN("Withdrawn");
    private final String value;

    DispositionEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DispositionEnum fromValue(String v) {
        for (DispositionEnum c: DispositionEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
