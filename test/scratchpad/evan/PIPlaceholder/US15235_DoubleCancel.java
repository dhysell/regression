package scratchpad.evan.PIPlaceholder;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.Cancellation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/223410766988">US15235 7-23-18 EMAIL TO TOM, DARREL, LISA Ability to have a backdated cancel on an already cancelled policy</a>
 * @Description
 * DEPENDENT ON ACHIEVERS US15043 TO BE DONE FIRST.  BS 6-5-18 This story is done. 
 * 
 * We cannot start work until we have the document. Moving to PI2 and will begin work on a document during 1.5 and 2.1 so that this can be completed by 2.3.
 * 
 * Spoke to Tom and he didn't think we needed a document about the new cancel. Emailed Tom, Darrel and Lisa on 7-23-18 for confirmation on this.
 * 
 * As any user that has permission to cancel a policy, I want to be able to start a cancel on an already cancelled policy.  
 * 
 * NOTE - NEED LISA/DARREL TO DECIDE ON HOW MANY DAYS BACK TO GO  - DONE - TO EFFECTIVE DATE OF PRIOR POLICY TERM. THIS MEANS A USER COULD GO BACK FOR EACH PRIOR EXISTING TERM
 * 
 * Per discussion with Lisa on 6-6-18 a user should be able to go back to the effective date of any term and cancel the policy. This should include prior cancelled terms.
 * 
 * Note: SmartComm work on separate story (see successors- dependencies) 
 * 
 * Steps to get there:
 * 
 *     Have inforce policy.
 *     Cancel policy for any reason. Process cancel.
 *     Do a new cancel that is effective prior to the original cancel. 
 *     Test multiple LOB's and policies with different terms in force and cancelled terms
 * 
 * Acceptance criteria:
 * 
 *     Ensure that user is able to start and complete the backdated cancel
 *     Ensure works for all carrier and insured cancel reasons
 *     Ensure that user is able to go back anywhere from 1 day to the effective date of the term
 *     Ensure that user is able to go back into any prior cancelled term and cancel backwards
 *     Ensure that "user" means all users with permission to start a cancel. Home office and county office users
 *     Ensure that the 2nd cancel cannot be effective on or after the original cancel date
 *     Ensure that letter generated has sentence regarding the original cancel date (might need separate SC story also)
 * @DATE August 9, 2018
 */

public class US15235_DoubleCancel extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;

    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
					.withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);
    }

	// 6/11/18: DISABLED PENDING DEV WORK
	@Test(enabled = true)
	public void CancelCanceledPolicy() throws Exception {
		
        generatePolicy();
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderModifiers pcModifiersPage = new GenericWorkorderModifiers(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);		
		GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		SoftAssert softAssert = new SoftAssert();
		PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
		StartCancellation pcCancellationPage = new StartCancellation(driver);
		StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
		GenericWorkorderBuildings pcBuildingsPage = new GenericWorkorderBuildings(driver);
		PolicySummary pcPolicysummaryPage = new PolicySummary(driver);
		GenericWorkorderQualification pcQualificationPage = new GenericWorkorderQualification(driver);
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickCancelPolicy();
		pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.Rewritten);
		pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		pcPolicyMenu.clickMenuActions();
		// Assert fail if cancel isn't option
		pcPolicyMenu.clickCancelPolicy();
		pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.PaymentStopped);
		pcCancellationPage.setEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -40));
		pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		
		// Acceptance criteria: 
        Assert.assertTrue(true, "Message");
    }
}