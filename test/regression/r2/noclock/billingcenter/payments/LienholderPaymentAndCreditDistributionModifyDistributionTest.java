package regression.r2.noclock.billingcenter.payments;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.payments.AccountPaymentsCreditDistributions;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyChargesToBC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description US6085 & US6089: LH Payment Tracking - Modify Distribution on Payments and Credit Distribution test
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/12%20-%20Payments/12-01%20LH%20Payment%20Tracking%20-%20Modify%20Payment%20Details%20and%20Distribution.docx">LH Payment Tracking - Modify Payment Details and Distribution</a>
 * @DATE January 11, 2016
 */
@QuarantineClass
public class LienholderPaymentAndCreditDistributionModifyDistributionTest extends BaseTest {
	private WebDriver driver;
	private String lienholderNumber;	
	private double paymentOnChargeGroup=100, excessLienPayment=75, lienPremiumChange;
	private ARUsers arUser = new ARUsers();
	private BCAccountMenu acctMenu;
	private GeneratePolicy myPolicyObj1, myPolicyObj2;	
	private String loanNumber1="LN11111", loanNumber2="LN22222";	
	private Date currentDate;

	private GeneratePolicy generatePolicy(String insuredName, ArrayList<PolicyLocation> locationsList) throws Exception{
        return new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName(insuredName)
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	
	private void verifyPaymentDetails(AccountPayments payment, String policyNumber, String loanNumber, Double amount, Double grossAmtApplied) {
		try {
			payment.getPaymentsAndCreditDistributionsDetailsTableRow(null, null, null, policyNumber.substring(3, 9), lienholderNumber, null, null, null, amount, null, grossAmtApplied);
		} catch (Exception e) {
			Assert.fail("The Payment Details table has some incorrect information.");
		}
	}
	
	//This method generates policy1 which has 1 random lienholder, it then passes this lienholder info to the setters of policy2 and generates policy2 which has the same lienholder number but with a different loan number. 
	@Test
	public void generateTwoPolicies() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();		
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();				

		//randomly create a lienholder for policy1
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber1);
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setBuildingLimit(100000);
		loc1Bldg1.setBppLimit(100000);
		loc1Bldg1.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(loc1Bldg1);		
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		
		// Generate policy1 with one lienholder and its loan number is "LN11111"		
		this.myPolicyObj1=generatePolicy("LHModifyDistr1",locationsList);
		//assign a new loan number for the lienholder and set new limits
		loc1LNBldg1AddInterest.setLoanContractNumber(loanNumber2);
		loc1Bldg1.setBuildingLimit(120);
		loc1Bldg1.setBppLimit(120);
		//get the lienholder info and pass it to policy2		
		AddressInfo lienholderAddressInfo=myPolicyObj1.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAddress();
		
		// Generate policy2 with the same lienholder number as policy1 and its loan number is "LN22222"
		AddressInfo macuAddress = new AddressInfo();
		macuAddress.setLine1(lienholderAddressInfo.getLine1());
		macuAddress.setCity(lienholderAddressInfo.getCity());
		macuAddress.setState(lienholderAddressInfo.getState());
		macuAddress.setZip(lienholderAddressInfo.getZip());

		//overwrite locationsList
		locationsList.set(0,new PolicyLocation(new AddressInfo(), locOneBuildingList));				
		this.myPolicyObj2=generatePolicy("LHModifyDistr2",locationsList);	

		lienholderNumber=myPolicyObj1.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();					
	}	
	//make a regular payment on a charge group of the lienholder (ties to a charge group and loan number)
	//make an excess payment (not tie to a charge group or loan number)
	//verify the "Modify Payment Details" link doesn't exist for both payments. Verify "Modify Distribution" link doesn't exist for excess payment
	@Test(dependsOnMethods = { "generateTwoPolicies" })	
	public void makeTwoTypesofPayments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		currentDate= DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        NewDirectBillPayment directBillPayment = new NewDirectBillPayment(driver);
        directBillPayment.makeLienHolderPaymentExecute(this.paymentOnChargeGroup, this.myPolicyObj1.busOwnLine.getPolicyNumber(), this.loanNumber1);
		accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();
		directBillPayment = new NewDirectBillPayment(driver);
		directBillPayment.makeDirectBillPaymentExecuteWithoutDistribution(excessLienPayment);
		new GuidewireHelpers(driver).logout();
		
		//This is the old method for creating LH Payments in R2 environments. Functionality was moved to Maintenance and will be brought back later.
	/*	Configuration.setProduct(Product.BillingCenter);
		login(arUser.getUserName(), arUser.getPassword());
		topMenu = TopMenuFactory.getMenu();				
		topMenu.clickDesktopTab();
		IDesktopMenu desktopMenu=DesktopFactory.getDesktopMenuPage();
		desktopMenu.clickDesktopMenuActionsMultipleAccountLienholderPayment();
		DesktopActionsLienholderMutipleAccountPaymentWorkflow lienPayment=DesktopFactory.getDesktopActionsLienholderMultiplePaymentsWorkflow();
		lienPayment.setAmount(excessLienPayment+paymentOnChargeGroup);
		lienPayment.clickNext();
		//make a payment to policy1, select its Loan number (loanNumber1)	
		lienPayment.fillOutNextLineOnPolicyPaymentsTable(paymentOnChargeGroup, null, null, myPolicyObj1.policyNumber, loanNumber1);
				lienPayment.clickNext();
		//make an excess payment (not tie to a charge group or loan number)
		lienPayment.fillOutNextLineOnAdditionalPaymentsTable(lienholderNumber, excessLienPayment, null, null, null);
		lienPayment.clickNext();
		lienPayment.clickFinish();*/
	}
	
	@Test(dependsOnMethods = { "makeTwoTypesofPayments" })
    public void makeModifyDistributionAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		//verify that Modify Distribution link not exist for excess payment
		WebElement excessPaymentRow=payment.getPaymentsAndCreditDistributionsTableRow(currentDate, currentDate, null, null, null, null, null, null, null, null, excessLienPayment, null, excessLienPayment);
		try {
			payment.clickActionsModifyDistributionLink(new TableUtils(driver).getRowNumberFromWebElementRow(excessPaymentRow));
			Assert.fail("Modifiy Distribution link shouldn't exist for excess LH Payment. Test Failed.");
		} catch (Exception e) {
			getQALogger().info("Modifiy Distribution link doesn't exist for excess LN payment. This is expected.");
		}
		excessPaymentRow.click();
		//verify payment details and verify that Modify Distribution link exists for payment related to charge group
		WebElement regularPaymentRow=payment.getPaymentsAndCreditDistributionsTableRow(currentDate, currentDate, null, null, null, null, null, null, null, null, paymentOnChargeGroup, paymentOnChargeGroup, null);
		regularPaymentRow.click();
		//verify Payment details
        verifyPaymentDetails(payment, myPolicyObj1.busOwnLine.getPolicyNumber(), loanNumber1, myPolicyObj1.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), paymentOnChargeGroup);
		//after click, regularPaymentRow lost its pointer, need to get it again
		regularPaymentRow=payment.getPaymentsAndCreditDistributionsTableRow(currentDate, currentDate, null, null, null, null, null, null, null, null, paymentOnChargeGroup, paymentOnChargeGroup, null);
		try {			
			payment.clickActionsModifyDistributionLink(new TableUtils(driver).getRowNumberFromWebElementRow(regularPaymentRow));			
		} catch (Exception e) {
			Assert.fail("The Modify Distribution link doesn't exist.");
		}
		//verify that the payment amount to be modified is not editable
        NewDirectBillPayment modifyPmt = new NewDirectBillPayment(driver);
		try {
			modifyPmt.setAmount(123);
			Assert.fail("The Modifiy Distribution should not be editable. Test Failed.");
		} catch (Exception e) {
			getQALogger().info("Modifiy Distribution amount is not editable. This is expected.");
		}		
		//modify distribution between two policies
		//assign the amount from policy #1 to policy #2, select its first loan# (LN22222)
		modifyPmt.clickOverrideDistribution();
		modifyPmt.selectPolicyNumber(myPolicyObj2.busOwnLine.getPolicyNumber());
		modifyPmt.setLoanNumber(loanNumber2);
		modifyPmt.makeDirectBillPaymentExecute(myPolicyObj2.busOwnLine.getPolicyNumber(), loanNumber2, paymentOnChargeGroup);
		//verify Payment details after  Modified Distribution
        verifyPaymentDetails(payment, myPolicyObj2.busOwnLine.getPolicyNumber(), loanNumber2, myPolicyObj2.busOwnLine.getPremium().getTotalAdditionalInterestPremium(), paymentOnChargeGroup);
	}
	
	//the following is to test Credit Distribution screen Modify Distribution functionality
	@Test(dependsOnMethods = { "makeModifyDistributionAndVerify" })
    public void reduceLNCoverageToTestCreditDistribution() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj1.underwriterInfo.getUnderwriterUserName(), myPolicyObj1.underwriterInfo.getUnderwriterPassword(), myPolicyObj1.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.changeBuildingCoverage(1, 50000, 50000);
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();

		//get lien premium change in PC->ChargeToBC screen
        PolicySummary summary = new PolicySummary(driver);
		String transactionNumber=summary.getTransactionNumber(TransactionType.Policy_Change, "change coverage");
        SideMenuPC menu = new SideMenuPC(driver);
		menu.clickSideMenuChargesToBC();
        PolicyChargesToBC chgToBC = new PolicyChargesToBC(driver);
		lienPremiumChange=chgToBC.getAmount(transactionNumber, lienholderNumber);
	}
	@Test(dependsOnMethods = { "reduceLNCoverageToTestCreditDistribution" })		
	public void modifyDistributionTestOnCreditDistributionScreen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		acctMenu.clickAccountMenuPaymentsCreditDistributions();
		AccountPaymentsCreditDistributions crtDistribution=new AccountPaymentsCreditDistributions(driver);
		int creditDistributionRow=new TableUtils(driver).getRowNumberFromWebElementRow(crtDistribution.getPaymentsAndCreditDistributionsTableRow(currentDate, currentDate, null, null, null, null, null, null, null, null, lienPremiumChange, null, null));
		try {			
			crtDistribution.clickActionsModifyDistributionLink(creditDistributionRow);			
		} catch (Exception e) {
			Assert.fail("The Modify Distribution link doesn't exist.");
		}
        NewDirectBillPayment modifyPmt = new NewDirectBillPayment(driver);
		
		//modify distribution between two policies
		//assign the amount from policy #1 to policy #2, select its first loan# (LN22222)
		modifyPmt.clickUseUnappliedFundAmount();
		modifyPmt.clickOverrideDistribution();
		modifyPmt.selectPolicyNumber(myPolicyObj2.busOwnLine.getPolicyNumber());
		modifyPmt.setLoanNumber(loanNumber2);
		modifyPmt.makeDirectBillPaymentExecute(myPolicyObj2.busOwnLine.getPolicyNumber(), loanNumber2, excessLienPayment);
		/*
		 * verification Modify Distribution on Credit Distribution is blocked by DE3229, will finish this part when the defect is fixed.
		 */
		//verify the Modified Distribution
		//verifyPaymentDetails(modifyPmt, myPolicyObj2.policyNumber, loanNumber2, myPolicyObj2.totalAdditionalInterestPremium, excessLienPayment);		
		
	}
}
