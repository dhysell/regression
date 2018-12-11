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
 * <p>Java class for mvr_source_origin.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="mvr_source_origin">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Department of Motor Vehicles"/>
 *     &lt;enumeration value="Driver History Database"/>
 *     &lt;enumeration value="Duplicate Order Check File"/>
 *     &lt;enumeration value="Database File"/>
 *     &lt;enumeration value="Precycle"/>
 *     &lt;enumeration value="Recycle"/>
 *     &lt;enumeration value="DHDB 2.0 Batch"/>
 *     &lt;enumeration value="DHDB 2.0 Interactive"/>
 *     &lt;enumeration value="REN DHDB 2.0 Batch"/>
 *     &lt;enumeration value="REN DHDB 2.0 Interactive"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "mvr_source_origin")
@XmlEnum
public enum MvrSourceOriginEnum {

    @XmlEnumValue("Department of Motor Vehicles")
    DEPARTMENT_OF_MOTOR_VEHICLES("Department of Motor Vehicles"),
    @XmlEnumValue("Driver History Database")
    DRIVER_HISTORY_DATABASE("Driver History Database"),
    @XmlEnumValue("Duplicate Order Check File")
    DUPLICATE_ORDER_CHECK_FILE("Duplicate Order Check File"),
    @XmlEnumValue("Database File")
    DATABASE_FILE("Database File"),
    @XmlEnumValue("Precycle")
    PRECYCLE("Precycle"),
    @XmlEnumValue("Recycle")
    RECYCLE("Recycle"),
    @XmlEnumValue("DHDB 2.0 Batch")
    DHDB_2_0_BATCH("DHDB 2.0 Batch"),
    @XmlEnumValue("DHDB 2.0 Interactive")
    DHDB_2_0_INTERACTIVE("DHDB 2.0 Interactive"),
    @XmlEnumValue("REN DHDB 2.0 Batch")
    REN_DHDB_2_0_BATCH("REN DHDB 2.0 Batch"),
    @XmlEnumValue("REN DHDB 2.0 Interactive")
    REN_DHDB_2_0_INTERACTIVE("REN DHDB 2.0 Interactive"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    MvrSourceOriginEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MvrSourceOriginEnum fromValue(String v) {
        for (MvrSourceOriginEnum c: MvrSourceOriginEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}