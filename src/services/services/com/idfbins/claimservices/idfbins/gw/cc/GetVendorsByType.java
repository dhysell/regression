
package services.services.com.idfbins.claimservices.idfbins.gw.cc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimVendorType;


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
 *         &lt;element name="cvt" type="{http://guidewire.com/ab/typekey}ClaimVendorType" minOccurs="0"/>
 *         &lt;element name="preferred" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
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
    "cvt",
    "preferred"
})
@XmlRootElement(name = "getVendorsByType")
public class GetVendorsByType {

    @XmlSchemaType(name = "string")
    protected ClaimVendorType cvt;
    protected boolean preferred;

    /**
     * Gets the value of the cvt property.
     * 
     * @return
     *     possible object is
     *     {@link ClaimVendorType }
     *     
     */
    public ClaimVendorType getCvt() {
        return cvt;
    }

    /**
     * Sets the value of the cvt property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClaimVendorType }
     *     
     */
    public void setCvt(ClaimVendorType value) {
        this.cvt = value;
    }

    /**
     * Gets the value of the preferred property.
     * 
     */
    public boolean isPreferred() {
        return preferred;
    }

    /**
     * Sets the value of the preferred property.
     * 
     */
    public void setPreferred(boolean value) {
        this.preferred = value;
    }

}
