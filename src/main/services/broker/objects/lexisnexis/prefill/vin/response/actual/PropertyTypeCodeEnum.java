//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:09:30 PM MST 
//


package services.broker.objects.lexisnexis.prefill.vin.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for propertyTypeCode.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="propertyTypeCode">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Agricultural"/>
 *     &lt;enumeration value="Amusement-Recreation"/>
 *     &lt;enumeration value="Apartment"/>
 *     &lt;enumeration value="Commercial"/>
 *     &lt;enumeration value="Commercial Condominium"/>
 *     &lt;enumeration value="Condominium"/>
 *     &lt;enumeration value="Duplex"/>
 *     &lt;enumeration value="Exempt"/>
 *     &lt;enumeration value="Financial Institution"/>
 *     &lt;enumeration value="Hospital"/>
 *     &lt;enumeration value="Hotel, Motel"/>
 *     &lt;enumeration value="Industrial"/>
 *     &lt;enumeration value="Industrial Heavy"/>
 *     &lt;enumeration value="Miscellaneous"/>
 *     &lt;enumeration value="Office Building"/>
 *     &lt;enumeration value="Parking"/>
 *     &lt;enumeration value="Residential"/>
 *     &lt;enumeration value="Retail"/>
 *     &lt;enumeration value="Single Family Residence"/>
 *     &lt;enumeration value="Transport"/>
 *     &lt;enumeration value="Utilities"/>
 *     &lt;enumeration value="Vacant"/>
 *     &lt;enumeration value="Warehouse"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "propertyTypeCode")
@XmlEnum
public enum PropertyTypeCodeEnum {

    @XmlEnumValue("Agricultural")
    AGRICULTURAL("Agricultural"),
    @XmlEnumValue("Amusement-Recreation")
    AMUSEMENT_RECREATION("Amusement-Recreation"),
    @XmlEnumValue("Apartment")
    APARTMENT("Apartment"),
    @XmlEnumValue("Commercial")
    COMMERCIAL("Commercial"),
    @XmlEnumValue("Commercial Condominium")
    COMMERCIAL_CONDOMINIUM("Commercial Condominium"),
    @XmlEnumValue("Condominium")
    CONDOMINIUM("Condominium"),
    @XmlEnumValue("Duplex")
    DUPLEX("Duplex"),
    @XmlEnumValue("Exempt")
    EXEMPT("Exempt"),
    @XmlEnumValue("Financial Institution")
    FINANCIAL_INSTITUTION("Financial Institution"),
    @XmlEnumValue("Hospital")
    HOSPITAL("Hospital"),
    @XmlEnumValue("Hotel, Motel")
    HOTEL_MOTEL("Hotel, Motel"),
    @XmlEnumValue("Industrial")
    INDUSTRIAL("Industrial"),
    @XmlEnumValue("Industrial Heavy")
    INDUSTRIAL_HEAVY("Industrial Heavy"),
    @XmlEnumValue("Miscellaneous")
    MISCELLANEOUS("Miscellaneous"),
    @XmlEnumValue("Office Building")
    OFFICE_BUILDING("Office Building"),
    @XmlEnumValue("Parking")
    PARKING("Parking"),
    @XmlEnumValue("Residential")
    RESIDENTIAL("Residential"),
    @XmlEnumValue("Retail")
    RETAIL("Retail"),
    @XmlEnumValue("Single Family Residence")
    SINGLE_FAMILY_RESIDENCE("Single Family Residence"),
    @XmlEnumValue("Transport")
    TRANSPORT("Transport"),
    @XmlEnumValue("Utilities")
    UTILITIES("Utilities"),
    @XmlEnumValue("Vacant")
    VACANT("Vacant"),
    @XmlEnumValue("Warehouse")
    WAREHOUSE("Warehouse"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    PropertyTypeCodeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PropertyTypeCodeEnum fromValue(String v) {
        for (PropertyTypeCodeEnum c: PropertyTypeCodeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
