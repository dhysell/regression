//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NameAssociationIndicatorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="NameAssociationIndicatorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Policy Holder"/>
 *     &lt;enumeration value="Vehicle Operator"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "NameAssociationIndicatorType")
@XmlEnum
public enum NameAssociationIndicatorEnum {

    @XmlEnumValue("Policy Holder")
    POLICY_HOLDER("Policy Holder"),
    @XmlEnumValue("Vehicle Operator")
    VEHICLE_OPERATOR("Vehicle Operator");
    private final String value;

    NameAssociationIndicatorEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static NameAssociationIndicatorEnum fromValue(String v) {
        for (NameAssociationIndicatorEnum c: NameAssociationIndicatorEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
