//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for svc_additional_underwriting.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="svc_additional_underwriting">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="above abstract dismissed"/>
 *     &lt;enumeration value="nonchargeable violation"/>
 *     &lt;enumeration value="first violation not chargeable - ok with 2+ per hsehld"/>
 *     &lt;enumeration value="chargeable only with other permissible violation"/>
 *     &lt;enumeration value="out of state violation"/>
 *     &lt;enumeration value="four point violation"/>
 *     &lt;enumeration value="first offense not chargeable"/>
 *     &lt;enumeration value="chargeable if 3+ same violation with 12 months"/>
 *     &lt;enumeration value="out of state, first offense non-chargeable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "svc_additional_underwriting")
@XmlEnum
public enum MvrSvcAdditionalUnderwritingEnum {

    @XmlEnumValue("above abstract dismissed")
    ABOVE_ABSTRACT_DISMISSED("above abstract dismissed"),
    @XmlEnumValue("nonchargeable violation")
    NONCHARGEABLE_VIOLATION("nonchargeable violation"),
    @XmlEnumValue("first violation not chargeable - ok with 2+ per hsehld")
    FIRST_VIOLATION_NOT_CHARGEABLE_OK_WITH_2_PER_HSEHLD("first violation not chargeable - ok with 2+ per hsehld"),
    @XmlEnumValue("chargeable only with other permissible violation")
    CHARGEABLE_ONLY_WITH_OTHER_PERMISSIBLE_VIOLATION("chargeable only with other permissible violation"),
    @XmlEnumValue("out of state violation")
    OUT_OF_STATE_VIOLATION("out of state violation"),
    @XmlEnumValue("four point violation")
    FOUR_POINT_VIOLATION("four point violation"),
    @XmlEnumValue("first offense not chargeable")
    FIRST_OFFENSE_NOT_CHARGEABLE("first offense not chargeable"),
    @XmlEnumValue("chargeable if 3+ same violation with 12 months")
    CHARGEABLE_IF_3_SAME_VIOLATION_WITH_12_MONTHS("chargeable if 3+ same violation with 12 months"),
    @XmlEnumValue("out of state, first offense non-chargeable")
    OUT_OF_STATE_FIRST_OFFENSE_NON_CHARGEABLE("out of state, first offense non-chargeable");
    private final String value;

    MvrSvcAdditionalUnderwritingEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MvrSvcAdditionalUnderwritingEnum fromValue(String v) {
        for (MvrSvcAdditionalUnderwritingEnum c: MvrSvcAdditionalUnderwritingEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}