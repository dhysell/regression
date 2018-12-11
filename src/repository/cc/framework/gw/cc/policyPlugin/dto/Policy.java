
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Policy complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Policy">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PolicyNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Insured" type="{http://www.idfbins.com/PolicyRetrieveResponse}Contact" minOccurs="0"/>
 *         &lt;element name="PolicyHolder" type="{http://www.idfbins.com/PolicyRetrieveResponse}Contact" minOccurs="0"/>
 *         &lt;element name="PolicyContacts" type="{http://www.idfbins.com/PolicyRetrieveResponse}PolicyContacts"/>
 *         &lt;element name="PolicyCoverages" type="{http://www.idfbins.com/PolicyRetrieveResponse}PolicyCoverage" minOccurs="0"/>
 *         &lt;element name="PolicyLocations" type="{http://www.idfbins.com/PolicyRetrieveResponse}PolicyLocations" minOccurs="0"/>
 *         &lt;element name="RiskUnits" type="{http://www.idfbins.com/PolicyRetrieveResponse}RiskUnits"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CancelDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="InceptionDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PolicyEffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PolicyExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Policy", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "policyNumber",
        "insured",
        "policyHolder",
        "policyContacts",
        "policyCoverages",
        "policyLocations",
        "riskUnits",
        "id",
        "publicID",
        "cancelDate",
        "inceptionDate",
        "policyEffectiveDate",
        "policyExpirationDate"
})
public class Policy {

    @XmlElement(name = "PolicyNumber", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String policyNumber;
    @XmlElement(name = "Insured", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected Contact insured;
    @XmlElement(name = "PolicyHolder", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected Contact policyHolder;
    @XmlElement(name = "PolicyContacts", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected PolicyContacts policyContacts;
    @XmlElement(name = "PolicyCoverages", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected PolicyCoverage policyCoverages;
    @XmlElement(name = "PolicyLocations", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected PolicyLocations policyLocations;
    @XmlElement(name = "RiskUnits", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected RiskUnits riskUnits;
    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;
    @XmlElement(name = "CancelDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String cancelDate;
    @XmlElement(name = "InceptionDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String inceptionDate;
    @XmlElement(name = "PolicyEffectiveDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String policyEffectiveDate;
    @XmlElement(name = "PolicyExpirationDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String policyExpirationDate;

    /**
     * Gets the value of the policyNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * Sets the value of the policyNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPolicyNumber(String value) {
        this.policyNumber = value;
    }

    /**
     * Gets the value of the insured property.
     *
     * @return possible object is
     * {@link Contact }
     */
    public Contact getInsured() {
        return insured;
    }

    /**
     * Sets the value of the insured property.
     *
     * @param value allowed object is
     *              {@link Contact }
     */
    public void setInsured(Contact value) {
        this.insured = value;
    }

    /**
     * Gets the value of the policyHolder property.
     *
     * @return possible object is
     * {@link Contact }
     */
    public Contact getPolicyHolder() {
        return policyHolder;
    }

    /**
     * Sets the value of the policyHolder property.
     *
     * @param value allowed object is
     *              {@link Contact }
     */
    public void setPolicyHolder(Contact value) {
        this.policyHolder = value;
    }

    /**
     * Gets the value of the policyContacts property.
     *
     * @return possible object is
     * {@link PolicyContacts }
     */
    public PolicyContacts getPolicyContacts() {
        return policyContacts;
    }

    /**
     * Sets the value of the policyContacts property.
     *
     * @param value allowed object is
     *              {@link PolicyContacts }
     */
    public void setPolicyContacts(PolicyContacts value) {
        this.policyContacts = value;
    }

    /**
     * Gets the value of the policyCoverages property.
     *
     * @return possible object is
     * {@link PolicyCoverage }
     */
    public PolicyCoverage getPolicyCoverages() {
        return policyCoverages;
    }

    /**
     * Sets the value of the policyCoverages property.
     *
     * @param value allowed object is
     *              {@link PolicyCoverage }
     */
    public void setPolicyCoverages(PolicyCoverage value) {
        this.policyCoverages = value;
    }

    /**
     * Gets the value of the policyLocations property.
     *
     * @return possible object is
     * {@link PolicyLocations }
     */
    public PolicyLocations getPolicyLocations() {
        return policyLocations;
    }

    /**
     * Sets the value of the policyLocations property.
     *
     * @param value allowed object is
     *              {@link PolicyLocations }
     */
    public void setPolicyLocations(PolicyLocations value) {
        this.policyLocations = value;
    }

    /**
     * Gets the value of the riskUnits property.
     *
     * @return possible object is
     * {@link RiskUnits }
     */
    public RiskUnits getRiskUnits() {
        return riskUnits;
    }

    /**
     * Sets the value of the riskUnits property.
     *
     * @param value allowed object is
     *              {@link RiskUnits }
     */
    public void setRiskUnits(RiskUnits value) {
        this.riskUnits = value;
    }

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
     * Gets the value of the cancelDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCancelDate() {
        return cancelDate;
    }

    /**
     * Sets the value of the cancelDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCancelDate(String value) {
        this.cancelDate = value;
    }

    /**
     * Gets the value of the inceptionDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getInceptionDate() {
        return inceptionDate;
    }

    /**
     * Sets the value of the inceptionDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInceptionDate(String value) {
        this.inceptionDate = value;
    }

    /**
     * Gets the value of the policyEffectiveDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPolicyEffectiveDate() {
        return policyEffectiveDate;
    }

    /**
     * Sets the value of the policyEffectiveDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPolicyEffectiveDate(String value) {
        this.policyEffectiveDate = value;
    }

    /**
     * Gets the value of the policyExpirationDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPolicyExpirationDate() {
        return policyExpirationDate;
    }

    /**
     * Sets the value of the policyExpirationDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPolicyExpirationDate(String value) {
        this.policyExpirationDate = value;
    }

}
