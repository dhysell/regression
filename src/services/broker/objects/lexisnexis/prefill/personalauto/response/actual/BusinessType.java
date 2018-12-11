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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for businessType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="businessType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://cp.com/rules/client}bizNameType" maxOccurs="unbounded"/>
 *         &lt;element name="address" type="{http://cp.com/rules/client}businessAddressType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="stakeholder" type="{http://cp.com/rules/client}stakeHolderType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="telephone" type="{http://cp.com/rules/client}telephoneType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="fein" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element ref="{http://cp.com/rules/client}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "businessType", propOrder = {
    "nameList",
    "addressList",
    "stakeHolderList",
    "telephoneList",
    "fein",
    "parameterList"
})
public class BusinessType {

    @XmlElement(name = "name", required = true)
    protected List<BizNameType> nameList;
    @XmlElement(name = "address")
    protected List<BusinessAddressType> addressList;
    @XmlElement(name = "stakeholder")
    protected List<StakeHolderType> stakeHolderList;
    @XmlElement(name = "telephone")
    protected List<TelephoneType> telephoneList;
    protected String fein;
    @XmlElement(name = "parameter")
    protected List<ParameterType> parameterList;
    @XmlAttribute(name = "id", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the nameList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nameList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNameList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BizNameType }
     * 
     * 
     */
    public List<BizNameType> getNameList() {
        if (nameList == null) {
            nameList = new ArrayList<BizNameType>();
        }
        return this.nameList;
    }

    /**
     * Gets the value of the addressList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the addressList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAddressList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BusinessAddressType }
     * 
     * 
     */
    public List<BusinessAddressType> getAddressList() {
        if (addressList == null) {
            addressList = new ArrayList<BusinessAddressType>();
        }
        return this.addressList;
    }

    /**
     * Gets the value of the stakeHolderList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the stakeHolderList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStakeHolderList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.StakeHolderType }
     * 
     * 
     */
    public List<StakeHolderType> getStakeHolderList() {
        if (stakeHolderList == null) {
            stakeHolderList = new ArrayList<StakeHolderType>();
        }
        return this.stakeHolderList;
    }

    /**
     * Gets the value of the telephoneList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the telephoneList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTelephoneList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.TelephoneType }
     * 
     * 
     */
    public List<TelephoneType> getTelephoneList() {
        if (telephoneList == null) {
            telephoneList = new ArrayList<TelephoneType>();
        }
        return this.telephoneList;
    }

    /**
     * Gets the value of the fein property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFein() {
        return fein;
    }

    /**
     * Sets the value of the fein property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFein(String value) {
        this.fein = value;
    }

    /**
     * Gets the value of the parameterList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ParameterType }
     * 
     * 
     */
    public List<ParameterType> getParameterList() {
        if (parameterList == null) {
            parameterList = new ArrayList<ParameterType>();
        }
        return this.parameterList;
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

}
