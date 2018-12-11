package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DelinquencyReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description   US13897 -- Insured & Lien issuance 
* 				  Shortage invoice not paid by insured (only dues) triggers Past Due Full Cancel* 					
* @DATE March 21, 2018
*/
public class AddDuesMidtermToDelinquencyTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private double duesAmount;	
	
	@Test
	public void generatePolicy() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
	
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));
	
	    StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
	    myStandardFire.setLocationList(locationsList);
	    myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;
	
		this.myPolicyObj = new GeneratePolicy.Builder(driver)
	            .withProductType(ProductLineType.StandardFire)
	            .withLineSelection(LineSelection.StandardFirePL)
	            .withStandardFire(myStandardFire)
				.withInsFirstLastName("Dues", "Delinquency")				
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.ACH_EFT)
				.build(GeneratePolicyType.PolicyIssued);	
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })	
	public void payDownPaymentAndMoveClock() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		cf.setCenter(ApplicationOrCenter.BillingCenter);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//ACH-EFT will pay the down payment
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
	}
	@Test(dependsOnMethods = { "payDownPaymentAndMoveClock" })	
	public void addDuesInPolicyCenter() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		cf.setCenter(ApplicationOrCenter.PolicyCenter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), myPolicyObj.accountNumber);
        StartPolicyChange pcChange = new StartPolicyChange(driver);
        pcChange.selectMembershipLine();
        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
        complete.clickViewYourPolicy();
        PolicySummary pcSum = new PolicySummary(driver);
        duesAmount = pcSum.getPremiumByType(TransactionType.Policy_Change);
	}
	@Test(dependsOnMethods = { "addDuesInPolicyCenter" })	
	public void triggerDelinquencyAndVerify() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
		//wait for Policy Change to come
		BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
		AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		
		//verify the shortage invoice
		Date duesDueDate = DateUtils.dateAddSubtract(myPolicyObj.standardFire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		acctMenu.clickAccountMenuInvoices();
		AccountInvoices invoice = new AccountInvoices(driver);		
		if(!invoice.verifyInvoice(DateUtils.dateAddSubtract(duesDueDate, DateAddSubtractOptions.Day, myPolicyObj.standardFire.getPaymentPlanType().getInvoicingLeadTime()), duesDueDate, null, InvoiceType.Shortage, null, null, duesAmount, duesAmount)){
			Assert.fail("Didn't find the correct Dues invoice.");
		}
		//trigger delinquency
		ClockUtils.setCurrentDates(driver, duesDueDate);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);	
		//verify delinquency
		acctMenu.clickBCMenuDelinquencies();
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		delinquency.verifyDelinquencyExists(OpenClosed.Open, DelinquencyReason.PastDueFullCancel, myPolicyObj.accountNumber, null, myPolicyObj.accountNumber, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), duesAmount, duesAmount);
	}	
}
