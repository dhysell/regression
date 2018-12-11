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
 *     &lt;enumeration value="Built-in Refrigerator"/>
 *     &lt;enumeration value="Allowance"/>
 *     &lt;enumeration value="Dishwasher"/>
 *     &lt;enumeration value="Garbage Disposal"/>
 *     &lt;enumeration value="Ovens - Electric"/>
 *     &lt;enumeration value="Ovens - Gas"/>
 *     &lt;enumeration value="Range Hoods"/>
 *     &lt;enumeration value="Stove Tops - Gas/Electric"/>
 *     &lt;enumeration value="Stoves - Electric "/>
 *     &lt;enumeration value="Stoves - Gas"/>
 *     &lt;enumeration value="Trash Compactors"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum AppliancesBuiltinMaterialTypeEnum {

    @XmlEnumValue("Built-in Refrigerator")
    BUILT_IN_REFRIGERATOR("Built-in Refrigerator"),
    @XmlEnumValue("Allowance")
    ALLOWANCE("Allowance"),
    @XmlEnumValue("Dishwasher")
    DISHWASHER("Dishwasher"),
    @XmlEnumValue("Garbage Disposal")
    GARBAGE_DISPOSAL("Garbage Disposal"),
    @XmlEnumValue("Ovens - Electric")
    OVENS_ELECTRIC("Ovens - Electric"),
    @XmlEnumValue("Ovens - Gas")
    OVENS_GAS("Ovens - Gas"),
    @XmlEnumValue("Range Hoods")
    RANGE_HOODS("Range Hoods"),
    @XmlEnumValue("Stove Tops - Gas/Electric")
    STOVE_TOPS_GAS_ELECTRIC("Stove Tops - Gas/Electric"),
    @XmlEnumValue("Stoves - Electric ")
    STOVES_ELECTRIC("Stoves - Electric "),
    @XmlEnumValue("Stoves - Gas")
    STOVES_GAS("Stoves - Gas"),
    @XmlEnumValue("Trash Compactors")
    TRASH_COMPACTORS("Trash Compactors"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    AppliancesBuiltinMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AppliancesBuiltinMaterialTypeEnum fromValue(String v) {
        for (AppliancesBuiltinMaterialTypeEnum c: AppliancesBuiltinMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
