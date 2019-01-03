//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for businessAddressType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="businessAddressType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://cp.com/rules/client}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="type" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="primary"/>
 *             &lt;enumeration value="former"/>
 *             &lt;enumeration value="alternate"/>
 *             &lt;enumeration value="mailing"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "businessAddressType", propOrder = {
    "parameterList"
})
public class BusinessAddressType {

    @XmlElement(name = "parameter")
    protected List<ParameterType> parameterList;
    @XmlAttribute(name = "type", required = true)
    protected BusinessAddressType.BusinessAddressReferenceTypeEnum type;
    @XmlAttribute(name = "ref", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ref;

    /**
     * Gets the value of the parameterList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameterList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameterList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link broker.objects.lexisnexis.cbr.response.actual.ParameterType }
     * 
     * 
     */
    public List<ParameterType> getParameterList() {
        if (parameterList == null) {
            parameterList = new ArrayList<ParameterType>();
        }
        return this.parameterList;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link BusinessAddressType.BusinessAddressReferenceTypeEnum }
     *     
     */
    public BusinessAddressType.BusinessAddressReferenceTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link BusinessAddressType.BusinessAddressReferenceTypeEnum }
     *     
     */
    public void setType(BusinessAddressType.BusinessAddressReferenceTypeEnum value) {
        this.type = value;
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
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="primary"/>
     *     &lt;enumeration value="former"/>
     *     &lt;enumeration value="alternate"/>
     *     &lt;enumeration value="mailing"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum BusinessAddressReferenceTypeEnum {

        @XmlEnumValue("primary")
        PRIMARY("primary"),
        @XmlEnumValue("former")
        FORMER("former"),
        @XmlEnumValue("alternate")
        ALTERNATE("alternate"),
        @XmlEnumValue("mailing")
        MAILING("mailing");
        private final String value;

        BusinessAddressReferenceTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static BusinessAddressType.BusinessAddressReferenceTypeEnum fromValue(String v) {
            for (BusinessAddressType.BusinessAddressReferenceTypeEnum c: BusinessAddressType.BusinessAddressReferenceTypeEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}