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
 *     &lt;enumeration value="Central"/>
 *     &lt;enumeration value="Evaporative Cooler"/>
 *     &lt;enumeration value="Office Only"/>
 *     &lt;enumeration value="Window Unit"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Partial"/>
 *     &lt;enumeration value="Chilled Water"/>
 *     &lt;enumeration value="Refrigeration"/>
 *     &lt;enumeration value="Ventilation"/>
 *     &lt;enumeration value="Wall Unit"/>
 *     &lt;enumeration value="Type Unknown"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpAirConditioningTypeEnum {

    @XmlEnumValue("Central")
    CENTRAL("Central"),
    @XmlEnumValue("Evaporative Cooler")
    EVAPORATIVE_COOLER("Evaporative Cooler"),
    @XmlEnumValue("Office Only")
    OFFICE_ONLY("Office Only"),
    @XmlEnumValue("Window Unit")
    WINDOW_UNIT("Window Unit"),
    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Partial")
    PARTIAL("Partial"),
    @XmlEnumValue("Chilled Water")
    CHILLED_WATER("Chilled Water"),
    @XmlEnumValue("Refrigeration")
    REFRIGERATION("Refrigeration"),
    @XmlEnumValue("Ventilation")
    VENTILATION("Ventilation"),
    @XmlEnumValue("Wall Unit")
    WALL_UNIT("Wall Unit"),
    @XmlEnumValue("Type Unknown")
    TYPE_UNKNOWN("Type Unknown"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpAirConditioningTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpAirConditioningTypeEnum fromValue(String v) {
        for (RpAirConditioningTypeEnum c: RpAirConditioningTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
