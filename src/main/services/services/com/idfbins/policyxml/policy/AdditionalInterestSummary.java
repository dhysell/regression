//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AdditionalInterestSummary complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AdditionalInterestSummary"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contact" type="{http://www.idfbins.com/Policy}Contact"/&gt;
 *         &lt;element name="detailId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="address" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="designatedAddress" type="{http://www.idfbins.com/Policy}Location"/&gt;
 *         &lt;element name="buildingDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="premium" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="loanNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="coverageSummaries" type="{http://www.idfbins.com/Policy}CoverageSummaries" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalInterestSummary", propOrder = {
    "contact",
    "detailId",
    "address",
    "designatedAddress",
    "buildingDescription",
    "premium",
    "loanNumber",
    "coverageSummaries"
})
public class AdditionalInterestSummary {

    @XmlElement(required = true)
    protected Contact contact;
    protected String detailId;
    @XmlElement(required = true)
    protected String address;
    @XmlElement(required = true)
    protected Location designatedAddress;
    protected String buildingDescription;
    @XmlElement(required = true)
    protected BigDecimal premium;
    protected String loanNumber;
    protected services.services.com.idfbins.policyxml.policy.CoverageSummaries coverageSummaries;

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link Contact }
     *     
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link Contact }
     *     
     */
    public void setContact(Contact value) {
        this.contact = value;
    }

    /**
     * Gets the value of the detailId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDetailId() {
        return detailId;
    }

    /**
     * Sets the value of the detailId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDetailId(String value) {
        this.detailId = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the designatedAddress property.
     * 
     * @return
     *     possible object is
     *     {@link Location }
     *     
     */
    public Location getDesignatedAddress() {
        return designatedAddress;
    }

    /**
     * Sets the value of the designatedAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link Location }
     *     
     */
    public void setDesignatedAddress(Location value) {
        this.designatedAddress = value;
    }

    /**
     * Gets the value of the buildingDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuildingDescription() {
        return buildingDescription;
    }

    /**
     * Sets the value of the buildingDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildingDescription(String value) {
        this.buildingDescription = value;
    }

    /**
     * Gets the value of the premium property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPremium() {
        return premium;
    }

    /**
     * Sets the value of the premium property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPremium(BigDecimal value) {
        this.premium = value;
    }

    /**
     * Gets the value of the loanNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLoanNumber() {
        return loanNumber;
    }

    /**
     * Sets the value of the loanNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLoanNumber(String value) {
        this.loanNumber = value;
    }

    /**
     * Gets the value of the coverageSummaries property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.CoverageSummaries }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.CoverageSummaries getCoverageSummaries() {
        return coverageSummaries;
    }

    /**
     * Sets the value of the coverageSummaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.CoverageSummaries }
     *     
     */
    public void setCoverageSummaries(CoverageSummaries value) {
        this.coverageSummaries = value;
    }

}
