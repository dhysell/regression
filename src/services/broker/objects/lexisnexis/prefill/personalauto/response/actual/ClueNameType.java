//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clueNameType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clueNameType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cp.com/rules/client}nameType">
 *       &lt;sequence>
 *         &lt;element name="fsi_prefix" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_first" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_middle" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_last" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_suffix" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_maiden" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clueNameType", propOrder = {
    "fsiPrefix",
    "fsiFirst",
    "fsiMiddle",
    "fsiLast",
    "fsiSuffix",
    "fsiMaiden"
})
public class ClueNameType
    extends NameType
{

    @XmlElement(name = "fsi_prefix")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiPrefix;
    @XmlElement(name = "fsi_first")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiFirst;
    @XmlElement(name = "fsi_middle")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiMiddle;
    @XmlElement(name = "fsi_last")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiLast;
    @XmlElement(name = "fsi_suffix")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiSuffix;
    @XmlElement(name = "fsi_maiden")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiMaiden;

    /**
     * Gets the value of the fsiPrefix property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiPrefix() {
        return fsiPrefix;
    }

    /**
     * Sets the value of the fsiPrefix property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiPrefix(FSIEnum value) {
        this.fsiPrefix = value;
    }

    /**
     * Gets the value of the fsiFirst property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiFirst() {
        return fsiFirst;
    }

    /**
     * Sets the value of the fsiFirst property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiFirst(FSIEnum value) {
        this.fsiFirst = value;
    }

    /**
     * Gets the value of the fsiMiddle property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiMiddle() {
        return fsiMiddle;
    }

    /**
     * Sets the value of the fsiMiddle property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiMiddle(FSIEnum value) {
        this.fsiMiddle = value;
    }

    /**
     * Gets the value of the fsiLast property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiLast() {
        return fsiLast;
    }

    /**
     * Sets the value of the fsiLast property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiLast(FSIEnum value) {
        this.fsiLast = value;
    }

    /**
     * Gets the value of the fsiSuffix property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiSuffix() {
        return fsiSuffix;
    }

    /**
     * Sets the value of the fsiSuffix property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiSuffix(FSIEnum value) {
        this.fsiSuffix = value;
    }

    /**
     * Gets the value of the fsiMaiden property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiMaiden() {
        return fsiMaiden;
    }

    /**
     * Sets the value of the fsiMaiden property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiMaiden(FSIEnum value) {
        this.fsiMaiden = value;
    }

}
