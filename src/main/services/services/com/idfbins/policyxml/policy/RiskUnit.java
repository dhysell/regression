//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RiskUnit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RiskUnit"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Vehicle" type="{http://www.idfbins.com/Policy}Vehicle" minOccurs="0"/&gt;
 *         &lt;element name="Building" type="{http://www.idfbins.com/Policy}Building" minOccurs="0"/&gt;
 *         &lt;element name="Liability" type="{http://www.idfbins.com/Policy}Liability" minOccurs="0"/&gt;
 *         &lt;element name="InlandMarineItem" type="{http://www.idfbins.com/Policy}InlandMarine" minOccurs="0"/&gt;
 *         &lt;element name="Umbrella" type="{http://www.idfbins.com/Policy}Umbrella" minOccurs="0"/&gt;
 *         &lt;element name="Crop" type="{http://www.idfbins.com/Policy}Crop" minOccurs="0"/&gt;
 *         &lt;element name="Coverages" type="{http://www.idfbins.com/Policy}Coverages"/&gt;
 *         &lt;element name="Surcharges" type="{http://www.idfbins.com/Policy}Surcharges" minOccurs="0"/&gt;
 *         &lt;element name="ClassCode" type="{http://www.idfbins.com/Policy}ClassCode" minOccurs="0"/&gt;
 *         &lt;element name="Displayname" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="RUNumber" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="SubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Retired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="Sequence" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LocationID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="FBRUCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="relatedRiskUnitId" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="documents" type="{http://www.idfbins.com/Policy}Documents" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="RiskUnitType" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiskUnit", propOrder = {
    "id",
    "vehicle",
    "building",
    "liability",
    "inlandMarineItem",
    "umbrella",
    "crop",
    "coverages",
    "surcharges",
    "classCode",
    "displayname",
    "publicID",
    "ruNumber",
    "subType",
    "retired",
    "sequence",
    "locationID",
    "fbruCategory",
    "relatedRiskUnitId",
    "documents"
})
public class RiskUnit {

    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "Vehicle")
    protected services.services.com.idfbins.policyxml.policy.Vehicle vehicle;
    @XmlElement(name = "Building")
    protected Building building;
    @XmlElement(name = "Liability")
    protected services.services.com.idfbins.policyxml.policy.Liability liability;
    @XmlElement(name = "InlandMarineItem")
    protected InlandMarine inlandMarineItem;
    @XmlElement(name = "Umbrella")
    protected Umbrella umbrella;
    @XmlElement(name = "Crop")
    protected Crop crop;
    @XmlElement(name = "Coverages", required = true)
    protected Coverages coverages;
    @XmlElement(name = "Surcharges")
    protected Surcharges surcharges;
    @XmlElement(name = "ClassCode")
    protected services.services.com.idfbins.policyxml.policy.ClassCode classCode;
    @XmlElement(name = "Displayname", required = true)
    protected String displayname;
    @XmlElement(name = "PublicID", required = true)
    protected String publicID;
    @XmlElement(name = "RUNumber")
    protected long ruNumber;
    @XmlElement(name = "SubType")
    protected String subType;
    @XmlElement(name = "Retired")
    protected boolean retired;
    @XmlElement(name = "Sequence")
    protected String sequence;
    @XmlElement(name = "LocationID", required = true)
    protected String locationID;
    @XmlElement(name = "FBRUCategory")
    protected String fbruCategory;
    protected Long relatedRiskUnitId;
    protected services.services.com.idfbins.policyxml.policy.Documents documents;
    @XmlAttribute(name = "RiskUnitType")
    protected String riskUnitType;

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
     * Gets the value of the vehicle property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.Vehicle }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.Vehicle }
     *     
     */
    public void setVehicle(Vehicle value) {
        this.vehicle = value;
    }

    /**
     * Gets the value of the building property.
     * 
     * @return
     *     possible object is
     *     {@link Building }
     *     
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Sets the value of the building property.
     * 
     * @param value
     *     allowed object is
     *     {@link Building }
     *     
     */
    public void setBuilding(Building value) {
        this.building = value;
    }

    /**
     * Gets the value of the liability property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.Liability }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.Liability getLiability() {
        return liability;
    }

    /**
     * Sets the value of the liability property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.Liability }
     *     
     */
    public void setLiability(Liability value) {
        this.liability = value;
    }

    /**
     * Gets the value of the inlandMarineItem property.
     * 
     * @return
     *     possible object is
     *     {@link InlandMarine }
     *     
     */
    public InlandMarine getInlandMarineItem() {
        return inlandMarineItem;
    }

    /**
     * Sets the value of the inlandMarineItem property.
     * 
     * @param value
     *     allowed object is
     *     {@link InlandMarine }
     *     
     */
    public void setInlandMarineItem(InlandMarine value) {
        this.inlandMarineItem = value;
    }

    /**
     * Gets the value of the umbrella property.
     * 
     * @return
     *     possible object is
     *     {@link Umbrella }
     *     
     */
    public Umbrella getUmbrella() {
        return umbrella;
    }

    /**
     * Sets the value of the umbrella property.
     * 
     * @param value
     *     allowed object is
     *     {@link Umbrella }
     *     
     */
    public void setUmbrella(Umbrella value) {
        this.umbrella = value;
    }

    /**
     * Gets the value of the crop property.
     * 
     * @return
     *     possible object is
     *     {@link Crop }
     *     
     */
    public Crop getCrop() {
        return crop;
    }

    /**
     * Sets the value of the crop property.
     * 
     * @param value
     *     allowed object is
     *     {@link Crop }
     *     
     */
    public void setCrop(Crop value) {
        this.crop = value;
    }

    /**
     * Gets the value of the coverages property.
     * 
     * @return
     *     possible object is
     *     {@link Coverages }
     *     
     */
    public Coverages getCoverages() {
        return coverages;
    }

    /**
     * Sets the value of the coverages property.
     * 
     * @param value
     *     allowed object is
     *     {@link Coverages }
     *     
     */
    public void setCoverages(Coverages value) {
        this.coverages = value;
    }

    /**
     * Gets the value of the surcharges property.
     * 
     * @return
     *     possible object is
     *     {@link Surcharges }
     *     
     */
    public Surcharges getSurcharges() {
        return surcharges;
    }

    /**
     * Sets the value of the surcharges property.
     * 
     * @param value
     *     allowed object is
     *     {@link Surcharges }
     *     
     */
    public void setSurcharges(Surcharges value) {
        this.surcharges = value;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.ClassCode }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.ClassCode getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.ClassCode }
     *     
     */
    public void setClassCode(ClassCode value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the displayname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * Sets the value of the displayname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDisplayname(String value) {
        this.displayname = value;
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
     * Gets the value of the ruNumber property.
     * 
     */
    public long getRUNumber() {
        return ruNumber;
    }

    /**
     * Sets the value of the ruNumber property.
     * 
     */
    public void setRUNumber(long value) {
        this.ruNumber = value;
    }

    /**
     * Gets the value of the subType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Sets the value of the subType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubType(String value) {
        this.subType = value;
    }

    /**
     * Gets the value of the retired property.
     * 
     */
    public boolean isRetired() {
        return retired;
    }

    /**
     * Sets the value of the retired property.
     * 
     */
    public void setRetired(boolean value) {
        this.retired = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequence(String value) {
        this.sequence = value;
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

    /**
     * Gets the value of the fbruCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFBRUCategory() {
        return fbruCategory;
    }

    /**
     * Sets the value of the fbruCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFBRUCategory(String value) {
        this.fbruCategory = value;
    }

    /**
     * Gets the value of the relatedRiskUnitId property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getRelatedRiskUnitId() {
        return relatedRiskUnitId;
    }

    /**
     * Sets the value of the relatedRiskUnitId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setRelatedRiskUnitId(Long value) {
        this.relatedRiskUnitId = value;
    }

    /**
     * Gets the value of the documents property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.Documents }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.Documents getDocuments() {
        return documents;
    }

    /**
     * Sets the value of the documents property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.Documents }
     *     
     */
    public void setDocuments(Documents value) {
        this.documents = value;
    }

    /**
     * Gets the value of the riskUnitType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRiskUnitType() {
        return riskUnitType;
    }

    /**
     * Sets the value of the riskUnitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRiskUnitType(String value) {
        this.riskUnitType = value;
    }

}
