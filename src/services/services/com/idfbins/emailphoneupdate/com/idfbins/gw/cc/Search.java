
package services.services.com.idfbins.emailphoneupdate.com.idfbins.gw.cc;

import services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.crm.CRMSearchCriteria;

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
 *         &lt;element name="criteria" type="{http://example.com/com/idfbins/ab/crm}CRMSearchCriteria" minOccurs="0"/>
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
    "criteria"
})
@XmlRootElement(name = "search")
public class Search {

    protected services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.crm.CRMSearchCriteria criteria;

    /**
     * Gets the value of the criteria property.
     * 
     * @return
     *     possible object is
     *     {@link services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.crm.CRMSearchCriteria }
     *     
     */
    public services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.crm.CRMSearchCriteria getCriteria() {
        return criteria;
    }

    /**
     * Sets the value of the criteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.crm.CRMSearchCriteria }
     *     
     */
    public void setCriteria(CRMSearchCriteria value) {
        this.criteria = value;
    }

}
