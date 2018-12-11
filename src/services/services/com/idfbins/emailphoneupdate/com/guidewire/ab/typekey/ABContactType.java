
package services.services.com.idfbins.emailphoneupdate.com.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ABContactType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ABContactType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="company"/>
 *     &lt;enumeration value="person"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ABContactType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum ABContactType {

    @XmlEnumValue("company")
    COMPANY("company"),
    @XmlEnumValue("person")
    PERSON("person");
    private final String value;

    ABContactType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ABContactType fromValue(String v) {
        for (ABContactType c: ABContactType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
