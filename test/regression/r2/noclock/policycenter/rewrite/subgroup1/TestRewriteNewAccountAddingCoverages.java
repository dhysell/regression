package regression.r2.noclock.policycenter.rewrite.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4809: Rewrite new account - Coverage E error message
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/89078914852">Link Text</a>
 * @Description
 * @DATE Feb 4, 2017
 */
@QuarantineClass
public class TestRewriteNewAccountAddingCoverages extends BaseTest {
    public GeneratePolicy myPolicyObj;
    private Underwriters uw;
    private GeneratePolicy stdFireLiab_Squire_PolicyObj;

    private WebDriver driver;

    @Test()
    public void testCreateStdFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("NewAccount", "Stdfire")
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test(dependsOnMethods = {"testCreateStdFirePolicy"})
    public void TestIssueSquireStandardFirePolicy() throws Exception {
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

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.stdFireLiab_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("SQ", "StandardFire")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        // standard fire
        ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList1.add(new PLPolicyLocationProperty(PropertyTypePL.Potatoes));
        PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

        locToAdd1.setPlNumAcres(11);
        locationsList1.add(locToAdd1);

        PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
        propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);

        new GuidewireHelpers(driver).logout();
        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setStdFireCommodity(true);
        myStandardFire.setLocationList(locationsList1);

        stdFireLiab_Squire_PolicyObj.standardFire = myStandardFire;
        stdFireLiab_Squire_PolicyObj.lineSelection.add(LineSelection.StandardFirePL);
        stdFireLiab_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"TestIssueSquireStandardFirePolicy"})
    private void testCancelSquireStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        Login login = new Login(driver);
        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                stdFireLiab_Squire_PolicyObj.standardFire.getPolicyNumber());

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByPolicyNumber(stdFireLiab_Squire_PolicyObj.standardFire.getPolicyNumber());
        String errorMessage = "";

        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not done!!!";

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();


        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                stdFireLiab_Squire_PolicyObj.squire.getPolicyNumber());


        currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        comment = "Renumbering to another policy";

        cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testCancelSquireStandardFirePolicy"})
    private void testCreateStandardFireNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_RewritePoliciesToThisAccount();

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.setAccountAccountNumber(stdFireLiab_Squire_PolicyObj.accountNumber);
        search.clickSearch();

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.clickSelect();


        if (rewritePage.checkPolicyExistsByProductName(LineSelection.StandardFirePL.getValue())) {
            Assert.fail("Rewrite policies checkbox exists for Standard Fire Commodity ");
        }

    }

    @Test(dependsOnMethods = {"testCreateStandardFireNewAccount"})
    private void testIssueSquireRewriteNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(this.stdFireLiab_Squire_PolicyObj.accountNumber, myPolicyObj.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickAccountSummaryPendingTransactionByStatus(ProductLineType.Squire.getValue());

        // entering Squire Mandatory Information
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);

        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            prequote = true;
        }
        sidemenu.clickSideMenuPolicyInfo();
        sidemenu.clickSideMenuRiskAnalysis();
        riskAnalysis.approveAll_IncludingSpecial();


        if (prequote) {
            riskAnalysis.Quote();
        }

        sidemenu.clickSideMenuQuote();

        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

    @Test(dependsOnMethods = {"testIssueSquireRewriteNewAccount"})
    private void testIssueStandardFireRewriteNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.startQuoteRewrite(this.stdFireLiab_Squire_PolicyObj.accountNumber, myPolicyObj.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickAccountSummaryPendingTransactionByStatus("Rewrite New Account");

        // entering Squire Mandatory Information
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);

        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            prequote = true;
        }
        sidemenu.clickSideMenuPolicyInfo();
        sidemenu.clickSideMenuRiskAnalysis();
        riskAnalysis.approveAll_IncludingSpecial();


        if (prequote) {
            riskAnalysis.Quote();
        }

        sidemenu.clickSideMenuQuote();

        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }
}
