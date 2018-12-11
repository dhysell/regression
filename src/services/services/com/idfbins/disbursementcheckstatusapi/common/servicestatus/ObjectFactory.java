
package services.services.com.idfbins.disbursementcheckstatusapi.common.servicestatus;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.idfbins.common.servicestatus package. 
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

    private final static QName _ServiceStatus_QNAME = new QName("http://www.idfbins.com/common/ServiceStatus", "serviceStatus");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.idfbins.common.servicestatus
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceStatus }
     * 
     */
    public ServiceStatus createServiceStatus() {
        return new ServiceStatus();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.idfbins.com/common/ServiceStatus", name = "serviceStatus")
    public JAXBElement<ServiceStatus> createServiceStatus(ServiceStatus value) {
        return new JAXBElement<ServiceStatus>(_ServiceStatus_QNAME, ServiceStatus.class, null, value);
    }

}
