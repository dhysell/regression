//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for participantInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="participantInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="participant_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="participant_type" type="{http://cp.com/rules/client}participantType" minOccurs="0"/>
 *         &lt;element name="participant_role" type="{http://cp.com/rules/client}participantRole" minOccurs="0"/>
 *         &lt;element name="policy_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="carrier_name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subject" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/>
 *         &lt;element name="vehicle" type="{http://www.w3.org/2001/XMLSchema}IDREF" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "participantInfoType", propOrder = {
    "participantNumber",
    "participantType",
    "participantRole",
    "policyNumber",
    "carrierName",
    "subject",
    "vehicle"
})
public class ParticipantInfoType {

    @XmlElement(name = "participant_number")
    protected String participantNumber;
    @XmlElement(name = "participant_type")
    @XmlSchemaType(name = "string")
    protected ParticipantTypeEnum participantType;
    @XmlElement(name = "participant_role")
    @XmlSchemaType(name = "string")
    protected ParticipantRoleEnum participantRole;
    @XmlElement(name = "policy_number")
    protected String policyNumber;
    @XmlElement(name = "carrier_name")
    protected String carrierName;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object subject;
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object vehicle;

    /**
     * Gets the value of the participantNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParticipantNumber() {
        return participantNumber;
    }

    /**
     * Sets the value of the participantNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParticipantNumber(String value) {
        this.participantNumber = value;
    }

    /**
     * Gets the value of the participantType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.ParticipantTypeEnum }
     *     
     */
    public ParticipantTypeEnum getParticipantType() {
        return participantType;
    }

    /**
     * Sets the value of the participantType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.ParticipantTypeEnum }
     *     
     */
    public void setParticipantType(ParticipantTypeEnum value) {
        this.participantType = value;
    }

    /**
     * Gets the value of the participantRole property.
     * 
     * @return
     *     possible object is
     *     {@link ParticipantRoleEnum }
     *     
     */
    public ParticipantRoleEnum getParticipantRole() {
        return participantRole;
    }

    /**
     * Sets the value of the participantRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParticipantRoleEnum }
     *     
     */
    public void setParticipantRole(ParticipantRoleEnum value) {
        this.participantRole = value;
    }

    /**
     * Gets the value of the policyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * Sets the value of the policyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyNumber(String value) {
        this.policyNumber = value;
    }

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
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSubject(Object value) {
        this.subject = value;
    }

    /**
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setVehicle(Object value) {
        this.vehicle = value;
    }

}
