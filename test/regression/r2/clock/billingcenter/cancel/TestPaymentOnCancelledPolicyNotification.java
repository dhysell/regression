package regression.r2.clock.billingcenter.cancel;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test is to ensure that payments on a partially cancelled policy trigger the payment on a cancelled policy activity in PC to
 * alert PC users that a payment was made on that specific policy loan number combo so they can reinstate that coverage.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/56891029623">Rally Story US7999</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-01%20Payment%20on%20Cancelled%20Policy.docx">Requirements Documentation</a>
 * @Description
 * @DATE Jun 30, 2016
 */
@QuarantineClass
public class TestPaymentOnCancelledPolicyNotification extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private double paymentAmountForOverpayment = 60.00;
	private String paymentDateTimeStamp = null;
	
	@Test
	public void generatePolicy() throws Exception {
				
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList <AdditionalInterest>();
		ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList <AdditionalInterest>();
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setClassClassification("storage");
		
		AddressInfo macuAddress = new AddressInfo();
		macuAddress.setLine1("PO Box 691010");
		macuAddress.setCity("San Antonio");
		macuAddress.setState(State.Texas);
		macuAddress.setZip("78269-1010");
		
		AdditionalInterest loc1Bld1AddInterest = new AdditionalInterest("Mountain America", macuAddress);
		loc1Bld1AddInterest.setAddress(macuAddress);
		loc1Bld1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bld1AddInterest.setLoanContractNumber("LN1234");
		loc1Bldg1AdditionalInterests.add(loc1Bld1AddInterest);
		loc1Bldg1.setAdditionalInterestList(loc1Bldg1AdditionalInterests);
		
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setYearBuilt(2010);
		loc1Bldg2.setClassClassification("storage");
		
		AdditionalInterest loc1Bldg2AddInterest = new AdditionalInterest("Mountain America", macuAddress);
		loc1Bldg2AddInterest.setAddress(macuAddress);
		loc1Bldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Bldg2AddInterest.setLoanContractNumber("LN5678");
		loc1Bldg2AdditionalInterests.add(loc1Bldg2AddInterest);
		loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);
		
		locOneBuildingList.add(loc1Bldg1);
		locOneBuildingList.add(loc1Bldg2);
		
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("Payment On Partial Cancel")
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities))
				.withPolOrgType(OrganizationType.Partnership)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 1);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
    public void makeHalfOfLHPayment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber());
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber(), (this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount() / 2));
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeHalfOfLHPayment" })
    public void partialCancelSecondLienholder() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.busOwnLine.getPolicyNumber());

        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.removeBuildingByBldgNumber(2);
		
		new GuidewireHelpers(driver).logout();

    }
	
	@Test(dependsOnMethods = { "partialCancelSecondLienholder" })
    public void verifyThatPartialPaymentActivityDoesNotTriggerInPC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC accountSummary = new AccountSummaryPC(driver);
		if (accountSummary.verifyCurrentActivity("Partial Cancel Payment Received", 15)) {
			Assert.fail("The Partial Cancel Payment Recieved activity should not have triggered by simply partial cancelling the lienholder. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "partialCancelSecondLienholder" })
    public void moveClocks2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 5);
	}
	
	@Test(dependsOnMethods = { "moveClocks2" })
    public void makePaymentOnCancelledPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber());
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
		this.paymentDateTimeStamp = DateUtils.dateFormatAsString("MM/dd/yyyy h:mm a", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        newPayment.makeDirectBillPaymentExecuteWithoutDistribution(this.myPolicyObj.busOwnLine.getPolicyNumber(), this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber(), this.paymentAmountForOverpayment);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makePaymentOnCancelledPolicy" })
    public void checkPaymentOnCancelledPolicyActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC accountSummary = new AccountSummaryPC(driver);
		if (accountSummary.verifyCurrentActivity("Partial Cancel Payment Received", 15)) {
			accountSummary.clickActivitySubject("Partial Cancel Payment Received");

            ActivityPopup activityPopup = new ActivityPopup(driver);
			String activityPopUpDescription = activityPopup.getActivityDescription();
            String activityDescriptionCheck = "Overpayment of " + StringsUtils.formatDoubleValueToDecimalPlaces(this.paymentAmountForOverpayment, 2) + " on account # " + this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber() + " was received on " + this.paymentDateTimeStamp + ". Paid by " + this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber() + "(" + this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNameFromPolicyCenter() + "). Policy Number: " + this.myPolicyObj.busOwnLine.getPolicyNumber() + ". Loan Number: " + this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber() + ".";
			
			if (!activityPopUpDescription.equalsIgnoreCase(activityDescriptionCheck)) {
				Assert.fail("The content of the activity was not correct. It should have read:\n " + activityDescriptionCheck + " \n But instead read:\n " + activityPopUpDescription);
			}
		} else {
			Assert.fail("The Partial Cancel Payment Received Activity was not found in Policy Center after waiting 25 seconds.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}
