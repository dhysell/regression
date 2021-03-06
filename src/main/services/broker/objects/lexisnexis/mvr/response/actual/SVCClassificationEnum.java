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
 * <p>Java class for svc_classification.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="svc_classification">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Registration, Titling and Licensing"/>
 *     &lt;enumeration value="Financial Responsibilities and Accidents"/>
 *     &lt;enumeration value="General Moving Violations"/>
 *     &lt;enumeration value="Motorcycle Violations"/>
 *     &lt;enumeration value="Serious Offenses ( includes serious moving violations)"/>
 *     &lt;enumeration value="Equipment Violations"/>
 *     &lt;enumeration value="Parking Violations"/>
 *     &lt;enumeration value="Miscellaneous Violations"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "svc_classification")
@XmlEnum
public enum SVCClassificationEnum {

    @XmlEnumValue("Registration, Titling and Licensing")
    REGISTRATION_TITLING_AND_LICENSING("Registration, Titling and Licensing"),
    @XmlEnumValue("Financial Responsibilities and Accidents")
    FINANCIAL_RESPONSIBILITIES_AND_ACCIDENTS("Financial Responsibilities and Accidents"),
    @XmlEnumValue("General Moving Violations")
    GENERAL_MOVING_VIOLATIONS("General Moving Violations"),
    @XmlEnumValue("Motorcycle Violations")
    MOTORCYCLE_VIOLATIONS("Motorcycle Violations"),
    @XmlEnumValue("Serious Offenses ( includes serious moving violations)")
    SERIOUS_OFFENSES_INCLUDES_SERIOUS_MOVING_VIOLATIONS("Serious Offenses ( includes serious moving violations)"),
    @XmlEnumValue("Equipment Violations")
    EQUIPMENT_VIOLATIONS("Equipment Violations"),
    @XmlEnumValue("Parking Violations")
    PARKING_VIOLATIONS("Parking Violations"),
    @XmlEnumValue("Miscellaneous Violations")
    MISCELLANEOUS_VIOLATIONS("Miscellaneous Violations"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    SVCClassificationEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SVCClassificationEnum fromValue(String v) {
        for (SVCClassificationEnum c: SVCClassificationEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
