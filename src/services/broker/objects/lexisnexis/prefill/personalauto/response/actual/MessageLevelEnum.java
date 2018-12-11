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
 * <p>Java class for messageLevel.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="messageLevel">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Error"/>
 *     &lt;enumeration value="Warning"/>
 *     &lt;enumeration value="Info"/>
 *     &lt;enumeration value="General"/>
 *     &lt;enumeration value="Specific Section"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "messageLevel")
@XmlEnum
public enum MessageLevelEnum {

    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Warning")
    WARNING("Warning"),
    @XmlEnumValue("Info")
    INFO("Info"),
    @XmlEnumValue("General")
    GENERAL("General"),
    @XmlEnumValue("Specific Section")
    SPECIFIC_SECTION("Specific Section");
    private final String value;

    MessageLevelEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageLevelEnum fromValue(String v) {
        for (MessageLevelEnum c: MessageLevelEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
