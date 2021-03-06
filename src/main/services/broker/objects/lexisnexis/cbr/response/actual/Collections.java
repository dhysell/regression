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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="collection" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="reporting_member_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="ecoa_code" type="{http://cp.com/rules/client}ecoa_codes"/>
 *                   &lt;element name="date_vendor_reported" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *                   &lt;element name="date_reported" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *                   &lt;element name="date_assigned" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *                   &lt;element name="date_last_activity" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *                   &lt;element name="date_balance" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *                   &lt;element name="date_last_checked" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
 *                   &lt;element name="status">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="Unpaid"/>
 *                         &lt;enumeration value="Financial counselor"/>
 *                         &lt;enumeration value="Adjustment"/>
 *                         &lt;enumeration value="Wage earner"/>
 *                         &lt;enumeration value="New listing"/>
 *                         &lt;enumeration value="Paid"/>
 *                         &lt;enumeration value="Account disputed"/>
 *                         &lt;enumeration value="Payment"/>
 *                         &lt;enumeration value="Unknown"/>
 *                         &lt;enumeration value="Checked"/>
 *                         &lt;enumeration value="In bankruptcy"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                   &lt;element name="grantor" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;complexContent>
 *                         &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                           &lt;sequence>
 *                             &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                             &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                           &lt;/sequence>
 *                         &lt;/restriction>
 *                       &lt;/complexContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                   &lt;element name="account_serial_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="orginal_amount" type="{http://cp.com/rules/client}whole_dollar" minOccurs="0"/>
 *                   &lt;element name="balance_amount" type="{http://cp.com/rules/client}whole_dollar" minOccurs="0"/>
 *                   &lt;element name="narratives" type="{http://cp.com/rules/client}narratives" minOccurs="0"/>
 *                   &lt;element name="messages" type="{http://cp.com/rules/client}messageListType" minOccurs="0"/>
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
    "collection"
})
@XmlRootElement(name = "collections")
public class Collections {

    protected List<Collections.Collection> collection;

    /**
     * Gets the value of the collection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCollection().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Collections.Collection }
     * 
     * 
     */
    public List<Collections.Collection> getCollection() {
        if (collection == null) {
            collection = new ArrayList<Collections.Collection>();
        }
        return this.collection;
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
     *         &lt;element name="reporting_member_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="ecoa_code" type="{http://cp.com/rules/client}ecoa_codes"/>
     *         &lt;element name="date_vendor_reported" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
     *         &lt;element name="date_reported" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
     *         &lt;element name="date_assigned" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
     *         &lt;element name="date_last_activity" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
     *         &lt;element name="date_balance" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
     *         &lt;element name="date_last_checked" type="{http://cp.com/rules/client}USDateType" minOccurs="0"/>
     *         &lt;element name="status">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="Unpaid"/>
     *               &lt;enumeration value="Financial counselor"/>
     *               &lt;enumeration value="Adjustment"/>
     *               &lt;enumeration value="Wage earner"/>
     *               &lt;enumeration value="New listing"/>
     *               &lt;enumeration value="Paid"/>
     *               &lt;enumeration value="Account disputed"/>
     *               &lt;enumeration value="Payment"/>
     *               &lt;enumeration value="Unknown"/>
     *               &lt;enumeration value="Checked"/>
     *               &lt;enumeration value="In bankruptcy"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *         &lt;element name="grantor" minOccurs="0">
     *           &lt;complexType>
     *             &lt;complexContent>
     *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *                 &lt;sequence>
     *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *                 &lt;/sequence>
     *               &lt;/restriction>
     *             &lt;/complexContent>
     *           &lt;/complexType>
     *         &lt;/element>
     *         &lt;element name="account_serial_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *         &lt;element name="orginal_amount" type="{http://cp.com/rules/client}whole_dollar" minOccurs="0"/>
     *         &lt;element name="balance_amount" type="{http://cp.com/rules/client}whole_dollar" minOccurs="0"/>
     *         &lt;element name="narratives" type="{http://cp.com/rules/client}narratives" minOccurs="0"/>
     *         &lt;element name="messages" type="{http://cp.com/rules/client}messageListType" minOccurs="0"/>
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
        "reportingMemberNumber",
        "ecoaCode",
        "dateVendorReported",
        "dateReported",
        "dateAssigned",
        "dateLastActivity",
        "dateBalance",
        "dateLastChecked",
        "status",
        "grantor",
        "accountSerialNumber",
        "orginalAmount",
        "balanceAmount",
        "narratives",
        "messages"
    })
    public static class Collection {

        @XmlElement(name = "reporting_member_number")
        protected String reportingMemberNumber;
        @XmlElement(name = "ecoa_code", required = true)
        @XmlSchemaType(name = "string")
        protected CollectionsEcoaCodeEnum ecoaCode;
        @XmlElement(name = "date_vendor_reported")
        protected String dateVendorReported;
        @XmlElement(name = "date_reported")
        protected String dateReported;
        @XmlElement(name = "date_assigned")
        protected String dateAssigned;
        @XmlElement(name = "date_last_activity")
        protected String dateLastActivity;
        @XmlElement(name = "date_balance")
        protected String dateBalance;
        @XmlElement(name = "date_last_checked")
        protected String dateLastChecked;
        @XmlElement(required = true)
        protected Collections.Collection.CollectionsStatusByEnum status;
        protected Collections.Collection.Grantor grantor;
        @XmlElement(name = "account_serial_number")
        protected String accountSerialNumber;
        @XmlElement(name = "orginal_amount")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected Integer orginalAmount;
        @XmlElement(name = "balance_amount")
        @XmlSchemaType(name = "nonNegativeInteger")
        protected Integer balanceAmount;
        protected Narratives narratives;
        protected MessageListType messages;

        /**
         * Gets the value of the reportingMemberNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getReportingMemberNumber() {
            return reportingMemberNumber;
        }

        /**
         * Sets the value of the reportingMemberNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setReportingMemberNumber(String value) {
            this.reportingMemberNumber = value;
        }

        /**
         * Gets the value of the ecoaCode property.
         * 
         * @return
         *     possible object is
         *     {@link CollectionsEcoaCodeEnum }
         *     
         */
        public CollectionsEcoaCodeEnum getEcoaCode() {
            return ecoaCode;
        }

        /**
         * Sets the value of the ecoaCode property.
         * 
         * @param value
         *     allowed object is
         *     {@link CollectionsEcoaCodeEnum }
         *     
         */
        public void setEcoaCode(CollectionsEcoaCodeEnum value) {
            this.ecoaCode = value;
        }

        /**
         * Gets the value of the dateVendorReported property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateVendorReported() {
            return dateVendorReported;
        }

        /**
         * Sets the value of the dateVendorReported property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateVendorReported(String value) {
            this.dateVendorReported = value;
        }

        /**
         * Gets the value of the dateReported property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateReported() {
            return dateReported;
        }

        /**
         * Sets the value of the dateReported property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateReported(String value) {
            this.dateReported = value;
        }

        /**
         * Gets the value of the dateAssigned property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateAssigned() {
            return dateAssigned;
        }

        /**
         * Sets the value of the dateAssigned property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateAssigned(String value) {
            this.dateAssigned = value;
        }

        /**
         * Gets the value of the dateLastActivity property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateLastActivity() {
            return dateLastActivity;
        }

        /**
         * Sets the value of the dateLastActivity property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateLastActivity(String value) {
            this.dateLastActivity = value;
        }

        /**
         * Gets the value of the dateBalance property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateBalance() {
            return dateBalance;
        }

        /**
         * Sets the value of the dateBalance property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateBalance(String value) {
            this.dateBalance = value;
        }

        /**
         * Gets the value of the dateLastChecked property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDateLastChecked() {
            return dateLastChecked;
        }

        /**
         * Sets the value of the dateLastChecked property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDateLastChecked(String value) {
            this.dateLastChecked = value;
        }

        /**
         * Gets the value of the status property.
         * 
         * @return
         *     possible object is
         *     {@link Collections.Collection.CollectionsStatusByEnum }
         *     
         */
        public Collections.Collection.CollectionsStatusByEnum getStatus() {
            return status;
        }

        /**
         * Sets the value of the status property.
         * 
         * @param value
         *     allowed object is
         *     {@link Collections.Collection.CollectionsStatusByEnum }
         *     
         */
        public void setStatus(Collections.Collection.CollectionsStatusByEnum value) {
            this.status = value;
        }

        /**
         * Gets the value of the grantor property.
         * 
         * @return
         *     possible object is
         *     {@link Collections.Collection.Grantor }
         *     
         */
        public Collections.Collection.Grantor getGrantor() {
            return grantor;
        }

        /**
         * Sets the value of the grantor property.
         * 
         * @param value
         *     allowed object is
         *     {@link Collections.Collection.Grantor }
         *     
         */
        public void setGrantor(Collections.Collection.Grantor value) {
            this.grantor = value;
        }

        /**
         * Gets the value of the accountSerialNumber property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAccountSerialNumber() {
            return accountSerialNumber;
        }

        /**
         * Sets the value of the accountSerialNumber property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAccountSerialNumber(String value) {
            this.accountSerialNumber = value;
        }

        /**
         * Gets the value of the orginalAmount property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getOrginalAmount() {
            return orginalAmount;
        }

        /**
         * Sets the value of the orginalAmount property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setOrginalAmount(Integer value) {
            this.orginalAmount = value;
        }

        /**
         * Gets the value of the balanceAmount property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getBalanceAmount() {
            return balanceAmount;
        }

        /**
         * Sets the value of the balanceAmount property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setBalanceAmount(Integer value) {
            this.balanceAmount = value;
        }

        /**
         * Gets the value of the narratives property.
         * 
         * @return
         *     possible object is
         *     {@link broker.objects.lexisnexis.cbr.response.actual.Narratives }
         *     
         */
        public Narratives getNarratives() {
            return narratives;
        }

        /**
         * Sets the value of the narratives property.
         * 
         * @param value
         *     allowed object is
         *     {@link broker.objects.lexisnexis.cbr.response.actual.Narratives }
         *     
         */
        public void setNarratives(Narratives value) {
            this.narratives = value;
        }

        /**
         * Gets the value of the messages property.
         * 
         * @return
         *     possible object is
         *     {@link MessageListType }
         *     
         */
        public MessageListType getMessages() {
            return messages;
        }

        /**
         * Sets the value of the messages property.
         * 
         * @param value
         *     allowed object is
         *     {@link MessageListType }
         *     
         */
        public void setMessages(MessageListType value) {
            this.messages = value;
        }


        /**
         * <p>Java class for null.
         * 
         * <p>The following schema fragment specifies the expected content contained within this class.
         * <p>
         * <pre>
         * &lt;simpleType>
         *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
         *     &lt;enumeration value="Unpaid"/>
         *     &lt;enumeration value="Financial counselor"/>
         *     &lt;enumeration value="Adjustment"/>
         *     &lt;enumeration value="Wage earner"/>
         *     &lt;enumeration value="New listing"/>
         *     &lt;enumeration value="Paid"/>
         *     &lt;enumeration value="Account disputed"/>
         *     &lt;enumeration value="Payment"/>
         *     &lt;enumeration value="Unknown"/>
         *     &lt;enumeration value="Checked"/>
         *     &lt;enumeration value="In bankruptcy"/>
         *   &lt;/restriction>
         * &lt;/simpleType>
         * </pre>
         * 
         */
        @XmlType(name = "")
        @XmlEnum
        public enum CollectionsStatusByEnum {

            @XmlEnumValue("Unpaid")
            UNPAID("Unpaid"),
            @XmlEnumValue("Financial counselor")
            FINANCIAL_COUNSELOR("Financial counselor"),
            @XmlEnumValue("Adjustment")
            ADJUSTMENT("Adjustment"),
            @XmlEnumValue("Wage earner")
            WAGE_EARNER("Wage earner"),
            @XmlEnumValue("New listing")
            NEW_LISTING("New listing"),
            @XmlEnumValue("Paid")
            PAID("Paid"),
            @XmlEnumValue("Account disputed")
            ACCOUNT_DISPUTED("Account disputed"),
            @XmlEnumValue("Payment")
            PAYMENT("Payment"),
            @XmlEnumValue("Unknown")
            UNKNOWN("Unknown"),
            @XmlEnumValue("Checked")
            CHECKED("Checked"),
            @XmlEnumValue("In bankruptcy")
            IN_BANKRUPTCY("In bankruptcy");
            private final String value;

            CollectionsStatusByEnum(String v) {
                value = v;
            }

            public String value() {
                return value;
            }

            public static Collections.Collection.CollectionsStatusByEnum fromValue(String v) {
                for (Collections.Collection.CollectionsStatusByEnum c: Collections.Collection.CollectionsStatusByEnum.values()) {
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
         *       &lt;sequence>
         *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
         *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
            "name",
            "description"
        })
        public static class Grantor {

            protected String name;
            protected String description;

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
             * Gets the value of the description property.
             * 
             * @return
             *     possible object is
             *     {@link String }
             *     
             */
            public String getDescription() {
                return description;
            }

            /**
             * Sets the value of the description property.
             * 
             * @param value
             *     allowed object is
             *     {@link String }
             *     
             */
            public void setDescription(String value) {
                this.description = value;
            }

        }

    }

}
