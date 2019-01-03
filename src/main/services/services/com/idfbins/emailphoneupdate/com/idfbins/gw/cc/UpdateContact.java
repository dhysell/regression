
package services.services.com.idfbins.emailphoneupdate.com.idfbins.gw.cc;

import services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo;

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
 *         &lt;element name="contactInfo" type="{http://example.com/com/idfbins/ab/dto}CRMContactInfo" minOccurs="0"/>
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
@XmlRootElement(name = "updateContact")
public class UpdateContact {

    protected services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo contactInfo;

    /**
     * Gets the value of the contactInfo property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo }
     *     
     */
    public services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo getContactInfo() {
        return contactInfo;
    }

    /**
     * Sets the value of the contactInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.dto.CRMContactInfo }
     *     
     */
    public void setContactInfo(CRMContactInfo value) {
        this.contactInfo = value;
    }

}
