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
 *         &lt;element ref="{}HP_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}HP_PHONE_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}HP_AREA_CODE" minOccurs="0"/>
 *         &lt;element ref="{}HP_TEL_NUM" minOccurs="0"/>
 *         &lt;element ref="{}HP_EXTENSION" minOccurs="0"/>
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
    "hpsegtype",
    "hpphonetype",
    "hpareacode",
    "hptelnum",
    "hpextension"
})
@XmlRootElement(name = "HEAD-PHONE")
public class HEADPHONE {

    @XmlElement(name = "HP_SEG_TYPE")
    protected String hpsegtype;
    @XmlElement(name = "HP_PHONE_TYPE")
    protected String hpphonetype;
    @XmlElement(name = "HP_AREA_CODE")
    protected String hpareacode;
    @XmlElement(name = "HP_TEL_NUM")
    protected String hptelnum;
    @XmlElement(name = "HP_EXTENSION")
    protected String hpextension;

    /**
     * Gets the value of the hpsegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHPSEGTYPE() {
        return hpsegtype;
    }

    /**
     * Sets the value of the hpsegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHPSEGTYPE(String value) {
        this.hpsegtype = value;
    }

    /**
     * Gets the value of the hpphonetype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHPPHONETYPE() {
        return hpphonetype;
    }

    /**
     * Sets the value of the hpphonetype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHPPHONETYPE(String value) {
        this.hpphonetype = value;
    }

    /**
     * Gets the value of the hpareacode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHPAREACODE() {
        return hpareacode;
    }

    /**
     * Sets the value of the hpareacode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHPAREACODE(String value) {
        this.hpareacode = value;
    }

    /**
     * Gets the value of the hptelnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHPTELNUM() {
        return hptelnum;
    }

    /**
     * Sets the value of the hptelnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHPTELNUM(String value) {
        this.hptelnum = value;
    }

    /**
     * Gets the value of the hpextension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHPEXTENSION() {
        return hpextension;
    }

    /**
     * Sets the value of the hpextension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHPEXTENSION(String value) {
        this.hpextension = value;
    }

}
