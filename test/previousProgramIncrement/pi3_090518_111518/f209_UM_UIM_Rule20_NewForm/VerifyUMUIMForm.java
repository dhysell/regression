package previousProgramIncrement.pi3_090518_111518.f209_UM_UIM_Rule20_NewForm;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.Vehicle;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author swathiAkarapu
 * @Requirement US16224
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/251066671772">US16224</a>
 * @Description
 * As a county office or home office user when doing a policy that has UM/UIM limits for the first time, a rewrite or being changed/updated I want to see the form inferred and available to print for signature.
 *
 * Requirements: New PL Product Model Spreadsheet  (refer to UW1022 edition 01 19)
 *
 * Steps to get there:
 * Do multiple new submissions as agent/PA
 * Have section III with UM/UIM limits & a policy with section III and UM/UIM limits rejected
 * Quote
 * See that there is form inferred and available to print for signature
 * Steps for other jobs:
 * As agent/PA or underwriter/coder do policy changes: adding new section III midterm; adding new car midterm to policy with UM/UIM and policy without UM/UIM (test just UM or UIM rejected also)
 * As agent/PA do all editable rewrite jobs
 * As underwriter/coder do all editable rewrite jobs including rewrite new account
 *
 * Acceptance criteria:
 * Ensure that UM/UIM form is inferred and available to print on new submission with section III (auto)
 * Ensure that UM/UIM form is inferred and available to print on policy change that adds a new section III (auto)
 * Ensure that UM/UIM form is inferred and available to print on policy change where UM/UIM limits are edited/changed; where UM/UIM has already been rejected and a new car is being added to policy
 * NOTE: if UM/UIM is not currently rejected and a new car is being added the form is not required
 * Ensure that UM/UIM form is inferred and available to print on Rewrite Full Term, Rewrite New Term and Rewrite New Account jobs
 * @DATE October  02
 */
public class VerifyUMUIMForm extends BaseTest {
    private GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
    private GeneratePolicy standardFirePolicy = null;
    public void generatePolicyWithUMandUIMselected() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
         SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh,
             repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwentyFiveK, true, repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty,
             true, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);


        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.FullApp);

    }

    @Test
    public void unInsuredAndUnderInsuredFormWhenUmandUIMselected() throws  Exception{
        generatePolicyWithUMandUIMselected();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);




        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);

        pcSideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");

    }

    private void generatePolicyWithUMandUIMrejected() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwentyFiveK, false, repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty,
                false, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);


        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.FullApp);

    }

    @Test
    public void unInsuredAndUnderInsuredFormWhenUmandUIMrejected() throws  Exception{
        generatePolicyWithUMandUIMrejected();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);

        pcSideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");

    }


    @Test
    public void policyChandeAddnewSectionIIIWithUMandUIMselected() throws  Exception{
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("cancel", "EditAdd")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);

        sideMenu.clickSideMenuLineSelection();
        repository.pc.workorders.generic.GenericWorkorderLineSelection lineSelection = new repository.pc.workorders.generic.GenericWorkorderLineSelection(driver);
        lineSelection.checkPersonalAutoLine(true);

        sideMenu.clickSideMenuQualification();
        repository.pc.workorders.generic.GenericWorkorderQualification qualificationPage = new repository.pc.workorders.generic.GenericWorkorderQualification(driver);
        qualificationPage.setSquireAutoFullTo(false);
        qualificationPage.setSquireGeneralFullTo(false);
        sideMenu.clickSideMenuPADrivers();

        paDrivers.addExistingDriver(myPolicyObject.pniContact.getLastName());
        paDrivers.selectMaritalStatus(repository.gw.enums.MaritalStatus.Single);
        paDrivers.selectGender(Gender.Male);
        paDrivers.setOccupation("Testing");
        paDrivers.setLicenseNumber("XW304757A");
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(repository.gw.enums.CommuteType.WorkFromHome);
        paDrivers.setPhysicalImpairmentOrEpilepsy(false);
        paDrivers.selectDriverHaveCurrentInsurance(false);
        paDrivers.setHowLongWithoutCoverage(repository.gw.enums.HowLongWithoutCoverage.NewDriver);
        paDrivers.clickOrderMVR();
        paDrivers.clickMotorVehicleRecord();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord motorRecord = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_MotorVehicleRecord(driver);
        motorRecord.selectMVRIncident(1, repository.gw.enums.SRPIncident.Waived);
        paDrivers.clickOK();

        sideMenu.clickSideMenuPAVehicles();
        repository.pc.workorders.generic.GenericWorkorderVehicles_Details vehicle = new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver);
        vehicle.createVehicleManually();
        vehicle.createGenericVehicle(Vehicle.VehicleTypePL.PrivatePassenger);
        vehicle.selectGaragedAtZip("ID");
        vehicle.selectDriverToAssign(myPolicyObject.pniContact.getLastName());
        vehicle.clickOK();

        sideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(true,  repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.TwentyFive);
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk_UWIssues.handleBlockSubmit(myPolicyObject);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        sideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");

    }

    /**
     *  where UM/UIM has already been rejected and a new car is being added to policy
     */

    @Test
    public void unInsuredAndUnderInsuredRejectedVerifyFormWhenPolicyChangeAddNewCar() throws  Exception{


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwentyFiveK, false, repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty,
                false, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);


        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        sideMenu.clickPolicyContractSectionIIIAutoLine();

        sideMenu.clickSideMenuPAVehicles();
        repository.pc.workorders.generic.GenericWorkorderVehicles_Details vehicle = new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver);
        vehicle.createVehicleManually();
        vehicle.createGenericVehicle(Vehicle.VehicleTypePL.PrivatePassenger);
        vehicle.selectGaragedAtZip("ID");
        vehicle.setNoDriverAssigned(true);
        vehicle.clickOK();

        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();

        sideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");

    }

    /**
     * NOTE: if UM/UIM is not currently rejected and a new car is being added the form is not required
     * @throws Exception
     */
    @Test
    public void unInsuredAndUnderInsuredNotRejectedVerifyFormWhenPolicyChangeAddNewCar() throws  Exception{


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwentyFiveK, true, repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty,
                true, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);


        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        sideMenu.clickPolicyContractSectionIIIAutoLine();

        sideMenu.clickSideMenuPAVehicles();
        repository.pc.workorders.generic.GenericWorkorderVehicles_Details vehicle = new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver);
        vehicle.createVehicleManually();
        vehicle.createGenericVehicle(Vehicle.VehicleTypePL.PrivatePassenger);
        vehicle.selectGaragedAtZip("ID");	
        vehicle.setNoDriverAssigned(true);
        vehicle.clickOK();

        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();

        sideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertFalse(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");

    }

    /**
     * Ensure that UM/UIM form is inferred and available to print on policy change where UM/UIM limits are edited/changed;
     */
    @Test
    public void unInsuredAndUnderInsuredSelectedVerifyFormWhenPolicyChangeWithLimits() throws  Exception{


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.FiftyHigh,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TwentyFiveK, true, repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty,
                true, repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit.Fifty);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);


        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(
                driver);

        // start policy change
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        policyChangePage.startPolicyChange("policy Change", currentSystemDate);
        sideMenu.clickPolicyContractSectionIIIAutoLine();
        sideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(true,  repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.TwentyFive);
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();

        sideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");

    }
    @Test
    public void verifyUIMFormRewriteFullTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(), myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();

        pcCancellationPage.setSource("Carrier");
        pcCancellationPage.setCancellationReason("policy rewritten or replaced (flat cancel)");
        pcCancellationPage.setExplanation("other");
        pcCancellationPage.setDescription("testing");
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();

        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteFullTerm();

        pcSideMenu.clickPolicyContractSectionIIIAutoLine();
        pcSideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(true,  repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty);

        StartRewrite rewriteWO = new StartRewrite(driver);
        rewriteWO.visitAllPages(myPolicyObject);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");


    }


    @Test
    public void verifyUIMFormRewriteNewTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);


        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(), myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();

        pcCancellationPage.setSource("Carrier");
        pcCancellationPage.setCancellationReason("policy rewritten or replaced (flat cancel)");
        pcCancellationPage.setExplanation("other");
        pcCancellationPage.setDescription("testing");
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();

        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteNewTerm();
        pcWorkOrder.clickNext();

        pcSideMenu.clickSideMenuQualification();
        repository.pc.workorders.generic.GenericWorkorderQualification pcQualificationPage = new repository.pc.workorders.generic.GenericWorkorderQualification(driver);
        pcQualificationPage.fillOutFullAppQualifications(myPolicyObject);
        pcSideMenu.clickPolicyContractSectionIIIAutoLine();
        pcSideMenu.clickSideMenuPACoverages();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setUninsuredCoverage(true,  repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit.Fifty);

        StartRewrite rewriteWO = new StartRewrite(driver);
        rewriteWO.visitAllPages(myPolicyObject);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");
    }


    @Test
    public void testRewriteNewAccount() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("UM", "UIM")
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObject.accountNumber);

        cancelPolicyForRewriteNewAccount(myPolicyObject.accountNumber);
         issueStandardFirePolicy();
       verifyFormOnRewriteNewAccountPolicy(standardFirePolicy, myPolicyObject);
    }

    private void cancelPolicyForRewriteNewAccount(String accountNumber) {

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);
        driver.quit();

    }

    private void issueStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<repository.gw.generate.custom.PolicyLocation> locationsList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> locationPropertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.DwellingPremisesCovE));
        repository.gw.generate.custom.PolicyLocation propertyLocation = new repository.gw.generate.custom.PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(50);
        propertyLocation.setPlNumResidence(25);
        locationsList.add(propertyLocation);



        standardFirePolicy = new GeneratePolicy.Builder(driver)

                .withProductType(repository.gw.enums.ProductLineType.StandardFire)
                .withLineSelection(repository.gw.enums.LineSelection.StandardFirePL)
                .withInsFirstLastName("StandardFire", "Policy")
                .withPolOrgType(repository.gw.enums.OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(repository.gw.enums.PaymentPlanType.Annual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
        driver.quit();
    }
    private void verifyFormOnRewriteNewAccountPolicy(GeneratePolicy stdPol, GeneratePolicy squirePol) throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdPol.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(squirePol.accountNumber, stdPol.accountNumber);
        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);

        accountSearchPC.searchAccountByAccountNumber(stdPol.accountNumber);
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPendingTransactionByStatus("Rewrite New Account");
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfo policyInfo = new repository.pc.workorders.generic.GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);
        policyInfo.clickPolicyInfoPrimaryNamedInsured();
        repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact policyInfoContactPage = new repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.clickUpdate();

        policyInfo.selectAddExistingOtherContactAdditionalInsured(squirePol.pniContact.getFirstName());
        repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
        ani.selectAdditionalInsuredRelationship(repository.gw.enums.RelationshipToInsured.Friend);
        ani.clickUpdate();

        sideMenu.clickSideMenuHouseholdMembers();
        repository.pc.workorders.generic.GenericWorkorderPolicyMembers houseHold = new repository.pc.workorders.generic.GenericWorkorderPolicyMembers(driver);
        int row = houseHold.getPolicyHouseholdMembersTableRow(stdPol.pniContact.getFirstName());
        houseHold.clickPolicyHouseHoldTableCell(row, "Name");

        repository.pc.workorders.generic.GenericWorkorderPolicyMembers householdMember = new repository.pc.workorders.generic.GenericWorkorderPolicyMembers(driver);
        householdMember.clickOK();
        houseHold.addExistingInsured(squirePol.pniContact.getFirstName());
        householdMember.clickRelatedContactsTab();
        householdMember.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        repository.pc.workorders.generic.GenericWorkorderInsuranceScore creditReport = new repository.pc.workorders.generic.GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(stdPol);
        sideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis risk = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuForms();
        List<String> formsDescriptions = new repository.pc.workorders.generic.GenericWorkorderForms(driver).getFormDescriptionsFromTable();
        Assert.assertTrue(formsDescriptions.contains("Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022"), "Idaho Uninsured Motorist and Underinsured Motorist Disclosure UW 1022 - not found");
    }
}
