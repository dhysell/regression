package regression.r2.clock.billingcenter.invoicing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.RewriteType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author bhiltbrand
* @Requirement This test checks after changing a down payment on a rewrite new term to something different than the original term that the invoices use that new payment plan instead of the old plan.
* @DATE Dec 5, 2017
*/
public class TestRewriteNewTermInvoicing extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj, myNewTermPolicyObjPL;
	public Agents agent;
	public Underwriters uw;	
	public ARUsers arUser;

	@Test
	public void generateAutoOnlyPolicy() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);		
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAgent(this.agent)
				.withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
				.withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
				.withInsFirstLastName("Rewrite", "Invoicing")
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(repository.gw.enums.PaymentPlanType.Semi_Annual)
				.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
				.build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
	} 

	@Test(dependsOnMethods = { "generateAutoOnlyPolicy" }, enabled = true)
	public void cancelPolicy() throws Exception {
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Cancelling Policy", null, true);
        ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 20);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.PC_Workflow);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
		
	}
	
	@Test(dependsOnMethods = { "cancelPolicy" }, enabled = true)
	public void verifyThatChargesMakeItToBC() throws Exception {
		this.arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Cancellation);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyThatChargesMakeItToBC" })
	public void moveClocks() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 35);
	}
	
	@Test(dependsOnMethods = { "moveClocks" })
	public void rewritePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartRewrite rewrite = new StartRewrite(driver);
		rewrite.startRewriteNewTerm();
		rewrite.rewriteNewTermGuts(repository.gw.enums.LineSelection.PersonalAutoLinePL);
		rewrite.clickThroughAllPagesUntilRiskAnalysis(myPolicyObj, RewriteType.NewTerm);
		rewrite.riskAnalysisAndQuote();

        GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		paymentPage.makeDownPayment(repository.gw.enums.PaymentPlanType.Quarterly, repository.gw.enums.PaymentType.Cash, 0);

        GenericWorkorder genericWorkorder = new GenericWorkorder(driver);
		genericWorkorder.clickIssuePolicyButton();
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "rewritePolicy" })
	public void verifyThatRewriteChargesMakeItToBC() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Rewrite);
	
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyThatRewriteChargesMakeItToBC" })
	public void verifyChargesAndInvoices() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickAccountMenuInvoices();

        AccountInvoices invoices = new AccountInvoices(driver);
		List<Date> dueDatesList = invoices.getListOfDueDates();
		
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		List<Date> toRemove = new ArrayList<>();
		for (Date date : dueDatesList) {
			if (currentDate.after(date)) {
		        toRemove.add(date);
		    }
		}
		dueDatesList.removeAll(toRemove);
		
		if (dueDatesList.size() != 4) {
			Assert.fail("The list of new invoices after the rewrite did not contain 4 entries matching up with the new payment plan. Test failed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyThatRewriteChargesMakeItToBC" })
	public void verifyTroubleTicket() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.squire.getPolicyNumber());
        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
		policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets troubleTickets = new BCCommonTroubleTickets(driver);
		if (!troubleTickets.waitForTroubleTicketsToArrive(20, repository.gw.enums.TroubleTicketType.RewriteToHold)) {
			Assert.fail("The Promise Payment trouble ticket from the rewrite was not created as expected. Test Failed.");
		}
		
		new GuidewireHelpers(driver).logout();		
	}
}
