//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Destinations complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Destinations"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="imageRight" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="operationsPrinter" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="destinationName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="destinationTiming" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Destinations", propOrder = {
    "imageRight",
    "operationsPrinter",
    "destinationName",
    "destinationTiming"
})
public class Destinations {

    protected boolean imageRight;
    protected boolean operationsPrinter;
    protected String destinationName;
    protected String destinationTiming;

    /**
     * Gets the value of the imageRight property.
     * 
     */
    public boolean isImageRight() {
        return imageRight;
    }

    /**
     * Sets the value of the imageRight property.
     * 
     */
    public void setImageRight(boolean value) {
        this.imageRight = value;
    }

    /**
     * Gets the value of the operationsPrinter property.
     * 
     */
    public boolean isOperationsPrinter() {
        return operationsPrinter;
    }

    /**
     * Sets the value of the operationsPrinter property.
     * 
     */
    public void setOperationsPrinter(boolean value) {
        this.operationsPrinter = value;
    }

    /**
     * Gets the value of the destinationName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationName() {
        return destinationName;
    }

    /**
     * Sets the value of the destinationName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationName(String value) {
        this.destinationName = value;
    }

    /**
     * Gets the value of the destinationTiming property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDestinationTiming() {
        return destinationTiming;
    }

    /**
     * Sets the value of the destinationTiming property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDestinationTiming(String value) {
        this.destinationTiming = value;
    }

}
