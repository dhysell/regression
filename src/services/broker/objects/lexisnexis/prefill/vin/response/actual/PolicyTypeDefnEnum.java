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
 * <p>Java class for policyTypeDefn.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="policyTypeDefn">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Personal Auto"/>
 *     &lt;enumeration value="Motorcycle"/>
 *     &lt;enumeration value="Motorhome"/>
 *     &lt;enumeration value="Homeowners"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "policyTypeDefn")
@XmlEnum
public enum PolicyTypeDefnEnum {

    @XmlEnumValue("Personal Auto")
    PERSONAL_AUTO("Personal Auto"),
    @XmlEnumValue("Motorcycle")
    MOTORCYCLE("Motorcycle"),
    @XmlEnumValue("Motorhome")
    MOTORHOME("Motorhome"),
    @XmlEnumValue("Homeowners")
    HOMEOWNERS("Homeowners");
    private final String value;

    PolicyTypeDefnEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PolicyTypeDefnEnum fromValue(String v) {
        for (PolicyTypeDefnEnum c: PolicyTypeDefnEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
