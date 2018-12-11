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
 * <p>Java class for null.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType>
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Porch - Wood"/>
 *     &lt;enumeration value="Porch - Slab"/>
 *     &lt;enumeration value="Add for Enclosure"/>
 *     &lt;enumeration value="Porch - Brick"/>
 *     &lt;enumeration value="Porch - Stone / Pavers"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum PorchMaterialTypeEnum {

    @XmlEnumValue("Porch - Wood")
    PORCH_WOOD("Porch - Wood"),
    @XmlEnumValue("Porch - Slab")
    PORCH_SLAB("Porch - Slab"),
    @XmlEnumValue("Add for Enclosure")
    ADD_FOR_ENCLOSURE("Add for Enclosure"),
    @XmlEnumValue("Porch - Brick")
    PORCH_BRICK("Porch - Brick"),
    @XmlEnumValue("Porch - Stone / Pavers")
    PORCH_STONE_PAVERS("Porch - Stone / Pavers"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    PorchMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static PorchMaterialTypeEnum fromValue(String v) {
        for (PorchMaterialTypeEnum c: PorchMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
