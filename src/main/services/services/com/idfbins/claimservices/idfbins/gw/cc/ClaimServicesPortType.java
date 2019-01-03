
package services.services.com.idfbins.claimservices.idfbins.gw.cc;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;
import services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto.AgentInfo;
import services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto.AssociatedPolicyInfo;
import services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto.ClaimVendorInfo;
import services.services.com.idfbins.claimservices.guidewire.ab.typekey.ClaimVendorType;
import services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresultcontainer.ABContactAPISearchResultContainer;
import services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto.ObjectFactory;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebService(name = "ClaimServicesPortType", targetNamespace = "http://www.idfbins.com/gw/cc")
@XmlSeeAlso({
	services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresultcontainer.ObjectFactory.class,
	ObjectFactory.class,
    services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresult.ObjectFactory.class,
    services.services.com.idfbins.claimservices.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.addressinfo.ObjectFactory.class,
    services.services.com.idfbins.claimservices.guidewire.ws.soapheaders.ObjectFactory.class,
    services.services.com.idfbins.claimservices.idfbins.gw.cc.ObjectFactory.class,
    services.services.com.idfbins.claimservices.guidewire.ab.typekey.ObjectFactory.class
})
public interface ClaimServicesPortType {


    /**
     * 
     * @param cvt
     * @param preferred
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "getVendorsByType", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetVendorsByType")
    @ResponseWrapper(localName = "getVendorsByTypeResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetVendorsByTypeResponse")
    public ClaimVendorInfo getVendorsByType(
        @WebParam(name = "cvt", targetNamespace = "http://www.idfbins.com/gw/cc")
        ClaimVendorType cvt,
        @WebParam(name = "preferred", targetNamespace = "http://www.idfbins.com/gw/cc")
        boolean preferred)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveSafeliteVendorPublicID", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveSafeliteVendorPublicID")
    @ResponseWrapper(localName = "retrieveSafeliteVendorPublicIDResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveSafeliteVendorPublicIDResponse")
    public ClaimVendorInfo retrieveSafeliteVendorPublicID()
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveQCSVendorPublicID", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveQCSVendorPublicID")
    @ResponseWrapper(localName = "retrieveQCSVendorPublicIDResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveQCSVendorPublicIDResponse")
    public ClaimVendorInfo retrieveQCSVendorPublicID()
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveQuestVendorPublicID", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveQuestVendorPublicID")
    @ResponseWrapper(localName = "retrieveQuestVendorPublicIDResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveQuestVendorPublicIDResponse")
    public ClaimVendorInfo retrieveQuestVendorPublicID()
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveFlagshipVendorPublicID", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveFlagshipVendorPublicID")
    @ResponseWrapper(localName = "retrieveFlagshipVendorPublicIDResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveFlagshipVendorPublicIDResponse")
    public ClaimVendorInfo retrieveFlagshipVendorPublicID()
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveCCCVendorPublicID", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveCCCVendorPublicID")
    @ResponseWrapper(localName = "retrieveCCCVendorPublicIDResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveCCCVendorPublicIDResponse")
    public ClaimVendorInfo retrieveCCCVendorPublicID()
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param value
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveVendorByVendorNumber", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveVendorByVendorNumber")
    @ResponseWrapper(localName = "retrieveVendorByVendorNumberResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveVendorByVendorNumberResponse")
    public ClaimVendorInfo retrieveVendorByVendorNumber(
        @WebParam(name = "value", targetNamespace = "http://www.idfbins.com/gw/cc")
        String value)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param name
     * @return
     *     returns com.example.com.idfbins.ab.dto.ClaimVendorInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveVendorByVendorName", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveVendorByVendorName")
    @ResponseWrapper(localName = "retrieveVendorByVendorNameResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveVendorByVendorNameResponse")
    public ClaimVendorInfo retrieveVendorByVendorName(
        @WebParam(name = "name", targetNamespace = "http://www.idfbins.com/gw/cc")
        String name)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param publicID
     * @return
     *     returns java.lang.String
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "getAgentNumberByPublicID", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetAgentNumberByPublicID")
    @ResponseWrapper(localName = "getAgentNumberByPublicIDResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetAgentNumberByPublicIDResponse")
    public String getAgentNumberByPublicID(
        @WebParam(name = "publicID", targetNamespace = "http://www.idfbins.com/gw/cc")
        String publicID)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param name
     * @param contactType
     * @return
     *     returns com.guidewire.ab.ws.gw.webservice.ab.ab801.abcontactapi.abcontactapisearchresultcontainer.ABContactAPISearchResultContainer
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "getSimpleSearchResults", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetSimpleSearchResults")
    @ResponseWrapper(localName = "getSimpleSearchResultsResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetSimpleSearchResultsResponse")
    public ABContactAPISearchResultContainer getSimpleSearchResults(
        @WebParam(name = "name", targetNamespace = "http://www.idfbins.com/gw/cc")
        String name,
        @WebParam(name = "contactType", targetNamespace = "http://www.idfbins.com/gw/cc")
        String contactType)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param agentNumber
     * @return
     *     returns com.example.com.idfbins.ab.dto.AgentInfo
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "getAgentInformation", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetAgentInformation")
    @ResponseWrapper(localName = "getAgentInformationResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetAgentInformationResponse")
    public AgentInfo getAgentInformation(
        @WebParam(name = "agentNumber", targetNamespace = "http://www.idfbins.com/gw/cc")
        String agentNumber)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param publicID
     * @return
     *     returns java.lang.String
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "getDBANamesForContact", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetDBANamesForContact")
    @ResponseWrapper(localName = "getDBANamesForContactResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.GetDBANamesForContactResponse")
    public String getDBANamesForContact(
        @WebParam(name = "publicID", targetNamespace = "http://www.idfbins.com/gw/cc")
        String publicID)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param roles
     * @param policyNumber
     * @param user
     * @param publicID
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "createInitialClaimContactActivity", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.CreateInitialClaimContactActivity")
    @ResponseWrapper(localName = "createInitialClaimContactActivityResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.CreateInitialClaimContactActivityResponse")
    public void createInitialClaimContactActivity(
        @WebParam(name = "publicID", targetNamespace = "http://www.idfbins.com/gw/cc")
        String publicID,
        @WebParam(name = "policyNumber", targetNamespace = "http://www.idfbins.com/gw/cc")
        String policyNumber,
        @WebParam(name = "roles", targetNamespace = "http://www.idfbins.com/gw/cc")
        String roles,
        @WebParam(name = "user", targetNamespace = "http://www.idfbins.com/gw/cc")
        String user)
        throws WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param info
     * @throws WsiAuthenticationException_Exception
     * @throws RetryableException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "createAssociatedPolicy", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.CreateAssociatedPolicy")
    @ResponseWrapper(localName = "createAssociatedPolicyResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.CreateAssociatedPolicyResponse")
    public void createAssociatedPolicy(
        @WebParam(name = "info", targetNamespace = "http://www.idfbins.com/gw/cc")
                AssociatedPolicyInfo info)
        throws RetryableException_Exception, WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param publicID
     * @param email
     * @throws WsiAuthenticationException_Exception
     * @throws RetryableException_Exception
     */
    @WebMethod
    @RequestWrapper(localName = "saveEmailAddress", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.SaveEmailAddress")
    @ResponseWrapper(localName = "saveEmailAddressResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.SaveEmailAddressResponse")
    public void saveEmailAddress(
        @WebParam(name = "publicID", targetNamespace = "http://www.idfbins.com/gw/cc")
        String publicID,
        @WebParam(name = "email", targetNamespace = "http://www.idfbins.com/gw/cc")
        String email)
        throws RetryableException_Exception, WsiAuthenticationException_Exception
    ;

    /**
     * 
     * @param ssn
     * @return
     *     returns java.lang.String
     * @throws WsiAuthenticationException_Exception
     */
    @WebMethod
    @WebResult(targetNamespace = "http://www.idfbins.com/gw/cc")
    @RequestWrapper(localName = "retrieveAddressBookUIDBySSN", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveAddressBookUIDBySSN")
    @ResponseWrapper(localName = "retrieveAddressBookUIDBySSNResponse", targetNamespace = "http://www.idfbins.com/gw/cc", className = "com.idfbins.gw.cc.RetrieveAddressBookUIDBySSNResponse")
    public String retrieveAddressBookUIDBySSN(
        @WebParam(name = "ssn", targetNamespace = "http://www.idfbins.com/gw/cc")
        String ssn)
        throws WsiAuthenticationException_Exception
    ;

}