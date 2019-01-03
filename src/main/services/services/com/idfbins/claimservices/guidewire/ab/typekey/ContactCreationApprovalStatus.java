
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactCreationApprovalStatus.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ContactCreationApprovalStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="approved"/>
 *     &lt;enumeration value="pending_approval"/>
 *     &lt;enumeration value="rejected"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ContactCreationApprovalStatus", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum ContactCreationApprovalStatus {

    @XmlEnumValue("approved")
    APPROVED("approved"),
    @XmlEnumValue("pending_approval")
    PENDING_APPROVAL("pending_approval"),
    @XmlEnumValue("rejected")
    REJECTED("rejected");
    private final String value;

    ContactCreationApprovalStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ContactCreationApprovalStatus fromValue(String v) {
        for (ContactCreationApprovalStatus c: ContactCreationApprovalStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
