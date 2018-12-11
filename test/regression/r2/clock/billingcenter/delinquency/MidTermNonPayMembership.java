package regression.r2.clock.billingcenter.delinquency;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDelinquencies;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TransactionType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
* @Author bhiltbrand
* @Requirement This test verifies that not paying an invoice containing just membership dues triggers a regular past due cancel, not a non-premium cancel. 
* @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/userstory/191402063312">Rally User Story US13923</a>
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-09%20Delinquency%20on%20Non-Premium%20Charges.docx">Non-Premium Delinquency Requirements</a>
* @Description 
* @DATE Mar 12, 2018
*/
public class MidTermNonPayMembership extends BaseTest {
	private GeneratePolicy myPolicyObj = null;
	private Agents agent;
	private ARUsers arUser;
	private Date invoiceDueDate;
	private WebDriver driver;

	@Test
	public void generatePolicy() throws GuidewireException, Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
				
		this.agent = AgentsHelper.getRandomAgent();
		
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

		this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withAgent(this.agent)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Mid-Term", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "generatePolicy" })
	public void payPolicyInFull() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);

		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilIssuanceChargesArrive();
		
		NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(this.myPolicyObj, this.myPolicyObj.squire.getPremium().getDownPaymentAmount(), this.myPolicyObj.squire.getPolicyNumber());
        
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 1);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
        new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "payPolicyInFull" })
	public void moveClocks() throws Exception {
		ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Month, 3);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
	public void addAnotherSetOfMembershipDuesToThePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		//loginToProductAndSearchPolicyByPolicyNumber(ApplicationOrCenter.PolicyCenter, this.agent.getAgentUserName(), this.agent.getAgentPassword(), this.myPolicyObj.squire.getPolicyNumber());
		new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());
		
		ArrayList<Contact> membershipMembersToAdd = new ArrayList<Contact>();
		Contact newMembershipMember = new Contact();
		newMembershipMember.setHasMembershipDues(true);
		membershipMembersToAdd.add(newMembershipMember);
		
		StartPolicyChange policyChange = new StartPolicyChange(driver);		
		this.myPolicyObj = policyChange.addMembershipDues(this.myPolicyObj, membershipMembersToAdd);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "addAnotherSetOfMembershipDuesToThePolicy" })
	public void dontPayShortageInvoice() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(TransactionType.Policy_Change);
		
		accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();
		
		AccountInvoices invoices = new AccountInvoices(driver);
		Date invoiceDate = invoices.getInvoiceDateByInvoiceType(InvoiceType.Shortage);
		this.invoiceDueDate = invoices.getInvoiceDueDateByInvoiceType(InvoiceType.Shortage);
		
		ClockUtils.setCurrentDates(driver, invoiceDate);
		
		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "dontPayShortageInvoice" })
	public void moveClocksAgain() throws Exception {
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(this.invoiceDueDate, DateAddSubtractOptions.Day, 1));
	}
	
	@Test(dependsOnMethods = { "moveClocksAgain" })
	public void verifyDelinquencyType() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

		new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuDelinquencies();
		
		BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
		Date currentBCDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter);
		boolean delinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, this.myPolicyObj.accountNumber, null, currentBCDate);

		if (!delinquencyFound) {
			Assert.fail("The Delinquency was either non-existant or not in an 'open' status.");
		} else {
			if (delinquency.getDelinquentAmount(OpenClosed.Open, this.myPolicyObj.accountNumber, this.myPolicyObj.squire.getPolicyNumber(), currentBCDate) != this.myPolicyObj.squire.getPremium().getChangeInCost()) {
				Assert.fail("The Delinquency Amount was not equal to the change in cost for the policy change. Test Failed");
			}
			String delinquencyReason = delinquency.getDelinquencyReason();
			if (!delinquencyReason.equals("Past Due Full Cancel")) {
				Assert.fail("The Delinquency Reason was not a Past Due Full Cancel as expected. It was a " + delinquencyReason + " reason. Test Failed");
			}
		}
		
		new GuidewireHelpers(driver).logout();
	}
}
