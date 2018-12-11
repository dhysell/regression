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
 * <p>Java class for RenewalOffer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RenewalOffer"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="additionalInterestBills" type="{http://www.idfbins.com/Policy}AdditionalInterestSummaries" minOccurs="0"/&gt;
 *         &lt;element name="priorTermBalance" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="downPayment" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *         &lt;element name="fees" type="{http://www.idfbins.com/Policy}Fees"/&gt;
 *         &lt;element name="totalDue" type="{http://www.w3.org/2001/XMLSchema}decimal"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RenewalOffer", propOrder = {
    "additionalInterestBills",
    "priorTermBalance",
    "downPayment",
    "fees",
    "totalDue"
})
public class RenewalOffer {

    protected AdditionalInterestSummaries additionalInterestBills;
    @XmlElement(required = true)
    protected BigDecimal priorTermBalance;
    @XmlElement(required = true)
    protected BigDecimal downPayment;
    @XmlElement(required = true)
    protected services.services.com.idfbins.policyxml.policy.Fees fees;
    @XmlElement(required = true)
    protected BigDecimal totalDue;

    /**
     * Gets the value of the additionalInterestBills property.
     * 
     * @return
     *     possible object is
     *     {@link AdditionalInterestSummaries }
     *     
     */
    public AdditionalInterestSummaries getAdditionalInterestBills() {
        return additionalInterestBills;
    }

    /**
     * Sets the value of the additionalInterestBills property.
     * 
     * @param value
     *     allowed object is
     *     {@link AdditionalInterestSummaries }
     *     
     */
    public void setAdditionalInterestBills(AdditionalInterestSummaries value) {
        this.additionalInterestBills = value;
    }

    /**
     * Gets the value of the priorTermBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPriorTermBalance() {
        return priorTermBalance;
    }

    /**
     * Sets the value of the priorTermBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPriorTermBalance(BigDecimal value) {
        this.priorTermBalance = value;
    }

    /**
     * Gets the value of the downPayment property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDownPayment() {
        return downPayment;
    }

    /**
     * Sets the value of the downPayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDownPayment(BigDecimal value) {
        this.downPayment = value;
    }

    /**
     * Gets the value of the fees property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.Fees }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.Fees getFees() {
        return fees;
    }

    /**
     * Sets the value of the fees property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.Fees }
     *     
     */
    public void setFees(Fees value) {
        this.fees = value;
    }

    /**
     * Gets the value of the totalDue property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalDue() {
        return totalDue;
    }

    /**
     * Sets the value of the totalDue property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalDue(BigDecimal value) {
        this.totalDue = value;
    }

}