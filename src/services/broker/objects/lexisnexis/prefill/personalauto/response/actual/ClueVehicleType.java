//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for clueVehicleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clueVehicleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="model_year" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger">
 *               &lt;maxInclusive value="2100"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="model_make" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vin" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="disposition" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="No collision compensation"/>
 *               &lt;enumeration value="Body damage / broken glass"/>
 *               &lt;enumeration value="Repaired"/>
 *               &lt;enumeration value="Stolen"/>
 *               &lt;enumeration value="Totaled"/>
 *               &lt;enumeration value="Damaged Other"/>
 *               &lt;enumeration value="Not reported"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="fsi_model_year" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_model_make" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *         &lt;element name="fsi_vin" type="{http://cp.com/rules/client}fsiType" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="unit_number" type="{http://www.w3.org/2001/XMLSchema}nonNegativeInteger" />
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *       &lt;attribute name="quoteback" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clueVehicleType", propOrder = {
    "modelYear",
    "modelMake",
    "vin",
    "disposition",
    "fsiModelYear",
    "fsiModelMake",
    "fsiVin"
})
public class ClueVehicleType {

    @XmlElement(name = "model_year", type = String.class)
    @XmlJavaTypeAdapter(Adapter16 .class)
    protected Integer modelYear;
    @XmlElement(name = "model_make")
    protected String modelMake;
    protected String vin;
    protected ClueVehicleType.AutoDispositionEnum disposition;
    @XmlElement(name = "fsi_model_year")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiModelYear;
    @XmlElement(name = "fsi_model_make")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiModelMake;
    @XmlElement(name = "fsi_vin")
    @XmlSchemaType(name = "string")
    protected FSIEnum fsiVin;
    @XmlAttribute(name = "unit_number")
    @XmlSchemaType(name = "nonNegativeInteger")
    protected BigInteger unitNumber;
    @XmlAttribute(name = "id")
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object id;
    @XmlAttribute(name = "quoteback")
    protected String quoteback;

    /**
     * Gets the value of the modelYear property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Integer getModelYear() {
        return modelYear;
    }

    /**
     * Sets the value of the modelYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelYear(Integer value) {
        this.modelYear = value;
    }

    /**
     * Gets the value of the modelMake property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getModelMake() {
        return modelMake;
    }

    /**
     * Sets the value of the modelMake property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setModelMake(String value) {
        this.modelMake = value;
    }

    /**
     * Gets the value of the vin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVin() {
        return vin;
    }

    /**
     * Sets the value of the vin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVin(String value) {
        this.vin = value;
    }

    /**
     * Gets the value of the disposition property.
     * 
     * @return
     *     possible object is
     *     {@link ClueVehicleType.AutoDispositionEnum }
     *     
     */
    public ClueVehicleType.AutoDispositionEnum getDisposition() {
        return disposition;
    }

    /**
     * Sets the value of the disposition property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClueVehicleType.AutoDispositionEnum }
     *     
     */
    public void setDisposition(ClueVehicleType.AutoDispositionEnum value) {
        this.disposition = value;
    }

    /**
     * Gets the value of the fsiModelYear property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiModelYear() {
        return fsiModelYear;
    }

    /**
     * Sets the value of the fsiModelYear property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiModelYear(FSIEnum value) {
        this.fsiModelYear = value;
    }

    /**
     * Gets the value of the fsiModelMake property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiModelMake() {
        return fsiModelMake;
    }

    /**
     * Sets the value of the fsiModelMake property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiModelMake(FSIEnum value) {
        this.fsiModelMake = value;
    }

    /**
     * Gets the value of the fsiVin property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiVin() {
        return fsiVin;
    }

    /**
     * Sets the value of the fsiVin property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiVin(FSIEnum value) {
        this.fsiVin = value;
    }

    /**
     * Gets the value of the unitNumber property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getUnitNumber() {
        return unitNumber;
    }

    /**
     * Sets the value of the unitNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setUnitNumber(BigInteger value) {
        this.unitNumber = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setId(Object value) {
        this.id = value;
    }

    /**
     * Gets the value of the quoteback property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuoteback() {
        return quoteback;
    }

    /**
     * Sets the value of the quoteback property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuoteback(String value) {
        this.quoteback = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="No collision compensation"/>
     *     &lt;enumeration value="Body damage / broken glass"/>
     *     &lt;enumeration value="Repaired"/>
     *     &lt;enumeration value="Stolen"/>
     *     &lt;enumeration value="Totaled"/>
     *     &lt;enumeration value="Damaged Other"/>
     *     &lt;enumeration value="Not reported"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum AutoDispositionEnum {

        @XmlEnumValue("No collision compensation")
        NO_COLLISION_COMPENSATION("No collision compensation"),
        @XmlEnumValue("Body damage / broken glass")
        BODY_DAMAGE_BROKEN_GLASS("Body damage / broken glass"),
        @XmlEnumValue("Repaired")
        REPAIRED("Repaired"),
        @XmlEnumValue("Stolen")
        STOLEN("Stolen"),
        @XmlEnumValue("Totaled")
        TOTALED("Totaled"),
        @XmlEnumValue("Damaged Other")
        DAMAGED_OTHER("Damaged Other"),
        @XmlEnumValue("Not reported")
        NOT_REPORTED("Not reported");
        private final String value;

        AutoDispositionEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static ClueVehicleType.AutoDispositionEnum fromValue(String v) {
            for (ClueVehicleType.AutoDispositionEnum c: ClueVehicleType.AutoDispositionEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
