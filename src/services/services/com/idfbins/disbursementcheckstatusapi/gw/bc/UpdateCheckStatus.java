
package services.services.com.idfbins.disbursementcheckstatusapi.gw.bc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import services.services.com.idfbins.disbursementcheckstatusapi.billingcenter.ropexportresponsetobc.SungardROPResponse;


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
 *         &lt;element name="response" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.idfbins.com/BillingCenter/ROPExportResponseToBC}SungardROPResponse"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
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
    "response"
})
@XmlRootElement(name = "updateCheckStatus")
public class UpdateCheckStatus {

    protected UpdateCheckStatus.Response response;

    /**
     * Gets the value of the response property.
     * 
     * @return
     *     possible object is
     *     {@link UpdateCheckStatus.Response }
     *     
     */
    public UpdateCheckStatus.Response getResponse() {
        return response;
    }

    /**
     * Sets the value of the response property.
     * 
     * @param value
     *     allowed object is
     *     {@link UpdateCheckStatus.Response }
     *     
     */
    public void setResponse(UpdateCheckStatus.Response value) {
        this.response = value;
    }


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
     *         &lt;element ref="{http://www.idfbins.com/BillingCenter/ROPExportResponseToBC}SungardROPResponse"/>
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
        "sungardROPResponse"
    })
    public static class Response {

        @XmlElement(name = "SungardROPResponse", namespace = "http://www.idfbins.com/BillingCenter/ROPExportResponseToBC", required = true)
        protected SungardROPResponse sungardROPResponse;

        /**
         * Gets the value of the sungardROPResponse property.
         * 
         * @return
         *     possible object is
         *     {@link SungardROPResponse }
         *     
         */
        public SungardROPResponse getSungardROPResponse() {
            return sungardROPResponse;
        }

        /**
         * Sets the value of the sungardROPResponse property.
         * 
         * @param value
         *     allowed object is
         *     {@link SungardROPResponse }
         *     
         */
        public void setSungardROPResponse(SungardROPResponse value) {
            this.sungardROPResponse = value;
        }

    }

}
