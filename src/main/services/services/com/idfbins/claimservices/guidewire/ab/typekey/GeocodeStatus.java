
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GeocodeStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="GeocodeStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="city"/>
 *     &lt;enumeration value="exact"/>
 *     &lt;enumeration value="failure"/>
 *     &lt;enumeration value="none"/>
 *     &lt;enumeration value="postalcode"/>
 *     &lt;enumeration value="street"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "GeocodeStatus", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum GeocodeStatus {

    @XmlEnumValue("city")
    CITY("city"),
    @XmlEnumValue("exact")
    EXACT("exact"),
    @XmlEnumValue("failure")
    FAILURE("failure"),
    @XmlEnumValue("none")
    NONE("none"),
    @XmlEnumValue("postalcode")
    POSTALCODE("postalcode"),
    @XmlEnumValue("street")
    STREET("street");
    private final String value;

    GeocodeStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GeocodeStatus fromValue(String v) {
        for (GeocodeStatus c: GeocodeStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
