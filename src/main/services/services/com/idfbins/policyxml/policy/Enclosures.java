//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Enclosures complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Enclosures"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="personToSendTo" type="{http://www.idfbins.com/Policy}Contact" minOccurs="0"/&gt;
 *         &lt;element name="receiverKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="addressToSendTo" type="{http://www.idfbins.com/Policy}Location" minOccurs="0"/&gt;
 *         &lt;element name="enclosure" type="{http://www.idfbins.com/Policy}Enclosure" maxOccurs="unbounded"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Enclosures", propOrder = {
    "personToSendTo",
    "receiverKey",
    "addressToSendTo",
    "enclosure"
})
public class Enclosures {

    protected services.services.com.idfbins.policyxml.policy.Contact personToSendTo;
    protected String receiverKey;
    protected services.services.com.idfbins.policyxml.policy.Location addressToSendTo;
    @XmlElement(required = true)
    protected List<services.services.com.idfbins.policyxml.policy.Enclosure> enclosure;

    /**
     * Gets the value of the personToSendTo property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.Contact }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.Contact getPersonToSendTo() {
        return personToSendTo;
    }

    /**
     * Sets the value of the personToSendTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.Contact }
     *     
     */
    public void setPersonToSendTo(Contact value) {
        this.personToSendTo = value;
    }

    /**
     * Gets the value of the receiverKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReceiverKey() {
        return receiverKey;
    }

    /**
     * Sets the value of the receiverKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReceiverKey(String value) {
        this.receiverKey = value;
    }

    /**
     * Gets the value of the addressToSendTo property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.policyxml.policy.Location }
     *     
     */
    public services.services.com.idfbins.policyxml.policy.Location getAddressToSendTo() {
        return addressToSendTo;
    }

    /**
     * Sets the value of the addressToSendTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.policyxml.policy.Location }
     *     
     */
    public void setAddressToSendTo(Location value) {
        this.addressToSendTo = value;
    }

    /**
     * Gets the value of the enclosure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enclosure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEnclosure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link services.services.com.idfbins.policyxml.policy.Enclosure }
     * 
     * 
     */
    public List<services.services.com.idfbins.policyxml.policy.Enclosure> getEnclosure() {
        if (enclosure == null) {
            enclosure = new ArrayList<Enclosure>();
        }
        return this.enclosure;
    }

}
