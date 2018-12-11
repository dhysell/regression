
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PolicyContact complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PolicyContact">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ContactRoles" type="{http://www.idfbins.com/PolicyRetrieveResponse}ContactRoles" minOccurs="0"/>
 *         &lt;element name="Contact" type="{http://www.idfbins.com/PolicyRetrieveResponse}Contact"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyContact", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "contactRoles",
        "contact"
})
public class PolicyContact {

    @XmlElement(name = "ContactRoles", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected ContactRoles contactRoles;
    @XmlElement(name = "Contact", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Contact contact;

    /**
     * Gets the value of the contactRoles property.
     *
     * @return possible object is
     * {@link ContactRoles }
     */
    public ContactRoles getContactRoles() {
        return contactRoles;
    }

    /**
     * Sets the value of the contactRoles property.
     *
     * @param value allowed object is
     *              {@link ContactRoles }
     */
    public void setContactRoles(ContactRoles value) {
        this.contactRoles = value;
    }

    /**
     * Gets the value of the contact property.
     *
     * @return possible object is
     * {@link Contact }
     */
    public Contact getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     *
     * @param value allowed object is
     *              {@link Contact }
     */
    public void setContact(Contact value) {
        this.contact = value;
    }

}
