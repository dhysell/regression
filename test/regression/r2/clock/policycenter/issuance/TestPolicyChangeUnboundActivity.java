package regression.r2.clock.policycenter.issuance;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;

/**
 * @Author nvadlamudi
 * @Requirement : US12434: Policy Change: New automated activity & auto-withdraw of old transactions
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Admin%20Tab/Activity%20Patterns.xlsx">PC8 � Common � CommonAdmin � Activity Detail</a>
 * @Description : validating Unbound transaction to be withdrawn activity
 * @DATE Nov 9, 2017
 */
@QuarantineClass
public class TestPolicyChangeUnboundActivity extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy mySQPolicyObjPL = null;

	@Test()
	public void testIssueSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("Unsubmit", "Activity")
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquirePol" })
    private void testCreatedPolChangeUnbound() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySQPolicyObjPL.agentInfo.getAgentUserName(),
                mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.startPolicyChange("First policy Change", mySQPolicyObjPL.squire.getEffectiveDate());

        new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		//Move clock 45 days to create Unbound activity
		testMoveClock(45);
	}
	
	@Test(dependsOnMethods = {"testCreatedPolChangeUnbound"})
    private void testCheckUnboundActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySQPolicyObjPL.agentInfo.getAgentUserName(),
                mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());

		String errorMessage = "";
        PolicySummary summaryPage = new PolicySummary(driver);
		
		if (!summaryPage.checkIfActivityExists("Unsubmitted transaction to be withdrawn"))
			errorMessage = "Activity 'Unsubmitted transaction to be withdrawn' is not displayed.\n";
		else{			
			if(!summaryPage.getActivityAssignment("Unsubmitted transaction to be withdrawn").contains(mySQPolicyObjPL.agentInfo.getAgentFirstName()) && !summaryPage.getActivityAssignment("Unsubmitted transaction to be withdrawn").contains(mySQPolicyObjPL.agentInfo.getAgentLastName())){
				errorMessage = errorMessage + "Unsubmitted transaction to be withdrawn is not assigned to agent. \n";
			}
		}
		
		summaryPage.clickActivity("Unsubmitted transaction to be withdrawn");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		String actDesc = "You have an unsubmitted transaction. Please review it and withdraw if no longer needed, or complete and bind the change. �If you don't take action, this transaction will be withdrawn in 15 days";
		if(!activityPopupPage.getActivityDescription().contains(actDesc)){
			errorMessage = errorMessage + "Expected activity description : "+ actDesc+ " is not displayed. \n";
		}
			
		if(activityPopupPage.getActivityEscalationDate() != null){
			errorMessage = errorMessage + "Activity Escalation date empty is not displayed. \n";
		}
		if(errorMessage !=""){
            Assert.fail(errorMessage);
		
		}
		//Move Clock 15days to check policy change transaction is withdrawn
		testMoveClock(15);
	}
	
	
	@Test(dependsOnMethods= {"testCheckUnboundActivity"})
    private void testCheckPolicyChangeWithdrawl() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(mySQPolicyObjPL.agentInfo.getAgentUserName(),
                mySQPolicyObjPL.agentInfo.getAgentPassword(), mySQPolicyObjPL.squire.getPolicyNumber());

        PolicySummary summaryPage = new PolicySummary(driver);
		List<WebElement> pendingTrans = summaryPage.getPendingPolicyTransaction(TransactionType.Policy_Change);
		if(pendingTrans.size()> 0){
            Assert.fail("Pending Policy Change Transaction is still Exists.");
		}
	}

    private void testMoveClock(int days) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Trigger_Activities);
        // delay required
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Activity_Escalation);
        // delay required
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
	}
}
