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
 *     &lt;enumeration value="One Opening"/>
 *     &lt;enumeration value="Type Unknown"/>
 *     &lt;enumeration value="Two Openings"/>
 *     &lt;enumeration value="Three Openings"/>
 *     &lt;enumeration value="Four Openings"/>
 *     &lt;enumeration value="Five Openings"/>
 *     &lt;enumeration value="Six Openings"/>
 *     &lt;enumeration value="Seven Openings"/>
 *     &lt;enumeration value="Eight Openings"/>
 *     &lt;enumeration value="Nine Openings"/>
 *     &lt;enumeration value="Wood"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpFireplaceTypeEnum {

    @XmlEnumValue("One Opening")
    ONE_OPENING("One Opening"),
    @XmlEnumValue("Type Unknown")
    TYPE_UNKNOWN("Type Unknown"),
    @XmlEnumValue("Two Openings")
    TWO_OPENINGS("Two Openings"),
    @XmlEnumValue("Three Openings")
    THREE_OPENINGS("Three Openings"),
    @XmlEnumValue("Four Openings")
    FOUR_OPENINGS("Four Openings"),
    @XmlEnumValue("Five Openings")
    FIVE_OPENINGS("Five Openings"),
    @XmlEnumValue("Six Openings")
    SIX_OPENINGS("Six Openings"),
    @XmlEnumValue("Seven Openings")
    SEVEN_OPENINGS("Seven Openings"),
    @XmlEnumValue("Eight Openings")
    EIGHT_OPENINGS("Eight Openings"),
    @XmlEnumValue("Nine Openings")
    NINE_OPENINGS("Nine Openings"),
    @XmlEnumValue("Wood")
    WOOD("Wood"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpFireplaceTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpFireplaceTypeEnum fromValue(String v) {
        for (RpFireplaceTypeEnum c: RpFireplaceTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
