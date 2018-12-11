//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for attachd_report_propertyCharacteristicsListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="attachd_report_propertyCharacteristicsListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="characteristicset" type="{http://cp.com/rules/client}realPropCharacteristicSetType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="address" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "attachd_report_propertyCharacteristicsListType", propOrder = {
    "realPropCharacteristicSetList"
})
public class AttachdReportPropertyCharacteristicsListType {

    @XmlElement(name = "characteristicset", required = true)
    protected List<RealPropCharacteristicSetType> realPropCharacteristicSetList;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "address")
    protected String address;

    /**
     * Gets the value of the realPropCharacteristicSetList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realPropCharacteristicSetList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealPropCharacteristicSetList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.mvr.response.actual.RealPropCharacteristicSetType }
     * 
     * 
     */
    public List<RealPropCharacteristicSetType> getRealPropCharacteristicSetList() {
        if (realPropCharacteristicSetList == null) {
            realPropCharacteristicSetList = new ArrayList<RealPropCharacteristicSetType>();
        }
        return this.realPropCharacteristicSetList;
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

}
