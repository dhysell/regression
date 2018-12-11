package regression.r2.clock.billingcenter.other;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.AccountPolicies;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author JQU
* @Description DE6113: Term extension with membership dues charges is rejected by BC
*				Create a squire policy (ensure new membership dues are created)
				Ensure payment plan is annual and one payer (insured)
				Pay 95% of down
				Move two days and run invoice and invoice due
				Down invoice is carried forward
				Start a policy change - term extension 32 days
				Issue policy change
				Actual Result: BC rejected the charges. Message was stuck in PC "com.guidewire.pl.web.controller.UserDisplayableException: Invoice Items total $86.00; but Charge amount is $43.00"
				Expected Result: The term extension charges should come over to BC		
* @DATE Jul 26, 2017
*/
public class TermExtensionWithMembershipDuesTest extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	
	@Test
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOnePropertyOne = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOnePropertyOne.setOwner(true);		
		locOnePropertyList.add(locationOnePropertyOne);		
		PolicyLocation locationOne = new PolicyLocation(locOnePropertyList);
		locationOne.setPlNumResidence(2);
		locationsList.add(locationOne);
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("TermExtension", "LH")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);		
	}
	@Test(dependsOnMethods = { "generatePolicy" })
	public void make95PercentOfInsuredDownPayment() throws Exception {				
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        double amountToPay = NumberUtils.round(myPolicyObj.squire.getPremium().getDownPaymentAmount() * 0.95, 2);
        newPayment.makeInsuredDownpayment(myPolicyObj, amountToPay, myPolicyObj.squire.getPolicyNumber());
		//move a few days and run batch process
        ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Day, 3));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);		
		acctMenu.clickAccountMenuInvoices();
        AccountInvoices invoice = new AccountInvoices(driver);
        if (!invoice.verifyInvoice(myPolicyObj.squire.getEffectiveDate(), myPolicyObj.squire.getEffectiveDate(), null, null, null, InvoiceStatus.Carried_Forward, null, null))
			Assert.fail("Unpay amount is not carried forward.");			
	}
	@Test(dependsOnMethods = { "make95PercentOfInsuredDownPayment" })
    public void extendPolicyTermBy32Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        StartPolicyChange policyChange = new StartPolicyChange(driver);
        policyChange.changeExpirationDate(DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, 32), "Extend Policy");
		new GuidewireHelpers(driver).logout();	
	}
	@Test(dependsOnMethods = { "extendPolicyTermBy32Days" })
    public void verifyAfterPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu acctMenu = new BCAccountMenu(driver);
		acctMenu.clickBCMenuCharges();
        AccountCharges charge = new AccountCharges(driver);
		charge.waitUntilChargesFromPolicyContextArrive(60, TransactionType.Policy_Change);
		acctMenu.clickAccountMenuPolicies();
        AccountPolicies policiesPage = new AccountPolicies(driver);
        policiesPage.verifyPolicyInTable(myPolicyObj.squire.getPolicyNumber(), null, myPolicyObj.squire.getEffectiveDate(), DateUtils.dateAddSubtract(myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, 32), null, null, null, null, null, null);
	}
}
