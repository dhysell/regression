package regression.r2.noclock.policycenter.rewrite.subgroup1;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4893: Issue Policy Option is missing in Standard Fire Rewrite New account
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/91900299232">Link Text</a>
 * @Description
 * @DATE Mar 13, 2017
 */
public class TestStandardFireLiabilityRewriteNewAccount extends BaseTest {
    private GeneratePolicy squirePolicyObj;
    private GeneratePolicy stdFireLiab_Fire_PolicyObj;
    private Underwriters uw;

    private WebDriver driver;

    @Test()
    public void testGenerateQQSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
                MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Account", "Squire")
                .withPolOrgType(OrganizationType.Individual)
                .build(GeneratePolicyType.QuickQuote);
    }

    @Test
    public void testIssueStandardLiabilityWithStdFire() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        stdFireLiab_Fire_PolicyObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withInsFirstLastName("NewAccount", "Stdfire")
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

//		stdFireLiab_Liability_PolicyObj = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.StandardLiability)
//				.withLineSelection(LineSelection.StandardLiabilityPL)
//				.withStandardLiability(myStandardLiability)
//				.withAgent(stdFireLiab_Fire_PolicyObj.agentInfo)
//				.withStandardFirePolicyUsedForStandardLiability(stdFireLiab_Fire_PolicyObj, true)
//				.withInsPersonOrCompany(ContactSubType.Person)
//				.withPolOrgType(OrganizationType.Individual)
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);

        stdFireLiab_Fire_PolicyObj.standardLiability = myStandardLiability;
        stdFireLiab_Fire_PolicyObj.lineSelection.add(LineSelection.StandardLiabilityPL);
        stdFireLiab_Fire_PolicyObj.stdFireLiability = true;
        stdFireLiab_Fire_PolicyObj.addLineOfBusiness(ProductLineType.StandardLiability, GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"testIssueStandardLiabilityWithStdFire", "testGenerateQQSquirePolicy"})
    private void testCancelStandardFireLiabilityPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        Login login = new Login(driver);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                stdFireLiab_Fire_PolicyObj.standardFire.getPolicyNumber());

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        guidewireHelpers.logout();

        login.loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                stdFireLiab_Fire_PolicyObj.standardLiability.getPolicyNumber());


        currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        comment = "Renumbering to another policy";

        cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);
    }

    @Test(dependsOnMethods = {"testCancelStandardFireLiabilityPolicy"})
    private void testRewriteStandardFireLiabityToNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter_Supervisor);

        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_RewritePoliciesToThisAccount();

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.setAccountAccountNumber(stdFireLiab_Fire_PolicyObj.accountNumber);
        search.clickSearch();
        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.clickSelect();
        rewritePage.selectAllPolicies();
        rewritePage.click_button_rewritePolicies();

    }

    @Test(dependsOnMethods = {"testRewriteStandardFireLiabityToNewAccount"})
    private void testIssueStadardFireRewriteNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickAccountSummaryPendingTransactionByStatus(LineSelection.StandardFirePL.getValue());

        // entering Squire Mandatory Information
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);

        sidemenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReport(squirePolicyObj);
        sidemenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers policyMembers = new GenericWorkorderPolicyMembers(driver);
        policyMembers.clickPolicyHolderMembersByName(squirePolicyObj.pniContact.getLastName());
        policyMembers.selectNotNewAddressListing("ID");
        policyMembers.clickOK();

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
        sidemenu.clickSideMenuPayment();
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
        if (paymentPage.isBillingMethodEditable()) {
            Assert.fail("Billing method is editable...");
        }

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

    @Test(dependsOnMethods = {"testIssueStadardFireRewriteNewAccount"})
    private void testIssueStadardLiabilityRewriteNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirePolicyObj.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickAccountSummaryPendingTransactionByStatus(LineSelection.StandardLiabilityPL.getValue());
        // entering Squire Mandatory Information
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        sidemenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
        householdMember.editHouseHoldMember(squirePolicyObj.pniContact);
        householdMember.selectNotNewAddressListingIfNotExist(squirePolicyObj.pniContact.getAddress());
        householdMember.clickOK();
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

        sidemenu.clickSideMenuPayment();
        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
        if (paymentPage.isBillingMethodEditable()) {
            Assert.fail("Billing method is editable...");
        }

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

    }

}
