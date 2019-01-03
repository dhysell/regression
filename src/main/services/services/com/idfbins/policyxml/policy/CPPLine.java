//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.04.09 at 10:33:19 AM MDT 
//


package services.services.com.idfbins.policyxml.policy;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CPPLine complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CPPLine"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DescriptionOfOperations" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="YearBusinessStarted" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="SellLiquor" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="DescriptionOfOperations" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="YearBusinessStarted" type="{http://www.w3.org/2001/XMLSchema}int"/&gt;
 *         &lt;element name="SellLiquor" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="SmallBusinessTypeCode" type="{http://www.idfbins.com/Policy}ValuePair"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CPPLine", propOrder = {
    "content"
})
public class CPPLine {

    @XmlElementRefs({
        @XmlElementRef(name = "SmallBusinessTypeCode", namespace = "http://www.idfbins.com/Policy", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "YearBusinessStarted", namespace = "http://www.idfbins.com/Policy", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "DescriptionOfOperations", namespace = "http://www.idfbins.com/Policy", type = JAXBElement.class, required = false),
        @XmlElementRef(name = "SellLiquor", namespace = "http://www.idfbins.com/Policy", type = JAXBElement.class, required = false)
    })
    protected List<JAXBElement<?>> content;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "DescriptionOfOperations" is used by two different parts of a schema. See: 
     * line 1268 of file:/C:/Users/jevans/Desktop/jaxb-ri/bin/schema/policy/Policy.xsd
     * line 1264 of file:/C:/Users/jevans/Desktop/jaxb-ri/bin/schema/policy/Policy.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the content property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the content property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContent().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link String }{@code >}
     * {@link JAXBElement }{@code <}{@link Integer }{@code >}
     * {@link JAXBElement }{@code <}{@link ValuePair }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getContent() {
        if (content == null) {
            content = new ArrayList<JAXBElement<?>>();
        }
        return this.content;
    }

}