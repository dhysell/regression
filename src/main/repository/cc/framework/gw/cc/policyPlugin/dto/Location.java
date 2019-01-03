
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Location complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Location">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="DisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AddressLine1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AddressLine2" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AddressLine3" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="AddressLine4" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LocationType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Street" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="City" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PostalCode" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Latitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Longitude" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Retired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="Lot" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Block" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Quarter" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Section" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Township" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="TNSP_N_S" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Range" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Range_E_W" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Location", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "id",
        "displayName",
        "addressLine1",
        "addressLine2",
        "addressLine3",
        "addressLine4",
        "locationType",
        "street",
        "city",
        "state",
        "postalCode",
        "latitude",
        "longitude",
        "retired",
        "lot",
        "block",
        "quarter",
        "section",
        "township",
        "tnspns",
        "range",
        "rangeEW",
        "publicID"
})
public class Location {

    @XmlElement(name = "ID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String id;
    @XmlElement(name = "DisplayName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String displayName;
    @XmlElement(name = "AddressLine1", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String addressLine1;
    @XmlElement(name = "AddressLine2", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String addressLine2;
    @XmlElement(name = "AddressLine3", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String addressLine3;
    @XmlElement(name = "AddressLine4", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String addressLine4;
    @XmlElement(name = "LocationType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String locationType;
    @XmlElement(name = "Street", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String street;
    @XmlElement(name = "City", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String city;
    @XmlElement(name = "State", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String state;
    @XmlElement(name = "PostalCode", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String postalCode;
    @XmlElement(name = "Latitude", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String latitude;
    @XmlElement(name = "Longitude", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String longitude;
    @XmlElement(name = "Retired", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected boolean retired;
    @XmlElement(name = "Lot", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String lot;
    @XmlElement(name = "Block", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String block;
    @XmlElement(name = "Quarter", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String quarter;
    @XmlElement(name = "Section", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String section;
    @XmlElement(name = "Township", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String township;
    @XmlElement(name = "TNSP_N_S", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String tnspns;
    @XmlElement(name = "Range", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String range;
    @XmlElement(name = "Range_E_W", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String rangeEW;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;

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
     * Gets the value of the displayName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDisplayName(String value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the addressLine1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAddressLine1() {
        return addressLine1;
    }

    /**
     * Sets the value of the addressLine1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine1(String value) {
        this.addressLine1 = value;
    }

    /**
     * Gets the value of the addressLine2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAddressLine2() {
        return addressLine2;
    }

    /**
     * Sets the value of the addressLine2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine2(String value) {
        this.addressLine2 = value;
    }

    /**
     * Gets the value of the addressLine3 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAddressLine3() {
        return addressLine3;
    }

    /**
     * Sets the value of the addressLine3 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine3(String value) {
        this.addressLine3 = value;
    }

    /**
     * Gets the value of the addressLine4 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAddressLine4() {
        return addressLine4;
    }

    /**
     * Sets the value of the addressLine4 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAddressLine4(String value) {
        this.addressLine4 = value;
    }

    /**
     * Gets the value of the locationType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLocationType() {
        return locationType;
    }

    /**
     * Sets the value of the locationType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLocationType(String value) {
        this.locationType = value;
    }

    /**
     * Gets the value of the street property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the value of the street property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setStreet(String value) {
        this.street = value;
    }

    /**
     * Gets the value of the city property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the value of the city property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCity(String value) {
        this.city = value;
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
     * Gets the value of the postalCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets the value of the postalCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPostalCode(String value) {
        this.postalCode = value;
    }

    /**
     * Gets the value of the latitude property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLatitude(String value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLongitude(String value) {
        this.longitude = value;
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
     * Gets the value of the lot property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLot() {
        return lot;
    }

    /**
     * Sets the value of the lot property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLot(String value) {
        this.lot = value;
    }

    /**
     * Gets the value of the block property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getBlock() {
        return block;
    }

    /**
     * Sets the value of the block property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setBlock(String value) {
        this.block = value;
    }

    /**
     * Gets the value of the quarter property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getQuarter() {
        return quarter;
    }

    /**
     * Sets the value of the quarter property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setQuarter(String value) {
        this.quarter = value;
    }

    /**
     * Gets the value of the section property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSection() {
        return section;
    }

    /**
     * Sets the value of the section property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSection(String value) {
        this.section = value;
    }

    /**
     * Gets the value of the township property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTownship() {
        return township;
    }

    /**
     * Sets the value of the township property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTownship(String value) {
        this.township = value;
    }

    /**
     * Gets the value of the tnspns property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTNSPNS() {
        return tnspns;
    }

    /**
     * Sets the value of the tnspns property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTNSPNS(String value) {
        this.tnspns = value;
    }

    /**
     * Gets the value of the range property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRange() {
        return range;
    }

    /**
     * Sets the value of the range property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRange(String value) {
        this.range = value;
    }

    /**
     * Gets the value of the rangeEW property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRangeEW() {
        return rangeEW;
    }

    /**
     * Sets the value of the rangeEW property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRangeEW(String value) {
        this.rangeEW = value;
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

}
