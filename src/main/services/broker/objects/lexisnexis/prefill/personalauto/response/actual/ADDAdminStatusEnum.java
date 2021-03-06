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
 * <p>Java class for auto_dataprefill_add_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="auto_dataprefill_add_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="No Driver Discovery reported"/>
 *     &lt;enumeration value="Driver Discovery reported"/>
 *     &lt;enumeration value="not processed; invalid LexisNexis Account Number"/>
 *     &lt;enumeration value="not processed; insufficient search data"/>
 *     &lt;enumeration value="Driver Discovery search unavailable"/>
 *     &lt;enumeration value="Driver Discovery search not requested"/>
 *     &lt;enumeration value="Driver Discovery  state unavailable"/>
 *     &lt;enumeration value="Requested state not on Driver Discovery database"/>
 *     &lt;enumeration value="Search not processed; state limitation"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "auto_dataprefill_add_status")
@XmlEnum
public enum ADDAdminStatusEnum {

    @XmlEnumValue("No Driver Discovery reported")
    NO_DRIVER_DISCOVERY_REPORTED("No Driver Discovery reported"),
    @XmlEnumValue("Driver Discovery reported")
    DRIVER_DISCOVERY_REPORTED("Driver Discovery reported"),
    @XmlEnumValue("not processed; invalid LexisNexis Account Number")
    NOT_PROCESSED_INVALID_LEXIS_NEXIS_ACCOUNT_NUMBER("not processed; invalid LexisNexis Account Number"),
    @XmlEnumValue("not processed; insufficient search data")
    NOT_PROCESSED_INSUFFICIENT_SEARCH_DATA("not processed; insufficient search data"),
    @XmlEnumValue("Driver Discovery search unavailable")
    DRIVER_DISCOVERY_SEARCH_UNAVAILABLE("Driver Discovery search unavailable"),
    @XmlEnumValue("Driver Discovery search not requested")
    DRIVER_DISCOVERY_SEARCH_NOT_REQUESTED("Driver Discovery search not requested"),
    @XmlEnumValue("Driver Discovery  state unavailable")
    DRIVER_DISCOVERY_STATE_UNAVAILABLE("Driver Discovery  state unavailable"),
    @XmlEnumValue("Requested state not on Driver Discovery database")
    REQUESTED_STATE_NOT_ON_DRIVER_DISCOVERY_DATABASE("Requested state not on Driver Discovery database"),
    @XmlEnumValue("Search not processed; state limitation")
    SEARCH_NOT_PROCESSED_STATE_LIMITATION("Search not processed; state limitation");
    private final String value;

    ADDAdminStatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ADDAdminStatusEnum fromValue(String v) {
        for (ADDAdminStatusEnum c: ADDAdminStatusEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
