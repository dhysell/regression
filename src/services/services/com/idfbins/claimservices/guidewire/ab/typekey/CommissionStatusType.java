
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommissionStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CommissionStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="a"/>
 *     &lt;enumeration value="aa"/>
 *     &lt;enumeration value="b"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CommissionStatusType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum CommissionStatusType {

    @XmlEnumValue("a")
    A("a"),
    @XmlEnumValue("aa")
    AA("aa"),
    @XmlEnumValue("b")
    B("b");
    private final String value;

    CommissionStatusType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CommissionStatusType fromValue(String v) {
        for (CommissionStatusType c: CommissionStatusType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
