//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element ref="{http://cp.com/rules/client}alerts_scoring" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}search_dataset" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}vendor_dataset" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}summary" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}employment_history" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}public_records" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}collections" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}trade_account_activity" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}consumer_statements" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}inquiry_history" minOccurs="0"/>
 *         &lt;element ref="{http://cp.com/rules/client}additional_information" minOccurs="0"/>
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
    "alertsScoring",
    "searchDataset",
    "vendorDataset",
    "summary",
    "employmentHistory",
    "publicRecords",
    "collections",
    "tradeAccountActivity",
    "consumerStatements",
    "inquiryHistory",
    "additionalInformation"
})
@XmlRootElement(name = "report")
public class Report {

    @XmlElement(name = "alerts_scoring")
    protected AlertsScoring alertsScoring;
    @XmlElement(name = "search_dataset")
    protected SearchDataset searchDataset;
    @XmlElement(name = "vendor_dataset")
    protected VendorDataset vendorDataset;
    protected Summary summary;
    @XmlElement(name = "employment_history")
    protected EmploymentHistory employmentHistory;
    @XmlElement(name = "public_records")
    protected PublicRecords publicRecords;
    protected Collections collections;
    @XmlElement(name = "trade_account_activity")
    protected TradeAccountActivity tradeAccountActivity;
    @XmlElement(name = "consumer_statements")
    protected ConsumerStatements consumerStatements;
    @XmlElement(name = "inquiry_history")
    protected InquiryHistory inquiryHistory;
    @XmlElement(name = "additional_information")
    protected MessageListType additionalInformation;

    /**
     * Gets the value of the alertsScoring property.
     * 
     * @return
     *     possible object is
     *     {@link AlertsScoring }
     *     
     */
    public AlertsScoring getAlertsScoring() {
        return alertsScoring;
    }

    /**
     * Sets the value of the alertsScoring property.
     * 
     * @param value
     *     allowed object is
     *     {@link AlertsScoring }
     *     
     */
    public void setAlertsScoring(AlertsScoring value) {
        this.alertsScoring = value;
    }

    /**
     * Gets the value of the searchDataset property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.SearchDataset }
     *     
     */
    public SearchDataset getSearchDataset() {
        return searchDataset;
    }

    /**
     * Sets the value of the searchDataset property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.SearchDataset }
     *     
     */
    public void setSearchDataset(SearchDataset value) {
        this.searchDataset = value;
    }

    /**
     * Gets the value of the vendorDataset property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.VendorDataset }
     *     
     */
    public VendorDataset getVendorDataset() {
        return vendorDataset;
    }

    /**
     * Sets the value of the vendorDataset property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.VendorDataset }
     *     
     */
    public void setVendorDataset(VendorDataset value) {
        this.vendorDataset = value;
    }

    /**
     * Gets the value of the summary property.
     * 
     * @return
     *     possible object is
     *     {@link Summary }
     *     
     */
    public Summary getSummary() {
        return summary;
    }

    /**
     * Sets the value of the summary property.
     * 
     * @param value
     *     allowed object is
     *     {@link Summary }
     *     
     */
    public void setSummary(Summary value) {
        this.summary = value;
    }

    /**
     * Gets the value of the employmentHistory property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.EmploymentHistory }
     *     
     */
    public EmploymentHistory getEmploymentHistory() {
        return employmentHistory;
    }

    /**
     * Sets the value of the employmentHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.EmploymentHistory }
     *     
     */
    public void setEmploymentHistory(EmploymentHistory value) {
        this.employmentHistory = value;
    }

    /**
     * Gets the value of the publicRecords property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.PublicRecords }
     *     
     */
    public PublicRecords getPublicRecords() {
        return publicRecords;
    }

    /**
     * Sets the value of the publicRecords property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.PublicRecords }
     *     
     */
    public void setPublicRecords(PublicRecords value) {
        this.publicRecords = value;
    }

    /**
     * Gets the value of the collections property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.Collections }
     *     
     */
    public Collections getCollections() {
        return collections;
    }

    /**
     * Sets the value of the collections property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.Collections }
     *     
     */
    public void setCollections(Collections value) {
        this.collections = value;
    }

    /**
     * Gets the value of the tradeAccountActivity property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.TradeAccountActivity }
     *     
     */
    public TradeAccountActivity getTradeAccountActivity() {
        return tradeAccountActivity;
    }

    /**
     * Sets the value of the tradeAccountActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.TradeAccountActivity }
     *     
     */
    public void setTradeAccountActivity(TradeAccountActivity value) {
        this.tradeAccountActivity = value;
    }

    /**
     * Gets the value of the consumerStatements property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.ConsumerStatements }
     *     
     */
    public ConsumerStatements getConsumerStatements() {
        return consumerStatements;
    }

    /**
     * Sets the value of the consumerStatements property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.ConsumerStatements }
     *     
     */
    public void setConsumerStatements(ConsumerStatements value) {
        this.consumerStatements = value;
    }

    /**
     * Gets the value of the inquiryHistory property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.InquiryHistory }
     *     
     */
    public InquiryHistory getInquiryHistory() {
        return inquiryHistory;
    }

    /**
     * Sets the value of the inquiryHistory property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.InquiryHistory }
     *     
     */
    public void setInquiryHistory(InquiryHistory value) {
        this.inquiryHistory = value;
    }

    /**
     * Gets the value of the additionalInformation property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.cbr.response.actual.MessageListType }
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
     *     {@link broker.objects.lexisnexis.cbr.response.actual.MessageListType }
     *     
     */
    public void setAdditionalInformation(MessageListType value) {
        this.additionalInformation = value;
    }

}