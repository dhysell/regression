package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/205325334068">US14400 F99 - COMMON - Activity "Review and Submit CSR/SA Policy Change" should go to agent after CSRs click Request Approval</a>
 * @Description
 * Moving to 2.3. Waiting for more info from UW, Darrel and Tom. 8-6-18 met with business owners and legal. Decisions made and moving forward.
 * COMMON Tested BOP on 7-19-18 in UAT and works already. Screen shots on the CL task
 * As a county office user and home office user I want to see that anytime "Request Approval" is clicked by a CSR or SA for their policy change that the activity goes to the agent as 'Review and Submit CSR/SA Policy Change'. 
 * If a change is done that does not give the CSR or SA the 'Submit' button then they should choose "Request Approval" and needs to go to agent to edit and/or forward to underwriting.
 * 
 * Requirements: PC8 - Common - CommonAdmin - Activity Detail (Refer UI Final tab)
 * Steps to Create:
 * 
 *     Have existing, in force policy
 *     As CSR and SA make changes that would require UW approval
 *     See that there is no 'submit' button available
 *     Go to "Risk Analysis" and choose 'Request Approval'
 * 
 * Acceptance Criteria:
 * 
 *     CSR/SA should have to 'Request Approval' if there is no 'Submit' button for them to click
 *     After clicking 'Request Approval' the change should go to the agent as activity "Review and Submit CSR/SA Policy Change"
 *     Do multiple types of policy changes as CSR and as SA 
 * 
 * Note: US14401 addresses the changes that CSR & SA can do and be able to 'Submit' without agent needing to approve
 * @DATE August 9, 2018
 */

public class US14400_ReviewSubmitCSRSAPolicyChangeRouting extends BaseTest {

	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;

	public void generatePolicyWithAgent(Agents agent) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withAgent(agent)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
				.withInsFirstLastName("ScopeCreeps", "NonFeature")
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(enabled = true)
	public void SectionIDeductibleChange() throws Exception {

		generatePolicyWithAgent(AgentsHelper.getRandomAgent());

		String CSRUsername = "cashcraft";

		new Login(driver).loginAndSearchAccountByAccountNumber(CSRUsername,	"gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);		
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new GenericWorkorderSquirePropertyCoverages(driver);
		BasePage basePage = new BasePage(driver);
		UWActivityPC activity = new UWActivityPC(driver);
		GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);

		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
		policyChangePage.startPolicyChange("Change Section I Deductible", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

		pcSideMenu.clickSideMenuSquirePropertyCoverages();

		pcSquirePropertyCoveragesPage.selectSectionIDeductible(SectionIDeductible.OneThousand);

		pcWorkOrder.clickGenericWorkorderQuote();

		// Acceptance criteria: submit button should be showing and not active
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was not disabled, it should be");

		pcSideMenu.clickSideMenuRiskAnalysis();

		pcRiskPage.clickRequestApproval();
		activity.setText("Sending this over stuff and stuff");
		activity.setSubject("asdadada");
		activity.clickSendRequest();
		pcCompletePage.clickViewYourPolicy();

		Activity currentActivity = new Activity();
		List<Activity> activities = pcPolicySummaryPage.getCurrentActivites_BasicInfo();
		for (Activity activityItem :activities) {
			if (activityItem.getSubject().contains("Review and submit SA/CSR policy change")) {
				currentActivity = activityItem;
			}
		}
		String assignedTo = currentActivity.getAssignedTo();

		assertTrue(assignedTo.contains(myPolicyObject.agentInfo.agentFirstName)&& assignedTo.contains(myPolicyObject.agentInfo.agentLastName) , "Activity was not sent to agent actual -"+assignedTo+" found -"+myPolicyObject.agentInfo.agentFirstName+" "+ myPolicyObject.agentInfo.agentLastName);



	}


	@Test(enabled = true)
	public void ChangePropertyDetailsWithSA() throws Exception {
		Agents agent= AgentsHelper.getAgentByUserName("tgallup");
		generatePolicyWithAgent(agent);
		String saUsername = "jgallup";

		new Login(driver).loginAndSearchAccountByAccountNumber(saUsername,
				"gw", myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		BasePage basePage = new BasePage(driver);
		UWActivityPC activity = new UWActivityPC(driver);
		GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);

		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();
		policyChangePage.startPolicyChange("change property details", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		pcSideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setSquareFootage(4000);
		pcWorkOrder.clickOK();
		pcWorkOrder.clickGenericWorkorderQuote();
		// Acceptance criteria: submit button should be showing and disable
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')]", 5), "Submit button was not visible, it should be");
		Assert.assertTrue(basePage.checkIfElementExists("//a[contains(@id, 'RequestApproval')][contains(@class, 'x-disabled')]", 5), "Submit button was  not disabled, it should  be");

		pcSideMenu.clickSideMenuRiskAnalysis();
		risk.clickRequestApproval();
		activity.setText("Sending this over stuff and stuff");
		activity.setNewNoteSubject("Please Special Approve this Stuff!!");
		// activity.setChangeReason(ChangeReason.AnyOtherChange);
		activity.clickSendRequest();
		pcCompletePage.clickViewYourPolicy();

		Activity currentActivity = new Activity();
		List<Activity> activities = pcPolicySummaryPage.getCurrentActivites_BasicInfo();
		for (Activity activityItem :activities) {
			if (activityItem.getSubject().contains("Review and submit SA/CSR policy change")) {
				currentActivity = activityItem;
			}
		}

		String assignedTo = currentActivity.getAssignedTo();

		assertTrue(assignedTo.contains(myPolicyObject.agentInfo.agentFirstName)&& assignedTo.contains(myPolicyObject.agentInfo.agentLastName) , "Activity was not sent to agent actual -"+assignedTo+" found -"+myPolicyObject.agentInfo.agentFirstName+" "+ myPolicyObject.agentInfo.agentLastName);


	}
}