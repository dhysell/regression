//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="order_number" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="state" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="reported_license_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="quoteback" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="pnc_account">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;pattern value="\d{6}([0-9 A-Z]{3})?"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="status">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Clear"/>
 *               &lt;enumeration value="Hit - Activity Found"/>
 *               &lt;enumeration value="No Hit"/>
 *               &lt;enumeration value="Invalid Order"/>
 *               &lt;enumeration value="Pending Information"/>
 *               &lt;enumeration value="Renewal Clear"/>
 *               &lt;enumeration value="Soundex Rejected"/>
 *               &lt;enumeration value="DMV Did Not Respond"/>
 *               &lt;enumeration value="Additional Response - Hit"/>
 *               &lt;enumeration value="Additional Response - No Hit"/>
 *               &lt;enumeration value="Additional Response - Clear"/>
 *               &lt;enumeration value="Unreturned"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="date_processed" type="{http://cp.com/rules/client}USDateType"/>
 *         &lt;element name="source" type="{http://cp.com/rules/client}source_origin_values"/>
 *         &lt;element name="origin" type="{http://cp.com/rules/client}source_origin_values"/>
 *         &lt;element name="mvr_return_count" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="no_hit_credit_indicator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "orderNumber",
    "state",
    "reportedLicenseNumber",
    "quoteback",
    "pncAccount",
    "status",
    "dateProcessed",
    "source",
    "origin",
    "mvrReturnCount",
    "noHitCreditIndicator"
})
@XmlRootElement(name = "admin")
public class Admin {

    @XmlElement(name = "order_number", required = true)
    protected String orderNumber;
    @XmlElement(required = true)
    protected String state;
    @XmlElement(name = "reported_license_number")
    protected String reportedLicenseNumber;
    protected String quoteback;
    @XmlElement(name = "pnc_account", required = true)
    protected String pncAccount;
    @XmlElement(required = true)
    protected Admin.ProcessingStatusEnum status;
    @XmlElement(name = "date_processed", required = true)
    protected String dateProcessed;
    @XmlElement(required = true)
    protected SourceOriginValues source;
    @XmlElement(required = true)
    protected SourceOriginValues origin;
    @XmlElement(name = "mvr_return_count")
    protected int mvrReturnCount;
    @XmlElement(name = "no_hit_credit_indicator")
    protected String noHitCreditIndicator;

    /**
     * Gets the value of the orderNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the value of the orderNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderNumber(String value) {
        this.orderNumber = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
    }

    /**
     * Gets the value of the reportedLicenseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReportedLicenseNumber() {
        return reportedLicenseNumber;
    }

    /**
     * Sets the value of the reportedLicenseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReportedLicenseNumber(String value) {
        this.reportedLicenseNumber = value;
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

    /**
     * Gets the value of the pncAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPncAccount() {
        return pncAccount;
    }

    /**
     * Sets the value of the pncAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPncAccount(String value) {
        this.pncAccount = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Admin.ProcessingStatusEnum }
     *     
     */
    public Admin.ProcessingStatusEnum getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Admin.ProcessingStatusEnum }
     *     
     */
    public void setStatus(Admin.ProcessingStatusEnum value) {
        this.status = value;
    }

    /**
     * Gets the value of the dateProcessed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateProcessed() {
        return dateProcessed;
    }

    /**
     * Sets the value of the dateProcessed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateProcessed(String value) {
        this.dateProcessed = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.SourceOriginValues }
     *     
     */
    public SourceOriginValues getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.SourceOriginValues }
     *     
     */
    public void setSource(SourceOriginValues value) {
        this.source = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.SourceOriginValues }
     *     
     */
    public SourceOriginValues getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.SourceOriginValues }
     *     
     */
    public void setOrigin(SourceOriginValues value) {
        this.origin = value;
    }

    /**
     * Gets the value of the mvrReturnCount property.
     * 
     */
    public int getMvrReturnCount() {
        return mvrReturnCount;
    }

    /**
     * Sets the value of the mvrReturnCount property.
     * 
     */
    public void setMvrReturnCount(int value) {
        this.mvrReturnCount = value;
    }

    /**
     * Gets the value of the noHitCreditIndicator property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNoHitCreditIndicator() {
        return noHitCreditIndicator;
    }

    /**
     * Sets the value of the noHitCreditIndicator property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNoHitCreditIndicator(String value) {
        this.noHitCreditIndicator = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Clear"/>
     *     &lt;enumeration value="Hit - Activity Found"/>
     *     &lt;enumeration value="No Hit"/>
     *     &lt;enumeration value="Invalid Order"/>
     *     &lt;enumeration value="Pending Information"/>
     *     &lt;enumeration value="Renewal Clear"/>
     *     &lt;enumeration value="Soundex Rejected"/>
     *     &lt;enumeration value="DMV Did Not Respond"/>
     *     &lt;enumeration value="Additional Response - Hit"/>
     *     &lt;enumeration value="Additional Response - No Hit"/>
     *     &lt;enumeration value="Additional Response - Clear"/>
     *     &lt;enumeration value="Unreturned"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum ProcessingStatusEnum {

        @XmlEnumValue("Clear")
        CLEAR("Clear"),
        @XmlEnumValue("Hit - Activity Found")
        HIT_ACTIVITY_FOUND("Hit - Activity Found"),
        @XmlEnumValue("No Hit")
        NO_HIT("No Hit"),
        @XmlEnumValue("Invalid Order")
        INVALID_ORDER("Invalid Order"),
        @XmlEnumValue("Pending Information")
        PENDING_INFORMATION("Pending Information"),
        @XmlEnumValue("Renewal Clear")
        RENEWAL_CLEAR("Renewal Clear"),
        @XmlEnumValue("Soundex Rejected")
        SOUNDEX_REJECTED("Soundex Rejected"),
        @XmlEnumValue("DMV Did Not Respond")
        DMV_DID_NOT_RESPOND("DMV Did Not Respond"),
        @XmlEnumValue("Additional Response - Hit")
        ADDITIONAL_RESPONSE_HIT("Additional Response - Hit"),
        @XmlEnumValue("Additional Response - No Hit")
        ADDITIONAL_RESPONSE_NO_HIT("Additional Response - No Hit"),
        @XmlEnumValue("Additional Response - Clear")
        ADDITIONAL_RESPONSE_CLEAR("Additional Response - Clear"),
        @XmlEnumValue("Unreturned")
        UNRETURNED("Unreturned");
        private final String value;

        ProcessingStatusEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static Admin.ProcessingStatusEnum fromValue(String v) {
            for (Admin.ProcessingStatusEnum c: Admin.ProcessingStatusEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
