
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PropertyItems complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PropertyItems">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PropertyItem" type="{http://www.idfbins.com/PolicyRetrieveResponse}PropertyItem" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PropertyItems", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "propertyItem"
})
public class PropertyItems {

    @XmlElement(name = "PropertyItem", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<PropertyItem> propertyItem;

    /**
     * Gets the value of the propertyItem property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the propertyItem property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPropertyItem().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PropertyItem }
     */
    public List<PropertyItem> getPropertyItem() {
        if (propertyItem == null) {
            propertyItem = new ArrayList<PropertyItem>();
        }
        return this.propertyItem;
    }

}
