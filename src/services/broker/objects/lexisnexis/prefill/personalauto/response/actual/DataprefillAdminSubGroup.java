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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataprefillAdminSubGroup complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataprefillAdminSubGroup">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="product_reference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="report_usage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="service_type" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="receipt_date" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="date_request_ordered" type="{http://cp.com/rules/client}USDateType"/>
 *         &lt;element name="time_request_processed" type="{http://cp.com/rules/client}editsTimeType" minOccurs="0"/>
 *         &lt;element name="date_request_completed" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataprefillAdminSubGroup", propOrder = {
    "productReference",
    "reportUsage",
    "serviceType",
    "receiptDate",
    "dateRequestOrdered",
    "timeRequestProcessed",
    "dateRequestCompleted"
})
public class DataprefillAdminSubGroup {

    @XmlElement(name = "product_reference")
    protected String productReference;
    @XmlElement(name = "report_usage")
    protected String reportUsage;
    @XmlElement(name = "service_type", required = true)
    protected String serviceType;
    @XmlElement(name = "receipt_date")
    protected String receiptDate;
    @XmlElement(name = "date_request_ordered", required = true)
    protected String dateRequestOrdered;
    @XmlElement(name = "time_request_processed")
    protected String timeRequestProcessed;
    @XmlElement(name = "date_request_completed")
    protected String dateRequestCompleted;

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
     * Gets the value of the reportUsage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportUsage() {
        return reportUsage;
    }

    /**
     * Sets the value of the reportUsage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportUsage(String value) {
        this.reportUsage = value;
    }

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
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

}