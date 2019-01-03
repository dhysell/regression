
package services.services.com.guidewire.policyservices.pc;

import services.services.com.guidewire.policyservices.ab.dto.PrimaryNamedInsuredInfo;

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
 *         &lt;element name="contactInfo" type="{http://example.com/com/idfbins/ab/dto}PrimaryNamedInsuredInfo" minOccurs="0"/>
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
    "contactInfo"
})
@XmlRootElement(name = "createPrimaryNamedInsuredChangeActivity")
public class CreatePrimaryNamedInsuredChangeActivity {

    protected services.services.com.guidewire.policyservices.ab.dto.PrimaryNamedInsuredInfo contactInfo;

    /**
     * Gets the value of the contactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.guidewire.policyservices.ab.dto.PrimaryNamedInsuredInfo }
     *     
     */
    public services.services.com.guidewire.policyservices.ab.dto.PrimaryNamedInsuredInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.guidewire.policyservices.ab.dto.PrimaryNamedInsuredInfo }
     *     
     */
    public void setContactInfo(PrimaryNamedInsuredInfo value) {
        this.contactInfo = value;
    }

}
