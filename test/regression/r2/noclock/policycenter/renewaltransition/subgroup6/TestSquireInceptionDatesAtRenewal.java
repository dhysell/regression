package regression.r2.noclock.policycenter.renewaltransition.subgroup6;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.InlandMarineTypes.BoatType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.WatercraftTypes;
import repository.gw.enums.InlandMarineTypes.WatercratItems;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMWatercraft;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.WatercraftScheduledItems;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

/**
 * @Author nvadlamudi
 * @Requirement : US9185: Update to Inception Date
 * @RequirementsLink <a href=
 * "http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Squire/PC8%20-%20Squire%20-%20QuoteApplication%20-%20Inception%20Date.xlsx">Link
 * Text</a>
 * @Description
 * @DATE Nov 16, 2016
 */
@QuarantineClass
public class TestSquireInceptionDatesAtRenewal extends BaseTest {
    private GeneratePolicy myCurrentSQPol;

    private WebDriver driver;

    @Test()
    private void testcreateSquireFullApp() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

        // Inland Marine
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Watercraft);

        // Watercraft
        ArrayList<AdditionalInterest> loc2Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
        loc2Bldg1AdditionalInterests.add(loc2Bldg1AddInterest);
        WatercraftScheduledItems boat = new WatercraftScheduledItems(WatercraftTypes.BoatAndMotor,
                DateUtils.convertStringtoDate("01/01/1999", "MM/dd/yyyy"), 10000);
        boat.setLimit(3123);
        boat.setItem(WatercratItems.Boat);
        boat.setLength(28);
        boat.setBoatType(BoatType.Outboard);
        ArrayList<WatercraftScheduledItems> boats = new ArrayList<WatercraftScheduledItems>();
        boats.add(boat);
        ArrayList<String> boatAddInsured = new ArrayList<String>();
        boatAddInsured.add("Cor Hofman");
        SquireIMWatercraft boatType = new SquireIMWatercraft(WatercraftTypes.BoatAndMotor,
                PAComprehensive_CollisionDeductible.OneHundred100, boats);
        boatType.setAdditionalInterest(loc2Bldg1AdditionalInterests);
        ArrayList<SquireIMWatercraft> boatTypes = new ArrayList<SquireIMWatercraft>();
        boatTypes.add(boatType);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.watercrafts_PL_IM = boatTypes;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        myCurrentSQPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withSquireEligibility(SquireEligibility.Country)
                .withInsFirstLastName("Inception", "Renewal")
                .withPolTermLengthDays(79)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testcreateSquireFullApp"})
    private void testAddCommissionInceptionDateToSquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchSubmission("lbarber", "gw", myCurrentSQPol.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickEditPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        // Commission

        polInfo.clickInceptionDateByRowAndColumnHeader(2, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(2, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        polInfo.clickInceptionDateByRowAndColumnHeader(3, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(3, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        polInfo.clickInceptionDateByRowAndColumnHeader(4, "New");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setOverrideCommissionReason(4, "Testing purpose");
        polInfo.sendArbitraryKeys(Keys.TAB);

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.performRiskAnalysisAndQuote(myCurrentSQPol);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.hasBlockBind()) {
            riskAnalysis.handleBlockSubmit(myCurrentSQPol);
        }

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        guidewireHelpers.setPolicyType(myCurrentSQPol, GeneratePolicyType.FullApp);
        myCurrentSQPol.convertTo(driver, GeneratePolicyType.PolicyIssued);


    }

    @Test(dependsOnMethods = {"testAddCommissionInceptionDateToSquire"})
    private void testRenewSquire() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        new Login(driver).loginAndSearchPolicyByAccountNumber("lbarber", "gw", myCurrentSQPol.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickRenewPolicy();

        BCTopMenu topMenu = new BCTopMenu(driver);
        topMenu.clickDesktopTab();


        new BatchHelpers(driver).runBatchProcess(BatchProcess.PC_Workflow);

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(myCurrentSQPol);

        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
        searchJob.searchJobByAccountNumber(myCurrentSQPol.accountNumber, "003");

        sideMenu.clickSideMenuPLInsuranceScore();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        sideMenu.clickSideMenuPolicyInfo();
        String errorMessage = "";

        if (polInfo.isInceptionDateCheckBoxCheckedByRowColumn(2, "New") && polInfo.getOverrideCommissionReason(2).trim() != "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 2 is not cleared.\n";
        }

        if (polInfo.isInceptionDateCheckBoxCheckedByRowColumn(3, "New") && polInfo.getOverrideCommissionReason(3) != "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 3 is not cleared.\n";
        }

        if (polInfo.isInceptionDateCheckBoxCheckedByRowColumn(4, "New") && polInfo.getOverrideCommissionReason(4) != "") {
            errorMessage = errorMessage + "Overrride Commission checkbox and reason in row: 4 is not cleared.\n";
        }


        polInfo.setTransferedFromAnotherPolicy(true);

        Date newInceptionDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -10);

        //Entering invalid policy to get UW Issue
		polInfo.setCheckBoxInInceptionDateByRow(1, true);
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setInceptionDateByRow(1, newInceptionDate);
        polInfo.setInceptionDatePolicyNumberDirectly(InceptionDateSections.Policy, "01-111111-01");
        polInfo.sendArbitraryKeys(Keys.TAB);
        polInfo.setInceptionDateByRow(1, polInfo.getPolicyInfoEffectiveDate());
        polInfo.sendArbitraryKeys(Keys.TAB);


        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickQuote();

        if (errorMessage != "") {
            Assert.fail(errorMessage);
        }

    }
}
