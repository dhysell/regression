//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clueBaseSubjectType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clueBaseSubjectType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://cp.com/rules/client}clueNameType"/>
 *         &lt;element name="birthdate" type="{http://cp.com/rules/client}LenientDateType" minOccurs="0"/>
 *         &lt;element name="fsi_birthdate" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="gender" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Female"/>
 *               &lt;enumeration value="Male"/>
 *               &lt;enumeration value="Unknown"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fsi_gender" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="ssn" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fsi_ssn" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="deathdate" type="{http://cp.com/rules/client}LenientDateType" minOccurs="0"/>
 *         &lt;element name="quoteback" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="address" type="{http://cp.com/rules/client}clueAddressType" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="telephone" type="{http://cp.com/rules/client}clueTelephoneType" minOccurs="0"/>
 *         &lt;element name="driversLicense" type="{http://cp.com/rules/client}clueDriversLicenseType" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="relationship" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Subject"/>
 *               &lt;enumeration value="Subject's spouse"/>
 *               &lt;enumeration value="Subject's dependent"/>
 *               &lt;enumeration value="Insured"/>
 *               &lt;enumeration value="Policyholder"/>
 *               &lt;enumeration value="Claimant"/>
 *               &lt;enumeration value="Other"/>
 *               &lt;enumeration value="Surname match"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="policy" type="{http://cp.com/rules/client}cluePolicyType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="history" type="{http://cp.com/rules/client}HistoryType" />
 *       &lt;attribute name="name_association_indicator" type="{http://cp.com/rules/client}NameAssociationIndicatorType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clueBaseSubjectType", propOrder = {
    "name",
    "birthdate",
    "fsiBirthdate",
    "gender",
    "fsiGender",
    "ssn",
    "fsiSsn",
    "deathdate",
    "quoteback",
    "addressList",
    "telephone",
    "driversLicenseList",
    "relationship",
    "policy"
})
@XmlSeeAlso({
    DataprefillClueADDSubjectType.class,
    CluePropertySubjectType.class,
    ClueSubjectType.class
})
public class ClueBaseSubjectType {

    @XmlElement(required = true)
    protected ClueNameType name;
    protected String birthdate;
    @XmlElement(name = "fsi_birthdate")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiBirthdate;
    protected ClueBaseSubjectType.GenderEnum gender;
    @XmlElement(name = "fsi_gender")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiGender;
    protected String ssn;
    @XmlElement(name = "fsi_ssn")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiSsn;
    protected String deathdate;
    protected String quoteback;
    @XmlElement(name = "address")
    protected List<ClueAddressType> addressList;
    protected ClueTelephoneType telephone;
    @XmlElement(name = "driversLicense")
    protected List<ClueDriversLicenseType> driversLicenseList;
    protected ClueBaseSubjectType.ClueSubjectRelationshipEnum relationship;
    protected CluePolicyType policy;
    @XmlAttribute(name = "ref")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ref;
    @XmlAttribute(name = "history")
    protected HistoryEnum history;
    @XmlAttribute(name = "name_association_indicator")
    protected NameAssociationIndicatorEnum nameAssociationIndicator;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ClueNameType }
     *     
     */
    public ClueNameType getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ClueNameType }
     *     
     */
    public void setName(ClueNameType value) {
        this.name = value;
    }

    /**
     * Gets the value of the birthdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthdate() {
        return birthdate;
    }

    /**
     * Sets the value of the birthdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthdate(String value) {
        this.birthdate = value;
    }

    /**
     * Gets the value of the fsiBirthdate property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiBirthdate() {
        return fsiBirthdate;
    }

    /**
     * Sets the value of the fsiBirthdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiBirthdate(FSIEnum value) {
        this.fsiBirthdate = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link ClueBaseSubjectType.GenderEnum }
     *     
     */
    public ClueBaseSubjectType.GenderEnum getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClueBaseSubjectType.GenderEnum }
     *     
     */
    public void setGender(ClueBaseSubjectType.GenderEnum value) {
        this.gender = value;
    }

    /**
     * Gets the value of the fsiGender property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiGender() {
        return fsiGender;
    }

    /**
     * Sets the value of the fsiGender property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiGender(FSIEnum value) {
        this.fsiGender = value;
    }

    /**
     * Gets the value of the ssn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Sets the value of the ssn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsn(String value) {
        this.ssn = value;
    }

    /**
     * Gets the value of the fsiSsn property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiSsn() {
        return fsiSsn;
    }

    /**
     * Sets the value of the fsiSsn property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiSsn(FSIEnum value) {
        this.fsiSsn = value;
    }

    /**
     * Gets the value of the deathdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeathdate() {
        return deathdate;
    }

    /**
     * Sets the value of the deathdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeathdate(String value) {
        this.deathdate = value;
    }

    /**
     * Gets the value of the quoteback property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuoteback() {
        return quoteback;
    }

    /**
     * Sets the value of the quoteback property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuoteback(String value) {
        this.quoteback = value;
    }

    /**
     * Gets the value of the addressList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ClueAddressType }
     * 
     * 
     */
    public List<ClueAddressType> getAddressList() {
        if (addressList == null) {
            addressList = new ArrayList<ClueAddressType>();
        }
        return this.addressList;
    }

    /**
     * Gets the value of the telephone property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ClueTelephoneType }
     *     
     */
    public ClueTelephoneType getTelephone() {
        return telephone;
    }

    /**
     * Sets the value of the telephone property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ClueTelephoneType }
     *     
     */
    public void setTelephone(ClueTelephoneType value) {
        this.telephone = value;
    }

    /**
     * Gets the value of the driversLicenseList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the driversLicenseList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDriversLicenseList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ClueDriversLicenseType }
     * 
     * 
     */
    public List<ClueDriversLicenseType> getDriversLicenseList() {
        if (driversLicenseList == null) {
            driversLicenseList = new ArrayList<ClueDriversLicenseType>();
        }
        return this.driversLicenseList;
    }

    /**
     * Gets the value of the relationship property.
     * 
     * @return
     *     possible object is
     *     {@link ClueBaseSubjectType.ClueSubjectRelationshipEnum }
     *     
     */
    public ClueBaseSubjectType.ClueSubjectRelationshipEnum getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClueBaseSubjectType.ClueSubjectRelationshipEnum }
     *     
     */
    public void setRelationship(ClueBaseSubjectType.ClueSubjectRelationshipEnum value) {
        this.relationship = value;
    }

    /**
     * Gets the value of the policy property.
     * 
     * @return
     *     possible object is
     *     {@link CluePolicyType }
     *     
     */
    public CluePolicyType getPolicy() {
        return policy;
    }

    /**
     * Sets the value of the policy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CluePolicyType }
     *     
     */
    public void setPolicy(CluePolicyType value) {
        this.policy = value;
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
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.HistoryEnum }
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
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.HistoryEnum }
     *     
     */
    public void setHistory(HistoryEnum value) {
        this.history = value;
    }

    /**
     * Gets the value of the nameAssociationIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.NameAssociationIndicatorEnum }
     *     
     */
    public NameAssociationIndicatorEnum getNameAssociationIndicator() {
        return nameAssociationIndicator;
    }

    /**
     * Sets the value of the nameAssociationIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.NameAssociationIndicatorEnum }
     *     
     */
    public void setNameAssociationIndicator(NameAssociationIndicatorEnum value) {
        this.nameAssociationIndicator = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Subject"/>
     *     &lt;enumeration value="Subject's spouse"/>
     *     &lt;enumeration value="Subject's dependent"/>
     *     &lt;enumeration value="Insured"/>
     *     &lt;enumeration value="Policyholder"/>
     *     &lt;enumeration value="Claimant"/>
     *     &lt;enumeration value="Other"/>
     *     &lt;enumeration value="Surname match"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum ClueSubjectRelationshipEnum {

        @XmlEnumValue("Subject")
        SUBJECT("Subject"),
        @XmlEnumValue("Subject's spouse")
        SUBJECT_S_SPOUSE("Subject's spouse"),
        @XmlEnumValue("Subject's dependent")
        SUBJECT_S_DEPENDENT("Subject's dependent"),
        @XmlEnumValue("Insured")
        INSURED("Insured"),
        @XmlEnumValue("Policyholder")
        POLICYHOLDER("Policyholder"),
        @XmlEnumValue("Claimant")
        CLAIMANT("Claimant"),
        @XmlEnumValue("Other")
        OTHER("Other"),
        @XmlEnumValue("Surname match")
        SURNAME_MATCH("Surname match");
        private final String value;

        ClueSubjectRelationshipEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ClueBaseSubjectType.ClueSubjectRelationshipEnum fromValue(String v) {
            for (ClueBaseSubjectType.ClueSubjectRelationshipEnum c: ClueBaseSubjectType.ClueSubjectRelationshipEnum.values()) {
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
     *     &lt;enumeration value="Female"/>
     *     &lt;enumeration value="Male"/>
     *     &lt;enumeration value="Unknown"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum GenderEnum {

        @XmlEnumValue("Female")
        FEMALE("Female"),
        @XmlEnumValue("Male")
        MALE("Male"),
        @XmlEnumValue("Unknown")
        UNKNOWN("Unknown");
        private final String value;

        GenderEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ClueBaseSubjectType.GenderEnum fromValue(String v) {
            for (ClueBaseSubjectType.GenderEnum c: ClueBaseSubjectType.GenderEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
