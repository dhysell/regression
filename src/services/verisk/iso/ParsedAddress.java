//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:19:26 PM MDT 
//


package services.verisk.iso;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParsedAddress complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ParsedAddress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AddrTypeCd" type="{}StringCd" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="HouseNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PreDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="StreetName" type="{}String" minOccurs="0"/>
 *         &lt;element name="StreetType" type="{}String" minOccurs="0"/>
 *         &lt;element name="PostDirection" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ApartmentNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="City" type="{}String" minOccurs="0"/>
 *         &lt;element name="StateProvCd" type="{}StringCd" minOccurs="0"/>
 *         &lt;element name="PostalCode" type="{}String" minOccurs="0"/>
 *         &lt;element name="Zip4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ZipCarrierRoute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CountryCd" type="{}StringCd" minOccurs="0"/>
 *         &lt;element name="Country" type="{}String" minOccurs="0"/>
 *         &lt;element name="Latitude" type="{}String" minOccurs="0"/>
 *         &lt;element name="Longitude" type="{}String" minOccurs="0"/>
 *         &lt;element name="County" type="{}String" minOccurs="0"/>
 *         &lt;element name="Region" type="{}String" minOccurs="0"/>
 *         &lt;element name="RegionCd" type="{}StringCd" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParsedAddress", propOrder = {
    "addrTypeCd",
    "houseNumber",
    "preDirection",
    "streetName",
    "streetType",
    "postDirection",
    "apartmentNumber",
    "city",
    "stateProvCd",
    "postalCode",
    "zip4",
    "zipCarrierRoute",
    "countryCd",
    "country",
    "latitude",
    "longitude",
    "county",
    "region",
    "regionCd"
})
public class ParsedAddress {

    @XmlElement(name = "AddrTypeCd")
    protected List<StringCd> addrTypeCd;
    @XmlElement(name = "HouseNumber")
    protected java.lang.String houseNumber;
    @XmlElement(name = "PreDirection")
    protected java.lang.String preDirection;
    @XmlElement(name = "StreetName")
    protected services.verisk.iso.String streetName;
    @XmlElement(name = "StreetType")
    protected services.verisk.iso.String streetType;
    @XmlElement(name = "PostDirection")
    protected java.lang.String postDirection;
    @XmlElement(name = "ApartmentNumber")
    protected java.lang.String apartmentNumber;
    @XmlElement(name = "City")
    protected services.verisk.iso.String city;
    @XmlElement(name = "StateProvCd")
    protected StringCd stateProvCd;
    @XmlElement(name = "PostalCode")
    protected services.verisk.iso.String postalCode;
    @XmlElement(name = "Zip4")
    protected java.lang.String zip4;
    @XmlElement(name = "ZipCarrierRoute")
    protected java.lang.String zipCarrierRoute;
    @XmlElement(name = "CountryCd")
    protected StringCd countryCd;
    @XmlElement(name = "Country")
    protected services.verisk.iso.String country;
    @XmlElement(name = "Latitude")
    protected services.verisk.iso.String latitude;
    @XmlElement(name = "Longitude")
    protected services.verisk.iso.String longitude;
    @XmlElement(name = "County")
    protected services.verisk.iso.String county;
    @XmlElement(name = "Region")
    protected services.verisk.iso.String region;
    @XmlElement(name = "RegionCd")
    protected StringCd regionCd;

    /**
     * Gets the value of the addrTypeCd property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addrTypeCd property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddrTypeCd().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link StringCd }
     * 
     * 
     */
    public List<StringCd> getAddrTypeCd() {
        if (addrTypeCd == null) {
            addrTypeCd = new ArrayList<StringCd>();
        }
        return this.addrTypeCd;
    }

    /**
     * Gets the value of the houseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getHouseNumber() {
        return houseNumber;
    }

    /**
     * Sets the value of the houseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setHouseNumber(java.lang.String value) {
        this.houseNumber = value;
    }

    /**
     * Gets the value of the preDirection property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getPreDirection() {
        return preDirection;
    }

    /**
     * Sets the value of the preDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setPreDirection(java.lang.String value) {
        this.preDirection = value;
    }

    /**
     * Gets the value of the streetName property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getStreetName() {
        return streetName;
    }

    /**
     * Sets the value of the streetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setStreetName(services.verisk.iso.String value) {
        this.streetName = value;
    }

    /**
     * Gets the value of the streetType property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getStreetType() {
        return streetType;
    }

    /**
     * Sets the value of the streetType property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setStreetType(services.verisk.iso.String value) {
        this.streetType = value;
    }

    /**
     * Gets the value of the postDirection property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getPostDirection() {
        return postDirection;
    }

    /**
     * Sets the value of the postDirection property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setPostDirection(java.lang.String value) {
        this.postDirection = value;
    }

    /**
     * Gets the value of the apartmentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getApartmentNumber() {
        return apartmentNumber;
    }

    /**
     * Sets the value of the apartmentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setApartmentNumber(java.lang.String value) {
        this.apartmentNumber = value;
    }

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setCity(services.verisk.iso.String value) {
        this.city = value;
    }

    /**
     * Gets the value of the stateProvCd property.
     * 
     * @return
     *     possible object is
     *     {@link StringCd }
     *     
     */
    public StringCd getStateProvCd() {
        return stateProvCd;
    }

    /**
     * Sets the value of the stateProvCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringCd }
     *     
     */
    public void setStateProvCd(StringCd value) {
        this.stateProvCd = value;
    }

    /**
     * Gets the value of the postalCode property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setPostalCode(services.verisk.iso.String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the zip4 property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getZip4() {
        return zip4;
    }

    /**
     * Sets the value of the zip4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setZip4(java.lang.String value) {
        this.zip4 = value;
    }

    /**
     * Gets the value of the zipCarrierRoute property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getZipCarrierRoute() {
        return zipCarrierRoute;
    }

    /**
     * Sets the value of the zipCarrierRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setZipCarrierRoute(java.lang.String value) {
        this.zipCarrierRoute = value;
    }

    /**
     * Gets the value of the countryCd property.
     * 
     * @return
     *     possible object is
     *     {@link StringCd }
     *     
     */
    public StringCd getCountryCd() {
        return countryCd;
    }

    /**
     * Sets the value of the countryCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringCd }
     *     
     */
    public void setCountryCd(StringCd value) {
        this.countryCd = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setCountry(services.verisk.iso.String value) {
        this.country = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setLatitude(services.verisk.iso.String value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setLongitude(services.verisk.iso.String value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the county property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getCounty() {
        return county;
    }

    /**
     * Sets the value of the county property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setCounty(services.verisk.iso.String value) {
        this.county = value;
    }

    /**
     * Gets the value of the region property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getRegion() {
        return region;
    }

    /**
     * Sets the value of the region property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setRegion(services.verisk.iso.String value) {
        this.region = value;
    }

    /**
     * Gets the value of the regionCd property.
     * 
     * @return
     *     possible object is
     *     {@link StringCd }
     *     
     */
    public StringCd getRegionCd() {
        return regionCd;
    }

    /**
     * Sets the value of the regionCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringCd }
     *     
     */
    public void setRegionCd(StringCd value) {
        this.regionCd = value;
    }

}