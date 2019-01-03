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
 *         &lt;choice>
 *           &lt;element name="TargetMeasurement">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;extension base="{}MEASUREMENT">
 *                   &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *                 &lt;/extension>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *           &lt;element name="TargetCurrency">
 *             &lt;complexType>
 *               &lt;complexContent>
 *                 &lt;extension base="{}CURRENCY">
 *                   &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *                 &lt;/extension>
 *               &lt;/complexContent>
 *             &lt;/complexType>
 *           &lt;/element>
 *         &lt;/choice>
 *         &lt;element name="ConversionFactor" type="{}String" minOccurs="0"/>
 *         &lt;element name="ConversionDt" type="{}String" minOccurs="0"/>
 *         &lt;element name="ConversionSource" type="{}String" minOccurs="0"/>
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
    "targetMeasurement",
    "targetCurrency",
    "conversionFactor",
    "conversionDt",
    "conversionSource"
})
@XmlRootElement(name = "ConversionRate")
public class ConversionRate {

    @XmlElement(name = "TargetMeasurement")
    protected ConversionRate.TargetMeasurement targetMeasurement;
    @XmlElement(name = "TargetCurrency")
    protected ConversionRate.TargetCurrency targetCurrency;
    @XmlElement(name = "ConversionFactor")
    protected services.verisk.iso.String conversionFactor;
    @XmlElement(name = "ConversionDt")
    protected services.verisk.iso.String conversionDt;
    @XmlElement(name = "ConversionSource")
    protected services.verisk.iso.String conversionSource;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the targetMeasurement property.
     * 
     * @return
     *     possible object is
     *     {@link ConversionRate.TargetMeasurement }
     *     
     */
    public ConversionRate.TargetMeasurement getTargetMeasurement() {
        return targetMeasurement;
    }

    /**
     * Sets the value of the targetMeasurement property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConversionRate.TargetMeasurement }
     *     
     */
    public void setTargetMeasurement(ConversionRate.TargetMeasurement value) {
        this.targetMeasurement = value;
    }

    /**
     * Gets the value of the targetCurrency property.
     * 
     * @return
     *     possible object is
     *     {@link ConversionRate.TargetCurrency }
     *     
     */
    public ConversionRate.TargetCurrency getTargetCurrency() {
        return targetCurrency;
    }

    /**
     * Sets the value of the targetCurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link ConversionRate.TargetCurrency }
     *     
     */
    public void setTargetCurrency(ConversionRate.TargetCurrency value) {
        this.targetCurrency = value;
    }

    /**
     * Gets the value of the conversionFactor property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getConversionFactor() {
        return conversionFactor;
    }

    /**
     * Sets the value of the conversionFactor property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setConversionFactor(services.verisk.iso.String value) {
        this.conversionFactor = value;
    }

    /**
     * Gets the value of the conversionDt property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getConversionDt() {
        return conversionDt;
    }

    /**
     * Sets the value of the conversionDt property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setConversionDt(services.verisk.iso.String value) {
        this.conversionDt = value;
    }

    /**
     * Gets the value of the conversionSource property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getConversionSource() {
        return conversionSource;
    }

    /**
     * Sets the value of the conversionSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setConversionSource(services.verisk.iso.String value) {
        this.conversionSource = value;
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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}CURRENCY">
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class TargetCurrency
        extends CURRENCY
    {

        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected java.lang.String id;

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


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;extension base="{}MEASUREMENT">
     *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
     *     &lt;/extension>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class TargetMeasurement
        extends MEASUREMENT
    {

        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected java.lang.String id;

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

}