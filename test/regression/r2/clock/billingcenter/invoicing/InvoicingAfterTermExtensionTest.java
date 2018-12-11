package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description   DE6131-Invoice missing after term was extended
* 				 Steps: Standard fire with monthly payment plan type
					Pay off down payment
					Move clock to one day before the bill date of the second invoice
					Extend term by 3 months
					Defect: one invoice is missing after term is extended.
* 					
* @DATE August 29, 2017
*/
public class InvoicingAfterTermExtensionTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy stdFirePolicy;
	private ARUsers arUser = new ARUsers();
	private Date newExpirationDate;
	private List<Date> invoiceDateListFromUI, dueDateListFromUI;
	private List<Date> invoiceDateListShouldBe, dueDateListShouldBe;

	@Test
	public void generateStandardFire() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome));
		locationsList.add(new PolicyLocation(locOnePropertyList));

        StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
        myStandardFire.setLocationList(locationsList);
        myStandardFire.section1Deductible = SectionIDeductible.FiveHundred;

        this.stdFirePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withStandardFire(myStandardFire)
				.withInsFirstLastName("Invoicing", "Extension")
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}		
	@Test(dependsOnMethods = { "generateStandardFire" })
	public void payDownpaymentAndMoveClock() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.stdFirePolicy.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		
		invoiceDateListShouldBe=invoice.getListOfInvoiceDates();
		dueDateListShouldBe=invoice.getListOfDueDates();
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(stdFirePolicy, stdFirePolicy.standardFire.getPremium().getDownPaymentAmount(), stdFirePolicy.accountNumber);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		//move clock to 1 day BEFORE next invoice date 
        Date nextDueDate = DateUtils.dateAddSubtract(stdFirePolicy.standardFire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(nextDueDate, DateAddSubtractOptions.Day, -15));
		new GuidewireHelpers(driver).logout();
	}
	@Test(dependsOnMethods = { "payDownpaymentAndMoveClock" })
    public void extendTermBy3Months() throws Exception {
        newExpirationDate = DateUtils.dateAddSubtract(stdFirePolicy.standardFire.getExpirationDate(), DateAddSubtractOptions.Month, 3);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), stdFirePolicy.accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.changeExpirationDate(newExpirationDate, "Extend Policy by 3 Months.");		
		new GuidewireHelpers(driver).logout();		
	}
	@Test(dependsOnMethods = { "extendTermBy3Months" })
	public void verifyInvoicesAfterTermExtension() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.stdFirePolicy.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		//veriy the Invoice/Due dates
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
		dueDateListFromUI=invoice.getListOfDueDates();
		invoiceDateListFromUI=invoice.getListOfInvoiceDates();
		int beginningSize=dueDateListShouldBe.size();
		for(int i=0; i<3; i++){
            dueDateListShouldBe.add(beginningSize + i, DateUtils.dateAddSubtract(stdFirePolicy.standardFire.getExpirationDate(), DateAddSubtractOptions.Month, i));
			invoiceDateListShouldBe.add(beginningSize+i, DateUtils.dateAddSubtract(dueDateListShouldBe.get(dueDateListShouldBe.size()-1), DateAddSubtractOptions.Day, -15));
		
		}	
		//Dates have different hh/mm/ss, convert to String
		for(int j=0;j<invoiceDateListFromUI.size();j++){
			if(!DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDateListFromUI.get(j)).equals(DateUtils.dateFormatAsString("MM/dd/yyyy", invoiceDateListShouldBe.get(j))))				
				Assert.fail("Invoice dates are incorrect after term extension.");			
			if(!DateUtils.dateFormatAsString("MM/dd/yyyy", dueDateListFromUI.get(j)).equals(DateUtils.dateFormatAsString("MM/dd/yyyy", dueDateListShouldBe.get(j))))				
				Assert.fail("Due dates are incorrect after term extension.");				
		}		
	}
}
