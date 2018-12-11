
package services.services.com.idfbins.claimservices.example.com.idfbins.ab.dto;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.example.com.idfbins.ab.dto package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.example.com.idfbins.ab.dto
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClaimVendorInfo }
     * 
     */
    public ClaimVendorInfo createClaimVendorInfo() {
        return new ClaimVendorInfo();
    }

    /**
     * Create an instance of {@link AssociatedPolicyInfo }
     * 
     */
    public AssociatedPolicyInfo createAssociatedPolicyInfo() {
        return new AssociatedPolicyInfo();
    }

    /**
     * Create an instance of {@link AgentInfo }
     * 
     */
    public AgentInfo createAgentInfo() {
        return new AgentInfo();
    }

    /**
     * Create an instance of {@link ClaimVendorInfo.Vendors }
     * 
     */
    public ClaimVendorInfo.Vendors createClaimVendorInfoVendors() {
        return new ClaimVendorInfo.Vendors();
    }

}
