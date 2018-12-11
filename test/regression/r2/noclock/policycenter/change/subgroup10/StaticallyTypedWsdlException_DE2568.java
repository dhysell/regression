/*Steps to Recreate:
In PC8QA. Issue a policy with an annual term and annual payment plan
On the same day change payment plan in BillingCenter to quarterly (Policy Summary -> Payment Schedule -> Edit)
On the same day do an expiration date change (Actions -> Expiration Date Change)
Change to six month term
Click QUote and see error
Also happened on a normal policy change*/

/*
 * Jon Larsen 6/10/2015
 * the defect calls for an issued policy with Annual payment
 * the test generates a policy with either Annual or Semi-Annual for better testing
 * 
 */
package regression.r2.noclock.policycenter.change.subgroup10;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyPaymentSchedule;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
public class StaticallyTypedWsdlException_DE2568 extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicy = null;

	// jlarsen 11/23/2015
	///////////////////////////////////////////////////////////////////////////////////////////////
	////// ALL TEST IN THIS CLASS HAVE BEEN DISABLED AND MOVED TO
	// PolicyChange.java /////////
	///////////////////////////////////////////////////////////////////////////////////////////////

	@Test(enabled = false)
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));
		ArrayList<PaymentPlanType> policyType = new ArrayList<PaymentPlanType>() {
			private static final long serialVersionUID = 1L;

			{
				this.add(PaymentPlanType.Annual);
				this.add(PaymentPlanType.Semi_Annual);
			}
		};

        myPolicy = new GeneratePolicy.Builder(driver).withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Wsdl Exception").withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(policyType.get(new Random().nextInt(policyType.size())))
				.withDownPaymentType(PaymentType.Credit_Debit).build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" }, enabled = false)
	public void policyChanges() throws Exception {

		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

        Login login = new Login(driver);
		login.login("sbrunson", "gw");

		BCSearchPolicies search = new BCSearchPolicies(driver);
		search.searchPolicyByAccountNumber(myPolicy.accountNumber);

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

        login = new Login(driver);
		login.login(myPolicy.underwriterInfo.getUnderwriterUserName(),
				myPolicy.underwriterInfo.getUnderwriterPassword());


		SearchPoliciesPC pcSearch = new SearchPoliciesPC(driver);
		pcSearch.searchPolicyByAccountNumber(myPolicy.accountNumber);

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
			riskAnaysis.handleBlockSubmit(myPolicy);
            SideMenuPC sideMenuStuff = new SideMenuPC(driver);
			sideMenuStuff.clickSideMenuQuote();
		}

        ErrorHandlingHelpers quoteError = new ErrorHandlingHelpers(driver);
		if (quoteError.errorHandlingRiskAnalysis()) {
			Assert.fail("Generate Was NOT able to generate a valid quote after five(5) tries or two(2) minutes.");
		}


        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
	}

}
