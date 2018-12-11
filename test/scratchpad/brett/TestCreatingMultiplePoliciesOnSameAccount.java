package scratchpad.brett;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class TestCreatingMultiplePoliciesOnSameAccount extends BaseTest {
    private GeneratePolicy myPolicyObj = null;
    private GeneratePolicy multiPolicyObj = null;
    private Agents agent;
    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));
        locationsList.get(0).getAddress().setType(AddressType.Billing);

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
        }};

        final ArrayList<Contact> driverList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{
            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(driverList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("MultiPolicy Creation")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {
        //ArrayList<AdditionalInterest> lienholderBuildingAdditionalInterests = new ArrayList<AdditionalInterest>();
        PolicyLocationBuilding lienholderBuilding = new PolicyLocationBuilding();
        //AdditionalInterest lienholderBuildingAddInterest = new AdditionalInterest(ContactSubType.Company);
        //lienholderBuildingAddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        //lienholderBuildingAddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Building);
        //lienholderBuildingAdditionalInterests.add(lienholderBuildingAddInterest);
        //lienholderBuilding.setAdditionalInterestList(lienholderBuildingAdditionalInterests);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locOneBuildingList.add(lienholderBuilding);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
        locationsList.get(0).getAddress().setType(AddressType.Billing);

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(myPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(myPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(myPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(myPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(myPolicyObj.pniContact.getAddress().getZip());

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.multiPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsPersonOrCompany(myPolicyObj.pniContact.getPersonOrCompany())
                .withInsCompanyName(myPolicyObj.pniContact.getCompanyName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(OrganizationType.LLC)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(myPolicyObj.paymentPlanType)
                .withDownPaymentType(myPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println(myPolicyObj.toString());
        System.out.println(multiPolicyObj.toString());
    }
}
