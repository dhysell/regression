
package services.services.com.idfbins.claimservices.guidewire.ab.typekey;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ClaimVendorType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ClaimVendorType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="aplus"/>
 *     &lt;enumeration value="attorney"/>
 *     &lt;enumeration value="autotowagency"/>
 *     &lt;enumeration value="autorepairshop"/>
 *     &lt;enumeration value="clue"/>
 *     &lt;enumeration value="dba_autotowagency"/>
 *     &lt;enumeration value="dba_autorepairshop"/>
 *     &lt;enumeration value="dba_salvagevendor"/>
 *     &lt;enumeration value="deptoftrans"/>
 *     &lt;enumeration value="doctor"/>
 *     &lt;enumeration value="lawfirm"/>
 *     &lt;enumeration value="mcas"/>
 *     &lt;enumeration value="medicalcareorg"/>
 *     &lt;enumeration value="cms"/>
 *     &lt;enumeration value="other"/>
 *     &lt;enumeration value="salvagevendor"/>
 *     &lt;enumeration value="drprepairshop"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ClaimVendorType", namespace = "http://guidewire.com/ab/typekey")
@XmlEnum
public enum ClaimVendorType {

    @XmlEnumValue("aplus")
    APLUS("aplus"),
    @XmlEnumValue("attorney")
    ATTORNEY("attorney"),
    @XmlEnumValue("autotowagency")
    AUTOTOWAGENCY("autotowagency"),
    @XmlEnumValue("autorepairshop")
    AUTOREPAIRSHOP("autorepairshop"),
    @XmlEnumValue("clue")
    CLUE("clue"),
    @XmlEnumValue("dba_autotowagency")
    DBA_AUTOTOWAGENCY("dba_autotowagency"),
    @XmlEnumValue("dba_autorepairshop")
    DBA_AUTOREPAIRSHOP("dba_autorepairshop"),
    @XmlEnumValue("dba_salvagevendor")
    DBA_SALVAGEVENDOR("dba_salvagevendor"),
    @XmlEnumValue("deptoftrans")
    DEPTOFTRANS("deptoftrans"),
    @XmlEnumValue("doctor")
    DOCTOR("doctor"),
    @XmlEnumValue("lawfirm")
    LAWFIRM("lawfirm"),
    @XmlEnumValue("mcas")
    MCAS("mcas"),
    @XmlEnumValue("medicalcareorg")
    MEDICALCAREORG("medicalcareorg"),
    @XmlEnumValue("cms")
    CMS("cms"),
    @XmlEnumValue("other")
    OTHER("other"),
    @XmlEnumValue("salvagevendor")
    SALVAGEVENDOR("salvagevendor"),
    @XmlEnumValue("drprepairshop")
    DRPREPAIRSHOP("drprepairshop");
    private final String value;

    ClaimVendorType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ClaimVendorType fromValue(String v) {
        for (ClaimVendorType c: ClaimVendorType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
