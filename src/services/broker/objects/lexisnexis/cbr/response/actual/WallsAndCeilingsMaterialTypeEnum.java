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
 *     &lt;enumeration value="Drywall and Paint"/>
 *     &lt;enumeration value="Plaster and Paint"/>
 *     &lt;enumeration value="Paneled Walls"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum WallsAndCeilingsMaterialTypeEnum {

    @XmlEnumValue("Drywall and Paint")
    DRYWALL_AND_PAINT("Drywall and Paint"),
    @XmlEnumValue("Plaster and Paint")
    PLASTER_AND_PAINT("Plaster and Paint"),
    @XmlEnumValue("Paneled Walls")
    PANELED_WALLS("Paneled Walls"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    WallsAndCeilingsMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static WallsAndCeilingsMaterialTypeEnum fromValue(String v) {
        for (WallsAndCeilingsMaterialTypeEnum c: WallsAndCeilingsMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
