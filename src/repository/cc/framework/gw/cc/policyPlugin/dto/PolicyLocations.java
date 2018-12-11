
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for PolicyLocations complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PolicyLocations">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PolicyLocation" type="{http://www.idfbins.com/PolicyRetrieveResponse}PolicyLocation" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyLocations", namespace = "http://www.idfbins.com/PolicyRetrieveResponse", propOrder = {
        "policyLocation"
})
public class PolicyLocations {

    @XmlElement(name = "PolicyLocation", namespace = "http://www.idfbins.com/PolicyRetrieveResponse")
    protected List<PolicyLocation> policyLocation;

    /**
     * Gets the value of the policyLocation property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policyLocation property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicyLocation().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PolicyLocation }
     */
    public List<PolicyLocation> getPolicyLocation() {
        if (policyLocation == null) {
            policyLocation = new ArrayList<PolicyLocation>();
        }
        return this.policyLocation;
    }

}
