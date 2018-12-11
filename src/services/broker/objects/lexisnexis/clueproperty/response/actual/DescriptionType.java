//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:41 PM MST 
//


package services.broker.objects.lexisnexis.clueproperty.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for descriptionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="descriptionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="race" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="African American"/>
 *               &lt;enumeration value="Alaska Native"/>
 *               &lt;enumeration value="American Indian"/>
 *               &lt;enumeration value="Asian"/>
 *               &lt;enumeration value="Black"/>
 *               &lt;enumeration value="Hispanic"/>
 *               &lt;enumeration value="Native Hawaiian"/>
 *               &lt;enumeration value="Pacific Islander"/>
 *               &lt;enumeration value="White"/>
 *               &lt;enumeration value="Multi Racial"/>
 *               &lt;enumeration value="Unknown"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="sex" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Female"/>
 *               &lt;enumeration value="Male"/>
 *               &lt;enumeration value="Unknown"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="height" type="{http://cp.com/rules/client}heightType" minOccurs="0"/>
 *         &lt;element name="weight" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *               &lt;maxInclusive value="9999"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="eyecolor" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Black"/>
 *               &lt;enumeration value="Blue"/>
 *               &lt;enumeration value="Brown"/>
 *               &lt;enumeration value="Green"/>
 *               &lt;enumeration value="Grey"/>
 *               &lt;enumeration value="Hazel"/>
 *               &lt;enumeration value="Maroon"/>
 *               &lt;enumeration value="Multi-Colored"/>
 *               &lt;enumeration value="Pink"/>
 *               &lt;enumeration value="Unknown"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="haircolor" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Black"/>
 *               &lt;enumeration value="Blond"/>
 *               &lt;enumeration value="Brown"/>
 *               &lt;enumeration value="Grey"/>
 *               &lt;enumeration value="Red"/>
 *               &lt;enumeration value="Sandy"/>
 *               &lt;enumeration value="White"/>
 *               &lt;enumeration value="Unknown"/>
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
@XmlType(name = "descriptionType", propOrder = {
    "race",
    "sex",
    "height",
    "weight",
    "eyecolor",
    "haircolor"
})
public class DescriptionType {

    protected DescriptionType.RaceEnum race;
    protected DescriptionType.SexEnum sex;
    protected HeightType height;
    @XmlElement(type = String.class)
    @XmlJavaTypeAdapter(Adapter14 .class)
    protected Integer weight;
    protected DescriptionType.EyeColorEnum eyecolor;
    protected DescriptionType.HairColorEnum haircolor;

    /**
     * Gets the value of the race property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType.RaceEnum }
     *     
     */
    public DescriptionType.RaceEnum getRace() {
        return race;
    }

    /**
     * Sets the value of the race property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType.RaceEnum }
     *     
     */
    public void setRace(DescriptionType.RaceEnum value) {
        this.race = value;
    }

    /**
     * Gets the value of the sex property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType.SexEnum }
     *     
     */
    public DescriptionType.SexEnum getSex() {
        return sex;
    }

    /**
     * Sets the value of the sex property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType.SexEnum }
     *     
     */
    public void setSex(DescriptionType.SexEnum value) {
        this.sex = value;
    }

    /**
     * Gets the value of the height property.
     * 
     * @return
     *     possible object is
     *     {@link HeightType }
     *     
     */
    public HeightType getHeight() {
        return height;
    }

    /**
     * Sets the value of the height property.
     * 
     * @param value
     *     allowed object is
     *     {@link HeightType }
     *     
     */
    public void setHeight(HeightType value) {
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
    public Integer getWeight() {
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
    public void setWeight(Integer value) {
        this.weight = value;
    }

    /**
     * Gets the value of the eyecolor property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType.EyeColorEnum }
     *     
     */
    public DescriptionType.EyeColorEnum getEyecolor() {
        return eyecolor;
    }

    /**
     * Sets the value of the eyecolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType.EyeColorEnum }
     *     
     */
    public void setEyecolor(DescriptionType.EyeColorEnum value) {
        this.eyecolor = value;
    }

    /**
     * Gets the value of the haircolor property.
     * 
     * @return
     *     possible object is
     *     {@link DescriptionType.HairColorEnum }
     *     
     */
    public DescriptionType.HairColorEnum getHaircolor() {
        return haircolor;
    }

    /**
     * Sets the value of the haircolor property.
     * 
     * @param value
     *     allowed object is
     *     {@link DescriptionType.HairColorEnum }
     *     
     */
    public void setHaircolor(DescriptionType.HairColorEnum value) {
        this.haircolor = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Black"/>
     *     &lt;enumeration value="Blue"/>
     *     &lt;enumeration value="Brown"/>
     *     &lt;enumeration value="Green"/>
     *     &lt;enumeration value="Grey"/>
     *     &lt;enumeration value="Hazel"/>
     *     &lt;enumeration value="Maroon"/>
     *     &lt;enumeration value="Multi-Colored"/>
     *     &lt;enumeration value="Pink"/>
     *     &lt;enumeration value="Unknown"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum EyeColorEnum {

        @XmlEnumValue("Black")
        BLACK("Black"),
        @XmlEnumValue("Blue")
        BLUE("Blue"),
        @XmlEnumValue("Brown")
        BROWN("Brown"),
        @XmlEnumValue("Green")
        GREEN("Green"),
        @XmlEnumValue("Grey")
        GREY("Grey"),
        @XmlEnumValue("Hazel")
        HAZEL("Hazel"),
        @XmlEnumValue("Maroon")
        MAROON("Maroon"),
        @XmlEnumValue("Multi-Colored")
        MULTI_COLORED("Multi-Colored"),
        @XmlEnumValue("Pink")
        PINK("Pink"),
        @XmlEnumValue("Unknown")
        UNKNOWN("Unknown");
        private final String value;

        EyeColorEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DescriptionType.EyeColorEnum fromValue(String v) {
            for (DescriptionType.EyeColorEnum c: DescriptionType.EyeColorEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="Black"/>
     *     &lt;enumeration value="Blond"/>
     *     &lt;enumeration value="Brown"/>
     *     &lt;enumeration value="Grey"/>
     *     &lt;enumeration value="Red"/>
     *     &lt;enumeration value="Sandy"/>
     *     &lt;enumeration value="White"/>
     *     &lt;enumeration value="Unknown"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum HairColorEnum {

        @XmlEnumValue("Black")
        BLACK("Black"),
        @XmlEnumValue("Blond")
        BLOND("Blond"),
        @XmlEnumValue("Brown")
        BROWN("Brown"),
        @XmlEnumValue("Grey")
        GREY("Grey"),
        @XmlEnumValue("Red")
        RED("Red"),
        @XmlEnumValue("Sandy")
        SANDY("Sandy"),
        @XmlEnumValue("White")
        WHITE("White"),
        @XmlEnumValue("Unknown")
        UNKNOWN("Unknown");
        private final String value;

        HairColorEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DescriptionType.HairColorEnum fromValue(String v) {
            for (DescriptionType.HairColorEnum c: DescriptionType.HairColorEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="African American"/>
     *     &lt;enumeration value="Alaska Native"/>
     *     &lt;enumeration value="American Indian"/>
     *     &lt;enumeration value="Asian"/>
     *     &lt;enumeration value="Black"/>
     *     &lt;enumeration value="Hispanic"/>
     *     &lt;enumeration value="Native Hawaiian"/>
     *     &lt;enumeration value="Pacific Islander"/>
     *     &lt;enumeration value="White"/>
     *     &lt;enumeration value="Multi Racial"/>
     *     &lt;enumeration value="Unknown"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum RaceEnum {

        @XmlEnumValue("African American")
        AFRICAN_AMERICAN("African American"),
        @XmlEnumValue("Alaska Native")
        ALASKA_NATIVE("Alaska Native"),
        @XmlEnumValue("American Indian")
        AMERICAN_INDIAN("American Indian"),
        @XmlEnumValue("Asian")
        ASIAN("Asian"),
        @XmlEnumValue("Black")
        BLACK("Black"),
        @XmlEnumValue("Hispanic")
        HISPANIC("Hispanic"),
        @XmlEnumValue("Native Hawaiian")
        NATIVE_HAWAIIAN("Native Hawaiian"),
        @XmlEnumValue("Pacific Islander")
        PACIFIC_ISLANDER("Pacific Islander"),
        @XmlEnumValue("White")
        WHITE("White"),
        @XmlEnumValue("Multi Racial")
        MULTI_RACIAL("Multi Racial"),
        @XmlEnumValue("Unknown")
        UNKNOWN("Unknown");
        private final String value;

        RaceEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DescriptionType.RaceEnum fromValue(String v) {
            for (DescriptionType.RaceEnum c: DescriptionType.RaceEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

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
    public enum SexEnum {

        @XmlEnumValue("Female")
        FEMALE("Female"),
        @XmlEnumValue("Male")
        MALE("Male"),
        @XmlEnumValue("Unknown")
        UNKNOWN("Unknown");
        private final String value;

        SexEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DescriptionType.SexEnum fromValue(String v) {
            for (DescriptionType.SexEnum c: DescriptionType.SexEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
