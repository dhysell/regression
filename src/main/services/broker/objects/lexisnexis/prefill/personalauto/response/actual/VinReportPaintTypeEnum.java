//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vin_report_paint_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="vin_report_paint_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VIN Services PAINT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "vin_report_paint_type")
@XmlEnum
public enum VinReportPaintTypeEnum {

    @XmlEnumValue("VIN Services PAINT")
    VIN_SERVICES_PAINT("VIN Services PAINT");
    private final String value;

    VinReportPaintTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VinReportPaintTypeEnum fromValue(String v) {
        for (VinReportPaintTypeEnum c: VinReportPaintTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}