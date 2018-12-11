//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for costSummaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="costSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="insuranceToValue" type="{http://cp.com/rules/client}threeDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="erc" type="{http://cp.com/rules/client}costType" minOccurs="0"/>
 *         &lt;element name="profit" type="{http://cp.com/rules/client}nineDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="architectAmount" type="{http://cp.com/rules/client}sixDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="salesTaxIndicator" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="debrisAmount" type="{http://cp.com/rules/client}sixDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="acv" type="{http://cp.com/rules/client}costType" minOccurs="0"/>
 *         &lt;element name="overheadAmount" type="{http://cp.com/rules/client}sixDigitPositiveInteger" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "costSummaryType", propOrder = {
    "insuranceToValue",
    "erc",
    "profit",
    "architectAmount",
    "salesTaxIndicator",
    "debrisAmount",
    "acv",
    "overheadAmount"
})
public class CostSummaryType {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter7 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer insuranceToValue;
    protected CostType erc;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter6 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer profit;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter10 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer architectAmount;
    @XmlSchemaType(name = "string")
    protected YesNoType salesTaxIndicator;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter10 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer debrisAmount;
    protected CostType acv;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter10 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer overheadAmount;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "address")
    protected String address;

    /**
     * Gets the value of the insuranceToValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getInsuranceToValue() {
        return insuranceToValue;
    }

    /**
     * Sets the value of the insuranceToValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInsuranceToValue(Integer value) {
        this.insuranceToValue = value;
    }

    /**
     * Gets the value of the erc property.
     * 
     * @return
     *     possible object is
     *     {@link CostType }
     *     
     */
    public CostType getErc() {
        return erc;
    }

    /**
     * Sets the value of the erc property.
     * 
     * @param value
     *     allowed object is
     *     {@link CostType }
     *     
     */
    public void setErc(CostType value) {
        this.erc = value;
    }

    /**
     * Gets the value of the profit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getProfit() {
        return profit;
    }

    /**
     * Sets the value of the profit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfit(Integer value) {
        this.profit = value;
    }

    /**
     * Gets the value of the architectAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getArchitectAmount() {
        return architectAmount;
    }

    /**
     * Sets the value of the architectAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArchitectAmount(Integer value) {
        this.architectAmount = value;
    }

    /**
     * Gets the value of the salesTaxIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.YesNoType }
     *     
     */
    public YesNoType getSalesTaxIndicator() {
        return salesTaxIndicator;
    }

    /**
     * Sets the value of the salesTaxIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.YesNoType }
     *     
     */
    public void setSalesTaxIndicator(YesNoType value) {
        this.salesTaxIndicator = value;
    }

    /**
     * Gets the value of the debrisAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getDebrisAmount() {
        return debrisAmount;
    }

    /**
     * Sets the value of the debrisAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDebrisAmount(Integer value) {
        this.debrisAmount = value;
    }

    /**
     * Gets the value of the acv property.
     * 
     * @return
     *     possible object is
     *     {@link CostType }
     *     
     */
    public CostType getAcv() {
        return acv;
    }

    /**
     * Sets the value of the acv property.
     * 
     * @param value
     *     allowed object is
     *     {@link CostType }
     *     
     */
    public void setAcv(CostType value) {
        this.acv = value;
    }

    /**
     * Gets the value of the overheadAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getOverheadAmount() {
        return overheadAmount;
    }

    /**
     * Sets the value of the overheadAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverheadAmount(Integer value) {
        this.overheadAmount = value;
    }

    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUnitNumber() {
        return unitNumber;
    }

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUnitNumber(BigInteger value) {
        this.unitNumber = value;
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

}
