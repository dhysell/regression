//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.01.04 at 12:05:23 PM MST 
//


package services.broker.objects.lexisnexis.prefill.personalauto.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dataprefill_admin_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dataprefill_admin_type">
 *   &lt;complexContent>
 *     &lt;extension base="{http://cp.com/rules/client}commonAdminType">
 *       &lt;sequence>
 *         &lt;element name="CC_DriverDiscovery_admin" type="{http://cp.com/rules/client}dataprefillAdminSubGroup" minOccurs="0"/>
 *         &lt;element name="vin_admin" type="{http://cp.com/rules/client}dataprefillAdminSubGroup" minOccurs="0"/>
 *         &lt;element name="vmr_admin" type="{http://cp.com/rules/client}dataprefillAdminSubGroup" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dataprefill_admin_type", propOrder = {
    "ccDriverDiscoveryAdmin",
    "vinAdmin",
    "vmrAdmin"
})
public class DataprefillAdminType
    extends CommonAdminType
{

    @XmlElement(name = "CC_DriverDiscovery_admin")
    protected DataprefillAdminSubGroup ccDriverDiscoveryAdmin;
    @XmlElement(name = "vin_admin")
    protected DataprefillAdminSubGroup vinAdmin;
    @XmlElement(name = "vmr_admin")
    protected DataprefillAdminSubGroup vmrAdmin;

    /**
     * Gets the value of the ccDriverDiscoveryAdmin property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillAdminSubGroup }
     *     
     */
    public DataprefillAdminSubGroup getCCDriverDiscoveryAdmin() {
        return ccDriverDiscoveryAdmin;
    }

    /**
     * Sets the value of the ccDriverDiscoveryAdmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillAdminSubGroup }
     *     
     */
    public void setCCDriverDiscoveryAdmin(DataprefillAdminSubGroup value) {
        this.ccDriverDiscoveryAdmin = value;
    }

    /**
     * Gets the value of the vinAdmin property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillAdminSubGroup }
     *     
     */
    public DataprefillAdminSubGroup getVinAdmin() {
        return vinAdmin;
    }

    /**
     * Sets the value of the vinAdmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillAdminSubGroup }
     *     
     */
    public void setVinAdmin(DataprefillAdminSubGroup value) {
        this.vinAdmin = value;
    }

    /**
     * Gets the value of the vmrAdmin property.
     * 
     * @return
     *     possible object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillAdminSubGroup }
     *     
     */
    public DataprefillAdminSubGroup getVmrAdmin() {
        return vmrAdmin;
    }

    /**
     * Sets the value of the vmrAdmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link broker.objects.lexisnexis.prefill.personalauto.response.actual.DataprefillAdminSubGroup }
     *     
     */
    public void setVmrAdmin(DataprefillAdminSubGroup value) {
        this.vmrAdmin = value;
    }

}
