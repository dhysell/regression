//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mileageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mileageType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="inspection_date" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="reason" type="{http://cp.com/rules/client}vmrReasonType" minOccurs="0"/>
 *         &lt;element name="overall_result" type="{http://cp.com/rules/client}vmrOverallResultType" minOccurs="0"/>
 *         &lt;element name="estimated_annual_mileage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="actual_mileage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unusual_activity" type="{http://cp.com/rules/client}vmrUnusualActivityType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="group_sequence_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mileageType", propOrder = {
    "inspectionDate",
    "reason",
    "overallResult",
    "estimatedAnnualMileage",
    "actualMileage",
    "unusualActivity"
})
public class MileageType {

    @XmlElement(name = "inspection_date")
    protected String inspectionDate;
    protected VmrReasonType reason;
    @XmlElement(name = "overall_result")
    protected VmrOverallResultType overallResult;
    @XmlElement(name = "estimated_annual_mileage")
    protected String estimatedAnnualMileage;
    @XmlElement(name = "actual_mileage")
    protected String actualMileage;
    @XmlElement(name = "unusual_activity")
    protected VmrUnusualActivityType unusualActivity;
    @XmlAttribute(name = "group_sequence_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger groupSequenceNumber;

    /**
     * Gets the value of the inspectionDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInspectionDate() {
        return inspectionDate;
    }

    /**
     * Sets the value of the inspectionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInspectionDate(String value) {
        this.inspectionDate = value;
    }

    /**
     * Gets the value of the reason property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.VmrReasonType }
     *     
     */
    public VmrReasonType getReason() {
        return reason;
    }

    /**
     * Sets the value of the reason property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.VmrReasonType }
     *     
     */
    public void setReason(VmrReasonType value) {
        this.reason = value;
    }

    /**
     * Gets the value of the overallResult property.
     * 
     * @return
     *     possible object is
     *     {@link VmrOverallResultType }
     *     
     */
    public VmrOverallResultType getOverallResult() {
        return overallResult;
    }

    /**
     * Sets the value of the overallResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link VmrOverallResultType }
     *     
     */
    public void setOverallResult(VmrOverallResultType value) {
        this.overallResult = value;
    }

    /**
     * Gets the value of the estimatedAnnualMileage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEstimatedAnnualMileage() {
        return estimatedAnnualMileage;
    }

    /**
     * Sets the value of the estimatedAnnualMileage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEstimatedAnnualMileage(String value) {
        this.estimatedAnnualMileage = value;
    }

    /**
     * Gets the value of the actualMileage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActualMileage() {
        return actualMileage;
    }

    /**
     * Sets the value of the actualMileage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActualMileage(String value) {
        this.actualMileage = value;
    }

    /**
     * Gets the value of the unusualActivity property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.VmrUnusualActivityType }
     *     
     */
    public VmrUnusualActivityType getUnusualActivity() {
        return unusualActivity;
    }

    /**
     * Sets the value of the unusualActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.VmrUnusualActivityType }
     *     
     */
    public void setUnusualActivity(VmrUnusualActivityType value) {
        this.unusualActivity = value;
    }

    /**
     * Gets the value of the groupSequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGroupSequenceNumber() {
        return groupSequenceNumber;
    }

    /**
     * Sets the value of the groupSequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGroupSequenceNumber(BigInteger value) {
        this.groupSequenceNumber = value;
    }

}
