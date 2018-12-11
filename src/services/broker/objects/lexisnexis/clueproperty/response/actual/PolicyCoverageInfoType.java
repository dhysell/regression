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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for policyCoverageInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="policyCoverageInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="CarrierName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="BILimits" type="{http://cp.com/rules/client}LimitCoverageStruct" minOccurs="0"/>
 *         &lt;element name="PDLimits" type="{http://cp.com/rules/client}LimitCoverageStruct" minOccurs="0"/>
 *         &lt;element name="UMLimits" type="{http://cp.com/rules/client}LimitCoverageStruct" minOccurs="0"/>
 *         &lt;element name="MedPayCoverage" type="{http://cp.com/rules/client}LimitCoverageStruct" minOccurs="0"/>
 *         &lt;element name="TowingAndLabor" type="{http://cp.com/rules/client}LimitCoverageStruct" minOccurs="0"/>
 *         &lt;element name="RentalCarCoverage" type="{http://cp.com/rules/client}LimitCoverageStruct" minOccurs="0"/>
 *         &lt;element name="AdditionalField1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalField2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AdditionalField3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "policyCoverageInfoType", propOrder = {

})
public class PolicyCoverageInfoType {

    @XmlElement(name = "CarrierName")
    protected String carrierName;
    @XmlElement(name = "BILimits")
    protected LimitCoverageStruct biLimits;
    @XmlElement(name = "PDLimits")
    protected LimitCoverageStruct pdLimits;
    @XmlElement(name = "UMLimits")
    protected LimitCoverageStruct umLimits;
    @XmlElement(name = "MedPayCoverage")
    protected LimitCoverageStruct medPayCoverage;
    @XmlElement(name = "TowingAndLabor")
    protected LimitCoverageStruct towingAndLabor;
    @XmlElement(name = "RentalCarCoverage")
    protected LimitCoverageStruct rentalCarCoverage;
    @XmlElement(name = "AdditionalField1")
    protected String additionalField1;
    @XmlElement(name = "AdditionalField2")
    protected String additionalField2;
    @XmlElement(name = "AdditionalField3")
    protected String additionalField3;

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
     * Gets the value of the biLimits property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public LimitCoverageStruct getBILimits() {
        return biLimits;
    }

    /**
     * Sets the value of the biLimits property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public void setBILimits(LimitCoverageStruct value) {
        this.biLimits = value;
    }

    /**
     * Gets the value of the pdLimits property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public LimitCoverageStruct getPDLimits() {
        return pdLimits;
    }

    /**
     * Sets the value of the pdLimits property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public void setPDLimits(LimitCoverageStruct value) {
        this.pdLimits = value;
    }

    /**
     * Gets the value of the umLimits property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public LimitCoverageStruct getUMLimits() {
        return umLimits;
    }

    /**
     * Sets the value of the umLimits property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public void setUMLimits(LimitCoverageStruct value) {
        this.umLimits = value;
    }

    /**
     * Gets the value of the medPayCoverage property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public LimitCoverageStruct getMedPayCoverage() {
        return medPayCoverage;
    }

    /**
     * Sets the value of the medPayCoverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public void setMedPayCoverage(LimitCoverageStruct value) {
        this.medPayCoverage = value;
    }

    /**
     * Gets the value of the towingAndLabor property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public LimitCoverageStruct getTowingAndLabor() {
        return towingAndLabor;
    }

    /**
     * Sets the value of the towingAndLabor property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public void setTowingAndLabor(LimitCoverageStruct value) {
        this.towingAndLabor = value;
    }

    /**
     * Gets the value of the rentalCarCoverage property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public LimitCoverageStruct getRentalCarCoverage() {
        return rentalCarCoverage;
    }

    /**
     * Sets the value of the rentalCarCoverage property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.LimitCoverageStruct }
     *     
     */
    public void setRentalCarCoverage(LimitCoverageStruct value) {
        this.rentalCarCoverage = value;
    }

    /**
     * Gets the value of the additionalField1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField1() {
        return additionalField1;
    }

    /**
     * Sets the value of the additionalField1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField1(String value) {
        this.additionalField1 = value;
    }

    /**
     * Gets the value of the additionalField2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField2() {
        return additionalField2;
    }

    /**
     * Sets the value of the additionalField2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField2(String value) {
        this.additionalField2 = value;
    }

    /**
     * Gets the value of the additionalField3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdditionalField3() {
        return additionalField3;
    }

    /**
     * Sets the value of the additionalField3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdditionalField3(String value) {
        this.additionalField3 = value;
    }

}
