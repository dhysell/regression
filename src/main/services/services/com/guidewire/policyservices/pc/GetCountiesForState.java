
package services.services.com.guidewire.policyservices.pc;

import services.services.com.guidewire.policyservices.ab.typekey.State;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
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
 *         &lt;element name="state" type="{http://guidewire.com/ab/typekey}State" minOccurs="0"/>
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
    "state"
})
@XmlRootElement(name = "getCountiesForState")
public class GetCountiesForState {

    @XmlSchemaType(name = "string")
    protected services.services.com.guidewire.policyservices.ab.typekey.State state;

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.guidewire.policyservices.ab.typekey.State }
     *     
     */
    public services.services.com.guidewire.policyservices.ab.typekey.State getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.guidewire.policyservices.ab.typekey.State }
     *     
     */
    public void setState(State value) {
        this.state = value;
    }

}
