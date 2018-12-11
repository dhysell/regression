
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PolicyLocation complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PolicyLocation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Location" type="{http://www.idfbins.com/PolicyRetrieveResponse}Location"/>
 *         &lt;element name="Buildings" type="{http://www.idfbins.com/PolicyRetrieveResponse}Buildings" minOccurs="0"/>
 *         &lt;element name="PropertyOwners" type="{http://www.idfbins.com/PolicyRetrieveResponse}PropertyOwners"/>
 *         &lt;element name="PropertyItems" type="{http://www.idfbins.com/PolicyRetrieveResponse}PropertyItem" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="PolicyLocationNumber" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyLocation", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "location",
        "buildings",
        "propertyOwners",
        "propertyItems",
        "policyLocationNumber"
})
public class PolicyLocation {

    @XmlElement(name = "Location", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected Location location;
    @XmlElement(name = "Buildings", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected Buildings buildings;
    @XmlElement(name = "PropertyOwners", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected PropertyOwners propertyOwners;
    @XmlElement(name = "PropertyItems", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<PropertyItem> propertyItems;
    @XmlElement(name = "PolicyLocationNumber", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", required = true)
    protected String policyLocationNumber;

    /**
     * Gets the value of the location property.
     *
     * @return possible object is
     * {@link Location }
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     *
     * @param value allowed object is
     *              {@link Location }
     */
    public void setLocation(Location value) {
        this.location = value;
    }

    /**
     * Gets the value of the buildings property.
     *
     * @return possible object is
     * {@link Buildings }
     */
    public Buildings getBuildings() {
        return buildings;
    }

    /**
     * Sets the value of the buildings property.
     *
     * @param value allowed object is
     *              {@link Buildings }
     */
    public void setBuildings(Buildings value) {
        this.buildings = value;
    }

    /**
     * Gets the value of the propertyOwners property.
     *
     * @return possible object is
     * {@link PropertyOwners }
     */
    public PropertyOwners getPropertyOwners() {
        return propertyOwners;
    }

    /**
     * Sets the value of the propertyOwners property.
     *
     * @param value allowed object is
     *              {@link PropertyOwners }
     */
    public void setPropertyOwners(PropertyOwners value) {
        this.propertyOwners = value;
    }

    /**
     * Gets the value of the propertyItems property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyItems property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyItems().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyItem }
     */
    public List<PropertyItem> getPropertyItems() {
        if (propertyItems == null) {
            propertyItems = new ArrayList<PropertyItem>();
        }
        return this.propertyItems;
    }

    /**
     * Gets the value of the policyLocationNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPolicyLocationNumber() {
        return policyLocationNumber;
    }

    /**
     * Sets the value of the policyLocationNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPolicyLocationNumber(String value) {
        this.policyLocationNumber = value;
    }

}
