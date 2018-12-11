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
 *     &lt;enumeration value="Frame"/>
 *     &lt;enumeration value="Adobe"/>
 *     &lt;enumeration value="Dome"/>
 *     &lt;enumeration value="Brick"/>
 *     &lt;enumeration value="Metal"/>
 *     &lt;enumeration value="Steel"/>
 *     &lt;enumeration value="Concrete Block"/>
 *     &lt;enumeration value="Wood"/>
 *     &lt;enumeration value="Mixed"/>
 *     &lt;enumeration value="Concrete"/>
 *     &lt;enumeration value="Masonry"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Heavy"/>
 *     &lt;enumeration value="Light"/>
 *     &lt;enumeration value="Log"/>
 *     &lt;enumeration value="Manufactured/Modular"/>
 *     &lt;enumeration value="Stone/Rock"/>
 *     &lt;enumeration value="Tilt-up"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpConstructionTypeEnum {

    @XmlEnumValue("Frame")
    FRAME("Frame"),
    @XmlEnumValue("Adobe")
    ADOBE("Adobe"),
    @XmlEnumValue("Dome")
    DOME("Dome"),
    @XmlEnumValue("Brick")
    BRICK("Brick"),
    @XmlEnumValue("Metal")
    METAL("Metal"),
    @XmlEnumValue("Steel")
    STEEL("Steel"),
    @XmlEnumValue("Concrete Block")
    CONCRETE_BLOCK("Concrete Block"),
    @XmlEnumValue("Wood")
    WOOD("Wood"),
    @XmlEnumValue("Mixed")
    MIXED("Mixed"),
    @XmlEnumValue("Concrete")
    CONCRETE("Concrete"),
    @XmlEnumValue("Masonry")
    MASONRY("Masonry"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Heavy")
    HEAVY("Heavy"),
    @XmlEnumValue("Light")
    LIGHT("Light"),
    @XmlEnumValue("Log")
    LOG("Log"),
    @XmlEnumValue("Manufactured/Modular")
    MANUFACTURED_MODULAR("Manufactured/Modular"),
    @XmlEnumValue("Stone/Rock")
    STONE_ROCK("Stone/Rock"),
    @XmlEnumValue("Tilt-up")
    TILT_UP("Tilt-up"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpConstructionTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpConstructionTypeEnum fromValue(String v) {
        for (RpConstructionTypeEnum c: RpConstructionTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
