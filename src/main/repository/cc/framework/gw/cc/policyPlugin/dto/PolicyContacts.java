
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PolicyContacts complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PolicyContacts">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PolicyContact" type="{http://www.idfbins.com/PolicyRetrieveResponse}PolicyContact" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyContacts", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "policyContact"
})
public class PolicyContacts {

    @XmlElement(name = "PolicyContact", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<PolicyContact> policyContact;

    /**
     * Gets the value of the policyContact property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policyContact property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicyContact().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicyContact }
     */
    public List<PolicyContact> getPolicyContact() {
        if (policyContact == null) {
            policyContact = new ArrayList<PolicyContact>();
        }
        return this.policyContact;
    }

}
