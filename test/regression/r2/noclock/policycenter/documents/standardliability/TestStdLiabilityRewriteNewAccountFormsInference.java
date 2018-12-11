package regression.r2.noclock.policycenter.documents.standardliability;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing,US9327 - [Part III] Re-work inference for all forms, jobs and policies
 * @Description -
 * @DATE Mar 20, 2017
 */
public class TestStdLiabilityRewriteNewAccountFormsInference extends BaseTest {
    private GeneratePolicy myStdLibobj, myPolicyObjPL, stdLibLessThan5;
    private String polNumber1, polNumber2;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
    private Underwriters uw;
    private GeneratePolicy myAutoPolicyObj;

    private WebDriver driver;

    @Test
    public void testIssueStandardLibPolicyRewrite() throws Exception {
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

        myStdLibobj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }


    @Test(dependsOnMethods = {"testIssueStandardLibPolicyRewrite"})
    public void testValidateFormsNoOfAcres1() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStdLibobj.accountNumber);
        //Cancel policy
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        //Generate Squire Policy
        myPolicyObjPL = createPLAutoPolicy(GeneratePolicyType.PolicyIssued);

        //Rewrite New Account
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        login.loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(myStdLibobj.accountNumber, myPolicyObjPL.accountNumber);

        SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
        jobSearchPC.searchJobByAccountNumberAndJobType(myPolicyObjPL.accountNumber, TransactionType.Rewrite);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickPolicyHolderMembersByName(myPolicyObjPL.pniContact.getFirstName());
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListing("ID");
        householdMember.clickOK();

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

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickCoveragesExclusionsAndConditions();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        excConds.clickSpecialEndorsementForLiability205(descs);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.StdLiab_AddExclusionConditionSpecialEndorsementForLiability205);


        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        String errorMessage = "Account Number: " + myStdLibobj.accountNumber;
        boolean testfailed = false;
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + " ERROR: Documents for Rewrite New Account In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        sideMenu.clickSideMenuQuote();

        StartRewrite rewrite = new StartRewrite(driver);
        rewrite.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        polNumber1 = completePage.getPolicyNumber();
        guidewireHelpers.logout();

        if (testfailed) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testValidateFormsNoOfAcres1"})
    private void testValidateStdLibNewAccountDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), polNumber1);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Rewrite New Account");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"Special Endorsement for Liability", "Limited Employer's Liability Endorsement",
                "Combined Single Limit Endorsement (Farm Liability)", "Limited Pollution Coverage Endorsement",
                "Farmers Liability Policy Booklet", "Farm Liability Policy Declarations", "Notice of Policy on Privacy"};

        boolean testFailed = false;
        String errorMessage = "Account Number: " + myStdLibobj.accountNumber;
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

    //No of Acres < 5
    @Test
    public void testIssueStandardLibRewriteNewAccountAcresLessthan5() throws Exception {
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

        stdLibLessThan5 = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Guy", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }


    @Test(dependsOnMethods = {"testIssueStandardLibRewriteNewAccountAcresLessthan5"})
    public void testValidateFormsNoOfAcresLessThan5() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdLibLessThan5.accountNumber);
        //Cancel policy
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        //Generate Squire Policy
        myAutoPolicyObj = createPLAutoPolicy(GeneratePolicyType.PolicyIssued);

        //Rewrite New Account
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myAutoPolicyObj.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(stdLibLessThan5.accountNumber, myAutoPolicyObj.accountNumber);

        SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
        jobSearchPC.searchJobByAccountNumberAndJobType(myAutoPolicyObj.accountNumber, TransactionType.Rewrite);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.clickPolicyHolderMembersByName(myAutoPolicyObj.pniContact.getFirstName());
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.selectNotNewAddressListing("ID");
        householdMember.clickOK();

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

        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
        sideMenu.clickSideMenuForms();

        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);

        String errorMessage = "Account Number: " + stdLibLessThan5.accountNumber;
        boolean testfailed = false;
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + " ERROR: Documents for Rewrite New Account In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }

        sideMenu.clickSideMenuQuote();

        StartRewrite rewrite = new StartRewrite(driver);
        rewrite.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        polNumber2 = completePage.getPolicyNumber();
        guidewireHelpers.logout();

        if (testfailed) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testValidateFormsNoOfAcresLessThan5"})
    private void testValidateNewAccountDocumentsAcresLessThan5() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), polNumber2);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Rewrite New Account");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"Special Endorsement for Liability", "Combined Single Limit Endorsement (Personal Liability)",
                "Personal Liability Policy Booklet", "Personal Liability Policy Declarations",
                "Notice of Policy on Privacy"};

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

    private GeneratePolicy createPLAutoPolicy(GeneratePolicyType policyType) throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        return new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Driverslicense")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .build(policyType);

    }

}
