package regression.r2.noclock.billingcenter.charges;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test is designed to make sure that SR-22 fees (and to a lesser extent, Membership Dues, since a PL policy requires them and they are invoiced
 * and paid with the same priority) come over to BC and are paid correctly.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/58119113332">Rally Story US8250</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/userstory/49944254084">Rally Story US6649</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-05%20SR-22%20Installment%20Scheduling.docx">Requirements Documentation</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/PersonalAuto/PC8%20-%20PA%20-%20QuoteApplication%20-%20SR-22%20Charge.xlsx">Requirements Documentation</a>
 * @Description
 * @DATE Sep 1, 2016
 */
public class TestSR22InstallmentScheduling extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private ARUsers arUser = new ARUsers();
	private int numberOfSR22Charges = 0;
	
	@Test(enabled = true)
	public void generatePolicy() throws Exception {

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact policyDriver = new Contact();
		policyDriver.setRelationToInsured(RelationshipToInsured.Insured);
		policyDriver.setSR22Charges(true);
		driversList.add(policyDriver);

		// vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
		vehicleList.add(vehicle);

		// line auto coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
		coverages.setUnderinsured(false);

		// whole auto line
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Contact myContact = new Contact("SR22", "Test");
		myContact.setSR22Charges(true);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withContact(myContact)
                .withPaymentPlanType(PaymentPlanType.getRandom())
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(enabled = true, dependsOnMethods = { "generatePolicy" })
	public void verifySR22Scheduling() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);

		boolean originalInvoiceRowFound = false;
		List <WebElement> invoiceRows = new TableUtils(driver).getAllTableRows(invoices.getAccountInvoicesTable());
		for (Contact driver : myPolicyObj.squire.squirePA.getDriversList()) {
			if (driver.hasSR22Charges()) {
				numberOfSR22Charges++;
			}
		}
		
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();

        columnRowKeyValuePairs.put("Policy", myPolicyObj.squire.getPolicyNumber());
		columnRowKeyValuePairs.put("Category", ChargeCategory.SR22_Fee.getValue());
		columnRowKeyValuePairs.put("Product", SquireEligibility.CustomAuto.getValue());
        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(myPolicyObj.squire.getPremium().getSr22ChargesAmount() / this.numberOfSR22Charges));
			
		for (WebElement tableRow : invoiceRows) {
			new BasePage(driver).clickWhenClickable(tableRow);

			List <WebElement> invoiceChargesRows = new TableUtils(driver).getRowsInTableByColumnsAndValues(invoices.getAccountInvoiceChargesTable(), columnRowKeyValuePairs);
			if (invoiceChargesRows.size() > 0) {
				if (invoiceChargesRows.size() != this.numberOfSR22Charges) {
					String numberSR22FailureWording = "";
					String invoiceChargeRowFailureWording = "";
					
					if (invoiceChargesRows.size() == 1) {
						numberSR22FailureWording = "charge";
					} else {
						numberSR22FailureWording = "charges";
					}
					
					if (invoiceChargesRows.size() == 1) {
						invoiceChargeRowFailureWording = "was";
					} else {
						invoiceChargeRowFailureWording = "were";
					}
					
					Assert.fail("The number of charges contained in the invoice for SR-22 Charges did not match the number of SR-22 charges expected. There should have been " + this.numberOfSR22Charges + " " + numberSR22FailureWording + " , but there " + invoiceChargeRowFailureWording + " only " + invoiceChargesRows.size() + " in the invoice. Test failed.");
				}
				originalInvoiceRowFound = true;
				break;
			}
		}
		if (originalInvoiceRowFound == false) {
			Assert.fail("The Invoice Charges Table did not contain the line for SR-22 charges as expected. Test Failed.");
		}
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();
		

		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();


		DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
		//We must make a payment here to include all SR-22 charges and Membership dues, since they are both high priority and will be paid off equally.
        multiPayment.makeMultiplePayment(myPolicyObj.squire.getPolicyNumber(), null, (myPolicyObj.squire.getPremium().getSr22ChargesAmount() + myPolicyObj.squire.getPremium().getMembershipDuesAmount()));
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);

		BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
		accountSearch.searchAccountByAccountNumber(myPolicyObj.accountNumber);
		

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
        invoices = new AccountInvoices(driver);

        invoices = new AccountInvoices(driver);
		invoices.clickAccountInvoicesTableRowByRowNumber(1);

        columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(myPolicyObj.squire.getPremium().getSr22ChargesAmount() / this.numberOfSR22Charges));
		
		boolean paidInvoiceRowFound = false;
		List <WebElement> invoiceChargesRows = new TableUtils(driver).getRowsInTableByColumnsAndValues(invoices.getAccountInvoiceChargesTable(), columnRowKeyValuePairs);
		if (invoiceChargesRows.size() > 0) {
			if (invoiceChargesRows.size() != this.numberOfSR22Charges) {
				String numberSR22FailureWording = "";
				String invoiceChargeRowFailureWording = "";
				
				if (invoiceChargesRows.size() == 1) {
					numberSR22FailureWording = "charge";
				} else {
					numberSR22FailureWording = "charges";
				}
				
				if (invoiceChargesRows.size() == 1) {
					invoiceChargeRowFailureWording = "was";
				} else {
					invoiceChargeRowFailureWording = "were";
				}
				
				Assert.fail("The number of charges contained in the invoice for SR-22 Charges did not match the number of SR-22 charges expected. There should have been " + this.numberOfSR22Charges + " " + numberSR22FailureWording + " , but there " + invoiceChargeRowFailureWording + " only " + invoiceChargesRows.size() + " in the invoice. Test failed.");
			}
			paidInvoiceRowFound = true;
		}
		
		if (paidInvoiceRowFound == false) {
			Assert.fail("The Invoice Charges Table did not contain the line for SR-22 Charges as expected, with SR-22 Charges paid first. Test Failed.");
		}
		
		ArrayList<String> membershipDuesColumnNames = new ArrayList<String>();
		membershipDuesColumnNames.add("Policy");
		membershipDuesColumnNames.add("Category");
		membershipDuesColumnNames.add("Product");
		membershipDuesColumnNames.add("Amount");
		membershipDuesColumnNames.add("Paid Amount");
		
		ArrayList<String> membershipDuesColumnRowValues = new ArrayList<String>();
        membershipDuesColumnRowValues.add(myPolicyObj.squire.getPolicyNumber());
		membershipDuesColumnRowValues.add(ChargeCategory.Membership_Dues.getValue());
		membershipDuesColumnRowValues.add(SquireEligibility.CustomAuto.getValue());
        membershipDuesColumnRowValues.add(StringsUtils.currencyRepresentationOfNumber(myPolicyObj.squire.getPremium().getMembershipDuesAmountForPrimaryNamedInsured()));
        membershipDuesColumnRowValues.add(StringsUtils.currencyRepresentationOfNumber(myPolicyObj.squire.getPremium().getMembershipDuesAmountForPrimaryNamedInsured()));

        if (myPolicyObj.pniContact.hasMembershipDues()) {
			List <WebElement> membershipDuesInvoiceChargesRows = new TableUtils(driver).getRowsInTableByColumnsAndValues(invoices.getAccountInvoiceChargesTable(), columnRowKeyValuePairs);
			if (membershipDuesInvoiceChargesRows.size() < 1) {
				Assert.fail("The Invoice Charges Table did not contain the line for membership dues for the PNI as expected, with membership dues paid first. Test Failed.");
			}
		}
		for (PolicyInfoAdditionalNamedInsured ani : myPolicyObj.aniList) {
			if (ani.hasMembershipDues()) {
				membershipDuesColumnRowValues.remove(membershipDuesColumnRowValues.size() - 1);
				membershipDuesColumnRowValues.remove(membershipDuesColumnRowValues.size() - 1);
				
				List <WebElement> membershipDuesInvoiceChargesRows = new TableUtils(driver).getRowsInTableByColumnsAndValues(invoices.getAccountInvoiceChargesTable(), columnRowKeyValuePairs);
				if (membershipDuesInvoiceChargesRows.size() < 1) {
					Assert.fail("The Invoice Charges Table did not contain the line for membership dues for the ANI as expected, with membership dues paid first. Test Failed.");
				}
			}
		}
	}
}
