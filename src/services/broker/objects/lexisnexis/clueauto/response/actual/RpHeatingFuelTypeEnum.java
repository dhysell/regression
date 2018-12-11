//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for null.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType>
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Gas"/>
 *     &lt;enumeration value="Coal"/>
 *     &lt;enumeration value="Electric"/>
 *     &lt;enumeration value="Oil"/>
 *     &lt;enumeration value="Solar"/>
 *     &lt;enumeration value="Wood"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Propane"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpHeatingFuelTypeEnum {

    @XmlEnumValue("Gas")
    GAS("Gas"),
    @XmlEnumValue("Coal")
    COAL("Coal"),
    @XmlEnumValue("Electric")
    ELECTRIC("Electric"),
    @XmlEnumValue("Oil")
    OIL("Oil"),
    @XmlEnumValue("Solar")
    SOLAR("Solar"),
    @XmlEnumValue("Wood")
    WOOD("Wood"),
    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Propane")
    PROPANE("Propane"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpHeatingFuelTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpHeatingFuelTypeEnum fromValue(String v) {
        for (RpHeatingFuelTypeEnum c: RpHeatingFuelTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
