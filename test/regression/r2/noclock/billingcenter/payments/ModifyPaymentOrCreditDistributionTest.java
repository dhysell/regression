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
import repository.bc.wizards.NewCreditWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.CreditType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IncludeOnly;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author jqu
 * @Description From the payment and credit distribution screen, doing a modify distribution on a previous credit distribution line,
 * "This Payment/This Credit Distribution" amounts should update after putting Override Amount.
 * @RequirementsLink N/A
 * @DATE March 22nd, 2016
 */
@QuarantineClass
public class ModifyPaymentOrCreditDistributionTest extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;	
	private String lienholderNumber, loanNumber="LN11111";
	private ARUsers arUser = new ARUsers();
	private double lienPremium;
	private double totalPayment=new Double(NumberUtils.generateRandomNumberInt(100, 200));
	private double amountToDistribute=new Double(NumberUtils.generateRandomNumberInt(30, 90));
	private BCAccountMenu acctMenu;
	private Date currentDate;
	private boolean creditDistributionScreen=false;
	
	private void verifyPaymentOrCreditDistribution() throws GuidewireException{
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
		directPayment.clickOverrideDistribution();

		if(creditDistributionScreen){
			directPayment.selectDistributionTabIncludeOnly(IncludeOnly.Up_to_amount_under_contract);
		}
        directPayment.setOverrideAmount(null, myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber, null, null, null, lienPremium, amountToDistribute);
		if (directPayment.getAvailableAmount() != totalPayment || directPayment.getDistributedAmount() != amountToDistribute || directPayment.getRemainingAmount() != (totalPayment - amountToDistribute))
			Assert.fail("the Available/Distributed/Remaining amounts are wrong. They are " +directPayment.getAvailableAmount()+"/"+directPayment.getDistributedAmount()+"/"+directPayment.getRemainingAmount()
					+", while they should be "+totalPayment+"/"+amountToDistribute+"/"+(totalPayment-amountToDistribute)+".");
	}
	@Test
	public void generate() throws Exception {
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();			
		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);		
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg2AddInterest.setLoanContractNumber(loanNumber);
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);
		PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
		loc1Bldg2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(loc1Bldg2);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withInsCompanyName("TestLienCreditOnCancel")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		lienholderNumber=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();	
		lienPremium=myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
	}
	//make payment/credit on lienholder
	@Test(dependsOnMethods = { "generate" })
	public void payLienholders() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
		//make a payment to test Modify Distribution on Payment screen
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), loanNumber, lienPremium);
		//make a credit to test Modify Distribution on Credit Distribution screen
		acctMenu.clickAccountMenuActionsNewTransactionCredit();
		NewCreditWizard newCredit = new NewCreditWizard(driver);
        newCredit.setUnappliedFund(myPolicyObj.busOwnLine.getPolicyNumber());
		newCredit.setCreditType(CreditType.Other);
		newCredit.setAmount(totalPayment);
		newCredit.clickNext();
		newCredit.clickFinish();
		//distribute the credit to the policy
		acctMenu.clickAccountMenuActionsNewDirectBillCreditDistribution();
		directPayment.clickUseUnappliedFundAmount();
		directPayment.selectPolicyNumber(myPolicyObj.busOwnLine.getPolicyNumber());
		directPayment.selectLoanNumber(myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber());
		directPayment.clickExecute();
	}
	//Verify "This Payment" amounts by modifying distribution on Payment screen
	@Test(dependsOnMethods = { "payLienholders" })
	public void modifyDistributionOnPaymentAndVerify() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPayments();
		AccountPayments payment = new AccountPayments(driver);
		WebElement paymentRow = null;
		try{
			paymentRow=payment.getPaymentsAndCreditDistributionsTableRow(currentDate, currentDate, null, null, null, null, null, null, null, null, totalPayment, totalPayment, null);		
		}catch(Exception e){
			Assert.fail("The payment doesn't exist.");
		}			
		payment.clickActionsModifyDistributionLink(new TableUtils(driver).getRowNumberFromWebElementRow(paymentRow));
		verifyPaymentOrCreditDistribution();
	}
	//Verify "This Credit Distribution" amounts by modifying distribution on Credit Distribution screen
	@Test(dependsOnMethods = { "modifyDistributionOnPaymentAndVerify" })
	public void modifyDistributionOnCreditDistributionAndVerify() throws Exception {
		creditDistributionScreen=true;
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuPaymentsCreditDistributions();		
		AccountPaymentsCreditDistributions creditDistribution=new AccountPaymentsCreditDistributions(driver);
		WebElement paymentRow = null;
		try{
			paymentRow=creditDistribution.getPaymentsAndCreditDistributionsTableRow(currentDate, currentDate, null, null, null, null, null, null, null, null, null, totalPayment, null);				
		}catch(Exception e){
			Assert.fail("The credit payment doesn't exist.");
		}			
		creditDistribution.clickActionsModifyDistributionLink(new TableUtils(driver).getRowNumberFromWebElementRow(paymentRow));
		verifyPaymentOrCreditDistribution();
	}
}
