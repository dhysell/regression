//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cluePropertyPolicyType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cluePropertyPolicyType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Boat Owners"/>
 *     &lt;enumeration value="Condominium"/>
 *     &lt;enumeration value="Fire"/>
 *     &lt;enumeration value="Flood"/>
 *     &lt;enumeration value="Homeowners"/>
 *     &lt;enumeration value="Inland Marine"/>
 *     &lt;enumeration value="Personal Umbrella"/>
 *     &lt;enumeration value="Mobile Home"/>
 *     &lt;enumeration value="Earthquake"/>
 *     &lt;enumeration value="Ranch/Farm"/>
 *     &lt;enumeration value="Tenant"/>
 *     &lt;enumeration value="Other (Scheduled property, etc.)"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cluePropertyPolicyType")
@XmlEnum
public enum PropertyPolicyTypeEnum {

    @XmlEnumValue("Boat Owners")
    BOAT_OWNERS("Boat Owners"),
    @XmlEnumValue("Condominium")
    CONDOMINIUM("Condominium"),
    @XmlEnumValue("Fire")
    FIRE("Fire"),
    @XmlEnumValue("Flood")
    FLOOD("Flood"),
    @XmlEnumValue("Homeowners")
    HOMEOWNERS("Homeowners"),
    @XmlEnumValue("Inland Marine")
    INLAND_MARINE("Inland Marine"),
    @XmlEnumValue("Personal Umbrella")
    PERSONAL_UMBRELLA("Personal Umbrella"),
    @XmlEnumValue("Mobile Home")
    MOBILE_HOME("Mobile Home"),
    @XmlEnumValue("Earthquake")
    EARTHQUAKE("Earthquake"),
    @XmlEnumValue("Ranch/Farm")
    RANCH_FARM("Ranch/Farm"),
    @XmlEnumValue("Tenant")
    TENANT("Tenant"),
    @XmlEnumValue("Other (Scheduled property, etc.)")
    OTHER_SCHEDULED_PROPERTY_ETC("Other (Scheduled property, etc.)");
    private final String value;

    PropertyPolicyTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PropertyPolicyTypeEnum fromValue(String v) {
        for (PropertyPolicyTypeEnum c: PropertyPolicyTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
