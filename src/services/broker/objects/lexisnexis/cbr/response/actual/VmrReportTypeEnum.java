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
 * <p>Java class for vmrReportType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vmrReportType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Vin search"/>
 *     &lt;enumeration value="License Plate search"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "vmrReportType")
@XmlEnum
public enum VmrReportTypeEnum {

    @XmlEnumValue("Vin search")
    VIN_SEARCH("Vin search"),
    @XmlEnumValue("License Plate search")
    LICENSE_PLATE_SEARCH("License Plate search");
    private final String value;

    VmrReportTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VmrReportTypeEnum fromValue(String v) {
        for (VmrReportTypeEnum c: VmrReportTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
