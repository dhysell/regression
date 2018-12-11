//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

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
 *     &lt;enumeration value="Forced Air (Gas)"/>
 *     &lt;enumeration value="Forced Air (Oil)"/>
 *     &lt;enumeration value="Central Air Conditioning"/>
 *     &lt;enumeration value="No fuel used"/>
 *     &lt;enumeration value="Coal (Stove)"/>
 *     &lt;enumeration value="Wood (Stove)"/>
 *     &lt;enumeration value="Electricity (Heat Pump)"/>
 *     &lt;enumeration value="Solar energy"/>
 *     &lt;enumeration value="Hydronic (Water) Radiant Heat Floor"/>
 *     &lt;enumeration value="Radiant Baseboard heat"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum ClimateControlMaterialTypeEnum {

    @XmlEnumValue("Forced Air (Gas)")
    FORCED_AIR_GAS("Forced Air (Gas)"),
    @XmlEnumValue("Forced Air (Oil)")
    FORCED_AIR_OIL("Forced Air (Oil)"),
    @XmlEnumValue("Central Air Conditioning")
    CENTRAL_AIR_CONDITIONING("Central Air Conditioning"),
    @XmlEnumValue("No fuel used")
    NO_FUEL_USED("No fuel used"),
    @XmlEnumValue("Coal (Stove)")
    COAL_STOVE("Coal (Stove)"),
    @XmlEnumValue("Wood (Stove)")
    WOOD_STOVE("Wood (Stove)"),
    @XmlEnumValue("Electricity (Heat Pump)")
    ELECTRICITY_HEAT_PUMP("Electricity (Heat Pump)"),
    @XmlEnumValue("Solar energy")
    SOLAR_ENERGY("Solar energy"),
    @XmlEnumValue("Hydronic (Water) Radiant Heat Floor")
    HYDRONIC_WATER_RADIANT_HEAT_FLOOR("Hydronic (Water) Radiant Heat Floor"),
    @XmlEnumValue("Radiant Baseboard heat")
    RADIANT_BASEBOARD_HEAT("Radiant Baseboard heat"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    ClimateControlMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClimateControlMaterialTypeEnum fromValue(String v) {
        for (ClimateControlMaterialTypeEnum c: ClimateControlMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
