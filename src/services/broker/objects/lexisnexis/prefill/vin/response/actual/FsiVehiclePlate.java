//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:09:30 PM MST 
//


package services.broker.objects.lexisnexis.prefill.vin.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fsi_vehiclePlate complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fsi_vehiclePlate">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="state" type="{http://cp.com/rules/client}CPfsiType" minOccurs="0"/>
 *         &lt;element name="number" type="{http://cp.com/rules/client}CPfsiType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fsi_vehiclePlate", propOrder = {
    "state",
    "number"
})
public class FsiVehiclePlate {

    @XmlSchemaType(name = "string")
    protected CPFSIEnum state;
    @XmlSchemaType(name = "string")
    protected CPFSIEnum number;

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.vin.response.actual.CPFSIEnum }
     *     
     */
    public CPFSIEnum getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.vin.response.actual.CPFSIEnum }
     *     
     */
    public void setState(CPFSIEnum value) {
        this.state = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.vin.response.actual.CPFSIEnum }
     *     
     */
    public CPFSIEnum getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.vin.response.actual.CPFSIEnum }
     *     
     */
    public void setNumber(CPFSIEnum value) {
        this.number = value;
    }

}
