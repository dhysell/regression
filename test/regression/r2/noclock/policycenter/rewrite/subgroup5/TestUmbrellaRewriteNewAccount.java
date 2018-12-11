package regression.r2.noclock.policycenter.rewrite.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchAccountsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE4802: Umbrella Rewrite New account - quote and issue policy
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Feb 2, 2017
 */
@QuarantineClass
public class TestUmbrellaRewriteNewAccount extends BaseTest {
    public GeneratePolicy myPolicyObj;
    private Underwriters uw;
    private GeneratePolicy myStandardFirePolicyObjPL;
    private WebDriver driver;

    @Test
    public void testIssueSquireUmbrellaPolicy() throws Exception {
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

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
                MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Rewrite", "NewAccount")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        new GuidewireHelpers(driver).logout();


        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myPolicyObj.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolicyObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);

    }

    @Test()
    public void TestCreateStandardFirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
        productLines.add(LineSelection.StandardFirePL);

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

        myStandardFirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
                .withInsFirstLastName("Test", "NewAccount")
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testIssueSquireUmbrellaPolicy"})
    private void testCancelSquireUmbrellaPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                myPolicyObj.squire.getPolicyNumber());

        Date currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        String comment = "Renumbering to another policy";

        StartCancellation cancelPol = new StartCancellation(driver);
        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);

        // searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
        policySearchPC.searchPolicyByAccountNumber(myPolicyObj.accountNumber);
        String errorMessage = "";
        PolicySummary summary = new PolicySummary(driver);
        if (summary.getTransactionNumber(TransactionType.Cancellation, comment) == null)
            errorMessage = "Cancellation is not done!!!";

        if (errorMessage != "")
            Assert.fail(errorMessage);
    }

    @Test(dependsOnMethods = {"testCancelSquireUmbrellaPolicy", "TestCreateStandardFirePolicy"})
    private void testCreateSquireUmbrellaNewAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStandardFirePolicyObjPL.accountNumber);
        ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_RewritePoliciesToThisAccount();

        SearchAccountsPC search = new SearchAccountsPC(driver);
        search.setAccountAccountNumber(myPolicyObj.accountNumber);
        search.clickSearch();

        StartRewrite rewritePage = new StartRewrite(driver);
        rewritePage.clickSelect();
        rewritePage.selectAllPolicies();
        rewritePage.click_button_rewritePolicies();

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        // entering Squire Mandatory Information
        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);

        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.Quote();
        boolean prequote = false;
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
            prequote = true;
        }
        sidemenu.clickSideMenuPolicyInfo();
        sidemenu.clickSideMenuRiskAnalysis();

        if (riskAnalysis.checkIfSpecialApproveButtonsExist(false)) {
            riskAnalysis.clickRequestApproval();
            UWActivityPC activity = new UWActivityPC(driver);
            activity.setText("Please Special Approve this Stuff!!");
            activity.setNewNoteSubject("Please Special Approve this Stuff!!");
            activity.clickSendRequest();
            InfoBar infoBar = new InfoBar(driver);
            infoBar.clickInfoBarAccountNumber();
            AccountSummaryPC aSumm = new AccountSummaryPC(driver);
            ArrayList<String> activityOwners = new ArrayList<String>();
            activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
            Underwriters uwSpecialApproval = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));
            TopInfo topInfoStuff = new TopInfo(driver);
            topInfoStuff = new TopInfo(driver);
            topInfoStuff.clickTopInfoLogout();
            Login loginPage = new Login(driver);
            loginPage.login(uwSpecialApproval.getUnderwriterUserName(), uwSpecialApproval.getUnderwriterPassword());
            SearchAccountsPC search1 = new SearchAccountsPC(driver);
            search1.searchAccountByAccountNumber(myStandardFirePolicyObjPL.accountNumber);
            AccountSummaryPC acct = new AccountSummaryPC(driver);
            acct.clickActivitySubject("Approval Requested");
            ActivityPopup actPop = new ActivityPopup(driver);
            actPop.clickCompleteButton();

            SideMenuPC sideBar = new SideMenuPC(driver);
            sideBar.clickSideMenuRiskAnalysis();

            riskAnalysis.approveAll_IncludingSpecial();
        }

        if (prequote) {
            riskAnalysis.Quote();
        }

        sidemenu.clickSideMenuQuote();
        rewritePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        completePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

        // Umbrella quote and issue

        SearchAccountsPC accountSearchPC = new SearchAccountsPC(driver);
        accountSearchPC.searchAccountByAccountNumber(myStandardFirePolicyObjPL.accountNumber);
        accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.PersonalUmbrella);
        sidemenu.clickSideMenuPolicyInfo();
        sidemenu.clickSideMenuRiskAnalysis();

        riskAnalysis.Quote();

        rewritePage.clickIssuePolicy();
        rewritePage.waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

}
