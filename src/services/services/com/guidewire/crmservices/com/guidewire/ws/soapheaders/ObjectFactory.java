
package services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.guidewire.ws.soapheaders package. 
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

    private final static QName _TransactionId_QNAME = new QName("http://guidewire.com/ws/soapheaders", "transaction_id");
    private final static QName _Authentication_QNAME = new QName("http://guidewire.com/ws/soapheaders", "authentication");
    private final static QName _Locale_QNAME = new QName("http://guidewire.com/ws/soapheaders", "locale");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.guidewire.ws.soapheaders
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId }
     * 
     */
    public services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId createTransactionId() {
        return new services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId();
    }

    /**
     * Create an instance of {@link services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale }
     * 
     */
    public services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale createLocale() {
        return new services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale();
    }

    /**
     * Create an instance of {@link services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication }
     * 
     */
    public services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication createAuthentication() {
        return new services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://guidewire.com/ws/soapheaders", name = "transaction_id")
    public JAXBElement<services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId> createTransactionId(services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId value) {
        return new JAXBElement<services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.TransactionId>(_TransactionId_QNAME, TransactionId.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://guidewire.com/ws/soapheaders", name = "authentication")
    public JAXBElement<services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication> createAuthentication(services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication value) {
        return new JAXBElement<services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Authentication>(_Authentication_QNAME, Authentication.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://guidewire.com/ws/soapheaders", name = "locale")
    public JAXBElement<services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale> createLocale(services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale value) {
        return new JAXBElement<services.services.com.guidewire.crmservices.com.guidewire.ws.soapheaders.Locale>(_Locale_QNAME, Locale.class, null, value);
    }

}
