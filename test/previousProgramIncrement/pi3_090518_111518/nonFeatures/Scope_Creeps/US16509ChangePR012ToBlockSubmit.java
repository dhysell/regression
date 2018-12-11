package previousProgramIncrement.pi3_090518_111518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.OrganizationType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.rewrite.StartRewrite;

import java.util.ArrayList;

/**
 * @Author swathiAkarapu
 * @Requirement US16509 , US16591
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/254854862312">US16509</a> -old one
 *  <a href="https://rally1.rallydev.com/#/203558454824ud/detail/userstory/257683616472">US16591</a> - updated rules
 * @Description
 * US16509
 * As an agent/PA when I have a policy with $5,000 deductible on section I and do not have a Coverage A Residence Premises or Condominium Residence Premises with limit greater or equal to $200,000 I want to see a block submit for UW.
 *
 * Steps to get there:
 * Start a new policy or do a policy change with a section I deduct of $5,000
 * Residence or Condo Residence with a value of less than $200,000
 * Quote
 * Acceptance criteria:
 * Ensure that agent/PA has a block submit for PR012 and needs UW approval to issue
 * Ensure that after UW approves the block it can be sent back to the county for issuance
 * Ensure that the block submit triggers on new business, policy change, all editable rewrite jobs on all lines of business where these buildings are available
 *
 * US16591
 *As a home office or county office user I want to see a block issue for underwriting if there is $5,000 deductible chosen for section I.
 * (need to update PR012)
 *
 * Steps to get there:
 * As agent start a new submission
 * On section I choose $5,000 for deductible
 * Finish adding items and quote
 * The UW issue on risk analysis should read "Section I deductible is $5,000 or more".  The details for PR012 should read "Section I deductible of $5,000 or  more requires underwriting approval. (PR012)"
 * Acceptance Criteria:
 * Ensure that on all new submissions if $5,000 deduct is chosen on section I then policy has block issue for underwriting to approve
 * Ensure that on any policy change where the deduct for section I is currently less than $5,000 and changes to $5,000 that the policy has a block issue for underwriting to approve
 * Ensure that on any editable rewrite job including rewrite new account if the section I deduct is changed from less than $5,000 to $5,000 there is a block issue for underwriting to approve
 * Ensure the user is still able to complete a quick quote successfully
 *
 * @DATE September 25
 */
public class US16509ChangePR012ToBlockSubmit extends BaseTest {
    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    private void generate() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        repository.gw.generate.custom.PLPolicyLocationProperty myProperty = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
        myProperty.getPropertyCoverages().getCoverageA().setLimit(100000);
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> propertyList = new ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty>();
        propertyList.add(myProperty);
        repository.gw.generate.custom.PolicyLocation myLocation = new repository.gw.generate.custom.PolicyLocation(propertyList);
        ArrayList<repository.gw.generate.custom.PolicyLocation> locationList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        locationList.add(myLocation);
        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationList;
        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.propertyAndLiability.section1Deductible= repository.gw.enums.Property.SectionIDeductible.FiveThousand;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withSquire(mySquire)
                .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                .withInsFirstLastName("Pr012", "error")
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withPolOrgType(repository.gw.enums.OrganizationType.Individual)
                .isDraft()
                .build(repository.gw.enums.GeneratePolicyType.FullApp);
    }


    @Test
    public void verifyPR012() throws Exception {
        generate();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();
        new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickSectionIICoveragesTab();
        new repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages(driver).clickCoveragesExclusionsAndConditions();
        pcSideMenu.clickSideMenuSquirePropertyLineReview();
        pcSideMenu.clickSideMenuModifiers();
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcWorkOrder.clickGenericWorkorderQuote();
        pcSideMenu.clickSideMenuRiskAnalysis();
        boolean pro012 = isPro012(riskAnalysis);
        softAssert.assertTrue(pro012, "PR012 not found");
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk_UWIssues.handleBlockSubmit(myPolicyObject);
        softAssert.assertAll();
    }
    public void generateIssuePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter );
        driver = buildDriver(cf);
       repository.gw.generate.custom.PLPolicyLocationProperty myProperty = new repository.gw.generate.custom.PLPolicyLocationProperty(repository.gw.enums.Property.PropertyTypePL.ResidencePremises);
        myProperty.getPropertyCoverages().getCoverageA().setLimit(210000);
        ArrayList<repository.gw.generate.custom.PLPolicyLocationProperty> propertyList = new ArrayList<PLPolicyLocationProperty>();
        propertyList.add(myProperty);
        repository.gw.generate.custom.PolicyLocation myLocation = new repository.gw.generate.custom.PolicyLocation(propertyList);
        ArrayList<repository.gw.generate.custom.PolicyLocation> locationList = new ArrayList<repository.gw.generate.custom.PolicyLocation>();
        locationList.add(myLocation);
        repository.gw.generate.custom.SquirePropertyAndLiability myPropertyAndLiability = new repository.gw.generate.custom.SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationList;
        repository.gw.generate.custom.Squire mySquire = new repository.gw.generate.custom.Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PropertyAndLiabilityLinePL)
                .withSquire(mySquire)
                .withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
                .withInsFirstLastName("Pr012", "error")
                .withDownPaymentType(repository.gw.enums.PaymentType.Cash)
                .withPolOrgType(OrganizationType.Individual)
                .build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
    }
    @Test
    public void verifyPR012_policyChange() throws Exception {

       generateIssuePolicy();

       new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("first policy Change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        SoftAssert softAssert = new SoftAssert();
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();


        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.FiveThousand);
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        boolean pro012= isPro012(riskAnalysis);
        softAssert.assertTrue(pro012, "PR012 not found");
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
        risk_UWIssues.handleBlockSubmit(myPolicyObject);
        softAssert.assertAll();
    }


    @Test
    public void verifyPR012_rewrite() throws Exception {

        generateIssuePolicy();

        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(), myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

        repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages(driver);
        repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
        StartCancellation pcCancellationPage = new StartCancellation(driver);
        pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(repository.gw.enums.PolicyTermStatus.InForce);
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickCancelPolicy();

        pcCancellationPage.setSource("Carrier");
        pcCancellationPage.setCancellationReason("policy rewritten or replaced (flat cancel)");
        pcCancellationPage.setExplanation("other");
        pcCancellationPage.setDescription("testing");
        pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.Rewritten);
        pcCancellationPage.clickStartCancellation();
        pcCancellationPage.clickSubmitOptionsCancelNow();

        pcWorkorderCompletePage.clickViewYourPolicy();
        pcPolicyMenu.clickMenuActions();
        pcPolicyMenu.clickRewriteFullTerm();

        SoftAssert softAssert = new SoftAssert();
        pcSideMenu.clickSideMenuPropertyLocations();
        pcSideMenu.clickSideMenuSquirePropertyDetail();
        pcSideMenu.clickSideMenuSquirePropertyCoverages();


        pcSquirePropertyCoveragesPage.selectSectionIDeductible(repository.gw.enums.Property.SectionIDeductible.FiveThousand);
        StartRewrite rewriteWO = new StartRewrite(driver);
        rewriteWO.visitAllPages(myPolicyObject);
        pcWorkOrder.clickGenericWorkorderQuote();
        repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
        pcSideMenu.clickSideMenuRiskAnalysis();
        boolean pro012 = isPro012(riskAnalysis);
        softAssert.assertTrue(pro012, "PR012 not found");
        softAssert.assertAll();
    }

    private boolean isPro012(repository.pc.workorders.generic.GenericWorkorderRiskAnalysis riskAnalysis) {
        boolean pro012=false;
        for(repository.gw.generate.custom.UnderwriterIssue issue : riskAnalysis.getUnderwriterIssues().getBlockSubmitList()) {
            if(issue.getShortDescription().contains("Section I deductible is $5,000 or more") && issue.getLongDescription().contains("SECTION I: Deductible of $5,000 or more requires underwriting approval")) {
                pro012 =true;
                break;
            }
        }
        return pro012;
    }

}
