//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for commonAdminType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="commonAdminType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="quoteback" type="{http://cp.com/rules/client}quotebackType" maxOccurs="unbounded"/>
 *         &lt;element name="pnc_account">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="special_billing_id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="product_reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="report_code" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="report_description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="receipt_date" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="date_request_ordered" type="{http://cp.com/rules/client}USDateType"/>
 *         &lt;element name="time_request_processed" type="{http://cp.com/rules/client}editsTimeType" minOccurs="0"/>
 *         &lt;element name="date_request_completed" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="customer_organization_code" maxOccurs="4" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="level">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *                       &lt;maxInclusive value="4"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *               &lt;/extension>
 *             &lt;/simpleContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="attachment_status" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="10" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}parameter" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "commonAdminType", propOrder = {
    "quoteback",
    "pncAccount",
    "specialBillingId",
    "productReference",
    "reportCode",
    "reportDescription",
    "receiptDate",
    "dateRequestOrdered",
    "timeRequestProcessed",
    "dateRequestCompleted",
    "customerOrganizationCode",
    "attachmentStatus",
    "parameterList"
})
public class CommonAdminType {

    @XmlElement(required = true)
    protected List<QuotebackType> quoteback;
    @XmlElement(name = "pnc_account", required = true)
    protected CommonAdminType.PncAccount pncAccount;
    @XmlElement(name = "special_billing_id")
    protected String specialBillingId;
    @XmlElement(name = "product_reference")
    protected String productReference;
    @XmlElement(name = "report_code", required = true)
    protected String reportCode;
    @XmlElement(name = "report_description")
    protected String reportDescription;
    @XmlElement(name = "receipt_date")
    protected String receiptDate;
    @XmlElement(name = "date_request_ordered", required = true)
    protected String dateRequestOrdered;
    @XmlElement(name = "time_request_processed")
    protected String timeRequestProcessed;
    @XmlElement(name = "date_request_completed")
    protected String dateRequestCompleted;
    @XmlElement(name = "customer_organization_code")
    protected List<CommonAdminType.CustomerOrganizationCode> customerOrganizationCode;
    @XmlElement(name = "attachment_status")
    protected List<String> attachmentStatus;
    @XmlElement(name = "parameter")
    protected ParameterType parameterList;

    /**
     * Gets the value of the quoteback property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the quoteback property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuoteback().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link QuotebackType }
     * 
     * 
     */
    public List<QuotebackType> getQuoteback() {
        if (quoteback == null) {
            quoteback = new ArrayList<QuotebackType>();
        }
        return this.quoteback;
    }

    /**
     * Gets the value of the pncAccount property.
     * 
     * @return
     *     possible object is
     *     {@link CommonAdminType.PncAccount }
     *     
     */
    public CommonAdminType.PncAccount getPncAccount() {
        return pncAccount;
    }

    /**
     * Sets the value of the pncAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommonAdminType.PncAccount }
     *     
     */
    public void setPncAccount(CommonAdminType.PncAccount value) {
        this.pncAccount = value;
    }

    /**
     * Gets the value of the specialBillingId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpecialBillingId() {
        return specialBillingId;
    }

    /**
     * Sets the value of the specialBillingId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpecialBillingId(String value) {
        this.specialBillingId = value;
    }

    /**
     * Gets the value of the productReference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProductReference() {
        return productReference;
    }

    /**
     * Sets the value of the productReference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductReference(String value) {
        this.productReference = value;
    }

    /**
     * Gets the value of the reportCode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportCode() {
        return reportCode;
    }

    /**
     * Sets the value of the reportCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportCode(String value) {
        this.reportCode = value;
    }

    /**
     * Gets the value of the reportDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportDescription() {
        return reportDescription;
    }

    /**
     * Sets the value of the reportDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportDescription(String value) {
        this.reportDescription = value;
    }

    /**
     * Gets the value of the receiptDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiptDate() {
        return receiptDate;
    }

    /**
     * Sets the value of the receiptDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiptDate(String value) {
        this.receiptDate = value;
    }

    /**
     * Gets the value of the dateRequestOrdered property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateRequestOrdered() {
        return dateRequestOrdered;
    }

    /**
     * Sets the value of the dateRequestOrdered property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRequestOrdered(String value) {
        this.dateRequestOrdered = value;
    }

    /**
     * Gets the value of the timeRequestProcessed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeRequestProcessed() {
        return timeRequestProcessed;
    }

    /**
     * Sets the value of the timeRequestProcessed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeRequestProcessed(String value) {
        this.timeRequestProcessed = value;
    }

    /**
     * Gets the value of the dateRequestCompleted property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateRequestCompleted() {
        return dateRequestCompleted;
    }

    /**
     * Sets the value of the dateRequestCompleted property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateRequestCompleted(String value) {
        this.dateRequestCompleted = value;
    }

    /**
     * Gets the value of the customerOrganizationCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerOrganizationCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerOrganizationCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CommonAdminType.CustomerOrganizationCode }
     * 
     * 
     */
    public List<CommonAdminType.CustomerOrganizationCode> getCustomerOrganizationCode() {
        if (customerOrganizationCode == null) {
            customerOrganizationCode = new ArrayList<CommonAdminType.CustomerOrganizationCode>();
        }
        return this.customerOrganizationCode;
    }

    /**
     * Gets the value of the attachmentStatus property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attachmentStatus property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttachmentStatus().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getAttachmentStatus() {
        if (attachmentStatus == null) {
            attachmentStatus = new ArrayList<String>();
        }
        return this.attachmentStatus;
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterType }
     *     
     */
    public ParameterType getParameterList() {
        return parameterList;
    }

    /**
     * Sets the value of the parameterList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterType }
     *     
     */
    public void setParameterList(ParameterType value) {
        this.parameterList = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="level">
     *         &lt;simpleType>
     *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
     *             &lt;maxInclusive value="4"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class CustomerOrganizationCode {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "level")
        protected Integer level;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the level property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getLevel() {
            return level;
        }

        /**
         * Sets the value of the level property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setLevel(Integer value) {
            this.level = value;
        }

    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class PncAccount {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "name")
        protected String name;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
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

    }

}
