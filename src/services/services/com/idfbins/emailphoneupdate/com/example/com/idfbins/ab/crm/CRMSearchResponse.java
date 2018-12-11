
package services.services.com.idfbins.emailphoneupdate.com.example.com.idfbins.ab.crm;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CRMSearchResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CRMSearchResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AccountCreatedInAB" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AccountCreatedInPC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="AccountNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AddressBookUID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="HasMultiple" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="SearchResults" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Entry" type="{http://example.com/com/idfbins/ab/crm}CRMSearchResult" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "CRMSearchResponse", propOrder = {
    "accountCreatedInAB",
    "accountCreatedInPC",
    "accountNumber",
    "addressBookUID",
    "hasMultiple",
    "searchResults"
})
public class CRMSearchResponse {

    @XmlElement(name = "AccountCreatedInAB")
    protected Boolean accountCreatedInAB;
    @XmlElement(name = "AccountCreatedInPC")
    protected Boolean accountCreatedInPC;
    @XmlElement(name = "AccountNumber")
    protected String accountNumber;
    @XmlElement(name = "AddressBookUID")
    protected String addressBookUID;
    @XmlElement(name = "HasMultiple")
    protected Boolean hasMultiple;
    @XmlElement(name = "SearchResults")
    protected CRMSearchResponse.SearchResults searchResults;

    /**
     * Gets the value of the accountCreatedInAB property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccountCreatedInAB() {
        return accountCreatedInAB;
    }

    /**
     * Sets the value of the accountCreatedInAB property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccountCreatedInAB(Boolean value) {
        this.accountCreatedInAB = value;
    }

    /**
     * Gets the value of the accountCreatedInPC property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isAccountCreatedInPC() {
        return accountCreatedInPC;
    }

    /**
     * Sets the value of the accountCreatedInPC property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setAccountCreatedInPC(Boolean value) {
        this.accountCreatedInPC = value;
    }

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the addressBookUID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAddressBookUID() {
        return addressBookUID;
    }

    /**
     * Sets the value of the addressBookUID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAddressBookUID(String value) {
        this.addressBookUID = value;
    }

    /**
     * Gets the value of the hasMultiple property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isHasMultiple() {
        return hasMultiple;
    }

    /**
     * Sets the value of the hasMultiple property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setHasMultiple(Boolean value) {
        this.hasMultiple = value;
    }

    /**
     * Gets the value of the searchResults property.
     * 
     * @return
     *     possible object is
     *     {@link CRMSearchResponse.SearchResults }
     *     
     */
    public CRMSearchResponse.SearchResults getSearchResults() {
        return searchResults;
    }

    /**
     * Sets the value of the searchResults property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRMSearchResponse.SearchResults }
     *     
     */
    public void setSearchResults(CRMSearchResponse.SearchResults value) {
        this.searchResults = value;
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
     *         &lt;element name="Entry" type="{http://example.com/com/idfbins/ab/crm}CRMSearchResult" maxOccurs="unbounded" minOccurs="0"/>
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
    public static class SearchResults {

        @XmlElement(name = "Entry", nillable = true)
        protected List<CRMSearchResult> entry;

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
         * {@link CRMSearchResult }
         * 
         * 
         */
        public List<CRMSearchResult> getEntry() {
            if (entry == null) {
                entry = new ArrayList<CRMSearchResult>();
            }
            return this.entry;
        }

    }

}
