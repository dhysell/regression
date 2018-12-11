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
 *     &lt;enumeration value="Concrete Tile"/>
 *     &lt;enumeration value="Asphalt Shingles"/>
 *     &lt;enumeration value="Asphalt Shingles High profile"/>
 *     &lt;enumeration value="Roofing - Wood Shake"/>
 *     &lt;enumeration value="Mission / Clay Tile"/>
 *     &lt;enumeration value="Rolled 90lb"/>
 *     &lt;enumeration value="Stone - Steel"/>
 *     &lt;enumeration value="Hot Mop - 3 ply w/rocks"/>
 *     &lt;enumeration value="Rubber"/>
 *     &lt;enumeration value="Slate"/>
 *     &lt;enumeration value="Copper Roof"/>
 *     &lt;enumeration value="Glazed Tile"/>
 *     &lt;enumeration value="Tin"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RoofingMaterialTypeEnum {

    @XmlEnumValue("Concrete Tile")
    CONCRETE_TILE("Concrete Tile"),
    @XmlEnumValue("Asphalt Shingles")
    ASPHALT_SHINGLES("Asphalt Shingles"),
    @XmlEnumValue("Asphalt Shingles High profile")
    ASPHALT_SHINGLES_HIGH_PROFILE("Asphalt Shingles High profile"),
    @XmlEnumValue("Roofing - Wood Shake")
    ROOFING_WOOD_SHAKE("Roofing - Wood Shake"),
    @XmlEnumValue("Mission / Clay Tile")
    MISSION_CLAY_TILE("Mission / Clay Tile"),
    @XmlEnumValue("Rolled 90lb")
    ROLLED_90_LB("Rolled 90lb"),
    @XmlEnumValue("Stone - Steel")
    STONE_STEEL("Stone - Steel"),
    @XmlEnumValue("Hot Mop - 3 ply w/rocks")
    HOT_MOP_3_PLY_W_ROCKS("Hot Mop - 3 ply w/rocks"),
    @XmlEnumValue("Rubber")
    RUBBER("Rubber"),
    @XmlEnumValue("Slate")
    SLATE("Slate"),
    @XmlEnumValue("Copper Roof")
    COPPER_ROOF("Copper Roof"),
    @XmlEnumValue("Glazed Tile")
    GLAZED_TILE("Glazed Tile"),
    @XmlEnumValue("Tin")
    TIN("Tin"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RoofingMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RoofingMaterialTypeEnum fromValue(String v) {
        for (RoofingMaterialTypeEnum c: RoofingMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
