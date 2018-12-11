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
 *     &lt;enumeration value="Asbestos"/>
 *     &lt;enumeration value="Built Up"/>
 *     &lt;enumeration value="Composition Shingle"/>
 *     &lt;enumeration value="Concrete"/>
 *     &lt;enumeration value="Metal"/>
 *     &lt;enumeration value="Slate"/>
 *     &lt;enumeration value="Gravel and Rock"/>
 *     &lt;enumeration value="Tar and Gravel"/>
 *     &lt;enumeration value="Bermuda"/>
 *     &lt;enumeration value="Built Up Metal"/>
 *     &lt;enumeration value="Aluminum"/>
 *     &lt;enumeration value="Wood Shake/Shingle"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Asphalt"/>
 *     &lt;enumeration value="Roll Composition"/>
 *     &lt;enumeration value="Steel"/>
 *     &lt;enumeration value="Tile"/>
 *     &lt;enumeration value="Urethane"/>
 *     &lt;enumeration value="Shingle(Not Wood)"/>
 *     &lt;enumeration value="Wood"/>
 *     &lt;enumeration value="Gypsum"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpRoofCoverTypeEnum {

    @XmlEnumValue("Asbestos")
    ASBESTOS("Asbestos"),
    @XmlEnumValue("Built Up")
    BUILT_UP("Built Up"),
    @XmlEnumValue("Composition Shingle")
    COMPOSITION_SHINGLE("Composition Shingle"),
    @XmlEnumValue("Concrete")
    CONCRETE("Concrete"),
    @XmlEnumValue("Metal")
    METAL("Metal"),
    @XmlEnumValue("Slate")
    SLATE("Slate"),
    @XmlEnumValue("Gravel and Rock")
    GRAVEL_AND_ROCK("Gravel and Rock"),
    @XmlEnumValue("Tar and Gravel")
    TAR_AND_GRAVEL("Tar and Gravel"),
    @XmlEnumValue("Bermuda")
    BERMUDA("Bermuda"),
    @XmlEnumValue("Built Up Metal")
    BUILT_UP_METAL("Built Up Metal"),
    @XmlEnumValue("Aluminum")
    ALUMINUM("Aluminum"),
    @XmlEnumValue("Wood Shake/Shingle")
    WOOD_SHAKE_SHINGLE("Wood Shake/Shingle"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Asphalt")
    ASPHALT("Asphalt"),
    @XmlEnumValue("Roll Composition")
    ROLL_COMPOSITION("Roll Composition"),
    @XmlEnumValue("Steel")
    STEEL("Steel"),
    @XmlEnumValue("Tile")
    TILE("Tile"),
    @XmlEnumValue("Urethane")
    URETHANE("Urethane"),
    @XmlEnumValue("Shingle(Not Wood)")
    SHINGLE_NOT_WOOD("Shingle(Not Wood)"),
    @XmlEnumValue("Wood")
    WOOD("Wood"),
    @XmlEnumValue("Gypsum")
    GYPSUM("Gypsum"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpRoofCoverTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpRoofCoverTypeEnum fromValue(String v) {
        for (RpRoofCoverTypeEnum c: RpRoofCoverTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
