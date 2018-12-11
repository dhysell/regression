
package services.services.com.guidewire.policyservices.pc;

import services.services.com.guidewire.policyservices.ab.dto.PaidCountyDueInfo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="return" type="{http://example.com/com/idfbins/ab/dto}PaidCountyDueInfo" minOccurs="0"/>
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
    "_return"
})
@XmlRootElement(name = "getLatestPaidCountyDueForContactByDateResponse")
public class GetLatestPaidCountyDueForContactByDateResponse {

    @XmlElement(name = "return")
    protected services.services.com.guidewire.policyservices.ab.dto.PaidCountyDueInfo _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.guidewire.policyservices.ab.dto.PaidCountyDueInfo }
     *     
     */
    public services.services.com.guidewire.policyservices.ab.dto.PaidCountyDueInfo getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.guidewire.policyservices.ab.dto.PaidCountyDueInfo }
     *     
     */
    public void setReturn(PaidCountyDueInfo value) {
        this._return = value;
    }

}
