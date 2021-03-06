//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:19:26 PM MDT 
//


package services.verisk.iso;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element name="CommercialName" type="{}String"/>
 *         &lt;element name="IndexName" type="{}String" minOccurs="0"/>
 *         &lt;element name="SupplementaryNameInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="SupplementaryNameCd" type="{}StringCd"/>
 *                   &lt;element name="SupplementaryName" type="{}String"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "commercialName",
    "indexName",
    "supplementaryNameInfo"
})
@XmlRootElement(name = "CommlName")
public class CommlName {

    @XmlElement(name = "CommercialName", required = true)
    protected services.verisk.iso.String commercialName;
    @XmlElement(name = "IndexName")
    protected services.verisk.iso.String indexName;
    @XmlElement(name = "SupplementaryNameInfo")
    protected List<CommlName.SupplementaryNameInfo> supplementaryNameInfo;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the commercialName property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getCommercialName() {
        return commercialName;
    }

    /**
     * Sets the value of the commercialName property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setCommercialName(services.verisk.iso.String value) {
        this.commercialName = value;
    }

    /**
     * Gets the value of the indexName property.
     * 
     * @return
     *     possible object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public services.verisk.iso.String getIndexName() {
        return indexName;
    }

    /**
     * Sets the value of the indexName property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.verisk.iso.String }
     *     
     */
    public void setIndexName(services.verisk.iso.String value) {
        this.indexName = value;
    }

    /**
     * Gets the value of the supplementaryNameInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the supplementaryNameInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSupplementaryNameInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommlName.SupplementaryNameInfo }
     * 
     * 
     */
    public List<CommlName.SupplementaryNameInfo> getSupplementaryNameInfo() {
        if (supplementaryNameInfo == null) {
            supplementaryNameInfo = new ArrayList<CommlName.SupplementaryNameInfo>();
        }
        return this.supplementaryNameInfo;
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
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="SupplementaryNameCd" type="{}StringCd"/>
     *         &lt;element name="SupplementaryName" type="{}String"/>
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
        "supplementaryNameCd",
        "supplementaryName"
    })
    public static class SupplementaryNameInfo {

        @XmlElement(name = "SupplementaryNameCd", required = true)
        protected StringCd supplementaryNameCd;
        @XmlElement(name = "SupplementaryName", required = true)
        protected services.verisk.iso.String supplementaryName;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected java.lang.String id;

        /**
         * Gets the value of the supplementaryNameCd property.
         * 
         * @return
         *     possible object is
         *     {@link StringCd }
         *     
         */
        public StringCd getSupplementaryNameCd() {
            return supplementaryNameCd;
        }

        /**
         * Sets the value of the supplementaryNameCd property.
         * 
         * @param value
         *     allowed object is
         *     {@link StringCd }
         *     
         */
        public void setSupplementaryNameCd(StringCd value) {
            this.supplementaryNameCd = value;
        }

        /**
         * Gets the value of the supplementaryName property.
         * 
         * @return
         *     possible object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public services.verisk.iso.String getSupplementaryName() {
            return supplementaryName;
        }

        /**
         * Sets the value of the supplementaryName property.
         * 
         * @param value
         *     allowed object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public void setSupplementaryName(services.verisk.iso.String value) {
            this.supplementaryName = value;
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

}
