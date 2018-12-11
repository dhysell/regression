
package services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresult;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimPartyType;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimVendorType;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ContactCreationApprovalStatus;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.GenderType;
import services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.addressinfo.AddressInfo;


/**
 * <p>Java class for ABContactAPISearchResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ABContactAPISearchResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountNumber_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressBookUID_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgencyNumber_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgentNumber_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AlternateName_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AssignedAgent_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BusinessPhoneCountry_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BusinessPhoneExtension_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BusinessPhone_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CellPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CellPhoneCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CellPhoneExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClaimPartyType_FBM" type="{http://guidewire.com/ab/typekey}ClaimPartyType" minOccurs="0"/>
 *         &lt;element name="ClaimVendorName_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClaimVendorNumber_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ClaimVendorType_FBM" type="{http://guidewire.com/ab/typekey}ClaimVendorType" minOccurs="0"/>
 *         &lt;element name="ContactType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountyNumber_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CreateStatus" type="{http://guidewire.com/ab/typekey}ContactCreationApprovalStatus" minOccurs="0"/>
 *         &lt;element name="DateOfBirth" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="DateOfDeath_FBM" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="DoingBusinessAsString_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EFTReady_FBM" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="EmailAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaxPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaxPhoneCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaxPhoneExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstNameKanji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FormerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GenderType_FBM" type="{http://guidewire.com/ab/typekey}GenderType" minOccurs="0"/>
 *         &lt;element name="HomePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HomePhoneCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HomePhoneExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastNameKanji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LienholderNumber_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LinkID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MiddleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NameKanji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Particle" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Preferred" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Prefix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrimaryAddress" type="{http://guidewire.com/ab/ws/gw/webservice/ab/ab801/abcontactapi/AddressInfo}AddressInfo" minOccurs="0"/>
 *         &lt;element name="PrimaryAddressDeliveryOptionString_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrimaryPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SSN_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Score" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/>
 *         &lt;element name="Suffix" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TagTypes_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TerminationsAsString_FBM" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VendorAvailability" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VendorType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WorkPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WorkPhoneCountry" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WorkPhoneExtension" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ABContactAPISearchResult", propOrder = {
    "accountNumberFBM",
    "addressBookUIDFBM",
    "agencyNumberFBM",
    "agentNumberFBM",
    "alternateNameFBM",
    "assignedAgentFBM",
    "businessPhoneCountryFBM",
    "businessPhoneExtensionFBM",
    "businessPhoneFBM",
    "cellPhone",
    "cellPhoneCountry",
    "cellPhoneExtension",
    "claimPartyTypeFBM",
    "claimVendorNameFBM",
    "claimVendorNumberFBM",
    "claimVendorTypeFBM",
    "contactType",
    "countyNumberFBM",
    "createStatus",
    "dateOfBirth",
    "dateOfDeathFBM",
    "doingBusinessAsStringFBM",
    "eftReadyFBM",
    "emailAddress1",
    "emailAddress2",
    "faxPhone",
    "faxPhoneCountry",
    "faxPhoneExtension",
    "firstName",
    "firstNameKanji",
    "formerName",
    "genderTypeFBM",
    "homePhone",
    "homePhoneCountry",
    "homePhoneExtension",
    "lastName",
    "lastNameKanji",
    "lienholderNumberFBM",
    "linkID",
    "middleName",
    "name",
    "nameKanji",
    "particle",
    "preferred",
    "prefix",
    "primaryAddress",
    "primaryAddressDeliveryOptionStringFBM",
    "primaryPhone",
    "ssnfbm",
    "score",
    "suffix",
    "tagTypesFBM",
    "taxID",
    "terminationsAsStringFBM",
    "vendorAvailability",
    "vendorType",
    "workPhone",
    "workPhoneCountry",
    "workPhoneExtension"
})
public class ABContactAPISearchResult {

    @XmlElement(name = "AccountNumber_FBM")
    protected String accountNumberFBM;
    @XmlElement(name = "AddressBookUID_FBM")
    protected String addressBookUIDFBM;
    @XmlElement(name = "AgencyNumber_FBM")
    protected String agencyNumberFBM;
    @XmlElement(name = "AgentNumber_FBM")
    protected String agentNumberFBM;
    @XmlElement(name = "AlternateName_FBM")
    protected String alternateNameFBM;
    @XmlElement(name = "AssignedAgent_FBM")
    protected String assignedAgentFBM;
    @XmlElement(name = "BusinessPhoneCountry_FBM")
    protected String businessPhoneCountryFBM;
    @XmlElement(name = "BusinessPhoneExtension_FBM")
    protected String businessPhoneExtensionFBM;
    @XmlElement(name = "BusinessPhone_FBM")
    protected String businessPhoneFBM;
    @XmlElement(name = "CellPhone")
    protected String cellPhone;
    @XmlElement(name = "CellPhoneCountry")
    protected String cellPhoneCountry;
    @XmlElement(name = "CellPhoneExtension")
    protected String cellPhoneExtension;
    @XmlElement(name = "ClaimPartyType_FBM")
    @XmlSchemaType(name = "string")
    protected ClaimPartyType claimPartyTypeFBM;
    @XmlElement(name = "ClaimVendorName_FBM")
    protected String claimVendorNameFBM;
    @XmlElement(name = "ClaimVendorNumber_FBM")
    protected String claimVendorNumberFBM;
    @XmlElement(name = "ClaimVendorType_FBM")
    @XmlSchemaType(name = "string")
    protected ClaimVendorType claimVendorTypeFBM;
    @XmlElement(name = "ContactType")
    protected String contactType;
    @XmlElement(name = "CountyNumber_FBM")
    protected String countyNumberFBM;
    @XmlElement(name = "CreateStatus")
    @XmlSchemaType(name = "string")
    protected ContactCreationApprovalStatus createStatus;
    @XmlElement(name = "DateOfBirth")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfBirth;
    @XmlElement(name = "DateOfDeath_FBM")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dateOfDeathFBM;
    @XmlElement(name = "DoingBusinessAsString_FBM")
    protected String doingBusinessAsStringFBM;
    @XmlElement(name = "EFTReady_FBM")
    protected Boolean eftReadyFBM;
    @XmlElement(name = "EmailAddress1")
    protected String emailAddress1;
    @XmlElement(name = "EmailAddress2")
    protected String emailAddress2;
    @XmlElement(name = "FaxPhone")
    protected String faxPhone;
    @XmlElement(name = "FaxPhoneCountry")
    protected String faxPhoneCountry;
    @XmlElement(name = "FaxPhoneExtension")
    protected String faxPhoneExtension;
    @XmlElement(name = "FirstName")
    protected String firstName;
    @XmlElement(name = "FirstNameKanji")
    protected String firstNameKanji;
    @XmlElement(name = "FormerName")
    protected String formerName;
    @XmlElement(name = "GenderType_FBM")
    @XmlSchemaType(name = "string")
    protected GenderType genderTypeFBM;
    @XmlElement(name = "HomePhone")
    protected String homePhone;
    @XmlElement(name = "HomePhoneCountry")
    protected String homePhoneCountry;
    @XmlElement(name = "HomePhoneExtension")
    protected String homePhoneExtension;
    @XmlElement(name = "LastName")
    protected String lastName;
    @XmlElement(name = "LastNameKanji")
    protected String lastNameKanji;
    @XmlElement(name = "LienholderNumber_FBM")
    protected String lienholderNumberFBM;
    @XmlElement(name = "LinkID")
    protected String linkID;
    @XmlElement(name = "MiddleName")
    protected String middleName;
    @XmlElement(name = "Name")
    protected String name;
    @XmlElement(name = "NameKanji")
    protected String nameKanji;
    @XmlElement(name = "Particle")
    protected String particle;
    @XmlElement(name = "Preferred")
    protected Boolean preferred;
    @XmlElement(name = "Prefix")
    protected String prefix;
    @XmlElement(name = "PrimaryAddress")
    protected AddressInfo primaryAddress;
    @XmlElement(name = "PrimaryAddressDeliveryOptionString_FBM")
    protected String primaryAddressDeliveryOptionStringFBM;
    @XmlElement(name = "PrimaryPhone")
    protected String primaryPhone;
    @XmlElement(name = "SSN_FBM")
    protected String ssnfbm;
    @XmlElement(name = "Score")
    protected Double score;
    @XmlElement(name = "Suffix")
    protected String suffix;
    @XmlElement(name = "TagTypes_FBM")
    protected String tagTypesFBM;
    @XmlElement(name = "TaxID")
    protected String taxID;
    @XmlElement(name = "TerminationsAsString_FBM")
    protected String terminationsAsStringFBM;
    @XmlElement(name = "VendorAvailability")
    protected String vendorAvailability;
    @XmlElement(name = "VendorType")
    protected String vendorType;
    @XmlElement(name = "WorkPhone")
    protected String workPhone;
    @XmlElement(name = "WorkPhoneCountry")
    protected String workPhoneCountry;
    @XmlElement(name = "WorkPhoneExtension")
    protected String workPhoneExtension;

    /**
     * Gets the value of the accountNumberFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumberFBM() {
        return accountNumberFBM;
    }

    /**
     * Sets the value of the accountNumberFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumberFBM(String value) {
        this.accountNumberFBM = value;
    }

    /**
     * Gets the value of the addressBookUIDFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressBookUIDFBM() {
        return addressBookUIDFBM;
    }

    /**
     * Sets the value of the addressBookUIDFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressBookUIDFBM(String value) {
        this.addressBookUIDFBM = value;
    }

    /**
     * Gets the value of the agencyNumberFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyNumberFBM() {
        return agencyNumberFBM;
    }

    /**
     * Sets the value of the agencyNumberFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyNumberFBM(String value) {
        this.agencyNumberFBM = value;
    }

    /**
     * Gets the value of the agentNumberFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentNumberFBM() {
        return agentNumberFBM;
    }

    /**
     * Sets the value of the agentNumberFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentNumberFBM(String value) {
        this.agentNumberFBM = value;
    }

    /**
     * Gets the value of the alternateNameFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateNameFBM() {
        return alternateNameFBM;
    }

    /**
     * Sets the value of the alternateNameFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateNameFBM(String value) {
        this.alternateNameFBM = value;
    }

    /**
     * Gets the value of the assignedAgentFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssignedAgentFBM() {
        return assignedAgentFBM;
    }

    /**
     * Sets the value of the assignedAgentFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssignedAgentFBM(String value) {
        this.assignedAgentFBM = value;
    }

    /**
     * Gets the value of the businessPhoneCountryFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessPhoneCountryFBM() {
        return businessPhoneCountryFBM;
    }

    /**
     * Sets the value of the businessPhoneCountryFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessPhoneCountryFBM(String value) {
        this.businessPhoneCountryFBM = value;
    }

    /**
     * Gets the value of the businessPhoneExtensionFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessPhoneExtensionFBM() {
        return businessPhoneExtensionFBM;
    }

    /**
     * Sets the value of the businessPhoneExtensionFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessPhoneExtensionFBM(String value) {
        this.businessPhoneExtensionFBM = value;
    }

    /**
     * Gets the value of the businessPhoneFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessPhoneFBM() {
        return businessPhoneFBM;
    }

    /**
     * Sets the value of the businessPhoneFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessPhoneFBM(String value) {
        this.businessPhoneFBM = value;
    }

    /**
     * Gets the value of the cellPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellPhone() {
        return cellPhone;
    }

    /**
     * Sets the value of the cellPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellPhone(String value) {
        this.cellPhone = value;
    }

    /**
     * Gets the value of the cellPhoneCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellPhoneCountry() {
        return cellPhoneCountry;
    }

    /**
     * Sets the value of the cellPhoneCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellPhoneCountry(String value) {
        this.cellPhoneCountry = value;
    }

    /**
     * Gets the value of the cellPhoneExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellPhoneExtension() {
        return cellPhoneExtension;
    }

    /**
     * Sets the value of the cellPhoneExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellPhoneExtension(String value) {
        this.cellPhoneExtension = value;
    }

    /**
     * Gets the value of the claimPartyTypeFBM property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimPartyType }
     *     
     */
    public ClaimPartyType getClaimPartyTypeFBM() {
        return claimPartyTypeFBM;
    }

    /**
     * Sets the value of the claimPartyTypeFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimPartyType }
     *     
     */
    public void setClaimPartyTypeFBM(ClaimPartyType value) {
        this.claimPartyTypeFBM = value;
    }

    /**
     * Gets the value of the claimVendorNameFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimVendorNameFBM() {
        return claimVendorNameFBM;
    }

    /**
     * Sets the value of the claimVendorNameFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimVendorNameFBM(String value) {
        this.claimVendorNameFBM = value;
    }

    /**
     * Gets the value of the claimVendorNumberFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimVendorNumberFBM() {
        return claimVendorNumberFBM;
    }

    /**
     * Sets the value of the claimVendorNumberFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimVendorNumberFBM(String value) {
        this.claimVendorNumberFBM = value;
    }

    /**
     * Gets the value of the claimVendorTypeFBM property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimVendorType }
     *     
     */
    public ClaimVendorType getClaimVendorTypeFBM() {
        return claimVendorTypeFBM;
    }

    /**
     * Sets the value of the claimVendorTypeFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimVendorType }
     *     
     */
    public void setClaimVendorTypeFBM(ClaimVendorType value) {
        this.claimVendorTypeFBM = value;
    }

    /**
     * Gets the value of the contactType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * Sets the value of the contactType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactType(String value) {
        this.contactType = value;
    }

    /**
     * Gets the value of the countyNumberFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountyNumberFBM() {
        return countyNumberFBM;
    }

    /**
     * Sets the value of the countyNumberFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountyNumberFBM(String value) {
        this.countyNumberFBM = value;
    }

    /**
     * Gets the value of the createStatus property.
     * 
     * @return
     *     possible object is
     *     {@link ContactCreationApprovalStatus }
     *     
     */
    public ContactCreationApprovalStatus getCreateStatus() {
        return createStatus;
    }

    /**
     * Sets the value of the createStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactCreationApprovalStatus }
     *     
     */
    public void setCreateStatus(ContactCreationApprovalStatus value) {
        this.createStatus = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfBirth(XMLGregorianCalendar value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the dateOfDeathFBM property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDateOfDeathFBM() {
        return dateOfDeathFBM;
    }

    /**
     * Sets the value of the dateOfDeathFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDateOfDeathFBM(XMLGregorianCalendar value) {
        this.dateOfDeathFBM = value;
    }

    /**
     * Gets the value of the doingBusinessAsStringFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoingBusinessAsStringFBM() {
        return doingBusinessAsStringFBM;
    }

    /**
     * Sets the value of the doingBusinessAsStringFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoingBusinessAsStringFBM(String value) {
        this.doingBusinessAsStringFBM = value;
    }

    /**
     * Gets the value of the eftReadyFBM property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isEFTReadyFBM() {
        return eftReadyFBM;
    }

    /**
     * Sets the value of the eftReadyFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setEFTReadyFBM(Boolean value) {
        this.eftReadyFBM = value;
    }

    /**
     * Gets the value of the emailAddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress1() {
        return emailAddress1;
    }

    /**
     * Sets the value of the emailAddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress1(String value) {
        this.emailAddress1 = value;
    }

    /**
     * Gets the value of the emailAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmailAddress2() {
        return emailAddress2;
    }

    /**
     * Sets the value of the emailAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmailAddress2(String value) {
        this.emailAddress2 = value;
    }

    /**
     * Gets the value of the faxPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxPhone() {
        return faxPhone;
    }

    /**
     * Sets the value of the faxPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxPhone(String value) {
        this.faxPhone = value;
    }

    /**
     * Gets the value of the faxPhoneCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxPhoneCountry() {
        return faxPhoneCountry;
    }

    /**
     * Sets the value of the faxPhoneCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxPhoneCountry(String value) {
        this.faxPhoneCountry = value;
    }

    /**
     * Gets the value of the faxPhoneExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaxPhoneExtension() {
        return faxPhoneExtension;
    }

    /**
     * Sets the value of the faxPhoneExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaxPhoneExtension(String value) {
        this.faxPhoneExtension = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the firstNameKanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstNameKanji() {
        return firstNameKanji;
    }

    /**
     * Sets the value of the firstNameKanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstNameKanji(String value) {
        this.firstNameKanji = value;
    }

    /**
     * Gets the value of the formerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFormerName() {
        return formerName;
    }

    /**
     * Sets the value of the formerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFormerName(String value) {
        this.formerName = value;
    }

    /**
     * Gets the value of the genderTypeFBM property.
     * 
     * @return
     *     possible object is
     *     {@link GenderType }
     *     
     */
    public GenderType getGenderTypeFBM() {
        return genderTypeFBM;
    }

    /**
     * Sets the value of the genderTypeFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link GenderType }
     *     
     */
    public void setGenderTypeFBM(GenderType value) {
        this.genderTypeFBM = value;
    }

    /**
     * Gets the value of the homePhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePhone() {
        return homePhone;
    }

    /**
     * Sets the value of the homePhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePhone(String value) {
        this.homePhone = value;
    }

    /**
     * Gets the value of the homePhoneCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePhoneCountry() {
        return homePhoneCountry;
    }

    /**
     * Sets the value of the homePhoneCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePhoneCountry(String value) {
        this.homePhoneCountry = value;
    }

    /**
     * Gets the value of the homePhoneExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomePhoneExtension() {
        return homePhoneExtension;
    }

    /**
     * Sets the value of the homePhoneExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomePhoneExtension(String value) {
        this.homePhoneExtension = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the lastNameKanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastNameKanji() {
        return lastNameKanji;
    }

    /**
     * Sets the value of the lastNameKanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastNameKanji(String value) {
        this.lastNameKanji = value;
    }

    /**
     * Gets the value of the lienholderNumberFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLienholderNumberFBM() {
        return lienholderNumberFBM;
    }

    /**
     * Sets the value of the lienholderNumberFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLienholderNumberFBM(String value) {
        this.lienholderNumberFBM = value;
    }

    /**
     * Gets the value of the linkID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkID() {
        return linkID;
    }

    /**
     * Sets the value of the linkID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkID(String value) {
        this.linkID = value;
    }

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the nameKanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameKanji() {
        return nameKanji;
    }

    /**
     * Sets the value of the nameKanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameKanji(String value) {
        this.nameKanji = value;
    }

    /**
     * Gets the value of the particle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParticle() {
        return particle;
    }

    /**
     * Sets the value of the particle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParticle(String value) {
        this.particle = value;
    }

    /**
     * Gets the value of the preferred property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isPreferred() {
        return preferred;
    }

    /**
     * Sets the value of the preferred property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setPreferred(Boolean value) {
        this.preferred = value;
    }

    /**
     * Gets the value of the prefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Sets the value of the prefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrefix(String value) {
        this.prefix = value;
    }

    /**
     * Gets the value of the primaryAddress property.
     * 
     * @return
     *     possible object is
     *     {@link AddressInfo }
     *     
     */
    public AddressInfo getPrimaryAddress() {
        return primaryAddress;
    }

    /**
     * Sets the value of the primaryAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressInfo }
     *     
     */
    public void setPrimaryAddress(AddressInfo value) {
        this.primaryAddress = value;
    }

    /**
     * Gets the value of the primaryAddressDeliveryOptionStringFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryAddressDeliveryOptionStringFBM() {
        return primaryAddressDeliveryOptionStringFBM;
    }

    /**
     * Sets the value of the primaryAddressDeliveryOptionStringFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryAddressDeliveryOptionStringFBM(String value) {
        this.primaryAddressDeliveryOptionStringFBM = value;
    }

    /**
     * Gets the value of the primaryPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryPhone() {
        return primaryPhone;
    }

    /**
     * Sets the value of the primaryPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryPhone(String value) {
        this.primaryPhone = value;
    }

    /**
     * Gets the value of the ssnfbm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSNFBM() {
        return ssnfbm;
    }

    /**
     * Sets the value of the ssnfbm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSNFBM(String value) {
        this.ssnfbm = value;
    }

    /**
     * Gets the value of the score property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getScore() {
        return score;
    }

    /**
     * Sets the value of the score property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setScore(Double value) {
        this.score = value;
    }

    /**
     * Gets the value of the suffix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Sets the value of the suffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSuffix(String value) {
        this.suffix = value;
    }

    /**
     * Gets the value of the tagTypesFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTagTypesFBM() {
        return tagTypesFBM;
    }

    /**
     * Sets the value of the tagTypesFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTagTypesFBM(String value) {
        this.tagTypesFBM = value;
    }

    /**
     * Gets the value of the taxID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxID() {
        return taxID;
    }

    /**
     * Sets the value of the taxID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxID(String value) {
        this.taxID = value;
    }

    /**
     * Gets the value of the terminationsAsStringFBM property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTerminationsAsStringFBM() {
        return terminationsAsStringFBM;
    }

    /**
     * Sets the value of the terminationsAsStringFBM property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTerminationsAsStringFBM(String value) {
        this.terminationsAsStringFBM = value;
    }

    /**
     * Gets the value of the vendorAvailability property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorAvailability() {
        return vendorAvailability;
    }

    /**
     * Sets the value of the vendorAvailability property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorAvailability(String value) {
        this.vendorAvailability = value;
    }

    /**
     * Gets the value of the vendorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVendorType() {
        return vendorType;
    }

    /**
     * Sets the value of the vendorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVendorType(String value) {
        this.vendorType = value;
    }

    /**
     * Gets the value of the workPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkPhone() {
        return workPhone;
    }

    /**
     * Sets the value of the workPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkPhone(String value) {
        this.workPhone = value;
    }

    /**
     * Gets the value of the workPhoneCountry property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkPhoneCountry() {
        return workPhoneCountry;
    }

    /**
     * Sets the value of the workPhoneCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkPhoneCountry(String value) {
        this.workPhoneCountry = value;
    }

    /**
     * Gets the value of the workPhoneExtension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWorkPhoneExtension() {
        return workPhoneExtension;
    }

    /**
     * Sets the value of the workPhoneExtension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWorkPhoneExtension(String value) {
        this.workPhoneExtension = value;
    }

}
