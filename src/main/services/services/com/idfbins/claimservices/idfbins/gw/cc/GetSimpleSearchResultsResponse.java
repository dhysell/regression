
package services.services.com.idfbins.claimservices.idfbins.gw.cc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresultcontainer.ABContactAPISearchResultContainer;


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
 *         &lt;element name="return" type="{http://guidewire.com/ab/ws/gw/webservice/ab/ab801/abcontactapi/ABContactAPISearchResultContainer}ABContactAPISearchResultContainer" minOccurs="0"/>
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
@XmlRootElement(name = "getSimpleSearchResultsResponse")
public class GetSimpleSearchResultsResponse {

    @XmlElement(name = "return")
    protected ABContactAPISearchResultContainer _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link ABContactAPISearchResultContainer }
     *     
     */
    public ABContactAPISearchResultContainer getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link ABContactAPISearchResultContainer }
     *     
     */
    public void setReturn(ABContactAPISearchResultContainer value) {
        this._return = value;
    }

}
