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
 * <p>Java class for summary_account_codes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="summary_account_codes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Too New to Rate or Approved But Not Used"/>
 *     &lt;enumeration value="Pays Account as Agreed"/>
 *     &lt;enumeration value="Pays/Paid 30-60 Days or Max 2 Payments Past Due"/>
 *     &lt;enumeration value="Pays/Paid 60-90 Days or Max 3 Payments Past Due"/>
 *     &lt;enumeration value="Pays/Paid 90-120 Days or Max 4 Payments Past Due"/>
 *     &lt;enumeration value="Pays/Paid 120+ Days or 4+ Payments Past Due"/>
 *     &lt;enumeration value="Regular Payments Under Debtor Plan/Arrangement"/>
 *     &lt;enumeration value="Repossession"/>
 *     &lt;enumeration value="Bad Debt"/>
 *     &lt;enumeration value="Status Not Known"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "summary_account_codes")
@XmlEnum
public enum SummaryAccountTypesEnum {

    @XmlEnumValue("Too New to Rate or Approved But Not Used")
    TOO_NEW_TO_RATE_OR_APPROVED_BUT_NOT_USED("Too New to Rate or Approved But Not Used"),
    @XmlEnumValue("Pays Account as Agreed")
    PAYS_ACCOUNT_AS_AGREED("Pays Account as Agreed"),
    @XmlEnumValue("Pays/Paid 30-60 Days or Max 2 Payments Past Due")
    PAYS_PAID_30_60_DAYS_OR_MAX_2_PAYMENTS_PAST_DUE("Pays/Paid 30-60 Days or Max 2 Payments Past Due"),
    @XmlEnumValue("Pays/Paid 60-90 Days or Max 3 Payments Past Due")
    PAYS_PAID_60_90_DAYS_OR_MAX_3_PAYMENTS_PAST_DUE("Pays/Paid 60-90 Days or Max 3 Payments Past Due"),
    @XmlEnumValue("Pays/Paid 90-120 Days or Max 4 Payments Past Due")
    PAYS_PAID_90_120_DAYS_OR_MAX_4_PAYMENTS_PAST_DUE("Pays/Paid 90-120 Days or Max 4 Payments Past Due"),
    @XmlEnumValue("Pays/Paid 120+ Days or 4+ Payments Past Due")
    PAYS_PAID_120_DAYS_OR_4_PAYMENTS_PAST_DUE("Pays/Paid 120+ Days or 4+ Payments Past Due"),
    @XmlEnumValue("Regular Payments Under Debtor Plan/Arrangement")
    REGULAR_PAYMENTS_UNDER_DEBTOR_PLAN_ARRANGEMENT("Regular Payments Under Debtor Plan/Arrangement"),
    @XmlEnumValue("Repossession")
    REPOSSESSION("Repossession"),
    @XmlEnumValue("Bad Debt")
    BAD_DEBT("Bad Debt"),
    @XmlEnumValue("Status Not Known")
    STATUS_NOT_KNOWN("Status Not Known"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    SummaryAccountTypesEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static SummaryAccountTypesEnum fromValue(String v) {
        for (SummaryAccountTypesEnum c: SummaryAccountTypesEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
