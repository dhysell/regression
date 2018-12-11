
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PayMethodType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="PayMethodType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="check"/>
 *     &lt;enumeration value="eft"/>
 *     &lt;enumeration value="payroll"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "PayMethodType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum PayMethodType {

    @XmlEnumValue("check")
    CHECK("check"),
    @XmlEnumValue("eft")
    EFT("eft"),
    @XmlEnumValue("payroll")
    PAYROLL("payroll");
    private final String value;

    PayMethodType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PayMethodType fromValue(String v) {
        for (PayMethodType c: PayMethodType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
