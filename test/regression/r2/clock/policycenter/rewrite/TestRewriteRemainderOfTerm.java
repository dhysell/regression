package regression.r2.clock.policycenter.rewrite;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.common.BCCommonTroubleTickets;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.RewriteType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.enums.Underwriter;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US4789 : [Part II] PL - Rewrite Remainder of Term
 * @Description -  Issue Policy, Create Rewrite Remainder of the Term, Validations for Rewrite Option  available when policy canceled >=0 days and <=45 days
 * but effective date of rewrite must be >=1 and <=30 days from the cancellation date, edited effective date etc *
 * @DATE Aug 15, 2016
 */
@QuarantineClass
public class TestRewriteRemainderOfTerm extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private Agents agent;
	Underwriters uw;

	@Test ()
	public void testGenerateSquireAutoIssuance() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAgent(agent)
				.withProductType(repository.gw.enums.ProductLineType.Squire)
                .withLineSelection(repository.gw.enums.LineSelection.PersonalAutoLinePL)
				.withCreateNew(repository.gw.enums.CreateNew.Create_New_Always)
				.withInsPersonOrCompany(repository.gw.enums.ContactSubType.Person)
				.withInsFirstLastName("Guy", "Auto")
				.withPolOrgType(repository.gw.enums.OrganizationType.Individual)
				.withPaymentPlanType(repository.gw.enums.PaymentPlanType.Annual)
				.withDownPaymentType(repository.gw.enums.PaymentType.Cash)
				.build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
	} 

	@Test (dependsOnMethods = { "testGenerateSquireAutoIssuance" })
	public void CancelMidTermAndRewriteReaminderBefore45Days() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		Date cancellationDate = DateUtils.dateAddSubtract(currentDate, repository.gw.enums.DateAddSubtractOptions.Day, 20);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", cancellationDate, true);

		new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
		// move clock 25 days
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 25);

		//rewriteMidTerm <=45 days from cancel date		
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
        try {
			policyMenu.clickRewriteRemainderOfTerm();
		}catch(Exception e){
			throw new Exception("RewriteRemainderOfTerm option not exist under Actions Menu");
		}
	}
	
	@Test ()
	public void validateTransactionEffectiveDate() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
		//validate Transaction Effective Date
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);		
		Date currentDate = DateUtils.dateAddSubtract(currentSystemDate, repository.gw.enums.DateAddSubtractOptions.Day, 1);
        PolicySummary transaction = new PolicySummary(driver);
		Date transactionEffectiveDate = transaction.getTransactionEffectiveDate(repository.gw.enums.TransactionType.Rewrite);

		boolean testFailed = false;
		String errorMessage = "";
		boolean messageFound = false;
		if(!currentDate.equals(transactionEffectiveDate)) {
			messageFound = true;					
		}
		if (messageFound) {
			testFailed = true;
			errorMessage = errorMessage + "Effective date not defaulted to tomorrow";
		}
		if (testFailed)
			Assert.fail(errorMessage);
	}
	
	@Test (dependsOnMethods = { "CancelMidTermAndRewriteReaminderBefore45Days" })
	public void rewriteRemainderTerm45daysafterCancel() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

		// move clock 50 days
		ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 50);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        if (policyMenu.checkRewriteRemainderOfTermExist())
			throw new Exception("Rewrite Remainder of Term should not available 45 days after cancellation.");						
	}
	

	public GeneratePolicy createRewriteAutoOnlyPolicy() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		AddressInfo aiAddress = new AddressInfo("3696 South 6800 West", "West Valley City", State.Utah,"84128");
		
		ArrayList<repository.gw.enums.LineSelection> lines = new ArrayList<repository.gw.enums.LineSelection>();
		lines.add(repository.gw.enums.LineSelection.PersonalAutoLinePL);
		
		Vehicle vehicle1 = new Vehicle();
		AdditionalInterest autoAI = new AdditionalInterest();
		autoAI.setAdditionalInterestSubType(repository.gw.enums.AdditionalInterestSubType.PLSectionIIIAuto);
		autoAI.setCompanyOrInsured(repository.gw.enums.ContactSubType.Company);
		autoAI.setCompanyName("Bank of Mom and Dad");
		autoAI.setAdditionalInterestType(repository.gw.enums.AdditionalInterestType.LienholderPL);
		autoAI.setAddress(aiAddress);
		autoAI.setNewContact(repository.gw.enums.CreateNew.Create_New_Only_If_Does_Not_Exist);
		
		ArrayList<AdditionalInterest> autoAIList = new ArrayList<AdditionalInterest>();
		autoAIList.add(autoAI);
		vehicle1.setAdditionalInterest(autoAIList);
		ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
		vehicles.add(vehicle1);
		SquirePersonalAuto spa = new SquirePersonalAuto();
		spa.setVehicleList(vehicles);

        Squire mySquire = new Squire(repository.gw.enums.SquireEligibility.City);
        mySquire.squirePA = spa;

        GeneratePolicy autoOnly = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(repository.gw.enums.ProductLineType.Squire)
				.withPolOrgType(OrganizationType.Individual)
				.withInsFirstLastName("Test", "Auto")
				.build(repository.gw.enums.GeneratePolicyType.PolicyIssued);
		return autoOnly;
		
	}
	
	/**
	 * @Author sbroderick
	 * @Requirement : DE5049 : Rate as of date incorrect on rewrite remainder of term
	 * @Description -  
	 * Create a squire policy bind and issue 
	   Cancel policy effective the day after issuance Carrier-lack of underwriting information-other
	   Cancel now
	   Rewrite remainder of term effective one day after the cancel date.
	   visit policy info page and compare rate as of date on the rewrite remainder of term to the issuance rate as of date
	 * @DATE May 10, 2017
	 */
	
	@Test
	public void testMoneyCancel() throws Exception{
		GeneratePolicy autoOnly = createRewriteAutoOnlyPolicy();
		Underwriters uw = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), repository.gw.enums.DateAddSubtractOptions.Day, 1));
		
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(), autoOnly.squire.getPolicyNumber());
        BCCommonTroubleTickets troubleTicket = new BCCommonTroubleTickets(driver);
		troubleTicket.closeFirstTroubleTicket();

		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
		new GuidewireHelpers(driver).logout();
		
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        PolicySummary policySummary = new PolicySummary(driver);
		boolean activity = false;
		int lcv = 0;
		do{
			new Login(driver).loginAndSearchPolicyByAccountNumber(uw.underwriterUserName, uw.underwriterPassword, autoOnly.accountNumber);
            policySummary = new PolicySummary(driver);
			activity = policySummary.checkIfActivityExists("Cancel Policy for Accounts Receivable");
			lcv++;
			new GuidewireHelpers(driver).logout();
		}while(!activity && lcv<10);
		
		ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), repository.gw.enums.DateAddSubtractOptions.Day, 21));
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.PC_Workflow);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.underwriterUserName, uw.underwriterPassword, autoOnly.accountNumber);
		StartRewrite rewriteWO = new StartRewrite(driver);
		rewriteWO.initiateRewriteRemainderOfTerm();
		rewriteWO.clickThroughAllPagesUntilRiskAnalysis(autoOnly, RewriteType.RemainderOfTerm);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		Date rewriteRateDate = policyInfoPage.getPolicyInfoRateDate();
		sideMenu.clickSideMenuRiskAnalysis();
		rewriteWO = new StartRewrite(driver);
		rewriteWO.riskAnalysisAndQuote();
		rewriteWO.clickIssuePolicy();		
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.underwriterUserName, uw.underwriterPassword, autoOnly.accountNumber);
        SideMenuPC mySideMenu = new SideMenuPC(driver);
		mySideMenu.clickSideMenuToolsDocuments();

//		Reinstatement Notice Partial and Reinstate Notice-Partial UW inferred
        PolicyDocuments myPolDoc = new PolicyDocuments(driver);
		myPolDoc.setDocumentName("Reinstatement Notice Partial");
		myPolDoc.selectRelatedTo("Rewrite Remainder of Term");
		myPolDoc.clickSearch();
		if(myPolDoc.getDocumentCount("Reinstatement Notice Partial") > 0){
			Assert.fail("The Reinstatement Notice Partial and Reinstate Notice-Partial UW are now review cases.  File defect.");
		}

        myPolDoc = new PolicyDocuments(driver);
		myPolDoc.setDocumentName("Reinstatement Notice Partial");
		myPolDoc.selectRelatedTo("Rewrite Remainder of Term");
		myPolDoc.clickSearch();
		if(myPolDoc.getDocumentCount("Reinstate Notice-Partial UW") > 0){
			Assert.fail("The Reinstatement Notice Partial and Reinstate Notice-Partial UW are now review cases.  File defect.");
		}

        if (!rewriteRateDate.equals(DateUtils.dateFormatAsString("MM/dd/yyyy", autoOnly.squire.getEffectiveDate()))) {
			Assert.fail("For Rewrite Remainder of Term the rate date should be the same as issuance.");
		}
	}
}
