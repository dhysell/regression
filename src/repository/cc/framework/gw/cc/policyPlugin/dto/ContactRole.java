
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContactRole complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ContactRole">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RoleName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContactRole", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "roleName"
})
public class ContactRole {

    @XmlElement(name = "RoleName", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected String roleName;

    /**
     * Gets the value of the roleName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * Sets the value of the roleName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRoleName(String value) {
        this.roleName = value;
    }

}
