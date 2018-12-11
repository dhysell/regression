package scratchpad.jon;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Vehicle.AdditionalInterestTypeCPP;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.Comprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;

public class PolicyCA extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;
    boolean testFailed = false;
    String failureString = "";
    private WebDriver driver;


    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                    this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                        this.add(new AdditionalInterest() {{
                            this.setCompanyOrInsured(ContactSubType.Company);
                            this.setAdditionalInterestSubType(AdditionalInterestSubType.CAVehicles);
                            this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002);
                        }});
                    }});
                }});
                this.add(new Vehicle() {{
                    this.setGaragedAt(locationsList.get(0).getAddress());
                    this.setVehicleType(VehicleType.Miscellaneous);
                    this.setBodyType(BodyType.MotorHomesUpTo22Feet);
                    this.setMotorHomeHaveLivingQuarters(true);
                    this.setMotorHomeContentsCoverage(true);
                    this.setComprehensive(true);
                    this.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
                }});
            }
        };

        final ArrayList<Contact> personList = new ArrayList<Contact>() {
            private static final long serialVersionUID = 1L;

            {
                this.add(new Contact() {{
                    this.setGender(Gender.Male);
                    this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
                }});
            }
        };

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{

            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};

        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("AvailablityRules")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);

        System.out.println(myPolicyObjCPP.accountNumber);
    }
}


















