package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.documents.Documents;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.ChargeObject;
import repository.pc.policy.PolicyChargesToBC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartNameChange;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/194194064452">US14028</a>
 * @Description 
	(Problem found in production 277597)
	As a policycenter user I want to be able to remove the SR22 filing on an insured without reversing the $25 filing fee. As the correct user I want to be able to choose "reversed" to actually reverse the $25 filing fee.
	As a policycenter user I want to be able to flat cancel a policy and see the SR22 $25 filing fee reversed. If a Rewrite New Account is done after a flat cancel the SR22 and the $25 filing fee should transfer and be charged on the new account.
	
	Acceptance Criteria: See DE4172 link for permission for SR22 check box, effective date, filing fee
	    If in same job user (UW) charges a $25 filing fee and then reversed the fee there should be no charge or reversal go to BC
	    On a policy change if a user unchecks the SR22 box the effective date, the filing fee and the filing charge date should still display and create block issue for UW to approve
	    When UW gets the policy change with block issue they will verify and issue.
	    If SR22 $25 filing fee needs to be reversed only UW Supervisor should have the drop down to choose "reversed".  This should create a $25 reversal fee to BC. UW can issue.
	    When either above are issued the effective date, the filing fee and the filing charge would remain on driver but be uneditable.
	    On future policy changes the SR22 box should be available for users with permission to recheck. If agent/PA checks box follow original steps of UW issue and only the checkbox is displayed.
	    UW will get policy change, this should bring back the Effective Date, Filing Fee and Filing Charge Date blank and editable for UW with permissions. UW can do new dates and new $25 fee as required.
 * @DATE Apr 24, 2018
 */

@Test(groups= {"ClockMove"})
public class US14028_SR22Validation extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;
	Underwriters uw=null;


	@Test(enabled = true)
	public void testScenario1PolicyChanges() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// Pulled out functionality of issuing for reuse within this user story
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers pcSectionIIIDriversPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers(driver);
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail pcSquireDriverContactPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		repository.pc.workorders.generic.GenericWorkorderVehicles pcVehicleWorkorder = new repository.pc.workorders.generic.GenericWorkorderVehicles(driver);
		repository.pc.workorders.generic.GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new repository.pc.workorders.generic.GenericWorkorderVehicles_Details(driver);
		Vehicle newVehicle = new Vehicle();
		repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis pcRiskPage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
		repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
		PolicyChargesToBC pcChargesToBCPage = new PolicyChargesToBC(driver);
		// issue Policy With Sr22 charges
		issuePolicyWithSr22(pcSideMenu, pcSectionIIIDriversPage, pcSquireDriverContactPage, pcWorkOrder);

		// 4 months in log in as sawells & remove existing car + add another for liability only
		ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Month, 4);
		new GuidewireHelpers(driver).logout();

		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();

		policyChangePage.startPolicyChange("Remove car + add another for liability only", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
        
		pcSideMenu.clickPolicyContractVehicles();
		pcVehicleWorkorder.removeAllVehicles();
		newVehicle.setDriverPL(myPolicyObject.pniContact);
		pcVehicleWorkorder.createVehicleManually();
		
		pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, newVehicle);
	    pcWorkorderVehicleDetails.clickOK();

		pcWorkOrder.clickGenericWorkorderQuote();		
		pcSideMenu.clickSideMenuRiskAnalysis();			
		pcRiskPage.approveAll();
		pcWorkOrder.clickIssuePolicyButton();
		
		// Another 7 days in login as spayton, policy change uncheck SR22 on driver then issue
		ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 7);
		pcWorkorderCompletePage.clickViewYourPolicy();

		policyChangePage = new StartPolicyChange(driver);
		String policyChange2Comment = "After another 7 days, uncheck SR22 on driver then issue";
		policyChangePage.startPolicyChange(policyChange2Comment, DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
			
		pcSideMenu.clickPolicyContractSectionIIIAutoLine();
		pcSectionIIIDriversPage.clickEditFirstDriver();		
		pcSquireDriverContactPage.setSR22Checkbox(false);
		pcSquireDriverContactPage.clickOK();

		pcWorkOrder.clickGenericWorkorderQuote();		
		pcSideMenu.clickSideMenuRiskAnalysis();
        pcRiskPage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
		pcRiskPage.approveAll_IncludingSpecial();
		pcWorkOrder.clickIssuePolicyButton();

        pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
		pcWorkorderCompletePage.clickViewYourPolicy();

		String policyTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(repository.gw.enums.TransactionType.Policy_Change);
		
		pcSideMenu.clickSideMenuChargesToBC();
		Boolean sr22ChargeFound = false;
		List<ChargeObject> charges = pcChargesToBCPage.getChargesToBC(policyTransactionNumber);
		for (ChargeObject charge : charges) {
			if (charge.getLineAmount() == 25.0) {
				sr22ChargeFound = true;
			}
		}
		
		// Acceptance criteria: After policy change the SR22 should remain since we didn't choose to reverse.
		Assert.assertFalse(sr22ChargeFound, "$25 fee found! Sr22 should not be present after policy changes.");
	}

	private void issuePolicyWithSr22(SideMenuPC pcSideMenu, repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers pcSectionIIIDriversPage, repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail pcSquireDriverContactPage, repository.pc.workorders.generic.GenericWorkorder pcWorkOrder) throws Exception {
		Squire mySquire = new Squire();
		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, -25))
				.withSquire(mySquire)
				.withProductType(repository.gw.enums.ProductLineType.Squire)
				.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
				.withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
				.withPolOrgType(repository.gw.enums.OrganizationType.Individual)
				.withInsFirstLastName("ScopeCreeps", "US14028")
				.isDraft()
				.build(repository.gw.enums.GeneratePolicyType.FullApp);


		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.getAgentUserName(),myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);

		pcSideMenu.clickSideMenuPADrivers();
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(myPolicyObject.pniContact.getFirstName());
		paDrivers.setSR22Checkbox(true);
		paDrivers.clickOk();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		repository.pc.workorders.generic.GenericWorkorderQuote quotePage = new repository.pc.workorders.generic.GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk_UWIssues.requestApproval();
		risk_UWIssues.LogOutAndLoginWithAssignedUWAndComplete(myPolicyObject);

		// HERE, ADD SR22 CHARGE
		pcSideMenu.clickPolicyContractSectionIIIAutoLine();
		pcSectionIIIDriversPage.clickEditFirstDriver();
		pcSquireDriverContactPage.setSR22Checkbox(true);
		pcSquireDriverContactPage.setSR22EffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		pcSquireDriverContactPage.setSR22FilingFee(repository.gw.enums.SR22FilingFee.Charged);
		pcSquireDriverContactPage.clickOK();
		pcSideMenu.clickSideMenuRiskAnalysis();
		risk_UWIssues.approveAll();
		risk_UWIssues.clickReleaseLock();
		new GuidewireHelpers(driver).logout();


		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObject.agentInfo.agentUserName,
				myPolicyObject.agentInfo.agentPassword, myPolicyObject.accountNumber);
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(repository.gw.enums.ProductLineType.Squire);
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcSideMenu.clickSideMenuPayment();
		repository.pc.workorders.generic.GenericWorkorderPayment pcPaymentPage = new repository.pc.workorders.generic.GenericWorkorderPayment(driver);
		pcPaymentPage.clickAddDownPayment();
		pcPaymentPage.setDownPaymentType(repository.gw.enums.PaymentType.Cash);
		pcPaymentPage.setDownPaymentAmount(2000);
		pcPaymentPage.clickOK();
		pcSideMenu.clickSideMenuForms();
		pcWorkOrder.clickGenericWorkorderIssue(repository.gw.enums.IssuanceType.Issue);
	}


	@Test(enabled = true)
	public void testScenario2RewriteNewTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers pcSectionIIIDriversPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers(driver);
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail pcSquireDriverContactPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
		repository.pc.workorders.generic.GenericWorkorderRiskAnalysis pcRiskPage = new repository.pc.workorders.generic.GenericWorkorderRiskAnalysis(driver);
		repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
		PolicyChargesToBC pcChargesToBCPage = new PolicyChargesToBC(driver);
		StartRewrite pcRewritePage = new StartRewrite(driver);
		repository.pc.workorders.generic.GenericWorkorder workOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
		// issue Policy With Sr22 charges
		issuePolicyWithSr22(pcSideMenu, pcSectionIIIDriversPage, pcSquireDriverContactPage, pcWorkOrder);
		cancel();
		double totalCharges = pcPolicySummaryPage.getTotalPremium();
		Assert.assertEquals(totalCharges, 0.0, "SR22 Charge was not removed on cancel");
		// Clock move 2 days since it has to be >=1 <=30 days from cancel for remainder
		ClockUtils.setCurrentDates(driver, repository.gw.enums.DateAddSubtractOptions.Day, 1);
		//pcRewritePage.rewriteNewTerm();
		pcRewritePage.startRewriteNewTerm();
		pcSideMenu.clickSideMenuQualification();
		repository.pc.workorders.generic.GenericWorkorderQualification pcQualificationPage = new repository.pc.workorders.generic.GenericWorkorderQualification(driver);
		pcQualificationPage.fillOutFullAppQualifications(myPolicyObject);
		// HERE, ADD SR22 CHARGE
		//new GuidewireHelpers(driver).editPolicyTransaction();
		pcSideMenu.clickPolicyContractSectionIIIAutoLine();
		pcSectionIIIDriversPage.clickEditFirstDriver();

		pcSquireDriverContactPage.setSR22Checkbox(true);
		pcSquireDriverContactPage.setSR22EffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		pcSquireDriverContactPage.setSR22FilingFee(repository.gw.enums.SR22FilingFee.Charged);
		pcSquireDriverContactPage.clickOK();
		StartRewrite rewriteWO = new StartRewrite(driver);
		rewriteWO.visitAllPages(myPolicyObject);
		pcWorkOrder.clickGenericWorkorderQuote();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcRiskPage.approveAll();
		workOrder.clickIssuePolicyButton();

		pcWorkorderCompletePage.clickViewYourPolicy();

		String policyTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(repository.gw.enums.TransactionType.Rewrite_New_Term);

		pcSideMenu.clickSideMenuChargesToBC();
		Boolean sr22ChargeFound = false;
		List<ChargeObject> charges = pcChargesToBCPage.getChargesToBC(policyTransactionNumber);
		for (ChargeObject charge : charges) {
			if (charge.getLineAmount() == 25.0) {
				sr22ChargeFound = true;
			}
		}

		// Acceptance criteria: After re-write new term the SR22 charge should be back on
		Assert.assertTrue(sr22ChargeFound, "$25 fee not found! Sr22 was not present after rewrite new term.");
	}
		
		
	@Test(enabled = true)
	public void testScenario2RewriteFullTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Documents pcDocumentsPage = new Documents(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers pcSectionIIIDriversPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers(driver);
		repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail pcSquireDriverContactPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		repository.pc.workorders.generic.GenericWorkorder pcWorkOrder = new repository.pc.workorders.generic.GenericWorkorder(driver);
		repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
		PolicyChargesToBC pcChargesToBCPage = new PolicyChargesToBC(driver);
		StartRewrite pcRewritePage = new StartRewrite(driver);
		// issue Policy With Sr22 charges
		issuePolicyWithSr22(pcSideMenu, pcSectionIIIDriversPage, pcSquireDriverContactPage, pcWorkOrder);
		cancel();
		double totalCharges = pcPolicySummaryPage.getTotalPremium();		
		
		pcSideMenu.clickSideMenuToolsDocuments();
		
		Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
		String todaysDate = DateUtils.dateFormatAsString("MM/dd/yyyy", today);
		Boolean sr26IsOnPage = !pcDocumentsPage.tr_DocumentsByNameTypeAuthorDate("UW", "SR 26", uw.getUnderwriterFirstName(), todaysDate).isEmpty();
		
		// Acceptance criteria: 
		Assert.assertEquals(totalCharges, 0.0, "All charges did not get reversed on cancel.");
	    Assert.assertTrue(sr26IsOnPage, "Sr26 document was not on policy");
		
		pcRewritePage.rewriteFullTerm(myPolicyObject);
		
		
		pcWorkorderCompletePage.clickViewYourPolicy();
		String policyTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(repository.gw.enums.TransactionType.Rewrite);
		
		pcSideMenu.clickSideMenuChargesToBC();

		Boolean sr22ChargeFound = false;
		List<ChargeObject> charges = pcChargesToBCPage.getChargesToBC(policyTransactionNumber);
		for (ChargeObject charge : charges) {
			if (charge.getLineAmount() == 25.0) {
				sr22ChargeFound = true;
			}
		}
		
		// Acceptance criteria: After re-write new term the SR22 charge should be back on
		Assert.assertTrue(sr22ChargeFound, "$25 fee not found! Sr22 was not present after rewrite full term.");
	}
	
	
		// Needs work 5/2 8:17am
		// Keeping disabled for now, as automation seems difficult and more work than it's worth 5/2 4:08pm
		@Test(enabled = false)
		public void testScenario2RewriteNewAccount() throws Exception {
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
			
			Squire mySquire = new Squire();

            myPolicyObject = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(repository.gw.enums.ProductLineType.Squire)
					.withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
					.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
					.withPolOrgType(repository.gw.enums.OrganizationType.Individual)
					.withInsFirstLastName("ScopeCreeps", "US14028")
					.build(repository.gw.enums.GeneratePolicyType.PolicySubmitted);

            new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);

            PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
			
			pcPolicySummaryPage.clickActivity("Submitted Full Application");
			
			AddSr22ToSubmittedPolicyAndIssueThenCancel();

			double totalCharges = pcPolicySummaryPage.getTotalPremium();
			
			// Acceptance criteria: 
			Assert.assertEquals(totalCharges, 0.0, "All charges were not reversed after cancel!");

            StartRewrite pcRewritePage = new StartRewrite(driver);
			
			
			// TODO: Fix whole builder/generatecontact thing...
            StartNameChange nameChange = new StartNameChange(driver);
			//GenerateContact newContact = nameChange.changePNI(myPolicyObject, GenerateContactType.Person);
			GenerateContact newContact = nameChange.createContact(true, GenerateContactType.Person, "ScopeCreeps", "US14028");
			
			
			GeneratePolicy newPolicy = pcRewritePage.rewriteNewAccount(myPolicyObject, newContact);
			
			// TODO: Here maybe check charges on current acct number, and then compare that with charges on newPolicy above

            repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
			pcWorkorderCompletePage.clickViewYourPolicy();


            double submissionCharges = pcPolicySummaryPage.getRenewalPremium(repository.gw.enums.TransactionType.Submission);
			double rewriteFullTermCharges = pcPolicySummaryPage.getRenewalPremium(repository.gw.enums.TransactionType.Rewrite);
			
			// Acceptance criteria: After re-write new term the SR22 charge should be back on
			Assert.assertTrue((rewriteFullTermCharges == (submissionCharges + 25)), "Charges on the rewrite new account were not greater than submission charges + 25. The $25 fee from SR22 is not present.");
		}
	
	
		// TEST BEING MOVED OUT OF US14028
		// TEST BEING MOVED OUT OF US14028
		// TEST BEING MOVED OUT OF US14028
		@Test(enabled = false)
		public void testScenario2RewriteRemainderOfTerm() throws Exception {
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
			
			Squire mySquire = new Squire();

            myPolicyObject = new GeneratePolicy.Builder(driver)
					.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), repository.gw.enums.DateAddSubtractOptions.Day, -25))
					.withSquire(mySquire)
					.withProductType(repository.gw.enums.ProductLineType.Squire)
					.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
					.withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
					.withPolOrgType(repository.gw.enums.OrganizationType.Individual)
					.withInsFirstLastName("ScopeCreeps", "US14028")
					.build(repository.gw.enums.GeneratePolicyType.PolicySubmitted);

            new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);

			// Pulled out functionality of issuing for reuse within this user story
            PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
			
			pcPolicySummaryPage.clickActivity("Submitted Full Application");
			AddSr22ToSubmittedPolicyAndIssueThenCancel();
			
			// Test moved out, so disabling for now
			//double totalCharges = CheckCharges();		
			double totalCharges = 0.0;


            StartRewrite pcRewritePage = new StartRewrite(driver);
			
			pcRewritePage.rewriteRemainderOfTerm(myPolicyObject);
			
			// TODO: Clock move 1 day since it has to be >=1 <=30 days from cancel for remainder
            repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
			pcWorkorderCompletePage.clickViewYourPolicy();
			
			

			// Acceptance criteria: 
			Assert.assertEquals(totalCharges, 25.0, "SR22 Charge was not present!");
		}
		// TEST BEING MOVED OUT OF US14028
		// TEST BEING MOVED OUT OF US14028
		// TEST BEING MOVED OUT OF US14028	
		
		
		
		

	
	
	// HELPER FUNCTION TO ISSUE/CANCEL
	// Note 5/2/18: Functionality could be useful in other places, should make as state agnostic as possible and pull out to generichelpers
	private void AddSr22ToSubmittedPolicyAndIssueThenCancel() {
        SideMenuPC pcSideMenu = new SideMenuPC(driver);
		pcSideMenu.clickPolicyContractSectionIIIAutoLine();
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers pcSectionIIIDriversPage = new GenericWorkorderSquireAutoDrivers(driver);
        repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail pcSquireDriverContactPage = new repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		pcSectionIIIDriversPage.clickEdit();
		
		pcSquireDriverContactPage.setSR22Checkbox(true);
		pcSquireDriverContactPage.setSR22EffectiveDate(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		pcSquireDriverContactPage.setSR22FilingFee(repository.gw.enums.SR22FilingFee.Charged);
		pcSquireDriverContactPage.clickOK();

        repository.pc.workorders.generic.GenericWorkorder pcGenericWorkorder = new repository.pc.workorders.generic.GenericWorkorder(driver);
		pcGenericWorkorder.clickGenericWorkorderQuote();
		pcGenericWorkorder.clickGenericWorkorderIssue(repository.gw.enums.IssuanceType.FollowedByPolicyCancel);

        repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
        PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
		pcWorkorderCompletePage.clickViewYourPolicy();
		
		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickCancelPolicy();

        StartCancellation pcCancellationPage = new StartCancellation(driver);
		pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NameChange);
        pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
	}


	private void cancel() throws Exception {
		repository.pc.workorders.generic.GenericWorkorderComplete pcWorkorderCompletePage = new repository.pc.workorders.generic.GenericWorkorderComplete(driver);
		PolicyMenu pcPolicyMenu = new PolicyMenu(driver);

		new GuidewireHelpers(driver).logout();
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				this.myPolicyObject.accountNumber);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByLatestStatus();


		pcPolicyMenu.clickMenuActions();
		pcPolicyMenu.clickCancelPolicy();

		StartCancellation pcCancellationPage = new StartCancellation(driver);
		pcCancellationPage.setSourceReasonAndExplanation(repository.gw.enums.Cancellation.CancellationSourceReasonExplanation.NameChange);
		pcCancellationPage.clickStartCancellation();
		pcCancellationPage.clickSubmitOptionsCancelNow();
		pcWorkorderCompletePage.clickViewYourPolicy();
	}


}



//                   ________	           ______
//      |		|	|			|		  |     |
//      |		|	|			|		  |     |
//      |		|	|			|		  |_____|
//      |-------|	|--------	|		  |
//      |		|	|			|		  |
//      |		|	|			|		  |
//      |		|	|________	|______	  |

// 5/8 : removed for now, but keeping in case I want to refactor into base classes


// HELPER FUNCTION TO CHECK CHARGES
//private double CheckCharges() throws Exception {
//SideMenuPC pcSideMenu = new SideMenuPC(driver);
//PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
//String submissionTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(TransactionType.Submission);
//String issuanceTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(TransactionType.Issuance);
//String cancellationTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(TransactionType.Cancellation);
//
//pcSideMenu.clickSideMenuChargesToBC();
//
//PolicyChargesToBC pcChargesToBCPage = new PolicyChargesToBC(driver);
//
//List<ChargeObject> submissionCharges = pcChargesToBCPage.getChargesToBC(submissionTransactionNumber);
//List<ChargeObject> issuanceCharges = pcChargesToBCPage.getChargesToBC(issuanceTransactionNumber);
//List<ChargeObject> cancellationCharges = pcChargesToBCPage.getChargesToBC(cancellationTransactionNumber);
//
//double totalCharges = 0;
//
//for (ChargeObject chargeAmount : submissionCharges) {
//totalCharges += chargeAmount.getLineAmount();
//}
//for (ChargeObject chargeAmount : issuanceCharges) {
//totalCharges += chargeAmount.getLineAmount();
//}
//for (ChargeObject chargeAmount : cancellationCharges) {
//totalCharges += chargeAmount.getLineAmount();
//}
//
//return totalCharges;
//}
//
//// HELPER FUNCTION TO CHECK CHARGES
//private double CheckSpecificCharges(TransactionType type) throws Exception {
//SideMenuPC pcSideMenu = new SideMenuPC(driver);
//PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
//if (!pcPolicySummaryPage.summaryPageExists()) {
//pcSideMenu.clickSideMenuToolsSummary();	
//}		
//String TransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(type);
//
//pcSideMenu.clickSideMenuChargesToBC();
//
//PolicyChargesToBC pcChargesToBCPage = new PolicyChargesToBC(driver);
//
//List<ChargeObject> Charges = pcChargesToBCPage.getChargesToBC(TransactionNumber);
//double charge = pcChargesToBCPage.getAmountByChargeGroup(myPolicyObject.getFirstName());
//double totalCharges = 0;
//
//for (ChargeObject chargeAmount : Charges) {
//totalCharges += chargeAmount.getLineAmount();
//}
//
//return totalCharges;
//}
//
//private ArrayList<Double> CheckSubmissionAndChangeCharges() throws Exception {
//SideMenuPC pcSideMenu = new SideMenuPC(driver);
//PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
//if (!pcPolicySummaryPage.summaryPageExists()) {
//pcSideMenu.clickSideMenuToolsSummary();	
//}		
//
//ArrayList<Double> chargeAmounts = new ArrayList<>();
//String submissionTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(TransactionType.Submission);
//String changeTransactionNumber = pcPolicySummaryPage.getCompletedTransactionNumberByType(TransactionType.Policy_Change);
//
//pcSideMenu.clickSideMenuChargesToBC();
//
//PolicyChargesToBC pcChargesToBCPage = new PolicyChargesToBC(driver);
//
//List<ChargeObject> submissionCharges = pcChargesToBCPage.getChargesToBC(submissionTransactionNumber);
//List<ChargeObject> changeCharges = pcChargesToBCPage.getChargesToBC(changeTransactionNumber);
//
//double totalChargesSubmission = 0;
//double totalChargesChange = 0;
//
//for (ChargeObject chargeAmount : submissionCharges) {
//totalChargesSubmission += chargeAmount.getLineAmount();
//}
//
//for (ChargeObject chargeAmount : changeCharges) {
//totalChargesChange += chargeAmount.getLineAmount();
//}
//
//chargeAmounts.add(totalChargesSubmission);
//chargeAmounts.add(totalChargesChange);
//return chargeAmounts;
//}
















