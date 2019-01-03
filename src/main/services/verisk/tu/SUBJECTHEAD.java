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
 *         &lt;element ref="{}SH_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}SH_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}SUB_IDENT" minOccurs="0"/>
 *         &lt;element ref="{}FILE_NUM" minOccurs="0"/>
 *         &lt;element ref="{}FILE_HIT" minOccurs="0"/>
 *         &lt;element ref="{}SSN_MATCH_IND" minOccurs="0"/>
 *         &lt;element ref="{}CONSU_SMT_IND" minOccurs="0"/>
 *         &lt;element ref="{}BRU_MKT_FCTL" minOccurs="0"/>
 *         &lt;element ref="{}BRU_SUBMKT_FCTL" minOccurs="0"/>
 *         &lt;element ref="{}SPRN_IND" minOccurs="0"/>
 *         &lt;element ref="{}INFSIN_DATE" minOccurs="0"/>
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
    "shsegtype",
    "shseglength",
    "subident",
    "filenum",
    "filehit",
    "ssnmatchind",
    "consusmtind",
    "brumktfctl",
    "brusubmktfctl",
    "sprnind",
    "infsindate"
})
@XmlRootElement(name = "SUBJECT-HEAD")
public class SUBJECTHEAD {

    @XmlElement(name = "SH_SEG_TYPE")
    protected String shsegtype;
    @XmlElement(name = "SH_SEG_LENGTH")
    protected String shseglength;
    @XmlElement(name = "SUB_IDENT")
    protected String subident;
    @XmlElement(name = "FILE_NUM")
    protected String filenum;
    @XmlElement(name = "FILE_HIT")
    protected String filehit;
    @XmlElement(name = "SSN_MATCH_IND")
    protected String ssnmatchind;
    @XmlElement(name = "CONSU_SMT_IND")
    protected String consusmtind;
    @XmlElement(name = "BRU_MKT_FCTL")
    protected String brumktfctl;
    @XmlElement(name = "BRU_SUBMKT_FCTL")
    protected String brusubmktfctl;
    @XmlElement(name = "SPRN_IND")
    protected String sprnind;
    @XmlElement(name = "INFSIN_DATE")
    protected String infsindate;

    /**
     * Gets the value of the shsegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHSEGTYPE() {
        return shsegtype;
    }

    /**
     * Sets the value of the shsegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHSEGTYPE(String value) {
        this.shsegtype = value;
    }

    /**
     * Gets the value of the shseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSHSEGLENGTH() {
        return shseglength;
    }

    /**
     * Sets the value of the shseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSHSEGLENGTH(String value) {
        this.shseglength = value;
    }

    /**
     * Gets the value of the subident property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUBIDENT() {
        return subident;
    }

    /**
     * Sets the value of the subident property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUBIDENT(String value) {
        this.subident = value;
    }

    /**
     * Gets the value of the filenum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFILENUM() {
        return filenum;
    }

    /**
     * Sets the value of the filenum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFILENUM(String value) {
        this.filenum = value;
    }

    /**
     * Gets the value of the filehit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFILEHIT() {
        return filehit;
    }

    /**
     * Sets the value of the filehit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFILEHIT(String value) {
        this.filehit = value;
    }

    /**
     * Gets the value of the ssnmatchind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSNMATCHIND() {
        return ssnmatchind;
    }

    /**
     * Sets the value of the ssnmatchind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSNMATCHIND(String value) {
        this.ssnmatchind = value;
    }

    /**
     * Gets the value of the consusmtind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCONSUSMTIND() {
        return consusmtind;
    }

    /**
     * Sets the value of the consusmtind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCONSUSMTIND(String value) {
        this.consusmtind = value;
    }

    /**
     * Gets the value of the brumktfctl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBRUMKTFCTL() {
        return brumktfctl;
    }

    /**
     * Sets the value of the brumktfctl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBRUMKTFCTL(String value) {
        this.brumktfctl = value;
    }

    /**
     * Gets the value of the brusubmktfctl property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBRUSUBMKTFCTL() {
        return brusubmktfctl;
    }

    /**
     * Sets the value of the brusubmktfctl property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBRUSUBMKTFCTL(String value) {
        this.brusubmktfctl = value;
    }

    /**
     * Gets the value of the sprnind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSPRNIND() {
        return sprnind;
    }

    /**
     * Sets the value of the sprnind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSPRNIND(String value) {
        this.sprnind = value;
    }

    /**
     * Gets the value of the infsindate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFSINDATE() {
        return infsindate;
    }

    /**
     * Sets the value of the infsindate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFSINDATE(String value) {
        this.infsindate = value;
    }

}