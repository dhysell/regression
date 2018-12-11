
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RiskUnit complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RiskUnit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Vehicle" type="{http://www.idfbins.com/PolicyRetrieveResponse}Vehicle" minOccurs="0"/>
 *         &lt;element name="Building" type="{http://www.idfbins.com/PolicyRetrieveResponse}Building" minOccurs="0"/>
 *         &lt;element name="Coverages" type="{http://www.idfbins.com/PolicyRetrieveResponse}Coverages"/>
 *         &lt;element name="ClassCode" type="{http://www.idfbins.com/PolicyRetrieveResponse}ClassCode" minOccurs="0"/>
 *         &lt;element name="Displayname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RUNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SubType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Retired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Sequence" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LocationID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="RiskUnitType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiskUnit", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "id",
        "vehicle",
        "building",
        "coverages",
        "classCode",
        "displayname",
        "publicID",
        "ruNumber",
        "subType",
        "retired",
        "sequence",
        "locationID",
        "riskUnitType"
})
public class RiskUnit {

    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "Vehicle", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected Vehicle vehicle;
    @XmlElement(name = "Building", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected Building building;
    @XmlElement(name = "Coverages", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Coverages coverages;
    @XmlElement(name = "ClassCode", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected ClassCode classCode;
    @XmlElement(name = "Displayname", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String displayname;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;
    @XmlElement(name = "RUNumber", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected int ruNumber;
    @XmlElement(name = "SubType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String subType;
    @XmlElement(name = "Retired", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected boolean retired;
    @XmlElement(name = "Sequence", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String sequence;
    @XmlElement(name = "LocationID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String locationID;
    @XmlElement(name = "RiskUnitType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String riskUnitType;

    /**
     * Gets the value of the id property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getID() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setID(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the vehicle property.
     *
     * @return possible object is
     * {@link Vehicle }
     */
    public Vehicle getVehicle() {
        return vehicle;
    }

    /**
     * Sets the value of the vehicle property.
     *
     * @param value allowed object is
     *              {@link Vehicle }
     */
    public void setVehicle(Vehicle value) {
        this.vehicle = value;
    }

    /**
     * Gets the value of the building property.
     *
     * @return possible object is
     * {@link Building }
     */
    public Building getBuilding() {
        return building;
    }

    /**
     * Sets the value of the building property.
     *
     * @param value allowed object is
     *              {@link Building }
     */
    public void setBuilding(Building value) {
        this.building = value;
    }

    /**
     * Gets the value of the coverages property.
     *
     * @return possible object is
     * {@link Coverages }
     */
    public Coverages getCoverages() {
        return coverages;
    }

    /**
     * Sets the value of the coverages property.
     *
     * @param value allowed object is
     *              {@link Coverages }
     */
    public void setCoverages(Coverages value) {
        this.coverages = value;
    }

    /**
     * Gets the value of the classCode property.
     *
     * @return possible object is
     * {@link ClassCode }
     */
    public ClassCode getClassCode() {
        return classCode;
    }

    /**
     * Sets the value of the classCode property.
     *
     * @param value allowed object is
     *              {@link ClassCode }
     */
    public void setClassCode(ClassCode value) {
        this.classCode = value;
    }

    /**
     * Gets the value of the displayname property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDisplayname() {
        return displayname;
    }

    /**
     * Sets the value of the displayname property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDisplayname(String value) {
        this.displayname = value;
    }

    /**
     * Gets the value of the publicID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * Sets the value of the publicID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPublicID(String value) {
        this.publicID = value;
    }

    /**
     * Gets the value of the ruNumber property.
     */
    public int getRUNumber() {
        return ruNumber;
    }

    /**
     * Sets the value of the ruNumber property.
     */
    public void setRUNumber(int value) {
        this.ruNumber = value;
    }

    /**
     * Gets the value of the subType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSubType() {
        return subType;
    }

    /**
     * Sets the value of the subType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSubType(String value) {
        this.subType = value;
    }

    /**
     * Gets the value of the retired property.
     */
    public boolean isRetired() {
        return retired;
    }

    /**
     * Sets the value of the retired property.
     */
    public void setRetired(boolean value) {
        this.retired = value;
    }

    /**
     * Gets the value of the sequence property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSequence(String value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the locationID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLocationID() {
        return locationID;
    }

    /**
     * Sets the value of the locationID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocationID(String value) {
        this.locationID = value;
    }

    /**
     * Gets the value of the riskUnitType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRiskUnitType() {
        return riskUnitType;
    }

    /**
     * Sets the value of the riskUnitType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRiskUnitType(String value) {
        this.riskUnitType = value;
    }

    @Override
    public String toString() {
        return this.vehicle.toString();
    }

}
