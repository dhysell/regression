//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="returned_name_address" type="{http://cp.com/rules/client}mvrReturnedNameAddressType" minOccurs="0"/>
 *         &lt;element name="birth_date" type="{http://cp.com/rules/client}LenientDateType" minOccurs="0"/>
 *         &lt;element name="height" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gender" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Female"/>
 *               &lt;enumeration value="Male"/>
 *               &lt;enumeration value="Unknown"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="eye_color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hair_color" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="also_known_as" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ssn" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;length value="9"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
@XmlType(name = "", propOrder = {
    "returnedNameAddress",
    "birthDate",
    "height",
    "weight",
    "gender",
    "eyeColor",
    "hairColor",
    "alsoKnownAs",
    "ssn"
})
@XmlRootElement(name = "identification")
public class Identification {

    @XmlElement(name = "returned_name_address")
    protected MvrReturnedNameAddressType returnedNameAddress;
    @XmlElement(name = "birth_date")
    protected String birthDate;
    protected String height;
    protected String weight;
    protected Identification.MvrIdGenderEnum gender;
    @XmlElement(name = "eye_color")
    protected String eyeColor;
    @XmlElement(name = "hair_color")
    protected String hairColor;
    @XmlElement(name = "also_known_as")
    protected String alsoKnownAs;
    protected String ssn;

    /**
     * Gets the value of the returnedNameAddress property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.MvrReturnedNameAddressType }
     *     
     */
    public MvrReturnedNameAddressType getReturnedNameAddress() {
        return returnedNameAddress;
    }

    /**
     * Sets the value of the returnedNameAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.mvr.response.actual.MvrReturnedNameAddressType }
     *     
     */
    public void setReturnedNameAddress(MvrReturnedNameAddressType value) {
        this.returnedNameAddress = value;
    }

    /**
     * Gets the value of the birthDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     * Sets the value of the birthDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBirthDate(String value) {
        this.birthDate = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHeight(String value) {
        this.height = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link Identification.MvrIdGenderEnum }
     *     
     */
    public Identification.MvrIdGenderEnum getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link Identification.MvrIdGenderEnum }
     *     
     */
    public void setGender(Identification.MvrIdGenderEnum value) {
        this.gender = value;
    }

    /**
     * Gets the value of the eyeColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEyeColor() {
        return eyeColor;
    }

    /**
     * Sets the value of the eyeColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEyeColor(String value) {
        this.eyeColor = value;
    }

    /**
     * Gets the value of the hairColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHairColor() {
        return hairColor;
    }

    /**
     * Sets the value of the hairColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHairColor(String value) {
        this.hairColor = value;
    }

    /**
     * Gets the value of the alsoKnownAs property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAlsoKnownAs() {
        return alsoKnownAs;
    }

    /**
     * Sets the value of the alsoKnownAs property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAlsoKnownAs(String value) {
        this.alsoKnownAs = value;
    }

    /**
     * Gets the value of the ssn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSsn() {
        return ssn;
    }

    /**
     * Sets the value of the ssn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSsn(String value) {
        this.ssn = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Female"/>
     *     &lt;enumeration value="Male"/>
     *     &lt;enumeration value="Unknown"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum MvrIdGenderEnum {

        @XmlEnumValue("Female")
        FEMALE("Female"),
        @XmlEnumValue("Male")
        MALE("Male"),
        @XmlEnumValue("Unknown")
        UNKNOWN("Unknown");
        private final String value;

        MvrIdGenderEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static Identification.MvrIdGenderEnum fromValue(String v) {
            for (Identification.MvrIdGenderEnum c: Identification.MvrIdGenderEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
