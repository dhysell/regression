package regression.r2.noclock.policycenter.renewaltransition.subgroup4;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.DocFormEvents.PolicyCenter;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQualification;
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
 * @Description : Issue  Standard Fire and adding the forms/verifying the PL 01 01 and PL 33 01 forms generated under PL Applications and Renewals
 * @DATE Oct 10, 2016
 */
@QuarantineClass
public class TestStdFirePLApplicationAndRenewalAuditInference extends BaseTest {
    private GeneratePolicy stdFireRenewalPolicyObj, stdFirereWrieFullTermObj, rewriteNewTermPolicyObj;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocalStdFireRenewal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocalStdFirePersonalLines = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
    private Underwriters uw;

    private WebDriver driver;

    //Renewal
    @Test()
    public void testStdFire_GenerateRenewal() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -287);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(5);
        locationsList.add(propertyLocation);

        stdFireRenewalPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("StdFire", "Renewal")
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

    }

    //Forms Inference Adverse Action Letter form check for Renewal job
    @Test(dependsOnMethods = {"testStdFire_GenerateRenewal"})
    public void testStdFire_RenewalJob() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login with UW
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFireRenewalPolicyObj.standardFire.getPolicyNumber());

        StartRenewal renewal = new StartRenewal(driver);
        renewal.startRenewal();
        InfoBar infoBar = new InfoBar(driver);
        infoBar.clickInfoBarPolicyNumber();
        renewal.waitForPreRenewalDirections();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
        preRenewalPage.closePreRenewalExplanations(stdFireRenewalPolicyObj);

        PolicySummary policySummary = new PolicySummary(driver);
        policySummary.clickPendingTransaction(TransactionType.Renewal);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        this.eventsHitDuringSubmissionCreationPlusLocalStdFireRenewal.add(PolicyCenter.StdFireLiabRenewal);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocalStdFireRenewal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0)
            Assert.fail("ERROR: Form for ActionAdverseLetter In Expected But Not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface());

    }

    @Test()
    public void testStdFire_GenerateRewriteNewTerm() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -55);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(5);
        locationsList.add(propertyLocation);

        rewriteNewTermPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Test", "Rewrite")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Forms Inference Adverse Action Letter form check for Rewrite New Term job
    @Test(dependsOnMethods = {"testStdFire_GenerateRewriteNewTerm"})
    public void testStdFire_RewriteNewTermJob() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewTermPolicyObj.standardFire.getPolicyNumber());

        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = rewriteNewTermPolicyObj.standardFire.getEffectiveDate();
        Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", cancellationDate, true);
        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewTermPolicyObj.standardFire.getPolicyNumber());

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
        qualificationPage.clickPL_Cancelled(false);
        qualificationPage.clickPL_Bankruptcy(false);

        qualificationPage.clickPL_STDFireSupportingBusiness(true);
        qualificationPage.clickPL_HOLosses(false);
        qualificationPage.clickPL_HOPriorInsurance(false);
        qualificationPage.clickPL_HOExistingDamage(false);
        //qualificationPage.clickPL_HOFloodInsurance(false);
        //qualificationPage.clickPL_HOBusinessConducted(false);
        qualificationPage.clickQualificationNext();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        sideMenu.clickSideMenuPLInsuranceScore();

        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuSquirePropertyDetail();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyCLUE();

        sideMenu.clickSideMenuPAModifiers();

        this.eventsHitDuringSubmissionCreationPlusLocalStdFirePersonalLines.add(PolicyCenter.StdFireLiab);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        validateStdFirePLApplication();
    }

    @Test()
    public void testStdFire_GenerateStandardFire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locationPropertyList = new ArrayList<PLPolicyLocationProperty>();
        locationPropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        PolicyLocation propertyLocation = new PolicyLocation(locationPropertyList);
        propertyLocation.setPlNumAcres(10);
        propertyLocation.setPlNumResidence(5);
        locationsList.add(propertyLocation);

        stdFirereWrieFullTermObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withInsFirstLastName("Test", "FullTerm")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    //Forms Inference Adverse Action Letter form check for Rewrite FullTerm job
    @Test(dependsOnMethods = {"testStdFire_GenerateStandardFire"})
    public void testStdFire_RewriteFullTerm() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirereWrieFullTermObj.accountNumber);

        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);


        new GuidewireHelpers(driver).logout();
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdFirereWrieFullTermObj.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickRewriteFullTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuHouseholdMembers();
        sideMenu.clickSideMenuPLInsuranceScore();

        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuSquirePropertyDetail();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        sideMenu.clickSideMenuSquirePropertyCLUE();

        sideMenu.clickSideMenuPAModifiers();

        this.eventsHitDuringSubmissionCreationPlusLocalStdFirePersonalLines.add(PolicyCenter.StdFireLiab);

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();

        validateStdFirePLApplication();

    }

    private void validateStdFirePLApplication() throws Exception {

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocalStdFirePersonalLines);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal, this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0)
            Assert.fail("ERROR: Form for ActionAdverseLetter In Expected But Not in UserInterface - " + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface());

    }

}
