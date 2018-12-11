
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PropertyOwners complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PropertyOwners">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PropertyOwner" type="{http://www.idfbins.com/PolicyRetrieveResponse}Owner" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyOwners", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "propertyOwner"
})
public class PropertyOwners {

    @XmlElement(name = "PropertyOwner", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<Owner> propertyOwner;

    /**
     * Gets the value of the propertyOwner property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyOwner property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyOwner().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Owner }
     */
    public List<Owner> getPropertyOwner() {
        if (propertyOwner == null) {
            propertyOwner = new ArrayList<Owner>();
        }
        return this.propertyOwner;
    }

}
