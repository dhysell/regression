package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
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
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderVehicles;

public class CATestClassCode extends BaseTest {

    private GeneratePolicy myPolObj;
    private StringBuilder userName = new StringBuilder();
    private StringBuilder password = new StringBuilder();
    StringBuilder accountNumber = new StringBuilder();
    private StringBuilder policyNumber = new StringBuilder();

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test(description = "Creates an account through Quick Quote")
    public void createPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{

            this.add(new Vehicle(2) {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});

        }};

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
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
        myPolObj = new GeneratePolicy.Builder(driver)

                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompanyDependingOnDay("Bruce", "Wayne", "Wayne Tech")
                .withPolOrgType(OrganizationType.Joint_Venture)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolObj.accountNumber);

        userName.append(myPolObj.agentInfo.getAgentUserName());
        password.append(myPolObj.agentInfo.getAgentPassword());
        accountNumber.append(myPolObj.accountNumber);

        System.out.println("#############\nAccount Number: " + accountNumber);
        System.out.println("Agent: " + userName + "\n#############");
    }

    /**
     * @Author bmartin
     * @Requirement If Vehicle Type =  "Trailer" ,  add a check box labeled 'No VIN' above the VIN field. When checked, <br>
     * the VIN number will not be required and system will assign a VIN unique to the policy. <br>
     * User will not be able to edit VIN when 'No VIN' is checked. 6/27/2016
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Commercial%20Auto/PC8%20-%20CA%20-%20QuoteApplication%20-%20Vehicle%20Information%20.xlsx">PC8 CA Quote Application Vehicle Information - UI Final - Section "Commercial Auto - Vehicles - Create Vehicles - Vehicle Information - Vehicle Details (Trailer)"</a>
     * @Description Checks the No VIN box and makes sure the VIN box is uneditable and not required.
     * @DATE July 26, 2016
     */
    @Test(description = "Checks the No VIN box and makes sure the VIN box is uneditable.",
            dependsOnMethods = {"createPolicy"})
    public void checkTrailerVINUneditable() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName.toString(), password.toString(), accountNumber.toString());

        SideMenuPC mySide = new SideMenuPC(driver);
        mySide.clickSideMenuCAVehicles();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderVehicles myVehicles = new GenericWorkorderVehicles(driver);
        myVehicles.checkTrailerVINUneditable();

    }

    @Test(description = "Run account through Issuance.",
            dependsOnMethods = {"createPolicy"})
    public void setAccountThroughFull() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolObj.convertTo(driver, GeneratePolicyType.FullApp);

        policyNumber.append(myPolObj.commercialPackage.getPolicyNumber());

        System.out.println("PolicyIssued \n");

    }

    /**
     * @Author bmartin
     * @Requirement If there is only one row added to the scheduled item under IDCA 31 3006, <br>
     * and if "Other" is chosen on Damaged Item and/or Damage Description, <br>
     * Explanation Of Damage column is required at Full App and should have the asterisk.    7/11/16<br>
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Commercial%20Auto/PC8%20-%20CA%20-%20QuoteApplication%20-%20Vehicle%20Information%20.xlsx">PC8 - CA - QuoteApplication - Vehicle Information</a>
     * @Description Validation Message on  Removal Of Property Damage Coverage
     * @DATE July 26, 2016
     */
    @Test(description = "Set damage to anything but other, and make sure the final field is uneditable. ",
            dependsOnMethods = {"setAccountThroughFull"})
    public void checkVehicleDamageValidation() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName.toString(), password.toString(), accountNumber.toString());

        SideMenuPC mySide = new SideMenuPC(driver);
        mySide.clickSideMenuCAVehicles();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderVehicles myVehicles = new GenericWorkorderVehicles(driver);
        myVehicles.checkRemovalOfPropertyDamageCoverage();

    }

    /**
     * @Author bmartin
     * @Requirement Checks to make sure the class code showing is the correct class code.
     * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20Auto%20Commercial/Class%20Code%20List.xlsx">Class Code List</a>
     * @Description Checks to make sure the class code showing is the correct class code.
     * @DATE Nov 11, 2015
     */


    /*
     * TEST WILL FAIL TILL US9699 IS COMPLETE.
     */

    //jlarsen 2/10/2017 TEST DISABLED BECAUSE OF A KNOWN ISSUE THAT CPP HASN'T FIGURED OUT HOW TO SOLVE IT YET.
    @Test(enabled = false,
            description = "Checks to make sure the class code showing is the correct class code.",
            dependsOnMethods = {"setAccountThroughFull"})
    public void checkClassCode() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchJob(userName.toString(), password.toString(), accountNumber.toString());

        SideMenuPC mySide = new SideMenuPC(driver);
        mySide.clickSideMenuCAVehicles();

        new GuidewireHelpers(driver).editPolicyTransaction();

        GenericWorkorderVehicles myVehicles = new GenericWorkorderVehicles(driver);
        myVehicles.checkClassCode(myPolObj);

    }

}
