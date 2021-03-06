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
 * <p>Java class for interested_parties_enum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="interested_parties_enum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Legal Owner"/>
 *     &lt;enumeration value="Lienholder"/>
 *     &lt;enumeration value="Leaseholder"/>
 *     &lt;enumeration value="Lessee"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "interested_parties_enum")
@XmlEnum
public enum InterestedPartiesEnum {

    @XmlEnumValue("Legal Owner")
    LEGAL_OWNER("Legal Owner"),
    @XmlEnumValue("Lienholder")
    LIENHOLDER("Lienholder"),
    @XmlEnumValue("Leaseholder")
    LEASEHOLDER("Leaseholder"),
    @XmlEnumValue("Lessee")
    LESSEE("Lessee");
    private final String value;

    InterestedPartiesEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InterestedPartiesEnum fromValue(String v) {
        for (InterestedPartiesEnum c: InterestedPartiesEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
