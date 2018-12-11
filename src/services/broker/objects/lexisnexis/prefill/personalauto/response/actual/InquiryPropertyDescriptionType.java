//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for inquiryPropertyDescriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="inquiryPropertyDescriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="description1" type="{http://cp.com/rules/client}propertyDescriptionStringType" minOccurs="0"/>
 *         &lt;element name="addvalue1" type="{http://cp.com/rules/client}sixDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="addquality1" type="{http://cp.com/rules/client}qualityCodeType" minOccurs="0"/>
 *         &lt;element name="addcondition1" type="{http://cp.com/rules/client}conditionCodeType" minOccurs="0"/>
 *         &lt;element name="livingAreaIndicator1" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="description2" type="{http://cp.com/rules/client}propertyDescriptionStringType" minOccurs="0"/>
 *         &lt;element name="addvalue2" type="{http://cp.com/rules/client}sixDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="addquality2" type="{http://cp.com/rules/client}qualityCodeType" minOccurs="0"/>
 *         &lt;element name="addcondition2" type="{http://cp.com/rules/client}conditionCodeType" minOccurs="0"/>
 *         &lt;element name="livingAreaIndicator2" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "inquiryPropertyDescriptionType", propOrder = {
    "description1",
    "addvalue1",
    "addquality1",
    "addcondition1",
    "livingAreaIndicator1",
    "description2",
    "addvalue2",
    "addquality2",
    "addcondition2",
    "livingAreaIndicator2"
})
public class InquiryPropertyDescriptionType {

    protected String description1;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer addvalue1;
    @XmlSchemaType(name = "string")
    protected QualityCodeTypeEnum addquality1;
    @XmlSchemaType(name = "string")
    protected ConditionCodeTypeEnum addcondition1;
    @XmlSchemaType(name = "string")
    protected YesNoType livingAreaIndicator1;
    protected String description2;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter3 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer addvalue2;
    @XmlSchemaType(name = "string")
    protected QualityCodeTypeEnum addquality2;
    @XmlSchemaType(name = "string")
    protected ConditionCodeTypeEnum addcondition2;
    @XmlSchemaType(name = "string")
    protected YesNoType livingAreaIndicator2;

    /**
     * Gets the value of the description1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription1() {
        return description1;
    }

    /**
     * Sets the value of the description1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription1(String value) {
        this.description1 = value;
    }

    /**
     * Gets the value of the addvalue1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAddvalue1() {
        return addvalue1;
    }

    /**
     * Sets the value of the addvalue1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddvalue1(Integer value) {
        this.addvalue1 = value;
    }

    /**
     * Gets the value of the addquality1 property.
     * 
     * @return
     *     possible object is
     *     {@link QualityCodeTypeEnum }
     *     
     */
    public QualityCodeTypeEnum getAddquality1() {
        return addquality1;
    }

    /**
     * Sets the value of the addquality1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualityCodeTypeEnum }
     *     
     */
    public void setAddquality1(QualityCodeTypeEnum value) {
        this.addquality1 = value;
    }

    /**
     * Gets the value of the addcondition1 property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionCodeTypeEnum }
     *     
     */
    public ConditionCodeTypeEnum getAddcondition1() {
        return addcondition1;
    }

    /**
     * Sets the value of the addcondition1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionCodeTypeEnum }
     *     
     */
    public void setAddcondition1(ConditionCodeTypeEnum value) {
        this.addcondition1 = value;
    }

    /**
     * Gets the value of the livingAreaIndicator1 property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getLivingAreaIndicator1() {
        return livingAreaIndicator1;
    }

    /**
     * Sets the value of the livingAreaIndicator1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.YesNoType }
     *     
     */
    public void setLivingAreaIndicator1(YesNoType value) {
        this.livingAreaIndicator1 = value;
    }

    /**
     * Gets the value of the description2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription2() {
        return description2;
    }

    /**
     * Sets the value of the description2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription2(String value) {
        this.description2 = value;
    }

    /**
     * Gets the value of the addvalue2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAddvalue2() {
        return addvalue2;
    }

    /**
     * Sets the value of the addvalue2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddvalue2(Integer value) {
        this.addvalue2 = value;
    }

    /**
     * Gets the value of the addquality2 property.
     * 
     * @return
     *     possible object is
     *     {@link QualityCodeTypeEnum }
     *     
     */
    public QualityCodeTypeEnum getAddquality2() {
        return addquality2;
    }

    /**
     * Sets the value of the addquality2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link QualityCodeTypeEnum }
     *     
     */
    public void setAddquality2(QualityCodeTypeEnum value) {
        this.addquality2 = value;
    }

    /**
     * Gets the value of the addcondition2 property.
     * 
     * @return
     *     possible object is
     *     {@link ConditionCodeTypeEnum }
     *     
     */
    public ConditionCodeTypeEnum getAddcondition2() {
        return addcondition2;
    }

    /**
     * Sets the value of the addcondition2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConditionCodeTypeEnum }
     *     
     */
    public void setAddcondition2(ConditionCodeTypeEnum value) {
        this.addcondition2 = value;
    }

    /**
     * Gets the value of the livingAreaIndicator2 property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getLivingAreaIndicator2() {
        return livingAreaIndicator2;
    }

    /**
     * Sets the value of the livingAreaIndicator2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.YesNoType }
     *     
     */
    public void setLivingAreaIndicator2(YesNoType value) {
        this.livingAreaIndicator2 = value;
    }

}
