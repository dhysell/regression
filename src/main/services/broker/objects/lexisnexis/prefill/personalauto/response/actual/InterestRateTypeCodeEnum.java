//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for interestRateType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="interestRateType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Adjustable"/>
 *     &lt;enumeration value="All Inclusive Deed Of Trust"/>
 *     &lt;enumeration value="Balloon"/>
 *     &lt;enumeration value="Fixed"/>
 *     &lt;enumeration value="Multiple Loan Amounts"/>
 *     &lt;enumeration value="Other"/>
 *     &lt;enumeration value="Variable"/>
 *     &lt;enumeration value="Variable/Adjustable Rate"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "interestRateType")
@XmlEnum
public enum InterestRateTypeCodeEnum {

    @XmlEnumValue("Adjustable")
    ADJUSTABLE("Adjustable"),
    @XmlEnumValue("All Inclusive Deed Of Trust")
    ALL_INCLUSIVE_DEED_OF_TRUST("All Inclusive Deed Of Trust"),
    @XmlEnumValue("Balloon")
    BALLOON("Balloon"),
    @XmlEnumValue("Fixed")
    FIXED("Fixed"),
    @XmlEnumValue("Multiple Loan Amounts")
    MULTIPLE_LOAN_AMOUNTS("Multiple Loan Amounts"),
    @XmlEnumValue("Other")
    OTHER("Other"),
    @XmlEnumValue("Variable")
    VARIABLE("Variable"),
    @XmlEnumValue("Variable/Adjustable Rate")
    VARIABLE_ADJUSTABLE_RATE("Variable/Adjustable Rate"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    InterestRateTypeCodeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static InterestRateTypeCodeEnum fromValue(String v) {
        for (InterestRateTypeCodeEnum c: InterestRateTypeCodeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
