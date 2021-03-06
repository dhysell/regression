//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.10.16 at 03:05:28 PM MDT 
//


package services.verisk.tu;

import java.util.ArrayList;
import java.util.List;
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
 *         &lt;element ref="{}SUBJECT-HEAD" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}NU01" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}ENDUSER" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-NAME" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-PERSONAL" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-ALERT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}AM01" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-ADDR" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-PHONE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-EMP" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HEAD-EMP-ADDR" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}CREDIT-SUM" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}CREDIT-SUM-DESC" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}PUBREC" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}COLLECTION" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}COMPLIANCE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}TRADE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}CONS-STATEMENT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}INQUIRY" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HAWK" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HAWK-INQUIRY" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HAWK-MSG-CODE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HAWK-DECEASED" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HAWK-YEAR-OF-ISSUE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}HAWK-MSG-TXT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}SCORE" maxOccurs="unbounded" minOccurs="0"/>
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
    "subjecthead",
    "nu01",
    "enduser",
    "headname",
    "headpersonal",
    "headalert",
    "am01",
    "headaddr",
    "headphone",
    "heademp",
    "headempaddr",
    "creditsum",
    "creditsumdesc",
    "pubrec",
    "collection",
    "compliance",
    "trade",
    "consstatement",
    "inquiry",
    "hawk",
    "hawkinquiry",
    "hawkmsgcode",
    "hawkdeceased",
    "hawkyearofissue",
    "hawkmsgtxt",
    "score"
})
@XmlRootElement(name = "REPORTS")
public class REPORTS {

    @XmlElement(name = "SUBJECT-HEAD")
    protected List<SUBJECTHEAD> subjecthead;
    @XmlElement(name = "NU01")
    protected List<NU01> nu01;
    @XmlElement(name = "ENDUSER")
    protected List<ENDUSER> enduser;
    @XmlElement(name = "HEAD-NAME")
    protected List<HEADNAME> headname;
    @XmlElement(name = "HEAD-PERSONAL")
    protected List<HEADPERSONAL> headpersonal;
    @XmlElement(name = "HEAD-ALERT")
    protected List<HEADALERT> headalert;
    @XmlElement(name = "AM01")
    protected List<AM01> am01;
    @XmlElement(name = "HEAD-ADDR")
    protected List<HEADADDR> headaddr;
    @XmlElement(name = "HEAD-PHONE")
    protected List<HEADPHONE> headphone;
    @XmlElement(name = "HEAD-EMP")
    protected List<HEADEMP> heademp;
    @XmlElement(name = "HEAD-EMP-ADDR")
    protected List<HEADEMPADDR> headempaddr;
    @XmlElement(name = "CREDIT-SUM")
    protected List<CREDITSUM> creditsum;
    @XmlElement(name = "CREDIT-SUM-DESC")
    protected List<CREDITSUMDESC> creditsumdesc;
    @XmlElement(name = "PUBREC")
    protected List<PUBREC> pubrec;
    @XmlElement(name = "COLLECTION")
    protected List<COLLECTION> collection;
    @XmlElement(name = "COMPLIANCE")
    protected List<COMPLIANCE> compliance;
    @XmlElement(name = "TRADE")
    protected List<TRADE> trade;
    @XmlElement(name = "CONS-STATEMENT")
    protected List<CONSSTATEMENT> consstatement;
    @XmlElement(name = "INQUIRY")
    protected List<INQUIRY> inquiry;
    @XmlElement(name = "HAWK")
    protected List<HAWK> hawk;
    @XmlElement(name = "HAWK-INQUIRY")
    protected List<HAWKINQUIRY> hawkinquiry;
    @XmlElement(name = "HAWK-MSG-CODE")
    protected List<HAWKMSGCODE> hawkmsgcode;
    @XmlElement(name = "HAWK-DECEASED")
    protected List<HAWKDECEASED> hawkdeceased;
    @XmlElement(name = "HAWK-YEAR-OF-ISSUE")
    protected List<HAWKYEAROFISSUE> hawkyearofissue;
    @XmlElement(name = "HAWK-MSG-TXT")
    protected List<HAWKMSGTXT> hawkmsgtxt;
    @XmlElement(name = "SCORE")
    protected List<SCORE> score;

    /**
     * Gets the value of the subjecthead property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subjecthead property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSUBJECTHEAD().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SUBJECTHEAD }
     * 
     * 
     */
    public List<SUBJECTHEAD> getSUBJECTHEAD() {
        if (subjecthead == null) {
            subjecthead = new ArrayList<SUBJECTHEAD>();
        }
        return this.subjecthead;
    }

    /**
     * Gets the value of the nu01 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nu01 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNU01().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NU01 }
     * 
     * 
     */
    public List<NU01> getNU01() {
        if (nu01 == null) {
            nu01 = new ArrayList<NU01>();
        }
        return this.nu01;
    }

    /**
     * Gets the value of the enduser property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the enduser property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getENDUSER().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ENDUSER }
     * 
     * 
     */
    public List<ENDUSER> getENDUSER() {
        if (enduser == null) {
            enduser = new ArrayList<ENDUSER>();
        }
        return this.enduser;
    }

    /**
     * Gets the value of the headname property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headname property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADNAME().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADNAME }
     * 
     * 
     */
    public List<HEADNAME> getHEADNAME() {
        if (headname == null) {
            headname = new ArrayList<HEADNAME>();
        }
        return this.headname;
    }

    /**
     * Gets the value of the headpersonal property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headpersonal property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADPERSONAL().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADPERSONAL }
     * 
     * 
     */
    public List<HEADPERSONAL> getHEADPERSONAL() {
        if (headpersonal == null) {
            headpersonal = new ArrayList<HEADPERSONAL>();
        }
        return this.headpersonal;
    }

    /**
     * Gets the value of the headalert property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headalert property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADALERT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADALERT }
     * 
     * 
     */
    public List<HEADALERT> getHEADALERT() {
        if (headalert == null) {
            headalert = new ArrayList<HEADALERT>();
        }
        return this.headalert;
    }

    /**
     * Gets the value of the am01 property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the am01 property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAM01().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AM01 }
     * 
     * 
     */
    public List<AM01> getAM01() {
        if (am01 == null) {
            am01 = new ArrayList<AM01>();
        }
        return this.am01;
    }

    /**
     * Gets the value of the headaddr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headaddr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADADDR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADADDR }
     * 
     * 
     */
    public List<HEADADDR> getHEADADDR() {
        if (headaddr == null) {
            headaddr = new ArrayList<HEADADDR>();
        }
        return this.headaddr;
    }

    /**
     * Gets the value of the headphone property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headphone property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADPHONE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADPHONE }
     * 
     * 
     */
    public List<HEADPHONE> getHEADPHONE() {
        if (headphone == null) {
            headphone = new ArrayList<HEADPHONE>();
        }
        return this.headphone;
    }

    /**
     * Gets the value of the heademp property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the heademp property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADEMP().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADEMP }
     * 
     * 
     */
    public List<HEADEMP> getHEADEMP() {
        if (heademp == null) {
            heademp = new ArrayList<HEADEMP>();
        }
        return this.heademp;
    }

    /**
     * Gets the value of the headempaddr property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the headempaddr property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHEADEMPADDR().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HEADEMPADDR }
     * 
     * 
     */
    public List<HEADEMPADDR> getHEADEMPADDR() {
        if (headempaddr == null) {
            headempaddr = new ArrayList<HEADEMPADDR>();
        }
        return this.headempaddr;
    }

    /**
     * Gets the value of the creditsum property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creditsum property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCREDITSUM().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CREDITSUM }
     * 
     * 
     */
    public List<CREDITSUM> getCREDITSUM() {
        if (creditsum == null) {
            creditsum = new ArrayList<CREDITSUM>();
        }
        return this.creditsum;
    }

    /**
     * Gets the value of the creditsumdesc property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the creditsumdesc property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCREDITSUMDESC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CREDITSUMDESC }
     * 
     * 
     */
    public List<CREDITSUMDESC> getCREDITSUMDESC() {
        if (creditsumdesc == null) {
            creditsumdesc = new ArrayList<CREDITSUMDESC>();
        }
        return this.creditsumdesc;
    }

    /**
     * Gets the value of the pubrec property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the pubrec property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPUBREC().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PUBREC }
     * 
     * 
     */
    public List<PUBREC> getPUBREC() {
        if (pubrec == null) {
            pubrec = new ArrayList<PUBREC>();
        }
        return this.pubrec;
    }

    /**
     * Gets the value of the collection property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the collection property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCOLLECTION().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COLLECTION }
     * 
     * 
     */
    public List<COLLECTION> getCOLLECTION() {
        if (collection == null) {
            collection = new ArrayList<COLLECTION>();
        }
        return this.collection;
    }

    /**
     * Gets the value of the compliance property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compliance property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCOMPLIANCE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link COMPLIANCE }
     * 
     * 
     */
    public List<COMPLIANCE> getCOMPLIANCE() {
        if (compliance == null) {
            compliance = new ArrayList<COMPLIANCE>();
        }
        return this.compliance;
    }

    /**
     * Gets the value of the trade property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the trade property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTRADE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TRADE }
     * 
     * 
     */
    public List<TRADE> getTRADE() {
        if (trade == null) {
            trade = new ArrayList<TRADE>();
        }
        return this.trade;
    }

    /**
     * Gets the value of the consstatement property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the consstatement property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCONSSTATEMENT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CONSSTATEMENT }
     * 
     * 
     */
    public List<CONSSTATEMENT> getCONSSTATEMENT() {
        if (consstatement == null) {
            consstatement = new ArrayList<CONSSTATEMENT>();
        }
        return this.consstatement;
    }

    /**
     * Gets the value of the inquiry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the inquiry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getINQUIRY().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link INQUIRY }
     * 
     * 
     */
    public List<INQUIRY> getINQUIRY() {
        if (inquiry == null) {
            inquiry = new ArrayList<INQUIRY>();
        }
        return this.inquiry;
    }

    /**
     * Gets the value of the hawk property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hawk property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAWK().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HAWK }
     * 
     * 
     */
    public List<HAWK> getHAWK() {
        if (hawk == null) {
            hawk = new ArrayList<HAWK>();
        }
        return this.hawk;
    }

    /**
     * Gets the value of the hawkinquiry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hawkinquiry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAWKINQUIRY().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HAWKINQUIRY }
     * 
     * 
     */
    public List<HAWKINQUIRY> getHAWKINQUIRY() {
        if (hawkinquiry == null) {
            hawkinquiry = new ArrayList<HAWKINQUIRY>();
        }
        return this.hawkinquiry;
    }

    /**
     * Gets the value of the hawkmsgcode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hawkmsgcode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAWKMSGCODE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HAWKMSGCODE }
     * 
     * 
     */
    public List<HAWKMSGCODE> getHAWKMSGCODE() {
        if (hawkmsgcode == null) {
            hawkmsgcode = new ArrayList<HAWKMSGCODE>();
        }
        return this.hawkmsgcode;
    }

    /**
     * Gets the value of the hawkdeceased property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hawkdeceased property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAWKDECEASED().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HAWKDECEASED }
     * 
     * 
     */
    public List<HAWKDECEASED> getHAWKDECEASED() {
        if (hawkdeceased == null) {
            hawkdeceased = new ArrayList<HAWKDECEASED>();
        }
        return this.hawkdeceased;
    }

    /**
     * Gets the value of the hawkyearofissue property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hawkyearofissue property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAWKYEAROFISSUE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HAWKYEAROFISSUE }
     * 
     * 
     */
    public List<HAWKYEAROFISSUE> getHAWKYEAROFISSUE() {
        if (hawkyearofissue == null) {
            hawkyearofissue = new ArrayList<HAWKYEAROFISSUE>();
        }
        return this.hawkyearofissue;
    }

    /**
     * Gets the value of the hawkmsgtxt property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hawkmsgtxt property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHAWKMSGTXT().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HAWKMSGTXT }
     * 
     * 
     */
    public List<HAWKMSGTXT> getHAWKMSGTXT() {
        if (hawkmsgtxt == null) {
            hawkmsgtxt = new ArrayList<HAWKMSGTXT>();
        }
        return this.hawkmsgtxt;
    }

    /**
     * Gets the value of the score property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the score property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSCORE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SCORE }
     * 
     * 
     */
    public List<SCORE> getSCORE() {
        if (score == null) {
            score = new ArrayList<SCORE>();
        }
        return this.score;
    }

}
