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
 * <p>Java class for InlandMarine complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="InlandMarine"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="IMType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="IMNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Value" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemBrand" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemQuantity" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemLength" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemSerial" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemYear" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemMake" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemModel" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ItemVin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="TypeOfUsage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ExistingDamage" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="InspectedFlag" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Radius" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="CargoType" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="NumberOfMiles" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="AppraisalDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="EndNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="VehicleOwners" type="{http://www.idfbins.com/Policy}VehicleOwners"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InlandMarine", propOrder = {
    "imType",
    "imNumber",
    "value",
    "itemBrand",
    "itemQuantity",
    "itemLength",
    "itemSerial",
    "itemYear",
    "itemMake",
    "itemModel",
    "itemVin",
    "typeOfUsage",
    "existingDamage",
    "inspectedFlag",
    "radius",
    "cargoType",
    "numberOfMiles",
    "appraisalDate",
    "notes",
    "publicID",
    "description",
    "id",
    "endNumber",
    "vehicleOwners"
})
public class InlandMarine {

    @XmlElement(name = "IMType", required = true)
    protected String imType;
    @XmlElement(name = "IMNumber", required = true)
    protected String imNumber;
    @XmlElement(name = "Value", required = true)
    protected String value;
    @XmlElement(name = "ItemBrand", required = true)
    protected String itemBrand;
    @XmlElement(name = "ItemQuantity", required = true)
    protected String itemQuantity;
    @XmlElement(name = "ItemLength", required = true)
    protected String itemLength;
    @XmlElement(name = "ItemSerial", required = true)
    protected String itemSerial;
    @XmlElement(name = "ItemYear", required = true)
    protected String itemYear;
    @XmlElement(name = "ItemMake", required = true)
    protected String itemMake;
    @XmlElement(name = "ItemModel", required = true)
    protected String itemModel;
    @XmlElement(name = "ItemVin", required = true)
    protected String itemVin;
    @XmlElement(name = "TypeOfUsage", required = true)
    protected String typeOfUsage;
    @XmlElement(name = "ExistingDamage", required = true)
    protected String existingDamage;
    @XmlElement(name = "InspectedFlag", required = true)
    protected String inspectedFlag;
    @XmlElement(name = "Radius", required = true)
    protected String radius;
    @XmlElement(name = "CargoType", required = true)
    protected String cargoType;
    @XmlElement(name = "NumberOfMiles", required = true)
    protected String numberOfMiles;
    @XmlElement(name = "AppraisalDate", required = true)
    protected String appraisalDate;
    @XmlElement(name = "Notes", required = true)
    protected String notes;
    @XmlElement(name = "PublicID", required = true)
    protected String publicID;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "EndNumber", required = true)
    protected String endNumber;
    @XmlElement(name = "VehicleOwners", required = true)
    protected services.services.com.idfbins.policyxml.policy.VehicleOwners vehicleOwners;

    /**
     * Gets the value of the imType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIMType() {
        return imType;
    }

    /**
     * Sets the value of the imType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIMType(String value) {
        this.imType = value;
    }

    /**
     * Gets the value of the imNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIMNumber() {
        return imNumber;
    }

    /**
     * Sets the value of the imNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIMNumber(String value) {
        this.imNumber = value;
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
     * Gets the value of the itemBrand property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemBrand() {
        return itemBrand;
    }

    /**
     * Sets the value of the itemBrand property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemBrand(String value) {
        this.itemBrand = value;
    }

    /**
     * Gets the value of the itemQuantity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemQuantity() {
        return itemQuantity;
    }

    /**
     * Sets the value of the itemQuantity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemQuantity(String value) {
        this.itemQuantity = value;
    }

    /**
     * Gets the value of the itemLength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemLength() {
        return itemLength;
    }

    /**
     * Sets the value of the itemLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemLength(String value) {
        this.itemLength = value;
    }

    /**
     * Gets the value of the itemSerial property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemSerial() {
        return itemSerial;
    }

    /**
     * Sets the value of the itemSerial property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemSerial(String value) {
        this.itemSerial = value;
    }

    /**
     * Gets the value of the itemYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemYear() {
        return itemYear;
    }

    /**
     * Sets the value of the itemYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemYear(String value) {
        this.itemYear = value;
    }

    /**
     * Gets the value of the itemMake property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemMake() {
        return itemMake;
    }

    /**
     * Sets the value of the itemMake property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemMake(String value) {
        this.itemMake = value;
    }

    /**
     * Gets the value of the itemModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemModel() {
        return itemModel;
    }

    /**
     * Sets the value of the itemModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemModel(String value) {
        this.itemModel = value;
    }

    /**
     * Gets the value of the itemVin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getItemVin() {
        return itemVin;
    }

    /**
     * Sets the value of the itemVin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setItemVin(String value) {
        this.itemVin = value;
    }

    /**
     * Gets the value of the typeOfUsage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    /**
     * Sets the value of the typeOfUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTypeOfUsage(String value) {
        this.typeOfUsage = value;
    }

    /**
     * Gets the value of the existingDamage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExistingDamage() {
        return existingDamage;
    }

    /**
     * Sets the value of the existingDamage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExistingDamage(String value) {
        this.existingDamage = value;
    }

    /**
     * Gets the value of the inspectedFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInspectedFlag() {
        return inspectedFlag;
    }

    /**
     * Sets the value of the inspectedFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInspectedFlag(String value) {
        this.inspectedFlag = value;
    }

    /**
     * Gets the value of the radius property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRadius() {
        return radius;
    }

    /**
     * Sets the value of the radius property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRadius(String value) {
        this.radius = value;
    }

    /**
     * Gets the value of the cargoType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCargoType() {
        return cargoType;
    }

    /**
     * Sets the value of the cargoType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCargoType(String value) {
        this.cargoType = value;
    }

    /**
     * Gets the value of the numberOfMiles property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfMiles() {
        return numberOfMiles;
    }

    /**
     * Sets the value of the numberOfMiles property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfMiles(String value) {
        this.numberOfMiles = value;
    }

    /**
     * Gets the value of the appraisalDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAppraisalDate() {
        return appraisalDate;
    }

    /**
     * Sets the value of the appraisalDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAppraisalDate(String value) {
        this.appraisalDate = value;
    }

    /**
     * Gets the value of the notes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotes(String value) {
        this.notes = value;
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
     * Gets the value of the endNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndNumber() {
        return endNumber;
    }

    /**
     * Sets the value of the endNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndNumber(String value) {
        this.endNumber = value;
    }

    /**
     * Gets the value of the vehicleOwners property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.VehicleOwners }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.VehicleOwners getVehicleOwners() {
        return vehicleOwners;
    }

    /**
     * Sets the value of the vehicleOwners property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.VehicleOwners }
     *     
     */
    public void setVehicleOwners(VehicleOwners value) {
        this.vehicleOwners = value;
    }

}
