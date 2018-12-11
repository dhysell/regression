//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clueAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clueAddressType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cp.com/rules/client}clueBaseAddressType">
 *       &lt;sequence>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="claim_match_indicator" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;enumeration value="Address is part of claims record, but not necessarily the vehicle operator's address"/>
 *                 &lt;enumeration value="Claim resulted from address LexisNexis developed rather than originally provided address; not necessarily the vehicle operator's address"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *           &lt;element name="search_match_indicator" minOccurs="0">
 *             &lt;simpleType>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                 &lt;enumeration value="Address on file for the loss shown matches the mailing address in the search request"/>
 *                 &lt;enumeration value="Address on file for the loss shown matches the former address in the search request"/>
 *                 &lt;enumeration value="Address on file for the loss shown matches the risk address in the search request"/>
 *                 &lt;enumeration value="Address on file for the loss shown matches an address developed by IDENTITY PLUS"/>
 *               &lt;/restriction>
 *             &lt;/simpleType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="fsi_house" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_street1" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_street2" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="fsi_apartment" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *           &lt;element name="fsi_unit" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;/choice>
 *         &lt;element name="fsi_city" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_state" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_postalcode" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_zip4" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_county" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_country" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_province" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="history" type="{http://cp.com/rules/client}HistoryType" />
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="education"/>
 *             &lt;enumeration value="former"/>
 *             &lt;enumeration value="mailing"/>
 *             &lt;enumeration value="residence"/>
 *             &lt;enumeration value="work"/>
 *             &lt;enumeration value="property"/>
 *             &lt;enumeration value="risk"/>
 *             &lt;enumeration value="policyowner"/>
 *             &lt;enumeration value="crossstreet"/>
 *             &lt;enumeration value="business"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clueAddressType", propOrder = {
    "claimMatchIndicator",
    "searchMatchIndicator",
    "fsiHouse",
    "fsiStreet1",
    "fsiStreet2",
    "fsiApartment",
    "fsiUnit",
    "fsiCity",
    "fsiState",
    "fsiPostalcode",
    "fsiZip4",
    "fsiCounty",
    "fsiCountry",
    "fsiProvince"
})
public class ClueAddressType
    extends ClueBaseAddressType
{

    @XmlElement(name = "claim_match_indicator")
    protected ClueAddressType.ClaimMatchIndicatorEnum claimMatchIndicator;
    @XmlElement(name = "search_match_indicator")
    protected ClueAddressType.SearchMatchIndicatorEnum searchMatchIndicator;
    @XmlElement(name = "fsi_house")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiHouse;
    @XmlElement(name = "fsi_street1")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiStreet1;
    @XmlElement(name = "fsi_street2")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiStreet2;
    @XmlElement(name = "fsi_apartment")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiApartment;
    @XmlElement(name = "fsi_unit")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiUnit;
    @XmlElement(name = "fsi_city")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiCity;
    @XmlElement(name = "fsi_state")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiState;
    @XmlElement(name = "fsi_postalcode")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiPostalcode;
    @XmlElement(name = "fsi_zip4")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiZip4;
    @XmlElement(name = "fsi_county")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiCounty;
    @XmlElement(name = "fsi_country")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiCountry;
    @XmlElement(name = "fsi_province")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiProvince;
    @XmlAttribute(name = "ref")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ref;
    @XmlAttribute(name = "history")
    protected HistoryEnum history;
    @XmlAttribute(name = "type")
    protected ClueAddressType.ClueAddressReferenceTypeEnum type;

    /**
     * Gets the value of the claimMatchIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link ClueAddressType.ClaimMatchIndicatorEnum }
     *     
     */
    public ClueAddressType.ClaimMatchIndicatorEnum getClaimMatchIndicator() {
        return claimMatchIndicator;
    }

    /**
     * Sets the value of the claimMatchIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClueAddressType.ClaimMatchIndicatorEnum }
     *     
     */
    public void setClaimMatchIndicator(ClueAddressType.ClaimMatchIndicatorEnum value) {
        this.claimMatchIndicator = value;
    }

    /**
     * Gets the value of the searchMatchIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link ClueAddressType.SearchMatchIndicatorEnum }
     *     
     */
    public ClueAddressType.SearchMatchIndicatorEnum getSearchMatchIndicator() {
        return searchMatchIndicator;
    }

    /**
     * Sets the value of the searchMatchIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClueAddressType.SearchMatchIndicatorEnum }
     *     
     */
    public void setSearchMatchIndicator(ClueAddressType.SearchMatchIndicatorEnum value) {
        this.searchMatchIndicator = value;
    }

    /**
     * Gets the value of the fsiHouse property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiHouse() {
        return fsiHouse;
    }

    /**
     * Sets the value of the fsiHouse property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiHouse(FSIEnum value) {
        this.fsiHouse = value;
    }

    /**
     * Gets the value of the fsiStreet1 property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiStreet1() {
        return fsiStreet1;
    }

    /**
     * Sets the value of the fsiStreet1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiStreet1(FSIEnum value) {
        this.fsiStreet1 = value;
    }

    /**
     * Gets the value of the fsiStreet2 property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiStreet2() {
        return fsiStreet2;
    }

    /**
     * Sets the value of the fsiStreet2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiStreet2(FSIEnum value) {
        this.fsiStreet2 = value;
    }

    /**
     * Gets the value of the fsiApartment property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiApartment() {
        return fsiApartment;
    }

    /**
     * Sets the value of the fsiApartment property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiApartment(FSIEnum value) {
        this.fsiApartment = value;
    }

    /**
     * Gets the value of the fsiUnit property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiUnit() {
        return fsiUnit;
    }

    /**
     * Sets the value of the fsiUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiUnit(FSIEnum value) {
        this.fsiUnit = value;
    }

    /**
     * Gets the value of the fsiCity property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiCity() {
        return fsiCity;
    }

    /**
     * Sets the value of the fsiCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiCity(FSIEnum value) {
        this.fsiCity = value;
    }

    /**
     * Gets the value of the fsiState property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiState() {
        return fsiState;
    }

    /**
     * Sets the value of the fsiState property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiState(FSIEnum value) {
        this.fsiState = value;
    }

    /**
     * Gets the value of the fsiPostalcode property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiPostalcode() {
        return fsiPostalcode;
    }

    /**
     * Sets the value of the fsiPostalcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiPostalcode(FSIEnum value) {
        this.fsiPostalcode = value;
    }

    /**
     * Gets the value of the fsiZip4 property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiZip4() {
        return fsiZip4;
    }

    /**
     * Sets the value of the fsiZip4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiZip4(FSIEnum value) {
        this.fsiZip4 = value;
    }

    /**
     * Gets the value of the fsiCounty property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiCounty() {
        return fsiCounty;
    }

    /**
     * Sets the value of the fsiCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiCounty(FSIEnum value) {
        this.fsiCounty = value;
    }

    /**
     * Gets the value of the fsiCountry property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiCountry() {
        return fsiCountry;
    }

    /**
     * Sets the value of the fsiCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiCountry(FSIEnum value) {
        this.fsiCountry = value;
    }

    /**
     * Gets the value of the fsiProvince property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiProvince() {
        return fsiProvince;
    }

    /**
     * Sets the value of the fsiProvince property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.FSIEnum }
     *     
     */
    public void setFsiProvince(FSIEnum value) {
        this.fsiProvince = value;
    }

    /**
     * Gets the value of the ref property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setRef(Object value) {
        this.ref = value;
    }

    /**
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link HistoryEnum }
     *     
     */
    public HistoryEnum getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link HistoryEnum }
     *     
     */
    public void setHistory(HistoryEnum value) {
        this.history = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link ClueAddressType.ClueAddressReferenceTypeEnum }
     *     
     */
    public ClueAddressType.ClueAddressReferenceTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClueAddressType.ClueAddressReferenceTypeEnum }
     *     
     */
    public void setType(ClueAddressType.ClueAddressReferenceTypeEnum value) {
        this.type = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Address is part of claims record, but not necessarily the vehicle operator's address"/>
     *     &lt;enumeration value="Claim resulted from address LexisNexis developed rather than originally provided address; not necessarily the vehicle operator's address"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum ClaimMatchIndicatorEnum {

        @XmlEnumValue("Address is part of claims record, but not necessarily the vehicle operator's address")
        ADDRESS_IS_PART_OF_CLAIMS_RECORD_BUT_NOT_NECESSARILY_THE_VEHICLE_OPERATOR_S_ADDRESS("Address is part of claims record, but not necessarily the vehicle operator's address"),
        @XmlEnumValue("Claim resulted from address LexisNexis developed rather than originally provided address; not necessarily the vehicle operator's address")
        CLAIM_RESULTED_FROM_ADDRESS_LEXIS_NEXIS_DEVELOPED_RATHER_THAN_ORIGINALLY_PROVIDED_ADDRESS_NOT_NECESSARILY_THE_VEHICLE_OPERATOR_S_ADDRESS("Claim resulted from address LexisNexis developed rather than originally provided address; not necessarily the vehicle operator's address");
        private final String value;

        ClaimMatchIndicatorEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ClueAddressType.ClaimMatchIndicatorEnum fromValue(String v) {
            for (ClueAddressType.ClaimMatchIndicatorEnum c: ClueAddressType.ClaimMatchIndicatorEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="education"/>
     *     &lt;enumeration value="former"/>
     *     &lt;enumeration value="mailing"/>
     *     &lt;enumeration value="residence"/>
     *     &lt;enumeration value="work"/>
     *     &lt;enumeration value="property"/>
     *     &lt;enumeration value="risk"/>
     *     &lt;enumeration value="policyowner"/>
     *     &lt;enumeration value="crossstreet"/>
     *     &lt;enumeration value="business"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum ClueAddressReferenceTypeEnum {

        @XmlEnumValue("education")
        EDUCATION("education"),
        @XmlEnumValue("former")
        FORMER("former"),
        @XmlEnumValue("mailing")
        MAILING("mailing"),
        @XmlEnumValue("residence")
        RESIDENCE("residence"),
        @XmlEnumValue("work")
        WORK("work"),
        @XmlEnumValue("property")
        PROPERTY("property"),
        @XmlEnumValue("risk")
        RISK("risk"),
        @XmlEnumValue("policyowner")
        POLICYOWNER("policyowner"),
        @XmlEnumValue("crossstreet")
        CROSSSTREET("crossstreet"),
        @XmlEnumValue("business")
        BUSINESS("business");
        private final String value;

        ClueAddressReferenceTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ClueAddressType.ClueAddressReferenceTypeEnum fromValue(String v) {
            for (ClueAddressType.ClueAddressReferenceTypeEnum c: ClueAddressType.ClueAddressReferenceTypeEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Address on file for the loss shown matches the mailing address in the search request"/>
     *     &lt;enumeration value="Address on file for the loss shown matches the former address in the search request"/>
     *     &lt;enumeration value="Address on file for the loss shown matches the risk address in the search request"/>
     *     &lt;enumeration value="Address on file for the loss shown matches an address developed by IDENTITY PLUS"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum SearchMatchIndicatorEnum {

        @XmlEnumValue("Address on file for the loss shown matches the mailing address in the search request")
        ADDRESS_ON_FILE_FOR_THE_LOSS_SHOWN_MATCHES_THE_MAILING_ADDRESS_IN_THE_SEARCH_REQUEST("Address on file for the loss shown matches the mailing address in the search request"),
        @XmlEnumValue("Address on file for the loss shown matches the former address in the search request")
        ADDRESS_ON_FILE_FOR_THE_LOSS_SHOWN_MATCHES_THE_FORMER_ADDRESS_IN_THE_SEARCH_REQUEST("Address on file for the loss shown matches the former address in the search request"),
        @XmlEnumValue("Address on file for the loss shown matches the risk address in the search request")
        ADDRESS_ON_FILE_FOR_THE_LOSS_SHOWN_MATCHES_THE_RISK_ADDRESS_IN_THE_SEARCH_REQUEST("Address on file for the loss shown matches the risk address in the search request"),
        @XmlEnumValue("Address on file for the loss shown matches an address developed by IDENTITY PLUS")
        ADDRESS_ON_FILE_FOR_THE_LOSS_SHOWN_MATCHES_AN_ADDRESS_DEVELOPED_BY_IDENTITY_PLUS("Address on file for the loss shown matches an address developed by IDENTITY PLUS");
        private final String value;

        SearchMatchIndicatorEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ClueAddressType.SearchMatchIndicatorEnum fromValue(String v) {
            for (ClueAddressType.SearchMatchIndicatorEnum c: ClueAddressType.SearchMatchIndicatorEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
