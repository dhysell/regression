//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:19:26 PM MDT 
//


package services.verisk.iso;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element name="CodeListName" type="{}String" minOccurs="0"/>
 *         &lt;element name="CodeListVersion" type="{}String" minOccurs="0"/>
 *         &lt;element name="CodeListOwnerCd" type="{}StringCd" minOccurs="0"/>
 *         &lt;element name="Description" type="{}String" minOccurs="0"/>
 *         &lt;element name="WebsiteURL" type="{}String" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "codeListName",
    "codeListVersion",
    "codeListOwnerCd",
    "description",
    "websiteURL"
})
@XmlRootElement(name = "CodeList")
public class CodeList {

    @XmlElement(name = "CodeListName")
    protected services.verisk.iso.String codeListName;
    @XmlElement(name = "CodeListVersion")
    protected services.verisk.iso.String codeListVersion;
    @XmlElement(name = "CodeListOwnerCd")
    protected StringCd codeListOwnerCd;
    @XmlElement(name = "Description")
    protected services.verisk.iso.String description;
    @XmlElement(name = "WebsiteURL")
    protected services.verisk.iso.String websiteURL;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the codeListName property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getCodeListName() {
        return codeListName;
    }

    /**
     * Sets the value of the codeListName property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setCodeListName(services.verisk.iso.String value) {
        this.codeListName = value;
    }

    /**
     * Gets the value of the codeListVersion property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getCodeListVersion() {
        return codeListVersion;
    }

    /**
     * Sets the value of the codeListVersion property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setCodeListVersion(services.verisk.iso.String value) {
        this.codeListVersion = value;
    }

    /**
     * Gets the value of the codeListOwnerCd property.
     * 
     * @return
     *     possible object is
     *     {@link StringCd }
     *     
     */
    public StringCd getCodeListOwnerCd() {
        return codeListOwnerCd;
    }

    /**
     * Sets the value of the codeListOwnerCd property.
     * 
     * @param value
     *     allowed object is
     *     {@link StringCd }
     *     
     */
    public void setCodeListOwnerCd(StringCd value) {
        this.codeListOwnerCd = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setDescription(services.verisk.iso.String value) {
        this.description = value;
    }

    /**
     * Gets the value of the websiteURL property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getWebsiteURL() {
        return websiteURL;
    }

    /**
     * Sets the value of the websiteURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setWebsiteURL(services.verisk.iso.String value) {
        this.websiteURL = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link java.lang.String }
     *     
     */
    public java.lang.String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link java.lang.String }
     *     
     */
    public void setId(java.lang.String value) {
        this.id = value;
    }

}