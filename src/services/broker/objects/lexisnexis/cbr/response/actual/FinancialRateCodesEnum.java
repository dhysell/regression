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
 * <p>Java class for financial_rate_codes.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="financial_rate_codes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Too new to rate or approved but not used"/>
 *     &lt;enumeration value="Pays account as agreed"/>
 *     &lt;enumeration value="Not more than two payments past due"/>
 *     &lt;enumeration value="Not more than three payments past due"/>
 *     &lt;enumeration value="Not more than four payments past due"/>
 *     &lt;enumeration value="At least 120 days or more than four payments past due"/>
 *     &lt;enumeration value="Making regular payments under W.E.P."/>
 *     &lt;enumeration value="Repossession"/>
 *     &lt;enumeration value="Bad debt; placed for collection; skip"/>
 *     &lt;enumeration value="Account is inactive"/>
 *     &lt;enumeration value="Lost or stolen card "/>
 *     &lt;enumeration value="Contact member for status"/>
 *     &lt;enumeration value="Account refinanced or renewed"/>
 *     &lt;enumeration value="Consumer deceased"/>
 *     &lt;enumeration value="Included in financial counseling"/>
 *     &lt;enumeration value="Foreclosure proceedings started"/>
 *     &lt;enumeration value="In WEP of other party"/>
 *     &lt;enumeration value="Adjustment pending"/>
 *     &lt;enumeration value="Escrow account only"/>
 *     &lt;enumeration value="BMO need authorization to clear"/>
 *     &lt;enumeration value="Account included in CH13"/>
 *     &lt;enumeration value="Creditor declines to clear"/>
 *     &lt;enumeration value="No response from creditor"/>
 *     &lt;enumeration value="Dispute - resolution pending"/>
 *     &lt;enumeration value="Out-of-area trade"/>
 *     &lt;enumeration value="Unable to check without account number"/>
 *     &lt;enumeration value="Account not approved"/>
 *     &lt;enumeration value="Need consumer authorization"/>
 *     &lt;enumeration value="No record of account"/>
 *     &lt;enumeration value="Account included in bankruptcy"/>
 *     &lt;enumeration value="In bankruptcy / other party"/>
 *     &lt;enumeration value="Assigned to U.S. Dept of ED"/>
 *     &lt;enumeration value="Invalid / Unknown"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "financial_rate_codes")
@XmlEnum
public enum FinancialRateCodesEnum {

    @XmlEnumValue("Too new to rate or approved but not used")
    TOO_NEW_TO_RATE_OR_APPROVED_BUT_NOT_USED("Too new to rate or approved but not used"),
    @XmlEnumValue("Pays account as agreed")
    PAYS_ACCOUNT_AS_AGREED("Pays account as agreed"),
    @XmlEnumValue("Not more than two payments past due")
    NOT_MORE_THAN_TWO_PAYMENTS_PAST_DUE("Not more than two payments past due"),
    @XmlEnumValue("Not more than three payments past due")
    NOT_MORE_THAN_THREE_PAYMENTS_PAST_DUE("Not more than three payments past due"),
    @XmlEnumValue("Not more than four payments past due")
    NOT_MORE_THAN_FOUR_PAYMENTS_PAST_DUE("Not more than four payments past due"),
    @XmlEnumValue("At least 120 days or more than four payments past due")
    AT_LEAST_120_DAYS_OR_MORE_THAN_FOUR_PAYMENTS_PAST_DUE("At least 120 days or more than four payments past due"),
    @XmlEnumValue("Making regular payments under W.E.P.")
    MAKING_REGULAR_PAYMENTS_UNDER_W_E_P("Making regular payments under W.E.P."),
    @XmlEnumValue("Repossession")
    REPOSSESSION("Repossession"),
    @XmlEnumValue("Bad debt; placed for collection; skip")
    BAD_DEBT_PLACED_FOR_COLLECTION_SKIP("Bad debt; placed for collection; skip"),
    @XmlEnumValue("Account is inactive")
    ACCOUNT_IS_INACTIVE("Account is inactive"),
    @XmlEnumValue("Lost or stolen card ")
    LOST_OR_STOLEN_CARD("Lost or stolen card "),
    @XmlEnumValue("Contact member for status")
    CONTACT_MEMBER_FOR_STATUS("Contact member for status"),
    @XmlEnumValue("Account refinanced or renewed")
    ACCOUNT_REFINANCED_OR_RENEWED("Account refinanced or renewed"),
    @XmlEnumValue("Consumer deceased")
    CONSUMER_DECEASED("Consumer deceased"),
    @XmlEnumValue("Included in financial counseling")
    INCLUDED_IN_FINANCIAL_COUNSELING("Included in financial counseling"),
    @XmlEnumValue("Foreclosure proceedings started")
    FORECLOSURE_PROCEEDINGS_STARTED("Foreclosure proceedings started"),
    @XmlEnumValue("In WEP of other party")
    IN_WEP_OF_OTHER_PARTY("In WEP of other party"),
    @XmlEnumValue("Adjustment pending")
    ADJUSTMENT_PENDING("Adjustment pending"),
    @XmlEnumValue("Escrow account only")
    ESCROW_ACCOUNT_ONLY("Escrow account only"),
    @XmlEnumValue("BMO need authorization to clear")
    BMO_NEED_AUTHORIZATION_TO_CLEAR("BMO need authorization to clear"),
    @XmlEnumValue("Account included in CH13")
    ACCOUNT_INCLUDED_IN_CH_13("Account included in CH13"),
    @XmlEnumValue("Creditor declines to clear")
    CREDITOR_DECLINES_TO_CLEAR("Creditor declines to clear"),
    @XmlEnumValue("No response from creditor")
    NO_RESPONSE_FROM_CREDITOR("No response from creditor"),
    @XmlEnumValue("Dispute - resolution pending")
    DISPUTE_RESOLUTION_PENDING("Dispute - resolution pending"),
    @XmlEnumValue("Out-of-area trade")
    OUT_OF_AREA_TRADE("Out-of-area trade"),
    @XmlEnumValue("Unable to check without account number")
    UNABLE_TO_CHECK_WITHOUT_ACCOUNT_NUMBER("Unable to check without account number"),
    @XmlEnumValue("Account not approved")
    ACCOUNT_NOT_APPROVED("Account not approved"),
    @XmlEnumValue("Need consumer authorization")
    NEED_CONSUMER_AUTHORIZATION("Need consumer authorization"),
    @XmlEnumValue("No record of account")
    NO_RECORD_OF_ACCOUNT("No record of account"),
    @XmlEnumValue("Account included in bankruptcy")
    ACCOUNT_INCLUDED_IN_BANKRUPTCY("Account included in bankruptcy"),
    @XmlEnumValue("In bankruptcy / other party")
    IN_BANKRUPTCY_OTHER_PARTY("In bankruptcy / other party"),
    @XmlEnumValue("Assigned to U.S. Dept of ED")
    ASSIGNED_TO_U_S_DEPT_OF_ED("Assigned to U.S. Dept of ED"),
    @XmlEnumValue("Invalid / Unknown")
    INVALID_UNKNOWN("Invalid / Unknown"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    FinancialRateCodesEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FinancialRateCodesEnum fromValue(String v) {
        for (FinancialRateCodesEnum c: FinancialRateCodesEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
