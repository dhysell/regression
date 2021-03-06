//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for rentalCarsListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="rentalCarsListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rental_car" type="{http://cp.com/rules/client}rentalCarType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rentalCarsListType", propOrder = {
    "rentalCarList"
})
public class RentalCarsListType {

    @XmlElement(name = "rental_car", required = true)
    protected List<RentalCarType> rentalCarList;

    /**
     * Gets the value of the rentalCarList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rentalCarList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRentalCarList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.clueproperty.response.actual.RentalCarType }
     * 
     * 
     */
    public List<RentalCarType> getRentalCarList() {
        if (rentalCarList == null) {
            rentalCarList = new ArrayList<RentalCarType>();
        }
        return this.rentalCarList;
    }

}
