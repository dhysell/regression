//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for customerOrganizationIdType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customerOrganizationIdType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="level1" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="level2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="level3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="level4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="level5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerOrganizationIdType", propOrder = {
    "level1",
    "level2",
    "level3",
    "level4",
    "level5"
})
public class CustomerOrganizationIdType {

    @XmlElement(required = true)
    protected String level1;
    protected String level2;
    protected String level3;
    protected String level4;
    protected String level5;

    /**
     * Gets the value of the level1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel1() {
        return level1;
    }

    /**
     * Sets the value of the level1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel1(String value) {
        this.level1 = value;
    }

    /**
     * Gets the value of the level2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel2() {
        return level2;
    }

    /**
     * Sets the value of the level2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel2(String value) {
        this.level2 = value;
    }

    /**
     * Gets the value of the level3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel3() {
        return level3;
    }

    /**
     * Sets the value of the level3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel3(String value) {
        this.level3 = value;
    }

    /**
     * Gets the value of the level4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel4() {
        return level4;
    }

    /**
     * Sets the value of the level4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel4(String value) {
        this.level4 = value;
    }

    /**
     * Gets the value of the level5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel5() {
        return level5;
    }

    /**
     * Sets the value of the level5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel5(String value) {
        this.level5 = value;
    }

}
