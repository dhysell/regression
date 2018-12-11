package regression.r2.noclock.policycenter.documents.umbrella;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.UnderlyingInsuranceUmbrella;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US10152: QA - Stabilization - Automation Forms Testing
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/userstory/84420444948">Link Text</a>
 * @Description : Umbrella Forms for all Jobs
 * @DATE Mar 29, 2017
 */
@QuarantineClass
public class TestUmbrellaRewriteNewTermFormInference extends BaseTest {

    private GeneratePolicy squireFullApp;
    private Underwriters uw;
    private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
    private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
    private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
    private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();

    private WebDriver driver;

    @Test()
    public void testIssueSquire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK);
        coverages.setUninsuredLimit(UninsuredLimit.CSL300K);
        coverages.setUnderinsuredLimit(UnderinsuredLimit.CSL300K);
        SquirePersonalAuto myAuto = new SquirePersonalAuto();
        myAuto.setCoverages(coverages);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();
        liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -55);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire(SquireEligibility.random());
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("FullTerm", "Umbrella")
                .withProductType(ProductLineType.Squire)
                .withSquireEligibility(SquireEligibility.random())
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPolTermLengthDays(79)
                .withPolEffectiveDate(newEff)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquire"})
    public void testIssueUmbrellaPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -55);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setEffectiveDate(newEff);
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squireFullApp.squireUmbrellaInfo = squireUmbrellaInfo;
        squireFullApp.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

        eventsHitDuringSubmissionCreationPlusLocal.addAll(this.squireFullApp.policyForms.eventsHitDuringSubmissionCreation);
        formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal
                .addAll(this.squireFullApp.policyForms.formsOrDocsExpectedBasedOnSubmissionEventsHit);
        formsOrDocsActualFromUISubmissionPlusLocal.addAll(this.squireFullApp.policyForms.formsOrDocsActualFromUISubmission);
        actualExpectedDocDifferencesSubmissionPlusLocal.setInExpectedNotInUserInterface(
                this.squireFullApp.policyForms.actualExpectedDocDifferencesSubmission.getInExpectedNotInUserInterface());
        actualExpectedDocDifferencesSubmissionPlusLocal.setInUserInterfaceNotInExpected(
                this.squireFullApp.policyForms.actualExpectedDocDifferencesSubmission.getInUserInterfaceNotInExpected());

    }

    @Test(dependsOnMethods = {"testIssueUmbrellaPolicy"})
    public void testInitiateCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                squireFullApp.squire.getPolicyNumber());

        Date currentDate = squireFullApp.squire.getEffectiveDate();
        Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
        StartCancellation cancelPol = new StartCancellation(driver);

        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose",
                cancellationDate, true);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testInitiateCancellation"})
    private void testSquireRewriteNewTermAddCoverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                squireFullApp.squire.getPolicyNumber());
        StartRewrite rewritePage = new StartRewrite(driver);
//		//        PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		policyMenu.clickRewriteNewTerm();
        rewritePage.startRewriteNewTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquireEligibility();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);
        eligibilityPage.clickNext();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
        lineSelection.clickNext();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);

        qualificationPage.setSquireHOFullTo(false, "Testing purpose");
        qualificationPage.clickPL_GLLosses(false);
        qualificationPage.clickPL_GLHazard(false);
        qualificationPage.clickPL_GLManufacturing(false);
        qualificationPage.clickQualificationGLhorses(false);
        qualificationPage.clickPL_GLLivestock(false);

        if (!squireFullApp.squire.squireEligibility.equals(SquireEligibility.City)) {
            qualificationPage.clickQualificationGLCattle(false);
        }
        qualificationPage.setSquireAutoFullTo(false);
        for (int i = 0; i < 8; i++) {
            eligibilityPage.clickNext();
        }

        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickSectionIICoveragesTab();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);

        SectionIICoverages sectionIICoverage = new SectionIICoverages(SectionIICoveragesEnum.PrivateLandingStrips, 0, 2);
        addCoverage.addCoverages(sectionIICoverage);
        addCoverage.setQuantity(sectionIICoverage);

        coverages.clickCoveragesExclusionsAndConditions();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> namesDescs = new ArrayList<String>();
        namesDescs.add("test1descAndName");
        namesDescs.add("test2descAndName");
        excConds.clickVendorAsAdditionalInsuredEndorsement207(namesDescs);


        for (int i = 0; i < 8; i++) {
            eligibilityPage.clickNext();
        }
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuRiskAnalysis();
        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();
        sideMenu.clickSideMenuQuote();
        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

    @Test(dependsOnMethods = {"testSquireRewriteNewTermAddCoverages"})
    private void testUmbrellaRewriteNewTermAddCoverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.squireUmbrellaInfo.getPolicyNumber());
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startRewriteNewTerm();
//		//        PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		policyMenu.clickRewriteNewTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuQualification();

        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
        quals.setUmbrellaQuestionsFavorably();
        quals.setPL_UmbrellaPolicyHagerty(true);
        sideMenu.clickSideMenuPolicyInfo();
        sideMenu.clickSideMenuPolicyInfo();

        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PersonalUmbrella_UnderlyingSquireHasPrivateLandingStrips);


        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_NoticeOfPolicyOnPrivacy);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PersonalUmbrella_UnderlyingSquireHas207);


        sideMenu.clickSideMenuSquireUmbrellaCoverages();
        sideMenu.clickSideMenuSquireUmbrellaCoverages();

        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);

        covs.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_2000000);

        covs.clickExclusionsConditions();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        excConds.clickSpecialEndorsementForLiability205(descs);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(
                DocFormEvents.PolicyCenter.PersonalUmbrella_AddExclusionConditionSpecialEndorsementForLiability205);
        excConds.sendArbitraryKeys(Keys.TAB);

        if (squireFullApp.squire.squireEligibility.equals(SquireEligibility.City)) {
            excConds.clickPersonalUmbrellaExcludedDriverEndorsement246(this.squireFullApp.squire.squirePA.getDriversList());
            this.eventsHitDuringSubmissionCreationPlusLocal
                    .add(DocFormEvents.PolicyCenter.PersonalUmbrella_AddExclusionConditionExcludedDriverEndorsement246);
            this.eventsHitDuringSubmissionCreationPlusLocal
                    .add(DocFormEvents.PolicyCenter.PersonalUmbrella_PersonalUmbrellaPolicyDeclarations);

        } else {
            excConds.clickFarmAndRanchUmbrellaExcludedDriverEndorsement224(
                    this.squireFullApp.squire.squirePA.getDriversList());
            this.eventsHitDuringSubmissionCreationPlusLocal
                    .add(DocFormEvents.PolicyCenter.PersonalUmbrella_AddExclusionConditionExcludedDriverEndorsement224);
            this.eventsHitDuringSubmissionCreationPlusLocal
                    .add(DocFormEvents.PolicyCenter.PersonalUmbrella_SilicaOrSilicaRelatedDustExclusionEndorsement219);

            this.eventsHitDuringSubmissionCreationPlusLocal
                    .add(DocFormEvents.PolicyCenter.PersonalUmbrella_FarmUmbrellaPolicyDeclarations);
        }

        excConds.clickUmbrellaLimitationEndorsement270();
        this.eventsHitDuringSubmissionCreationPlusLocal
                .add(DocFormEvents.PolicyCenter.PersonalUmbrella_AddExclusionConditionUmbrellaLimitationEndo270);

        ArrayList<UnderlyingInsuranceUmbrella> underlyingInsurances = new ArrayList<UnderlyingInsuranceUmbrella>();
        UnderlyingInsuranceUmbrella undLyingIns1 = new UnderlyingInsuranceUmbrella();
        undLyingIns1.setCompany("TestCompany1");
        undLyingIns1.setPolicyNumber("1234567");
        undLyingIns1.setLimitOfInsurance("TestLimit1");
        underlyingInsurances.add(undLyingIns1);
        excConds.addAdditionalUnderlyingInsuranceUmbrellaEndorsement217Details(underlyingInsurances);
        excConds.sendArbitraryKeys(Keys.TAB);

        this.eventsHitDuringSubmissionCreationPlusLocal
                .add(DocFormEvents.PolicyCenter.PersonalUmbrella_AdditionalUnderlyingInsuranceEndorsement217);
        this.eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.PL_PersonalLinesApplication);


        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

        risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        }
        sideMenu.clickSideMenuRiskAnalysis();
        risk.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuForms();
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
        boolean testfailed = false;
        String errorMessage = "Account Number: " + this.squireFullApp.accountNumber;
        this.formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
        this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
                .getListOfDocumentNamesForListOfEventNames(this.eventsHitDuringSubmissionCreationPlusLocal);
        this.actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
                this.formsOrDocsActualFromUISubmissionPlusLocal,
                this.formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);
        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + "ERROR: Documents for Rewrite New Term In UserInterface But Not in Expected - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInUserInterfaceNotInExpected() + "\n";
        }

        if (this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() > 0) {
            testfailed = true;
            errorMessage = errorMessage + "ERROR: Documents for Rewrite New Term In Expected But Not in UserInterface - "
                    + this.actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface() + "\n";
        }
        sideMenu.clickSideMenuQuote();
        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());


        if (testfailed) {
            Assert.fail(errorMessage);
        }

    }

    @Test(dependsOnMethods = {"testUmbrellaRewriteNewTermAddCoverages"})
    private void testValidateUmbrellaDocuments() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.squireUmbrellaInfo.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo("Rewrite New Term");
        docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

        String[] documents = {"Special Endorsement for Liability", "Additional Insured - Vendor's Endorsement",
                "Umbrella Limitation Endorsement", "Personal Umbrella Excluded Driver Endorsement",
                "Silica or Silica-Related Dust Exclusion Endorsement",
                "Farm and Ranch Umbrella Excluded Driver Endorsement", "Farm Umbrella Policy Declarations",
                "Farm & Ranch Umbrella Policy Booklet", "Personal Umbrella Policy Booklet",
                "Personal Umbrella Policy Declarations", "Underlying Insurance",
                "Private Airstrip Umbrella Endorsement", "Notice of Policy on Privacy", "Personal Lines Application"};

        boolean testFailed = false;
        String errorMessage = "Account Number: " + this.squireFullApp.accountNumber;
        for (String document : documents) {
            boolean documentFound = false;
            for (String desc : descriptions) {
                if (desc.contains(document)) {
                    documentFound = true;
                    break;
                }

                if (this.squireFullApp.squire.squireEligibility.equals(SquireEligibility.City)
                        && (desc.equals("Personal Umbrella Excluded Driver Endorsement")
                        || desc.equals("Personal Umbrella Policy Booklet")
                        || desc.equals("Personal Umbrella Policy Declarations"))) {

                    documentFound = true;
                    break;
                }

                if (!this.squireFullApp.squire.squireEligibility.equals(SquireEligibility.City)
                        && (desc.equals("Silica or Silica-Related Dust Exclusion Endorsement")
                        || desc.equals("Farm and Ranch Umbrella Excluded Driver Endorsement")
                        || desc.equals("Farm & Ranch Umbrella Policy Booklet")
                        || desc.equals("Farm Umbrella Policy Declarations"))) {
                    documentFound = true;
                    break;
                }
            }

            if (!documentFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected document : '" + document
                        + "' not available in documents page. \n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);

    }
}
