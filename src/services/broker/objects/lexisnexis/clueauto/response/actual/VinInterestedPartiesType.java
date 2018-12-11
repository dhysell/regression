//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vinInterestedPartiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vinInterestedPartiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="party" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="type" type="{http://cp.com/rules/client}interested_parties_enum" />
 *                 &lt;attribute name="fsi_description" type="{http://cp.com/rules/client}CPfsiType" />
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
@XmlType(name = "vinInterestedPartiesType", propOrder = {
    "party"
})
public class VinInterestedPartiesType {

    protected List<VinInterestedPartiesType.Party> party;

    /**
     * Gets the value of the party property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the party property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VinInterestedPartiesType.Party }
     * 
     * 
     */
    public List<VinInterestedPartiesType.Party> getParty() {
        if (party == null) {
            party = new ArrayList<VinInterestedPartiesType.Party>();
        }
        return this.party;
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
     *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
     *       &lt;/sequence>
     *       &lt;attribute name="type" type="{http://cp.com/rules/client}interested_parties_enum" />
     *       &lt;attribute name="fsi_description" type="{http://cp.com/rules/client}CPfsiType" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "description"
    })
    public static class Party {

        protected String description;
        @XmlAttribute(name = "type")
        protected InterestedPartiesEnum type;
        @XmlAttribute(name = "fsi_description")
        protected CPFSIEnum fsiDescription;

        /**
         * Gets the value of the description property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getDescription() {
            return description;
        }

        /**
         * Sets the value of the description property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setDescription(String value) {
            this.description = value;
        }

        /**
         * Gets the value of the type property.
         * 
         * @return
         *     possible object is
         *     {@link InterestedPartiesEnum }
         *     
         */
        public InterestedPartiesEnum getType() {
            return type;
        }

        /**
         * Sets the value of the type property.
         * 
         * @param value
         *     allowed object is
         *     {@link InterestedPartiesEnum }
         *     
         */
        public void setType(InterestedPartiesEnum value) {
            this.type = value;
        }

        /**
         * Gets the value of the fsiDescription property.
         * 
         * @return
         *     possible object is
         *     {@link broker.objects.lexisnexis.clueauto.response.actual.CPFSIEnum }
         *     
         */
        public CPFSIEnum getFsiDescription() {
            return fsiDescription;
        }

        /**
         * Sets the value of the fsiDescription property.
         * 
         * @param value
         *     allowed object is
         *     {@link broker.objects.lexisnexis.clueauto.response.actual.CPFSIEnum }
         *     
         */
        public void setFsiDescription(CPFSIEnum value) {
            this.fsiDescription = value;
        }

    }

}
