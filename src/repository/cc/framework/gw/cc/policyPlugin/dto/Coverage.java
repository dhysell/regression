
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Coverage complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Coverage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Displayname" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="EffectiveDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExpirationDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ExposureLimit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IncidentLimit" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Notes" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="State" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="LimitsIndicator" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="PublicID" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="CoverageType" type="{http://www.idfbins.com/PolicyRetrieveResponse}CoverageType"/>
 *         &lt;element name="CoverageTerms" type="{http://www.idfbins.com/PolicyRetrieveResponse}CoverageTerms"/>
 *         &lt;element name="Deductible" type="{http://www.w3.org/2001/XMLSchema}float"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Coverage", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "displayname",
        "effectiveDate",
        "expirationDate",
        "exposureLimit",
        "incidentLimit",
        "notes",
        "state",
        "limitsIndicator",
        "publicID",
        "coverageType",
        "coverageTerms",
        "deductible"
})
public class Coverage {

    @XmlElement(name = "Displayname", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String displayname;
    @XmlElement(name = "EffectiveDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String effectiveDate;
    @XmlElement(name = "ExpirationDate", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String expirationDate;
    @XmlElement(name = "ExposureLimit", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String exposureLimit;
    @XmlElement(name = "IncidentLimit", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String incidentLimit;
    @XmlElement(name = "Notes", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String notes;
    @XmlElement(name = "State", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String state;
    @XmlElement(name = "LimitsIndicator", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String limitsIndicator;
    @XmlElement(name = "PublicID", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String publicID;
    @XmlElement(name = "CoverageType", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected CoverageType coverageType;
    @XmlElement(name = "CoverageTerms", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected CoverageTerms coverageTerms;
    @XmlElement(name = "Deductible", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected float deductible;

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
     * Gets the value of the effectiveDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getEffectiveDate() {
        return effectiveDate;
    }

    /**
     * Sets the value of the effectiveDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setEffectiveDate(String value) {
        this.effectiveDate = value;
    }

    /**
     * Gets the value of the expirationDate property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the value of the expirationDate property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExpirationDate(String value) {
        this.expirationDate = value;
    }

    /**
     * Gets the value of the exposureLimit property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getExposureLimit() {
        return exposureLimit;
    }

    /**
     * Sets the value of the exposureLimit property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setExposureLimit(String value) {
        this.exposureLimit = value;
    }

    /**
     * Gets the value of the incidentLimit property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getIncidentLimit() {
        return incidentLimit;
    }

    /**
     * Sets the value of the incidentLimit property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setIncidentLimit(String value) {
        this.incidentLimit = value;
    }

    /**
     * Gets the value of the notes property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the value of the notes property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNotes(String value) {
        this.notes = value;
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
     * Gets the value of the limitsIndicator property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getLimitsIndicator() {
        return limitsIndicator;
    }

    /**
     * Sets the value of the limitsIndicator property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setLimitsIndicator(String value) {
        this.limitsIndicator = value;
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
     * Gets the value of the coverageType property.
     *
     * @return possible object is
     * {@link CoverageType }
     */
    public CoverageType getCoverageType() {
        return coverageType;
    }

    /**
     * Sets the value of the coverageType property.
     *
     * @param value allowed object is
     *              {@link CoverageType }
     */
    public void setCoverageType(CoverageType value) {
        this.coverageType = value;
    }

    /**
     * Gets the value of the coverageTerms property.
     *
     * @return possible object is
     * {@link CoverageTerms }
     */
    public CoverageTerms getCoverageTerms() {
        return coverageTerms;
    }

    /**
     * Sets the value of the coverageTerms property.
     *
     * @param value allowed object is
     *              {@link CoverageTerms }
     */
    public void setCoverageTerms(CoverageTerms value) {
        this.coverageTerms = value;
    }

    /**
     * Gets the value of the deductible property.
     */
    public float getDeductible() {
        return deductible;
    }

    /**
     * Sets the value of the deductible property.
     */
    public void setDeductible(float value) {
        this.deductible = value;
    }

}
