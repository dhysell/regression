//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for subjectTypeEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="subjectTypeEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Report Subject"/>
 *     &lt;enumeration value="Reference"/>
 *     &lt;enumeration value="Employee"/>
 *     &lt;enumeration value="Dependent"/>
 *     &lt;enumeration value="Spouse"/>
 *     &lt;enumeration value="Claimant - Policyholder"/>
 *     &lt;enumeration value="Claimant - Insured"/>
 *     &lt;enumeration value="Claimant - Third Party"/>
 *     &lt;enumeration value="Associated Policy"/>
 *     &lt;enumeration value="Claimant - Vehicle Operator"/>
 *     &lt;enumeration value="Claimant - Spouse"/>
 *     &lt;enumeration value="Claimant - Dependent"/>
 *     &lt;enumeration value="Claimant - Other"/>
 *     &lt;enumeration value="Alias (A/K/A)"/>
 *     &lt;enumeration value="Former Name"/>
 *     &lt;enumeration value="other"/>
 *     &lt;enumeration value="Policy Owner"/>
 *     &lt;enumeration value="Claimant"/>
 *     &lt;enumeration value="Insured"/>
 *     &lt;enumeration value="Policy Holder"/>
 *     &lt;enumeration value="Vehicle Operator"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "subjectTypeEnum")
@XmlEnum
public enum SubjectTypeEnum {

    @XmlEnumValue("Report Subject")
    REPORT_SUBJECT("Report Subject"),
    @XmlEnumValue("Reference")
    REFERENCE("Reference"),
    @XmlEnumValue("Employee")
    EMPLOYEE("Employee"),
    @XmlEnumValue("Dependent")
    DEPENDENT("Dependent"),
    @XmlEnumValue("Spouse")
    SPOUSE("Spouse"),
    @XmlEnumValue("Claimant - Policyholder")
    CLAIMANT_POLICYHOLDER("Claimant - Policyholder"),
    @XmlEnumValue("Claimant - Insured")
    CLAIMANT_INSURED("Claimant - Insured"),
    @XmlEnumValue("Claimant - Third Party")
    CLAIMANT_THIRD_PARTY("Claimant - Third Party"),
    @XmlEnumValue("Associated Policy")
    ASSOCIATED_POLICY("Associated Policy"),
    @XmlEnumValue("Claimant - Vehicle Operator")
    CLAIMANT_VEHICLE_OPERATOR("Claimant - Vehicle Operator"),
    @XmlEnumValue("Claimant - Spouse")
    CLAIMANT_SPOUSE("Claimant - Spouse"),
    @XmlEnumValue("Claimant - Dependent")
    CLAIMANT_DEPENDENT("Claimant - Dependent"),
    @XmlEnumValue("Claimant - Other")
    CLAIMANT_OTHER("Claimant - Other"),
    @XmlEnumValue("Alias (A/K/A)")
    ALIAS_A_K_A("Alias (A/K/A)"),
    @XmlEnumValue("Former Name")
    FORMER_NAME("Former Name"),
    @XmlEnumValue("other")
    OTHER("other"),
    @XmlEnumValue("Policy Owner")
    POLICY_OWNER("Policy Owner"),
    @XmlEnumValue("Claimant")
    CLAIMANT("Claimant"),
    @XmlEnumValue("Insured")
    INSURED("Insured"),
    @XmlEnumValue("Policy Holder")
    POLICY_HOLDER("Policy Holder"),
    @XmlEnumValue("Vehicle Operator")
    VEHICLE_OPERATOR("Vehicle Operator");
    private final String value;

    SubjectTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SubjectTypeEnum fromValue(String v) {
        for (SubjectTypeEnum c: SubjectTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
