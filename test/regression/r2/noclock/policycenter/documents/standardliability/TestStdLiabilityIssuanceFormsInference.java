package regression.r2.noclock.policycenter.documents.standardliability;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing,US9327 - [Part III] Re-work inference for all forms, jobs and policies
 * @Description -
 * @DATE Mar 20, 2017
 */
public class TestStdLiabilityIssuanceFormsInference extends BaseTest {

    private GeneratePolicy stdLibGreaterThan5, stdLibLessThan5;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
    private Underwriters uw;
    private WebDriver driver;

    @Test
    public void testGenerateStandardLiability() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        PolicyLocation propLoc = new PolicyLocation();
        propLoc.setPlNumAcres(3);
        propLoc.setPlNumResidence(2);
        locationsList.add(propLoc);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        stdLibGreaterThan5 = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

    }

    //StdLiability Issuance No of Acres >5
    @Test(dependsOnMethods = {"testGenerateStandardLiability"})
    public void testIssueStdLiability() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibGreaterThan5.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        sideMenu.clickSideMenuSquireProperty();

        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        location.clickEditLocation(1);
//        location.setAcres(7);
        location.clickOK();

        //UW I286
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_AddGeneralLiabilityLimitsCSLGreaterThanEqualTo5Acres);
        //UW I265
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_LimitedEmployersLiabilityEndorsement);
        //UW I287
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_LimitedPollutionCoverageEndorsement);
        // ID FL 02 01 Form
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_Job_SubmissionGreaterThanEqualTo5Acres);
        // ID FL 03 01 Form
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_FarmLiabilityPolicyDeclarations);
        // LG 10 01 Form
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_NoticeOfPolicyOnPrivacy);

        sideMenu.clickSideMenuSquireProperty();
        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickCoveragesExclusionsAndConditions();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        excConds.clickSpecialEndorsementForLiability205(descs);

        //UW I205
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_AddExclusionConditionSpecialEndorsementForLiability205);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        String errorMessage = "Account Number: " + stdLibGreaterThan5.accountNumber;
        boolean testfailed = false;
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + " ERROR: Documents for Issuance In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        sideMenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
        submittedPage.waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();

        if (testfailed) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testIssueStdLiability"})
    private void testValidateStdLibIssuanceDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibGreaterThan5.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Issuance");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"Special Endorsement for Liability", "Limited Employer's Liability Endorsement",
                "Combined Single Limit Endorsement (Farm Liability)", "Limited Pollution Coverage Endorsement",
                "Farmers Liability Policy Booklet", "Farm Liability Policy Declarations",
                "Notice of Policy on Privacy"};


        boolean testFailed = false;
        String errorMessage = "Account Number: " + stdLibGreaterThan5.accountNumber;
        for (String document : documents) {
            boolean documentFound = false;
            for (String desc : descriptions) {
                if (desc.equals(document)) {
                    documentFound = true;
                    break;
                }
            }

            if (!documentFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected document : '" + document + "' not available in documents page. \n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);

    }


    //No Of Acres < 5 Scenario

    @Test
    public void testGenerateStandardLiabilityAcresLessThan5() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        PolicyLocation propLoc = new PolicyLocation();
        propLoc.setPlNumAcres(10);
        propLoc.setPlNumResidence(2);
        locationsList.add(propLoc);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        stdLibLessThan5 = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);

    }

    //StdLiability Issuance No of Acres < 5
    @Test(dependsOnMethods = {"testGenerateStandardLiabilityAcresLessThan5"})
    public void testIssueStdLiabilityLessThan5() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibLessThan5.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        sideMenu.clickSideMenuSquireProperty();

        GenericWorkorderSquirePropertyAndLiabilityLocation location = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
        location.clickEditLocation(1);
//        location.setAcres(3);
        location.clickOK();

        //UW I285
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_AddGeneralLiabilityLimitsCSLLessThan5Acres);
        //ID PL 02 01
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_Job_SubmissionLessThan5Acres);
        //ID PL 03 01
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_PersonalLiabilityPolicyDeclarations);
        this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdLiab_Job_SubmissionGreaterThanEqualTo5Acres);
        this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdLiab_AddGeneralLiabilityLimitsCSLGreaterThanEqualTo5Acres);
        this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdLiab_LimitedEmployersLiabilityEndorsement);
        this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdLiab_LimitedPollutionCoverageEndorsement);
        this.eventsHitDuringSubmissionCreationPlusLocal.remove(DocFormEvents.PolicyCenter.StdLiab_FarmLiabilityPolicyDeclarations);

        sideMenu.clickSideMenuSquirePropertyCoverages();

        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickCoveragesExclusionsAndConditions();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        excConds.clickSpecialEndorsementForLiability205(descs);

        //UW I205
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_AddExclusionConditionSpecialEndorsementForLiability205);

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.Quote();
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils.getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        String errorMessage = "Account Number: " + stdLibLessThan5.accountNumber;
        boolean testfailed = false;
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + " ERROR: Documents for Issuance In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        sideMenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
        submittedPage.waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();

        if (testfailed) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testIssueStdLiabilityLessThan5"})
    private void testValidateStdLibIssuanceDocumentsAcresLessThan5() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibLessThan5.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Issuance");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"Special Endorsement for Liability", "Combined Single Limit Endorsement (Personal Liability)",
                "Personal Liability Policy Booklet", "Personal Liability Policy Declarations", "Notice of Policy on Privacy"};

        boolean testFailed = false;
        String errorMessage = "Account Number: " + stdLibLessThan5.accountNumber;
        for (String document : documents) {
            boolean documentFound = false;
            for (String desc : descriptions) {
                if (desc.equals(document)) {
                    documentFound = true;
                    break;
                }
            }

            if (!documentFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected document : '" + document + "' not available in documents page. \n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);
    }
}