//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for propertySalesInfoType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="propertySalesInfoType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="recordingDate" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="salesDate" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *         &lt;element name="documentNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="salesAmount" type="{http://cp.com/rules/client}amountType" minOccurs="0"/>
 *         &lt;element name="salesType" type="{http://cp.com/rules/client}salesTypeCode" minOccurs="0"/>
 *         &lt;element name="saleFullOrPart" type="{http://cp.com/rules/client}saleFullOrPartCodeType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="group_sequence_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="classification" type="{http://cp.com/rules/client}salesInfoClassificationCode" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertySalesInfoType", propOrder = {
    "recordingDate",
    "salesDate",
    "documentNumber",
    "salesAmount",
    "salesType",
    "saleFullOrPart"
})
public class PropertySalesInfoType {

    protected String recordingDate;
    protected String salesDate;
    protected String documentNumber;
    protected String salesAmount;
    @XmlSchemaType(name = "string")
    protected SalesTypeCodeEnum salesType;
    @XmlSchemaType(name = "string")
    protected SaleFullOrPartCodeTypeEnum saleFullOrPart;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "address")
    protected String address;
    @XmlAttribute(name = "group_sequence_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger groupSequenceNumber;
    @XmlAttribute(name = "classification")
    protected SalesInfoCodeEnum classification;

    /**
     * Gets the value of the recordingDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecordingDate() {
        return recordingDate;
    }

    /**
     * Sets the value of the recordingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecordingDate(String value) {
        this.recordingDate = value;
    }

    /**
     * Gets the value of the salesDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesDate() {
        return salesDate;
    }

    /**
     * Sets the value of the salesDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesDate(String value) {
        this.salesDate = value;
    }

    /**
     * Gets the value of the documentNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentNumber() {
        return documentNumber;
    }

    /**
     * Sets the value of the documentNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentNumber(String value) {
        this.documentNumber = value;
    }

    /**
     * Gets the value of the salesAmount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSalesAmount() {
        return salesAmount;
    }

    /**
     * Sets the value of the salesAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSalesAmount(String value) {
        this.salesAmount = value;
    }

    /**
     * Gets the value of the salesType property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.SalesTypeCodeEnum }
     *     
     */
    public SalesTypeCodeEnum getSalesType() {
        return salesType;
    }

    /**
     * Sets the value of the salesType property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.SalesTypeCodeEnum }
     *     
     */
    public void setSalesType(SalesTypeCodeEnum value) {
        this.salesType = value;
    }

    /**
     * Gets the value of the saleFullOrPart property.
     * 
     * @return
     *     possible object is
     *     {@link SaleFullOrPartCodeTypeEnum }
     *     
     */
    public SaleFullOrPartCodeTypeEnum getSaleFullOrPart() {
        return saleFullOrPart;
    }

    /**
     * Sets the value of the saleFullOrPart property.
     * 
     * @param value
     *     allowed object is
     *     {@link SaleFullOrPartCodeTypeEnum }
     *     
     */
    public void setSaleFullOrPart(SaleFullOrPartCodeTypeEnum value) {
        this.saleFullOrPart = value;
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
     * Gets the value of the address property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the value of the address property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddress(String value) {
        this.address = value;
    }

    /**
     * Gets the value of the groupSequenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGroupSequenceNumber() {
        return groupSequenceNumber;
    }

    /**
     * Sets the value of the groupSequenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGroupSequenceNumber(BigInteger value) {
        this.groupSequenceNumber = value;
    }

    /**
     * Gets the value of the classification property.
     * 
     * @return
     *     possible object is
     *     {@link SalesInfoCodeEnum }
     *     
     */
    public SalesInfoCodeEnum getClassification() {
        return classification;
    }

    /**
     * Sets the value of the classification property.
     * 
     * @param value
     *     allowed object is
     *     {@link SalesInfoCodeEnum }
     *     
     */
    public void setClassification(SalesInfoCodeEnum value) {
        this.classification = value;
    }

}
