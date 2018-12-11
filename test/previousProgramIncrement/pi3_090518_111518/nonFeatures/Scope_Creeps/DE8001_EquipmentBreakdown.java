package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty;

/**
 * @Author swathiAkarapu
 * @Requirement DE8001
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/257560756176">DE8001</a>
 * @Description
 * Ensure that the premium all shows with section I premium for all instances of Equipment Breakdown on the policy. Premium for all coverage E outbuildings, FPP tools and/or irrigation equipment on policy and section IV tools and/or irrigation equipment should be included in the section I premium total.
 * @DATE october 4, 2018
 */

public class DE8001_EquipmentBreakdown extends BaseTest {

    private WebDriver driver;
    public GeneratePolicy myPolicyObject = null;

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withSquireEligibility(repository.gw.enums.SquireEligibility.FarmAndRanch)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                 .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
         }

    @Test(enabled = true)
    public void ensureEquipmentBreakDownOnSectionIPremium() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withSquireEligibility(repository.gw.enums.SquireEligibility.FarmAndRanch)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withPolOrgType(OrganizationType.Individual)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .isDraft()
                 .build(repository.gw.enums.GeneratePolicyType.FullApp);

        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();
        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(),myPolicyObject.accountNumber);

         SideMenuPC pcSideMenu = new SideMenuPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty pcFarmPersonalPropertyPage = new repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty(driver);
        SoftAssert softAssert = new SoftAssert();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis pcRiskAnalysisWorkorder = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkorder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        repository.pc.workorders.generic.GenericWorkorderPayment pcPaymentWorkorder = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcCompleteWorkorder = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages pcAddtionalCoveragesWorkorder = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);


        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuSquirePropertyCoverages();

        pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
        pcFarmPersonalPropertyPage.checkCoverageD(true);
        pcFarmPersonalPropertyPage.selectCoverages(repository.gw.enums.FPP.FarmPersonalPropertyTypes.Tools);
        pcAddtionalCoveragesWorkorder.setFarmPersonalPropertyRisk("A");
        pcFarmPersonalPropertyPage.addItem(repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes.GPS, 1, 1000, "GPS #1");


        pcFarmPersonalPropertyPage.selectCoverageType(repository.gw.enums.FPP.FPPCoverageTypes.BlanketInclude);
        pcFarmPersonalPropertyPage.selectDeductible(repository.gw.enums.FPP.FPPDeductible.Ded_500);
        pcSideMenu.clickSideMenuRiskAnalysis();


        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);

        softAssert.assertTrue(quotePage.getSectionILineCoveragesHasEquipmentBreakdown(), "Equipment breakdown not Found in section 1 ");
        softAssert.assertAll();
    }

    @Test(enabled = true)
    public void ensureEquipmentBreakDownOnSectionIPremium_policyChange() throws Exception {

        generatePolicy();
        Underwriters underwriterInfo = UnderwritersHelper.getRandomUnderwriter();

        new Login(driver).loginAndSearchAccountByAccountNumber(underwriterInfo.getUnderwriterUserName(), underwriterInfo.getUnderwriterPassword(),myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireFarmPersonalProperty pcFarmPersonalPropertyPage = new GenericWorkorderSquireFarmPersonalProperty(driver);
        SoftAssert softAssert = new SoftAssert();
         StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages pcAddtionalCoveragesWorkorder = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);


        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();

        policyChangePage.startPolicyChange("change property details", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        pcSideMenu.clickSideMenuSquirePropertyCoverages();



        pcPropertyDetailCoveragesPage.clickFarmPersonalProperty();
        pcFarmPersonalPropertyPage.checkCoverageD(true);
        pcFarmPersonalPropertyPage.selectCoverages(repository.gw.enums.FPP.FarmPersonalPropertyTypes.Tools);
        pcAddtionalCoveragesWorkorder.setFarmPersonalPropertyRisk("A");
        pcFarmPersonalPropertyPage.addItem(repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes.GPS, 1, 1000, "GPS #1");


        pcFarmPersonalPropertyPage.selectCoverageType(repository.gw.enums.FPP.FPPCoverageTypes.BlanketInclude);
        pcFarmPersonalPropertyPage.selectDeductible(repository.gw.enums.FPP.FPPDeductible.Ded_500);
        pcSideMenu.clickSideMenuRiskAnalysis();


        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);

        softAssert.assertTrue(quotePage.getSectionILineCoveragesHasEquipmentBreakdown(), "Equipment breakdown not Found in section 1 ");
        softAssert.assertAll();
    }

}
