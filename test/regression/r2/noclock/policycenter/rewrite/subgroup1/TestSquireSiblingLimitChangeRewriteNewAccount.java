package regression.r2.noclock.policycenter.rewrite.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.CommuteType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAccountPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US10741: PL/Sibling - Limits were increased in FB rules, need
 * to inlcude in PC
 * @RequirementsLink <a href=
 * "https://rally1.rallydev.com/#/33274298124d/detail/userstory/98911260332">
 * Link Text</a>
 * @Description
 * @DATE Mar 20, 2017
 */
@QuarantineClass
public class TestSquireSiblingLimitChangeRewriteNewAccount extends BaseTest {
    private GeneratePolicy mySqPol;
    private GeneratePolicy myInitialSqPolicy;
    private Underwriters uw;

    private WebDriver driver;

    @Test
    private void testIssueSquirePol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myInitialSqPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Initial", "REDWINE")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    @Test(dependsOnMethods = {"testIssueSquirePol"})
    private void testSquireAutoSiblingPol() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        mySqPol = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Test", "Sibling")
                .build(GeneratePolicyType.FullApp);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchSubmission(mySqPol.agentInfo.getAgentUserName(), mySqPol.agentInfo.getAgentPassword(), mySqPol.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        Date dob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -22);

        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(mySqPol.pniContact.getFirstName());
        paDrivers.setAutoDriversDOB(dob);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.clickOk();

        sideMenu.clickSideMenuPolicyInfo();
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);

        // Tab line
        policyInfo.sendArbitraryKeys(Keys.TAB);
        policyInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.myInitialSqPolicy.squire.getPolicyNumber());
        new GuidewireHelpers(driver).clickNext();

        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk.performRiskAnalysisAndQuote(mySqPol);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        if (quotePage.hasBlockBind()) {
            risk.handleBlockSubmit(mySqPol);
        }

        guidewireHelpers.logout();


        guidewireHelpers.setPolicyType(mySqPol, GeneratePolicyType.FullApp);
        mySqPol.convertTo(driver, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testSquireAutoSiblingPol"})
    public void testInitiateRewriteNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.mySqPol.squire.getPolicyNumber());

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        new GuidewireHelpers(driver).logout();
    }


    @Test(dependsOnMethods = {"testInitiateRewriteNewAccount"})
    private void testSiblingLimitChangeAndValidateBusRules() throws Exception {

        Agents agentInfo = AgentsHelper.getRandomAgent();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Login login = new Login(driver);
        login.login(agentInfo.getAgentUserName(), agentInfo.getAgentPassword());

        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
        menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
        newSubmissionPage.fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Person, "Rewrite", "NewAccount", null, null, null, null, null);

        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
        createAccountPage.setDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -35));
        createAccountPage.setSubmissionCreateAccountBasicsAltID("NewAccount43543");
        createAccountPage.fillOutPrimaryAddressFields(new AddressInfo());
        createAccountPage.clickSubmissionCreateAccountUpdate();

        TopMenuAccountPC menuAccount = new TopMenuAccountPC(driver);
        menuAccount.clickFirst();
        AccountSummaryPC acctSummary = new AccountSummaryPC(driver);
        String accountNumber = acctSummary.getAccountNumber();

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();


        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        login.loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(this.mySqPol.accountNumber, accountNumber);
        acctSummary = new AccountSummaryPC(driver);
        acctSummary.clickAccountSummaryPendingTransactionByStatus("Rewrite New Account");

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);

        Date dob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -22);
        sidemenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
        hmember.clickPolicyHouseHoldTableCell(1, "Name");

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListingIfNotExist(mySqPol.pniContact.getAddress());
        householdMember.clickOK();
        sidemenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.addExistingDriver("NewAccount");
        paDrivers.setPhysicalImpairmentOrEpilepsy(true);
        paDrivers.setAutoDriversDOB(dob);
        paDrivers.sendArbitraryKeys(Keys.TAB);
        paDrivers.selectGender(Gender.Female);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
        paDrivers.setOccupation("Software");
        paDrivers.setLicenseNumber("ABCD1234");
        State state = State.Idaho;
        paDrivers.selectLicenseState(state);
        paDrivers.selectCommuteType(CommuteType.WorkFromHome);
        paDrivers.selectDriverHaveCurrentInsurance(true);
        paDrivers.setCurrentInsuranceCompanyPolicyNumber("Farm Bureau Policy #: 1B124576");
        paDrivers.clickOk();

        sidemenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.editVehicleByVehicleNumber(1);
        vehiclePage.selectDriverToAssign("NewAccount");
        vehiclePage.selectDriverVehicleUse(DriverVehicleUsePL.Principal);
        vehiclePage.clickOK();

        sidemenu.clickSideMenuPACoverages();
        // checking Squire Auto - Coverages for sibling policy
        LiabilityLimit limit = (guidewireHelpers.getRandBoolean()) ? LiabilityLimit.CSL300K : ((guidewireHelpers.getRandBoolean()) ? LiabilityLimit.OneHundredLow : LiabilityLimit.OneHundredHigh);

        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
        coveragePage.setLiabilityCoverage(limit);

        sidemenu.clickSideMenuPolicyInfo();
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);

        // Tab line
        guidewireHelpers.sendArbitraryKeys(Keys.TAB);
        policyInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.myInitialSqPolicy.squire.getPolicyNumber());
        new GuidewireHelpers(driver).clickNext();

        sidemenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.Quote();

        sidemenu.clickSideMenuRiskAnalysis();

        // validate UW block bind and block issuance
        boolean testFailed = false;
        String errorMessage = "";

        for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
            String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
            if (!currentUWIssueText.contains("Blocking Quote") && (!currentUWIssueText.contains("Blocking Issuance"))) {

                if (currentUWIssueText.contains("Sibling Policy Liability Limits")) {
                    testFailed = true;
                    errorMessage = errorMessage + "Expected error Bind Issue : 'Sibling Policy Liability Limits' is not displayed.";

                    break;
                }
            }
        }

        if (testFailed)
            Assert.fail(errorMessage);

    }

}
