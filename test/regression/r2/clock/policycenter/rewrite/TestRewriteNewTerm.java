package regression.r2.clock.policycenter.rewrite;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import persistence.enums.ARCompany;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US4792 : [Part II] PL - Rewrite New Term
 * @Description -  Issue Policy, Create Rewrite New Term, Validations for Rewrite Option available when policy canceled >30 days and <=10 years
 * also navigated to all rewrite screens editable/changes made qualifications, coverages, modifiers, effective date  etc
 * @DATE Aug 15, 2016
 */
@QuarantineClass
public class TestRewriteNewTerm extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj, myNewTermPolicyObjPL;
	private Agents agent;
	Underwriters uw;

	@Test
	public void testGenerateSquireAutoIssuance() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withAgent(agent)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withCreateNew(CreateNew.Create_New_Always)				
				.withInsPersonOrCompany(ContactSubType.Person)				
				.withInsFirstLastName("Guy", "Auto")
				.withPolOrgType(OrganizationType.Individual)				
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}
	
	@Test(dependsOnMethods = { "testGenerateSquireAutoIssuance" }, enabled = true)
	public void verifyThatChargesMakeItToBC() throws Exception {
		ARUsers arUsers = ARUsersHelper.getRandomARUserByCompany(ARCompany.Personal);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUsers.getUserName(), arUsers.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
		accountCharges.waitUntilIssuanceChargesArrive();
		new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "verifyThatChargesMakeItToBC" }, enabled = true)
	public void before180Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", cancellationDate, true);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
	
		// move clock 31 days
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 31);	

		//rewriteNewTerm available >=30 and <180 days days from cancel date		
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();

		if(!policyMenu.checkRewriteNewTermExist())
			throw new Exception("Rewrite FullTerm not available 30 days days after cancellation.");	

		policyMenu.clickRewriteNewTerm();
	}

	@Test(dependsOnMethods = { "before180Days" })
	public void verifyQualificationPageIsReAnswered() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
        SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
		jobSearchPC.searchJobByAccountNumberAndJobType(myPolicyObj.accountNumber, TransactionType.Rewrite_New_Term);

        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);

		eligibilityPage.clickNext();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.clickNext();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQualificationNext();

		//Validate Qualification Page
		boolean testFailed = false;
		String errorMessage = "";
		boolean messageFound = false;
        ErrorHandling errorBannerMessagesList = new ErrorHandling(driver);
		for (WebElement errorMessages : errorBannerMessagesList.text_ErrorHandlingErrorBannerMessages()) {
			String errorString = errorMessages.getText();
			if (errorString.equalsIgnoreCase("Please fill in all required fields.")) {	
				qualificationPage.clickPL_PALosses(false);
				qualificationPage.clickPL_Traffic(false);
				qualificationPage.click_ConvictedOfAnyFelony(false);
				qualificationPage.clickPL_Business(false);
				qualificationPage.clickPL_Hagerty(false);
				qualificationPage.clickPL_Cancelled(false);
				qualificationPage.clickPL_Bankruptcy(false);
			}
			else
				messageFound = true;					
		}

		if (messageFound) {
			testFailed = true;
			errorMessage = errorMessage + "Elgibility Page Is not Editable";
		}

		if (testFailed)
			Assert.fail(errorMessage);	
		
		//Navigating to all the Side Menus
		qualificationPage.clickQualificationNext();
        new GuidewireHelpers(driver).clickNext();
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
		houseHold.clickNext();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
		creditReport.clickNext();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuPACoverages();
		
		//Coverages
        GenericWorkorderSquireAutoCoverages_Coverages coverages = new GenericWorkorderSquireAutoCoverages_Coverages(driver);
		coverages.setLiabilityCoverage(LiabilityLimit.OneHundredHigh);
		coverages.setMedicalCoverage(MedicalLimit.TenK);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickSaveDraftButton();
		new GuidewireHelpers(driver).logout();	

		//login with UW for quote and payment

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());
		
		//validate Transaction Effective Date
		Date currentSystemDate = DateUtils.getDateValueOfFormat(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), "MM/dd/yyyy");		
		Date currentDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);
        PolicySummary transaction = new PolicySummary(driver);
		Date transactionEffectiveDate = transaction.getTransactionEffectiveDate(TransactionType.Rewrite);
		System.out.println("Transaction Date is" + DateUtils.dateFormatAsString("MM/dd/yyyy", transactionEffectiveDate));
		
		testFailed = false;
		errorMessage = "";
        messageFound = !currentDate.equals(transactionEffectiveDate);

		if (messageFound) {
			testFailed = true;
			errorMessage = errorMessage + "Effective date not defaulted to tomorrow";
		}

		if (testFailed)
			Assert.fail(errorMessage);

		transaction.clickpolicyPendingTransaction();				

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers drivers = new GenericWorkorderSquireAutoDrivers(driver);
		drivers.clickEditButtonInDriverTable(1);
		drivers.clickOrderMVR();
		drivers.clickOk();

		//Modifiers - cannot modify modifiers in squire. this section needs taken out.
		sideMenu.clickSideMenuModifiers();
        GenericWorkorderModifiers modifiers = new GenericWorkorderModifiers(driver);
		modifiers.enterSquireModifierAdditionalDiscount(3, "10", "Underwriter Adjustments");
		
		//Need to click risk analysis page and click tabs before quote button will be available.
		quote.clickQuote();

		sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment payments = new GenericWorkorderPayment(driver);
		payments.makeDownPayment(PaymentPlanType.Annual, PaymentType.Cash, 0.00);
		sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorder go = new GenericWorkorder(driver);
		go.clickGenericWorkorderQuote();
		go.clickGenericWorkorderIssue(IssuanceType.Issue);
		
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.accountNumber);
        PolicySummary summary = new PolicySummary(driver);
		Date rewriteEffectiveDate = summary.getTransactionEffectiveDate(TransactionType.Rewrite);
		summary.clickCompletedTransactionByType(TransactionType.Rewrite);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo pInfo = new GenericWorkorderPolicyInfo(driver);
		Date rateDate = pInfo.getPolicyInfoRateDate();
		if(!rateDate.equals(rewriteEffectiveDate)){
			Assert.fail("Rewrite New Term should have the Policy Rate Date be the same as the effective Date.");
		}
		new GuidewireHelpers(driver).logout();	
	}
	
	@Test(dependsOnMethods = { "verifyQualificationPageIsReAnswered" })
	public void after180Days() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		testGenerateSquireAutoIssuance();
		before180Days();

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());

		// move clock 160 days
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 160);
		new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObj.squire.getPolicyNumber());


        PolicyMenu policyMenu = new PolicyMenu(driver);
        policyMenu.clickMenuActions();
        if (policyMenu.checkRewriteNewTermExist()) {
			Assert.fail("Rewrite FullTerm should not available 180 days after cancellation.");
		}
		testRenewalRateDate(this.myPolicyObj, TransactionType.Rewrite);
	}
	
	/**	 
	 * @Author skandibanda
	 * @Description :  DE4288 - Rewrite new term gives wrong effective date.
	 
	 */
	
//	@Test (enabled = false)
//	public void testGenerateSquirePoliocy() throws Exception {
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -355);
//		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
//		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
//		squirePersonalAuto.setCoverages(coverages);		
//
//		myNewTermPolicyObjPL = new GeneratePolicy.Builder(driver)
//				.withProductType(ProductLineType.Squire)
//                .withLineSelection(LineSelection.PersonalAutoLinePL)
//				.withInsPersonOrCompany(ContactSubType.Person)				
//				.withInsFirstLastName("Guy", "Auto")
//				.withPolEffectiveDate(newEff)
//				.withPolOrgType(OrganizationType.Individual)				
//				.withPaymentPlanType(PaymentPlanType.Annual)
//				.withDownPaymentType(PaymentType.Cash)
//				.build(GeneratePolicyType.PolicyIssued);
//
//	} 
//	
//	@Test(dependsOnMethods = { "testGenerateSquirePoliocy" }, enabled = false)
//	public void verifyAbleToRewriteNewTerm() throws Exception {		
//
//		Configuration.setProduct(ApplicationOrCenter.PolicyCenter);
//		//		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
//		
//		loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myNewTermPolicyObjPL.accountNumber);
//		// move clock 15 days
//		Date currentSystemDate = DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter);
//
//        StartCancellation cancelPol = new StartCancellation(driver);
//		Date currentDate = DateUtils.getCenterDate(ApplicationOrCenter.PolicyCenter);		
//		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", currentDate, true);
//		//		logout();
//        loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myNewTermPolicyObjPL.squire.getPolicyNumber());
//		
//		// move clock 15 days		
//		ClockUtils.setCurrentDates(DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 15));
//        PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		//
//		//Rewrite NewTerm
//		policyMenu.clickRewriteNewTerm();		
//		
//		if(ErrorHandlingHelpers.errorExists()) 
//			Assert.fail("Errors should not be exist and able to Rewrite new account.");	
//
//	}	
	
		
		/**
		* @throws Exception 
		 * @Author sbroderick
		* @Requirement: Rate Date must match the effective Date
		* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Homeowners/PC8%20-%20HO%20-%20CancelReinstateRewrite%20-%20Rewrite%20All%20Together.xlsx">Link Text</a>
		* @Description 
		* @DATE Jun 6, 2017
		*/
		private void testRenewalRateDate(GeneratePolicy myPol, TransactionType transactionType) throws Exception{
			Config cf = new Config(ApplicationOrCenter.PolicyCenter);
			driver = buildDriver(cf);
            new Login(driver).loginAndSearchPolicyByPolicyNumber(myPol.underwriterInfo.getUnderwriterUserName(), myPol.underwriterInfo.getUnderwriterPassword(), myPol.squire.getPolicyNumber());
            PolicySummary summary = new PolicySummary(driver);
			Date rewriteEffectiveDate = summary.getTransactionEffectiveDate(transactionType);
			summary.clickCompletedTransactionByType(transactionType);
            SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPolicyInfo();
            GenericWorkorderPolicyInfo pInfo = new GenericWorkorderPolicyInfo(driver);
			Date rateDate = pInfo.getPolicyInfoRateDate();
			if(!rateDate.equals(rewriteEffectiveDate)){
				Assert.fail(transactionType.getValue() + " should have the Policy Rate Date be the same as the "+transactionType.getValue() +" Effective Date.");
			}
		}
}
