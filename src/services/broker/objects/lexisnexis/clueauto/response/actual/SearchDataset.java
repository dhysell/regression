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
 *         &lt;element name="group_address" type="{http://cp.com/rules/client}clueAddressType" maxOccurs="2" minOccurs="0"/>
 *         &lt;element name="group_prior_policy" type="{http://cp.com/rules/client}cluePolicyType" minOccurs="0"/>
 *         &lt;element name="subjects" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="subject" type="{http://cp.com/rules/client}clueSubjectType" maxOccurs="50"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="vehicle" type="{http://cp.com/rules/client}clueVehicleType" maxOccurs="50" minOccurs="0"/>
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
    "groupAddress",
    "groupPriorPolicy",
    "subjects",
    "vehicle"
})
@XmlRootElement(name = "search_dataset")
public class SearchDataset {

    @XmlElement(name = "group_address")
    protected List<ClueAddressType> groupAddress;
    @XmlElement(name = "group_prior_policy")
    protected CluePolicyType groupPriorPolicy;
    protected SearchDataset.Subjects subjects;
    protected List<ClueVehicleType> vehicle;

    /**
     * Gets the value of the groupAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groupAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroupAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.clueauto.response.actual.ClueAddressType }
     * 
     * 
     */
    public List<ClueAddressType> getGroupAddress() {
        if (groupAddress == null) {
            groupAddress = new ArrayList<ClueAddressType>();
        }
        return this.groupAddress;
    }

    /**
     * Gets the value of the groupPriorPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link CluePolicyType }
     *     
     */
    public CluePolicyType getGroupPriorPolicy() {
        return groupPriorPolicy;
    }

    /**
     * Sets the value of the groupPriorPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link CluePolicyType }
     *     
     */
    public void setGroupPriorPolicy(CluePolicyType value) {
        this.groupPriorPolicy = value;
    }

    /**
     * Gets the value of the subjects property.
     * 
     * @return
     *     possible object is
     *     {@link SearchDataset.Subjects }
     *     
     */
    public SearchDataset.Subjects getSubjects() {
        return subjects;
    }

    /**
     * Sets the value of the subjects property.
     * 
     * @param value
     *     allowed object is
     *     {@link SearchDataset.Subjects }
     *     
     */
    public void setSubjects(SearchDataset.Subjects value) {
        this.subjects = value;
    }

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
     * {@link broker.objects.lexisnexis.clueauto.response.actual.ClueVehicleType }
     * 
     * 
     */
    public List<ClueVehicleType> getVehicle() {
        if (vehicle == null) {
            vehicle = new ArrayList<ClueVehicleType>();
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
     *         &lt;element name="subject" type="{http://cp.com/rules/client}clueSubjectType" maxOccurs="50"/>
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
    public static class Subjects {

        @XmlElement(required = true)
        protected List<ClueSubjectType> subject;

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
         * {@link broker.objects.lexisnexis.clueauto.response.actual.ClueSubjectType }
         * 
         * 
         */
        public List<ClueSubjectType> getSubject() {
            if (subject == null) {
                subject = new ArrayList<ClueSubjectType>();
            }
            return this.subject;
        }

    }

}
