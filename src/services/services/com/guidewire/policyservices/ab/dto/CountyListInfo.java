
package services.services.com.guidewire.policyservices.ab.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CountyListInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CountyListInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AllCounties" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Entry" type="{http://example.com/com/idfbins/ab/dto}CountyInfo" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "CountyListInfo", propOrder = {
    "allCounties"
})
public class CountyListInfo {

    @XmlElement(name = "AllCounties")
    protected CountyListInfo.AllCounties allCounties;

    /**
     * Gets the value of the allCounties property.
     * 
     * @return
     *     possible object is
     *     {@link CountyListInfo.AllCounties }
     *     
     */
    public CountyListInfo.AllCounties getAllCounties() {
        return allCounties;
    }

    /**
     * Sets the value of the allCounties property.
     * 
     * @param value
     *     allowed object is
     *     {@link CountyListInfo.AllCounties }
     *     
     */
    public void setAllCounties(CountyListInfo.AllCounties value) {
        this.allCounties = value;
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
     *         &lt;element name="Entry" type="{http://example.com/com/idfbins/ab/dto}CountyInfo" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class AllCounties {

        @XmlElement(name = "Entry", nillable = true)
        protected List<services.services.com.guidewire.policyservices.ab.dto.CountyInfo> entry;

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
         * {@link services.services.com.guidewire.policyservices.ab.dto.CountyInfo }
         * 
         * 
         */
        public List<services.services.com.guidewire.policyservices.ab.dto.CountyInfo> getEntry() {
            if (entry == null) {
                entry = new ArrayList<CountyInfo>();
            }
            return this.entry;
        }

    }

}
