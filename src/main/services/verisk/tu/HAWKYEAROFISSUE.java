//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:05:28 PM MDT 
//


package services.verisk.tu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}HY_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}HY_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}ID_QUAL" minOccurs="0"/>
 *         &lt;element ref="{}HY_MSG_CODE" minOccurs="0"/>
 *         &lt;element ref="{}HY_SRC_IND" minOccurs="0"/>
 *         &lt;element ref="{}NO_YRS_COV" minOccurs="0"/>
 *         &lt;element ref="{}RANGE_YRS_FROM" minOccurs="0"/>
 *         &lt;element ref="{}RANGE_YRS_TO" minOccurs="0"/>
 *         &lt;element ref="{}HY_STATE" minOccurs="0"/>
 *         &lt;element ref="{}SIGN_AGE_OBTFROM" minOccurs="0"/>
 *         &lt;element ref="{}AGE_OBTFROM" minOccurs="0"/>
 *         &lt;element ref="{}SIGN_AGE_OBTTO" minOccurs="0"/>
 *         &lt;element ref="{}AGE_OBTTO" minOccurs="0"/>
 *         &lt;element ref="{}ISS_YEAR_STA" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "hysegtype",
    "hyseglength",
    "idqual",
    "hymsgcode",
    "hysrcind",
    "noyrscov",
    "rangeyrsfrom",
    "rangeyrsto",
    "hystate",
    "signageobtfrom",
    "ageobtfrom",
    "signageobtto",
    "ageobtto",
    "issyearsta"
})
@XmlRootElement(name = "HAWK-YEAR-OF-ISSUE")
public class HAWKYEAROFISSUE {

    @XmlElement(name = "HY_SEG_TYPE")
    protected String hysegtype;
    @XmlElement(name = "HY_SEG_LENGTH")
    protected String hyseglength;
    @XmlElement(name = "ID_QUAL")
    protected String idqual;
    @XmlElement(name = "HY_MSG_CODE")
    protected String hymsgcode;
    @XmlElement(name = "HY_SRC_IND")
    protected String hysrcind;
    @XmlElement(name = "NO_YRS_COV")
    protected String noyrscov;
    @XmlElement(name = "RANGE_YRS_FROM")
    protected String rangeyrsfrom;
    @XmlElement(name = "RANGE_YRS_TO")
    protected String rangeyrsto;
    @XmlElement(name = "HY_STATE")
    protected String hystate;
    @XmlElement(name = "SIGN_AGE_OBTFROM")
    protected String signageobtfrom;
    @XmlElement(name = "AGE_OBTFROM")
    protected String ageobtfrom;
    @XmlElement(name = "SIGN_AGE_OBTTO")
    protected String signageobtto;
    @XmlElement(name = "AGE_OBTTO")
    protected String ageobtto;
    @XmlElement(name = "ISS_YEAR_STA")
    protected String issyearsta;

    /**
     * Gets the value of the hysegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHYSEGTYPE() {
        return hysegtype;
    }

    /**
     * Sets the value of the hysegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHYSEGTYPE(String value) {
        this.hysegtype = value;
    }

    /**
     * Gets the value of the hyseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHYSEGLENGTH() {
        return hyseglength;
    }

    /**
     * Sets the value of the hyseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHYSEGLENGTH(String value) {
        this.hyseglength = value;
    }

    /**
     * Gets the value of the idqual property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIDQUAL() {
        return idqual;
    }

    /**
     * Sets the value of the idqual property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIDQUAL(String value) {
        this.idqual = value;
    }

    /**
     * Gets the value of the hymsgcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHYMSGCODE() {
        return hymsgcode;
    }

    /**
     * Sets the value of the hymsgcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHYMSGCODE(String value) {
        this.hymsgcode = value;
    }

    /**
     * Gets the value of the hysrcind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHYSRCIND() {
        return hysrcind;
    }

    /**
     * Sets the value of the hysrcind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHYSRCIND(String value) {
        this.hysrcind = value;
    }

    /**
     * Gets the value of the noyrscov property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNOYRSCOV() {
        return noyrscov;
    }

    /**
     * Sets the value of the noyrscov property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNOYRSCOV(String value) {
        this.noyrscov = value;
    }

    /**
     * Gets the value of the rangeyrsfrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRANGEYRSFROM() {
        return rangeyrsfrom;
    }

    /**
     * Sets the value of the rangeyrsfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRANGEYRSFROM(String value) {
        this.rangeyrsfrom = value;
    }

    /**
     * Gets the value of the rangeyrsto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRANGEYRSTO() {
        return rangeyrsto;
    }

    /**
     * Sets the value of the rangeyrsto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRANGEYRSTO(String value) {
        this.rangeyrsto = value;
    }

    /**
     * Gets the value of the hystate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHYSTATE() {
        return hystate;
    }

    /**
     * Sets the value of the hystate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHYSTATE(String value) {
        this.hystate = value;
    }

    /**
     * Gets the value of the signageobtfrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIGNAGEOBTFROM() {
        return signageobtfrom;
    }

    /**
     * Sets the value of the signageobtfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIGNAGEOBTFROM(String value) {
        this.signageobtfrom = value;
    }

    /**
     * Gets the value of the ageobtfrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAGEOBTFROM() {
        return ageobtfrom;
    }

    /**
     * Sets the value of the ageobtfrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAGEOBTFROM(String value) {
        this.ageobtfrom = value;
    }

    /**
     * Gets the value of the signageobtto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSIGNAGEOBTTO() {
        return signageobtto;
    }

    /**
     * Sets the value of the signageobtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSIGNAGEOBTTO(String value) {
        this.signageobtto = value;
    }

    /**
     * Gets the value of the ageobtto property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAGEOBTTO() {
        return ageobtto;
    }

    /**
     * Sets the value of the ageobtto property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAGEOBTTO(String value) {
        this.ageobtto = value;
    }

    /**
     * Gets the value of the issyearsta property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISSYEARSTA() {
        return issyearsta;
    }

    /**
     * Sets the value of the issyearsta property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISSYEARSTA(String value) {
        this.issyearsta = value;
    }

}
