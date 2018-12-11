
package repository.cc.framework.gw.cc.policyPlugin.dto;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the policyPlugin package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Policy_QNAME = new QName("http://www.idfbins.com/PolicyRetrieveResponse", "Policy");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: policyPlugin
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Policy }
     */
    public Policy createPolicy() {
        return new Policy();
    }

    /**
     * Create an instance of {@link Building }
     */
    public Building createBuilding() {
        return new Building();
    }

    /**
     * Create an instance of {@link VehicleStyleType }
     */
    public VehicleStyleType createVehicleStyleType() {
        return new VehicleStyleType();
    }

    /**
     * Create an instance of {@link Owner }
     */
    public Owner createOwner() {
        return new Owner();
    }

    /**
     * Create an instance of {@link Deduction }
     */
    public Deduction createDeduction() {
        return new Deduction();
    }

    /**
     * Create an instance of {@link PolicyContact }
     */
    public PolicyContact createPolicyContact() {
        return new PolicyContact();
    }

    /**
     * Create an instance of {@link ManufacturerType }
     */
    public ManufacturerType createManufacturerType() {
        return new ManufacturerType();
    }

    /**
     * Create an instance of {@link RiskUnit }
     */
    public RiskUnit createRiskUnit() {
        return new RiskUnit();
    }

    /**
     * Create an instance of {@link OffRoadStyleType }
     */
    public OffRoadStyleType createOffRoadStyleType() {
        return new OffRoadStyleType();
    }

    /**
     * Create an instance of {@link ContactRole }
     */
    public ContactRole createContactRole() {
        return new ContactRole();
    }

    /**
     * Create an instance of {@link Vehicle }
     */
    public Vehicle createVehicle() {
        return new Vehicle();
    }

    /**
     * Create an instance of {@link Coverages }
     */
    public Coverages createCoverages() {
        return new Coverages();
    }

    /**
     * Create an instance of {@link CoverageTerms }
     */
    public CoverageTerms createCoverageTerms() {
        return new CoverageTerms();
    }

    /**
     * Create an instance of {@link PropertyItem }
     */
    public PropertyItem createPropertyItem() {
        return new PropertyItem();
    }

    /**
     * Create an instance of {@link CoverageType }
     */
    public CoverageType createCoverageType() {
        return new CoverageType();
    }

    /**
     * Create an instance of {@link PolicyLocation }
     */
    public PolicyLocation createPolicyLocation() {
        return new PolicyLocation();
    }

    /**
     * Create an instance of {@link ContactRoles }
     */
    public ContactRoles createContactRoles() {
        return new ContactRoles();
    }

    /**
     * Create an instance of {@link PolicyContacts }
     */
    public PolicyContacts createPolicyContacts() {
        return new PolicyContacts();
    }

    /**
     * Create an instance of {@link PolicyCoverage }
     */
    public PolicyCoverage createPolicyCoverage() {
        return new PolicyCoverage();
    }

    /**
     * Create an instance of {@link Buildings }
     */
    public Buildings createBuildings() {
        return new Buildings();
    }

    /**
     * Create an instance of {@link CoverageTerm }
     */
    public CoverageTerm createCoverageTerm() {
        return new CoverageTerm();
    }

    /**
     * Create an instance of {@link Coverage }
     */
    public Coverage createCoverage() {
        return new Coverage();
    }

    /**
     * Create an instance of {@link RiskUnits }
     */
    public RiskUnits createRiskUnits() {
        return new RiskUnits();
    }

    /**
     * Create an instance of {@link PropertyOwners }
     */
    public PropertyOwners createPropertyOwners() {
        return new PropertyOwners();
    }

    /**
     * Create an instance of {@link PropertyItems }
     */
    public PropertyItems createPropertyItems() {
        return new PropertyItems();
    }

    /**
     * Create an instance of {@link Contact }
     */
    public Contact createContact() {
        return new Contact();
    }

    /**
     * Create an instance of {@link VehicleOwners }
     */
    public VehicleOwners createVehicleOwners() {
        return new VehicleOwners();
    }

    /**
     * Create an instance of {@link PolicyLocations }
     */
    public PolicyLocations createPolicyLocations() {
        return new PolicyLocations();
    }

    /**
     * Create an instance of {@link ClassCode }
     */
    public ClassCode createClassCode() {
        return new ClassCode();
    }

    /**
     * Create an instance of {@link VehicleSRPType }
     */
    public VehicleSRPType createVehicleSRPType() {
        return new VehicleSRPType();
    }

    /**
     * Create an instance of {@link Location }
     */
    public Location createLocation() {
        return new Location();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Policy }{@code >}}
     */
    @XmlElementDecl(namespace = "http://www.idfbins.com/PolicyRetrieveResponse", name = "Policy")
    public JAXBElement<Policy> createPolicy(Policy value) {
        return new JAXBElement<Policy>(_Policy_QNAME, Policy.class, null, value);
    }

}
