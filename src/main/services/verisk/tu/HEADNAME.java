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
 *         &lt;element ref="{}HN_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}HN_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}SRC_IND" minOccurs="0"/>
 *         &lt;element ref="{}NAME_IND" minOccurs="0"/>
 *         &lt;element ref="{}LAST_NAME" minOccurs="0"/>
 *         &lt;element ref="{}FIRST_NAME" minOccurs="0"/>
 *         &lt;element ref="{}MDL_NAME" minOccurs="0"/>
 *         &lt;element ref="{}PREFIX" minOccurs="0"/>
 *         &lt;element ref="{}SUFFIX" minOccurs="0"/>
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
    "hnsegtype",
    "hnseglength",
    "srcind",
    "nameind",
    "lastname",
    "firstname",
    "mdlname",
    "prefix",
    "suffix"
})
@XmlRootElement(name = "HEAD-NAME")
public class HEADNAME {

    @XmlElement(name = "HN_SEG_TYPE")
    protected String hnsegtype;
    @XmlElement(name = "HN_SEG_LENGTH")
    protected String hnseglength;
    @XmlElement(name = "SRC_IND")
    protected String srcind;
    @XmlElement(name = "NAME_IND")
    protected String nameind;
    @XmlElement(name = "LAST_NAME")
    protected String lastname;
    @XmlElement(name = "FIRST_NAME")
    protected String firstname;
    @XmlElement(name = "MDL_NAME")
    protected String mdlname;
    @XmlElement(name = "PREFIX")
    protected String prefix;
    @XmlElement(name = "SUFFIX")
    protected String suffix;

    /**
     * Gets the value of the hnsegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHNSEGTYPE() {
        return hnsegtype;
    }

    /**
     * Sets the value of the hnsegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHNSEGTYPE(String value) {
        this.hnsegtype = value;
    }

    /**
     * Gets the value of the hnseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHNSEGLENGTH() {
        return hnseglength;
    }

    /**
     * Sets the value of the hnseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHNSEGLENGTH(String value) {
        this.hnseglength = value;
    }

    /**
     * Gets the value of the srcind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSRCIND() {
        return srcind;
    }

    /**
     * Sets the value of the srcind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSRCIND(String value) {
        this.srcind = value;
    }

    /**
     * Gets the value of the nameind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNAMEIND() {
        return nameind;
    }

    /**
     * Sets the value of the nameind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNAMEIND(String value) {
        this.nameind = value;
    }

    /**
     * Gets the value of the lastname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLASTNAME() {
        return lastname;
    }

    /**
     * Sets the value of the lastname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLASTNAME(String value) {
        this.lastname = value;
    }

    /**
     * Gets the value of the firstname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFIRSTNAME() {
        return firstname;
    }

    /**
     * Sets the value of the firstname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFIRSTNAME(String value) {
        this.firstname = value;
    }

    /**
     * Gets the value of the mdlname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDLNAME() {
        return mdlname;
    }

    /**
     * Sets the value of the mdlname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDLNAME(String value) {
        this.mdlname = value;
    }

    /**
     * Gets the value of the prefix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPREFIX() {
        return prefix;
    }

    /**
     * Sets the value of the prefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPREFIX(String value) {
        this.prefix = value;
    }

    /**
     * Gets the value of the suffix property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUFFIX() {
        return suffix;
    }

    /**
     * Sets the value of the suffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUFFIX(String value) {
        this.suffix = value;
    }

}
