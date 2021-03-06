//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.16 at 04:49:21 PM MST 
//


package services.broker.objects.lexisnexis.clueauto.response.actual;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for clueTelephoneType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="clueTelephoneType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://cp.com/rules/client>telephoneType">
 *       &lt;attribute name="fsi_area" type="{http://cp.com/rules/client}fsiType" />
 *       &lt;attribute name="fsi_exchange" type="{http://cp.com/rules/client}fsiType" />
 *       &lt;attribute name="fsi_number" type="{http://cp.com/rules/client}fsiType" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clueTelephoneType")
public class ClueTelephoneType
    extends TelephoneType
{

    @XmlAttribute(name = "fsi_area")
    protected FSIEnum fsiArea;
    @XmlAttribute(name = "fsi_exchange")
    protected FSIEnum fsiExchange;
    @XmlAttribute(name = "fsi_number")
    protected FSIEnum fsiNumber;

    /**
     * Gets the value of the fsiArea property.
     * 
     * @return
     *     possible object is
     *     {@link services.broker.objects.lexisnexis.clueauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiArea() {
        return fsiArea;
    }

    /**
     * Sets the value of the fsiArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.broker.objects.lexisnexis.clueauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiArea(FSIEnum value) {
        this.fsiArea = value;
    }

    /**
     * Gets the value of the fsiExchange property.
     * 
     * @return
     *     possible object is
     *     {@link services.broker.objects.lexisnexis.clueauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiExchange() {
        return fsiExchange;
    }

    /**
     * Sets the value of the fsiExchange property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.broker.objects.lexisnexis.clueauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiExchange(FSIEnum value) {
        this.fsiExchange = value;
    }

    /**
     * Gets the value of the fsiNumber property.
     * 
     * @return
     *     possible object is
     *     {@link services.broker.objects.lexisnexis.clueauto.response.actual.FSIEnum }
     *     
     */
    public FSIEnum getFsiNumber() {
        return fsiNumber;
    }

    /**
     * Sets the value of the fsiNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link services.broker.objects.lexisnexis.clueauto.response.actual.FSIEnum }
     *     
     */
    public void setFsiNumber(FSIEnum value) {
        this.fsiNumber = value;
    }

}
