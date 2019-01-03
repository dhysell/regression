
package services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresultcontainer;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresult.ABContactAPISearchResult;


/**
 * <p>Java class for ABContactAPISearchResultContainer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ABContactAPISearchResultContainer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Results" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Entry" type="{http://guidewire.com/ab/ws/gw/webservice/ab/ab801/abcontactapi/ABContactAPISearchResult}ABContactAPISearchResult" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="TotalResults" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ABContactAPISearchResultContainer", propOrder = {
    "results",
    "totalResults"
})
public class ABContactAPISearchResultContainer {

    @XmlElement(name = "Results")
    protected ABContactAPISearchResultContainer.Results results;
    @XmlElement(name = "TotalResults")
    protected Integer totalResults;

    /**
     * Gets the value of the results property.
     * 
     * @return
     *     possible object is
     *     {@link ABContactAPISearchResultContainer.Results }
     *     
     */
    public ABContactAPISearchResultContainer.Results getResults() {
        return results;
    }

    /**
     * Sets the value of the results property.
     * 
     * @param value
     *     allowed object is
     *     {@link ABContactAPISearchResultContainer.Results }
     *     
     */
    public void setResults(ABContactAPISearchResultContainer.Results value) {
        this.results = value;
    }

    /**
     * Gets the value of the totalResults property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getTotalResults() {
        return totalResults;
    }

    /**
     * Sets the value of the totalResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setTotalResults(Integer value) {
        this.totalResults = value;
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
     *         &lt;element name="Entry" type="{http://guidewire.com/ab/ws/gw/webservice/ab/ab801/abcontactapi/ABContactAPISearchResult}ABContactAPISearchResult" maxOccurs="unbounded" minOccurs="0"/>
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
        "entry"
    })
    public static class Results {

        @XmlElement(name = "Entry", nillable = true)
        protected List<ABContactAPISearchResult> entry;

        /**
         * Gets the value of the entry property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the entry property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getEntry().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link ABContactAPISearchResult }
         * 
         * 
         */
        public List<ABContactAPISearchResult> getEntry() {
            if (entry == null) {
                entry = new ArrayList<ABContactAPISearchResult>();
            }
            return this.entry;
        }

    }

}
