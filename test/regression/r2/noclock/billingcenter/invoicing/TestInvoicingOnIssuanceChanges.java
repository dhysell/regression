package regression.r2.noclock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.State;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquirePACoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
* @Author bhiltbrand
* @Requirement This test is to ensure that changes made to a policy during issuance revolving around payer assignment and decrease in premium are handled as expected in BC.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/137219512852">Raly Defect DE6155</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/137220872920">Raly Defect DE6156</a>
* @Description 
* @DATE Jul 27, 2017
*/
public class TestInvoicingOnIssuanceChanges extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null; 
	private ARUsers arUser = new ARUsers();
	private List<String> errorList = new ArrayList<String>();
	private int paymentPlanPeriods = 0;
	private double policyChangeChargePerInvoicePeriod = 0;
	private double policyChangeChargeForFirstInvoicePeriod = 0;
	private Date currentDate;
	
	@Test
	public void generate() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle vehicleOne = new Vehicle();
		vehicleList.add(vehicleOne);
		
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K, MedicalLimit.TenK,true,
				UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		
		AddressInfo bankAddress = new AddressInfo();
		bankAddress.setLine1("PO Box 1551");
		bankAddress.setCity("Springfield");
		bankAddress.setState(State.Ohio);
		bankAddress.setZip("45501-1551");
		AdditionalInterest propertyAdditionalInterest = new AdditionalInterest("US Bank", bankAddress);
		propertyAdditionalInterest.setAddress(bankAddress);
		propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
		propertyCoverages.getCoverageA().setLimit(500000);
		propertyCoverages.getCoverageC().setAdditionalValue(300000);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOneProperty.setPropertyCoverages(propertyCoverages);
		locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myProperty = new SquirePropertyAndLiability();
        myProperty.locationList = locationsList;
        myProperty.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myProperty;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Issuance", "Invoice")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Monthly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicySubmitted);
	}
	
	@Test(dependsOnMethods = { "generate" })
	public void changeLHLocationAndLiabilityOnIssuance() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.myPolicyObj.underwriterInfo.getUnderwriterUserName(), this.myPolicyObj.underwriterInfo.getUnderwriterPassword());
		SearchAccountsPC accountSearch = new SearchAccountsPC(driver);
		accountSearch.searchAccountByAccountNumber(this.myPolicyObj.accountNumber);
		AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		try {
			accountSummaryPage.clickActivitySubject("Submitted Full Application");
		} catch (Exception e) {
			accountSummaryPage.clickActivitySubject("Review and approve submission");
		}
		ActivityPopup activityPopupPage = new ActivityPopup(driver);
		try {
			activityPopupPage.setUWIssuanceActivity();				
		} catch(Exception e) {	
			activityPopupPage.clickCloseWorkSheet();
		}

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.clickAdditionalInterestByName("US Bank");
		GenericWorkorderAdditionalInterests additionalInterestPage = new GenericWorkorderAdditionalInterests(driver);
		//change LH address to location 10 (from location 21). New address is PO Box 5760 Springfield, OH 45501-5760
		additionalInterestPage.changePropertyAdditionalInterestAddressListing("PO Box 5760");
		additionalInterestPage.clickBuildingsPropertyAdditionalInterestsUpdateButton();
		propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickOk();
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquirePACoverages vehicleCoveragesPage = new GenericWorkorderSquirePACoverages(driver);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL100K, MedicalLimit.TenK,true,	UninsuredLimit.CSL100K, false, UnderinsuredLimit.CSL100K);
		myPolicyObj.squire.squirePA.setCoverages(coverages);
		vehicleCoveragesPage.fillOutSquireAutoCoverages(myPolicyObj);

        GenericWorkorder genericWorkorder = new GenericWorkorder(driver);
		genericWorkorder.clickGenericWorkorderQuote();
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQuote();
		GenericWorkorderQuote quoteScreen = new GenericWorkorderQuote(driver);
        myPolicyObj.squire.getPremium().setChangeInCost(quoteScreen.getQuoteChangeInCost());
		genericWorkorder.clickGenericWorkorderIssue(IssuanceType.NoActionRequired);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "changeLHLocationAndLiabilityOnIssuance" })
	public void checkInvoicingAfterIssuance() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical_Advanced, ARCompany.Personal);	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices accountInvoices = new AccountInvoices(driver);
		this.paymentPlanPeriods = accountInvoices.getInvoiceTableRowCountOfPlannedPositiveInvoices();
        this.policyChangeChargePerInvoicePeriod = NumberUtils.round((this.myPolicyObj.squire.getPremium().getChangeInCost() / this.paymentPlanPeriods), 2);
        this.policyChangeChargeForFirstInvoicePeriod = NumberUtils.round(this.myPolicyObj.squire.getPremium().getChangeInCost() - (this.policyChangeChargePerInvoicePeriod * (this.paymentPlanPeriods - 1)), 2);
		this.currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        accountInvoices.clickAccountInvoiceTableRow(currentDate, currentDate, null, InvoiceType.NewBusinessDownPayment, this.myPolicyObj.squire.getPolicyNumber(), InvoiceStatus.Billed, null, null);
		if (accountInvoices.verifyInvoiceCharges(null, currentDate, this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, "Installment", null, this.myPolicyObj.squire.getPremium().getChangeInCost(), this.myPolicyObj.squire.getPremium().getChangeInCost(), null, null)) {
			errorList.add("The Invoice Charges table for the New Business Down Payment contained the full amount of the decrease in coverage from the Issuance job. This should have been spread evenly over all remaining invoices.");
        } else if (!accountInvoices.verifyInvoiceCharges(null, currentDate, this.myPolicyObj.squire.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, "Installment", null, this.policyChangeChargeForFirstInvoicePeriod, this.policyChangeChargeForFirstInvoicePeriod, null, null)) {
			errorList.add("The Invoice Charges table for the New Business Down Payment did not contain the correct amount of the decrease in coverage from the Issuance job, spread evenly across all remaining invoices.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "checkInvoicingAfterIssuance" })
	public void changePayerAssignmentBackToInsured() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.resetPayerAssignmentToInsured();
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "changePayerAssignmentBackToInsured" })
	public void checkInvoicingAfterChange() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Clerical_Advanced, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

		AccountCharges accountCharges = new AccountCharges(driver);
		double payerAssignmentChangeAmount = accountCharges.getChargeAmount(this.myPolicyObj.accountNumber, "Change Payer Assignment");

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices accountInvoices = new AccountInvoices(driver);
        Date dueDateForSecondInvoice = DateUtils.dateAddSubtract(this.myPolicyObj.squire.getEffectiveDate(), DateAddSubtractOptions.Month, 1);
		Date invoiceDateForSecondInvoice = DateUtils.dateAddSubtract(dueDateForSecondInvoice, DateAddSubtractOptions.Day, -15);
		//Click the second row on the invoice table to inspect that for invoicing roll-up.
        accountInvoices.clickAccountInvoiceTableRow(invoiceDateForSecondInvoice, dueDateForSecondInvoice, null, InvoiceType.Scheduled, this.myPolicyObj.busOwnLine.getPolicyNumber(), InvoiceStatus.Planned, null, null);
		double payerAssignmentChangeChargePerInvoicePeriod = NumberUtils.round((payerAssignmentChangeAmount / this.paymentPlanPeriods), 2);
		double payerAssignmentChangeChargeForFirstInvoicePeriod = NumberUtils.round(payerAssignmentChangeAmount - (payerAssignmentChangeChargePerInvoicePeriod * (this.paymentPlanPeriods - 1)), 2);
        if (accountInvoices.verifyInvoiceCharges(null, currentDate, this.myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, "Installment", null, payerAssignmentChangeAmount, 0.00, null, null)) {
			errorList.add("The Invoice Charges table for the Second Scheduled Invoice contained the full amount of the change in payer assignment back to the insured. This should have been spread evenly over all remaining invoices.");
        } else if (!accountInvoices.verifyInvoiceCharges(null, currentDate, this.myPolicyObj.busOwnLine.getPolicyNumber(), ChargeCategory.Premium, TransactionType.Policy_Change, null, "Installment", null, payerAssignmentChangeChargeForFirstInvoicePeriod, 0.00, null, null)) {
			errorList.add("The Invoice Charges table for the Second Scheduled Invoice did not contain the correct amount from the change, spread evenly across all remaining invoices.");
		}
		new GuidewireHelpers(driver).logout();
		
		if (!this.errorList.isEmpty()) {
			for (String error : this.errorList) {
				getQALogger().error(error);
			}
			Assert.fail("This invoicing test failed. Please see the errors collected above.");
		} else {
			getQALogger().info("Test Passed.");
		}
	}
}
