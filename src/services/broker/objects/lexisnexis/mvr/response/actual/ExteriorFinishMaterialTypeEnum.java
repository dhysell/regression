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
 *     &lt;enumeration value="Aluminum Siding"/>
 *     &lt;enumeration value="Cedar Siding"/>
 *     &lt;enumeration value="Hardboard (Masonite)"/>
 *     &lt;enumeration value="Plywood Sheeting (T-111)"/>
 *     &lt;enumeration value="Redwood Siding"/>
 *     &lt;enumeration value="Steel (Galvanized or Painted)"/>
 *     &lt;enumeration value="Stucco"/>
 *     &lt;enumeration value="Veneer (Brick)"/>
 *     &lt;enumeration value="Veneer (Slate or Stone)"/>
 *     &lt;enumeration value="Vinyl Siding"/>
 *     &lt;enumeration value="Clap Board Siding"/>
 *     &lt;enumeration value="Veneer (Cut Brownstone)"/>
 *     &lt;enumeration value="Veneer (Brownstone Rubble)"/>
 *     &lt;enumeration value="Fiber Cement (Hardie Board)"/>
 *     &lt;enumeration value="Wood Shake"/>
 *     &lt;enumeration value="No Siding"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum ExteriorFinishMaterialTypeEnum {

    @XmlEnumValue("Aluminum Siding")
    ALUMINUM_SIDING("Aluminum Siding"),
    @XmlEnumValue("Cedar Siding")
    CEDAR_SIDING("Cedar Siding"),
    @XmlEnumValue("Hardboard (Masonite)")
    HARDBOARD_MASONITE("Hardboard (Masonite)"),
    @XmlEnumValue("Plywood Sheeting (T-111)")
    PLYWOOD_SHEETING_T_111("Plywood Sheeting (T-111)"),
    @XmlEnumValue("Redwood Siding")
    REDWOOD_SIDING("Redwood Siding"),
    @XmlEnumValue("Steel (Galvanized or Painted)")
    STEEL_GALVANIZED_OR_PAINTED("Steel (Galvanized or Painted)"),
    @XmlEnumValue("Stucco")
    STUCCO("Stucco"),
    @XmlEnumValue("Veneer (Brick)")
    VENEER_BRICK("Veneer (Brick)"),
    @XmlEnumValue("Veneer (Slate or Stone)")
    VENEER_SLATE_OR_STONE("Veneer (Slate or Stone)"),
    @XmlEnumValue("Vinyl Siding")
    VINYL_SIDING("Vinyl Siding"),
    @XmlEnumValue("Clap Board Siding")
    CLAP_BOARD_SIDING("Clap Board Siding"),
    @XmlEnumValue("Veneer (Cut Brownstone)")
    VENEER_CUT_BROWNSTONE("Veneer (Cut Brownstone)"),
    @XmlEnumValue("Veneer (Brownstone Rubble)")
    VENEER_BROWNSTONE_RUBBLE("Veneer (Brownstone Rubble)"),
    @XmlEnumValue("Fiber Cement (Hardie Board)")
    FIBER_CEMENT_HARDIE_BOARD("Fiber Cement (Hardie Board)"),
    @XmlEnumValue("Wood Shake")
    WOOD_SHAKE("Wood Shake"),
    @XmlEnumValue("No Siding")
    NO_SIDING("No Siding"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    ExteriorFinishMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExteriorFinishMaterialTypeEnum fromValue(String v) {
        for (ExteriorFinishMaterialTypeEnum c: ExteriorFinishMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}