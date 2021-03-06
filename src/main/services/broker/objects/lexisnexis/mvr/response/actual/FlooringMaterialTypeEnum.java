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
 *     &lt;enumeration value="Ceramic Tile"/>
 *     &lt;enumeration value="Laminate Hardboard"/>
 *     &lt;enumeration value="Granite Tile"/>
 *     &lt;enumeration value="Hardwood Parquet"/>
 *     &lt;enumeration value="Hardwood Solid Oak"/>
 *     &lt;enumeration value="Hardwood Veneer"/>
 *     &lt;enumeration value="Linoleum"/>
 *     &lt;enumeration value="Linoleum - Tile"/>
 *     &lt;enumeration value="Marble"/>
 *     &lt;enumeration value="Terrazzo"/>
 *     &lt;enumeration value="Carpet"/>
 *     &lt;enumeration value="Stone Tile"/>
 *     &lt;enumeration value="Pine Flooring"/>
 *     &lt;enumeration value="Hardwood Exotic"/>
 *     &lt;enumeration value="Flooring - Allowance"/>
 *     &lt;enumeration value="Unknown"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "")
@XmlEnum
public enum FlooringMaterialTypeEnum {

    @XmlEnumValue("Ceramic Tile")
    CERAMIC_TILE("Ceramic Tile"),
    @XmlEnumValue("Laminate Hardboard")
    LAMINATE_HARDBOARD("Laminate Hardboard"),
    @XmlEnumValue("Granite Tile")
    GRANITE_TILE("Granite Tile"),
    @XmlEnumValue("Hardwood Parquet")
    HARDWOOD_PARQUET("Hardwood Parquet"),
    @XmlEnumValue("Hardwood Solid Oak")
    HARDWOOD_SOLID_OAK("Hardwood Solid Oak"),
    @XmlEnumValue("Hardwood Veneer")
    HARDWOOD_VENEER("Hardwood Veneer"),
    @XmlEnumValue("Linoleum")
    LINOLEUM("Linoleum"),
    @XmlEnumValue("Linoleum - Tile")
    LINOLEUM_TILE("Linoleum - Tile"),
    @XmlEnumValue("Marble")
    MARBLE("Marble"),
    @XmlEnumValue("Terrazzo")
    TERRAZZO("Terrazzo"),
    @XmlEnumValue("Carpet")
    CARPET("Carpet"),
    @XmlEnumValue("Stone Tile")
    STONE_TILE("Stone Tile"),
    @XmlEnumValue("Pine Flooring")
    PINE_FLOORING("Pine Flooring"),
    @XmlEnumValue("Hardwood Exotic")
    HARDWOOD_EXOTIC("Hardwood Exotic"),
    @XmlEnumValue("Flooring - Allowance")
    FLOORING_ALLOWANCE("Flooring - Allowance"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown");
    private final String value;

    FlooringMaterialTypeEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static FlooringMaterialTypeEnum fromValue(String v) {
        for (FlooringMaterialTypeEnum c: FlooringMaterialTypeEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
