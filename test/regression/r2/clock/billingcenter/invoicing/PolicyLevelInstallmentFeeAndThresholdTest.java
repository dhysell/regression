package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.CarryOverCharge;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author jqu
* @Description: Test Installment Fee on policy level (single policy or lead policy), and threshold for Carry Over Charges
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Invoice%20Scheduling/Charges/Installment%20Fee_Business%20Requirements.docx">Business Requirements</a> 
* @DATE Nov 05, 2015
*/

@QuarantineClass
public class PolicyLevelInstallmentFeeAndThresholdTest extends BaseTest {
	private WebDriver driver;
    private BCAccountMenu acctMenu;
	private List<Date> calculatedInvoiceDate;
	private int errorCount=0;
	private AccountInvoices acctInvoice;
	private boolean found;
	private AccountCharges acctChg;
	private CarryOverCharge carryoverChg;
	private double installmentFee=4;
	public ARUsers arUser = new ARUsers();
	private GeneratePolicy squirePolicyObj = null;
	
	private boolean verifyInstallmentFeeInCharges(Date date, String policyNumber, double amount){
		acctMenu.clickBCMenuCharges();
        acctChg = new AccountCharges(driver);
		found= acctChg.verifyCharges(date, ChargeCategory.Installment_Fee, policyNumber, amount,null,null,null,null,null);
		if(!found){
			System.out.println("Couldn't find the Installment Fee for policy "+ policyNumber);
			errorCount++;
		}
		return found;
	}	
	private void createCarryOverCharge(double amount, String pcNumber){
		acctMenu.clickBCMenuCharges();
        acctChg = new AccountCharges(driver);
        acctChg.clickCarryOverCharge(squirePolicyObj.accountNumber, TransactionNumber.Premium_Charged, TransactionType.Policy_Issuance, pcNumber);
        carryoverChg = new CarryOverCharge(driver);
		carryoverChg.setCarryOverAmount(amount);
		carryoverChg.clickUpdate();
    }
	
	@Test
    public void generatePolicy() throws Exception {
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.squirePolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("Full Cancel", "UW Delinquency")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		getQALogger().info("Policy name is " + squirePolicyObj.pniContact.getFullName());
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void addPolicyToAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        squirePolicyObj.squireUmbrellaInfo = squireUmbrellaInfo;
        squirePolicyObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "addPolicyToAccount" })
	public void createInstallmentFeeAndVerify() throws Exception {		
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), squirePolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
        List<Date> calculatedDueDate = acctInvoice.calculateListOfDueDates(squirePolicyObj.squireUmbrellaInfo.getEffectiveDate(), squirePolicyObj.squireUmbrellaInfo.getExpirationDate(), squirePolicyObj.paymentPlanType);
		calculatedInvoiceDate=acctInvoice.calculateListOfInvoiceDates(calculatedDueDate, squirePolicyObj.paymentPlanType);	
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);		
		//pay downpayment		
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
        directPay.makeInsuredDownpayment(squirePolicyObj, squirePolicyObj.squire.getPremium().getDownPaymentAmount() + squirePolicyObj.squireUmbrellaInfo.getPremium().getDownPaymentAmount(), squirePolicyObj.squireUmbrellaInfo.getPolicyNumber());
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		//move clock to first installment(second invoice)
		ClockUtils.setCurrentDates(cf, calculatedInvoiceDate.get(1));
		
		//run invoice to create the Installment Fee
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickBCMenuCharges();
        acctChg = new AccountCharges(driver);
        verifyInstallmentFeeInCharges(calculatedInvoiceDate.get(1), squirePolicyObj.squireUmbrellaInfo.getPolicyNumber(), installmentFee);
	}
	
	@Test(dependsOnMethods = { "createInstallmentFeeAndVerify" })
    public void createAndVerifyInstallmentFeeForLeadPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), squirePolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);

		ClockUtils.setCurrentDates(cf, calculatedInvoiceDate.get(2));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		acctInvoice.clickRowByInvoiceDate(calculatedInvoiceDate.get(2));
        found = acctInvoice.verifyInvoiceCharges(null, calculatedInvoiceDate.get(2), squirePolicyObj.squireUmbrellaInfo.getPolicyNumber(), ChargeCategory.Installment_Fee, null, null, null, null, installmentFee, null, null, null);
		if(!found){
			System.out.println("Didn't find the installment fee for the lead policy");
			errorCount++;
		}		
	}	
	@Test(dependsOnMethods = { "createAndVerifyInstallmentFeeForLeadPolicy" })	
	public void testCarryOverThreshold() throws Exception {
		double amountBelowThreshold=12, amountOverThreshold=16;
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), squirePolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        createCarryOverCharge(amountBelowThreshold, squirePolicyObj.squireUmbrellaInfo.getPolicyNumber());
		
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(calculatedInvoiceDate.get(2), DateAddSubtractOptions.Day, 31));
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		driver.navigate().refresh();
        found = acctInvoice.verifyInvoice(InvoiceType.Shortage, InvoiceStatus.Carried_Forward, amountBelowThreshold);
		if(!found){
			System.out.println("The status for the Carry Over Charge (under threshold) is not correct.");
			errorCount++;
        }
        createCarryOverCharge(amountOverThreshold, squirePolicyObj.squireUmbrellaInfo.getPolicyNumber());
		acctMenu.clickAccountMenuInvoices();
        acctInvoice = new AccountInvoices(driver);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		driver.navigate().refresh();
        found = acctInvoice.verifyInvoice(InvoiceType.Shortage, InvoiceStatus.Billed, amountOverThreshold);
		if(!found){
			System.out.println("The status for the Carry Over Charge (over threshold) is not correct.");
			errorCount++;
		}		
		if(errorCount>0){
			throw new GuidewireException("BillingCenter: ",
					"found errors, please see the output for the detail.");
	    }	
	}	
}
