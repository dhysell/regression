//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoverageTerm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CoverageTerm"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Code" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ExposureLimit" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="IncidentLimit" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="DisplayValue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ValueType" type="{http://www.idfbins.com/Policy}ValueTypeEnumeration" minOccurs="0"/&gt;
 *         &lt;element name="CostData" type="{http://www.idfbins.com/Policy}CostData" minOccurs="0"/&gt;
 *         &lt;element name="ModelType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="RestrictionModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="AggregateModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoverageTerm", propOrder = {
    "id",
    "code",
    "displayName",
    "description",
    "exposureLimit",
    "incidentLimit",
    "displayValue",
    "value",
    "publicID",
    "valueType",
    "costData",
    "modelType",
    "restrictionModel",
    "aggregateModel"
})
public class CoverageTerm {

    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "Code", required = true)
    protected String code;
    @XmlElement(name = "DisplayName", required = true)
    protected String displayName;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "ExposureLimit")
    protected long exposureLimit;
    @XmlElement(name = "IncidentLimit")
    protected long incidentLimit;
    @XmlElement(name = "DisplayValue", required = true)
    protected String displayValue;
    @XmlElement(name = "Value", required = true)
    protected String value;
    @XmlElement(name = "PublicID", required = true)
    protected String publicID;
    @XmlElement(name = "ValueType")
    @XmlSchemaType(name = "string")
    protected ValueTypeEnumeration valueType;
    @XmlElement(name = "CostData")
    protected CostData costData;
    @XmlElement(name = "ModelType")
    protected String modelType;
    @XmlElement(name = "RestrictionModel")
    protected String restrictionModel;
    @XmlElement(name = "AggregateModel")
    protected String aggregateModel;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getID() {
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
    public void setID(String value) {
        this.id = value;
    }

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
     * Gets the value of the exposureLimit property.
     * 
     */
    public long getExposureLimit() {
        return exposureLimit;
    }

    /**
     * Sets the value of the exposureLimit property.
     * 
     */
    public void setExposureLimit(long value) {
        this.exposureLimit = value;
    }

    /**
     * Gets the value of the incidentLimit property.
     * 
     */
    public long getIncidentLimit() {
        return incidentLimit;
    }

    /**
     * Sets the value of the incidentLimit property.
     * 
     */
    public void setIncidentLimit(long value) {
        this.incidentLimit = value;
    }

    /**
     * Gets the value of the displayValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayValue() {
        return displayValue;
    }

    /**
     * Sets the value of the displayValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayValue(String value) {
        this.displayValue = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the publicID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * Sets the value of the publicID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPublicID(String value) {
        this.publicID = value;
    }

    /**
     * Gets the value of the valueType property.
     * 
     * @return
     *     possible object is
     *     {@link ValueTypeEnumeration }
     *     
     */
    public ValueTypeEnumeration getValueType() {
        return valueType;
    }

    /**
     * Sets the value of the valueType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ValueTypeEnumeration }
     *     
     */
    public void setValueType(ValueTypeEnumeration value) {
        this.valueType = value;
    }

    /**
     * Gets the value of the costData property.
     * 
     * @return
     *     possible object is
     *     {@link CostData }
     *     
     */
    public CostData getCostData() {
        return costData;
    }

    /**
     * Sets the value of the costData property.
     * 
     * @param value
     *     allowed object is
     *     {@link CostData }
     *     
     */
    public void setCostData(CostData value) {
        this.costData = value;
    }

    /**
     * Gets the value of the modelType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelType() {
        return modelType;
    }

    /**
     * Sets the value of the modelType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelType(String value) {
        this.modelType = value;
    }

    /**
     * Gets the value of the restrictionModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestrictionModel() {
        return restrictionModel;
    }

    /**
     * Sets the value of the restrictionModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestrictionModel(String value) {
        this.restrictionModel = value;
    }

    /**
     * Gets the value of the aggregateModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAggregateModel() {
        return aggregateModel;
    }

    /**
     * Sets the value of the aggregateModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAggregateModel(String value) {
        this.aggregateModel = value;
    }

}
