package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions;

import java.util.ArrayList;

/**
 * @Author swathiAkarapu
 * @Requirement DE7984
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/defect/256436761864">DE7984</a>
 * @Description
 * Block submit business rule:
 * Excluded drivers on umbrella are not equal to or greater than excluded drivers on squire UB013
 *
 * Transition in squire & umbrella policy where squire has a 304 excluded driver endorsement with free form names (persons not on section III as drivers).
 * Make sure squire has 304 excluded drivers that are not policy members and need to be added free form on the umbrella
 * If the excluded drivers on umbrella are not equal to or greater than excluded drivers on the squire policy, there needs to be a block submit on the umbrella policy (agent/PA should not be able to auto-issue the squire or the umbrella).
 * If the umbrella is started after the squire is in force there needs to be a block submit if the excluded drivers are not equal to or greater than the excluded drivers on the squire policy
 * Also test on new submission and a policy change where excluded driver is added.
 * Test adding an umbrella policy midterm
 * Actual: Free form excluded drivers on squire not listed on umbrella
 * Expected: If there are free form excluded drivers on squire that are not added as excluded driver on umbrella there needs to be a block submit on umbrella to make sure excluded drivers get added
 * @DATE October 31, 2018
 */
public class DE7984UmbrellaPolicyExcludedDriver extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;
    private String excludedDriverFirstName = "Excluded";

    private void generatePolicy() throws Exception {
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.CSL300K,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TenK);
        ArrayList<repository.gw.generate.custom.Contact> driversList = new ArrayList<repository.gw.generate.custom.Contact>();
        repository.gw.generate.custom.Contact person = new repository.gw.generate.custom.Contact();
        person.setFirstName(excludedDriverFirstName);
        driversList.add(person);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setDriversList(driversList);
        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
        mySquire.squirePA = squirePersonalAuto;
        mySquire.squirePA.setDriversList(driversList);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("umbrella", "exclude")
                .withPolEffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter))
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }

    @Test(enabled = true)
    public void freeFormDrivedExculded_newSubmittion() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicy();
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        repository.pc.workorders.generic.GenericWorkorderQualification pcQualificationPage = new repository.pc.workorders.generic.GenericWorkorderQualification(driver);
        repository.pc.workorders.generic.GenericWorkorderSquirePACoverages pcSquirePACoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions pcPAExclusionsConditionsPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages pcUmbrellaCoverages = new repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderPayment pcPaymentPage = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis pcRiskPage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);

        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PreQual')]//span[contains(text(), 'Qualification')]"));
        new GuidewireHelpers(driver).editPolicyTransaction();
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PAWiz')]//span[contains(text(), 'Section III - Auto')]"));
        pcSideMenu.clickSideMenuPADrivers();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(excludedDriverFirstName);
        paDrivers.setExcludedDriverRadio(true);
        paDrivers.clickOk();
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PALine')]//span[contains(text(), 'Coverages')]"));
        pcSquirePACoveragesPage.clickCoverageExclusionsTab();
        pcPAExclusionsConditionsPage.addNewDriverExclusionEndorsement304("Exclude Him");
        pcPAExclusionsConditionsPage.addDriverExclusionEndorsement304(excludedDriverFirstName);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.approveAll();
        pcSideMenu.clickSideMenuPayment();
        pcPaymentPage.clickAddDownPayment();
        pcPaymentPage.setDownPaymentType(repository.gw.enums.PaymentType.Cash);
        pcPaymentPage.setDownPaymentAmount(5000);
        pcPaymentPage.clickOK();
        pcRiskPage.clickReleaseLock();
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,
                myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcWorkOrder.clickGenericWorkorderIssue(repository.gw.enums.IssuanceType.Issue);
        repository.gw.generate.custom.SquireUmbrellaInfo squireUmbrellaInfo = new repository.gw.generate.custom.SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(repository.gw.enums.SquireUmbrellaIncreasedLimit.Limit_1000000);
        squireUmbrellaInfo.setEffectiveDate(myPolicyObject.squire.getEffectiveDate());
        squireUmbrellaInfo.setDraft(true);
        myPolicyObject.squireUmbrellaInfo = squireUmbrellaInfo;
        new GuidewireHelpers(driver).logout();

        myPolicyObject.addLineOfBusiness(repository.gw.enums.ProductLineType.PersonalUmbrella, repository.gw.enums.GeneratePolicyType.QuickQuote);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObject.accountNumber);

        pcSideMenu.clickSideMenuPolicyInfo();
        pcSideMenu.clickSideMenuSquireUmbrellaCoverages();
        pcUmbrellaCoverages.clickExclusionsConditions();
        pcSideMenu.clickSideMenuRiskAnalysis();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcWorkOrder.clickGenericWorkorderFullApp();
        pcQualificationPage.setUmbrellaQuestionsFavorably();
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        boolean blockSubmit = isBlockSubmit(riskAnalysis);
        softAssert.assertTrue(blockSubmit, "Excluded drivers on umbrella are not equal to or greater than excluded drivers on squire - not found");
        softAssert.assertAll();
    }

    private void generatePolicyIssue() throws Exception {
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit.CSL300K,
                repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit.TenK);
        ArrayList<repository.gw.generate.custom.Contact> driversList = new ArrayList<repository.gw.generate.custom.Contact>();
        repository.gw.generate.custom.Contact person = new repository.gw.generate.custom.Contact();
        person.setFirstName(excludedDriverFirstName);
        driversList.add(person);
        repository.gw.generate.custom.SquirePersonalAuto squirePersonalAuto = new repository.gw.generate.custom.SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setDriversList(driversList);

        repository.gw.generate.custom.Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;
        mySquire.squirePA.setDriversList(driversList);

        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL, repository.gw.enums.LineSelection.PersonalAutoLinePL)
                .withPolOrgType(OrganizationType.Individual)
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withInsFirstLastName("umbrella", "exclude")
                .withPolEffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter))
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);

        repository.gw.generate.custom.SquireUmbrellaInfo squireUmbrellaInfo = new repository.gw.generate.custom.SquireUmbrellaInfo();
        squireUmbrellaInfo.setIncreasedLimit(repository.gw.enums.SquireUmbrellaIncreasedLimit.Limit_1000000);
        squireUmbrellaInfo.setEffectiveDate(myPolicyObject.squire.getEffectiveDate());
        squireUmbrellaInfo.setDraft(true);
        myPolicyObject.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolicyObject.addLineOfBusiness(repository.gw.enums.ProductLineType.PersonalUmbrella, repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void freeFormDriverExclueded_policyChange() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        generatePolicyIssue();
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObject.squire.getPolicyNumber());


        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        SoftAssert softAssert = new SoftAssert();
        repository.pc.workorders.generic.GenericWorkorderSquirePACoverages pcSquirePACoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePACoverages(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_ExclusionsConditions pcPAExclusionsConditionsPage = new GenericWorkorderSquireAutoCoverages_ExclusionsConditions(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages pcUmbrellaCoverages = new repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages(driver);
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("Square policy Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        pcSideMenu.clickSideMenuPADrivers();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        paDrivers.clickEditButtonInDriverTableByDriverName(excludedDriverFirstName);
        paDrivers.setExcludedDriverRadio(true);
        paDrivers.clickOk();
        new repository.driverConfiguration.BasePage(driver).clickWhenClickable(By.xpath("//td[contains(@id, 'PALine')]//span[contains(text(), 'Coverages')]"));
        pcSquirePACoveragesPage.clickCoverageExclusionsTab();
        pcPAExclusionsConditionsPage.addNewDriverExclusionEndorsement304("Exclude Him");
        pcPAExclusionsConditionsPage.addDriverExclusionEndorsement304(excludedDriverFirstName);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        riskAnalysis.approveAll();
        riskAnalysis.clickIssuePolicyButton();
        new GuidewireHelpers(driver).logout();

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                this.myPolicyObject.squireUmbrellaInfo.getPolicyNumber());
        policyChangePage.startPolicyChange("Umbrella policy Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

        pcSideMenu.clickSideMenuPolicyInfo();
        pcSideMenu.clickSideMenuSquireUmbrellaCoverages();

        pcUmbrellaCoverages.clickExclusionsConditions();
        repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages umbCovs = new repository.pc.workorders.generic.GenericWorkorderSquireUmbrellaCoverages(driver);
        umbCovs.clicResyncWithSquire();
        pcSideMenu.clickSideMenuRiskAnalysis();

        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
        if (quotePage.isPreQuoteDisplayed()) {
            quotePage.clickPreQuoteDetails();
        }

        boolean blockSubmit = isBlockSubmit(riskAnalysis);
        softAssert.assertTrue(blockSubmit, "Excluded drivers on umbrella are not equal to or greater than excluded drivers on squire - not found");
        softAssert.assertAll();
    }

    private boolean isBlockSubmit(repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis) {
        boolean blockSubmit = false;
        for (repository.gw.generate.custom.UnderwriterIssue issue : riskAnalysis.getUnderwriterIssues().getBlockSubmitList()) {
            if (issue.getShortDescription().contains("Excluded drivers on umbrella are not equal to or greater than excluded drivers on squire") && issue.getLongDescription().contains("Excluded drivers on umbrella are not equal to or greater than excluded drivers on squire")) {
                blockSubmit = true;
                break;
            }
        }
        return blockSubmit;
    }
}