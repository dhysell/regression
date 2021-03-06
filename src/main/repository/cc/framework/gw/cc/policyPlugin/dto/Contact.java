
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Contact complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Contact">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrganizationType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MailLocation" type="{http://www.idfbins.com/PolicyRetrieveResponse}Location"/>
 *         &lt;element name="Email1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Email2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FaxPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Primary" type="{http://www.idfbins.com/PolicyRetrieveResponse}Location"/>
 *         &lt;element name="PrimaryLanguage" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PrimaryPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Retired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="SSNOfficialID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="STAXOfficialID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="STUNOfficialID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="SubType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Synced" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TaxID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TaxStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TUNSOfficialID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="VendorNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="VendorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="W9Received" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="W9ReceivedDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="W9ValidFrom" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="W9ValidTo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WorkPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CellPhone" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DOB" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="Employer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="FirstName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LastName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="MaritalStatus" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Gender" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LicenseNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LicenseState" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Occupation" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OrganizationName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="ContactType" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Contact", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "id",
        "organizationType",
        "name",
        "displayName",
        "mailLocation",
        "email1",
        "email2",
        "faxPhone",
        "notes",
        "primary",
        "primaryLanguage",
        "primaryPhone",
        "retired",
        "ssnOfficialID",
        "staxOfficialID",
        "stunOfficialID",
        "subType",
        "synced",
        "taxID",
        "taxStatus",
        "tunsOfficialID",
        "vendorNumber",
        "vendorType",
        "w9Received",
        "w9ReceivedDate",
        "w9ValidFrom",
        "w9ValidTo",
        "workPhone",
        "cellPhone",
        "dob",
        "employer",
        "firstName",
        "lastName",
        "maritalStatus",
        "gender",
        "licenseNumber",
        "licenseState",
        "occupation",
        "publicID",
        "organizationName"
})
public class Contact {

    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "OrganizationType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String organizationType;
    @XmlElement(name = "Name", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String name;
    @XmlElement(name = "DisplayName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String displayName;
    @XmlElement(name = "MailLocation", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Location mailLocation;
    @XmlElement(name = "Email1", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String email1;
    @XmlElement(name = "Email2", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String email2;
    @XmlElement(name = "FaxPhone", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String faxPhone;
    @XmlElement(name = "Notes", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String notes;
    @XmlElement(name = "Primary", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Location primary;
    @XmlElement(name = "PrimaryLanguage", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String primaryLanguage;
    @XmlElement(name = "PrimaryPhone", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String primaryPhone;
    @XmlElement(name = "Retired", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected boolean retired;
    @XmlElement(name = "SSNOfficialID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String ssnOfficialID;
    @XmlElement(name = "STAXOfficialID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String staxOfficialID;
    @XmlElement(name = "STUNOfficialID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String stunOfficialID;
    @XmlElement(name = "SubType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String subType;
    @XmlElement(name = "Synced", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String synced;
    @XmlElement(name = "TaxID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String taxID;
    @XmlElement(name = "TaxStatus", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String taxStatus;
    @XmlElement(name = "TUNSOfficialID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String tunsOfficialID;
    @XmlElement(name = "VendorNumber", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String vendorNumber;
    @XmlElement(name = "VendorType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String vendorType;
    @XmlElement(name = "W9Received", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String w9Received;
    @XmlElement(name = "W9ReceivedDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String w9ReceivedDate;
    @XmlElement(name = "W9ValidFrom", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String w9ValidFrom;
    @XmlElement(name = "W9ValidTo", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String w9ValidTo;
    @XmlElement(name = "WorkPhone", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String workPhone;
    @XmlElement(name = "CellPhone", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String cellPhone;
    @XmlElement(name = "DOB", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar dob;
    @XmlElement(name = "Employer", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String employer;
    @XmlElement(name = "FirstName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String firstName;
    @XmlElement(name = "LastName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String lastName;
    @XmlElement(name = "MaritalStatus", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String maritalStatus;
    @XmlElement(name = "Gender", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String gender;
    @XmlElement(name = "LicenseNumber", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String licenseNumber;
    @XmlElement(name = "LicenseState", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String licenseState;
    @XmlElement(name = "Occupation", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String occupation;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;
    @XmlElement(name = "OrganizationName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String organizationName;
    @XmlAttribute(name = "ContactType")
    protected String contactType;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the organizationType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrganizationType() {
        return organizationType;
    }

    /**
     * Sets the value of the organizationType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrganizationType(String value) {
        this.organizationType = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the displayName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the mailLocation property.
     *
     * @return possible object is
     * {@link Location }
     */
    public Location getMailLocation() {
        return mailLocation;
    }

    /**
     * Sets the value of the mailLocation property.
     *
     * @param value allowed object is
     *              {@link Location }
     */
    public void setMailLocation(Location value) {
        this.mailLocation = value;
    }

    /**
     * Gets the value of the email1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail1() {
        return email1;
    }

    /**
     * Sets the value of the email1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail1(String value) {
        this.email1 = value;
    }

    /**
     * Gets the value of the email2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmail2() {
        return email2;
    }

    /**
     * Sets the value of the email2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmail2(String value) {
        this.email2 = value;
    }

    /**
     * Gets the value of the faxPhone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFaxPhone() {
        return faxPhone;
    }

    /**
     * Sets the value of the faxPhone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFaxPhone(String value) {
        this.faxPhone = value;
    }

    /**
     * Gets the value of the notes property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNotes(String value) {
        this.notes = value;
    }

    /**
     * Gets the value of the primary property.
     *
     * @return possible object is
     * {@link Location }
     */
    public Location getPrimary() {
        return primary;
    }

    /**
     * Sets the value of the primary property.
     *
     * @param value allowed object is
     *              {@link Location }
     */
    public void setPrimary(Location value) {
        this.primary = value;
    }

    /**
     * Gets the value of the primaryLanguage property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    /**
     * Sets the value of the primaryLanguage property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrimaryLanguage(String value) {
        this.primaryLanguage = value;
    }

    /**
     * Gets the value of the primaryPhone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPrimaryPhone() {
        return primaryPhone;
    }

    /**
     * Sets the value of the primaryPhone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPrimaryPhone(String value) {
        this.primaryPhone = value;
    }

    /**
     * Gets the value of the retired property.
     */
    public boolean isRetired() {
        return retired;
    }

    /**
     * Sets the value of the retired property.
     */
    public void setRetired(boolean value) {
        this.retired = value;
    }

    /**
     * Gets the value of the ssnOfficialID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSSNOfficialID() {
        return ssnOfficialID;
    }

    /**
     * Sets the value of the ssnOfficialID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSSNOfficialID(String value) {
        this.ssnOfficialID = value;
    }

    /**
     * Gets the value of the staxOfficialID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSTAXOfficialID() {
        return staxOfficialID;
    }

    /**
     * Sets the value of the staxOfficialID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSTAXOfficialID(String value) {
        this.staxOfficialID = value;
    }

    /**
     * Gets the value of the stunOfficialID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSTUNOfficialID() {
        return stunOfficialID;
    }

    /**
     * Sets the value of the stunOfficialID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSTUNOfficialID(String value) {
        this.stunOfficialID = value;
    }

    /**
     * Gets the value of the subType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Sets the value of the subType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubType(String value) {
        this.subType = value;
    }

    /**
     * Gets the value of the synced property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSynced() {
        return synced;
    }

    /**
     * Sets the value of the synced property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSynced(String value) {
        this.synced = value;
    }

    /**
     * Gets the value of the taxID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTaxID() {
        return taxID;
    }

    /**
     * Sets the value of the taxID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTaxID(String value) {
        this.taxID = value;
    }

    /**
     * Gets the value of the taxStatus property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTaxStatus() {
        return taxStatus;
    }

    /**
     * Sets the value of the taxStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTaxStatus(String value) {
        this.taxStatus = value;
    }

    /**
     * Gets the value of the tunsOfficialID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTUNSOfficialID() {
        return tunsOfficialID;
    }

    /**
     * Sets the value of the tunsOfficialID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTUNSOfficialID(String value) {
        this.tunsOfficialID = value;
    }

    /**
     * Gets the value of the vendorNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVendorNumber() {
        return vendorNumber;
    }

    /**
     * Sets the value of the vendorNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVendorNumber(String value) {
        this.vendorNumber = value;
    }

    /**
     * Gets the value of the vendorType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVendorType() {
        return vendorType;
    }

    /**
     * Sets the value of the vendorType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVendorType(String value) {
        this.vendorType = value;
    }

    /**
     * Gets the value of the w9Received property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getW9Received() {
        return w9Received;
    }

    /**
     * Sets the value of the w9Received property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setW9Received(String value) {
        this.w9Received = value;
    }

    /**
     * Gets the value of the w9ReceivedDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getW9ReceivedDate() {
        return w9ReceivedDate;
    }

    /**
     * Sets the value of the w9ReceivedDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setW9ReceivedDate(String value) {
        this.w9ReceivedDate = value;
    }

    /**
     * Gets the value of the w9ValidFrom property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getW9ValidFrom() {
        return w9ValidFrom;
    }

    /**
     * Sets the value of the w9ValidFrom property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setW9ValidFrom(String value) {
        this.w9ValidFrom = value;
    }

    /**
     * Gets the value of the w9ValidTo property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getW9ValidTo() {
        return w9ValidTo;
    }

    /**
     * Sets the value of the w9ValidTo property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setW9ValidTo(String value) {
        this.w9ValidTo = value;
    }

    /**
     * Gets the value of the workPhone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getWorkPhone() {
        return workPhone;
    }

    /**
     * Sets the value of the workPhone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWorkPhone(String value) {
        this.workPhone = value;
    }

    /**
     * Gets the value of the cellPhone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCellPhone() {
        return cellPhone;
    }

    /**
     * Sets the value of the cellPhone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCellPhone(String value) {
        this.cellPhone = value;
    }

    /**
     * Gets the value of the dob property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getDOB() {
        return dob;
    }

    /**
     * Sets the value of the dob property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setDOB(XMLGregorianCalendar value) {
        this.dob = value;
    }

    /**
     * Gets the value of the employer property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEmployer() {
        return employer;
    }

    /**
     * Sets the value of the employer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEmployer(String value) {
        this.employer = value;
    }

    /**
     * Gets the value of the firstName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the lastName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the maritalStatus property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the value of the maritalStatus property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMaritalStatus(String value) {
        this.maritalStatus = value;
    }

    /**
     * Gets the value of the gender property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the licenseNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLicenseNumber() {
        return licenseNumber;
    }

    /**
     * Sets the value of the licenseNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLicenseNumber(String value) {
        this.licenseNumber = value;
    }

    /**
     * Gets the value of the licenseState property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLicenseState() {
        return licenseState;
    }

    /**
     * Sets the value of the licenseState property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLicenseState(String value) {
        this.licenseState = value;
    }

    /**
     * Gets the value of the occupation property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOccupation() {
        return occupation;
    }

    /**
     * Sets the value of the occupation property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOccupation(String value) {
        this.occupation = value;
    }

    /**
     * Gets the value of the publicID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * Sets the value of the publicID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublicID(String value) {
        this.publicID = value;
    }

    /**
     * Gets the value of the organizationName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Sets the value of the organizationName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOrganizationName(String value) {
        this.organizationName = value;
    }

    /**
     * Gets the value of the contactType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getContactType() {
        return contactType;
    }

    /**
     * Sets the value of the contactType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setContactType(String value) {
        this.contactType = value;
    }

}
