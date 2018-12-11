//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for policyHolderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="policyHolderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="relationship" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="primary"/>
 *             &lt;enumeration value="secondary"/>
 *             &lt;enumeration value="excluded"/>
 *             &lt;enumeration value="employee"/>
 *             &lt;enumeration value="listed"/>
 *             &lt;enumeration value="other"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="subject" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "policyHolderType")
public class PolicyHolderType {

    @XmlAttribute(name = "relationship", required = true)
    protected PolicyHolderType.PolicyRelationshipEnum relationship;
    @XmlAttribute(name = "subject", required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object subject;

    /**
     * Gets the value of the relationship property.
     * 
     * @return
     *     possible object is
     *     {@link PolicyHolderType.PolicyRelationshipEnum }
     *     
     */
    public PolicyHolderType.PolicyRelationshipEnum getRelationship() {
        return relationship;
    }

    /**
     * Sets the value of the relationship property.
     * 
     * @param value
     *     allowed object is
     *     {@link PolicyHolderType.PolicyRelationshipEnum }
     *     
     */
    public void setRelationship(PolicyHolderType.PolicyRelationshipEnum value) {
        this.relationship = value;
    }

    /**
     * Gets the value of the subject property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getSubject() {
        return subject;
    }

    /**
     * Sets the value of the subject property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setSubject(Object value) {
        this.subject = value;
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
     *     &lt;enumeration value="secondary"/>
     *     &lt;enumeration value="excluded"/>
     *     &lt;enumeration value="employee"/>
     *     &lt;enumeration value="listed"/>
     *     &lt;enumeration value="other"/>
     *   &lt;/restriction>
     * &lt;/simpleType>
     * </pre>
     * 
     */
    @XmlType(name = "")
    @XmlEnum
    public enum PolicyRelationshipEnum {

        @XmlEnumValue("primary")
        PRIMARY("primary"),
        @XmlEnumValue("secondary")
        SECONDARY("secondary"),
        @XmlEnumValue("excluded")
        EXCLUDED("excluded"),
        @XmlEnumValue("employee")
        EMPLOYEE("employee"),
        @XmlEnumValue("listed")
        LISTED("listed"),
        @XmlEnumValue("other")
        OTHER("other");
        private final String value;

        PolicyRelationshipEnum(String v) {
            value = v;
        }

        public String value() {
            return value;
        }

        public static PolicyHolderType.PolicyRelationshipEnum fromValue(String v) {
            for (PolicyHolderType.PolicyRelationshipEnum c: PolicyHolderType.PolicyRelationshipEnum.values()) {
                if (c.value.equals(v)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(v);
        }

    }

}
