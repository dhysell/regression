//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for participantRole.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="participantRole">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Owner"/>
 *     &lt;enumeration value="Driver"/>
 *     &lt;enumeration value="Passenger"/>
 *     &lt;enumeration value="Witness"/>
 *     &lt;enumeration value="Other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "participantRole")
@XmlEnum
public enum ParticipantRoleEnum {

    @XmlEnumValue("Owner")
    OWNER("Owner"),
    @XmlEnumValue("Driver")
    DRIVER("Driver"),
    @XmlEnumValue("Passenger")
    PASSENGER("Passenger"),
    @XmlEnumValue("Witness")
    WITNESS("Witness"),
    @XmlEnumValue("Other")
    OTHER("Other");
    private final String value;

    ParticipantRoleEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ParticipantRoleEnum fromValue(String v) {
        for (ParticipantRoleEnum c: ParticipantRoleEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
