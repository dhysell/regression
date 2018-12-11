package regression.r2.clock.billingcenter.renewals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
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
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;


/**
* @Author bhiltbrand
* @Requirement This test is designed to test the blackout window for policy cancellation during renewal. It creates a policy and renews it, 
* then randomly decides between one of 4 different scenarios to cancel the policy and verify the resultant invoicing.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/81080857012">Rally Story US10044</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-04%20Regular%20Renewal%20Installment%20Scheduling.docx">Regular Renewal Installment Scheduling Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-03%20New%20Renewal%20Installment%20Scheduling.docx">Transition Renewal Installment Scheduling Requirements</a>
* @Description 
* @DATE Mar 23, 2017
*/
public class TestRenewalBlackoutWindowCancellations extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser;
	public BCAccountMenu acctMenu;
	Underwriters uw;
	GeneratePolicy myPolicyObj;
	Date cancellationDate = null;
	public boolean cancelBeforeInvoiceBilled;
	public boolean cancellationForFutureTermOnly;
	
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


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Blckout", "Wndw-Cncl")
				.withPolTermLengthDays(50)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void makeInsuredDownPayment() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "makeInsuredDownPayment" })
	public void manuallyRenewPolicy() throws Exception{
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal, UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        StartRenewal renewalWorkflow = new StartRenewal(driver);
		renewalWorkflow.renewPolicyManually(RenewalCode.Renew_Good_Risk, myPolicyObj);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "manuallyRenewPolicy" })
    public void determineWhichScenarioToRun() {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		
		switch (dayOfMonth) {
			case 1: case 5: case 9: case 13: case 17: case 21: case 25:
				this.cancelBeforeInvoiceBilled = true;
				this.cancellationForFutureTermOnly = true;
				break;
			case 2: case 6: case 10: case 14: case 18: case 22: case 26:
				this.cancelBeforeInvoiceBilled = true;
				this.cancellationForFutureTermOnly = false;
				break;
			case 3: case 7: case 11: case 15: case 19: case 23: case 27:
				this.cancelBeforeInvoiceBilled = false;
				this.cancellationForFutureTermOnly = true;
				break;
			case 4: case 8: case 12: case 16: case 20: case 24: case 28:
				this.cancelBeforeInvoiceBilled = false;
				this.cancellationForFutureTermOnly = false;
				break;
			default:
				this.cancelBeforeInvoiceBilled = getRandomBoolean();
				this.cancellationForFutureTermOnly = getRandomBoolean();
				break;
		}
		
		getQALogger().info("Value chosen for the 'cancelBeforeInvoiceBilled' variable was " + this.cancelBeforeInvoiceBilled);
		getQALogger().info("Value chosen for the 'cancellationForFutureTermOnly' variable was " + this.cancellationForFutureTermOnly);
	}
	
	@Test(dependsOnMethods = { "determineWhichScenarioToRun" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 29);		
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
    public void performPolicyCancellationAndVerifyInvoices() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		if (this.cancelBeforeInvoiceBilled) {
			cancelPolicy();
		} else {
			new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
            cancelPolicy();
		}

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Cancellation);
		
		double policyCancelChargeAmount = accountCharges.getChargeAmount(this.myPolicyObj.accountNumber, "Policy Cancellation");

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices accountInvoices = new AccountInvoices(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		
		if (this.cancelBeforeInvoiceBilled) {
			if (this.cancellationForFutureTermOnly) {
				//ATTENTION: This is here in the hopes that it will start failing when it's fixed, but the invoice item being checked here should not actually be here
				//It should be something else. However, I don't know what it should look like and won't know until membership dues are cancelled the way they should be.
                Date currentDatePlus1 = DateUtils.dateAddSubtract(this.myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, 1);
				try {
                    accountInvoices.getAccountInvoiceTableRow(this.myPolicyObj.squire.getExpirationDate(), this.myPolicyObj.squire.getExpirationDate(), null, InvoiceType.Scheduled, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, (Math.abs(policyCancelChargeAmount) + this.myPolicyObj.squire.getPremium().getMembershipDuesAmount()), this.myPolicyObj.squire.getPremium().getMembershipDuesAmount());
				} catch (Exception e) {
					Assert.fail("The rolled-up invoices for cancelling the future term, billed in a shortage, was not found. Test failed. ATTENTION: This may indicate that this test is now failing at a point that needs re-visited. Check to see if membership dues are being reversed as part of the cancel. Check with Brett Hiltbrand if you have questions.");
				}
				try {
                    accountInvoices.clickAccountInvoiceTableRow(currentDatePlus1, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the cancellation either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, currentDatePlus1, this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Cancellation, null, null, null, policyCancelChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyCancelChargeAmount) + " with an event date of " + currentDate + " was not found in the invoice item where it should be. Test failed.");
				}
			} else {
				//ATTENTION: This is here in the hopes that it will start failing when it's fixed, but the invoice item being checked here should not actually be here
				//It should be something else. However, I don't know what it should look like and won't know until membership dues are cancelled the way they should be.
				try {
                    accountInvoices.getAccountInvoiceTableRow(this.myPolicyObj.squire.getExpirationDate(), this.myPolicyObj.squire.getExpirationDate(), null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, this.myPolicyObj.squire.getPremium().getMembershipDuesAmount(), null);
				} catch (Exception e) {
					Assert.fail("The rolled-up invoices for cancelling the future term, billed in a shortage, was not found. Test failed. ATTENTION: This may indicate that this test is now failing at a point that needs re-visited. Check to see if membership dues are being reversed as part of the cancel. Check with Brett Hiltbrand if you have questions.");
				}
				try {
                    accountInvoices.clickAccountInvoiceTableRow(currentDate, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the cancellation either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, currentDate, this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Cancellation, null, null, null, policyCancelChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyCancelChargeAmount) + " with an event date of " + currentDate + " was not found in the invoice item where it should be. Test failed.");
				}
			}
		} else {
			if (this.cancellationForFutureTermOnly) {
				double renewalInvoiceAmount = 0.00;
				double renewalInvoiceDueAmount = 0.00;
				double invoiceRollupAmount = 0.00;
				try {
					TableUtils tableUtils = new TableUtils(driver);
                    renewalInvoiceAmount = accountInvoices.getInvoiceDueAmountByRowNumber(tableUtils.getRowNumberFromWebElementRow(accountInvoices.getAccountInvoiceTableRow(currentDate, null, null, InvoiceType.RenewalDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Billed, null, null)));
                    renewalInvoiceDueAmount = accountInvoices.getInvoiceAmountByRowNumber(tableUtils.getRowNumberFromWebElementRow(accountInvoices.getAccountInvoiceTableRow(currentDate, null, null, InvoiceType.RenewalDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Billed, null, null)));
                    invoiceRollupAmount = accountInvoices.getInvoiceDueAmountByRowNumber(tableUtils.getRowNumberFromWebElementRow(accountInvoices.getAccountInvoiceTableRow(this.myPolicyObj.squire.getExpirationDate(), null, null, InvoiceType.Scheduled, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null)));
                    accountInvoices.clickAccountInvoiceTableRow(DateUtils.dateAddSubtract(this.myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, 1), null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice items that should exist from the cancellation either did not exist, or were not in their expected state. Test failed.");
				}
				if (Math.abs(policyCancelChargeAmount) != (invoiceRollupAmount + renewalInvoiceAmount - renewalInvoiceDueAmount)) {
					Assert.fail("The shortage invoice for the cancellation did not roll up to cancel all charges for the future term. Test Failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, DateUtils.dateAddSubtract(this.myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, 1), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Cancellation, null, null, null, policyCancelChargeAmount, null, null, null)) {
                    Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyCancelChargeAmount) + " with an event date of " + this.myPolicyObj.squire.getExpirationDate() + " was not found in the invoice item where it should be. Test failed.");
				}
 			} else {
 				try {
                    accountInvoices.clickAccountInvoiceTableRow(currentDate, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the cancellation either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, currentDate, this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Cancellation, null, null, null, policyCancelChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyCancelChargeAmount) + " with an event date of " + currentDate + " was not found in the invoice item where it should be. Test failed.");
				}
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	
	private boolean getRandomBoolean() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}

    private void cancelPolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	WebDriver policyCancellationDriver = driver = buildDriver(cf);
		try {
			this.uw = UnderwritersHelper.getRandomUnderwriter();
			new Login(policyCancellationDriver).login(this.uw.getUnderwriterUserName(), this.uw.getUnderwriterPassword());
            SearchPoliciesPC policySearch = new SearchPoliciesPC(policyCancellationDriver);
			if (this.cancellationForFutureTermOnly) {
                policySearch.searchPolicyByPolicyNumberAndTerm(this.myPolicyObj.squire.getPolicyNumber(), PolicyTermStatus.Scheduled);
			} else {
                policySearch.searchPolicyByPolicyNumberAndTerm(this.myPolicyObj.squire.getPolicyNumber(), PolicyTermStatus.InForce);
			}
            StartCancellation policyCancellation = new StartCancellation(policyCancellationDriver);
			if (this.cancellationForFutureTermOnly) {
                policyCancellation.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "Policy Cancellation", DateUtils.dateAddSubtract(this.myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, 1), true);
			} else {
				policyCancellation.cancelPolicy(CancellationSourceReasonExplanation.PoorLossHistory, "Policy Cancellation", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);
			}
			
			new GuidewireHelpers(policyCancellationDriver).logout();
		
		} catch (Exception e) {
			getQALogger().error("There was an exception caught while Running batch processes. Attempting to log out gracefully...");
			getQALogger().error(e.getMessage());
			
			try {
				new GuidewireHelpers(policyCancellationDriver).logout();
			} catch (Exception e2) {
				getQALogger().error("There was an exception caught while Running the policy change, and the attempt to recover gracefully failed. Rolling back the driver now...");
				getQALogger().error(e2.getMessage());
			}
		}
	}		
}