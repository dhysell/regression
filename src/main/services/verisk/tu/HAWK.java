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
 *         &lt;element ref="{}HW_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}HW_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}HW_PROD_CODE" minOccurs="0"/>
 *         &lt;element ref="{}HW_PROD_STATUS" minOccurs="0"/>
 *         &lt;element ref="{}HW_SEARCH_STATUS" minOccurs="0"/>
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
    "hwsegtype",
    "hwseglength",
    "hwprodcode",
    "hwprodstatus",
    "hwsearchstatus"
})
@XmlRootElement(name = "HAWK")
public class HAWK {

    @XmlElement(name = "HW_SEG_TYPE")
    protected String hwsegtype;
    @XmlElement(name = "HW_SEG_LENGTH")
    protected String hwseglength;
    @XmlElement(name = "HW_PROD_CODE")
    protected String hwprodcode;
    @XmlElement(name = "HW_PROD_STATUS")
    protected String hwprodstatus;
    @XmlElement(name = "HW_SEARCH_STATUS")
    protected String hwsearchstatus;

    /**
     * Gets the value of the hwsegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHWSEGTYPE() {
        return hwsegtype;
    }

    /**
     * Sets the value of the hwsegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHWSEGTYPE(String value) {
        this.hwsegtype = value;
    }

    /**
     * Gets the value of the hwseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHWSEGLENGTH() {
        return hwseglength;
    }

    /**
     * Sets the value of the hwseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHWSEGLENGTH(String value) {
        this.hwseglength = value;
    }

    /**
     * Gets the value of the hwprodcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHWPRODCODE() {
        return hwprodcode;
    }

    /**
     * Sets the value of the hwprodcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHWPRODCODE(String value) {
        this.hwprodcode = value;
    }

    /**
     * Gets the value of the hwprodstatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHWPRODSTATUS() {
        return hwprodstatus;
    }

    /**
     * Sets the value of the hwprodstatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHWPRODSTATUS(String value) {
        this.hwprodstatus = value;
    }

    /**
     * Gets the value of the hwsearchstatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHWSEARCHSTATUS() {
        return hwsearchstatus;
    }

    /**
     * Sets the value of the hwsearchstatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHWSEARCHSTATUS(String value) {
        this.hwsearchstatus = value;
    }

}
