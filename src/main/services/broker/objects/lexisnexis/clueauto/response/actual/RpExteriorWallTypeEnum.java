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
 *     &lt;enumeration value="Tilt-up/Concrete"/>
 *     &lt;enumeration value="Asbestos Shingle"/>
 *     &lt;enumeration value="Brick"/>
 *     &lt;enumeration value="Brick Veneer"/>
 *     &lt;enumeration value="Block"/>
 *     &lt;enumeration value="Composition"/>
 *     &lt;enumeration value="Concrete"/>
 *     &lt;enumeration value="Concrete Block"/>
 *     &lt;enumeration value="Glass"/>
 *     &lt;enumeration value="Log"/>
 *     &lt;enumeration value="Metal"/>
 *     &lt;enumeration value="Rock/Stone"/>
 *     &lt;enumeration value="Stucco"/>
 *     &lt;enumeration value="Tile"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Wood Shingle"/>
 *     &lt;enumeration value="Wood"/>
 *     &lt;enumeration value="Wood Siding"/>
 *     &lt;enumeration value="Aluminum/Vinyl Siding"/>
 *     &lt;enumeration value="Adobe"/>
 *     &lt;enumeration value="Combination"/>
 *     &lt;enumeration value="Shingle (Not Wood)"/>
 *     &lt;enumeration value="Marble"/>
 *     &lt;enumeration value="Masonry"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpExteriorWallTypeEnum {

    @XmlEnumValue("Tilt-up/Concrete")
    TILT_UP_CONCRETE("Tilt-up/Concrete"),
    @XmlEnumValue("Asbestos Shingle")
    ASBESTOS_SHINGLE("Asbestos Shingle"),
    @XmlEnumValue("Brick")
    BRICK("Brick"),
    @XmlEnumValue("Brick Veneer")
    BRICK_VENEER("Brick Veneer"),
    @XmlEnumValue("Block")
    BLOCK("Block"),
    @XmlEnumValue("Composition")
    COMPOSITION("Composition"),
    @XmlEnumValue("Concrete")
    CONCRETE("Concrete"),
    @XmlEnumValue("Concrete Block")
    CONCRETE_BLOCK("Concrete Block"),
    @XmlEnumValue("Glass")
    GLASS("Glass"),
    @XmlEnumValue("Log")
    LOG("Log"),
    @XmlEnumValue("Metal")
    METAL("Metal"),
    @XmlEnumValue("Rock/Stone")
    ROCK_STONE("Rock/Stone"),
    @XmlEnumValue("Stucco")
    STUCCO("Stucco"),
    @XmlEnumValue("Tile")
    TILE("Tile"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Wood Shingle")
    WOOD_SHINGLE("Wood Shingle"),
    @XmlEnumValue("Wood")
    WOOD("Wood"),
    @XmlEnumValue("Wood Siding")
    WOOD_SIDING("Wood Siding"),
    @XmlEnumValue("Aluminum/Vinyl Siding")
    ALUMINUM_VINYL_SIDING("Aluminum/Vinyl Siding"),
    @XmlEnumValue("Adobe")
    ADOBE("Adobe"),
    @XmlEnumValue("Combination")
    COMBINATION("Combination"),
    @XmlEnumValue("Shingle (Not Wood)")
    SHINGLE_NOT_WOOD("Shingle (Not Wood)"),
    @XmlEnumValue("Marble")
    MARBLE("Marble"),
    @XmlEnumValue("Masonry")
    MASONRY("Masonry"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpExteriorWallTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpExteriorWallTypeEnum fromValue(String v) {
        for (RpExteriorWallTypeEnum c: RpExteriorWallTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
