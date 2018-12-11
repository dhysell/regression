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
 * <p>Java class for influenceCodeType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="influenceCodeType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Proximity To Airport"/>
 *     &lt;enumeration value="Ocean View"/>
 *     &lt;enumeration value="View (Type Not Specified)"/>
 *     &lt;enumeration value="Waterfront - Canal"/>
 *     &lt;enumeration value="Contamination Site"/>
 *     &lt;enumeration value="Corner"/>
 *     &lt;enumeration value="Waterfront-Beach (Ocean, River or Lake)"/>
 *     &lt;enumeration value="Cul-de-sac"/>
 *     &lt;enumeration value="Freeway Proximity"/>
 *     &lt;enumeration value="High Traffic Area"/>
 *     &lt;enumeration value="Mountain View"/>
 *     &lt;enumeration value="Historical"/>
 *     &lt;enumeration value="Waterfront-Not Specified"/>
 *     &lt;enumeration value="Lake View"/>
 *     &lt;enumeration value="Major Street/thoroughfare"/>
 *     &lt;enumeration value="Proximity To Railroad"/>
 *     &lt;enumeration value="River View"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "influenceCodeType")
@XmlEnum
public enum InfluenceCodeTypeEnum {

    @XmlEnumValue("Proximity To Airport")
    PROXIMITY_TO_AIRPORT("Proximity To Airport"),
    @XmlEnumValue("Ocean View")
    OCEAN_VIEW("Ocean View"),
    @XmlEnumValue("View (Type Not Specified)")
    VIEW_TYPE_NOT_SPECIFIED("View (Type Not Specified)"),
    @XmlEnumValue("Waterfront - Canal")
    WATERFRONT_CANAL("Waterfront - Canal"),
    @XmlEnumValue("Contamination Site")
    CONTAMINATION_SITE("Contamination Site"),
    @XmlEnumValue("Corner")
    CORNER("Corner"),
    @XmlEnumValue("Waterfront-Beach (Ocean, River or Lake)")
    WATERFRONT_BEACH_OCEAN_RIVER_OR_LAKE("Waterfront-Beach (Ocean, River or Lake)"),
    @XmlEnumValue("Cul-de-sac")
    CUL_DE_SAC("Cul-de-sac"),
    @XmlEnumValue("Freeway Proximity")
    FREEWAY_PROXIMITY("Freeway Proximity"),
    @XmlEnumValue("High Traffic Area")
    HIGH_TRAFFIC_AREA("High Traffic Area"),
    @XmlEnumValue("Mountain View")
    MOUNTAIN_VIEW("Mountain View"),
    @XmlEnumValue("Historical")
    HISTORICAL("Historical"),
    @XmlEnumValue("Waterfront-Not Specified")
    WATERFRONT_NOT_SPECIFIED("Waterfront-Not Specified"),
    @XmlEnumValue("Lake View")
    LAKE_VIEW("Lake View"),
    @XmlEnumValue("Major Street/thoroughfare")
    MAJOR_STREET_THOROUGHFARE("Major Street/thoroughfare"),
    @XmlEnumValue("Proximity To Railroad")
    PROXIMITY_TO_RAILROAD("Proximity To Railroad"),
    @XmlEnumValue("River View")
    RIVER_VIEW("River View"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    InfluenceCodeTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InfluenceCodeTypeEnum fromValue(String v) {
        for (InfluenceCodeTypeEnum c: InfluenceCodeTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
