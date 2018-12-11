//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policyretrieveresponse;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Vehicle complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Vehicle"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VehicleOwners" type="{http://www.idfbins.com/PolicyRetrieveResponse}VehicleOwners"/&gt;
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="LicensePlate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Make" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Manufacturer" type="{http://www.idfbins.com/PolicyRetrieveResponse}ManufacturerType"/&gt;
 *         &lt;element name="Model" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="OffRoadStyle" type="{http://www.idfbins.com/PolicyRetrieveResponse}OffRoadStyleType"/&gt;
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="VehicleStyle" type="{http://www.idfbins.com/PolicyRetrieveResponse}VehicleStyleType"/&gt;
 *         &lt;element name="VehicleSRP" type="{http://www.idfbins.com/PolicyRetrieveResponse}VehicleSRPType"/&gt;
 *         &lt;element name="Vin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="Year" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="VehicleNumber" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vehicle", propOrder = {
    "vehicleOwners",
    "id",
    "publicID",
    "licensePlate",
    "make",
    "manufacturer",
    "model",
    "offRoadStyle",
    "state",
    "vehicleStyle",
    "vehicleSRP",
    "vin",
    "year",
    "vehicleNumber"
})
public class Vehicle {

    @XmlElement(name = "VehicleOwners", required = true)
    protected VehicleOwners vehicleOwners;
    @XmlElement(name = "ID", required = true)
    protected String id;
    @XmlElement(name = "PublicID", required = true)
    protected String publicID;
    @XmlElement(name = "LicensePlate", required = true)
    protected String licensePlate;
    @XmlElement(name = "Make", required = true)
    protected String make;
    @XmlElement(name = "Manufacturer", required = true)
    protected services.services.com.idfbins.policyxml.policyretrieveresponse.ManufacturerType manufacturer;
    @XmlElement(name = "Model", required = true)
    protected String model;
    @XmlElement(name = "OffRoadStyle", required = true)
    protected OffRoadStyleType offRoadStyle;
    @XmlElement(name = "State", required = true)
    protected String state;
    @XmlElement(name = "VehicleStyle", required = true)
    protected services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleStyleType vehicleStyle;
    @XmlElement(name = "VehicleSRP", required = true)
    protected services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleSRPType vehicleSRP;
    @XmlElement(name = "Vin", required = true)
    protected String vin;
    @XmlElement(name = "Year")
    protected int year;
    @XmlElement(name = "VehicleNumber", required = true)
    protected String vehicleNumber;

    /**
     * Gets the value of the vehicleOwners property.
     * 
     * @return
     *     possible object is
     *     {@link VehicleOwners }
     *     
     */
    public VehicleOwners getVehicleOwners() {
        return vehicleOwners;
    }

    /**
     * Sets the value of the vehicleOwners property.
     * 
     * @param value
     *     allowed object is
     *     {@link VehicleOwners }
     *     
     */
    public void setVehicleOwners(VehicleOwners value) {
        this.vehicleOwners = value;
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
     * Gets the value of the licensePlate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the value of the licensePlate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLicensePlate(String value) {
        this.licensePlate = value;
    }

    /**
     * Gets the value of the make property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the value of the make property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMake(String value) {
        this.make = value;
    }

    /**
     * Gets the value of the manufacturer property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policyretrieveresponse.ManufacturerType }
     *     
     */
    public services.services.com.idfbins.policyxml.policyretrieveresponse.ManufacturerType getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policyretrieveresponse.ManufacturerType }
     *     
     */
    public void setManufacturer(ManufacturerType value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the model property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the offRoadStyle property.
     * 
     * @return
     *     possible object is
     *     {@link OffRoadStyleType }
     *     
     */
    public OffRoadStyleType getOffRoadStyle() {
        return offRoadStyle;
    }

    /**
     * Sets the value of the offRoadStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link OffRoadStyleType }
     *     
     */
    public void setOffRoadStyle(OffRoadStyleType value) {
        this.offRoadStyle = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the vehicleStyle property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleStyleType }
     *     
     */
    public services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleStyleType getVehicleStyle() {
        return vehicleStyle;
    }

    /**
     * Sets the value of the vehicleStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleStyleType }
     *     
     */
    public void setVehicleStyle(VehicleStyleType value) {
        this.vehicleStyle = value;
    }

    /**
     * Gets the value of the vehicleSRP property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleSRPType }
     *     
     */
    public services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleSRPType getVehicleSRP() {
        return vehicleSRP;
    }

    /**
     * Sets the value of the vehicleSRP property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policyretrieveresponse.VehicleSRPType }
     *     
     */
    public void setVehicleSRP(VehicleSRPType value) {
        this.vehicleSRP = value;
    }

    /**
     * Gets the value of the vin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVin() {
        return vin;
    }

    /**
     * Sets the value of the vin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVin(String value) {
        this.vin = value;
    }

    /**
     * Gets the value of the year property.
     * 
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     * 
     */
    public void setYear(int value) {
        this.year = value;
    }

    /**
     * Gets the value of the vehicleNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVehicleNumber() {
        return vehicleNumber;
    }

    /**
     * Sets the value of the vehicleNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVehicleNumber(String value) {
        this.vehicleNumber = value;
    }

}
