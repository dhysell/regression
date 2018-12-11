//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for driversLicenseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="driversLicenseType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://cp.com/rules/client}parameter" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="history">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="current"/>
 *             &lt;enumeration value="prior"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="type">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="commercial"/>
 *             &lt;enumeration value="personal"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="state">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;length value="2"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="number" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="driving_experience_length" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="status" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "driversLicenseType", propOrder = {
    "parameterList"
})
@XmlSeeAlso({
    ClueDriversLicenseType.class
})
public class DriversLicenseType {

    @XmlElement(name = "parameter")
    protected List<ParameterType> parameterList;
    @XmlAttribute(name = "history")
    protected DriversLicenseType.DriversLicenseHistoryEnum history;
    @XmlAttribute(name = "type")
    protected DriversLicenseType.DriversLicenseTypeEnum type;
    @XmlAttribute(name = "state")
    protected String state;
    @XmlAttribute(name = "number")
    protected String number;
    @XmlAttribute(name = "driving_experience_length")
    protected Integer drivingExperienceLength;
    @XmlAttribute(name = "status")
    protected String status;

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
     * {@link broker.objects.lexisnexis.clueauto.response.actual.ParameterType }
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
     * Gets the value of the history property.
     * 
     * @return
     *     possible object is
     *     {@link DriversLicenseType.DriversLicenseHistoryEnum }
     *     
     */
    public DriversLicenseType.DriversLicenseHistoryEnum getHistory() {
        return history;
    }

    /**
     * Sets the value of the history property.
     * 
     * @param value
     *     allowed object is
     *     {@link DriversLicenseType.DriversLicenseHistoryEnum }
     *     
     */
    public void setHistory(DriversLicenseType.DriversLicenseHistoryEnum value) {
        this.history = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link DriversLicenseType.DriversLicenseTypeEnum }
     *     
     */
    public DriversLicenseType.DriversLicenseTypeEnum getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link DriversLicenseType.DriversLicenseTypeEnum }
     *     
     */
    public void setType(DriversLicenseType.DriversLicenseTypeEnum value) {
        this.type = value;
    }

    /**
     * Gets the value of the state property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the value of the state property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setState(String value) {
        this.state = value;
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
     * Gets the value of the drivingExperienceLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDrivingExperienceLength() {
        return drivingExperienceLength;
    }

    /**
     * Sets the value of the drivingExperienceLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDrivingExperienceLength(Integer value) {
        this.drivingExperienceLength = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatus(String value) {
        this.status = value;
    }


    /**
     * <p>Java class for null.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * <p>
     * <pre>
     * &lt;simpleType>
     *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *     &lt;enumeration value="current"/>
     *     &lt;enumeration value="prior"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum DriversLicenseHistoryEnum {

        @XmlEnumValue("current")
        CURRENT("current"),
        @XmlEnumValue("prior")
        PRIOR("prior");
        private final String value;

        DriversLicenseHistoryEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DriversLicenseType.DriversLicenseHistoryEnum fromValue(String v) {
            for (DriversLicenseType.DriversLicenseHistoryEnum c: DriversLicenseType.DriversLicenseHistoryEnum.values()) {
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
     *     &lt;enumeration value="commercial"/>
     *     &lt;enumeration value="personal"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum DriversLicenseTypeEnum {

        @XmlEnumValue("commercial")
        COMMERCIAL("commercial"),
        @XmlEnumValue("personal")
        PERSONAL("personal");
        private final String value;

        DriversLicenseTypeEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static DriversLicenseType.DriversLicenseTypeEnum fromValue(String v) {
            for (DriversLicenseType.DriversLicenseTypeEnum c: DriversLicenseType.DriversLicenseTypeEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
