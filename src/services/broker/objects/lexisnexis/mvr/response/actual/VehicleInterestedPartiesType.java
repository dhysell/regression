//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for vehicleInterestedPartiesType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vehicleInterestedPartiesType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="party" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;simpleContent>
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                 &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *                 &lt;attribute name="type" use="required" type="{http://cp.com/rules/client}interested_parties_enum" />
 *                 &lt;attribute name="fsi_description" type="{http://cp.com/rules/client}CPfsiType" />
 *               &lt;/extension>
 *             &lt;/simpleContent>
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
@XmlType(name = "vehicleInterestedPartiesType", propOrder = {
    "party"
})
public class VehicleInterestedPartiesType {

    protected List<VehicleInterestedPartiesType.Party> party;

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
     * {@link VehicleInterestedPartiesType.Party }
     * 
     * 
     */
    public List<VehicleInterestedPartiesType.Party> getParty() {
        if (party == null) {
            party = new ArrayList<VehicleInterestedPartiesType.Party>();
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
     *   &lt;simpleContent>
     *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
     *       &lt;attribute name="ref" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
     *       &lt;attribute name="type" use="required" type="{http://cp.com/rules/client}interested_parties_enum" />
     *       &lt;attribute name="fsi_description" type="{http://cp.com/rules/client}CPfsiType" />
     *     &lt;/extension>
     *   &lt;/simpleContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "value"
    })
    public static class Party {

        @XmlValue
        protected String value;
        @XmlAttribute(name = "ref")
        @XmlIDREF
        @XmlSchemaType(name = "IDREF")
        protected Object ref;
        @XmlAttribute(name = "type", required = true)
        protected InterestedPartiesEnum type;
        @XmlAttribute(name = "fsi_description")
        protected CPFSIEnum fsiDescription;

        /**
         * Gets the value of the value property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getValue() {
            return value;
        }

        /**
         * Sets the value of the value property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setValue(String value) {
            this.value = value;
        }

        /**
         * Gets the value of the ref property.
         * 
         * @return
         *     possible object is
         *     {@link Object }
         *     
         */
        public Object getRef() {
            return ref;
        }

        /**
         * Sets the value of the ref property.
         * 
         * @param value
         *     allowed object is
         *     {@link Object }
         *     
         */
        public void setRef(Object value) {
            this.ref = value;
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
         *     {@link CPFSIEnum }
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
         *     {@link CPFSIEnum }
         *     
         */
        public void setFsiDescription(CPFSIEnum value) {
            this.fsiDescription = value;
        }

    }

}
