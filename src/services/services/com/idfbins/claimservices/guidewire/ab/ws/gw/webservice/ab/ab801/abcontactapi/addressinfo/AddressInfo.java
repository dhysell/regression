
package services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.addressinfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.Country;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.GeocodeStatus;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.State;


/**
 * <p>Java class for AddressInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddressInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddressLine1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine1Kanji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine2Kanji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressLine3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressType" type="{http://guidewire.com/ab/typekey}AddressType" minOccurs="0"/>
 *         &lt;element name="CEDEX" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CEDEXBureau" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CityKanji" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Country" type="{http://guidewire.com/ab/typekey}Country" minOccurs="0"/>
 *         &lt;element name="County" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="GeocodeStatus" type="{http://guidewire.com/ab/typekey}GeocodeStatus" minOccurs="0"/>
 *         &lt;element name="LinkID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SpatialPoint" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="State" type="{http://guidewire.com/ab/typekey}State" minOccurs="0"/>
 *         &lt;element name="ValidUntil" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddressInfo", propOrder = {
    "addressLine1",
    "addressLine1Kanji",
    "addressLine2",
    "addressLine2Kanji",
    "addressLine3",
    "addressType",
    "cedex",
    "cedexBureau",
    "city",
    "cityKanji",
    "country",
    "county",
    "description",
    "geocodeStatus",
    "linkID",
    "postalCode",
    "spatialPoint",
    "state",
    "validUntil"
})
public class AddressInfo {

    @XmlElement(name = "AddressLine1")
    protected String addressLine1;
    @XmlElement(name = "AddressLine1Kanji")
    protected String addressLine1Kanji;
    @XmlElement(name = "AddressLine2")
    protected String addressLine2;
    @XmlElement(name = "AddressLine2Kanji")
    protected String addressLine2Kanji;
    @XmlElement(name = "AddressLine3")
    protected String addressLine3;
    @XmlElement(name = "AddressType")
    protected String addressType;
    @XmlElement(name = "CEDEX")
    protected String cedex;
    @XmlElement(name = "CEDEXBureau")
    protected String cedexBureau;
    @XmlElement(name = "City")
    protected String city;
    @XmlElement(name = "CityKanji")
    protected String cityKanji;
    @XmlElement(name = "Country")
    @XmlSchemaType(name = "string")
    protected Country country;
    @XmlElement(name = "County")
    protected String county;
    @XmlElement(name = "Description")
    protected String description;
    @XmlElement(name = "GeocodeStatus")
    @XmlSchemaType(name = "string")
    protected GeocodeStatus geocodeStatus;
    @XmlElement(name = "LinkID")
    protected String linkID;
    @XmlElement(name = "PostalCode")
    protected String postalCode;
    @XmlElement(name = "SpatialPoint")
    protected String spatialPoint;
    @XmlElement(name = "State")
    @XmlSchemaType(name = "string")
    protected State state;
    @XmlElement(name = "ValidUntil")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar validUntil;

    /**
     * Gets the value of the addressLine1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets the value of the addressLine1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine1(String value) {
        this.addressLine1 = value;
    }

    /**
     * Gets the value of the addressLine1Kanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine1Kanji() {
        return addressLine1Kanji;
    }

    /**
     * Sets the value of the addressLine1Kanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine1Kanji(String value) {
        this.addressLine1Kanji = value;
    }

    /**
     * Gets the value of the addressLine2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets the value of the addressLine2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine2(String value) {
        this.addressLine2 = value;
    }

    /**
     * Gets the value of the addressLine2Kanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine2Kanji() {
        return addressLine2Kanji;
    }

    /**
     * Sets the value of the addressLine2Kanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine2Kanji(String value) {
        this.addressLine2Kanji = value;
    }

    /**
     * Gets the value of the addressLine3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressLine3() {
        return addressLine3;
    }

    /**
     * Sets the value of the addressLine3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressLine3(String value) {
        this.addressLine3 = value;
    }

    /**
     * Gets the value of the addressType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressType() {
        return addressType;
    }

    /**
     * Sets the value of the addressType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressType(String value) {
        this.addressType = value;
    }

    /**
     * Gets the value of the cedex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEDEX() {
        return cedex;
    }

    /**
     * Sets the value of the cedex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEDEX(String value) {
        this.cedex = value;
    }

    /**
     * Gets the value of the cedexBureau property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCEDEXBureau() {
        return cedexBureau;
    }

    /**
     * Sets the value of the cedexBureau property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCEDEXBureau(String value) {
        this.cedexBureau = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCity(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the cityKanji property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCityKanji() {
        return cityKanji;
    }

    /**
     * Sets the value of the cityKanji property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCityKanji(String value) {
        this.cityKanji = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link Country }
     *     
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link Country }
     *     
     */
    public void setCountry(Country value) {
        this.country = value;
    }

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCounty() {
        return county;
    }

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCounty(String value) {
        this.county = value;
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
     * Gets the value of the geocodeStatus property.
     * 
     * @return
     *     possible object is
     *     {@link GeocodeStatus }
     *     
     */
    public GeocodeStatus getGeocodeStatus() {
        return geocodeStatus;
    }

    /**
     * Sets the value of the geocodeStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link GeocodeStatus }
     *     
     */
    public void setGeocodeStatus(GeocodeStatus value) {
        this.geocodeStatus = value;
    }

    /**
     * Gets the value of the linkID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkID() {
        return linkID;
    }

    /**
     * Sets the value of the linkID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkID(String value) {
        this.linkID = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the spatialPoint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpatialPoint() {
        return spatialPoint;
    }

    /**
     * Sets the value of the spatialPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpatialPoint(String value) {
        this.spatialPoint = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link State }
     *     
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link State }
     *     
     */
    public void setState(State value) {
        this.state = value;
    }

    /**
     * Gets the value of the validUntil property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getValidUntil() {
        return validUntil;
    }

    /**
     * Sets the value of the validUntil property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setValidUntil(XMLGregorianCalendar value) {
        this.validUntil = value;
    }

}
