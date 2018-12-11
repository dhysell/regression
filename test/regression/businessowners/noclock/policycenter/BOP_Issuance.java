package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsPromisedPayments;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.topmenu.BCTopMenuAccount;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPayment;

/**
 * @Author ecoleman
 * @Requirement BOP Issuance
 * @Description testing to make sure that a BOP policy issuance works
 * @DATE Apr 03, 2018
 */
public class BOP_Issuance extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withProductType(ProductLineType.Businessowners)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsCompanyName("MMM BOP")
				.withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.getRandom())
				.withLineSelection(LineSelection.Businessowners)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	
	/**
	 * @Author ecoleman
	 * @Description - Verifications
	 * @DATE Apr 03, 2018
	 * @throws Exception
	 */
	@Test(enabled=true)
	public void verifyBOPPolicyIssuance() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderPayment pcPaymentPage = new GenericWorkorderPayment(driver);
		
		pcSideMenu.clickSideMenuPayment();

		Double collectedByAgentAmountFromPC = pcPaymentPage.getInsuredPaymentAmount();
		Double totalPremiumAmountFromPC = pcPaymentPage.getTotalPremiumPortion();
		
		bcSide(collectedByAgentAmountFromPC, totalPremiumAmountFromPC);
	}
	
	// 5/16/18 : Split out BC side to deal with stale driver reference in the TableUtils
	// 			 Remove when tableutils aren't static anymore.
    private void bcSide(Double collectedByAgentAmountFromPC, Double totalPremiumAmountFromPC) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).login("su", "gw");

        BCAccountSummary bcAccountSummaryPage = new BCAccountSummary(driver);
        BCTopMenuAccount bcAccountTopMenu = new BCTopMenuAccount(driver);
        BCAccountMenu bcAccountSideMenu = new BCAccountMenu(driver);
		AccountPaymentsPromisedPayments bcPromisedPaymentsPage = new AccountPaymentsPromisedPayments(driver);
        AccountPayments BCAccountPaymentsPage = new AccountPayments(driver);
        BCPolicySummary bcPolicySummaryPage = new BCPolicySummary(driver);
		
		
		bcAccountTopMenu.menuAccountSearchAccountByAccountNumber(myPolicyObject.accountNumber);

		String downPaymentTypeFromPolicy = myPolicyObject.downPaymentType.toString();
		downPaymentTypeFromPolicy = StripStringAndTokenizeAndGetFirstToken(downPaymentTypeFromPolicy);
		
		switch (myPolicyObject.downPaymentType) {
		case Cash:
		case Intercompany_Transfer:
		case Title_Company:
		case Cash_Equivalent:
		case Check:
		case Website:
			bcAccountSideMenu.clickAccountMenuPaymentsPromisedPayments();	
			bcPromisedPaymentsPage.waitUntilBindPromisedPaymentsArrive(120);
			Double paymentAmountFromBC = Double.parseDouble(bcPromisedPaymentsPage.label_PaymentAmount.getText().replace(",", "").replace("$", ""));
			String paymentTypeFromBC = StripStringAndTokenizeAndGetFirstToken(bcPromisedPaymentsPage.label_PaymentType.getText());
			Assert.assertEquals(paymentAmountFromBC, collectedByAgentAmountFromPC, "Payment amount was not correct, actual:  '" + paymentAmountFromBC + "' expected: '" + collectedByAgentAmountFromPC + "'");
			Assert.assertEquals(paymentTypeFromBC, downPaymentTypeFromPolicy, "Payment amount was not correct, actual:  '" + paymentTypeFromBC + "' expected: '" + downPaymentTypeFromPolicy + "'");
			break;
		case ACH_EFT:
		case Credit_Debit:
			bcAccountSummaryPage.waitUntilRecievedAndGetFirstRecentPaymentAmount(downPaymentTypeFromPolicy);
			bcAccountSideMenu.clickAccountMenuPayments();
			double recentPaymentAmount = BCAccountPaymentsPage.getPaymentAmount(1);
			Assert.assertTrue(BCAccountPaymentsPage.verifyPaymentInstrument(downPaymentTypeFromPolicy), " '" + downPaymentTypeFromPolicy + "' was not on page.");
			Assert.assertEquals(java.util.Optional.of(recentPaymentAmount), collectedByAgentAmountFromPC, "Payment amount did not match '" + collectedByAgentAmountFromPC + "' Expected, but '" + recentPaymentAmount + "' Actual");
			break;
		}

		bcAccountSideMenu.clickBCMenuSummary();
        // TODO: 5/8/18 test this
		bcAccountSummaryPage.clickPolicyNumberInOpenPolicyStatusTable(myPolicyObject.accountNumber);
		//bcAccountSummaryPage.clickFirstPolicyInOpenPolicyStatusTable();

		String paymentType = bcPolicySummaryPage.getPaymentPlanType();
		paymentType = StripStringAndTokenizeAndGetFirstToken(paymentType);
		String planPaymentType = myPolicyObject.paymentPlanType.toString();
		//String planPaymentType = PaymentPlanType.Annual.toString();
		planPaymentType = StripStringAndTokenizeAndGetFirstToken(planPaymentType);
		double totalPremiumBCPolicyLevel = bcPolicySummaryPage.getPremiumCharges();
		
		Assert.assertEquals(java.util.Optional.of(totalPremiumBCPolicyLevel), totalPremiumAmountFromPC, "Premium amount did not match '" + totalPremiumAmountFromPC + "' Expected, but '" + totalPremiumBCPolicyLevel + "' Actual");
		Assert.assertTrue(planPaymentType.contains(paymentType), "Payment plan did not match! '" + planPaymentType + "' Expected, but '" + paymentType + "' Actual");
	}
	
	// HELPER FUNCTION
	private String StripStringAndTokenizeAndGetFirstToken(String input) { 
		return input.split("-|\\.|,|$|_|\\s")[0];
	}
}
