package regression.r2.clock.billingcenter.troubletickets;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.payments.AccountPayments;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchPolicies;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentReturnedPaymentReason;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PersonalPropertyDeductible;
import repository.gw.enums.PersonalPropertyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TroubleTicketRelatedEntitiesOptions;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PersonalProperty;
import repository.gw.generate.custom.PersonalPropertyList;
import repository.gw.generate.custom.PersonalPropertyScheduledItem;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
* @Author bhiltbrand
* @Requirement When Payments are reversed on overridden policies, the reversal should apply to all policy invoice charge items.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/101497613544">Rally Defect DE5193</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/101496975776">Rally Defect DE5192</a>
* @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/101499504840">Rally Defect DE5195</a>
* @Description This test verifies that payments on multiple policies are reversed as expected and that trouble tickets are working as expected.
* @DATE May 1, 2017
*/
public class TestMultiPolicyTroubleTickets extends BaseTest {
	private WebDriver driver;
	private Agents agent;
	private GeneratePolicy standardFirePolicy;
	private GeneratePolicy standardLiabilityPolicy;
	private GeneratePolicy standardInlandMarinePolicy;
	private ARUsers arUser;
	private double combinedDownPaymentAmount = 0.00;

	@Test ()
	public void generateStandardFirePolicy() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises));
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(5);
		locationsList.add(propLoc);

        this.standardFirePolicy = new GeneratePolicy.Builder(driver)
				.withAgent(this.agent)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("MultiPolicy", "Test")
				.withInsAge(26)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"generateStandardFirePolicy"})
	public void generateStandardLiabilityPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		SquireLiability liabilityLimit = new SquireLiability();
		liabilityLimit.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);
		
		ArrayList<LineSelection> productLines = new ArrayList<LineSelection>();
		productLines.add(LineSelection.StandardLiabilityPL);

        StandardFireAndLiability myFireAndLiability = new StandardFireAndLiability();
        myFireAndLiability.liabilitySection = liabilityLimit;

        this.standardLiabilityPolicy = new GeneratePolicy.Builder(driver)
				.withAgent(this.agent)
				.withCreateNew(CreateNew.Do_Not_Create_New)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardFire(myFireAndLiability)
                .withPolicyLocations(this.standardFirePolicy.standardFire.getLocationList())
				.withContact(standardFirePolicy.pniContact)
				.withPolOrgType(this.standardFirePolicy.polOrgType)
				.withPaymentPlanType(this.standardFirePolicy.paymentPlanType)
				.withDownPaymentType(this.standardFirePolicy.downPaymentType)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"generateStandardLiabilityPolicy"})
	public void generateStandardInlandMarinePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<InlandMarine> inlandMarineCoverageSelection_PL_IM = new ArrayList<InlandMarine>();
		inlandMarineCoverageSelection_PL_IM.add(InlandMarine.PersonalProperty);
		
		// PersonalProperty
		PersonalProperty ppropTool = new PersonalProperty();
		ppropTool.setType(PersonalPropertyType.Tools);
		ppropTool.setDeductible(PersonalPropertyDeductible.Ded25);
		PersonalPropertyScheduledItem tool = new PersonalPropertyScheduledItem();
		tool.setDescription("Testing Tool");
		tool.setLimit(5000);
		
		ArrayList<PersonalPropertyScheduledItem> tools = new ArrayList<PersonalPropertyScheduledItem>();
		tools.add(tool);

		PersonalPropertyList ppropList = new PersonalPropertyList();
		ppropList.setTools(ppropTool);
		ppropTool.setScheduledItems(tools);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = inlandMarineCoverageSelection_PL_IM;
        myStandardInlandMarine.personalProperty_PL_IM = ppropList.getAllPersonalPropertyAsList();

        this.standardInlandMarinePolicy = new GeneratePolicy.Builder(driver)
				.withAgent(this.agent)
				.withCreateNew(CreateNew.Do_Not_Create_New)
				.withProductType(ProductLineType.StandardIM)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withLineSelection(LineSelection.StandardInlandMarine)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName(this.standardFirePolicy.pniContact.getFirstName(), this.standardFirePolicy.pniContact.getLastName())
				.withPaymentPlanType(this.standardFirePolicy.paymentPlanType)
				.withDownPaymentType(this.standardFirePolicy.downPaymentType)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = {"generateStandardInlandMarinePolicy"})
	public void overrideInvoiceStreams() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.standardLiabilityPolicy.standardLiability.getPolicyNumber());
		
		//Close Trouble ticket for Standard Liability Policy and override invoice stream to the Standard Fire Policy.
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets policyTroubleTickets = new BCCommonTroubleTickets(driver);
		policyTroubleTickets.closeFirstTroubleTicket();

        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuSummary();

        BCPolicySummary policySummary = new BCPolicySummary(driver);
        policySummary.overrideInvoiceStream(this.standardFirePolicy.standardFire.getPolicyNumber());
		
		//Close Trouble ticket for Standard Inland Marine Policy and override invoice stream to the Standard Fire Policy.
        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        policySearch.searchPolicyByPolicyNumber(this.standardInlandMarinePolicy.standardInlandMarine.getPolicyNumber());

        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        policyTroubleTickets = new BCCommonTroubleTickets(driver);
		policyTroubleTickets.closeFirstTroubleTicket();

        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuSummary();

        policySummary = new BCPolicySummary(driver);
        policySummary.overrideInvoiceStream(this.standardFirePolicy.standardFire.getPolicyNumber());
		
		//Add Standard Liability and Standard Inland Marine Policies to the Standard Fire Policy Trouble Ticket.
        policySearch = new BCSearchPolicies(driver);
        policySearch.searchPolicyByPolicyNumber(this.standardFirePolicy.standardFire.getPolicyNumber());

        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        policyTroubleTickets = new BCCommonTroubleTickets(driver);
		policyTroubleTickets.clickTroubleTicketNumberInTable(new TableUtils(driver).getRowCount(policyTroubleTickets.getTroubleTicketsTable()));
        policyTroubleTickets.addTroubleTicketRelatedEntities(TroubleTicketRelatedEntitiesOptions.Policies, this.standardLiabilityPolicy.standardLiability.getPolicyNumber());
        policyTroubleTickets.addTroubleTicketRelatedEntities(TroubleTicketRelatedEntitiesOptions.Policies, this.standardInlandMarinePolicy.standardInlandMarine.getPolicyNumber());
        new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = {"overrideInvoiceStreams"})
	public void payDownPaymentAndMoveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		
		BCTopMenu topMenu = new BCTopMenu(driver);
		topMenu.clickDesktopTab();

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuActionsMultiplePayment();

        DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
        this.combinedDownPaymentAmount = this.standardFirePolicy.standardFire.getPremium().getDownPaymentAmount() + this.standardInlandMarinePolicy.standardInlandMarine.getPremium().getDownPaymentAmount() + this.standardLiabilityPolicy.standardLiability.getPremium().getDownPaymentAmount();
        multiPayment.makeMultiplePayment(this.standardFirePolicy.standardFire.getPolicyNumber(), PaymentInstrumentEnum.Check, this.combinedDownPaymentAmount);
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 6);
		
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = {"payDownPaymentAndMoveClocks"})
	public void reversePaymentAndCheckInvoices() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.standardFirePolicy.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices accountInvoices = new AccountInvoices(driver);
		try {
            accountInvoices.clickAccountInvoiceTableRow(this.standardFirePolicy.standardFire.getEffectiveDate(), this.standardFirePolicy.standardFire.getEffectiveDate(), null, InvoiceType.NewBusinessDownPayment, this.standardFirePolicy.standardFire.getPolicyNumber(), InvoiceStatus.Due, this.combinedDownPaymentAmount, 0.00);
		} catch (Exception e) {
			Assert.fail("The Invoice for the New Business Down in the amount of the combined downpayment for all three policies was not found. Test Failed.");
		}
        List<WebElement> invoiceChargesTableRows = new TableUtils(driver).getAllTableRows(accountInvoices.getAccountInvoiceChargesTable());
		for (WebElement row : invoiceChargesTableRows) {
			if (NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(accountInvoices.getAccountInvoiceChargesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(row), "Paid Amount")) == 0.00) {
				Assert.fail("The Payment made from the Multiple Payment screen did not pay all invoice charges.");
			}
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuPayments();

        AccountPayments accountPayments = new AccountPayments(driver);
		accountPayments.reversePaymentAtFault(null,this.combinedDownPaymentAmount, this.combinedDownPaymentAmount, null, PaymentReturnedPaymentReason.InsufficientFunds);

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        accountInvoices = new AccountInvoices(driver);
		try {
            accountInvoices.clickAccountInvoiceTableRow(this.standardFirePolicy.standardFire.getEffectiveDate(), this.standardFirePolicy.standardFire.getEffectiveDate(), null, InvoiceType.NewBusinessDownPayment, this.standardFirePolicy.standardFire.getPolicyNumber(), InvoiceStatus.Due, this.combinedDownPaymentAmount, this.combinedDownPaymentAmount);
		} catch (Exception e) {
			Assert.fail("The Invoice for the New Business Down in the amount of the combined downpayment, for all three policies, with the full amount due again, was not found. Test Failed.");
		}
        invoiceChargesTableRows = new TableUtils(driver).getAllTableRows(accountInvoices.getAccountInvoiceChargesTable());
		for (WebElement row : invoiceChargesTableRows) {
			if (NumberUtils.getCurrencyValueFromElement(new TableUtils(driver).getCellTextInTableByRowAndColumnName(accountInvoices.getAccountInvoiceChargesTable(), new TableUtils(driver).getRowNumberFromWebElementRow(row), "Paid Amount")) != 0.00) {
				Assert.fail("The Payment was reversed, but it did not apply to all invoice charges as required. There is still money left applied. Test Failed.");
			}
		}

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuSummary();

        BCAccountSummary accountSummary = new BCAccountSummary(driver);
        if (accountSummary.getUnappliedFundByPolicyNumber(this.standardFirePolicy.standardFire.getPolicyNumber()) < 0.00) {
			Assert.fail("The Unapplied Funds bucket for the lead policy was negative, but should not be. Test Failed.");
		}
		new GuidewireHelpers(driver).logout();
	}
}