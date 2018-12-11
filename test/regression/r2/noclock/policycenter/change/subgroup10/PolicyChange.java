package regression.r2.noclock.policycenter.change.subgroup10;

import java.util.Date;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.driverConfiguration.Config;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartNameChange;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.helpers.NamesHelper;
import scratchpad.jon.mine.GenerateTestPolicy;
@QuarantineClass
public class PolicyChange extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObj = null;
	public GenerateContact myContactObj;

	@Test(enabled = true)
	public void createPolicy() throws Exception {

		GenerateTestPolicy policy = new GenerateTestPolicy();
		policy.generateRandomPolicy("Policy Change");
		this.myPolicyObj = policy.myPolicy;
		
		this.myPolicyObj.pniContact.getAddress().setPhoneBusiness("2085551111");
		this.myPolicyObj.pniContact.getAddress().setPhoneHome("2085552222");
		this.myPolicyObj.pniContact.getAddress().setPhoneWork("2085553333");
		this.myPolicyObj.pniContact.getAddress().setPhoneMobile("2085554444");
		this.myPolicyObj.pniContact.getAddress().setPhoneFax("2085555555");

		System.out.println(myPolicyObj.accountNumber);
		System.out.println(myPolicyObj.agentInfo.getAgentUserName());
	}

	/**
	 * @author jlarsen Requirement - US5387 - Agents should not be able to
	 *         change modifiers mid term. Please make modifiers page un-editable
	 *         for agents during a Policy Change work order. Link - 6.6.1 &
	 *         6.8.5 Description - randomly try to set one(1) of the modifiers
	 *         values. if it doen't fail to do so test will fail.
	 * @DATE - Sep 23, 2015
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "createPolicy" }, enabled = true)
	public void editModifiersPage() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
				myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Can I change the Modifiers Page?", null);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuModifiers();

        GenericWorkorderModifiers modify = new GenericWorkorderModifiers(driver);
		int number = NumberUtils.generateRandomNumberInt(0, 3);

		try {
			switch (number) {
			case 0:
				System.out.println("Trying to set: Building features");
				modify.setModifiersBuildingFeaturesCreditDebit(1);
				Assert.fail("Building features: On the modifiers page was able to be set midterm.");
			case 1:
				System.out.println("Trying to set: Premises and equipment");
				modify.setModifiersPremisesEquipmentCreditDebit(1);
				Assert.fail("Premises and equipment: On the modifiers page was able to be set midterm.");
			case 2:
				System.out.println("Trying to set: Management");
				modify.setModifiersManagementCreditDebit(1);
				Assert.fail("Management: On the modifiers page was able to be set midterm.");
			case 3:
				System.out.println("Trying to set: Employees");
				modify.setModifiersEmployeesCreditDebit(1);
				Assert.fail("Employees: On the modifiers page was able to be set midterm.");
			}
		} catch (Exception e) {
			// DO NOTHING BECAUSE TEST PASSED
		} finally {
			// withdraw transaction
            GenericWorkorder wo = new GenericWorkorder(driver);
			wo.clickWithdrawTransaction();
            /*			selectOKOrCancelFromPopup(OkCancel.OK);*/
		}

	}

	// jlarsen 11/23/2015
	// TEST MOVED IN FROM AgentWithdrawPolicyChange TO REMOVE UNESSESARY
	// GENERATE CALLS TO SHORTED TESTING RUN TIMES
	/**
	 * @throws Exception
	 * @Author drichards
	 * @Requirement US5550
	 * @Link <a href=
	 *       "http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Policy%20Change/6.0%20-%20Policy%20Change.pptx">
	 *       6.8.7.1</a>
	 * @Description Makes sure that a policy change, once bound, cannot be
	 *              withdrawn by the same agent.
	 * @DATE Sep 24, 2015
	 */
	@Test(dependsOnMethods = { "createPolicy" }, enabled = false)
	public void testAgentWithdraw() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.agentInfo.getAgentUserName(),
				myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		String changeReason = "For Agent Withdraw";
		policyChange.startPolicyChange(changeReason, null);
		policyChange.quoteAndSubmit(ChangeReason.Other, changeReason);

        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();

        PolicySummary summary = new PolicySummary(driver);
		summary.selectOpenTransaction(changeReason);

		// search for the withdraw button
        GenericWorkorder workorder = new GenericWorkorder(driver);
		boolean isFound = true;
		try {
			workorder.clickWithdrawTransaction();
		} catch (Exception e) {
			isFound = false;
		}
		if (isFound) {
			throw new Exception("The withraw button should not be available to agents on their own policy change.");
		}
	}

	// jlarsen 11/23/2015
	// TEST MOVED IN FROM StaticallyTypedWsdlException_DE2568 TO REMOVE
	// UNESSESARY GENERATE CALLS TO SHORTED TESTING RUN TIMES
	@Test(dependsOnMethods = { "createPolicy" }, enabled = true)
	public void policyChanges() throws Exception {

		if (myPolicyObj.paymentPlanType.equals(PaymentPlanType.Annual)
				|| myPolicyObj.paymentPlanType.equals(PaymentPlanType.Semi_Annual)) {
			Config cf = new Config(ApplicationOrCenter.BillingCenter);
			driver = buildDriver(cf);
			new Login(driver).loginAndSearchPolicyByAccountNumber("sbrunson", "gw",
					myPolicyObj.accountNumber);
            BCPolicyMenu sidebar = new BCPolicyMenu(driver);
			sidebar.clickPaymentSchedule();
            PolicyPaymentSchedule paymentSchedule = new PolicyPaymentSchedule(driver);
			paymentSchedule.clickEditSchedule();
            paymentSchedule.selectNewPaymentPlan(PaymentPlanType.Quarterly);
            paymentSchedule.clickExecute();
            TopInfo topInfoStuff = new TopInfo(driver);
			topInfoStuff.clickTopInfoLogout();
			driver.quit();
			
			cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
			new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
					myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

            ActionsPC actions = new ActionsPC(driver);
			actions.click_Actions();
			actions.click_ExpirationDateChange();
            StartPolicyChange change = new StartPolicyChange(driver);
			change.setDescription("Expiration Date Change DE2568");
			change.clickPolicyChangeNext();
            Map<ApplicationOrCenter, Date> datesMap = ClockUtils.getCurrentDates(driver);
			Date pcDate = datesMap.get(ApplicationOrCenter.PolicyCenter);
            GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
			policyInfo.setPolicyInfoTermType("Other");
            policyInfo.setPolicyInfoExpirationDate(DateUtils.dateAddSubtract(pcDate, DateAddSubtractOptions.Month, 6));
			new GenericWorkorder(driver).clickGenericWorkorderQuote();
            GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
			if (quotePage.hasBlockBind()) {
                GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
				riskAnaysis.handleBlockSubmit(myPolicyObj);
                SideMenuPC sideMenuStuff = new SideMenuPC(driver);
				sideMenuStuff.clickSideMenuQuote();
			}
            ErrorHandlingHelpers quoteError = new ErrorHandlingHelpers(driver);
			if (quoteError.errorHandlingRiskAnalysis()) {
				Assert.fail("Generate Was NOT able to generate a valid quote after five(5) tries or two(2) minutes.");
			}
            change = new StartPolicyChange(driver);
			change.clickIssuePolicy();
		} else {
			Assert.fail("The policy plan type does not match one needed for this test. Sorry");
		}
	}

	// jlarsen 11/23/2015
	// TEST MOVED IN FROM PNIChangeTests TO REMOVE UNESSESARY GENERATE CALLS TO
	// SHORTED TESTING RUN TIMES
	/**
	 * US3924 Tests a PNI name chenge from company to company
	 * 
	 * @throws Exception
	 */
	//jlarsen 12/13/2016 disabled dure to precess change with contact transfer
	@Test(dependsOnMethods = { "createPolicy" }, enabled = false)
	public void testCompanyToCompanyPNIChange() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
				myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

		// initiate add location policy change
        StartNameChange nameChangePage = new StartNameChange(driver);
		this.myContactObj = nameChangePage.changePNI(myPolicyObj, GenerateContactType.Company, "", NamesHelper.getRandomName().getCompanyName());

		new GuidewireHelpers(driver).logout();
		driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
				myPolicyObj.underwriterInfo.getUnderwriterPassword(), this.myContactObj.accountNumber);
        AccountSummaryPC accountPage = new AccountSummaryPC(driver);
		if (!this.myContactObj.companyName.equals(accountPage.getAccountName())) {
			Assert.fail("The new account was not found in PolicyCenter.");
		}
	}

}
