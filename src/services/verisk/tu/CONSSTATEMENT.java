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
 *         &lt;element ref="{}CS_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}CS_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}CS_CONT_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}INFO" minOccurs="0"/>
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
    "cssegtype",
    "csseglength",
    "csconttype",
    "info"
})
@XmlRootElement(name = "CONS-STATEMENT")
public class CONSSTATEMENT {

    @XmlElement(name = "CS_SEG_TYPE")
    protected String cssegtype;
    @XmlElement(name = "CS_SEG_LENGTH")
    protected String csseglength;
    @XmlElement(name = "CS_CONT_TYPE")
    protected String csconttype;
    @XmlElement(name = "INFO")
    protected String info;

    /**
     * Gets the value of the cssegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSSEGTYPE() {
        return cssegtype;
    }

    /**
     * Sets the value of the cssegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSSEGTYPE(String value) {
        this.cssegtype = value;
    }

    /**
     * Gets the value of the csseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSSEGLENGTH() {
        return csseglength;
    }

    /**
     * Sets the value of the csseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSSEGLENGTH(String value) {
        this.csseglength = value;
    }

    /**
     * Gets the value of the csconttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSCONTTYPE() {
        return csconttype;
    }

    /**
     * Sets the value of the csconttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSCONTTYPE(String value) {
        this.csconttype = value;
    }

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINFO() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINFO(String value) {
        this.info = value;
    }

}
