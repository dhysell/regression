//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
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
 *         &lt;element name="claims_history" maxOccurs="4" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="claim" maxOccurs="unbounded">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;extension base="{http://cp.com/rules/client}claimType">
 *                           &lt;sequence>
 *                             &lt;element name="first_payment_date" type="{http://cp.com/rules/client}LenientDateType" minOccurs="0"/>
 *                             &lt;element name="claim_association_indicator" minOccurs="0">
 *                               &lt;simpleType>
 *                                 &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                                   &lt;enumeration value="Indicates the policyholder was the person matched"/>
 *                                   &lt;enumeration value="Indicates the Vehicle Operator was the person matched"/>
 *                                 &lt;/restriction>
 *                               &lt;/simpleType>
 *                             &lt;/element>
 *                             &lt;element name="vehicle" type="{http://cp.com/rules/client}clueVehicleType"/>
 *                           &lt;/sequence>
 *                         &lt;/extension>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="type" use="required">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Subject Section"/>
 *                       &lt;enumeration value="Vehicle Section"/>
 *                       &lt;enumeration value="Possible Related Section"/>
 *                       &lt;enumeration value="Agent (Summary) Section"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="inquiry_history" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="inquiry_handling_history" maxOccurs="25" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="date" type="{http://cp.com/rules/client}LenientDateType" />
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="quoteback" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="additional_driver_discovery" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="subject" type="{http://cp.com/rules/client}clueBaseSubjectType" maxOccurs="10"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="additional_information" type="{http://cp.com/rules/client}messageListType" minOccurs="0"/>
 *         &lt;element name="attached_products" type="{http://cp.com/rules/client}attachedProductsType" minOccurs="0"/>
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
    "claimsHistory",
    "inquiryHistory",
    "additionalDriverDiscovery",
    "additionalInformation",
    "attachedProducts"
})
@XmlRootElement(name = "results_dataset")
public class ResultsDataset {

    @XmlElement(name = "claims_history")
    protected List<ResultsDataset.ClaimsHistory> claimsHistory;
    @XmlElement(name = "inquiry_history")
    protected ResultsDataset.InquiryHistory inquiryHistory;
    @XmlElement(name = "additional_driver_discovery")
    protected ResultsDataset.AdditionalDriverDiscovery additionalDriverDiscovery;
    @XmlElement(name = "additional_information")
    protected MessageListType additionalInformation;
    @XmlElement(name = "attached_products")
    protected AttachedProductsType attachedProducts;

    /**
     * Gets the value of the claimsHistory property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the claimsHistory property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClaimsHistory().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ResultsDataset.ClaimsHistory }
     * 
     * 
     */
    public List<ResultsDataset.ClaimsHistory> getClaimsHistory() {
        if (claimsHistory == null) {
            claimsHistory = new ArrayList<ResultsDataset.ClaimsHistory>();
        }
        return this.claimsHistory;
    }

    /**
     * Gets the value of the inquiryHistory property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsDataset.InquiryHistory }
     *     
     */
    public ResultsDataset.InquiryHistory getInquiryHistory() {
        return inquiryHistory;
    }

    /**
     * Sets the value of the inquiryHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsDataset.InquiryHistory }
     *     
     */
    public void setInquiryHistory(ResultsDataset.InquiryHistory value) {
        this.inquiryHistory = value;
    }

    /**
     * Gets the value of the additionalDriverDiscovery property.
     * 
     * @return
     *     possible object is
     *     {@link ResultsDataset.AdditionalDriverDiscovery }
     *     
     */
    public ResultsDataset.AdditionalDriverDiscovery getAdditionalDriverDiscovery() {
        return additionalDriverDiscovery;
    }

    /**
     * Sets the value of the additionalDriverDiscovery property.
     * 
     * @param value
     *     allowed object is
     *     {@link ResultsDataset.AdditionalDriverDiscovery }
     *     
     */
    public void setAdditionalDriverDiscovery(ResultsDataset.AdditionalDriverDiscovery value) {
        this.additionalDriverDiscovery = value;
    }

    /**
     * Gets the value of the additionalInformation property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.MessageListType }
     *     
     */
    public MessageListType getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Sets the value of the additionalInformation property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.MessageListType }
     *     
     */
    public void setAdditionalInformation(MessageListType value) {
        this.additionalInformation = value;
    }

    /**
     * Gets the value of the attachedProducts property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.AttachedProductsType }
     *     
     */
    public AttachedProductsType getAttachedProducts() {
        return attachedProducts;
    }

    /**
     * Sets the value of the attachedProducts property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.AttachedProductsType }
     *     
     */
    public void setAttachedProducts(AttachedProductsType value) {
        this.attachedProducts = value;
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
     *         &lt;element name="subject" type="{http://cp.com/rules/client}clueBaseSubjectType" maxOccurs="10"/>
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
        "subject"
    })
    public static class AdditionalDriverDiscovery {

        @XmlElement(required = true)
        protected List<ClueBaseSubjectType> subject;

        /**
         * Gets the value of the subject property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the subject property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSubject().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ClueBaseSubjectType }
         * 
         * 
         */
        public List<ClueBaseSubjectType> getSubject() {
            if (subject == null) {
                subject = new ArrayList<ClueBaseSubjectType>();
            }
            return this.subject;
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
     *         &lt;element name="claim" maxOccurs="unbounded">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;extension base="{http://cp.com/rules/client}claimType">
     *                 &lt;sequence>
     *                   &lt;element name="first_payment_date" type="{http://cp.com/rules/client}LenientDateType" minOccurs="0"/>
     *                   &lt;element name="claim_association_indicator" minOccurs="0">
     *                     &lt;simpleType>
     *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *                         &lt;enumeration value="Indicates the policyholder was the person matched"/>
     *                         &lt;enumeration value="Indicates the Vehicle Operator was the person matched"/>
     *                       &lt;/restriction>
     *                     &lt;/simpleType>
     *                   &lt;/element>
     *                   &lt;element name="vehicle" type="{http://cp.com/rules/client}clueVehicleType"/>
     *                 &lt;/sequence>
     *               &lt;/extension>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *       &lt;/sequence>
     *       &lt;attribute name="type" use="required">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Subject Section"/>
     *             &lt;enumeration value="Vehicle Section"/>
     *             &lt;enumeration value="Possible Related Section"/>
     *             &lt;enumeration value="Agent (Summary) Section"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "claim"
    })
    public static class ClaimsHistory {

        @XmlElement(required = true)
        protected List<ResultsDataset.ClaimsHistory.Claim> claim;
        @XmlAttribute(name = "type", required = true)
        protected ResultsDataset.ClaimsHistory.ClaimsHistoryEnum type;

        /**
         * Gets the value of the claim property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the claim property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getClaim().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ResultsDataset.ClaimsHistory.Claim }
         * 
         * 
         */
        public List<ResultsDataset.ClaimsHistory.Claim> getClaim() {
            if (claim == null) {
                claim = new ArrayList<ResultsDataset.ClaimsHistory.Claim>();
            }
            return this.claim;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link ResultsDataset.ClaimsHistory.ClaimsHistoryEnum }
         *     
         */
        public ResultsDataset.ClaimsHistory.ClaimsHistoryEnum getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link ResultsDataset.ClaimsHistory.ClaimsHistoryEnum }
         *     
         */
        public void setType(ResultsDataset.ClaimsHistory.ClaimsHistoryEnum value) {
            this.type = value;
        }


        /**
         * <p>Java class for anonymous complex type.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * 
         * <pre>
         * &lt;complexType>
         *   &lt;complexContent>
         *     &lt;extension base="{http://cp.com/rules/client}claimType">
         *       &lt;sequence>
         *         &lt;element name="first_payment_date" type="{http://cp.com/rules/client}LenientDateType" minOccurs="0"/>
         *         &lt;element name="claim_association_indicator" minOccurs="0">
         *           &lt;simpleType>
         *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *               &lt;enumeration value="Indicates the policyholder was the person matched"/>
         *               &lt;enumeration value="Indicates the Vehicle Operator was the person matched"/>
         *             &lt;/restriction>
         *           &lt;/simpleType>
         *         &lt;/element>
         *         &lt;element name="vehicle" type="{http://cp.com/rules/client}clueVehicleType"/>
         *       &lt;/sequence>
         *     &lt;/extension>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "firstPaymentDate",
            "claimAssociationIndicator",
            "vehicle"
        })
        public static class Claim
            extends ClaimType
        {

            @XmlElement(name = "first_payment_date")
            protected String firstPaymentDate;
            @XmlElement(name = "claim_association_indicator")
            protected ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum claimAssociationIndicator;
            @XmlElement(required = true)
            protected ClueVehicleType vehicle;

            /**
             * Gets the value of the firstPaymentDate property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getFirstPaymentDate() {
                return firstPaymentDate;
            }

            /**
             * Sets the value of the firstPaymentDate property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setFirstPaymentDate(String value) {
                this.firstPaymentDate = value;
            }

            /**
             * Gets the value of the claimAssociationIndicator property.
             * 
             * @return
             *     possible object is
             *     {@link ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum }
             *     
             */
            public ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum getClaimAssociationIndicator() {
                return claimAssociationIndicator;
            }

            /**
             * Sets the value of the claimAssociationIndicator property.
             * 
             * @param value
             *     allowed object is
             *     {@link ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum }
             *     
             */
            public void setClaimAssociationIndicator(ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum value) {
                this.claimAssociationIndicator = value;
            }

            /**
             * Gets the value of the vehicle property.
             * 
             * @return
             *     possible object is
             *     {@link broker.objects.lexisnexis.clueauto.response.actual.ClueVehicleType }
             *     
             */
            public ClueVehicleType getVehicle() {
                return vehicle;
            }

            /**
             * Sets the value of the vehicle property.
             * 
             * @param value
             *     allowed object is
             *     {@link broker.objects.lexisnexis.clueauto.response.actual.ClueVehicleType }
             *     
             */
            public void setVehicle(ClueVehicleType value) {
                this.vehicle = value;
            }


            /**
             * <p>Java class for null.
             * 
             * <p>The following schema fragment specifies the expected content contained within this class.
             * <p>
             * <pre>
             * &lt;simpleType>
             *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
             *     &lt;enumeration value="Indicates the policyholder was the person matched"/>
             *     &lt;enumeration value="Indicates the Vehicle Operator was the person matched"/>
             *   &lt;/restriction>
             * &lt;/simpleType>
             * </pre>
             * 
             */
            @XmlType(name = "")
            @XmlEnum
            public enum ClaimAssociationIndicatorEnum {

                @XmlEnumValue("Indicates the policyholder was the person matched")
                INDICATES_THE_POLICYHOLDER_WAS_THE_PERSON_MATCHED("Indicates the policyholder was the person matched"),
                @XmlEnumValue("Indicates the Vehicle Operator was the person matched")
                INDICATES_THE_VEHICLE_OPERATOR_WAS_THE_PERSON_MATCHED("Indicates the Vehicle Operator was the person matched");
                private final String value;

                ClaimAssociationIndicatorEnum(String v) {
                    value = v;
                }

                public String value() {
                    return value;
                }

                public static ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum fromValue(String v) {
                    for (ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum c: ResultsDataset.ClaimsHistory.Claim.ClaimAssociationIndicatorEnum.values()) {
                        if (c.value.equals(v)) {
                            return c;
                        }
                    }
                    throw new IllegalArgumentException(v);
                }

            }

        }


        /**
         * <p>Java class for null.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;simpleType>
         *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *     &lt;enumeration value="Subject Section"/>
         *     &lt;enumeration value="Vehicle Section"/>
         *     &lt;enumeration value="Possible Related Section"/>
         *     &lt;enumeration value="Agent (Summary) Section"/>
         *   &lt;/restriction>
         * &lt;/simpleType>
         * </pre>
         * 
         */
        @XmlType(name = "")
        @XmlEnum
        public enum ClaimsHistoryEnum {

            @XmlEnumValue("Subject Section")
            SUBJECT_SECTION("Subject Section"),
            @XmlEnumValue("Vehicle Section")
            VEHICLE_SECTION("Vehicle Section"),
            @XmlEnumValue("Possible Related Section")
            POSSIBLE_RELATED_SECTION("Possible Related Section"),
            @XmlEnumValue("Agent (Summary) Section")
            AGENT_SUMMARY_SECTION("Agent (Summary) Section");
            private final String value;

            ClaimsHistoryEnum(String v) {
                value = v;
            }

            public String value() {
                return value;
            }

            public static ResultsDataset.ClaimsHistory.ClaimsHistoryEnum fromValue(String v) {
                for (ResultsDataset.ClaimsHistory.ClaimsHistoryEnum c: ResultsDataset.ClaimsHistory.ClaimsHistoryEnum.values()) {
                    if (c.value.equals(v)) {
                        return c;
                    }
                }
                throw new IllegalArgumentException(v);
            }

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
     *         &lt;element name="inquiry_handling_history" maxOccurs="25" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="date" type="{http://cp.com/rules/client}LenientDateType" />
     *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *                 &lt;attribute name="quoteback" type="{http://www.w3.org/2001/XMLSchema}string" />
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
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
        "inquiryHandlingHistory"
    })
    public static class InquiryHistory {

        @XmlElement(name = "inquiry_handling_history")
        protected List<ResultsDataset.InquiryHistory.InquiryHandlingHistory> inquiryHandlingHistory;

        /**
         * Gets the value of the inquiryHandlingHistory property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the inquiryHandlingHistory property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getInquiryHandlingHistory().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ResultsDataset.InquiryHistory.InquiryHandlingHistory }
         * 
         * 
         */
        public List<ResultsDataset.InquiryHistory.InquiryHandlingHistory> getInquiryHandlingHistory() {
            if (inquiryHandlingHistory == null) {
                inquiryHandlingHistory = new ArrayList<ResultsDataset.InquiryHistory.InquiryHandlingHistory>();
            }
            return this.inquiryHandlingHistory;
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
         *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="date" type="{http://cp.com/rules/client}LenientDateType" />
         *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
         *       &lt;attribute name="quoteback" type="{http://www.w3.org/2001/XMLSchema}string" />
         *     &lt;/restriction>
         *   &lt;/complexContent>
         * &lt;/complexType>
         * </pre>
         * 
         * 
         */
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class InquiryHandlingHistory {

            @XmlAttribute(name = "id")
            protected String id;
            @XmlAttribute(name = "date")
            protected String date;
            @XmlAttribute(name = "name")
            protected String name;
            @XmlAttribute(name = "quoteback")
            protected String quoteback;

            /**
             * Gets the value of the id property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getId() {
                return id;
            }

            /**
             * Sets the value of the id property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setId(String value) {
                this.id = value;
            }

            /**
             * Gets the value of the date property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDate() {
                return date;
            }

            /**
             * Sets the value of the date property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDate(String value) {
                this.date = value;
            }

            /**
             * Gets the value of the name property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getName() {
                return name;
            }

            /**
             * Sets the value of the name property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setName(String value) {
                this.name = value;
            }

            /**
             * Gets the value of the quoteback property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getQuoteback() {
                return quoteback;
            }

            /**
             * Sets the value of the quoteback property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setQuoteback(String value) {
                this.quoteback = value;
            }

        }

    }

}
