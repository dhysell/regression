
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClaimPartyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ClaimPartyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="police_sheriff"/>
 *     &lt;enumeration value="court_magistrate"/>
 *     &lt;enumeration value="insurance"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ClaimPartyType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum ClaimPartyType {

    @XmlEnumValue("police_sheriff")
    POLICE_SHERIFF("police_sheriff"),
    @XmlEnumValue("court_magistrate")
    COURT_MAGISTRATE("court_magistrate"),
    @XmlEnumValue("insurance")
    INSURANCE("insurance"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    ClaimPartyType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClaimPartyType fromValue(String v) {
        for (ClaimPartyType c: ClaimPartyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
