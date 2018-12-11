//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for propertyAttributeType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="propertyAttributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="livingAreasqft" type="{http://cp.com/rules/client}tenDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfStories" type="{http://cp.com/rules/client}buildingStoriesFormat" minOccurs="0"/>
 *         &lt;element name="numberOfBedrooms" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfBaths" type="{http://cp.com/rules/client}buildingBathFormat" minOccurs="0"/>
 *         &lt;element name="numberOfFireplaces" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfUnits" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfRooms" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfFullBaths" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfHalfBaths" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="numberOfBathFixtures" type="{http://cp.com/rules/client}fiveDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="yearBuilt" type="{http://cp.com/rules/client}year" minOccurs="0"/>
 *         &lt;element name="effectiveYearBuilt" type="{http://cp.com/rules/client}year" minOccurs="0"/>
 *         &lt;element name="conditionOfStructure" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="slopeCode" type="{http://cp.com/rules/client}slopecodeType" minOccurs="0"/>
 *         &lt;element name="qualityOfStructure" type="{http://cp.com/rules/client}structurecodeType" minOccurs="0"/>
 *         &lt;element name="reportid" type="{http://cp.com/rules/client}reportidType" minOccurs="0"/>
 *         &lt;element name="poolIndicator" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="trampolineIndicator" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="acIndicator" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="fireplaceIndicator" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="policyCoverageValue" type="{http://cp.com/rules/client}nineDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="residenceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="buildingSqFootage" type="{http://cp.com/rules/client}fifteenDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="groundFloorSqFootage" type="{http://cp.com/rules/client}tenDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="basementSqFootage" type="{http://cp.com/rules/client}tenDigitPositiveInteger" minOccurs="0"/>
 *         &lt;element name="garageSqFootage" type="{http://cp.com/rules/client}tenDigitPositiveInteger" minOccurs="0"/>
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
@XmlType(name = "propertyAttributeType", propOrder = {
    "livingAreasqft",
    "numberOfStories",
    "numberOfBedrooms",
    "numberOfBaths",
    "numberOfFireplaces",
    "numberOfUnits",
    "numberOfRooms",
    "numberOfFullBaths",
    "numberOfHalfBaths",
    "numberOfBathFixtures",
    "yearBuilt",
    "effectiveYearBuilt",
    "conditionOfStructure",
    "slopeCode",
    "qualityOfStructure",
    "reportid",
    "poolIndicator",
    "trampolineIndicator",
    "acIndicator",
    "fireplaceIndicator",
    "policyCoverageValue",
    "residenceType",
    "buildingSqFootage",
    "groundFloorSqFootage",
    "basementSqFootage",
    "garageSqFootage"
})
public class PropertyAttributeType {

    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer livingAreasqft;
    protected String numberOfStories;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfBedrooms;
    protected String numberOfBaths;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfFireplaces;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfUnits;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfRooms;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfFullBaths;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfHalfBaths;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter4 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer numberOfBathFixtures;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer yearBuilt;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer effectiveYearBuilt;
    protected String conditionOfStructure;
    @XmlSchemaType(name = "string")
    protected SlopecodeTypeEnum slopeCode;
    @XmlSchemaType(name = "string")
    protected StructureCodeTypeEnum qualityOfStructure;
    protected String reportid;
    @XmlSchemaType(name = "string")
    protected YesNoType poolIndicator;
    @XmlSchemaType(name = "string")
    protected YesNoType trampolineIndicator;
    @XmlSchemaType(name = "string")
    protected YesNoType acIndicator;
    @XmlSchemaType(name = "string")
    protected YesNoType fireplaceIndicator;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter6 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer policyCoverageValue;
    protected String residenceType;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter8 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer buildingSqFootage;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer groundFloorSqFootage;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer basementSqFootage;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter2 .class)
    @XmlSchemaType(name = "nonNegativeInteger")
    protected Integer garageSqFootage;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "address")
    protected String address;

    /**
     * Gets the value of the livingAreasqft property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getLivingAreasqft() {
        return livingAreasqft;
    }

    /**
     * Sets the value of the livingAreasqft property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLivingAreasqft(Integer value) {
        this.livingAreasqft = value;
    }

    /**
     * Gets the value of the numberOfStories property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfStories() {
        return numberOfStories;
    }

    /**
     * Sets the value of the numberOfStories property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfStories(String value) {
        this.numberOfStories = value;
    }

    /**
     * Gets the value of the numberOfBedrooms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    /**
     * Sets the value of the numberOfBedrooms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfBedrooms(Integer value) {
        this.numberOfBedrooms = value;
    }

    /**
     * Gets the value of the numberOfBaths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumberOfBaths() {
        return numberOfBaths;
    }

    /**
     * Sets the value of the numberOfBaths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfBaths(String value) {
        this.numberOfBaths = value;
    }

    /**
     * Gets the value of the numberOfFireplaces property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfFireplaces() {
        return numberOfFireplaces;
    }

    /**
     * Sets the value of the numberOfFireplaces property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfFireplaces(Integer value) {
        this.numberOfFireplaces = value;
    }

    /**
     * Gets the value of the numberOfUnits property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfUnits() {
        return numberOfUnits;
    }

    /**
     * Sets the value of the numberOfUnits property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfUnits(Integer value) {
        this.numberOfUnits = value;
    }

    /**
     * Gets the value of the numberOfRooms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfRooms() {
        return numberOfRooms;
    }

    /**
     * Sets the value of the numberOfRooms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfRooms(Integer value) {
        this.numberOfRooms = value;
    }

    /**
     * Gets the value of the numberOfFullBaths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfFullBaths() {
        return numberOfFullBaths;
    }

    /**
     * Sets the value of the numberOfFullBaths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfFullBaths(Integer value) {
        this.numberOfFullBaths = value;
    }

    /**
     * Gets the value of the numberOfHalfBaths property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfHalfBaths() {
        return numberOfHalfBaths;
    }

    /**
     * Sets the value of the numberOfHalfBaths property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfHalfBaths(Integer value) {
        this.numberOfHalfBaths = value;
    }

    /**
     * Gets the value of the numberOfBathFixtures property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getNumberOfBathFixtures() {
        return numberOfBathFixtures;
    }

    /**
     * Sets the value of the numberOfBathFixtures property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumberOfBathFixtures(Integer value) {
        this.numberOfBathFixtures = value;
    }

    /**
     * Gets the value of the yearBuilt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getYearBuilt() {
        return yearBuilt;
    }

    /**
     * Sets the value of the yearBuilt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setYearBuilt(Integer value) {
        this.yearBuilt = value;
    }

    /**
     * Gets the value of the effectiveYearBuilt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getEffectiveYearBuilt() {
        return effectiveYearBuilt;
    }

    /**
     * Sets the value of the effectiveYearBuilt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEffectiveYearBuilt(Integer value) {
        this.effectiveYearBuilt = value;
    }

    /**
     * Gets the value of the conditionOfStructure property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getConditionOfStructure() {
        return conditionOfStructure;
    }

    /**
     * Sets the value of the conditionOfStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setConditionOfStructure(String value) {
        this.conditionOfStructure = value;
    }

    /**
     * Gets the value of the slopeCode property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.SlopecodeTypeEnum }
     *     
     */
    public SlopecodeTypeEnum getSlopeCode() {
        return slopeCode;
    }

    /**
     * Sets the value of the slopeCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.SlopecodeTypeEnum }
     *     
     */
    public void setSlopeCode(SlopecodeTypeEnum value) {
        this.slopeCode = value;
    }

    /**
     * Gets the value of the qualityOfStructure property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.StructureCodeTypeEnum }
     *     
     */
    public StructureCodeTypeEnum getQualityOfStructure() {
        return qualityOfStructure;
    }

    /**
     * Sets the value of the qualityOfStructure property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.StructureCodeTypeEnum }
     *     
     */
    public void setQualityOfStructure(StructureCodeTypeEnum value) {
        this.qualityOfStructure = value;
    }

    /**
     * Gets the value of the reportid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportid() {
        return reportid;
    }

    /**
     * Sets the value of the reportid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportid(String value) {
        this.reportid = value;
    }

    /**
     * Gets the value of the poolIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getPoolIndicator() {
        return poolIndicator;
    }

    /**
     * Sets the value of the poolIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public void setPoolIndicator(YesNoType value) {
        this.poolIndicator = value;
    }

    /**
     * Gets the value of the trampolineIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getTrampolineIndicator() {
        return trampolineIndicator;
    }

    /**
     * Sets the value of the trampolineIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public void setTrampolineIndicator(YesNoType value) {
        this.trampolineIndicator = value;
    }

    /**
     * Gets the value of the acIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getAcIndicator() {
        return acIndicator;
    }

    /**
     * Sets the value of the acIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public void setAcIndicator(YesNoType value) {
        this.acIndicator = value;
    }

    /**
     * Gets the value of the fireplaceIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getFireplaceIndicator() {
        return fireplaceIndicator;
    }

    /**
     * Sets the value of the fireplaceIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public void setFireplaceIndicator(YesNoType value) {
        this.fireplaceIndicator = value;
    }

    /**
     * Gets the value of the policyCoverageValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getPolicyCoverageValue() {
        return policyCoverageValue;
    }

    /**
     * Sets the value of the policyCoverageValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPolicyCoverageValue(Integer value) {
        this.policyCoverageValue = value;
    }

    /**
     * Gets the value of the residenceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResidenceType() {
        return residenceType;
    }

    /**
     * Sets the value of the residenceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResidenceType(String value) {
        this.residenceType = value;
    }

    /**
     * Gets the value of the buildingSqFootage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getBuildingSqFootage() {
        return buildingSqFootage;
    }

    /**
     * Sets the value of the buildingSqFootage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuildingSqFootage(Integer value) {
        this.buildingSqFootage = value;
    }

    /**
     * Gets the value of the groundFloorSqFootage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getGroundFloorSqFootage() {
        return groundFloorSqFootage;
    }

    /**
     * Sets the value of the groundFloorSqFootage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGroundFloorSqFootage(Integer value) {
        this.groundFloorSqFootage = value;
    }

    /**
     * Gets the value of the basementSqFootage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getBasementSqFootage() {
        return basementSqFootage;
    }

    /**
     * Sets the value of the basementSqFootage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBasementSqFootage(Integer value) {
        this.basementSqFootage = value;
    }

    /**
     * Gets the value of the garageSqFootage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getGarageSqFootage() {
        return garageSqFootage;
    }

    /**
     * Sets the value of the garageSqFootage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGarageSqFootage(Integer value) {
        this.garageSqFootage = value;
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
