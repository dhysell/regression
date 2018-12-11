//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for propertyTaxInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="propertyTaxInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="legalDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="county" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parcelNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="fipsCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="apnNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="blockNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lotNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="subdivisionName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="township" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="municipalityName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="range" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="section" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zoningDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="influenceCode" type="{http://cp.com/rules/client}influenceCodeType" minOccurs="0"/>
 *         &lt;element name="landUseCode" type="{http://cp.com/rules/client}landUseCodeType" minOccurs="0"/>
 *         &lt;element name="propertyTypeCode" type="{http://cp.com/rules/client}propertyTypeCode" minOccurs="0"/>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lotSize" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lotFrontFootage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="lotDepthFootage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acres" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="totalAssessedValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="totalCalculatedValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="totalMarketValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="totalLandValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="marketLandValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="assessedLandValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="improvementValue" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="assessedYear" type="{http://cp.com/rules/client}year" minOccurs="0"/>
 *         &lt;element name="taxCodeArea" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxBillingYear" type="{http://cp.com/rules/client}year" minOccurs="0"/>
 *         &lt;element name="homesteadExemption" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="taxAmount" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="percentImproved" type="{http://cp.com/rules/client}percentImprovedType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertyTaxInfoType", propOrder = {
    "legalDescription",
    "county",
    "parcelNumber",
    "fipsCode",
    "apnNumber",
    "blockNumber",
    "lotNumber",
    "subdivisionName",
    "township",
    "municipalityName",
    "range",
    "section",
    "zoningDescription",
    "influenceCode",
    "landUseCode",
    "propertyTypeCode",
    "latitude",
    "longitude",
    "lotSize",
    "lotFrontFootage",
    "lotDepthFootage",
    "acres",
    "totalAssessedValue",
    "totalCalculatedValue",
    "totalMarketValue",
    "totalLandValue",
    "marketLandValue",
    "assessedLandValue",
    "improvementValue",
    "assessedYear",
    "taxCodeArea",
    "taxBillingYear",
    "homesteadExemption",
    "taxAmount",
    "percentImproved"
})
public class PropertyTaxInfoType {

    protected String legalDescription;
    protected String county;
    protected String parcelNumber;
    protected String fipsCode;
    protected String apnNumber;
    protected String blockNumber;
    protected String lotNumber;
    protected String subdivisionName;
    protected String township;
    protected String municipalityName;
    protected String range;
    protected String section;
    protected String zoningDescription;
    @XmlSchemaType(name = "string")
    protected InfluenceCodeTypeEnum influenceCode;
    @XmlSchemaType(name = "string")
    protected LandUseCodeTypeEnum landUseCode;
    @XmlSchemaType(name = "string")
    protected PropertyTypeCodeEnum propertyTypeCode;
    protected String latitude;
    protected String longitude;
    protected String lotSize;
    protected String lotFrontFootage;
    protected String lotDepthFootage;
    protected String acres;
    protected String totalAssessedValue;
    protected String totalCalculatedValue;
    protected String totalMarketValue;
    protected String totalLandValue;
    protected String marketLandValue;
    protected String assessedLandValue;
    protected String improvementValue;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer assessedYear;
    protected String taxCodeArea;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer taxBillingYear;
    protected String homesteadExemption;
    protected String taxAmount;
    protected String percentImproved;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "address")
    protected String address;

    /**
     * Gets the value of the legalDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLegalDescription() {
        return legalDescription;
    }

    /**
     * Sets the value of the legalDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLegalDescription(String value) {
        this.legalDescription = value;
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
     * Gets the value of the parcelNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParcelNumber() {
        return parcelNumber;
    }

    /**
     * Sets the value of the parcelNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParcelNumber(String value) {
        this.parcelNumber = value;
    }

    /**
     * Gets the value of the fipsCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFipsCode() {
        return fipsCode;
    }

    /**
     * Sets the value of the fipsCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFipsCode(String value) {
        this.fipsCode = value;
    }

    /**
     * Gets the value of the apnNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApnNumber() {
        return apnNumber;
    }

    /**
     * Sets the value of the apnNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApnNumber(String value) {
        this.apnNumber = value;
    }

    /**
     * Gets the value of the blockNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBlockNumber() {
        return blockNumber;
    }

    /**
     * Sets the value of the blockNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBlockNumber(String value) {
        this.blockNumber = value;
    }

    /**
     * Gets the value of the lotNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotNumber() {
        return lotNumber;
    }

    /**
     * Sets the value of the lotNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotNumber(String value) {
        this.lotNumber = value;
    }

    /**
     * Gets the value of the subdivisionName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubdivisionName() {
        return subdivisionName;
    }

    /**
     * Sets the value of the subdivisionName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubdivisionName(String value) {
        this.subdivisionName = value;
    }

    /**
     * Gets the value of the township property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTownship() {
        return township;
    }

    /**
     * Sets the value of the township property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTownship(String value) {
        this.township = value;
    }

    /**
     * Gets the value of the municipalityName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMunicipalityName() {
        return municipalityName;
    }

    /**
     * Sets the value of the municipalityName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMunicipalityName(String value) {
        this.municipalityName = value;
    }

    /**
     * Gets the value of the range property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRange() {
        return range;
    }

    /**
     * Sets the value of the range property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRange(String value) {
        this.range = value;
    }

    /**
     * Gets the value of the section property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSection() {
        return section;
    }

    /**
     * Sets the value of the section property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSection(String value) {
        this.section = value;
    }

    /**
     * Gets the value of the zoningDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZoningDescription() {
        return zoningDescription;
    }

    /**
     * Sets the value of the zoningDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZoningDescription(String value) {
        this.zoningDescription = value;
    }

    /**
     * Gets the value of the influenceCode property.
     * 
     * @return
     *     possible object is
     *     {@link InfluenceCodeTypeEnum }
     *     
     */
    public InfluenceCodeTypeEnum getInfluenceCode() {
        return influenceCode;
    }

    /**
     * Sets the value of the influenceCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link InfluenceCodeTypeEnum }
     *     
     */
    public void setInfluenceCode(InfluenceCodeTypeEnum value) {
        this.influenceCode = value;
    }

    /**
     * Gets the value of the landUseCode property.
     * 
     * @return
     *     possible object is
     *     {@link LandUseCodeTypeEnum }
     *     
     */
    public LandUseCodeTypeEnum getLandUseCode() {
        return landUseCode;
    }

    /**
     * Sets the value of the landUseCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link LandUseCodeTypeEnum }
     *     
     */
    public void setLandUseCode(LandUseCodeTypeEnum value) {
        this.landUseCode = value;
    }

    /**
     * Gets the value of the propertyTypeCode property.
     * 
     * @return
     *     possible object is
     *     {@link PropertyTypeCodeEnum }
     *     
     */
    public PropertyTypeCodeEnum getPropertyTypeCode() {
        return propertyTypeCode;
    }

    /**
     * Sets the value of the propertyTypeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link PropertyTypeCodeEnum }
     *     
     */
    public void setPropertyTypeCode(PropertyTypeCodeEnum value) {
        this.propertyTypeCode = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatitude(String value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongitude(String value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the lotSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotSize() {
        return lotSize;
    }

    /**
     * Sets the value of the lotSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotSize(String value) {
        this.lotSize = value;
    }

    /**
     * Gets the value of the lotFrontFootage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotFrontFootage() {
        return lotFrontFootage;
    }

    /**
     * Sets the value of the lotFrontFootage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotFrontFootage(String value) {
        this.lotFrontFootage = value;
    }

    /**
     * Gets the value of the lotDepthFootage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLotDepthFootage() {
        return lotDepthFootage;
    }

    /**
     * Sets the value of the lotDepthFootage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLotDepthFootage(String value) {
        this.lotDepthFootage = value;
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
     * Gets the value of the totalAssessedValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalAssessedValue() {
        return totalAssessedValue;
    }

    /**
     * Sets the value of the totalAssessedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalAssessedValue(String value) {
        this.totalAssessedValue = value;
    }

    /**
     * Gets the value of the totalCalculatedValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalCalculatedValue() {
        return totalCalculatedValue;
    }

    /**
     * Sets the value of the totalCalculatedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalCalculatedValue(String value) {
        this.totalCalculatedValue = value;
    }

    /**
     * Gets the value of the totalMarketValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalMarketValue() {
        return totalMarketValue;
    }

    /**
     * Sets the value of the totalMarketValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalMarketValue(String value) {
        this.totalMarketValue = value;
    }

    /**
     * Gets the value of the totalLandValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTotalLandValue() {
        return totalLandValue;
    }

    /**
     * Sets the value of the totalLandValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTotalLandValue(String value) {
        this.totalLandValue = value;
    }

    /**
     * Gets the value of the marketLandValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMarketLandValue() {
        return marketLandValue;
    }

    /**
     * Sets the value of the marketLandValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMarketLandValue(String value) {
        this.marketLandValue = value;
    }

    /**
     * Gets the value of the assessedLandValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAssessedLandValue() {
        return assessedLandValue;
    }

    /**
     * Sets the value of the assessedLandValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssessedLandValue(String value) {
        this.assessedLandValue = value;
    }

    /**
     * Gets the value of the improvementValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImprovementValue() {
        return improvementValue;
    }

    /**
     * Sets the value of the improvementValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImprovementValue(String value) {
        this.improvementValue = value;
    }

    /**
     * Gets the value of the assessedYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getAssessedYear() {
        return assessedYear;
    }

    /**
     * Sets the value of the assessedYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAssessedYear(Integer value) {
        this.assessedYear = value;
    }

    /**
     * Gets the value of the taxCodeArea property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxCodeArea() {
        return taxCodeArea;
    }

    /**
     * Sets the value of the taxCodeArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxCodeArea(String value) {
        this.taxCodeArea = value;
    }

    /**
     * Gets the value of the taxBillingYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getTaxBillingYear() {
        return taxBillingYear;
    }

    /**
     * Sets the value of the taxBillingYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxBillingYear(Integer value) {
        this.taxBillingYear = value;
    }

    /**
     * Gets the value of the homesteadExemption property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomesteadExemption() {
        return homesteadExemption;
    }

    /**
     * Sets the value of the homesteadExemption property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomesteadExemption(String value) {
        this.homesteadExemption = value;
    }

    /**
     * Gets the value of the taxAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTaxAmount() {
        return taxAmount;
    }

    /**
     * Sets the value of the taxAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTaxAmount(String value) {
        this.taxAmount = value;
    }

    /**
     * Gets the value of the percentImproved property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPercentImproved() {
        return percentImproved;
    }

    /**
     * Sets the value of the percentImproved property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPercentImproved(String value) {
        this.percentImproved = value;
    }

    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUnitNumber() {
        return unitNumber;
    }

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUnitNumber(BigInteger value) {
        this.unitNumber = value;
    }

    /**
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

}
