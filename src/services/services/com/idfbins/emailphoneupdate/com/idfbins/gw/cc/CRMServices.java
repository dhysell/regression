
package services.services.com.idfbins.emailphoneupdate.com.idfbins.gw.cc;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "CRMServices", targetNamespace = "http://www.idfbins.com/gw/cc", wsdlLocation = "http://ab8uat.idfbins.com/ab/ws/com/idfbins/ab/webservice/CRMServices?WSDL")
public class CRMServices
    extends Service
{

    private final static URL CRMSERVICES_WSDL_LOCATION;
    private final static WebServiceException CRMSERVICES_EXCEPTION;
    private final static QName CRMSERVICES_QNAME = new QName("http://www.idfbins.com/gw/cc", "CRMServices");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://ab8uat.idfbins.com/ab/ws/com/idfbins/ab/webservice/CRMServices?WSDL");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        CRMSERVICES_WSDL_LOCATION = url;
        CRMSERVICES_EXCEPTION = e;
    }

    public CRMServices() {
        super(__getWsdlLocation(), CRMSERVICES_QNAME);
    }

    public CRMServices(WebServiceFeature... features) {
        super(__getWsdlLocation(), CRMSERVICES_QNAME, features);
    }

    public CRMServices(URL wsdlLocation) {
        super(wsdlLocation, CRMSERVICES_QNAME);
    }

    public CRMServices(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, CRMSERVICES_QNAME, features);
    }

    public CRMServices(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public CRMServices(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns CRMServicesPortType
     */
    @WebEndpoint(name = "CRMServicesSoap11Port")
    public CRMServicesPortType getCRMServicesSoap11Port() {
        return super.getPort(new QName("http://www.idfbins.com/gw/cc", "CRMServicesSoap11Port"), CRMServicesPortType.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns CRMServicesPortType
     */
    @WebEndpoint(name = "CRMServicesSoap11Port")
    public CRMServicesPortType getCRMServicesSoap11Port(WebServiceFeature... features) {
        return super.getPort(new QName("http://www.idfbins.com/gw/cc", "CRMServicesSoap11Port"), CRMServicesPortType.class, features);
    }

    private static URL __getWsdlLocation() {
        if (CRMSERVICES_EXCEPTION!= null) {
            throw CRMSERVICES_EXCEPTION;
        }
        return CRMSERVICES_WSDL_LOCATION;
    }

}
