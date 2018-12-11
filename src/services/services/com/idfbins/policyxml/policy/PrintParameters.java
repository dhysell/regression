//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import services.services.com.idfbins.policyxml.claim.ClaimHistorySummaries;


/**
 * <p>Java class for PrintParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PrintParameters"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ACORDCertificatePrintRequest" type="{http://www.idfbins.com/Policy}ACORDCertificatePrintRequest" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="claimHistorySummaries" type="{http://www.idfbins.com/Claim}ClaimHistorySummaries" minOccurs="0"/&gt;
 *         &lt;element name="enclosures" type="{http://www.idfbins.com/Policy}Enclosures" minOccurs="0"/&gt;
 *         &lt;element name="cancelNotices" type="{http://www.idfbins.com/Policy}CancelNotices" minOccurs="0"/&gt;
 *         &lt;element name="removedAddlInsureds" type="{http://www.idfbins.com/Policy}RemovedAddlInsureds" minOccurs="0"/&gt;
 *         &lt;element name="removedAddlInterests" type="{http://www.idfbins.com/Policy}RemovedAddlInterests" minOccurs="0"/&gt;
 *         &lt;element name="renewalOffer" type="{http://www.idfbins.com/Policy}RenewalOffer" minOccurs="0"/&gt;
 *         &lt;element name="ACORD28Requests" type="{http://www.idfbins.com/Policy}ACORD28Requests" minOccurs="0"/&gt;
 *         &lt;element name="EndorsementRequests" type="{http://www.idfbins.com/Policy}EndorsementRequests" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PrintParameters", propOrder = {
    "acordCertificatePrintRequest",
    "claimHistorySummaries",
    "enclosures",
    "cancelNotices",
    "removedAddlInsureds",
    "removedAddlInterests",
    "renewalOffer",
    "acord28Requests",
    "endorsementRequests"
})
public class PrintParameters {

    @XmlElement(name = "ACORDCertificatePrintRequest")
    protected List<services.services.com.idfbins.policyxml.policy.ACORDCertificatePrintRequest> acordCertificatePrintRequest;
    protected services.services.com.idfbins.policyxml.claim.ClaimHistorySummaries claimHistorySummaries;
    protected Enclosures enclosures;
    protected services.services.com.idfbins.policyxml.policy.CancelNotices cancelNotices;
    protected services.services.com.idfbins.policyxml.policy.RemovedAddlInsureds removedAddlInsureds;
    protected services.services.com.idfbins.policyxml.policy.RemovedAddlInterests removedAddlInterests;
    protected services.services.com.idfbins.policyxml.policy.RenewalOffer renewalOffer;
    @XmlElement(name = "ACORD28Requests")
    protected services.services.com.idfbins.policyxml.policy.ACORD28Requests acord28Requests;
    @XmlElement(name = "EndorsementRequests")
    protected services.services.com.idfbins.policyxml.policy.EndorsementRequests endorsementRequests;

    /**
     * Gets the value of the acordCertificatePrintRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the acordCertificatePrintRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getACORDCertificatePrintRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link services.services.com.idfbins.policyxml.policy.ACORDCertificatePrintRequest }
     * 
     * 
     */
    public List<services.services.com.idfbins.policyxml.policy.ACORDCertificatePrintRequest> getACORDCertificatePrintRequest() {
        if (acordCertificatePrintRequest == null) {
            acordCertificatePrintRequest = new ArrayList<ACORDCertificatePrintRequest>();
        }
        return this.acordCertificatePrintRequest;
    }

    /**
     * Gets the value of the claimHistorySummaries property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.claim.ClaimHistorySummaries }
     *     
     */
    public services.services.com.idfbins.policyxml.claim.ClaimHistorySummaries getClaimHistorySummaries() {
        return claimHistorySummaries;
    }

    /**
     * Sets the value of the claimHistorySummaries property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.claim.ClaimHistorySummaries }
     *     
     */
    public void setClaimHistorySummaries(ClaimHistorySummaries value) {
        this.claimHistorySummaries = value;
    }

    /**
     * Gets the value of the enclosures property.
     * 
     * @return
     *     possible object is
     *     {@link Enclosures }
     *     
     */
    public Enclosures getEnclosures() {
        return enclosures;
    }

    /**
     * Sets the value of the enclosures property.
     * 
     * @param value
     *     allowed object is
     *     {@link Enclosures }
     *     
     */
    public void setEnclosures(Enclosures value) {
        this.enclosures = value;
    }

    /**
     * Gets the value of the cancelNotices property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.CancelNotices }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.CancelNotices getCancelNotices() {
        return cancelNotices;
    }

    /**
     * Sets the value of the cancelNotices property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.CancelNotices }
     *     
     */
    public void setCancelNotices(CancelNotices value) {
        this.cancelNotices = value;
    }

    /**
     * Gets the value of the removedAddlInsureds property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.RemovedAddlInsureds }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.RemovedAddlInsureds getRemovedAddlInsureds() {
        return removedAddlInsureds;
    }

    /**
     * Sets the value of the removedAddlInsureds property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.RemovedAddlInsureds }
     *     
     */
    public void setRemovedAddlInsureds(RemovedAddlInsureds value) {
        this.removedAddlInsureds = value;
    }

    /**
     * Gets the value of the removedAddlInterests property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.RemovedAddlInterests }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.RemovedAddlInterests getRemovedAddlInterests() {
        return removedAddlInterests;
    }

    /**
     * Sets the value of the removedAddlInterests property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.RemovedAddlInterests }
     *     
     */
    public void setRemovedAddlInterests(RemovedAddlInterests value) {
        this.removedAddlInterests = value;
    }

    /**
     * Gets the value of the renewalOffer property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.RenewalOffer }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.RenewalOffer getRenewalOffer() {
        return renewalOffer;
    }

    /**
     * Sets the value of the renewalOffer property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.RenewalOffer }
     *     
     */
    public void setRenewalOffer(RenewalOffer value) {
        this.renewalOffer = value;
    }

    /**
     * Gets the value of the acord28Requests property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.ACORD28Requests }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.ACORD28Requests getACORD28Requests() {
        return acord28Requests;
    }

    /**
     * Sets the value of the acord28Requests property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.ACORD28Requests }
     *     
     */
    public void setACORD28Requests(ACORD28Requests value) {
        this.acord28Requests = value;
    }

    /**
     * Gets the value of the endorsementRequests property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.EndorsementRequests }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.EndorsementRequests getEndorsementRequests() {
        return endorsementRequests;
    }

    /**
     * Sets the value of the endorsementRequests property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.EndorsementRequests }
     *     
     */
    public void setEndorsementRequests(EndorsementRequests value) {
        this.endorsementRequests = value;
    }

}
