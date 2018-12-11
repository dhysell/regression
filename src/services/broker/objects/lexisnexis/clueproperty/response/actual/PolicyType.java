//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for policyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="policyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="issuer" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="startdate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enddate" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="holder" type="{http://cp.com/rules/client}policyHolderType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="policyCoverageInfo" type="{http://cp.com/rules/client}policyCoverageInfoType" minOccurs="0"/>
 *         &lt;element name="stateOfIssuance" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="policyFaceAmounts" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="category" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="type" type="{http://cp.com/rules/client}policyTypeDefn" />
 *       &lt;attribute name="number" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "policyType", propOrder = {
    "issuer",
    "startdate",
    "enddate",
    "holderList",
    "parameterList",
    "policyCoverageInfo",
    "stateOfIssuance",
    "policyFaceAmounts"
})
public class PolicyType {

    protected String issuer;
    protected String startdate;
    protected String enddate;
    @XmlElement(name = "holder")
    protected List<PolicyHolderType> holderList;
    @XmlElement(name = "parameter")
    protected List<ParameterType> parameterList;
    protected PolicyCoverageInfoType policyCoverageInfo;
    protected String stateOfIssuance;
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger policyFaceAmounts;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "category")
    protected String category;
    @XmlAttribute(name = "type")
    protected PolicyTypeDefnEnum type;
    @XmlAttribute(name = "number", required = true)
    protected String number;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

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
     * Gets the value of the startdate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartdate() {
        return startdate;
    }

    /**
     * Sets the value of the startdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartdate(String value) {
        this.startdate = value;
    }

    /**
     * Gets the value of the enddate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnddate() {
        return enddate;
    }

    /**
     * Sets the value of the enddate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnddate(String value) {
        this.enddate = value;
    }

    /**
     * Gets the value of the holderList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the holderList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHolderList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicyHolderType }
     * 
     * 
     */
    public List<PolicyHolderType> getHolderList() {
        if (holderList == null) {
            holderList = new ArrayList<PolicyHolderType>();
        }
        return this.holderList;
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.clueproperty.response.actual.ParameterType }
     * 
     * 
     */
    public List<ParameterType> getParameterList() {
        if (parameterList == null) {
            parameterList = new ArrayList<ParameterType>();
        }
        return this.parameterList;
    }

    /**
     * Gets the value of the policyCoverageInfo property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.PolicyCoverageInfoType }
     *     
     */
    public PolicyCoverageInfoType getPolicyCoverageInfo() {
        return policyCoverageInfo;
    }

    /**
     * Sets the value of the policyCoverageInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.PolicyCoverageInfoType }
     *     
     */
    public void setPolicyCoverageInfo(PolicyCoverageInfoType value) {
        this.policyCoverageInfo = value;
    }

    /**
     * Gets the value of the stateOfIssuance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStateOfIssuance() {
        return stateOfIssuance;
    }

    /**
     * Sets the value of the stateOfIssuance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStateOfIssuance(String value) {
        this.stateOfIssuance = value;
    }

    /**
     * Gets the value of the policyFaceAmounts property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPolicyFaceAmounts() {
        return policyFaceAmounts;
    }

    /**
     * Sets the value of the policyFaceAmounts property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPolicyFaceAmounts(BigInteger value) {
        this.policyFaceAmounts = value;
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
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCategory(String value) {
        this.category = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.PolicyTypeDefnEnum }
     *     
     */
    public PolicyTypeDefnEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueproperty.response.actual.PolicyTypeDefnEnum }
     *     
     */
    public void setType(PolicyTypeDefnEnum value) {
        this.type = value;
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

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

}
