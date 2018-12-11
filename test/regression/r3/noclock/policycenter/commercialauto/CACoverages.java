package regression.r3.noclock.policycenter.commercialauto;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoCommercialAutoLineCPP;
import repository.pc.workorders.generic.GenericWorkorderCommercialAutoCoveredVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author jlarsen
 * @Requirement http://projects.idfbins.com/policycenter/To%20Be%20Process/Commercial%20Package%20Policy%20(CPP)/Guidewire%20-%20General%20Liability/WCIC%20General%20Liability-Product-Model.xlsx
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description testing Commercial Auto coverages.
 * @DATE Apr 1, 2016
 */
public class CACoverages extends BaseTest {


    public GeneratePolicy myPolicyObjCPP = null;


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

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle(locationsList.get(0).getAddress()) {{
                this.setAdditionalInterest(new ArrayList<AdditionalInterest>() {{
                    this.add(new AdditionalInterest(ContactSubType.Company) {{
                        this.setAdditionalInterestSubType(AdditionalInterestSubType.CAVehicles);
                        this.setAdditionalInterestTypeCPP(AdditionalInterestTypeCPP.LossPayableClause_AudioVisualAndDataElectronicEquipmentCoverageAddedLimitsIDCA_31_3002);
                    }});
                }});
            }});
            this.add(new Vehicle(locationsList.get(0).getAddress()) {{
                this.setVehicleType(VehicleType.Miscellaneous);
                this.setBodyType(BodyType.MotorHomesUpTo22Feet);
                this.setMotorHomeHaveLivingQuarters(true);
                this.setMotorHomeContentsCoverage(true);
                this.setComprehensive(true);
                this.setComprehensiveDeductible(Comprehensive_CollisionDeductible.FiveHundred500);
            }});
        }};

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
                this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine());
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
                .isDraft()
                .build(GeneratePolicyType.FullApp);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testCommercialAutoOwnedLiabilityGroup() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCACommercialAutoLine();

        GenericWorkorderCommercialAutoCommercialAutoLineCPP caAuto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        caAuto.clickCoveragesTab();

        softAssert.assertTrue(guidewireHelpers.isSuggested("Liability"), "Liability is NOT Sugested.");
        softAssert.assertTrue(guidewireHelpers.isSuggested("Medical Payments CA 99 03"), "MedicalPayments CA 99 03 is NOT Sugested.");

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testCommercialAutoHiredAutoGroup() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCACommercialAutoLine();

        GenericWorkorderCommercialAutoCommercialAutoLineCPP caAuto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        caAuto.clickCoveragesTab();

        softAssert.assertTrue(guidewireHelpers.isSuggested("Hired Auto Liability"), "Hired Auto Liability is NOT Sugested.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Hired Auto Collision"), "Hired Auto Collision is NOT Electable.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Hired Auto Comprehensive"), "Hired Auto Comprehensive is NOT Electable.");

        //"� If under the screen ""Covered Vehicles"" and ANY is checked for Liability this Coverage becomes required. However if ANY is unchecked from Liability this Coverage is to remained Checked and the User if they want to will have to unselect it.
        //� If ANY is not checked this Coverages existences is to be Suggested."
        guidewireHelpers.logout();
        login.loginAndSearchSubmission("atubbs", "gw", myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACoveredVehicles();
        GenericWorkorderCommercialAutoCoveredVehicles coveredVehicles = new GenericWorkorderCommercialAutoCoveredVehicles(driver);
        coveredVehicles.clickEditCoveredVehicles();
        coveredVehicles.checkLiabiltiyAny(true);

        GenericWorkorder genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderSaveDraft();
        guidewireHelpers.logout();
        login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACommercialAutoLine();

        caAuto.clickCoveragesTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Hired Auto Liability"), "Hired Auto Liability is NOT Required when ANY Covered Vehicles is selected.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Hired Auto Collision"), "Hired Auto Collision is NOT Electable when ANY Covered Vehicles is selected.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Hired Auto Comprehensive"), "Hired Auto Comprehensive is NOT Electable when ANY Covered Vehicles is selected.");

        guidewireHelpers.logout();
        login.loginAndSearchSubmission("atubbs", "gw", myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACoveredVehicles();
        coveredVehicles = new GenericWorkorderCommercialAutoCoveredVehicles(driver);
        coveredVehicles.clickEditCoveredVehicles();
        coveredVehicles.checkLiabiltiyAny(false);

        genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderSaveDraft();

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testCommercialAutoNonOwnedGroup() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCACommercialAutoLine();

        GenericWorkorderCommercialAutoCommercialAutoLineCPP caAuto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        caAuto.clickCoveragesTab();

        softAssert.assertTrue(guidewireHelpers.isSuggested("Non-Owned Auto Liability"), "Non-Owned Auto Liability is NOT Sugested.");

        //"� If under the screen ""Covered Vehicles"" and ANY is checked for Liability this Coverage becomes required. However if ANY is unchecked from Liability this Coverage is to remained Checked and the User if they want to will have to unselect it.
        //� If ANY is not checked this Coverages existences is to be Suggested."
        guidewireHelpers.logout();
        login.loginAndSearchSubmission("atubbs", "gw", myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACoveredVehicles();
        GenericWorkorderCommercialAutoCoveredVehicles coveredVehicles = new GenericWorkorderCommercialAutoCoveredVehicles(driver);
        coveredVehicles.clickEditCoveredVehicles();
        coveredVehicles.checkLiabiltiyAny(true);

        GenericWorkorder genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderSaveDraft();

        guidewireHelpers.logout();
        login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACommercialAutoLine();

        caAuto.clickCoveragesTab();
        softAssert.assertTrue(guidewireHelpers.isRequired("Non-Owned Auto Liability"), "Non-Owned Auto Liability is NOT Required when ANY Covered Vehicles is selected.");

        guidewireHelpers.logout();
        login.loginAndSearchSubmission("atubbs", "gw", myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACoveredVehicles();
        coveredVehicles = new GenericWorkorderCommercialAutoCoveredVehicles(driver);
        coveredVehicles.clickEditCoveredVehicles();
        coveredVehicles.checkLiabiltiyAny(false);

        genwo = new GenericWorkorder(driver);
        genwo.clickGenericWorkorderSaveDraft();

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testVehicleAdditionalCoverageGroup() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);

        login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAVehicles();

        GenericWorkorderVehicles vehicles = new GenericWorkorderVehicles(driver);
        vehicles.editVehicleByVin(myPolicyObjCPP.commercialAutoCPP.getVehicleList().get(0).getVin());
        vehicles.clickVehicleAdditionalCoveragesTab();

        softAssert.assertTrue(guidewireHelpers.isElectable("Additional Named Insured For Designated Person Or Organization IDCA 31 3009"), "Additional Named Insured For Designated Person Or Organization IDCA 31 3009 is NOT Electable.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Audio, Visual And Data Electronic Equipment Coverage Added Limits CA 99 60"), "Audio, Visual And Data Electronic Equipment Coverage Added Limits CA 99 60 is NOT Electable.");

        vehicles.checkAudioVisualAndDataElectronicEquipmentCoverageAddedLimits(true);
        softAssert.assertTrue(guidewireHelpers.isRequired("Loss Payable Clause - Audio, Visual And Data Electronic Equipment Coverage Added Limits IDCA 31 3002"), "Loss Payable Clause - Audio, Visual And Data Electronic Equipment Coverage Added Limits IDCA 31 3002 is NOT Required.");
        vehicles.checkAudioVisualAndDataElectronicEquipmentCoverageAddedLimits(false);
        softAssert.assertTrue(guidewireHelpers.isElectable("Motor Carriers - Insurance For Non-Trucking Use CA 23 09"), "Motor Carriers - Insurance For Non-Trucking Use CA 23 09 is NOT Electable.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Rental Reimbursement CA 99 23"), "Rental Reimbursement CA 99 23 is NOT Electable.");

        vehicles.clickOK();


        //Fire, Fire And Theft, Fire, Theft And Windstorm And Limited Specified Causes Of Loss Coverages CA 99 14
//� This becomes available if on the Screen Vehicle Information > Tab Coverages > coverage Specificed Causes of Loss > check Fire / Fire, Theft, and Windstorm / Fire and Theft / Limited Specified Causes of Loss, attach CA 99 14 as required. This endorsement will be uneditable. However this endorsement is not to display if the term option Specified Causes of Loss is selected.

        //Lessor - Additional Insured And Loss Payee CA 20 01
//� This becomes available when Lessor - Additional Insured And Loss Payee is selected on the additional interest screen.

        //Mobile Homes Contents Coverage CA 20 16
//"� This becomes available if question ""Is contents coverage desired?"" is answered yes then automatically add Mobile Homes Contents Coverage CA 20 16
//		� This is available only when Comprehensive or Specified Causes of Loss is selected under the Coverages Tab on the Vehicle."

        //Rental Reimbursement CA 99 23
        //Electable

        //Auto Loan/Lease Gap Coverage CA 20 71
        //� Available when �Comprehensive�, �Collision�, or �Specified Causes of Loss� is selected.

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = {"generatePolicy"}, enabled = true)
    public void testCommercialAutoPhysicalDamageGroup() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAVehicles();

        GenericWorkorderVehicles_Details vehicles = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicles_Coverages = new GenericWorkorderVehicles_CoverageDetails(driver);
        vehicles.editVehicleByVin(myPolicyObjCPP.commercialAutoCPP.getVehicleList().get(0).getVin());
        vehicles.clickVehicleCoveragesTab();
        softAssert.assertTrue(guidewireHelpers.isElectable("Collision"), "Vehicle Collision is NOT Electable.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Comprehensive"), "Vehicle Comprehensive is NOT Electable.");
        softAssert.assertFalse(guidewireHelpers.isElectable("Specified Causes of Loss"), "Vehicle Specified Causes of Loss IS Electable.");

        vehicles.clickOK();
        vehicles.editVehicleByVin(myPolicyObjCPP.commercialAutoCPP.getVehicleList().get(1).getVin());
        vehicles_Coverages.clickVehicleCoveragesTab();
        softAssert.assertTrue(guidewireHelpers.isElectable("Collision"), "Vehicle Collision is NOT Electable.");
        softAssert.assertTrue(guidewireHelpers.isElectable("Comprehensive") || guidewireHelpers.isSuggested("Comprehensive"), "Vehicle Comprehensive is NOT Electable.");

        vehicles_Coverages.setComprehensive(true);
        softAssert.assertFalse(guidewireHelpers.isElectable("Specified Causes of Loss"), "Vehicle Specified Causes of Loss IS Electable when Collision is selected.");

        vehicles_Coverages.setComprehensive(false);
        vehicles_Coverages.setSpecifiedCauseOfLoss(true);
        softAssert.assertFalse(guidewireHelpers.isElectable("Comprehensive"), "Vehicle Comprehensive is Electable when Specified Causes Of Loss is selected.");

        //Roadside Assistance IDCA 31 3008
//� This is only available when Liability coverage is selected yes. This question is located under the Vehicle > Tab Details questions "Liability Coverage".
//� Add IDCA 31 3008 to all private passenger vehicles, light trucks and medium trucks less than 20,001 GVW, with liability coverage, as required. Other than that it is electable (not checked)

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void testCommercialAutoAdditionalCoverageGroups() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCACommercialAutoLine();

        GenericWorkorderCommercialAutoCommercialAutoLineCPP caAuto = new GenericWorkorderCommercialAutoCommercialAutoLineCPP(driver);
        caAuto.clickAdditionalCoveragesTab();

        softAssert.assertFalse(guidewireHelpers.isElectable("Coverage For Certain Operations In Connection With Railroads CA 20 70"), "Coverage For Certain Operations In Connection With Railroads CA 20 70 is Electable as Agent");
        softAssert.assertTrue(guidewireHelpers.isElectable("Drive Other Car - Comprehensive  CA 99 10"), "Drive Other Car - Comprehensive CA 99 10 is NOT Electable");//DOUBLE SPACE
        softAssert.assertTrue(guidewireHelpers.isElectable("Drive Other Car - Liability CA 99 10"), "Drive Other Car - Liability CA 99 10 is NOT Electable");
        softAssert.assertTrue(guidewireHelpers.isElectable("Drive Other Car - Medical Payments CA 99 10"), "Drive Other Car - Medical Payments CA 99 10 is NOT Electable");
        softAssert.assertTrue(guidewireHelpers.isElectable("Drive Other Car - Underinsured Motorists CA 99 10"), "Drive Other Car - Underinsured Motorists CA 99 10 is NOT Electable");
        softAssert.assertTrue(guidewireHelpers.isElectable("Drive Other Car - Uninsured Motorists CA 99 10"), "Drive Other Car - Uninsured Motorists CA 99 10 is NOT Electable");
        softAssert.assertTrue(guidewireHelpers.isElectable("Drive Other Car - Collision  CA 99 10"), "Drive Other Car - Collision CA 99 10 is NOT Electable");//DOUBLE SPACE
        caAuto.checkOtherCarLiability(true);
        softAssert.assertTrue(guidewireHelpers.isRequired("Drive Other Car - Named Individuals CA 99 10"), "Drive Other Car - Named Individuals CA 99 10 is NOT Electable");
        caAuto.checkOtherCarLiability(false);
        softAssert.assertTrue(guidewireHelpers.isElectable("MCS-90"), "MCS-90 is NOT Electable");

        guidewireHelpers.logout();
        login.loginAndSearchSubmission(UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Commercial, Underwriter.UnderwriterTitle.Underwriter).getUnderwriterUserName(), "gw", myPolicyObjCPP.accountNumber);

        sideMenu.clickSideMenuCACommercialAutoLine();
        caAuto.clickAdditionalCoveragesTab();
        softAssert.assertTrue(guidewireHelpers.isElectable("Coverage For Certain Operations In Connection With Railroads CA 20 70"), "Coverage For Certain Operations In Connection With Railroads CA 20 70 is NOT Electable as UnderWriter");

        if (guidewireHelpers.isElectable("Coverage For Certain Operations In Connection With Railroads CA 20 70")) {
            caAuto.checkCA_20_70(true);
            caAuto.setCA_20_70_HandRatedPremium("1000");
            caAuto.clickAddCA_20_70_Details();
            caAuto.setScheduledRailroad("B&O, Short Line");
            caAuto.setDesignatedJobSite("Monoploy Board");

            GenericWorkorder genWO = new GenericWorkorder(driver);
            genWO.clickGenericWorkorderSaveDraft();
            genWO.clickPolicyChangeNext();

            guidewireHelpers.logout();

            login.loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

            guidewireHelpers.editPolicyTransaction();

            sideMenu.clickSideMenuCACommercialAutoLine();

            caAuto.clickAdditionalCoveragesTab();
            softAssert.assertTrue(guidewireHelpers.isSuggested("Coverage For Certain Operations In Connection With Railroads CA 20 70"), "Coverage For Certain Operations In Connection With Railroads CA 20 70 is NOT Electable as Agent after UnderWriter added it.");

            caAuto.checkCA_20_70(false);
            softAssert.assertFalse(guidewireHelpers.isRequired("Coverage For Certain Operations In Connection With Railroads CA 20 70") || guidewireHelpers.isElectable("Coverage For Certain Operations In Connection With Railroads CA 20 70"), "Coverage For Certain Operations In Connection With Railroads CA 20 70 is Available/Required after unselected by Agent.");
        }


        //Motor Carrier Endorsement IDCA 31 3012
//� This becomes available when one of the following Secondary Class Types are selected: Truckers, Food Delivery, Dump and Transit Mix, or Contractors. This needs to stay on for the duration of the entire policy period. For example if I removed the secondary class type the endorsement needs to remain on the policy until renewal.


        //Individual Named Insured CA 99 17
//� This becomes available when an account is created as a person not a company.

        softAssert.assertAll();
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void testCommercialAutoOwnedVehicleGroupByState() throws Exception {
        SoftAssert softAssert = new SoftAssert();

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuCAStateInfo();
        softAssert.assertTrue(guidewireHelpers.isSuggested("Underinsured Motorists - Bodily Injury CA 31 18"), "Underinsured Motorists - Bodily Injury CA 31 18 is NOT Electable");
        softAssert.assertTrue(guidewireHelpers.isSuggested("Uninsured Motorists - Bodily Injury CA 31 15"), "Uninsured Motorists - Bodily Injury CA 31 15 is NOT Electable");

        softAssert.assertAll();
    }


}











