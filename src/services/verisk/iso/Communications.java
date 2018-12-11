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
 *         &lt;element name="PhoneInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PhoneTypeCd" type="{}StringCd" minOccurs="0"/>
 *                   &lt;element name="CommunicationUseCd" type="{}StringCd" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="PhoneNumber" type="{}String"/>
 *                   &lt;element name="EmailAddr" type="{}String" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="EmailInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CommunicationUseCd" type="{}StringCd" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="EmailAddr" type="{}String"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="WebsiteInfo" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="CommunicationUseCd" type="{}StringCd" minOccurs="0"/>
 *                   &lt;element name="WebsiteURL" type="{}String"/>
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
    "phoneInfo",
    "emailInfo",
    "websiteInfo"
})
@XmlRootElement(name = "Communications")
public class Communications {

    @XmlElement(name = "PhoneInfo")
    protected List<Communications.PhoneInfo> phoneInfo;
    @XmlElement(name = "EmailInfo")
    protected List<Communications.EmailInfo> emailInfo;
    @XmlElement(name = "WebsiteInfo")
    protected List<Communications.WebsiteInfo> websiteInfo;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected java.lang.String id;

    /**
     * Gets the value of the phoneInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the phoneInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPhoneInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Communications.PhoneInfo }
     * 
     * 
     */
    public List<Communications.PhoneInfo> getPhoneInfo() {
        if (phoneInfo == null) {
            phoneInfo = new ArrayList<Communications.PhoneInfo>();
        }
        return this.phoneInfo;
    }

    /**
     * Gets the value of the emailInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the emailInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEmailInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Communications.EmailInfo }
     * 
     * 
     */
    public List<Communications.EmailInfo> getEmailInfo() {
        if (emailInfo == null) {
            emailInfo = new ArrayList<Communications.EmailInfo>();
        }
        return this.emailInfo;
    }

    /**
     * Gets the value of the websiteInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the websiteInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWebsiteInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Communications.WebsiteInfo }
     * 
     * 
     */
    public List<Communications.WebsiteInfo> getWebsiteInfo() {
        if (websiteInfo == null) {
            websiteInfo = new ArrayList<Communications.WebsiteInfo>();
        }
        return this.websiteInfo;
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
     *         &lt;element name="CommunicationUseCd" type="{}StringCd" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="EmailAddr" type="{}String"/>
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
        "communicationUseCd",
        "emailAddr"
    })
    public static class EmailInfo {

        @XmlElement(name = "CommunicationUseCd")
        protected List<StringCd> communicationUseCd;
        @XmlElement(name = "EmailAddr", required = true)
        protected services.verisk.iso.String emailAddr;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected java.lang.String id;

        /**
         * Gets the value of the communicationUseCd property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the communicationUseCd property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCommunicationUseCd().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StringCd }
         * 
         * 
         */
        public List<StringCd> getCommunicationUseCd() {
            if (communicationUseCd == null) {
                communicationUseCd = new ArrayList<StringCd>();
            }
            return this.communicationUseCd;
        }

        /**
         * Gets the value of the emailAddr property.
         * 
         * @return
         *     possible object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public services.verisk.iso.String getEmailAddr() {
            return emailAddr;
        }

        /**
         * Sets the value of the emailAddr property.
         * 
         * @param value
         *     allowed object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public void setEmailAddr(services.verisk.iso.String value) {
            this.emailAddr = value;
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
     *         &lt;element name="PhoneTypeCd" type="{}StringCd" minOccurs="0"/>
     *         &lt;element name="CommunicationUseCd" type="{}StringCd" maxOccurs="unbounded" minOccurs="0"/>
     *         &lt;element name="PhoneNumber" type="{}String"/>
     *         &lt;element name="EmailAddr" type="{}String" minOccurs="0"/>
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
        "phoneTypeCd",
        "communicationUseCd",
        "phoneNumber",
        "emailAddr"
    })
    public static class PhoneInfo {

        @XmlElement(name = "PhoneTypeCd")
        protected StringCd phoneTypeCd;
        @XmlElement(name = "CommunicationUseCd")
        protected List<StringCd> communicationUseCd;
        @XmlElement(name = "PhoneNumber", required = true)
        protected services.verisk.iso.String phoneNumber;
        @XmlElement(name = "EmailAddr")
        protected services.verisk.iso.String emailAddr;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected java.lang.String id;

        /**
         * Gets the value of the phoneTypeCd property.
         * 
         * @return
         *     possible object is
         *     {@link StringCd }
         *     
         */
        public StringCd getPhoneTypeCd() {
            return phoneTypeCd;
        }

        /**
         * Sets the value of the phoneTypeCd property.
         * 
         * @param value
         *     allowed object is
         *     {@link StringCd }
         *     
         */
        public void setPhoneTypeCd(StringCd value) {
            this.phoneTypeCd = value;
        }

        /**
         * Gets the value of the communicationUseCd property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the communicationUseCd property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getCommunicationUseCd().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StringCd }
         * 
         * 
         */
        public List<StringCd> getCommunicationUseCd() {
            if (communicationUseCd == null) {
                communicationUseCd = new ArrayList<StringCd>();
            }
            return this.communicationUseCd;
        }

        /**
         * Gets the value of the phoneNumber property.
         * 
         * @return
         *     possible object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public services.verisk.iso.String getPhoneNumber() {
            return phoneNumber;
        }

        /**
         * Sets the value of the phoneNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public void setPhoneNumber(services.verisk.iso.String value) {
            this.phoneNumber = value;
        }

        /**
         * Gets the value of the emailAddr property.
         * 
         * @return
         *     possible object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public services.verisk.iso.String getEmailAddr() {
            return emailAddr;
        }

        /**
         * Sets the value of the emailAddr property.
         * 
         * @param value
         *     allowed object is
         *     {@link services.verisk.iso.String }
         *     
         */
        public void setEmailAddr(services.verisk.iso.String value) {
            this.emailAddr = value;
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
     *         &lt;element name="CommunicationUseCd" type="{}StringCd" minOccurs="0"/>
     *         &lt;element name="WebsiteURL" type="{}String"/>
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
        "communicationUseCd",
        "websiteURL"
    })
    public static class WebsiteInfo {

        @XmlElement(name = "CommunicationUseCd")
        protected StringCd communicationUseCd;
        @XmlElement(name = "WebsiteURL", required = true)
        protected services.verisk.iso.String websiteURL;
        @XmlAttribute(name = "id")
        @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
        @XmlID
        @XmlSchemaType(name = "ID")
        protected java.lang.String id;

        /**
         * Gets the value of the communicationUseCd property.
         * 
         * @return
         *     possible object is
         *     {@link StringCd }
         *     
         */
        public StringCd getCommunicationUseCd() {
            return communicationUseCd;
        }

        /**
         * Sets the value of the communicationUseCd property.
         * 
         * @param value
         *     allowed object is
         *     {@link StringCd }
         *     
         */
        public void setCommunicationUseCd(StringCd value) {
            this.communicationUseCd = value;
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

}
