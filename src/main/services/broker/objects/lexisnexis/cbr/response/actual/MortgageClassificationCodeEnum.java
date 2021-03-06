//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mortgageClassificationCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="mortgageClassificationCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Prior Sales Information from Taxroll data"/>
 *     &lt;enumeration value="Current Sales Information from Taxroll data"/>
 *     &lt;enumeration value="Current Sales Information from Deed data"/>
 *     &lt;enumeration value="Prior Sales Information from Deed data"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "mortgageClassificationCode")
@XmlEnum
public enum MortgageClassificationCodeEnum {

    @XmlEnumValue("Prior Sales Information from Taxroll data")
    PRIOR_SALES_INFORMATION_FROM_TAXROLL_DATA("Prior Sales Information from Taxroll data"),
    @XmlEnumValue("Current Sales Information from Taxroll data")
    CURRENT_SALES_INFORMATION_FROM_TAXROLL_DATA("Current Sales Information from Taxroll data"),
    @XmlEnumValue("Current Sales Information from Deed data")
    CURRENT_SALES_INFORMATION_FROM_DEED_DATA("Current Sales Information from Deed data"),
    @XmlEnumValue("Prior Sales Information from Deed data")
    PRIOR_SALES_INFORMATION_FROM_DEED_DATA("Prior Sales Information from Deed data");
    private final String value;

    MortgageClassificationCodeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MortgageClassificationCodeEnum fromValue(String v) {
        for (MortgageClassificationCodeEnum c: MortgageClassificationCodeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
