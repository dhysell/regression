//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 01:50:47 PM MST 
//


package services.broker.objects.lexisnexis.cbr.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for score_status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="score_status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Scored"/>
 *     &lt;enumeration value="No Score"/>
 *     &lt;enumeration value="No Hit"/>
 *     &lt;enumeration value="No Result"/>
 *     &lt;enumeration value="Error"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "score_status")
@XmlEnum
public enum ScoreStatusEnum {

    @XmlEnumValue("Scored")
    SCORED("Scored"),
    @XmlEnumValue("No Score")
    NO_SCORE("No Score"),
    @XmlEnumValue("No Hit")
    NO_HIT("No Hit"),
    @XmlEnumValue("No Result")
    NO_RESULT("No Result"),
    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    ScoreStatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ScoreStatusEnum fromValue(String v) {
        for (ScoreStatusEnum c: ScoreStatusEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
