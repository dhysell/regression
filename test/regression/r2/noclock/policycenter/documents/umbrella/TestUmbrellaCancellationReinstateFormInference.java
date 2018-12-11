package regression.r2.noclock.policycenter.documents.umbrella;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
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
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquirePropertyLiabilityExclusionsConditions;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.UnderlyingInsuranceUmbrella;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions;
import repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages;
import repository.pc.workorders.reinstate.StartReinstate;
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
public class TestUmbrellaCancellationReinstateFormInference extends BaseTest {

    private GeneratePolicy squireFullApp;
    private Underwriters uw;
    SquireEligibility squireType;

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


        ArrayList<String> namesDescs = new ArrayList<String>();
        namesDescs.add("test1descAndName");
        namesDescs.add("test2descAndName");
        SquirePropertyLiabilityExclusionsConditions propLiabExclusionsConditions = new SquirePropertyLiabilityExclusionsConditions() {
            {
                this.setVendorAsAdditionalInsuredEndorsement_207(true);
                this.setVendorAsAdditionalInsuredEndorsement_207_VendorNameList(namesDescs);
            }
        };

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;
        myPropertyAndLiability.propLiabExclusionsConditions = propLiabExclusionsConditions;

        Squire mySquire = new Squire(SquireEligibility.random());
        mySquire.squirePA = myAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.squireFullApp = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Cancel", "Umbrella")
                .withProductType(ProductLineType.Squire)
                .withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueSquire"})
    public void testBoundUmbrellaPolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squireFullApp.squireUmbrellaInfo = squireUmbrellaInfo;
        squireFullApp.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicySubmitted);

    }

    @Test(dependsOnMethods = {"testBoundUmbrellaPolicy"})
    public void testUmbrellaChanges() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.squireUmbrellaInfo.getPolicyNumber());

        SideMenuPC sideMenu = new SideMenuPC(driver);
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickIssuePolicy();

        sideMenu.clickSideMenuQualification();

        GenericWorkorderQualification quals = new GenericWorkorderQualification(driver);
        quals.setPL_UmbrellaPolicyHagerty(true);
        quals.clickNext();
        sideMenu.clickSideMenuSquireUmbrellaCoverages();

        GenericWorkorderSquireUmbrellaCoverages covs = new GenericWorkorderSquireUmbrellaCoverages(driver);
        covs.clickExclusionsConditions();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions excConds = new GenericWorkorderSquirePropertyAndLiabilityCoverages_ExclusionsAndConditions(driver);
        ArrayList<String> descs = new ArrayList<String>();
        descs.add("test1desc");
        excConds.clickSpecialEndorsementForLiability205(descs);
        excConds.sendArbitraryKeys(Keys.TAB);

        if (squireFullApp.squire.squireEligibility.equals(SquireEligibility.City)) {
            excConds.clickPersonalUmbrellaExcludedDriverEndorsement246(this.squireFullApp.squire.squirePA.getDriversList());
        } else {
            excConds.clickFarmAndRanchUmbrellaExcludedDriverEndorsement224(
                    this.squireFullApp.squire.squirePA.getDriversList());
        }

        excConds.clickUmbrellaLimitationEndorsement270();

        ArrayList<UnderlyingInsuranceUmbrella> underlyingInsurances = new ArrayList<UnderlyingInsuranceUmbrella>();
        UnderlyingInsuranceUmbrella undLyingIns1 = new UnderlyingInsuranceUmbrella();
        undLyingIns1.setCompany("TestCompany1");
        undLyingIns1.setPolicyNumber("1234567");
        undLyingIns1.setLimitOfInsurance("TestLimit1");
        underlyingInsurances.add(undLyingIns1);
        excConds.addAdditionalUnderlyingInsuranceUmbrellaEndorsement217Details(underlyingInsurances);

        sideMenu = new SideMenuPC(driver);
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

        sideMenu.clickSideMenuQuote();
        quote.issuePolicy(IssuanceType.NoActionRequired);

        GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
        submittedPage.waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();


    }

    @Test(dependsOnMethods = {"testUmbrellaChanges"})
    public void testInitiateCancellation() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.squire.getPolicyNumber());
        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

        String comment = "For Rewrite full term of the policy";
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

        new GuidewireHelpers(driver).logout();

        testValidateUmbrellaDocuments("Cancel");

    }

    @Test(dependsOnMethods = {"testInitiateCancellation"})
    private void testReinstateUmbrellaCoverages() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.squireFullApp.squire.getPolicyNumber());
        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        StartReinstate reinstatePolicy = new StartReinstate(driver);
        reinstatePolicy.setReinstateReason("Payment received");
        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.setDescription("Testing purpose");

        reinstatePolicy.quoteAndIssue();

        //Umbrella

        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(this.squireFullApp.squireUmbrellaInfo.getPolicyNumber());
        policyMenu.clickMenuActions();
        policyMenu.clickReinstatePolicy();
        reinstatePolicy.setReinstateReason("Payment received");
        cancelPol.setDescription("Testing purpose");

        reinstatePolicy.quoteAndIssue();

        new GuidewireHelpers(driver).logout();

        testValidateUmbrellaDocuments("Reinstate");
    }

    private void testValidateUmbrellaDocuments(String jobType) throws Exception {


        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.squireFullApp.squireUmbrellaInfo.getPolicyNumber());
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
        docs.selectRelatedTo(jobType);
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
                if (desc.equals(document)) {
                    documentFound = true;
                    break;
                }
            }

            if (documentFound) {
                testFailed = true;
                errorMessage = errorMessage + "Unexpected document : '" + document
                        + "' available in documents page. \n";
            }
        }
        if (testFailed)
            Assert.fail(errorMessage);

    }
}
