
package services.services.com.guidewire.crmservices.com.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PrimaryPhoneType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PrimaryPhoneType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="business"/>
 *     &lt;enumeration value="work"/>
 *     &lt;enumeration value="home"/>
 *     &lt;enumeration value="mobile"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PrimaryPhoneType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum PrimaryPhoneType {

    @XmlEnumValue("business")
    BUSINESS("business"),
    @XmlEnumValue("work")
    WORK("work"),
    @XmlEnumValue("home")
    HOME("home"),
    @XmlEnumValue("mobile")
    MOBILE("mobile");
    private final String value;

    PrimaryPhoneType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PrimaryPhoneType fromValue(String v) {
        for (PrimaryPhoneType c: PrimaryPhoneType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
