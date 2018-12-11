package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.NewActivityRequestPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.workorders.generic.GenericWorkorder;


/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/defect/220628177772">DE7509</a>
 * @Description 
 Production policy 283201

    Have an in force policy that includes section III
    UW does activity to agent "Get Auto Registration" requesting proof of registration on a specific auto
    Agent completes activity stating registration will be sent to home office

Actual: The "Get Auto Registration" activity is completed by agent. See that no activity is sent to UW to make sure document is uploaded or information is answered.
Expected: When agent completes the "Get Auto Registration" activity there should be an activity back to UW to verify that the correct documentation/information is received.

Requirements link(s): 


See attached screenshots.
 * @DATE May 31, 2018
 */

public class DE7509_AutoRegistrationActivity extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObject = new GeneratePolicy.Builder(driver)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PersonalAutoLinePL)
					.withPolOrgType(OrganizationType.Individual)
					.withInsFirstLastName("ScopeCreeps", "NonFeature")
					.build(GeneratePolicyType.PolicyIssued);
		driver.quit();
	}

	@Test(enabled = true)
	public void VerifyActivitySentBackToUWForAutoRegistration() throws Exception {
		generatePolicy();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
				myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		NewActivityRequestPC pcNewActivityRequest = new NewActivityRequestPC(driver);
		UWActivityPC pcUWActivity = new UWActivityPC(driver);
		ActivityPopup pcActivityPopup = new ActivityPopup(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);

		
		pcNewActivityRequest.initiateActivity(ActivtyRequestType.GetAutoRegistration);
		pcUWActivity.setText("Get that auto reg!");
		pcUWActivity.setSubject("Automan!");
		pcUWActivity.clickOK();
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName, myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
		
		pcAccountSummaryPage.clickCurrentActivitiesSubject("Get Auto Registration");
		pcActivityPopup.setActivityText("done sir UW!");
		pcActivityPopup.setActivitySubject("done sir UW!");
		pcActivityPopup.clickCompleteWithoutPopup();
		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
				myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
		
		// Acceptance criteria: After agent completes activity, the UW should have been sent another activity 
		Assert.assertTrue(pcAccountSummaryPage.getNumberOfActivities() > 0, "UW Activies should not be empty, but it was! Get Auto Registration should be there");
		
		ArrayList<String> activities = pcAccountSummaryPage.getActivitySubjectFromTable();		
		Assert.assertTrue(activities.contains("Request Was Completed for"), "The list of activities did not contain 'Get Auto Registration'");
	}




}




















