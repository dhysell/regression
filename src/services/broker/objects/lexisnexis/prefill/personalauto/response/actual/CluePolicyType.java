//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cluePolicyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cluePolicyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="issuer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element name="auto_type" type="{http://cp.com/rules/client}clueAutoPolicyType"/>
 *           &lt;element name="property_type" type="{http://cp.com/rules/client}cluePropertyPolicyType"/>
 *         &lt;/choice>
 *         &lt;element name="classification" type="{http://cp.com/rules/client}HistoryType" minOccurs="0"/>
 *         &lt;element name="fsi_policy_number" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_policy_type" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_issuer" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cluePolicyType", propOrder = {
    "issuer",
    "autoType",
    "propertyType",
    "classification",
    "fsiPolicyNumber",
    "fsiPolicyType",
    "fsiIssuer"
})
public class CluePolicyType {

    protected String issuer;
    @XmlElement(name = "auto_type")
    @XmlSchemaType(name = "string")
    protected AutoPolicyTypeEnum autoType;
    @XmlElement(name = "property_type")
    @XmlSchemaType(name = "string")
    protected PropertyPolicyTypeEnum propertyType;
    @XmlSchemaType(name = "string")
    protected HistoryEnum classification;
    @XmlElement(name = "fsi_policy_number")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiPolicyNumber;
    @XmlElement(name = "fsi_policy_type")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiPolicyType;
    @XmlElement(name = "fsi_issuer")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiIssuer;
    @XmlAttribute(name = "number")
    protected String number;

    /**
     * Gets the value of the issuer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the value of the issuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIssuer(String value) {
        this.issuer = value;
    }

    /**
     * Gets the value of the autoType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.AutoPolicyTypeEnum }
     *     
     */
    public AutoPolicyTypeEnum getAutoType() {
        return autoType;
    }

    /**
     * Sets the value of the autoType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.AutoPolicyTypeEnum }
     *     
     */
    public void setAutoType(AutoPolicyTypeEnum value) {
        this.autoType = value;
    }

    /**
     * Gets the value of the propertyType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.PropertyPolicyTypeEnum }
     *     
     */
    public PropertyPolicyTypeEnum getPropertyType() {
        return propertyType;
    }

    /**
     * Sets the value of the propertyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.PropertyPolicyTypeEnum }
     *     
     */
    public void setPropertyType(PropertyPolicyTypeEnum value) {
        this.propertyType = value;
    }

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.HistoryEnum }
     *     
     */
    public HistoryEnum getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.HistoryEnum }
     *     
     */
    public void setClassification(HistoryEnum value) {
        this.classification = value;
    }

    /**
     * Gets the value of the fsiPolicyNumber property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiPolicyNumber() {
        return fsiPolicyNumber;
    }

    /**
     * Sets the value of the fsiPolicyNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiPolicyNumber(FSIEnum value) {
        this.fsiPolicyNumber = value;
    }

    /**
     * Gets the value of the fsiPolicyType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiPolicyType() {
        return fsiPolicyType;
    }

    /**
     * Sets the value of the fsiPolicyType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiPolicyType(FSIEnum value) {
        this.fsiPolicyType = value;
    }

    /**
     * Gets the value of the fsiIssuer property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiIssuer() {
        return fsiIssuer;
    }

    /**
     * Sets the value of the fsiIssuer property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiIssuer(FSIEnum value) {
        this.fsiIssuer = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

}
