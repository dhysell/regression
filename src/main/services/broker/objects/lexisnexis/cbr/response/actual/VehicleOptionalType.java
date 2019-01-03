//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vehicleOptionalType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vehicleOptionalType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="anti_lock_brakes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="front_wheel_drive" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="four_wheel_drive" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="power_steering" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="power_brakes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="power_windows" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="air_conditioning" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tilt_wheel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="security" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restraint_airbags" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="restraint_belts" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vehicleOptionalType", propOrder = {
    "antiLockBrakes",
    "frontWheelDrive",
    "fourWheelDrive",
    "powerSteering",
    "powerBrakes",
    "powerWindows",
    "airConditioning",
    "tiltWheel",
    "security",
    "restraintAirbags",
    "restraintBelts"
})
public class VehicleOptionalType {

    @XmlElement(name = "anti_lock_brakes")
    protected String antiLockBrakes;
    @XmlElement(name = "front_wheel_drive")
    protected String frontWheelDrive;
    @XmlElement(name = "four_wheel_drive")
    protected String fourWheelDrive;
    @XmlElement(name = "power_steering")
    protected String powerSteering;
    @XmlElement(name = "power_brakes")
    protected String powerBrakes;
    @XmlElement(name = "power_windows")
    protected String powerWindows;
    @XmlElement(name = "air_conditioning")
    protected String airConditioning;
    @XmlElement(name = "tilt_wheel")
    protected String tiltWheel;
    protected String security;
    @XmlElement(name = "restraint_airbags")
    protected String restraintAirbags;
    @XmlElement(name = "restraint_belts")
    protected String restraintBelts;

    /**
     * Gets the value of the antiLockBrakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAntiLockBrakes() {
        return antiLockBrakes;
    }

    /**
     * Sets the value of the antiLockBrakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAntiLockBrakes(String value) {
        this.antiLockBrakes = value;
    }

    /**
     * Gets the value of the frontWheelDrive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrontWheelDrive() {
        return frontWheelDrive;
    }

    /**
     * Sets the value of the frontWheelDrive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrontWheelDrive(String value) {
        this.frontWheelDrive = value;
    }

    /**
     * Gets the value of the fourWheelDrive property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFourWheelDrive() {
        return fourWheelDrive;
    }

    /**
     * Sets the value of the fourWheelDrive property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFourWheelDrive(String value) {
        this.fourWheelDrive = value;
    }

    /**
     * Gets the value of the powerSteering property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerSteering() {
        return powerSteering;
    }

    /**
     * Sets the value of the powerSteering property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerSteering(String value) {
        this.powerSteering = value;
    }

    /**
     * Gets the value of the powerBrakes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerBrakes() {
        return powerBrakes;
    }

    /**
     * Sets the value of the powerBrakes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerBrakes(String value) {
        this.powerBrakes = value;
    }

    /**
     * Gets the value of the powerWindows property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPowerWindows() {
        return powerWindows;
    }

    /**
     * Sets the value of the powerWindows property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPowerWindows(String value) {
        this.powerWindows = value;
    }

    /**
     * Gets the value of the airConditioning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAirConditioning() {
        return airConditioning;
    }

    /**
     * Sets the value of the airConditioning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAirConditioning(String value) {
        this.airConditioning = value;
    }

    /**
     * Gets the value of the tiltWheel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTiltWheel() {
        return tiltWheel;
    }

    /**
     * Sets the value of the tiltWheel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTiltWheel(String value) {
        this.tiltWheel = value;
    }

    /**
     * Gets the value of the security property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurity() {
        return security;
    }

    /**
     * Sets the value of the security property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurity(String value) {
        this.security = value;
    }

    /**
     * Gets the value of the restraintAirbags property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestraintAirbags() {
        return restraintAirbags;
    }

    /**
     * Sets the value of the restraintAirbags property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestraintAirbags(String value) {
        this.restraintAirbags = value;
    }

    /**
     * Gets the value of the restraintBelts property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRestraintBelts() {
        return restraintBelts;
    }

    /**
     * Sets the value of the restraintBelts property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRestraintBelts(String value) {
        this.restraintBelts = value;
    }

}