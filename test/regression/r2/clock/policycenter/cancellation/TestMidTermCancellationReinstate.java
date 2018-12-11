package regression.r2.clock.policycenter.cancellation;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ReinstateReason;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.TransactionType;
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
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.reinstate.StartReinstate;
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
 * @Requirement If a policy is cancelled, then has a change done effective prior to cancellation, then is reinstated, then has another change attempted effective prior to cancellation,
 * PC was not allowing the second change to take place. This test verifies that this is fixed.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/128071644704d/detail/defect/176913830944">Rally Defect DE6860</a>
 * @Description
 * @DATE Nov 28, 2017
 */
@QuarantineClass
public class TestMidTermCancellationReinstate extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj, myNewTermPolicyObjPL;
	public Agents agent;
	public Underwriters uw;

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
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Change", "After Reinstate")
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Semi_Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "generateAutoOnlyPolicy" }, enabled = true)
	public void payDownPaymentAndMoveToMiddleOfTerm() throws Exception {
		ARUsers arUsers = ARUsersHelper.getRandomARUserByCompany(ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsers.getUserName(), arUsers.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges charges = new AccountCharges(driver);
		charges.waitUntilIssuanceChargesArrive();

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());
		
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Month, 4);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "payDownPaymentAndMoveToMiddleOfTerm" }, enabled = true)
	public void cancelPolicy() throws Exception {
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartCancellation cancellation = new StartCancellation(driver);
		cancellation.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Cancelling Policy", null, true);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 20);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "cancelPolicy" }, enabled = true)
	public void verifyThatChargesMakeItToBC() throws Exception {
		ARUsers arUsers = ARUsersHelper.getRandomARUserByCompany(ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsers.getUserName(), arUsers.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "verifyThatChargesMakeItToBC" })
	public void changePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("Increase Coverage", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Month, -1));

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPACoverages();

        GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setLiabilityCoverage(LiabilityLimit.ThreeHundredHigh);
		coverages.setMedicalCoverage(MedicalLimit.TenK);
		coverages.setUninsuredCoverage(true, UninsuredLimit.ThreeHundred);
		coverages.setUnderinsuredCoverage(true, UnderinsuredLimit.ThreeHundred);
		coverages.clickNext();

        policyChange = new StartPolicyChange(driver);
		policyChange.quoteAndIssue();
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "changePolicy" })
	public void reinstatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartReinstate reinstate = new StartReinstate(driver);
		reinstate.reinstatePolicy(ReinstateReason.Payment_Received, "Reinstating Policy cause it's the right thing to do.");
		
		new GuidewireHelpers(driver).logout();
	}
	
	@Test(dependsOnMethods = { "reinstatePolicy" })
	public void changePolicy2() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("Decrease Coverage", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Month, -1));
		
		if (policyChange.finds(By.xpath("//label[contains(@id, 'StartPolicyChangeScreen:ErrorMessage')]")).isEmpty()) {
			if ((policyChange.find(By.xpath("//label[contains(@id, 'StartPolicyChangeScreen:ErrorMessage')]")).getText().contains("Cannot change policy: Between dates")) && (policyChange.find(By.xpath("//label[contains(@id, 'StartPolicyChangeScreen:ErrorMessage')]")).getText().contains("this policy is canceled"))) {
				Assert.fail("The attempt to perform a policy change after reinstate resulted in an error stating that it couldn't change the policy. This should not be the case. Test Failed.");
			} else {
				Assert.fail("There was an error when attempting to perform a policy change after a reinstate, but it wasn't the error we were expecting. Please investigate.");
			}
		} else {
			getQALogger().info("There was no error when attempting to perform a policy change after a reinstate. Test passed.");
		}
		
		new GuidewireHelpers(driver).logout();
	}
}
