
package services.services.com.guidewire.policyservices.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AssociatedPolicyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AssociatedPolicyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="bop"/>
 *     &lt;enumeration value="brokerage"/>
 *     &lt;enumeration value="cpp"/>
 *     &lt;enumeration value="squire"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AssociatedPolicyType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum AssociatedPolicyType {

    @XmlEnumValue("bop")
    BOP("bop"),
    @XmlEnumValue("brokerage")
    BROKERAGE("brokerage"),
    @XmlEnumValue("cpp")
    CPP("cpp"),
    @XmlEnumValue("squire")
    SQUIRE("squire");
    private final String value;

    AssociatedPolicyType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AssociatedPolicyType fromValue(String v) {
        for (AssociatedPolicyType c: AssociatedPolicyType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
