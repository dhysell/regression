package regression.r2.noclock.billingcenter.disbursements;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
public class TestDisbursementOnCancelledPolicy extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj = null;
	private AccountDisbursements acctDisbmt;
	private double firstPaymentAmount = 0;
	private double secondPaymentAmount = 0;
	
	@Test()
	//creating a new insured policy
	public void testBasicIssuanceInsuredOnly() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
			.withInsPersonOrCompany(ContactSubType.Company)
			.withInsCompanyName("Test Policy")
			.withPolOrgType(OrganizationType.Partnership)
			.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
			.withPolicyLocations(locationsList)
			.withPaymentPlanType(PaymentPlanType.Monthly)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"testBasicIssuanceInsuredOnly"}) 
	//Cancellation of the policy
    public void testCancellation() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.myPolicyObj.underwriterInfo.getUnderwriterUserName(), this.myPolicyObj.underwriterInfo.getUnderwriterPassword(), this.myPolicyObj.accountNumber);

        StartCancellation cancelPolicy = new StartCancellation(driver);
		cancelPolicy.cancelPolicy(CancellationSourceReasonExplanation.SubmittedOnAccident, "Messup on Agent's Part", null, true);


		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();
}

	@Test(dependsOnMethods = {"testCancellation"})
    public void testMakePaymentMore() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		//log into BC-put in the policy number
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", this.myPolicyObj.accountNumber);
		
		//make a payment of less than $4.99
		firstPaymentAmount= 2.99;
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment paymentsPage = new NewDirectBillPayment(driver);
		paymentsPage.makeDirectBillPaymentExecuteWithoutDistribution(firstPaymentAmount);

		//reaching to the disbursement page 
		accountMenu.clickAccountMenuDisbursements();
		acctDisbmt = new AccountDisbursements(driver);
		
		//Checking that the disbursement is not created 
		int rowCount = acctDisbmt.getDisbursementTableRowCount();
		Assert.assertTrue(rowCount <= 1, "Assertion Failed, there is disbursement");
		
	}
		
	@Test(dependsOnMethods = {"testMakePaymentMore"})
    public void testMakePayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		//log into BC 
		new Login(driver).loginAndSearchAccountByAccountNumber("sbrunson", "gw", this.myPolicyObj.accountNumber);
		
		//make a payment of more than $4.99
		secondPaymentAmount = 5.99;
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment paymentsPage = new NewDirectBillPayment(driver);
		paymentsPage.makeDirectBillPaymentExecuteWithoutDistribution(secondPaymentAmount);
        accountMenu = new BCAccountMenu(driver);
		
		//reaching to the disbursement page
		accountMenu.clickAccountMenuDisbursements();
		acctDisbmt = new AccountDisbursements(driver);
		
		//checking that the disbursement is created for the amount more than $4.99
		List<WebElement> webElements = acctDisbmt.getDisbursements(firstPaymentAmount + secondPaymentAmount);
		Assert.assertTrue(webElements.size() > 0, "Assertion Failed, there is no disbursement");
		acctDisbmt.clickRowInDisbursementsTableByText(StringsUtils.currencyRepresentationOfNumber(firstPaymentAmount + secondPaymentAmount));
	}
	
}
	
