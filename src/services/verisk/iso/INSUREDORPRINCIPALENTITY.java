//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:19:26 PM MDT 
//


package services.verisk.iso;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for INSUREDORPRINCIPALENTITY complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="INSUREDORPRINCIPALENTITY">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element ref="{}PersonInfo"/>
 *         &lt;element ref="{}BusinessInfo"/>
 *         &lt;element ref="{}PrincipalInfo"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "INSUREDORPRINCIPALENTITY", propOrder = {
    "personInfo",
    "businessInfo",
    "principalInfo"
})
@XmlSeeAlso({
    InsuredOrPrincipalInfo.class
})
public class INSUREDORPRINCIPALENTITY {

    @XmlElement(name = "PersonInfo")
    protected PersonInfo personInfo;
    @XmlElement(name = "BusinessInfo")
    protected BusinessInfo businessInfo;
    @XmlElement(name = "PrincipalInfo")
    protected PrincipalInfo principalInfo;

    /**
     * Gets the value of the personInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PersonInfo }
     *     
     */
    public PersonInfo getPersonInfo() {
        return personInfo;
    }

    /**
     * Sets the value of the personInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PersonInfo }
     *     
     */
    public void setPersonInfo(PersonInfo value) {
        this.personInfo = value;
    }

    /**
     * Gets the value of the businessInfo property.
     * 
     * @return
     *     possible object is
     *     {@link BusinessInfo }
     *     
     */
    public BusinessInfo getBusinessInfo() {
        return businessInfo;
    }

    /**
     * Sets the value of the businessInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link BusinessInfo }
     *     
     */
    public void setBusinessInfo(BusinessInfo value) {
        this.businessInfo = value;
    }

    /**
     * Gets the value of the principalInfo property.
     * 
     * @return
     *     possible object is
     *     {@link PrincipalInfo }
     *     
     */
    public PrincipalInfo getPrincipalInfo() {
        return principalInfo;
    }

    /**
     * Sets the value of the principalInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrincipalInfo }
     *     
     */
    public void setPrincipalInfo(PrincipalInfo value) {
        this.principalInfo = value;
    }

}
