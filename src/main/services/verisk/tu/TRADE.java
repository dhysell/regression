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
 *         &lt;element ref="{}TR_SEG_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}TR_SEG_LENGTH" minOccurs="0"/>
 *         &lt;element ref="{}TR_IND_CODE" minOccurs="0"/>
 *         &lt;element ref="{}TR_MEM_CODE" minOccurs="0"/>
 *         &lt;element ref="{}TR_SUB_NAME" minOccurs="0"/>
 *         &lt;element ref="{}TR_ACT_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}TR_ACT_NUM" minOccurs="0"/>
 *         &lt;element ref="{}TR_ACT_DSG" minOccurs="0"/>
 *         &lt;element ref="{}TR_DT_OPEN" minOccurs="0"/>
 *         &lt;element ref="{}TR_DT_VERF" minOccurs="0"/>
 *         &lt;element ref="{}TR_VERF_IND" minOccurs="0"/>
 *         &lt;element ref="{}TR_DT_CLOSE" minOccurs="0"/>
 *         &lt;element ref="{}TR_DT_IND" minOccurs="0"/>
 *         &lt;element ref="{}TR_DT_PAID" minOccurs="0"/>
 *         &lt;element ref="{}TR_DT_LST" minOccurs="0"/>
 *         &lt;element ref="{}TR_MNR_PMT" minOccurs="0"/>
 *         &lt;element ref="{}TR_CURRENCY" minOccurs="0"/>
 *         &lt;element ref="{}TR_BALANCE" minOccurs="0"/>
 *         &lt;element ref="{}TR_HIGH_CRD" minOccurs="0"/>
 *         &lt;element ref="{}TR_CRD_LIMIT" minOccurs="0"/>
 *         &lt;element ref="{}TR_TERMS" minOccurs="0"/>
 *         &lt;element ref="{}TR_FREQ" minOccurs="0"/>
 *         &lt;element ref="{}TR_PMNT" minOccurs="0"/>
 *         &lt;element ref="{}TR_COLL" minOccurs="0"/>
 *         &lt;element ref="{}TR_LOAN_TYPE" minOccurs="0"/>
 *         &lt;element ref="{}TR_RMKS_CODE" minOccurs="0"/>
 *         &lt;element ref="{}TR_AMT_DUE" minOccurs="0"/>
 *         &lt;element ref="{}TR_NUM_PMNT" minOccurs="0"/>
 *         &lt;element ref="{}TR_MXM_DEL" minOccurs="0"/>
 *         &lt;element ref="{}TR_DEL_DT" minOccurs="0"/>
 *         &lt;element ref="{}TR_DEL_MOP" minOccurs="0"/>
 *         &lt;element ref="{}TR_START_DT" minOccurs="0"/>
 *         &lt;element ref="{}TR_PMNT_PAT" minOccurs="0"/>
 *         &lt;element ref="{}TR_NUM_MNTH" minOccurs="0"/>
 *         &lt;element ref="{}TR_30_LATE" minOccurs="0"/>
 *         &lt;element ref="{}TR_60_LATE" minOccurs="0"/>
 *         &lt;element ref="{}TR_90_LATE" minOccurs="0"/>
 *         &lt;element ref="{}TR_HIST_CNT" minOccurs="0"/>
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
    "trsegtype",
    "trseglength",
    "trindcode",
    "trmemcode",
    "trsubname",
    "tracttype",
    "tractnum",
    "tractdsg",
    "trdtopen",
    "trdtverf",
    "trverfind",
    "trdtclose",
    "trdtind",
    "trdtpaid",
    "trdtlst",
    "trmnrpmt",
    "trcurrency",
    "trbalance",
    "trhighcrd",
    "trcrdlimit",
    "trterms",
    "trfreq",
    "trpmnt",
    "trcoll",
    "trloantype",
    "trrmkscode",
    "tramtdue",
    "trnumpmnt",
    "trmxmdel",
    "trdeldt",
    "trdelmop",
    "trstartdt",
    "trpmntpat",
    "trnummnth",
    "tr30LATE",
    "tr60LATE",
    "tr90LATE",
    "trhistcnt"
})
@XmlRootElement(name = "TRADE")
public class TRADE {

    @XmlElement(name = "TR_SEG_TYPE")
    protected String trsegtype;
    @XmlElement(name = "TR_SEG_LENGTH")
    protected String trseglength;
    @XmlElement(name = "TR_IND_CODE")
    protected String trindcode;
    @XmlElement(name = "TR_MEM_CODE")
    protected String trmemcode;
    @XmlElement(name = "TR_SUB_NAME")
    protected String trsubname;
    @XmlElement(name = "TR_ACT_TYPE")
    protected String tracttype;
    @XmlElement(name = "TR_ACT_NUM")
    protected String tractnum;
    @XmlElement(name = "TR_ACT_DSG")
    protected String tractdsg;
    @XmlElement(name = "TR_DT_OPEN")
    protected String trdtopen;
    @XmlElement(name = "TR_DT_VERF")
    protected String trdtverf;
    @XmlElement(name = "TR_VERF_IND")
    protected String trverfind;
    @XmlElement(name = "TR_DT_CLOSE")
    protected String trdtclose;
    @XmlElement(name = "TR_DT_IND")
    protected String trdtind;
    @XmlElement(name = "TR_DT_PAID")
    protected String trdtpaid;
    @XmlElement(name = "TR_DT_LST")
    protected String trdtlst;
    @XmlElement(name = "TR_MNR_PMT")
    protected String trmnrpmt;
    @XmlElement(name = "TR_CURRENCY")
    protected String trcurrency;
    @XmlElement(name = "TR_BALANCE")
    protected String trbalance;
    @XmlElement(name = "TR_HIGH_CRD")
    protected String trhighcrd;
    @XmlElement(name = "TR_CRD_LIMIT")
    protected String trcrdlimit;
    @XmlElement(name = "TR_TERMS")
    protected String trterms;
    @XmlElement(name = "TR_FREQ")
    protected String trfreq;
    @XmlElement(name = "TR_PMNT")
    protected String trpmnt;
    @XmlElement(name = "TR_COLL")
    protected String trcoll;
    @XmlElement(name = "TR_LOAN_TYPE")
    protected String trloantype;
    @XmlElement(name = "TR_RMKS_CODE")
    protected String trrmkscode;
    @XmlElement(name = "TR_AMT_DUE")
    protected String tramtdue;
    @XmlElement(name = "TR_NUM_PMNT")
    protected String trnumpmnt;
    @XmlElement(name = "TR_MXM_DEL")
    protected String trmxmdel;
    @XmlElement(name = "TR_DEL_DT")
    protected String trdeldt;
    @XmlElement(name = "TR_DEL_MOP")
    protected String trdelmop;
    @XmlElement(name = "TR_START_DT")
    protected String trstartdt;
    @XmlElement(name = "TR_PMNT_PAT")
    protected String trpmntpat;
    @XmlElement(name = "TR_NUM_MNTH")
    protected String trnummnth;
    @XmlElement(name = "TR_30_LATE")
    protected String tr30LATE;
    @XmlElement(name = "TR_60_LATE")
    protected String tr60LATE;
    @XmlElement(name = "TR_90_LATE")
    protected String tr90LATE;
    @XmlElement(name = "TR_HIST_CNT")
    protected String trhistcnt;

    /**
     * Gets the value of the trsegtype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRSEGTYPE() {
        return trsegtype;
    }

    /**
     * Sets the value of the trsegtype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRSEGTYPE(String value) {
        this.trsegtype = value;
    }

    /**
     * Gets the value of the trseglength property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRSEGLENGTH() {
        return trseglength;
    }

    /**
     * Sets the value of the trseglength property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRSEGLENGTH(String value) {
        this.trseglength = value;
    }

    /**
     * Gets the value of the trindcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRINDCODE() {
        return trindcode;
    }

    /**
     * Sets the value of the trindcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRINDCODE(String value) {
        this.trindcode = value;
    }

    /**
     * Gets the value of the trmemcode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRMEMCODE() {
        return trmemcode;
    }

    /**
     * Sets the value of the trmemcode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRMEMCODE(String value) {
        this.trmemcode = value;
    }

    /**
     * Gets the value of the trsubname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRSUBNAME() {
        return trsubname;
    }

    /**
     * Sets the value of the trsubname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRSUBNAME(String value) {
        this.trsubname = value;
    }

    /**
     * Gets the value of the tracttype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRACTTYPE() {
        return tracttype;
    }

    /**
     * Sets the value of the tracttype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRACTTYPE(String value) {
        this.tracttype = value;
    }

    /**
     * Gets the value of the tractnum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRACTNUM() {
        return tractnum;
    }

    /**
     * Sets the value of the tractnum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRACTNUM(String value) {
        this.tractnum = value;
    }

    /**
     * Gets the value of the tractdsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRACTDSG() {
        return tractdsg;
    }

    /**
     * Sets the value of the tractdsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRACTDSG(String value) {
        this.tractdsg = value;
    }

    /**
     * Gets the value of the trdtopen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDTOPEN() {
        return trdtopen;
    }

    /**
     * Sets the value of the trdtopen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDTOPEN(String value) {
        this.trdtopen = value;
    }

    /**
     * Gets the value of the trdtverf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDTVERF() {
        return trdtverf;
    }

    /**
     * Sets the value of the trdtverf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDTVERF(String value) {
        this.trdtverf = value;
    }

    /**
     * Gets the value of the trverfind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRVERFIND() {
        return trverfind;
    }

    /**
     * Sets the value of the trverfind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRVERFIND(String value) {
        this.trverfind = value;
    }

    /**
     * Gets the value of the trdtclose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDTCLOSE() {
        return trdtclose;
    }

    /**
     * Sets the value of the trdtclose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDTCLOSE(String value) {
        this.trdtclose = value;
    }

    /**
     * Gets the value of the trdtind property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDTIND() {
        return trdtind;
    }

    /**
     * Sets the value of the trdtind property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDTIND(String value) {
        this.trdtind = value;
    }

    /**
     * Gets the value of the trdtpaid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDTPAID() {
        return trdtpaid;
    }

    /**
     * Sets the value of the trdtpaid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDTPAID(String value) {
        this.trdtpaid = value;
    }

    /**
     * Gets the value of the trdtlst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDTLST() {
        return trdtlst;
    }

    /**
     * Sets the value of the trdtlst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDTLST(String value) {
        this.trdtlst = value;
    }

    /**
     * Gets the value of the trmnrpmt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRMNRPMT() {
        return trmnrpmt;
    }

    /**
     * Sets the value of the trmnrpmt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRMNRPMT(String value) {
        this.trmnrpmt = value;
    }

    /**
     * Gets the value of the trcurrency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRCURRENCY() {
        return trcurrency;
    }

    /**
     * Sets the value of the trcurrency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRCURRENCY(String value) {
        this.trcurrency = value;
    }

    /**
     * Gets the value of the trbalance property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRBALANCE() {
        return trbalance;
    }

    /**
     * Sets the value of the trbalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRBALANCE(String value) {
        this.trbalance = value;
    }

    /**
     * Gets the value of the trhighcrd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRHIGHCRD() {
        return trhighcrd;
    }

    /**
     * Sets the value of the trhighcrd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRHIGHCRD(String value) {
        this.trhighcrd = value;
    }

    /**
     * Gets the value of the trcrdlimit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRCRDLIMIT() {
        return trcrdlimit;
    }

    /**
     * Sets the value of the trcrdlimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRCRDLIMIT(String value) {
        this.trcrdlimit = value;
    }

    /**
     * Gets the value of the trterms property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRTERMS() {
        return trterms;
    }

    /**
     * Sets the value of the trterms property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRTERMS(String value) {
        this.trterms = value;
    }

    /**
     * Gets the value of the trfreq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRFREQ() {
        return trfreq;
    }

    /**
     * Sets the value of the trfreq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRFREQ(String value) {
        this.trfreq = value;
    }

    /**
     * Gets the value of the trpmnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRPMNT() {
        return trpmnt;
    }

    /**
     * Sets the value of the trpmnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRPMNT(String value) {
        this.trpmnt = value;
    }

    /**
     * Gets the value of the trcoll property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRCOLL() {
        return trcoll;
    }

    /**
     * Sets the value of the trcoll property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRCOLL(String value) {
        this.trcoll = value;
    }

    /**
     * Gets the value of the trloantype property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRLOANTYPE() {
        return trloantype;
    }

    /**
     * Sets the value of the trloantype property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRLOANTYPE(String value) {
        this.trloantype = value;
    }

    /**
     * Gets the value of the trrmkscode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRRMKSCODE() {
        return trrmkscode;
    }

    /**
     * Sets the value of the trrmkscode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRRMKSCODE(String value) {
        this.trrmkscode = value;
    }

    /**
     * Gets the value of the tramtdue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRAMTDUE() {
        return tramtdue;
    }

    /**
     * Sets the value of the tramtdue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRAMTDUE(String value) {
        this.tramtdue = value;
    }

    /**
     * Gets the value of the trnumpmnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRNUMPMNT() {
        return trnumpmnt;
    }

    /**
     * Sets the value of the trnumpmnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRNUMPMNT(String value) {
        this.trnumpmnt = value;
    }

    /**
     * Gets the value of the trmxmdel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRMXMDEL() {
        return trmxmdel;
    }

    /**
     * Sets the value of the trmxmdel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRMXMDEL(String value) {
        this.trmxmdel = value;
    }

    /**
     * Gets the value of the trdeldt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDELDT() {
        return trdeldt;
    }

    /**
     * Sets the value of the trdeldt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDELDT(String value) {
        this.trdeldt = value;
    }

    /**
     * Gets the value of the trdelmop property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRDELMOP() {
        return trdelmop;
    }

    /**
     * Sets the value of the trdelmop property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRDELMOP(String value) {
        this.trdelmop = value;
    }

    /**
     * Gets the value of the trstartdt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRSTARTDT() {
        return trstartdt;
    }

    /**
     * Sets the value of the trstartdt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRSTARTDT(String value) {
        this.trstartdt = value;
    }

    /**
     * Gets the value of the trpmntpat property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRPMNTPAT() {
        return trpmntpat;
    }

    /**
     * Sets the value of the trpmntpat property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRPMNTPAT(String value) {
        this.trpmntpat = value;
    }

    /**
     * Gets the value of the trnummnth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRNUMMNTH() {
        return trnummnth;
    }

    /**
     * Sets the value of the trnummnth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRNUMMNTH(String value) {
        this.trnummnth = value;
    }

    /**
     * Gets the value of the tr30LATE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTR30LATE() {
        return tr30LATE;
    }

    /**
     * Sets the value of the tr30LATE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTR30LATE(String value) {
        this.tr30LATE = value;
    }

    /**
     * Gets the value of the tr60LATE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTR60LATE() {
        return tr60LATE;
    }

    /**
     * Sets the value of the tr60LATE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTR60LATE(String value) {
        this.tr60LATE = value;
    }

    /**
     * Gets the value of the tr90LATE property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTR90LATE() {
        return tr90LATE;
    }

    /**
     * Sets the value of the tr90LATE property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTR90LATE(String value) {
        this.tr90LATE = value;
    }

    /**
     * Gets the value of the trhistcnt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTRHISTCNT() {
        return trhistcnt;
    }

    /**
     * Sets the value of the trhistcnt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTRHISTCNT(String value) {
        this.trhistcnt = value;
    }

}
