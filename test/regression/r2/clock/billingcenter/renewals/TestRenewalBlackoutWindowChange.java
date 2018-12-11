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
import repository.gw.enums.ChargeCategory;
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
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.change.StartPolicyChange;
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
* @Requirement This test is designed to test the blackout window for policy changes during renewal. It creates a policy and renews it, 
* then randomly decides between one of 8 different scenarios to create a change and verify the resultant invoicing.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/81080857012">Rally Story US10044</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-04%20Regular%20Renewal%20Installment%20Scheduling.docx">Regular Renewal Installment Scheduling Requirements</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-03%20New%20Renewal%20Installment%20Scheduling.docx">Transition Renewal Installment Scheduling Requirements</a>
* @Description 
* @DATE Mar 23, 2017
*/
public class TestRenewalBlackoutWindowChange extends BaseTest {
	private WebDriver driver;
	public ARUsers arUser;
	public BCAccountMenu acctMenu;
	Underwriters uw;
	GeneratePolicy myPolicyObj;
	Date cancellationDate = null;
	public boolean policyChangeBeforeInvoiceBilled;
	public boolean policyChangeAppliedToCurrentTerm;
	public boolean positivePolicyChange;
	public boolean specialCase;
	
	@Test
    public void determineWhichScenarioToRun() {
		Calendar cal = Calendar.getInstance();
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		
		switch (dayOfMonth) {
			case 1: case 10: case 19:
				this.policyChangeBeforeInvoiceBilled = true;
				this.policyChangeAppliedToCurrentTerm = true;
				this.positivePolicyChange = true;
				this.specialCase = false;
				break;
			case 2: case 11: case 20:
				this.policyChangeBeforeInvoiceBilled = true;
				this.policyChangeAppliedToCurrentTerm = true;
				this.positivePolicyChange = false;
				this.specialCase = false;
				break;
			case 3: case 12: case 21:
				this.policyChangeBeforeInvoiceBilled = true;
				this.policyChangeAppliedToCurrentTerm = false;
				this.positivePolicyChange = true;
				this.specialCase = false;
				break;
			case 4: case 13: case 22:
				this.policyChangeBeforeInvoiceBilled = true;
				this.policyChangeAppliedToCurrentTerm = false;
				this.positivePolicyChange = false;
				this.specialCase = false;
				break;
			case 5: case 14: case 23:
				this.policyChangeBeforeInvoiceBilled = false;
				this.policyChangeAppliedToCurrentTerm = true;
				this.positivePolicyChange = true;
				this.specialCase = false;
				break;
			case 6: case 15: case 24:
				this.policyChangeBeforeInvoiceBilled = false;
				this.policyChangeAppliedToCurrentTerm = true;
				this.positivePolicyChange = false;
				this.specialCase = false;
				break;
			case 7: case 16: case 25:
				this.policyChangeBeforeInvoiceBilled = false;
				this.policyChangeAppliedToCurrentTerm = false;
				this.positivePolicyChange = true;
				this.specialCase = false;
				break;
			case 8: case 17: case 26:
				this.policyChangeBeforeInvoiceBilled = false;
				this.policyChangeAppliedToCurrentTerm = false;
				this.positivePolicyChange = false;
				this.specialCase = false;
				break;
			case 9: case 18: case 27:
				this.policyChangeBeforeInvoiceBilled = false;
				this.policyChangeAppliedToCurrentTerm = true;
				this.positivePolicyChange = false;
				this.specialCase = true;
				break;
			default:
				this.specialCase = getRandomBoolean();
				if (!this.specialCase) {
					this.policyChangeBeforeInvoiceBilled = getRandomBoolean();
					this.policyChangeAppliedToCurrentTerm = getRandomBoolean();
					this.positivePolicyChange = getRandomBoolean();
				} else {
					this.policyChangeBeforeInvoiceBilled = false;
					this.policyChangeAppliedToCurrentTerm = true;
					this.positivePolicyChange = false;
				}
				
				break;
		}
		
		getQALogger().info("Value chosen for the 'policyChangeBeforeInvoiceBilled' variable was " + this.policyChangeBeforeInvoiceBilled);
		getQALogger().info("Value chosen for the 'policyChangeAppliedToCurrentTerm' variable was " + this.policyChangeAppliedToCurrentTerm);
		getQALogger().info("Value chosen for the 'positivePolicyChange' variable was " + this.positivePolicyChange);
		getQALogger().info("Value chosen for the 'specialCase' variable was " + this.specialCase);
	}
	
	@Test (dependsOnMethods = { "determineWhichScenarioToRun" })
    public void generatePolicy() throws Exception {
		//Need to generate a huge policy if special case is chosen. I need to have enough premium to generate a credit on the last day of the policy.
		
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
				.withInsFirstLastName("Blckout", "Wndw-Chnge")
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
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		if (this.specialCase) {
            ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(this.myPolicyObj.squire.getExpirationDate(), DateAddSubtractOptions.Day, -1));
		} else {
			ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 29);
		}		
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
    public void performPolicyChangeAndVerifyInvoices() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		if (this.policyChangeBeforeInvoiceBilled) {
			createPolicyChange();
		} else {
			new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
			if (this.specialCase) {
				new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
			}
			createPolicyChange();
		}

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(120, TransactionType.Policy_Change);
		
		double policyChangeChargeAmount = accountCharges.getChargeAmount(this.myPolicyObj.accountNumber, "change coverage");

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices accountInvoices = new AccountInvoices(driver);
		int paymentPlanPeriods = accountInvoices.getInvoiceTableRowCountOfPlannedPositiveInvoices();
		double policyChangeChargeAmountPerInvoicePeriod = NumberUtils.round((policyChangeChargeAmount / paymentPlanPeriods), 2 );
		double policyChangeChargeAmountForFirstInvoicePeriod = NumberUtils.round(policyChangeChargeAmount - (policyChangeChargeAmountPerInvoicePeriod * (paymentPlanPeriods - 1)), 2);
		
		if (this.policyChangeBeforeInvoiceBilled) {
			if (this.policyChangeAppliedToCurrentTerm && this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(null, null, null, InvoiceType.RenewalDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmount) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			} else if (this.policyChangeAppliedToCurrentTerm && !this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(null, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmount) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			} else if (!this.policyChangeAppliedToCurrentTerm && this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(null, null, null, InvoiceType.RenewalDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, this.myPolicyObj.squire.getExpirationDate(), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmountForFirstInvoicePeriod, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmountForFirstInvoicePeriod) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			} else if (!this.policyChangeAppliedToCurrentTerm && !this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(null, null, null, InvoiceType.RenewalDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, this.myPolicyObj.squire.getExpirationDate(), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmountForFirstInvoicePeriod, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmountForFirstInvoicePeriod) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			}
		} else {
			Date nextBillableInvoice = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Month, 1), DateAddSubtractOptions.Day, 1);
			if (this.policyChangeAppliedToCurrentTerm && this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(nextBillableInvoice, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmount) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			} else if (this.policyChangeAppliedToCurrentTerm && !this.positivePolicyChange) {
				Date currentBCCenterDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
				try {
                    accountInvoices.clickAccountInvoiceTableRow(currentBCCenterDate, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, currentBCCenterDate, this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmount, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmount) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
				if (this.specialCase) {
					new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
					driver.navigate().refresh();
                    try {
                        accountInvoices.clickAccountInvoiceTableRow(currentBCCenterDate, currentBCCenterDate, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Billed, policyChangeChargeAmount, 0.00);
					} catch (Exception e) {
						Assert.fail("The Shortage Invoice from the change credit either did not exist, or was not in its expected state. Test failed.");
					}
					double renewalDownBilledAmount = NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(accountInvoices.getAccountInvoicesTable(), new TableUtils(driver).getRowInTableByColumnNameAndValue(accountInvoices.getAccountInvoicesTable(), "Invoice Type", InvoiceType.RenewalDownPayment.getValue()), "Amount"));
					try {
                        accountInvoices.clickAccountInvoiceTableRow(currentBCCenterDate, DateUtils.dateAddSubtract(currentBCCenterDate, DateAddSubtractOptions.Day, 20), null, InvoiceType.RenewalDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Billed, renewalDownBilledAmount, (renewalDownBilledAmount - Math.abs(policyChangeChargeAmount)));
					} catch (Exception e) {
						Assert.fail("The Shortage Invoice from the change credit did not pay toward the renewal down as expected. Test failed.");
					}
				}
			} else if (!this.policyChangeAppliedToCurrentTerm && this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(nextBillableInvoice, null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, this.myPolicyObj.squire.getExpirationDate(), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmountForFirstInvoicePeriod, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmountForFirstInvoicePeriod) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			} else if (!this.policyChangeAppliedToCurrentTerm && !this.positivePolicyChange) {
				try {
                    accountInvoices.clickAccountInvoiceTableRow(this.myPolicyObj.squire.getExpirationDate(), null, null, InvoiceType.Shortage, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Planned, null, null);
				} catch (Exception e) {
					Assert.fail("The Invoice item that should exist from the change either did not exist, or was not in its expected state. Test failed.");
				}
                if (!accountInvoices.verifyInvoiceCharges(null, this.myPolicyObj.squire.getExpirationDate(), this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, null, null, policyChangeChargeAmountForFirstInvoicePeriod, null, null, null)) {
					Assert.fail("The invoice charge for " + StringsUtils.currencyRepresentationOfNumber(policyChangeChargeAmountForFirstInvoicePeriod) + " with an event date of " + DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter) + " was not found in the invoice item where it should be. Test failed.");
				}
			}
		}
		new GuidewireHelpers(driver).logout();
	}
	
	private boolean getRandomBoolean() {
		Random rnd = new Random();
		return rnd.nextBoolean();
	}

    private void createPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver policyChangeDriver = driver = buildDriver(cf);
		try {
			this.uw = UnderwritersHelper.getRandomUnderwriter();
			new Login(policyChangeDriver).login(this.uw.getUnderwriterUserName(), this.uw.getUnderwriterPassword());
            SearchPoliciesPC policySearch = new SearchPoliciesPC(driver);
			if (this.policyChangeAppliedToCurrentTerm) {
                policySearch.searchPolicyByPolicyNumberAndTerm(this.myPolicyObj.squire.getPolicyNumber(), PolicyTermStatus.InForce);
			} else {
                policySearch.searchPolicyByPolicyNumberAndTerm(this.myPolicyObj.squire.getPolicyNumber(), PolicyTermStatus.Scheduled);
			}
            StartPolicyChange policyChange = new StartPolicyChange(driver);
			PLPolicyLocationProperty property = this.myPolicyObj.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0);
			if (this.positivePolicyChange) {
				policyChange.changePLPropertyCoverage((Double.valueOf(property.getPropertyCoverages().getCoverageA().getLimit()) + 50000), DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
			} else {
				policyChange.changePLPropertyCoverage((Double.valueOf(property.getPropertyCoverages().getCoverageA().getLimit()) - 50000), DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
			}
			
			new GuidewireHelpers(policyChangeDriver).logout();
		
		} catch (Exception e) {
			getQALogger().error("There was an exception caught while Running batch processes. Attempting to log out gracefully...");
			getQALogger().error(e.getMessage());
			
			try {
				new GuidewireHelpers(policyChangeDriver).logout();
			} catch (Exception e2) {
				getQALogger().error("There was an exception caught while Running the policy change, and the attempt to recover gracefully failed. Rolling back the driver now...");
				getQALogger().error(e2.getMessage());
			}
		}
	}
		
		
		/*// Moving clock for cancellation 
		ClockUtils.setCurrentDates(DateAddSubtractOptions.Day, 6);
		Configuration.setProduct(ApplicationOrCenter.BillingCenter);
		
		loginToProductAndSearchPolicyByPolicyNumber(ApplicationOrCenter.PolicyCenter, myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.policyNumber);
		StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "Cancelling Policy", null, true);
		
		cancellationDate = DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter);
	}

	@Test (dependsOnMethods = { "moveClockAndCancelPolicy" })
	public void verifyInvoices() throws Exception {
		//verify invoices on billing center		
		loginToProductAndSearchAccountByAccountNumber(ApplicationOrCenter.BillingCenter, arUser.getUserName(),arUser.getPassword(),myPolicyObj.accountNumber);
		
		acctMenu=AccountFactory.getAccountMenuPage();
		acctMenu.clickAccountMenuInvoices();	
		AccountInvoices tableData= AccountFactory.getAccountInvoicesPage();
		WebElement myInvoiceTable= tableData.getAccountInvoicesTable();
		WebElement lastRowOfInvoiceTable = TableUtils.getRowInTableByColumnNameAndValue(myInvoiceTable, "Invoice Date", DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObj.expirationDate));
		if (lastRowOfInvoiceTable != null) {
			String invoiceType = TableUtils.getCellTextInTableByRowAndColumnName(myInvoiceTable,
					TableUtils.getRowNumberFromWebElementRow(lastRowOfInvoiceTable), "Invoice Type");
			String invoiceDate = TableUtils.getCellTextInTableByRowAndColumnName(myInvoiceTable,
					TableUtils.getRowNumberFromWebElementRow(lastRowOfInvoiceTable), "Invoice Date");
			System.out.println("InvoiceDate------->" + invoiceDate + "InvoiceType------->" + invoiceType);
			Assert.assertEquals(invoiceType, "Shortage", "Invoice Type  is Not equal to Shortage");
			Assert.assertEquals(invoiceDate, DateUtils.dateFormatAsString("MM/dd/yyyy", myPolicyObj.expirationDate),
					"Invoice Date is Not equal to Expiry Date");
		}
		else {
			Assert.fail("Row not found");
		}
		
	}*/
		
}