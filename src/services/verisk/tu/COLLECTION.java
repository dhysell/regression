//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:05:28 PM MDT 
//


package services.verisk.tu;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}CL_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}CL_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}CL_IND_CODE" minOccurs="0"/>
 *         &lt;element ref="{}CL_MEM_CODE" minOccurs="0"/>
 *         &lt;element ref="{}CL_AG_NAME" minOccurs="0"/>
 *         &lt;element ref="{}CL_ACT_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}CL_ACT_NUM" minOccurs="0"/>
 *         &lt;element ref="{}CL_ACT_DSG" minOccurs="0"/>
 *         &lt;element ref="{}CL_CR_NAME" minOccurs="0"/>
 *         &lt;element ref="{}CL_DTE_OPEN" minOccurs="0"/>
 *         &lt;element ref="{}CL_DTE_VERF" minOccurs="0"/>
 *         &lt;element ref="{}CL_VERIFY_IND" minOccurs="0"/>
 *         &lt;element ref="{}CL_DTE_CLOSED" minOccurs="0"/>
 *         &lt;element ref="{}CL_DTE_CLOSED_IND" minOccurs="0"/>
 *         &lt;element ref="{}CL_DTE_PAID_OUT" minOccurs="0"/>
 *         &lt;element ref="{}CL_CURR_MAN_PAYMENT" minOccurs="0"/>
 *         &lt;element ref="{}CL_CURR_BALANCE" minOccurs="0"/>
 *         &lt;element ref="{}CL_ORG_BALANCE" minOccurs="0"/>
 *         &lt;element ref="{}CL_REMARKS_CODE" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "clsegtype",
    "clseglength",
    "clindcode",
    "clmemcode",
    "clagname",
    "clacttype",
    "clactnum",
    "clactdsg",
    "clcrname",
    "cldteopen",
    "cldteverf",
    "clverifyind",
    "cldteclosed",
    "cldteclosedind",
    "cldtepaidout",
    "clcurrmanpayment",
    "clcurrbalance",
    "clorgbalance",
    "clremarkscode"
})
@XmlRootElement(name = "COLLECTION")
public class COLLECTION {

    @XmlElement(name = "CL_SEG_TYPE")
    protected String clsegtype;
    @XmlElement(name = "CL_SEG_LENGTH")
    protected String clseglength;
    @XmlElement(name = "CL_IND_CODE")
    protected String clindcode;
    @XmlElement(name = "CL_MEM_CODE")
    protected String clmemcode;
    @XmlElement(name = "CL_AG_NAME")
    protected String clagname;
    @XmlElement(name = "CL_ACT_TYPE")
    protected String clacttype;
    @XmlElement(name = "CL_ACT_NUM")
    protected String clactnum;
    @XmlElement(name = "CL_ACT_DSG")
    protected String clactdsg;
    @XmlElement(name = "CL_CR_NAME")
    protected String clcrname;
    @XmlElement(name = "CL_DTE_OPEN")
    protected String cldteopen;
    @XmlElement(name = "CL_DTE_VERF")
    protected String cldteverf;
    @XmlElement(name = "CL_VERIFY_IND")
    protected String clverifyind;
    @XmlElement(name = "CL_DTE_CLOSED")
    protected String cldteclosed;
    @XmlElement(name = "CL_DTE_CLOSED_IND")
    protected String cldteclosedind;
    @XmlElement(name = "CL_DTE_PAID_OUT")
    protected String cldtepaidout;
    @XmlElement(name = "CL_CURR_MAN_PAYMENT")
    protected String clcurrmanpayment;
    @XmlElement(name = "CL_CURR_BALANCE")
    protected String clcurrbalance;
    @XmlElement(name = "CL_ORG_BALANCE")
    protected String clorgbalance;
    @XmlElement(name = "CL_REMARKS_CODE")
    protected String clremarkscode;

    /**
     * Gets the value of the clsegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLSEGTYPE() {
        return clsegtype;
    }

    /**
     * Sets the value of the clsegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLSEGTYPE(String value) {
        this.clsegtype = value;
    }

    /**
     * Gets the value of the clseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLSEGLENGTH() {
        return clseglength;
    }

    /**
     * Sets the value of the clseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLSEGLENGTH(String value) {
        this.clseglength = value;
    }

    /**
     * Gets the value of the clindcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLINDCODE() {
        return clindcode;
    }

    /**
     * Sets the value of the clindcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLINDCODE(String value) {
        this.clindcode = value;
    }

    /**
     * Gets the value of the clmemcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLMEMCODE() {
        return clmemcode;
    }

    /**
     * Sets the value of the clmemcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLMEMCODE(String value) {
        this.clmemcode = value;
    }

    /**
     * Gets the value of the clagname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLAGNAME() {
        return clagname;
    }

    /**
     * Sets the value of the clagname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLAGNAME(String value) {
        this.clagname = value;
    }

    /**
     * Gets the value of the clacttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLACTTYPE() {
        return clacttype;
    }

    /**
     * Sets the value of the clacttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLACTTYPE(String value) {
        this.clacttype = value;
    }

    /**
     * Gets the value of the clactnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLACTNUM() {
        return clactnum;
    }

    /**
     * Sets the value of the clactnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLACTNUM(String value) {
        this.clactnum = value;
    }

    /**
     * Gets the value of the clactdsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLACTDSG() {
        return clactdsg;
    }

    /**
     * Sets the value of the clactdsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLACTDSG(String value) {
        this.clactdsg = value;
    }

    /**
     * Gets the value of the clcrname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLCRNAME() {
        return clcrname;
    }

    /**
     * Sets the value of the clcrname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLCRNAME(String value) {
        this.clcrname = value;
    }

    /**
     * Gets the value of the cldteopen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLDTEOPEN() {
        return cldteopen;
    }

    /**
     * Sets the value of the cldteopen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLDTEOPEN(String value) {
        this.cldteopen = value;
    }

    /**
     * Gets the value of the cldteverf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLDTEVERF() {
        return cldteverf;
    }

    /**
     * Sets the value of the cldteverf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLDTEVERF(String value) {
        this.cldteverf = value;
    }

    /**
     * Gets the value of the clverifyind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLVERIFYIND() {
        return clverifyind;
    }

    /**
     * Sets the value of the clverifyind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLVERIFYIND(String value) {
        this.clverifyind = value;
    }

    /**
     * Gets the value of the cldteclosed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLDTECLOSED() {
        return cldteclosed;
    }

    /**
     * Sets the value of the cldteclosed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLDTECLOSED(String value) {
        this.cldteclosed = value;
    }

    /**
     * Gets the value of the cldteclosedind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLDTECLOSEDIND() {
        return cldteclosedind;
    }

    /**
     * Sets the value of the cldteclosedind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLDTECLOSEDIND(String value) {
        this.cldteclosedind = value;
    }

    /**
     * Gets the value of the cldtepaidout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLDTEPAIDOUT() {
        return cldtepaidout;
    }

    /**
     * Sets the value of the cldtepaidout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLDTEPAIDOUT(String value) {
        this.cldtepaidout = value;
    }

    /**
     * Gets the value of the clcurrmanpayment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLCURRMANPAYMENT() {
        return clcurrmanpayment;
    }

    /**
     * Sets the value of the clcurrmanpayment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLCURRMANPAYMENT(String value) {
        this.clcurrmanpayment = value;
    }

    /**
     * Gets the value of the clcurrbalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLCURRBALANCE() {
        return clcurrbalance;
    }

    /**
     * Sets the value of the clcurrbalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLCURRBALANCE(String value) {
        this.clcurrbalance = value;
    }

    /**
     * Gets the value of the clorgbalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLORGBALANCE() {
        return clorgbalance;
    }

    /**
     * Sets the value of the clorgbalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLORGBALANCE(String value) {
        this.clorgbalance = value;
    }

    /**
     * Gets the value of the clremarkscode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCLREMARKSCODE() {
        return clremarkscode;
    }

    /**
     * Sets the value of the clremarkscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCLREMARKSCODE(String value) {
        this.clremarkscode = value;
    }

}
