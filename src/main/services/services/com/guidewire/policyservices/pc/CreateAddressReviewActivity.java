
package services.services.com.guidewire.policyservices.pc;

import services.services.com.guidewire.policyservices.ab.dto.AssociatedPolicyInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="info" type="{http://example.com/com/idfbins/ab/dto}AssociatedPolicyInfo" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "info"
})
@XmlRootElement(name = "createAddressReviewActivity")
public class CreateAddressReviewActivity {

    protected services.services.com.guidewire.policyservices.ab.dto.AssociatedPolicyInfo info;

    /**
     * Gets the value of the info property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.guidewire.policyservices.ab.dto.AssociatedPolicyInfo }
     *     
     */
    public services.services.com.guidewire.policyservices.ab.dto.AssociatedPolicyInfo getInfo() {
        return info;
    }

    /**
     * Sets the value of the info property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.guidewire.policyservices.ab.dto.AssociatedPolicyInfo }
     *     
     */
    public void setInfo(AssociatedPolicyInfo value) {
        this.info = value;
    }

}
