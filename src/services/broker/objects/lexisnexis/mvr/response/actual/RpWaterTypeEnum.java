//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.10.19 at 04:26:02 PM MDT 
//


package services.broker.objects.lexisnexis.mvr.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for null.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType>
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Cistern"/>
 *     &lt;enumeration value="Yes"/>
 *     &lt;enumeration value="Municipal"/>
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Well"/>
 *     &lt;enumeration value="Spring/Creek"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum RpWaterTypeEnum {

    @XmlEnumValue("Cistern")
    CISTERN("Cistern"),
    @XmlEnumValue("Yes")
    YES("Yes"),
    @XmlEnumValue("Municipal")
    MUNICIPAL("Municipal"),
    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Well")
    WELL("Well"),
    @XmlEnumValue("Spring/Creek")
    SPRING_CREEK("Spring/Creek"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    RpWaterTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RpWaterTypeEnum fromValue(String v) {
        for (RpWaterTypeEnum c: RpWaterTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
