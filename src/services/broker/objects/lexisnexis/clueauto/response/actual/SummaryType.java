//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for summaryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="summaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="classification" type="{http://cp.com/rules/client}ClassificationType" minOccurs="0"/>
 *         &lt;element name="claim_reported">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="status">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Claims reported"/>
 *                       &lt;enumeration value="No claims reported"/>
 *                       &lt;enumeration value="Claims search unavailable"/>
 *                       &lt;enumeration value="Claims search not requested"/>
 *                       &lt;enumeration value="Search not processed; state limitation"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="count" type="{http://cp.com/rules/client}counterType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="prior_inquiries_reported">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="status">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Inquiry History reported"/>
 *                       &lt;enumeration value="No Inquiry History reported"/>
 *                       &lt;enumeration value="Inquiry History search unavailable"/>
 *                       &lt;enumeration value="Inquiry History search not requested"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="count" type="{http://cp.com/rules/client}counterType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="driverdiscovery_reported" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="status">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                       &lt;enumeration value="Driver Discovery reported"/>
 *                       &lt;enumeration value="No Driver Discovery reported"/>
 *                       &lt;enumeration value="Driver Discovery search unavailable"/>
 *                       &lt;enumeration value="Driver Discovery search not requested"/>
 *                       &lt;enumeration value="Driver Discovery state unavailable"/>
 *                       &lt;enumeration value="Requested state not on Driver Discovery database"/>
 *                       &lt;enumeration value="Search not processed; state limitation."/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="count" type="{http://cp.com/rules/client}counterType" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="at_fault_info_requested_status" type="{http://cp.com/rules/client}yesNoType" minOccurs="0"/>
 *         &lt;element name="homeowner_verification_status" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Processing complete with results"/>
 *               &lt;enumeration value="Processing complete no results available"/>
 *               &lt;enumeration value="Processing not complete. Application unavailable"/>
 *               &lt;enumeration value="Processing not complete. Invalid LexisNexis Account or Invalid LexisNexis Account-Z"/>
 *               &lt;enumeration value="Processing not complete. Insufficient data."/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "summaryType", propOrder = {
    "classification",
    "claimReported",
    "priorInquiriesReported",
    "driverdiscoveryReported",
    "atFaultInfoRequestedStatus",
    "homeownerVerificationStatus"
})
public class SummaryType {

    @XmlSchemaType(name = "string")
    protected ClassificationTypeEnum classification;
    @XmlElement(name = "claim_reported", required = true)
    protected SummaryType.ClaimReported claimReported;
    @XmlElement(name = "prior_inquiries_reported", required = true)
    protected SummaryType.PriorInquiriesReported priorInquiriesReported;
    @XmlElement(name = "driverdiscovery_reported")
    protected SummaryType.DriverdiscoveryReported driverdiscoveryReported;
    @XmlElement(name = "at_fault_info_requested_status")
    @XmlSchemaType(name = "string")
    protected YesNoType atFaultInfoRequestedStatus;
    @XmlElement(name = "homeowner_verification_status")
    protected SummaryType.HomeownerVerifStatusEnum homeownerVerificationStatus;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.ClassificationTypeEnum }
     *     
     */
    public ClassificationTypeEnum getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.ClassificationTypeEnum }
     *     
     */
    public void setClassification(ClassificationTypeEnum value) {
        this.classification = value;
    }

    /**
     * Gets the value of the claimReported property.
     * 
     * @return
     *     possible object is
     *     {@link SummaryType.ClaimReported }
     *     
     */
    public SummaryType.ClaimReported getClaimReported() {
        return claimReported;
    }

    /**
     * Sets the value of the claimReported property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummaryType.ClaimReported }
     *     
     */
    public void setClaimReported(SummaryType.ClaimReported value) {
        this.claimReported = value;
    }

    /**
     * Gets the value of the priorInquiriesReported property.
     * 
     * @return
     *     possible object is
     *     {@link SummaryType.PriorInquiriesReported }
     *     
     */
    public SummaryType.PriorInquiriesReported getPriorInquiriesReported() {
        return priorInquiriesReported;
    }

    /**
     * Sets the value of the priorInquiriesReported property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummaryType.PriorInquiriesReported }
     *     
     */
    public void setPriorInquiriesReported(SummaryType.PriorInquiriesReported value) {
        this.priorInquiriesReported = value;
    }

    /**
     * Gets the value of the driverdiscoveryReported property.
     * 
     * @return
     *     possible object is
     *     {@link SummaryType.DriverdiscoveryReported }
     *     
     */
    public SummaryType.DriverdiscoveryReported getDriverdiscoveryReported() {
        return driverdiscoveryReported;
    }

    /**
     * Sets the value of the driverdiscoveryReported property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummaryType.DriverdiscoveryReported }
     *     
     */
    public void setDriverdiscoveryReported(SummaryType.DriverdiscoveryReported value) {
        this.driverdiscoveryReported = value;
    }

    /**
     * Gets the value of the atFaultInfoRequestedStatus property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public YesNoType getAtFaultInfoRequestedStatus() {
        return atFaultInfoRequestedStatus;
    }

    /**
     * Sets the value of the atFaultInfoRequestedStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.clueauto.response.actual.YesNoType }
     *     
     */
    public void setAtFaultInfoRequestedStatus(YesNoType value) {
        this.atFaultInfoRequestedStatus = value;
    }

    /**
     * Gets the value of the homeownerVerificationStatus property.
     * 
     * @return
     *     possible object is
     *     {@link SummaryType.HomeownerVerifStatusEnum }
     *     
     */
    public SummaryType.HomeownerVerifStatusEnum getHomeownerVerificationStatus() {
        return homeownerVerificationStatus;
    }

    /**
     * Sets the value of the homeownerVerificationStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link SummaryType.HomeownerVerifStatusEnum }
     *     
     */
    public void setHomeownerVerificationStatus(SummaryType.HomeownerVerifStatusEnum value) {
        this.homeownerVerificationStatus = value;
    }

    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUnitNumber() {
        return unitNumber;
    }

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUnitNumber(BigInteger value) {
        this.unitNumber = value;
    }

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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="status">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Claims reported"/>
     *             &lt;enumeration value="No claims reported"/>
     *             &lt;enumeration value="Claims search unavailable"/>
     *             &lt;enumeration value="Claims search not requested"/>
     *             &lt;enumeration value="Search not processed; state limitation"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="count" type="{http://cp.com/rules/client}counterType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class ClaimReported {

        @XmlAttribute(name = "status")
        protected SummaryType.ClaimReported.ClaimReportedStatusEnum status;
        @XmlAttribute(name = "count")
        @XmlJavaTypeAdapter(Adapter5 .class)
        protected Integer count;

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link SummaryType.ClaimReported.ClaimReportedStatusEnum }
         *     
         */
        public SummaryType.ClaimReported.ClaimReportedStatusEnum getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link SummaryType.ClaimReported.ClaimReportedStatusEnum }
         *     
         */
        public void setStatus(SummaryType.ClaimReported.ClaimReportedStatusEnum value) {
            this.status = value;
        }

        /**
         * Gets the value of the count property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Integer getCount() {
            return count;
        }

        /**
         * Sets the value of the count property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCount(Integer value) {
            this.count = value;
        }


        /**
         * <p>Java class for null.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;simpleType>
         *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *     &lt;enumeration value="Claims reported"/>
         *     &lt;enumeration value="No claims reported"/>
         *     &lt;enumeration value="Claims search unavailable"/>
         *     &lt;enumeration value="Claims search not requested"/>
         *     &lt;enumeration value="Search not processed; state limitation"/>
         *   &lt;/restriction>
         * &lt;/simpleType>
         * </pre>
         * 
         */
        @XmlType(name = "")
        @XmlEnum
        public enum ClaimReportedStatusEnum {

            @XmlEnumValue("Claims reported")
            CLAIMS_REPORTED("Claims reported"),
            @XmlEnumValue("No claims reported")
            NO_CLAIMS_REPORTED("No claims reported"),
            @XmlEnumValue("Claims search unavailable")
            CLAIMS_SEARCH_UNAVAILABLE("Claims search unavailable"),
            @XmlEnumValue("Claims search not requested")
            CLAIMS_SEARCH_NOT_REQUESTED("Claims search not requested"),
            @XmlEnumValue("Search not processed; state limitation")
            SEARCH_NOT_PROCESSED_STATE_LIMITATION("Search not processed; state limitation");
            private final String value;

            ClaimReportedStatusEnum(String v) {
                value = v;
            }

            public String value() {
                return value;
            }

            public static SummaryType.ClaimReported.ClaimReportedStatusEnum fromValue(String v) {
                for (SummaryType.ClaimReported.ClaimReportedStatusEnum c: SummaryType.ClaimReported.ClaimReportedStatusEnum.values()) {
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
     *       &lt;attribute name="status">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Driver Discovery reported"/>
     *             &lt;enumeration value="No Driver Discovery reported"/>
     *             &lt;enumeration value="Driver Discovery search unavailable"/>
     *             &lt;enumeration value="Driver Discovery search not requested"/>
     *             &lt;enumeration value="Driver Discovery state unavailable"/>
     *             &lt;enumeration value="Requested state not on Driver Discovery database"/>
     *             &lt;enumeration value="Search not processed; state limitation."/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="count" type="{http://cp.com/rules/client}counterType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class DriverdiscoveryReported {

        @XmlAttribute(name = "status")
        protected SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum status;
        @XmlAttribute(name = "count")
        @XmlJavaTypeAdapter(Adapter5 .class)
        protected Integer count;

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum }
         *     
         */
        public SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum }
         *     
         */
        public void setStatus(SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum value) {
            this.status = value;
        }

        /**
         * Gets the value of the count property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Integer getCount() {
            return count;
        }

        /**
         * Sets the value of the count property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCount(Integer value) {
            this.count = value;
        }


        /**
         * <p>Java class for null.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;simpleType>
         *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *     &lt;enumeration value="Driver Discovery reported"/>
         *     &lt;enumeration value="No Driver Discovery reported"/>
         *     &lt;enumeration value="Driver Discovery search unavailable"/>
         *     &lt;enumeration value="Driver Discovery search not requested"/>
         *     &lt;enumeration value="Driver Discovery state unavailable"/>
         *     &lt;enumeration value="Requested state not on Driver Discovery database"/>
         *     &lt;enumeration value="Search not processed; state limitation."/>
         *   &lt;/restriction>
         * &lt;/simpleType>
         * </pre>
         * 
         */
        @XmlType(name = "")
        @XmlEnum
        public enum ADDReportedStatusEnum {

            @XmlEnumValue("Driver Discovery reported")
            DRIVER_DISCOVERY_REPORTED("Driver Discovery reported"),
            @XmlEnumValue("No Driver Discovery reported")
            NO_DRIVER_DISCOVERY_REPORTED("No Driver Discovery reported"),
            @XmlEnumValue("Driver Discovery search unavailable")
            DRIVER_DISCOVERY_SEARCH_UNAVAILABLE("Driver Discovery search unavailable"),
            @XmlEnumValue("Driver Discovery search not requested")
            DRIVER_DISCOVERY_SEARCH_NOT_REQUESTED("Driver Discovery search not requested"),
            @XmlEnumValue("Driver Discovery state unavailable")
            DRIVER_DISCOVERY_STATE_UNAVAILABLE("Driver Discovery state unavailable"),
            @XmlEnumValue("Requested state not on Driver Discovery database")
            REQUESTED_STATE_NOT_ON_DRIVER_DISCOVERY_DATABASE("Requested state not on Driver Discovery database"),
            @XmlEnumValue("Search not processed; state limitation.")
            SEARCH_NOT_PROCESSED_STATE_LIMITATION("Search not processed; state limitation.");
            private final String value;

            ADDReportedStatusEnum(String v) {
                value = v;
            }

            public String value() {
                return value;
            }

            public static SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum fromValue(String v) {
                for (SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum c: SummaryType.DriverdiscoveryReported.ADDReportedStatusEnum.values()) {
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
     *     &lt;enumeration value="Processing complete with results"/>
     *     &lt;enumeration value="Processing complete no results available"/>
     *     &lt;enumeration value="Processing not complete. Application unavailable"/>
     *     &lt;enumeration value="Processing not complete. Invalid LexisNexis Account or Invalid LexisNexis Account-Z"/>
     *     &lt;enumeration value="Processing not complete. Insufficient data."/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum HomeownerVerifStatusEnum {

        @XmlEnumValue("Processing complete with results")
        PROCESSING_COMPLETE_WITH_RESULTS("Processing complete with results"),
        @XmlEnumValue("Processing complete no results available")
        PROCESSING_COMPLETE_NO_RESULTS_AVAILABLE("Processing complete no results available"),
        @XmlEnumValue("Processing not complete. Application unavailable")
        PROCESSING_NOT_COMPLETE_APPLICATION_UNAVAILABLE("Processing not complete. Application unavailable"),
        @XmlEnumValue("Processing not complete. Invalid LexisNexis Account or Invalid LexisNexis Account-Z")
        PROCESSING_NOT_COMPLETE_INVALID_LEXIS_NEXIS_ACCOUNT_OR_INVALID_LEXIS_NEXIS_ACCOUNT_Z("Processing not complete. Invalid LexisNexis Account or Invalid LexisNexis Account-Z"),
        @XmlEnumValue("Processing not complete. Insufficient data.")
        PROCESSING_NOT_COMPLETE_INSUFFICIENT_DATA("Processing not complete. Insufficient data.");
        private final String value;

        HomeownerVerifStatusEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static SummaryType.HomeownerVerifStatusEnum fromValue(String v) {
            for (SummaryType.HomeownerVerifStatusEnum c: SummaryType.HomeownerVerifStatusEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
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
     *       &lt;attribute name="status">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *             &lt;enumeration value="Inquiry History reported"/>
     *             &lt;enumeration value="No Inquiry History reported"/>
     *             &lt;enumeration value="Inquiry History search unavailable"/>
     *             &lt;enumeration value="Inquiry History search not requested"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="count" type="{http://cp.com/rules/client}counterType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class PriorInquiriesReported {

        @XmlAttribute(name = "status")
        protected SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum status;
        @XmlAttribute(name = "count")
        @XmlJavaTypeAdapter(Adapter5 .class)
        protected Integer count;

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum }
         *     
         */
        public SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum }
         *     
         */
        public void setStatus(SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum value) {
            this.status = value;
        }

        /**
         * Gets the value of the count property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public Integer getCount() {
            return count;
        }

        /**
         * Sets the value of the count property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setCount(Integer value) {
            this.count = value;
        }


        /**
         * <p>Java class for null.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;simpleType>
         *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *     &lt;enumeration value="Inquiry History reported"/>
         *     &lt;enumeration value="No Inquiry History reported"/>
         *     &lt;enumeration value="Inquiry History search unavailable"/>
         *     &lt;enumeration value="Inquiry History search not requested"/>
         *   &lt;/restriction>
         * &lt;/simpleType>
         * </pre>
         * 
         */
        @XmlType(name = "")
        @XmlEnum
        public enum PriorInquiriesReportedStatusEnum {

            @XmlEnumValue("Inquiry History reported")
            INQUIRY_HISTORY_REPORTED("Inquiry History reported"),
            @XmlEnumValue("No Inquiry History reported")
            NO_INQUIRY_HISTORY_REPORTED("No Inquiry History reported"),
            @XmlEnumValue("Inquiry History search unavailable")
            INQUIRY_HISTORY_SEARCH_UNAVAILABLE("Inquiry History search unavailable"),
            @XmlEnumValue("Inquiry History search not requested")
            INQUIRY_HISTORY_SEARCH_NOT_REQUESTED("Inquiry History search not requested");
            private final String value;

            PriorInquiriesReportedStatusEnum(String v) {
                value = v;
            }

            public String value() {
                return value;
            }

            public static SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum fromValue(String v) {
                for (SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum c: SummaryType.PriorInquiriesReported.PriorInquiriesReportedStatusEnum.values()) {
                    if (c.value.equals(v)) {
                        return c;
                    }
                }
                throw new IllegalArgumentException(v);
            }

        }

    }

}
