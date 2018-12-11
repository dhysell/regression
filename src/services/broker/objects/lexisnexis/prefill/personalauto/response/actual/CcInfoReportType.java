//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cc_info_reportType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cc_info_reportType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CC_report" type="{http://cp.com/rules/client}messageListType" minOccurs="0"/>
 *         &lt;element name="discovered_subjects" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="subject" type="{http://cp.com/rules/client}dataprefill_clue_ADDSubjectType" maxOccurs="100"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="discovered_vehicles" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="vehicle" maxOccurs="25">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="vehicle_detail" type="{http://cp.com/rules/client}resultVehicleType"/>
 *                             &lt;element name="policy_detail" maxOccurs="3" minOccurs="0">
 *                               &lt;complexType>
 *                                 &lt;complexContent>
 *                                   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                                     &lt;sequence>
 *                                       &lt;element name="business_vehicle" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *                                       &lt;element name="coverage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                       &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                       &lt;element name="deductible" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                                     &lt;/sequence>
 *                                   &lt;/restriction>
 *                                 &lt;/complexContent>
 *                               &lt;/complexType>
 *                             &lt;/element>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
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
@XmlType(name = "cc_info_reportType", propOrder = {
    "ccReport",
    "discoveredSubjects",
    "discoveredVehicles"
})
public class CcInfoReportType {

    @XmlElement(name = "CC_report")
    protected MessageListType ccReport;
    @XmlElement(name = "discovered_subjects")
    protected CcInfoReportType.DiscoveredSubjects discoveredSubjects;
    @XmlElement(name = "discovered_vehicles")
    protected CcInfoReportType.DiscoveredVehicles discoveredVehicles;

    /**
     * Gets the value of the ccReport property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.MessageListType }
     *     
     */
    public MessageListType getCCReport() {
        return ccReport;
    }

    /**
     * Sets the value of the ccReport property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.MessageListType }
     *     
     */
    public void setCCReport(MessageListType value) {
        this.ccReport = value;
    }

    /**
     * Gets the value of the discoveredSubjects property.
     * 
     * @return
     *     possible object is
     *     {@link CcInfoReportType.DiscoveredSubjects }
     *     
     */
    public CcInfoReportType.DiscoveredSubjects getDiscoveredSubjects() {
        return discoveredSubjects;
    }

    /**
     * Sets the value of the discoveredSubjects property.
     * 
     * @param value
     *     allowed object is
     *     {@link CcInfoReportType.DiscoveredSubjects }
     *     
     */
    public void setDiscoveredSubjects(CcInfoReportType.DiscoveredSubjects value) {
        this.discoveredSubjects = value;
    }

    /**
     * Gets the value of the discoveredVehicles property.
     * 
     * @return
     *     possible object is
     *     {@link CcInfoReportType.DiscoveredVehicles }
     *     
     */
    public CcInfoReportType.DiscoveredVehicles getDiscoveredVehicles() {
        return discoveredVehicles;
    }

    /**
     * Sets the value of the discoveredVehicles property.
     * 
     * @param value
     *     allowed object is
     *     {@link CcInfoReportType.DiscoveredVehicles }
     *     
     */
    public void setDiscoveredVehicles(CcInfoReportType.DiscoveredVehicles value) {
        this.discoveredVehicles = value;
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
     *         &lt;element name="subject" type="{http://cp.com/rules/client}dataprefill_clue_ADDSubjectType" maxOccurs="100"/>
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
        "subjectList"
    })
    public static class DiscoveredSubjects {

        @XmlElement(name = "subject", required = true)
        protected List<DataprefillClueADDSubjectType> subjectList;

        /**
         * Gets the value of the subjectList property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the subjectList property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getSubjectList().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillClueADDSubjectType }
         * 
         * 
         */
        public List<DataprefillClueADDSubjectType> getSubjectList() {
            if (subjectList == null) {
                subjectList = new ArrayList<DataprefillClueADDSubjectType>();
            }
            return this.subjectList;
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
     *         &lt;element name="vehicle" maxOccurs="25">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="vehicle_detail" type="{http://cp.com/rules/client}resultVehicleType"/>
     *                   &lt;element name="policy_detail" maxOccurs="3" minOccurs="0">
     *                     &lt;complexType>
     *                       &lt;complexContent>
     *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                           &lt;sequence>
     *                             &lt;element name="business_vehicle" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
     *                             &lt;element name="coverage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                             &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                             &lt;element name="deductible" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                           &lt;/sequence>
     *                         &lt;/restriction>
     *                       &lt;/complexContent>
     *                     &lt;/complexType>
     *                   &lt;/element>
     *                 &lt;/sequence>
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
        "vehicle"
    })
    public static class DiscoveredVehicles {

        @XmlElement(required = true)
        protected List<CcInfoReportType.DiscoveredVehicles.Vehicle> vehicle;

        /**
         * Gets the value of the vehicle property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the vehicle property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getVehicle().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link CcInfoReportType.DiscoveredVehicles.Vehicle }
         * 
         * 
         */
        public List<CcInfoReportType.DiscoveredVehicles.Vehicle> getVehicle() {
            if (vehicle == null) {
                vehicle = new ArrayList<CcInfoReportType.DiscoveredVehicles.Vehicle>();
            }
            return this.vehicle;
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
         *         &lt;element name="vehicle_detail" type="{http://cp.com/rules/client}resultVehicleType"/>
         *         &lt;element name="policy_detail" maxOccurs="3" minOccurs="0">
         *           &lt;complexType>
         *             &lt;complexContent>
         *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
         *                 &lt;sequence>
         *                   &lt;element name="business_vehicle" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
         *                   &lt;element name="coverage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                   &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                   &lt;element name="deductible" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *                 &lt;/sequence>
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
            "vehicleDetail",
            "policyDetail"
        })
        public static class Vehicle {

            @XmlElement(name = "vehicle_detail", required = true)
            protected ResultVehicleType vehicleDetail;
            @XmlElement(name = "policy_detail")
            protected List<CcInfoReportType.DiscoveredVehicles.Vehicle.PolicyDetail> policyDetail;

            /**
             * Gets the value of the vehicleDetail property.
             * 
             * @return
             *     possible object is
             *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ResultVehicleType }
             *     
             */
            public ResultVehicleType getVehicleDetail() {
                return vehicleDetail;
            }

            /**
             * Sets the value of the vehicleDetail property.
             * 
             * @param value
             *     allowed object is
             *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.ResultVehicleType }
             *     
             */
            public void setVehicleDetail(ResultVehicleType value) {
                this.vehicleDetail = value;
            }

            /**
             * Gets the value of the policyDetail property.
             * 
             * <p>
             * This accessor method returns a reference to the live list,
             * not a snapshot. Therefore any modification you make to the
             * returned list will be present inside the JAXB object.
             * This is why there is not a <CODE>set</CODE> method for the policyDetail property.
             * 
             * <p>
             * For example, to add a new item, do as follows:
             * <pre>
             *    getPolicyDetail().add(newItem);
             * </pre>
             * 
             * 
             * <p>
             * Objects of the following type(s) are allowed in the list
             * {@link CcInfoReportType.DiscoveredVehicles.Vehicle.PolicyDetail }
             * 
             * 
             */
            public List<CcInfoReportType.DiscoveredVehicles.Vehicle.PolicyDetail> getPolicyDetail() {
                if (policyDetail == null) {
                    policyDetail = new ArrayList<CcInfoReportType.DiscoveredVehicles.Vehicle.PolicyDetail>();
                }
                return this.policyDetail;
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
             *         &lt;element name="business_vehicle" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
             *         &lt;element name="coverage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
             *         &lt;element name="limit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
             *         &lt;element name="deductible" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
                "businessVehicle",
                "coverage",
                "limit",
                "deductible"
            })
            public static class PolicyDetail {

                @XmlElement(name = "business_vehicle")
                protected Boolean businessVehicle;
                protected String coverage;
                protected String limit;
                protected String deductible;

                /**
                 * Gets the value of the businessVehicle property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link Boolean }
                 *     
                 */
                public Boolean isBusinessVehicle() {
                    return businessVehicle;
                }

                /**
                 * Sets the value of the businessVehicle property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link Boolean }
                 *     
                 */
                public void setBusinessVehicle(Boolean value) {
                    this.businessVehicle = value;
                }

                /**
                 * Gets the value of the coverage property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getCoverage() {
                    return coverage;
                }

                /**
                 * Sets the value of the coverage property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setCoverage(String value) {
                    this.coverage = value;
                }

                /**
                 * Gets the value of the limit property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getLimit() {
                    return limit;
                }

                /**
                 * Sets the value of the limit property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setLimit(String value) {
                    this.limit = value;
                }

                /**
                 * Gets the value of the deductible property.
                 * 
                 * @return
                 *     possible object is
                 *     {@link String }
                 *     
                 */
                public String getDeductible() {
                    return deductible;
                }

                /**
                 * Sets the value of the deductible property.
                 * 
                 * @param value
                 *     allowed object is
                 *     {@link String }
                 *     
                 */
                public void setDeductible(String value) {
                    this.deductible = value;
                }

            }

        }

    }

}
