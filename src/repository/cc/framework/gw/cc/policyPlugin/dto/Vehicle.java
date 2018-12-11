
package repository.cc.framework.gw.cc.policyPlugin.dto;

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
 * &lt;complexType name="Vehicle">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VehicleOwners" type="{http://www.idfbins.com/PolicyRetrieveResponse}VehicleOwners"/>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LicensePlate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Make" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Manufacturer" type="{http://www.idfbins.com/PolicyRetrieveResponse}ManufacturerType"/>
 *         &lt;element name="Model" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="OffRoadStyle" type="{http://www.idfbins.com/PolicyRetrieveResponse}OffRoadStyleType"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="VehicleStyle" type="{http://www.idfbins.com/PolicyRetrieveResponse}VehicleStyleType"/>
 *         &lt;element name="VehicleSRP" type="{http://www.idfbins.com/PolicyRetrieveResponse}VehicleSRPType"/>
 *         &lt;element name="Vin" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="year" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Vehicle", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
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
        "year"
})
public class Vehicle {

    @XmlElement(name = "VehicleOwners", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected VehicleOwners vehicleOwners;
    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;
    @XmlElement(name = "LicensePlate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String licensePlate;
    @XmlElement(name = "Make", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String make;
    @XmlElement(name = "Manufacturer", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected ManufacturerType manufacturer;
    @XmlElement(name = "Model", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String model;
    @XmlElement(name = "OffRoadStyle", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected OffRoadStyleType offRoadStyle;
    @XmlElement(name = "State", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String state;
    @XmlElement(name = "VehicleStyle", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected VehicleStyleType vehicleStyle;
    @XmlElement(name = "VehicleSRP", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected VehicleSRPType vehicleSRP;
    @XmlElement(name = "Vin", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String vin;
    @XmlElement(namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected int year;

    /**
     * Gets the value of the vehicleOwners property.
     *
     * @return possible object is
     * {@link VehicleOwners }
     */
    public VehicleOwners getVehicleOwners() {
        return vehicleOwners;
    }

    /**
     * Sets the value of the vehicleOwners property.
     *
     * @param value allowed object is
     *              {@link VehicleOwners }
     */
    public void setVehicleOwners(VehicleOwners value) {
        this.vehicleOwners = value;
    }

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
     * Gets the value of the licensePlate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLicensePlate() {
        return licensePlate;
    }

    /**
     * Sets the value of the licensePlate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLicensePlate(String value) {
        this.licensePlate = value;
    }

    /**
     * Gets the value of the make property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMake() {
        return make;
    }

    /**
     * Sets the value of the make property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMake(String value) {
        this.make = value;
    }

    /**
     * Gets the value of the manufacturer property.
     *
     * @return possible object is
     * {@link ManufacturerType }
     */
    public ManufacturerType getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the value of the manufacturer property.
     *
     * @param value allowed object is
     *              {@link ManufacturerType }
     */
    public void setManufacturer(ManufacturerType value) {
        this.manufacturer = value;
    }

    /**
     * Gets the value of the model property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setModel(String value) {
        this.model = value;
    }

    /**
     * Gets the value of the offRoadStyle property.
     *
     * @return possible object is
     * {@link OffRoadStyleType }
     */
    public OffRoadStyleType getOffRoadStyle() {
        return offRoadStyle;
    }

    /**
     * Sets the value of the offRoadStyle property.
     *
     * @param value allowed object is
     *              {@link OffRoadStyleType }
     */
    public void setOffRoadStyle(OffRoadStyleType value) {
        this.offRoadStyle = value;
    }

    /**
     * Gets the value of the state property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the vehicleStyle property.
     *
     * @return possible object is
     * {@link VehicleStyleType }
     */
    public VehicleStyleType getVehicleStyle() {
        return vehicleStyle;
    }

    /**
     * Sets the value of the vehicleStyle property.
     *
     * @param value allowed object is
     *              {@link VehicleStyleType }
     */
    public void setVehicleStyle(VehicleStyleType value) {
        this.vehicleStyle = value;
    }

    /**
     * Gets the value of the vehicleSRP property.
     *
     * @return possible object is
     * {@link VehicleSRPType }
     */
    public VehicleSRPType getVehicleSRP() {
        return vehicleSRP;
    }

    /**
     * Sets the value of the vehicleSRP property.
     *
     * @param value allowed object is
     *              {@link VehicleSRPType }
     */
    public void setVehicleSRP(VehicleSRPType value) {
        this.vehicleSRP = value;
    }

    /**
     * Gets the value of the vin property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVin() {
        return vin;
    }

    /**
     * Sets the value of the vin property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVin(String value) {
        this.vin = value;
    }

    /**
     * Gets the value of the year property.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the value of the year property.
     */
    public void setYear(int value) {
        this.year = value;
    }


    @Override
    public String toString() {
        return (this.year + " " + this.make + " " + this.model + " " + this.vin).trim();
    }

}
