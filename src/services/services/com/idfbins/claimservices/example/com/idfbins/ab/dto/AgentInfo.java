
package services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.CommissionLevelType;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.CommissionStatusType;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.PayMethodType;


/**
 * <p>Java class for AgentInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AgentInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressBookUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgencyManagerName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgencyManagerNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgencyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgencyNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AgentNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AllAgentNumbers" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AlternateName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BusinessPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CellPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CommissionLevelType" type="{http://guidewire.com/ab/typekey}CommissionLevelType" minOccurs="0"/>
 *         &lt;element name="CommissionStatusType" type="{http://guidewire.com/ab/typekey}CommissionStatusType" minOccurs="0"/>
 *         &lt;element name="ContactSubtype" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="County" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountyNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CropSpecialistIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DesignationCLU" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DesignationCPCU" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DesignationChFC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DesignationLUTCF" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DesignationLifeSpecialist" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DesignationRegisteredRepresentative" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EmailAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Employer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FaxPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GenderCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GuaranteedCommissionIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="HireDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="HomePhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LicenseNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LicenseStateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LifeSpecialistIndicator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="MailingAddressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailingAddressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailingCity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailingCounty" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailingPostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MailingStateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MiddleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NamePrefixCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NameSuffixCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Occupation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OfficeAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OfficeNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PayMethodType" type="{http://guidewire.com/ab/typekey}PayMethodType" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PrimaryPhoneCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SSNOfficialID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SpeedDial" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StateCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TaxStatusCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TerminationDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="WorkPhone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AgentInfo", propOrder = {
    "addressBookUID",
    "addressLine1",
    "addressLine2",
    "agencyManagerName",
    "agencyManagerNumber",
    "agencyName",
    "agencyNumber",
    "agentNumber",
    "allAgentNumbers",
    "alternateName",
    "businessPhone",
    "cellPhone",
    "city",
    "commissionLevelType",
    "commissionStatusType",
    "contactSubtype",
    "county",
    "countyNumber",
    "cropSpecialistIndicator",
    "designationCLU",
    "designationCPCU",
    "designationChFC",
    "designationLUTCF",
    "designationLifeSpecialist",
    "designationRegisteredRepresentative",
    "displayName",
    "emailAddress1",
    "emailAddress2",
    "employer",
    "faxPhone",
    "firstName",
    "genderCode",
    "guaranteedCommissionIndicator",
    "hireDate",
    "homePhone",
    "lastName",
    "licenseNumber",
    "licenseStateCode",
    "lifeSpecialistIndicator",
    "mailingAddressLine1",
    "mailingAddressLine2",
    "mailingCity",
    "mailingCounty",
    "mailingPostalCode",
    "mailingStateCode",
    "middleName",
    "namePrefixCode",
    "nameSuffixCode",
    "notes",
    "occupation",
    "officeAddress",
    "officeNumber",
    "payMethodType",
    "postalCode",
    "primaryPhoneCode",
    "ssnOfficialID",
    "speedDial",
    "stateCode",
    "taxID",
    "taxStatusCode",
    "terminationDate",
    "workPhone"
})
public class AgentInfo {

    @XmlElement(name = "AddressBookUID")
    protected String addressBookUID;
    @XmlElement(name = "AddressLine1")
    protected String addressLine1;
    @XmlElement(name = "AddressLine2")
    protected String addressLine2;
    @XmlElement(name = "AgencyManagerName")
    protected String agencyManagerName;
    @XmlElement(name = "AgencyManagerNumber")
    protected String agencyManagerNumber;
    @XmlElement(name = "AgencyName")
    protected String agencyName;
    @XmlElement(name = "AgencyNumber")
    protected String agencyNumber;
    @XmlElement(name = "AgentNumber")
    protected String agentNumber;
    @XmlElement(name = "AllAgentNumbers")
    protected String allAgentNumbers;
    @XmlElement(name = "AlternateName")
    protected String alternateName;
    @XmlElement(name = "BusinessPhone")
    protected String businessPhone;
    @XmlElement(name = "CellPhone")
    protected String cellPhone;
    @XmlElement(name = "City")
    protected String city;
    @XmlElement(name = "CommissionLevelType")
    @XmlSchemaType(name = "string")
    protected CommissionLevelType commissionLevelType;
    @XmlElement(name = "CommissionStatusType")
    @XmlSchemaType(name = "string")
    protected CommissionStatusType commissionStatusType;
    @XmlElement(name = "ContactSubtype")
    protected String contactSubtype;
    @XmlElement(name = "County")
    protected String county;
    @XmlElement(name = "CountyNumber")
    protected String countyNumber;
    @XmlElement(name = "CropSpecialistIndicator")
    protected Boolean cropSpecialistIndicator;
    @XmlElement(name = "DesignationCLU")
    protected Boolean designationCLU;
    @XmlElement(name = "DesignationCPCU")
    protected Boolean designationCPCU;
    @XmlElement(name = "DesignationChFC")
    protected Boolean designationChFC;
    @XmlElement(name = "DesignationLUTCF")
    protected Boolean designationLUTCF;
    @XmlElement(name = "DesignationLifeSpecialist")
    protected Boolean designationLifeSpecialist;
    @XmlElement(name = "DesignationRegisteredRepresentative")
    protected Boolean designationRegisteredRepresentative;
    @XmlElement(name = "DisplayName")
    protected String displayName;
    @XmlElement(name = "EmailAddress1")
    protected String emailAddress1;
    @XmlElement(name = "EmailAddress2")
    protected String emailAddress2;
    @XmlElement(name = "Employer")
    protected String employer;
    @XmlElement(name = "FaxPhone")
    protected String faxPhone;
    @XmlElement(name = "FirstName")
    protected String firstName;
    @XmlElement(name = "GenderCode")
    protected String genderCode;
    @XmlElement(name = "GuaranteedCommissionIndicator")
    protected Boolean guaranteedCommissionIndicator;
    @XmlElement(name = "HireDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar hireDate;
    @XmlElement(name = "HomePhone")
    protected String homePhone;
    @XmlElement(name = "LastName")
    protected String lastName;
    @XmlElement(name = "LicenseNumber")
    protected String licenseNumber;
    @XmlElement(name = "LicenseStateCode")
    protected String licenseStateCode;
    @XmlElement(name = "LifeSpecialistIndicator")
    protected Boolean lifeSpecialistIndicator;
    @XmlElement(name = "MailingAddressLine1")
    protected String mailingAddressLine1;
    @XmlElement(name = "MailingAddressLine2")
    protected String mailingAddressLine2;
    @XmlElement(name = "MailingCity")
    protected String mailingCity;
    @XmlElement(name = "MailingCounty")
    protected String mailingCounty;
    @XmlElement(name = "MailingPostalCode")
    protected String mailingPostalCode;
    @XmlElement(name = "MailingStateCode")
    protected String mailingStateCode;
    @XmlElement(name = "MiddleName")
    protected String middleName;
    @XmlElement(name = "NamePrefixCode")
    protected String namePrefixCode;
    @XmlElement(name = "NameSuffixCode")
    protected String nameSuffixCode;
    @XmlElement(name = "Notes")
    protected String notes;
    @XmlElement(name = "Occupation")
    protected String occupation;
    @XmlElement(name = "OfficeAddress")
    protected String officeAddress;
    @XmlElement(name = "OfficeNumber")
    protected String officeNumber;
    @XmlElement(name = "PayMethodType")
    @XmlSchemaType(name = "string")
    protected PayMethodType payMethodType;
    @XmlElement(name = "PostalCode")
    protected String postalCode;
    @XmlElement(name = "PrimaryPhoneCode")
    protected String primaryPhoneCode;
    @XmlElement(name = "SSNOfficialID")
    protected String ssnOfficialID;
    @XmlElement(name = "SpeedDial")
    protected String speedDial;
    @XmlElement(name = "StateCode")
    protected String stateCode;
    @XmlElement(name = "TaxID")
    protected String taxID;
    @XmlElement(name = "TaxStatusCode")
    protected String taxStatusCode;
    @XmlElement(name = "TerminationDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar terminationDate;
    @XmlElement(name = "WorkPhone")
    protected String workPhone;

    /**
     * Gets the value of the addressBookUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressBookUID() {
        return addressBookUID;
    }

    /**
     * Sets the value of the addressBookUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressBookUID(String value) {
        this.addressBookUID = value;
    }

    /**
     * Gets the value of the addressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets the value of the addressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine1(String value) {
        this.addressLine1 = value;
    }

    /**
     * Gets the value of the addressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets the value of the addressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine2(String value) {
        this.addressLine2 = value;
    }

    /**
     * Gets the value of the agencyManagerName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyManagerName() {
        return agencyManagerName;
    }

    /**
     * Sets the value of the agencyManagerName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyManagerName(String value) {
        this.agencyManagerName = value;
    }

    /**
     * Gets the value of the agencyManagerNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyManagerNumber() {
        return agencyManagerNumber;
    }

    /**
     * Sets the value of the agencyManagerNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyManagerNumber(String value) {
        this.agencyManagerNumber = value;
    }

    /**
     * Gets the value of the agencyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyName() {
        return agencyName;
    }

    /**
     * Sets the value of the agencyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyName(String value) {
        this.agencyName = value;
    }

    /**
     * Gets the value of the agencyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgencyNumber() {
        return agencyNumber;
    }

    /**
     * Sets the value of the agencyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgencyNumber(String value) {
        this.agencyNumber = value;
    }

    /**
     * Gets the value of the agentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAgentNumber() {
        return agentNumber;
    }

    /**
     * Sets the value of the agentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAgentNumber(String value) {
        this.agentNumber = value;
    }

    /**
     * Gets the value of the allAgentNumbers property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllAgentNumbers() {
        return allAgentNumbers;
    }

    /**
     * Sets the value of the allAgentNumbers property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllAgentNumbers(String value) {
        this.allAgentNumbers = value;
    }

    /**
     * Gets the value of the alternateName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlternateName() {
        return alternateName;
    }

    /**
     * Sets the value of the alternateName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlternateName(String value) {
        this.alternateName = value;
    }

    /**
     * Gets the value of the businessPhone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBusinessPhone() {
        return businessPhone;
    }

    /**
     * Sets the value of the businessPhone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBusinessPhone(String value) {
        this.businessPhone = value;
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
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the commissionLevelType property.
     * 
     * @return
     *     possible object is
     *     {@link CommissionLevelType }
     *     
     */
    public CommissionLevelType getCommissionLevelType() {
        return commissionLevelType;
    }

    /**
     * Sets the value of the commissionLevelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommissionLevelType }
     *     
     */
    public void setCommissionLevelType(CommissionLevelType value) {
        this.commissionLevelType = value;
    }

    /**
     * Gets the value of the commissionStatusType property.
     * 
     * @return
     *     possible object is
     *     {@link CommissionStatusType }
     *     
     */
    public CommissionStatusType getCommissionStatusType() {
        return commissionStatusType;
    }

    /**
     * Sets the value of the commissionStatusType property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommissionStatusType }
     *     
     */
    public void setCommissionStatusType(CommissionStatusType value) {
        this.commissionStatusType = value;
    }

    /**
     * Gets the value of the contactSubtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContactSubtype() {
        return contactSubtype;
    }

    /**
     * Sets the value of the contactSubtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContactSubtype(String value) {
        this.contactSubtype = value;
    }

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCounty() {
        return county;
    }

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCounty(String value) {
        this.county = value;
    }

    /**
     * Gets the value of the countyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountyNumber() {
        return countyNumber;
    }

    /**
     * Sets the value of the countyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountyNumber(String value) {
        this.countyNumber = value;
    }

    /**
     * Gets the value of the cropSpecialistIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isCropSpecialistIndicator() {
        return cropSpecialistIndicator;
    }

    /**
     * Sets the value of the cropSpecialistIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setCropSpecialistIndicator(Boolean value) {
        this.cropSpecialistIndicator = value;
    }

    /**
     * Gets the value of the designationCLU property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDesignationCLU() {
        return designationCLU;
    }

    /**
     * Sets the value of the designationCLU property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDesignationCLU(Boolean value) {
        this.designationCLU = value;
    }

    /**
     * Gets the value of the designationCPCU property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDesignationCPCU() {
        return designationCPCU;
    }

    /**
     * Sets the value of the designationCPCU property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDesignationCPCU(Boolean value) {
        this.designationCPCU = value;
    }

    /**
     * Gets the value of the designationChFC property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDesignationChFC() {
        return designationChFC;
    }

    /**
     * Sets the value of the designationChFC property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDesignationChFC(Boolean value) {
        this.designationChFC = value;
    }

    /**
     * Gets the value of the designationLUTCF property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDesignationLUTCF() {
        return designationLUTCF;
    }

    /**
     * Sets the value of the designationLUTCF property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDesignationLUTCF(Boolean value) {
        this.designationLUTCF = value;
    }

    /**
     * Gets the value of the designationLifeSpecialist property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDesignationLifeSpecialist() {
        return designationLifeSpecialist;
    }

    /**
     * Sets the value of the designationLifeSpecialist property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDesignationLifeSpecialist(Boolean value) {
        this.designationLifeSpecialist = value;
    }

    /**
     * Gets the value of the designationRegisteredRepresentative property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDesignationRegisteredRepresentative() {
        return designationRegisteredRepresentative;
    }

    /**
     * Sets the value of the designationRegisteredRepresentative property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDesignationRegisteredRepresentative(Boolean value) {
        this.designationRegisteredRepresentative = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
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
     * Gets the value of the employer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEmployer() {
        return employer;
    }

    /**
     * Sets the value of the employer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEmployer(String value) {
        this.employer = value;
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
     * Gets the value of the genderCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGenderCode() {
        return genderCode;
    }

    /**
     * Sets the value of the genderCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGenderCode(String value) {
        this.genderCode = value;
    }

    /**
     * Gets the value of the guaranteedCommissionIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isGuaranteedCommissionIndicator() {
        return guaranteedCommissionIndicator;
    }

    /**
     * Sets the value of the guaranteedCommissionIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setGuaranteedCommissionIndicator(Boolean value) {
        this.guaranteedCommissionIndicator = value;
    }

    /**
     * Gets the value of the hireDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getHireDate() {
        return hireDate;
    }

    /**
     * Sets the value of the hireDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setHireDate(XMLGregorianCalendar value) {
        this.hireDate = value;
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
     * Gets the value of the licenseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * Sets the value of the licenseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseNumber(String value) {
        this.licenseNumber = value;
    }

    /**
     * Gets the value of the licenseStateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicenseStateCode() {
        return licenseStateCode;
    }

    /**
     * Sets the value of the licenseStateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicenseStateCode(String value) {
        this.licenseStateCode = value;
    }

    /**
     * Gets the value of the lifeSpecialistIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isLifeSpecialistIndicator() {
        return lifeSpecialistIndicator;
    }

    /**
     * Sets the value of the lifeSpecialistIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLifeSpecialistIndicator(Boolean value) {
        this.lifeSpecialistIndicator = value;
    }

    /**
     * Gets the value of the mailingAddressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailingAddressLine1() {
        return mailingAddressLine1;
    }

    /**
     * Sets the value of the mailingAddressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailingAddressLine1(String value) {
        this.mailingAddressLine1 = value;
    }

    /**
     * Gets the value of the mailingAddressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailingAddressLine2() {
        return mailingAddressLine2;
    }

    /**
     * Sets the value of the mailingAddressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailingAddressLine2(String value) {
        this.mailingAddressLine2 = value;
    }

    /**
     * Gets the value of the mailingCity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailingCity() {
        return mailingCity;
    }

    /**
     * Sets the value of the mailingCity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailingCity(String value) {
        this.mailingCity = value;
    }

    /**
     * Gets the value of the mailingCounty property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailingCounty() {
        return mailingCounty;
    }

    /**
     * Sets the value of the mailingCounty property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailingCounty(String value) {
        this.mailingCounty = value;
    }

    /**
     * Gets the value of the mailingPostalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailingPostalCode() {
        return mailingPostalCode;
    }

    /**
     * Sets the value of the mailingPostalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailingPostalCode(String value) {
        this.mailingPostalCode = value;
    }

    /**
     * Gets the value of the mailingStateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMailingStateCode() {
        return mailingStateCode;
    }

    /**
     * Sets the value of the mailingStateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMailingStateCode(String value) {
        this.mailingStateCode = value;
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
     * Gets the value of the namePrefixCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamePrefixCode() {
        return namePrefixCode;
    }

    /**
     * Sets the value of the namePrefixCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamePrefixCode(String value) {
        this.namePrefixCode = value;
    }

    /**
     * Gets the value of the nameSuffixCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameSuffixCode() {
        return nameSuffixCode;
    }

    /**
     * Sets the value of the nameSuffixCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameSuffixCode(String value) {
        this.nameSuffixCode = value;
    }

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the occupation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Sets the value of the occupation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOccupation(String value) {
        this.occupation = value;
    }

    /**
     * Gets the value of the officeAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficeAddress() {
        return officeAddress;
    }

    /**
     * Sets the value of the officeAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficeAddress(String value) {
        this.officeAddress = value;
    }

    /**
     * Gets the value of the officeNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfficeNumber() {
        return officeNumber;
    }

    /**
     * Sets the value of the officeNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfficeNumber(String value) {
        this.officeNumber = value;
    }

    /**
     * Gets the value of the payMethodType property.
     * 
     * @return
     *     possible object is
     *     {@link PayMethodType }
     *     
     */
    public PayMethodType getPayMethodType() {
        return payMethodType;
    }

    /**
     * Sets the value of the payMethodType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PayMethodType }
     *     
     */
    public void setPayMethodType(PayMethodType value) {
        this.payMethodType = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the primaryPhoneCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryPhoneCode() {
        return primaryPhoneCode;
    }

    /**
     * Sets the value of the primaryPhoneCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryPhoneCode(String value) {
        this.primaryPhoneCode = value;
    }

    /**
     * Gets the value of the ssnOfficialID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSNOfficialID() {
        return ssnOfficialID;
    }

    /**
     * Sets the value of the ssnOfficialID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSNOfficialID(String value) {
        this.ssnOfficialID = value;
    }

    /**
     * Gets the value of the speedDial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeedDial() {
        return speedDial;
    }

    /**
     * Sets the value of the speedDial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeedDial(String value) {
        this.speedDial = value;
    }

    /**
     * Gets the value of the stateCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateCode() {
        return stateCode;
    }

    /**
     * Sets the value of the stateCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateCode(String value) {
        this.stateCode = value;
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
     * Gets the value of the taxStatusCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxStatusCode() {
        return taxStatusCode;
    }

    /**
     * Sets the value of the taxStatusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxStatusCode(String value) {
        this.taxStatusCode = value;
    }

    /**
     * Gets the value of the terminationDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTerminationDate() {
        return terminationDate;
    }

    /**
     * Sets the value of the terminationDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTerminationDate(XMLGregorianCalendar value) {
        this.terminationDate = value;
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

}
