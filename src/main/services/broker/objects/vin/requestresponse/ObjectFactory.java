//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.11.23 at 12:52:55 PM MST 
//


package services.broker.objects.vin.requestresponse;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the broker.objects.vin.request package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ValidateVINRequest_QNAME = new QName("http://www.idfbins.com/VINValidation", "validateVINRequest");
    private final static QName _ValidateVINResponse_QNAME = new QName("http://www.idfbins.com/VINValidation", "validateVINResponse");
    private final static QName _ServiceStatus_QNAME = new QName("http://www.idfbins.com/common/ServiceStatus", "serviceStatus");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: broker.objects.vin.request
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link broker.objects.vin.requestresponse.ValidateVINRequest }
     * 
     */
    public ValidateVINRequest createValidateVINRequest() {
        return new ValidateVINRequest();
    }

    /**
     * Create an instance of {@link ValidateVINResponse }
     * 
     */
    public ValidateVINResponse createValidateVINResponse() {
        return new ValidateVINResponse();
    }

    /**
     * Create an instance of {@link broker.objects.vin.requestresponse.ServiceStatus }
     * 
     */
    public ServiceStatus createServiceStatus() {
        return new ServiceStatus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link broker.objects.vin.requestresponse.ValidateVINRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.idfbins.com/VINValidation", name = "validateVINRequest")
    public JAXBElement<ValidateVINRequest> createValidateVINRequest(ValidateVINRequest value) {
        return new JAXBElement<ValidateVINRequest>(_ValidateVINRequest_QNAME, ValidateVINRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ValidateVINResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.idfbins.com/VINValidation", name = "validateVINResponse")
    public JAXBElement<ValidateVINResponse> createValidateVINResponse(ValidateVINResponse value) {
        return new JAXBElement<ValidateVINResponse>(_ValidateVINResponse_QNAME, ValidateVINResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link broker.objects.vin.requestresponse.ServiceStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.idfbins.com/common/ServiceStatus", name = "serviceStatus")
    public JAXBElement<ServiceStatus> createServiceStatus(ServiceStatus value) {
        return new JAXBElement<ServiceStatus>(_ServiceStatus_QNAME, ServiceStatus.class, null, value);
    }

}
