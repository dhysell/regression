package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
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
import repository.gw.generate.custom.CPPCommercialAutoGarageKeepers;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderGaragekeepersCoverageCPP;

//import pc.workorders.generic.GenericWorkorderGaragekeepersCoverageCPP;
@QuarantineClass
public class CABusinessRulesGarageKeepers extends BaseTest {

    GeneratePolicy myPolicyObjCPP;

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
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList) {{
            this.setNonSpecificLocation(true);
        }});

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
        }};

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
                this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{
            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};


        // GENERATE POLICY
        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Validation Rules")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObjCPP.accountNumber);


    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void validateBussinesRules() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAGarageKeepers();

        GenericWorkorderGaragekeepersCoverageCPP garageKeepers = new GenericWorkorderGaragekeepersCoverageCPP(driver);
        List<WebElement> locationsInDropdown = garageKeepers.getLocationList();
        for (WebElement location : locationsInDropdown) {
            if (myPolicyObjCPP.commercialPackage.locationList.get(1).getAddress().getLine1().equals(location.getText())) {
                Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "GarageKeepers was allowed to select a Non-Specific Address.");
            }
        }
    }


    @Test(dependsOnMethods = {}, enabled = true)
    public void testGarageKeepersCoverages() throws Exception {
        boolean testFailed = false;
        String failureMessage = "";

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAGarageKeepers();

        CPPCommercialAutoGarageKeepers garageKeeper = new CPPCommercialAutoGarageKeepers(myPolicyObjCPP.pniContact.getAddress());

        GenericWorkorderGaragekeepersCoverageCPP garageKeepers = new GenericWorkorderGaragekeepersCoverageCPP(driver);
        garageKeepers.addGarageKeeperByLocation(garageKeeper);

        //Garagekeepers Coverage
        //� This only becomes available for selection when an exsiting location is added on the wizard step for Garagekeepers.
        if (!guidewireHelpers.isRequired("Garagekeepers Coverage")) {
            testFailed = true;
            failureMessage = failureMessage + "GarageKeepers was not required when it should have been\n";
        }

        //Collision
        //� This only becomes available for selection when an exsiting location is added on the wizard step for Garagekeepers.
        if (!guidewireHelpers.isElectable("Collision")) {
            testFailed = true;
            failureMessage = failureMessage + "Collision was not Electable when it should have been\n";
        }

        //Comprehensive
        //"� Comprehensive is not available if Specified Causes of Loss for Garagekeepers is selected.
        //	� This only becomes available for selection when an exsiting location is added on the wizard step for Garagekeepers."
        if (!guidewireHelpers.isElectable("Comprehensive")) {
            testFailed = true;
            failureMessage = failureMessage + "Comprehensive was not Electable when it should have been\n";
        }

        //Specified Causes of Loss
        //"� Specified Causes of Loss is not available if Comprehensive is selected for Garagekeepers.
        //	� This only becomes available for selection when an exsiting location is added on the wizard step for Garagekeepers."
        if (!guidewireHelpers.isElectable("Specified Causes of Loss")) {
            testFailed = true;
            failureMessage = failureMessage + "Specified Causes of Loss was not Electable when it should have been\n";
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + failureMessage);
        }


    }


}









