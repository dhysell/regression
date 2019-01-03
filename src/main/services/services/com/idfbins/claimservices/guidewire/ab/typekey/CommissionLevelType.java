
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CommissionLevelType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CommissionLevelType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="life_bonus"/>
 *     &lt;enumeration value="loss_ratio"/>
 *     &lt;enumeration value="retention"/>
 *     &lt;enumeration value="rolling_twelve"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CommissionLevelType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum CommissionLevelType {

    @XmlEnumValue("life_bonus")
    LIFE_BONUS("life_bonus"),
    @XmlEnumValue("loss_ratio")
    LOSS_RATIO("loss_ratio"),
    @XmlEnumValue("retention")
    RETENTION("retention"),
    @XmlEnumValue("rolling_twelve")
    ROLLING_TWELVE("rolling_twelve");
    private final String value;

    CommissionLevelType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CommissionLevelType fromValue(String v) {
        for (CommissionLevelType c: CommissionLevelType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
