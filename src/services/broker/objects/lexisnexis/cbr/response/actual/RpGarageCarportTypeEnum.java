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
 * <p>Java class for null.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType>
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Mixed"/>
 *     &lt;enumeration value="Attached Garage"/>
 *     &lt;enumeration value="Built-in Garage"/>
 *     &lt;enumeration value="Carport"/>
 *     &lt;enumeration value="Detached Garage"/>
 *     &lt;enumeration value="Pole"/>
 *     &lt;enumeration value="Offsite"/>
 *     &lt;enumeration value="Garage"/>
 *     &lt;enumeration value="Unimproved"/>
 *     &lt;enumeration value="Parking Lot"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Open"/>
 *     &lt;enumeration value="Paved/Surface"/>
 *     &lt;enumeration value="Ramp"/>
 *     &lt;enumeration value="Parking Structure"/>
 *     &lt;enumeration value="Tuckunder"/>
 *     &lt;enumeration value="Underground Basement"/>
 *     &lt;enumeration value="Covered"/>
 *     &lt;enumeration value="Yes"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpGarageCarportTypeEnum {

    @XmlEnumValue("Mixed")
    MIXED("Mixed"),
    @XmlEnumValue("Attached Garage")
    ATTACHED_GARAGE("Attached Garage"),
    @XmlEnumValue("Built-in Garage")
    BUILT_IN_GARAGE("Built-in Garage"),
    @XmlEnumValue("Carport")
    CARPORT("Carport"),
    @XmlEnumValue("Detached Garage")
    DETACHED_GARAGE("Detached Garage"),
    @XmlEnumValue("Pole")
    POLE("Pole"),
    @XmlEnumValue("Offsite")
    OFFSITE("Offsite"),
    @XmlEnumValue("Garage")
    GARAGE("Garage"),
    @XmlEnumValue("Unimproved")
    UNIMPROVED("Unimproved"),
    @XmlEnumValue("Parking Lot")
    PARKING_LOT("Parking Lot"),
    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Open")
    OPEN("Open"),
    @XmlEnumValue("Paved/Surface")
    PAVED_SURFACE("Paved/Surface"),
    @XmlEnumValue("Ramp")
    RAMP("Ramp"),
    @XmlEnumValue("Parking Structure")
    PARKING_STRUCTURE("Parking Structure"),
    @XmlEnumValue("Tuckunder")
    TUCKUNDER("Tuckunder"),
    @XmlEnumValue("Underground Basement")
    UNDERGROUND_BASEMENT("Underground Basement"),
    @XmlEnumValue("Covered")
    COVERED("Covered"),
    @XmlEnumValue("Yes")
    YES("Yes"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpGarageCarportTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpGarageCarportTypeEnum fromValue(String v) {
        for (RpGarageCarportTypeEnum c: RpGarageCarportTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}