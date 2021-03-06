//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for claimInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="claimInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="carrier_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carrier_policy_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claim_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="claim_state" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="date_of_loss" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="participants" type="{http://cp.com/rules/client}participantInfoListType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "claimInfoType", propOrder = {
    "carrierName",
    "carrierPolicyNumber",
    "claimNumber",
    "claimState",
    "dateOfLoss",
    "participants"
})
public class ClaimInfoType {

    @XmlElement(name = "carrier_name")
    protected String carrierName;
    @XmlElement(name = "carrier_policy_number")
    protected String carrierPolicyNumber;
    @XmlElement(name = "claim_number")
    protected String claimNumber;
    @XmlElement(name = "claim_state")
    protected String claimState;
    @XmlElement(name = "date_of_loss")
    protected String dateOfLoss;
    protected ParticipantInfoListType participants;

    /**
     * Gets the value of the carrierName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierName() {
        return carrierName;
    }

    /**
     * Sets the value of the carrierName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierName(String value) {
        this.carrierName = value;
    }

    /**
     * Gets the value of the carrierPolicyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCarrierPolicyNumber() {
        return carrierPolicyNumber;
    }

    /**
     * Sets the value of the carrierPolicyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCarrierPolicyNumber(String value) {
        this.carrierPolicyNumber = value;
    }

    /**
     * Gets the value of the claimNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimNumber() {
        return claimNumber;
    }

    /**
     * Sets the value of the claimNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimNumber(String value) {
        this.claimNumber = value;
    }

    /**
     * Gets the value of the claimState property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClaimState() {
        return claimState;
    }

    /**
     * Sets the value of the claimState property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClaimState(String value) {
        this.claimState = value;
    }

    /**
     * Gets the value of the dateOfLoss property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfLoss() {
        return dateOfLoss;
    }

    /**
     * Sets the value of the dateOfLoss property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfLoss(String value) {
        this.dateOfLoss = value;
    }

    /**
     * Gets the value of the participants property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ParticipantInfoListType }
     *     
     */
    public ParticipantInfoListType getParticipants() {
        return participants;
    }

    /**
     * Sets the value of the participants property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.ParticipantInfoListType }
     *     
     */
    public void setParticipants(ParticipantInfoListType value) {
        this.participants = value;
    }

}
