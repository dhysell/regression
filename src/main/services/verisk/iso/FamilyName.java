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
 *         &lt;element name="TitlePrefix" type="{}String" minOccurs="0"/>
 *         &lt;element name="FamilyNames" type="{}String"/>
 *         &lt;element name="Surname" type="{}String"/>
 *         &lt;element name="NameSuffix" type="{}String" minOccurs="0"/>
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
    "titlePrefix",
    "familyNames",
    "surname",
    "nameSuffix"
})
@XmlRootElement(name = "FamilyName")
public class FamilyName {

    @XmlElement(name = "TitlePrefix")
    protected services.verisk.iso.String titlePrefix;
    @XmlElement(name = "FamilyNames", required = true)
    protected services.verisk.iso.String familyNames;
    @XmlElement(name = "Surname", required = true)
    protected services.verisk.iso.String surname;
    @XmlElement(name = "NameSuffix")
    protected services.verisk.iso.String nameSuffix;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the titlePrefix property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getTitlePrefix() {
        return titlePrefix;
    }

    /**
     * Sets the value of the titlePrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setTitlePrefix(services.verisk.iso.String value) {
        this.titlePrefix = value;
    }

    /**
     * Gets the value of the familyNames property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getFamilyNames() {
        return familyNames;
    }

    /**
     * Sets the value of the familyNames property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setFamilyNames(services.verisk.iso.String value) {
        this.familyNames = value;
    }

    /**
     * Gets the value of the surname property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getSurname() {
        return surname;
    }

    /**
     * Sets the value of the surname property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setSurname(services.verisk.iso.String value) {
        this.surname = value;
    }

    /**
     * Gets the value of the nameSuffix property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getNameSuffix() {
        return nameSuffix;
    }

    /**
     * Sets the value of the nameSuffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setNameSuffix(services.verisk.iso.String value) {
        this.nameSuffix = value;
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
