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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoverageType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CoverageType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="SubCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ClassTranCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ClassGroupCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Section" type="{http://www.idfbins.com/Policy}CoverageCategory" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="CoverageCategory" type="{http://www.idfbins.com/Policy}CoverageCategory" minOccurs="0"/&gt;
 *         &lt;element name="CoverageSubCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoverageType", propOrder = {
    "code",
    "subCode",
    "displayName",
    "description",
    "classTranCode",
    "classGroupCode",
    "section",
    "coverageCategory",
    "coverageSubCategory"
})
public class CoverageType {

    @XmlElement(name = "Code", required = true)
    protected String code;
    @XmlElement(name = "SubCode")
    protected String subCode;
    @XmlElement(name = "DisplayName", required = true)
    protected String displayName;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "ClassTranCode")
    protected String classTranCode;
    @XmlElement(name = "ClassGroupCode")
    protected String classGroupCode;
    @XmlElement(name = "Section")
    @XmlSchemaType(name = "string")
    protected List<services.services.com.idfbins.policyxml.policy.CoverageCategory> section;
    @XmlElement(name = "CoverageCategory")
    @XmlSchemaType(name = "string")
    protected services.services.com.idfbins.policyxml.policy.CoverageCategory coverageCategory;
    @XmlElement(name = "CoverageSubCategory")
    protected String coverageSubCategory;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the subCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubCode() {
        return subCode;
    }

    /**
     * Sets the value of the subCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubCode(String value) {
        this.subCode = value;
    }

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the classTranCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassTranCode() {
        return classTranCode;
    }

    /**
     * Sets the value of the classTranCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassTranCode(String value) {
        this.classTranCode = value;
    }

    /**
     * Gets the value of the classGroupCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassGroupCode() {
        return classGroupCode;
    }

    /**
     * Sets the value of the classGroupCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassGroupCode(String value) {
        this.classGroupCode = value;
    }

    /**
     * Gets the value of the section property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the section property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link services.services.com.idfbins.policyxml.policy.CoverageCategory }
     * 
     * 
     */
    public List<services.services.com.idfbins.policyxml.policy.CoverageCategory> getSection() {
        if (section == null) {
            section = new ArrayList<services.services.com.idfbins.policyxml.policy.CoverageCategory>();
        }
        return this.section;
    }

    /**
     * Gets the value of the coverageCategory property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.CoverageCategory }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.CoverageCategory getCoverageCategory() {
        return coverageCategory;
    }

    /**
     * Sets the value of the coverageCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.CoverageCategory }
     *     
     */
    public void setCoverageCategory(CoverageCategory value) {
        this.coverageCategory = value;
    }

    /**
     * Gets the value of the coverageSubCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCoverageSubCategory() {
        return coverageSubCategory;
    }

    /**
     * Sets the value of the coverageSubCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCoverageSubCategory(String value) {
        this.coverageSubCategory = value;
    }

}
