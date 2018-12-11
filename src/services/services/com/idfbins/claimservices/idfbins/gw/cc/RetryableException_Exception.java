
package services.services.com.idfbins.claimservices.idfbins.gw.cc;

import javax.xml.ws.WebFault;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebFault(name = "RetryableException", targetNamespace = "http://www.idfbins.com/gw/cc")
public class RetryableException_Exception
    extends Exception
{

    /**
     * Java type that goes as soapenv:Fault detail element.
     * 
     */
    private RetryableException faultInfo;

    /**
     * 
     * @param faultInfo
     * @param message
     */
    public RetryableException_Exception(String message, RetryableException faultInfo) {
        super(message);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @param faultInfo
     * @param cause
     * @param message
     */
    public RetryableException_Exception(String message, RetryableException faultInfo, Throwable cause) {
        super(message, cause);
        this.faultInfo = faultInfo;
    }

    /**
     * 
     * @return
     *     returns fault bean: com.idfbins.gw.cc.RetryableException
     */
    public RetryableException getFaultInfo() {
        return faultInfo;
    }

}
