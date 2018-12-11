package regression.r2.clock.policycenter.cancellation;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.administration.AdminEventMessages;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MessageQueue;
import repository.gw.enums.MessageQueuesFilterSelectOptions;
import repository.gw.enums.OpenClosed;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
@QuarantineClass
public class TestPLMultiPolicyDelinquencyMessages extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj = null;
	private Date targetDate = null;
	private ARUsers arUser;

	// ///////////////////
	// Main Test Methods//
	// ///////////////////

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
        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Non-Pay", "Cancel")
				.withInsPrimaryAddress(locationsList.get(0).getAddress())
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generatePolicy" })
	public void addPolicyToAccount() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

        myPolicyObj.squireUmbrellaInfo = squireUmbrellaInfo;
        myPolicyObj.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "addPolicyToAccount" })
	public void verifyThatChargesExistInBC() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);

        AccountCharges accountCharges = new AccountCharges(driver);
        accountCharges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(currentDate, TransactionType.Policy_Issuance, this.myPolicyObj.squire.getPolicyNumber());
        accountCharges.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(currentDate, TransactionType.Policy_Issuance, this.myPolicyObj.squireUmbrellaInfo.getPolicyNumber());

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuSummary();

        BCAccountSummary accountSummary = new BCAccountSummary(driver);
        accountSummary.clickPolicyNumberInOpenPolicyStatusTable(this.myPolicyObj.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets policyTroubleTickets = new BCCommonTroubleTickets(driver);
		policyTroubleTickets.waitForTroubleTicketsToArrive(120);

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

        accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuSummary();

        accountSummary = new BCAccountSummary(driver);
        accountSummary.clickPolicyNumberInOpenPolicyStatusTable(this.myPolicyObj.squireUmbrellaInfo.getPolicyNumber());

        policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        policyTroubleTickets = new BCCommonTroubleTickets(driver);
		policyTroubleTickets.waitForTroubleTicketsToArrive(120);
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyThatChargesExistInBC" })
    public void moveClocksAndRunInvoice() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 10);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
	}

	@Test(dependsOnMethods = { "moveClocksAndRunInvoice" })
    public void moveClocksAndRunInvoiceDue() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Release_Trouble_Ticket_Holds);
	}

	@Test(dependsOnMethods = { "moveClocksAndRunInvoiceDue" })
	public void runInvoiceDueAndCheckDelinquency() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu menu = new BCAccountMenu(driver);
		menu.clickBCMenuDelinquencies();

		this.targetDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        BCCommonDelinquencies delinquency = new BCCommonDelinquencies(driver);
        boolean firstPolicyDelinquencyFound = delinquency.verifyDelinquencyStatus(OpenClosed.Open, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(), targetDate);
		
		if (!firstPolicyDelinquencyFound) {
			Assert.fail("The first Delinquency was either non-existant or not in an 'open' status.");
		}

		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "runInvoiceDueAndCheckDelinquency" })
    public void rescindCancelationJobsInPolicyCenter() throws Exception {
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);

        AccountSummaryPC summaryPage = new AccountSummaryPC(driver);
        boolean pendingCancelFound = summaryPage.verifyCancellationInPolicyCenter(myPolicyObj.squire.getPolicyNumber(), 120);
		if (!pendingCancelFound) {
			Assert.fail("The policy did not get a pending cancellation transaction from BC after waiting for 2 minutes.");
		}
		
		summaryPage.clickWorkOrderbyProduct(ProductLineType.Squire);
        StartCancellation cancellationWorkorder = new StartCancellation(driver);
		cancellationWorkorder.clickCloseOptionsRescindCacellationWithoutDocumentation();

        InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

        summaryPage = new AccountSummaryPC(driver);
		summaryPage.clickWorkOrderbyProduct(ProductLineType.PersonalUmbrella);
        cancellationWorkorder = new StartCancellation(driver);
		cancellationWorkorder.clickCloseOptionsRescindCacellationWithoutDocumentation();

        infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

        summaryPage = new AccountSummaryPC(driver);
        summaryPage.clickPolicyNumber(this.myPolicyObj.squire.getPolicyNumber());

        cancellationWorkorder = new StartCancellation(driver);
		cancellationWorkorder.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null, null, false, 200.00);

        infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

		summaryPage.clickWorkOrderbyProduct(ProductLineType.Squire);
        cancellationWorkorder = new StartCancellation(driver);
		cancellationWorkorder.clickCloseOptionsRescindCacellationWithoutDocumentation();

        infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

        summaryPage = new AccountSummaryPC(driver);
		summaryPage.clickWorkOrderbyProduct(ProductLineType.PersonalUmbrella);
        cancellationWorkorder = new StartCancellation(driver);
		cancellationWorkorder.clickCloseOptionsRescindCacellationWithoutDocumentation();

		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "rescindCancelationJobsInPolicyCenter" })
    public void verifyDelinquencyStepsInBillingCenter() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login("su", "gw");

        TopMenuAdministrationPC topMenuAdministration = new TopMenuAdministrationPC(driver);
		topMenuAdministration.clickMessageQueues();

        AdminEventMessages eventMessages = new AdminEventMessages(driver);
		eventMessages.clickMessageQueue(MessageQueue.Nexus_Policy_Bind);
        eventMessages.selectMessageFilterOption(MessageQueuesFilterSelectOptions.Unfinished_Messages);
        eventMessages.clickAccountNumberLink(this.myPolicyObj.accountNumber);

        TableUtils tableUtils = new TableUtils(driver);
		int rowCount = tableUtils.getRowCount(eventMessages.getEventMessageQueuesNonSafeOrderObjectTable());
		for(int i = 0; i < rowCount; i++) {
			if (tableUtils.getCellTextInTableByRowAndColumnName(eventMessages.getEventMessageQueuesNonSafeOrderObjectTable(), 1, "Status").equalsIgnoreCase("Pending Acknowledgement")) {
				tableUtils.setCheckboxInTable(eventMessages.getEventMessageQueuesNonSafeOrderObjectTable(), 1, true);
				eventMessages.skipEventMessage();
                driver.navigate().refresh();
            } else if (tableUtils.getCellTextInTableByRowAndColumnName(eventMessages.getEventMessageQueuesNonSafeOrderObjectTable(), 1, "Status").equalsIgnoreCase("Pending Send")) {
				driver.navigate().refresh();
                i--;
			} else if (tableUtils.getCellTextInTableByRowAndColumnName(eventMessages.getEventMessageQueuesNonSafeOrderObjectTable(), 1, "Status").equalsIgnoreCase("Pending Send")) {
				//Need to assert fail if message errors!
			}
		}
		
		new GuidewireHelpers(driver).logout();
	}
}
