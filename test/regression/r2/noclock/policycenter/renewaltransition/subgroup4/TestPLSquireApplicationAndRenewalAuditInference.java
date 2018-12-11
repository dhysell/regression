package regression.r2.noclock.policycenter.renewaltransition.subgroup4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.OkCancel;
import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AddressType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.DocFormEvents.PolicyCenter;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoContact;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.renewal.StartRenewal;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/*
 * US13621: Don't force user to click all Risk Analysis tabs to Quote or Submit
 * This test will fail as the latest code is not in REGR environment.
 */

/**
 * @Author skandibanda
 * @Requirement :US8283: [Part II] PL - Application and Renewal Audit inference
 * @Description : Issue squire policy and add forms/verifying the PL 01 01 and PL 33 01 forms generated under PL Applications and
 * Renewals
 * @DATE Oct 10, 2016
 */
@QuarantineClass
public class TestPLSquireApplicationAndRenewalAuditInference extends BaseTest {
    private GeneratePolicy squireRenewalPolicyObj, rewriteNewTermPolicyObj, myPolicyObj, standardFirePolicy, squirePolicyObj;

    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringRenewalCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();


    private Underwriters uw;
    private WebDriver driver;

    //Squire Renewal
    @Test
    public void testFormInference_GenerateRenewal() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;

        squireRenewalPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Test", "Renewal")
                .withPolTermLengthDays(80)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    //Forms Inference PL Application Form check for Renewal job
    @Test(dependsOnMethods = {"testFormInference_GenerateRenewal"})
    public void testFormInference_RenewalJob() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squireRenewalPolicyObj.squire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();

        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();

        renewal.waitForPreRenewalDirections();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(squireRenewalPolicyObj);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);
        this.eventsHitDuringRenewalCreationPlusLocal.add(PolicyCenter.Squire_PersonalLinesApplication);

        this.eventsHitDuringRenewalCreationPlusLocal.add(PolicyCenter.Squire_PersonalLinesRenewalApplication);
        SideMenuPC sideMenu = new SideMenuPC(driver);

        sideMenu.clickSideMenuRiskAnalysis();

        sideMenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);

        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringRenewalCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0)
            Assert.fail("ERROR: Form for ActionAdverseLetter In Expected But Not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface());


    }

    //Squire Submission
    @Test
    public void testFormInference_GenerateSquireIssuance() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("Test", "PLApplication")
                .build(GeneratePolicyType.FullApp);

        this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.PL_PersonalLinesApplication);
        new Login(driver).loginAndSearchSubmission(myPolicyObj.agentInfo.agentUserName, myPolicyObj.agentInfo.agentPassword, myPolicyObj.accountNumber);
        validateSquirePLApplication("Full App");

        //Bind and Issue policy
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPayment();
        paymentPage.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);
        paymentPage.clickGenericWorkorderSubmitOptionsSubmit();
        new GuidewireHelpers(driver).logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");

        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        try {
            activityPopupPage.setUWIssuanceActivity();
        } catch (Exception e) {
            activityPopupPage.clickCloseWorkSheet();
        }

        sidemenu.clickSideMenuQualification();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
        sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuQuote();

        quotePage.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

    //Squire Policy Change
    @Test(dependsOnMethods = {"testFormInference_GenerateSquireIssuance"})
    public void testFormInference_PolicyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

        //start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", changeDate);
        SideMenuPC sideMenuStuff = new SideMenuPC(driver);
        sideMenuStuff.clickSideMenuQualification();

        this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.PL_PersonalLinesApplication);
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sideMenuStuff.clickSideMenuQuote();

        validateSquirePLApplication("Policy Change");

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickIssuePolicy();
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        guidewireHelpers.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

    //Squire Rewrite Full Term
    //Forms Inference Adverse Action Letter check for Rewrite FullTerm job
    @Test(dependsOnMethods = {"testFormInference_GenerateSquireIssuance"})
    public void testFormInference_RewriteFullTerm() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);

        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();
        if (guidewireHelpers.isAlertPresent()) {
            guidewireHelpers.selectOKOrCancelFromPopup(OkCancel.OK);
        }
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuLineSelection();
        sideMenu.clickSideMenuQualification();
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuHouseholdMembers();
        sideMenu.clickSideMenuPLInsuranceScore();
        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPACoverages();
        sideMenu.clickSideMenuPAVehicles();
        sideMenu.clickSideMenuClueAuto();
        sideMenu.clickSideMenuSquireLineReview();

        sideMenu.clickSideMenuPAModifiers();

        this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.PL_PersonalLinesApplication);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        validateSquirePLApplication("Rewrite Full Term");
    }

    //Rewrite New Term
    @Test
    public void testFormInference_GenerateRewriteNewTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -55);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        rewriteNewTermPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Test", "NewTerm")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Forms Inference Adverse Action Letter check for Rewrite New Term
    @Test(dependsOnMethods = {"testFormInference_GenerateRewriteNewTerm"})
    public void testFormInference_RewriteNewTermJob() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewTermPolicyObj.squire.getPolicyNumber());

        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = rewriteNewTermPolicyObj.squire.getEffectiveDate();
        Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", cancellationDate, true);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewTermPolicyObj.squire.getPolicyNumber());

        //rewriteNewTerm
//        PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		//		policyMenu.clickRewriteNewTerm();
        new StartRewrite(driver).startRewriteNewTerm();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);

        eligibilityPage.clickNext();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.clickNext();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQualificationNext();
        qualificationPage.clickPL_PALosses(false);
        qualificationPage.clickPL_Traffic(false);
        qualificationPage.click_ConvictedOfAnyFelony(false);
        qualificationPage.clickPL_Business(false);
        qualificationPage.clickPL_Hagerty(false);
        qualificationPage.clickPL_Cancelled(false);
        qualificationPage.clickPL_Bankruptcy(false);

        new GuidewireHelpers(driver).clickNext();
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        houseHold.clickNext();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.clickNext();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPACoverages();
        sideMenu.clickSideMenuPAVehicles();
        sideMenu.clickSideMenuClueAuto();
        sideMenu.clickSideMenuSquireLineReview();

        sideMenu.clickSideMenuPAModifiers();

        this.eventsHitDuringSubmissionCreationPlusLocal.add(PolicyCenter.PL_PersonalLinesApplication);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        validateSquirePLApplication("Rewrite New Term");
    }


    @Test
    public void testGenerateSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Account1", "Squire")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Rewrite New Account
    @Test(dependsOnMethods = {"testGenerateSquirePolicy"})
    public void testRewriteNewAccount() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

        testCancelPolicy(squirePolicyObj.accountNumber);
        testIssueStandardFirePolicy();
        testRewriteNewAccountPolicy(standardFirePolicy, squirePolicyObj);
        validateSquirePLApplication("Rewrite New Account");
    }

    private void testIssueStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(2);
        locationsList.add(propertyLocation);

        standardFirePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("StandardFire", "Policy")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Cancel Policy with reasons
    private void testCancelPolicy(String accountNumber) {

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

    }

    //Rewrite New Account with UW
    private void testRewriteNewAccountPolicy(GeneratePolicy stdPol, GeneratePolicy squirePol) throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdPol.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(squirePol.accountNumber, stdPol.accountNumber);

        rewritePage.click_link_gototheRewriteAccount();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);
        policyInfo.clickPolicyInfoPrimaryNamedInsured();
        GenericWorkorderPolicyInfoContact policyInfoContactPage = new GenericWorkorderPolicyInfoContact(driver);
        policyInfoContactPage.clickUpdate();

        policyInfo.selectAddExistingOtherContactAdditionalInsured(squirePol.pniContact.getFirstName());
        GenericWorkorderAdditionalNamedInsured ani = new GenericWorkorderAdditionalNamedInsured(driver);
        ani.selectAdditionalInsuredRelationship(RelationshipToInsured.Friend);
        ani.selectAddtionalInsuredAddress(stdPol.pniContact.getAddress().getLine1());
        ani.clickUpdate();
        policyInfo.setMembershipDues(squirePol.pniContact.getFirstName(), true);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
        int row = houseHold.getPolicyHouseholdMembersTableRow(stdPol.pniContact.getFirstName());
        houseHold.clickPolicyHouseHoldTableCell(row, "Name");

        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        AddressInfo address = new AddressInfo("6315 W YORK ST", "", "BOISE", State.Idaho, "837047573", CountyIdaho.Ada, "United States", AddressType.Home);
        householdMember.selectNotNewAddressListingIfNotExist(address);
        householdMember.clickOK();
        sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(stdPol);
        sideMenu.clickSideMenuPADrivers();
        sideMenu.clickSideMenuPACoverages();
        sideMenu.clickSideMenuPAVehicles();
        sideMenu.clickSideMenuClueAuto();
        sideMenu.clickSideMenuSquireLineReview();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
    }

    private void validateSquirePLApplication(String jobType) throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0)
            Assert.fail("ERROR: Job Type: " + jobType + " - Form for ActionAdverseLetter In Expected But Not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface());

    }

}