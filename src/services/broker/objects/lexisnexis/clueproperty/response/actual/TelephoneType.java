//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for telephoneType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="telephoneType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="cell"/>
 *             &lt;enumeration value="contact"/>
 *             &lt;enumeration value="contact fax"/>
 *             &lt;enumeration value="employer"/>
 *             &lt;enumeration value="former"/>
 *             &lt;enumeration value="previous employer"/>
 *             &lt;enumeration value="reference"/>
 *             &lt;enumeration value="residence"/>
 *             &lt;enumeration value="residence fax"/>
 *             &lt;enumeration value="work"/>
 *             &lt;enumeration value="work fax"/>
 *             &lt;enumeration value="physician/medical"/>
 *             &lt;enumeration value="current employer"/>
 *             &lt;enumeration value="policy owner"/>
 *             &lt;enumeration value="educator"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="country" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="area">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="exchange">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="3"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="number">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="4"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="extension" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "telephoneType", propOrder = {
    "value"
})
@XmlSeeAlso({
    ClueTelephoneType.class
})
public class TelephoneType {

    @XmlValue
    protected String value;
    @XmlAttribute(name = "type")
    protected TelephoneType.TelephoneTypeEnum type;
    @XmlAttribute(name = "country")
    protected String country;
    @XmlAttribute(name = "area")
    protected String area;
    @XmlAttribute(name = "exchange")
    protected String exchange;
    @XmlAttribute(name = "number")
    protected String number;
    @XmlAttribute(name = "extension")
    protected String extension;

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
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link TelephoneType.TelephoneTypeEnum }
     *     
     */
    public TelephoneType.TelephoneTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link TelephoneType.TelephoneTypeEnum }
     *     
     */
    public void setType(TelephoneType.TelephoneTypeEnum value) {
        this.type = value;
    }

    /**
     * Gets the value of the country property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the value of the country property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCountry(String value) {
        this.country = value;
    }

    /**
     * Gets the value of the area property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArea() {
        return area;
    }

    /**
     * Sets the value of the area property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArea(String value) {
        this.area = value;
    }

    /**
     * Gets the value of the exchange property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExchange() {
        return exchange;
    }

    /**
     * Sets the value of the exchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExchange(String value) {
        this.exchange = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumber(String value) {
        this.number = value;
    }

    /**
     * Gets the value of the extension property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Sets the value of the extension property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtension(String value) {
        this.extension = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="cell"/>
     *     &lt;enumeration value="contact"/>
     *     &lt;enumeration value="contact fax"/>
     *     &lt;enumeration value="employer"/>
     *     &lt;enumeration value="former"/>
     *     &lt;enumeration value="previous employer"/>
     *     &lt;enumeration value="reference"/>
     *     &lt;enumeration value="residence"/>
     *     &lt;enumeration value="residence fax"/>
     *     &lt;enumeration value="work"/>
     *     &lt;enumeration value="work fax"/>
     *     &lt;enumeration value="physician/medical"/>
     *     &lt;enumeration value="current employer"/>
     *     &lt;enumeration value="policy owner"/>
     *     &lt;enumeration value="educator"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum TelephoneTypeEnum {

        @XmlEnumValue("cell")
        CELL("cell"),
        @XmlEnumValue("contact")
        CONTACT("contact"),
        @XmlEnumValue("contact fax")
        CONTACT_FAX("contact fax"),
        @XmlEnumValue("employer")
        EMPLOYER("employer"),
        @XmlEnumValue("former")
        FORMER("former"),
        @XmlEnumValue("previous employer")
        PREVIOUS_EMPLOYER("previous employer"),
        @XmlEnumValue("reference")
        REFERENCE("reference"),
        @XmlEnumValue("residence")
        RESIDENCE("residence"),
        @XmlEnumValue("residence fax")
        RESIDENCE_FAX("residence fax"),
        @XmlEnumValue("work")
        WORK("work"),
        @XmlEnumValue("work fax")
        WORK_FAX("work fax"),
        @XmlEnumValue("physician/medical")
        PHYSICIAN_MEDICAL("physician/medical"),
        @XmlEnumValue("current employer")
        CURRENT_EMPLOYER("current employer"),
        @XmlEnumValue("policy owner")
        POLICY_OWNER("policy owner"),
        @XmlEnumValue("educator")
        EDUCATOR("educator");
        private final String value;

        TelephoneTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static TelephoneType.TelephoneTypeEnum fromValue(String v) {
            for (TelephoneType.TelephoneTypeEnum c: TelephoneType.TelephoneTypeEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
