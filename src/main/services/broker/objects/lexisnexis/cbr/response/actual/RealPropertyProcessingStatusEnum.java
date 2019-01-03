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
 * <p>Java class for realPropertyProcessingStatusType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="realPropertyProcessingStatusType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="processing complete with Real Property results"/>
 *     &lt;enumeration value="processing complete, no Real Property results"/>
 *     &lt;enumeration value="not processed, product not requested"/>
 *     &lt;enumeration value="not processed, product unavailable"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "realPropertyProcessingStatusType")
@XmlEnum
public enum RealPropertyProcessingStatusEnum {

    @XmlEnumValue("processing complete with Real Property results")
    PROCESSING_COMPLETE_WITH_REAL_PROPERTY_RESULTS("processing complete with Real Property results"),
    @XmlEnumValue("processing complete, no Real Property results")
    PROCESSING_COMPLETE_NO_REAL_PROPERTY_RESULTS("processing complete, no Real Property results"),
    @XmlEnumValue("not processed, product not requested")
    NOT_PROCESSED_PRODUCT_NOT_REQUESTED("not processed, product not requested"),
    @XmlEnumValue("not processed, product unavailable")
    NOT_PROCESSED_PRODUCT_UNAVAILABLE("not processed, product unavailable");
    private final String value;

    RealPropertyProcessingStatusEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static RealPropertyProcessingStatusEnum fromValue(String v) {
        for (RealPropertyProcessingStatusEnum c: RealPropertyProcessingStatusEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}