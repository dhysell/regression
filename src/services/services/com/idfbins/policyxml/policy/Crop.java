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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Crop complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Crop"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Acres" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CropCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CropCodeDescription" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FireInsuranceAmount" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="HailInsuranceAmount" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="WindInsuranceAmount" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="StubbleFlag" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="NameOfField" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LocationID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Crop", propOrder = {
    "id",
    "publicID",
    "description",
    "acres",
    "cropCode",
    "cropCodeDescription",
    "fireInsuranceAmount",
    "hailInsuranceAmount",
    "windInsuranceAmount",
    "stubbleFlag",
    "nameOfField",
    "locationID"
})
public class Crop {

    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "PublicID", required = true)
    protected String publicID;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "Acres", required = true)
    protected String acres;
    @XmlElement(name = "CropCode", required = true)
    protected String cropCode;
    @XmlElement(name = "CropCodeDescription", required = true)
    protected String cropCodeDescription;
    @XmlElement(name = "FireInsuranceAmount", required = true)
    protected String fireInsuranceAmount;
    @XmlElement(name = "HailInsuranceAmount", required = true)
    protected String hailInsuranceAmount;
    @XmlElement(name = "WindInsuranceAmount", required = true)
    protected String windInsuranceAmount;
    @XmlElement(name = "StubbleFlag", required = true)
    protected String stubbleFlag;
    @XmlElement(name = "NameOfField", required = true)
    protected String nameOfField;
    @XmlElement(name = "LocationID", required = true)
    protected String locationID;

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
     * Gets the value of the acres property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcres() {
        return acres;
    }

    /**
     * Sets the value of the acres property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcres(String value) {
        this.acres = value;
    }

    /**
     * Gets the value of the cropCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCropCode() {
        return cropCode;
    }

    /**
     * Sets the value of the cropCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCropCode(String value) {
        this.cropCode = value;
    }

    /**
     * Gets the value of the cropCodeDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCropCodeDescription() {
        return cropCodeDescription;
    }

    /**
     * Sets the value of the cropCodeDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCropCodeDescription(String value) {
        this.cropCodeDescription = value;
    }

    /**
     * Gets the value of the fireInsuranceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFireInsuranceAmount() {
        return fireInsuranceAmount;
    }

    /**
     * Sets the value of the fireInsuranceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFireInsuranceAmount(String value) {
        this.fireInsuranceAmount = value;
    }

    /**
     * Gets the value of the hailInsuranceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHailInsuranceAmount() {
        return hailInsuranceAmount;
    }

    /**
     * Sets the value of the hailInsuranceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHailInsuranceAmount(String value) {
        this.hailInsuranceAmount = value;
    }

    /**
     * Gets the value of the windInsuranceAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWindInsuranceAmount() {
        return windInsuranceAmount;
    }

    /**
     * Sets the value of the windInsuranceAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWindInsuranceAmount(String value) {
        this.windInsuranceAmount = value;
    }

    /**
     * Gets the value of the stubbleFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStubbleFlag() {
        return stubbleFlag;
    }

    /**
     * Sets the value of the stubbleFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStubbleFlag(String value) {
        this.stubbleFlag = value;
    }

    /**
     * Gets the value of the nameOfField property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameOfField() {
        return nameOfField;
    }

    /**
     * Sets the value of the nameOfField property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameOfField(String value) {
        this.nameOfField = value;
    }

    /**
     * Gets the value of the locationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationID() {
        return locationID;
    }

    /**
     * Sets the value of the locationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationID(String value) {
        this.locationID = value;
    }

}
