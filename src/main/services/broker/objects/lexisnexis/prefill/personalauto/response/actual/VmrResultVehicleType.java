//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vmrResultVehicleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vmrResultVehicleType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cp.com/rules/client}resultVehicleType">
 *       &lt;sequence>
 *         &lt;element name="mileage" type="{http://cp.com/rules/client}mileageType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vmrResultVehicleType", propOrder = {
    "mileage"
})
public class VmrResultVehicleType
    extends ResultVehicleType
{

    protected List<MileageType> mileage;

    /**
     * Gets the value of the mileage property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mileage property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMileage().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.MileageType }
     * 
     * 
     */
    public List<MileageType> getMileage() {
        if (mileage == null) {
            mileage = new ArrayList<MileageType>();
        }
        return this.mileage;
    }

}