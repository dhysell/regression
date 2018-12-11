package regression.r2.clock.policycenter.rewrite;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.BatchHelpers;
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
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_SelfReportingIncidents;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US10340 : [part II] Create an activity for No Prior Proof
 * @Description - System creates an activity and assign UW on 181 day from effective date with target of 7 business and escalation of 10 business when
 * PNI has No Prior Proof of Insurance > 0 in the SRP tab for Issuance, Rewrite New Account, Rewrite Full Term, or Rewrite New Term
 * @DATE Mar 01, 2017
 */
@QuarantineClass
public class TestCreateAnActivityForNoPriorProof extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy squirePolObj, squirefulltermPolObj, rewriteNewTermPolicyObj, myStdLibobj, rewriteNewAccountPolicyObj;
	private Underwriters uw;	

    //Issuance Job
	@Test
	private void generateSquireAutoQQ() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK, true, UninsuredLimit.Fifty,true,UnderinsuredLimit.Fifty);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Test", "FikeMarkers")
				.withProductType(ProductLineType.Squire)				
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.QuickQuote);

	}

	@Test(dependsOnMethods = {"generateSquireAutoQQ"})
	private void testAddSRPIncidentNoProofOfInsurance() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchSubmission(squirePolObj.agentInfo.getAgentUserName(), squirePolObj.agentInfo.getAgentPassword(), squirePolObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickEditPolicyTransaction();

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(squirePolObj.pniContact.getFirstName());
        paDrivers.clickSRPIncidentsTab();
		paDrivers.setNoProofInsuranceIncident(1);		
		paDrivers.clickOk();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();

	}

	@Test(dependsOnMethods = { "testAddSRPIncidentNoProofOfInsurance" })
	private void testIssueAndValidateNoPriorProofOfInsurenceActivities() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		squirePolObj.convertTo(driver, GeneratePolicyType.PolicyIssued);	

		//Validation related to No Proof Of Insurance Activities
		testValidateActivities(squirePolObj);
	}


    //Rewrite Full Term Job
	@Test
	private void generateFulltermSquireAutoQQ() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK, true, UninsuredLimit.Fifty,true,UnderinsuredLimit.Fifty);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirefulltermPolObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Test", "FikeMarkers")
				.withProductType(ProductLineType.Squire)				
				.withPolOrgType(OrganizationType.Individual)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

	}

	@Test(dependsOnMethods = {"generateFulltermSquireAutoQQ"})
	private void testRewrite() throws Exception{
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirefulltermPolObj.accountNumber);

		//Cancel Policy
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		String comment = "For Rewrite full term of the policy";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, comment, currentDate, true);

		driver.quit();
		
        //Rewrite Full Term
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), squirefulltermPolObj.accountNumber);
        StartRewrite rewritePage = new StartRewrite(driver);

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();

		policyMenu.clickRewriteFullTerm();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuLineSelection();
		sideMenu.clickSideMenuQualification();
		sideMenu.clickSideMenuPolicyInfo();
		sideMenu.clickSideMenuHouseholdMembers();
		sideMenu.clickSideMenuPLInsuranceScore();
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(squirefulltermPolObj.pniContact.getFirstName());
        paDrivers.clickSRPIncidentsTab();
		paDrivers.setNoProofInsuranceIncident(1);		
		paDrivers.clickOk();		
		sideMenu.clickSideMenuPACoverages();
		sideMenu.clickSideMenuPAVehicles();
		sideMenu.clickSideMenuClueAuto();
		sideMenu.clickSideMenuSquireLineReview();
		sideMenu.clickSideMenuPAModifiers();	

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.performRiskAnalysisAndQuote(squirefulltermPolObj);
		rewritePage.clickIssuePolicy();
		
		new GuidewireHelpers(driver).logout();

		//validate No Prior Proof of Issuance Activities
		testValidateActivities(squirefulltermPolObj);
	}

	//Rewrite New Term Job
	@Test 
	public void testGenerateRewriteNewTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -55);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        rewriteNewTermPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolEffectiveDate(newEff)				
				.withInsFirstLastName("Test", "NewTerm")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testGenerateRewriteNewTerm" })
	public void testRewriteNewTermJob() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewTermPolicyObj.squire.getPolicyNumber());

        StartCancellation cancelPol = new StartCancellation(driver);
        Date currentDate = rewriteNewTermPolicyObj.squire.getEffectiveDate();
		Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", cancellationDate, true);
        new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewTermPolicyObj.squire.getPolicyNumber());

		//rewriteNewTerm 	
//        PolicyMenu policyMenu = new PolicyMenu(driver);
//		policyMenu.clickMenuActions();
//		//		policyMenu.clickRewriteNewTerm();
        new StartRewrite(driver).startRewriteNewTerm();
        GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);

		eligibilityPage.clickNext();
        GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.clickNext();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQualificationNext();		
		qualificationPage.clickPL_PALosses(false);
		qualificationPage.clickPL_Traffic(false);
		qualificationPage.click_ConvictedOfAnyFelony(false);
		qualificationPage.clickPL_Business(false);
		qualificationPage.clickPL_Hagerty(false);
		qualificationPage.clickPL_Cancelled(false);
		qualificationPage.clickPL_Bankruptcy(false);

        new GuidewireHelpers(driver).clickNext();
        GenericWorkorderPolicyMembers houseHold = new GenericWorkorderPolicyMembers(driver);
		houseHold.clickNext();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
		creditReport.clickNext();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(rewriteNewTermPolicyObj.pniContact.getFirstName());
        paDrivers.clickSRPIncidentsTab();
		paDrivers.setNoProofInsuranceIncident(1);		
		paDrivers.clickOk();
		sideMenu.clickSideMenuPACoverages();
		sideMenu.clickSideMenuPAVehicles();
		sideMenu.clickSideMenuClueAuto();
		sideMenu.clickSideMenuSquireLineReview();
		sideMenu.clickSideMenuPAModifiers();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.performRiskAnalysisAndQuote(rewriteNewTermPolicyObj);

        StartRewrite rewritePage = new StartRewrite(driver);
		rewritePage.clickIssuePolicy();

		new GuidewireHelpers(driver).logout();

        //validate Activities
		testValidateActivities(rewriteNewTermPolicyObj);

	}
 
	//Rewrite New Account Job
	@Test 
	public void testGenerateRewriteNewAccount() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.FiveK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        rewriteNewAccountPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)					
				.withInsFirstLastName("Test", "NewTerm")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testGenerateRewriteNewAccount" })
	public void testRewriteNewAccountJob() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), rewriteNewAccountPolicyObj.accountNumber);
        //Cancel policy
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
		String comment = "Renumbering to another policy";
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NameChange, comment, currentDate, true);
        //Generate Squire Policy
		testStandardLibPolicyRewrite();
		driver.quit();
		//Rewrite New Account
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myStdLibobj.accountNumber);

        StartRewrite rewritePage = new StartRewrite(driver);
		rewritePage.startQuoteRewrite(rewriteNewAccountPolicyObj.accountNumber ,myStdLibobj.accountNumber);

        SearchOtherJobsPC jobSearchPC = new SearchOtherJobsPC(driver);
		jobSearchPC.searchJobByAccountNumberSelectProduct(myStdLibobj.accountNumber, ProductLineType.Squire);		

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.setPolicyInfoOrganizationType(OrganizationType.Individual);


		sideMenu.clickSideMenuPLInsuranceScore();
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
		creditReport.fillOutCreditReport(myStdLibobj);				
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers_SelfReportingIncidents paDrivers = new GenericWorkorderSquireAutoDrivers_SelfReportingIncidents(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(rewriteNewAccountPolicyObj.pniContact.getFirstName());
        paDrivers.clickSRPIncidentsTab();
		paDrivers.setNoProofInsuranceIncident(1);		
		paDrivers.clickOk();
		sideMenu.clickSideMenuPACoverages();
		sideMenu.clickSideMenuPAVehicles();
		sideMenu.clickSideMenuClueAuto();
		sideMenu.clickSideMenuSquireLineReview();
		sideMenu.clickSideMenuPAModifiers();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.performRiskAnalysisAndQuote(myStdLibobj);

		rewritePage.clickIssuePolicy();
		new GuidewireHelpers(driver).logout();

		//validate Activities
		testValidateActivities(rewriteNewAccountPolicyObj);

	}

	private void testStandardLibPolicyRewrite() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		locationsList.add(new PolicyLocation());

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        myStdLibobj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Guy", "StdLiab")					
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);			
	}
	
	private void testValidateActivities(GeneratePolicy policy) throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 181);
		
		//No proof of Insurance Activity
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Trigger_Activities);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(),uw.getUnderwriterPassword(), policy.accountNumber);

        PolicySummary summaryPage = new PolicySummary(driver);

		if (!summaryPage.checkIfActivityExists("No Prior Proof of Insurance on policy"))
			Assert.fail("No Prior Proof of Insurance on policy is not displayed.");

		summaryPage.clickActivity("No Prior Proof of Insurance on policy");
		
		//No Prior Proof Of Insurance escalated activity
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		Date esclationDate = activityPopupPage.getActivityEscalationDate();

		int days = DateUtils.getDifferenceBetweenDates(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), esclationDate, DateDifferenceOptions.Day);		
		new GuidewireHelpers(driver).logout();

		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days+2);	
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Activity_Escalation);
        // delay required

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(),uw.getUnderwriterPassword(), policy.accountNumber);
		String monthDate = DateUtils.dateFormatAsString("MM/dd", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		if (!summaryPage.checkIfActivityExists("Activity Escalated on "+ monthDate +" - No Prior Proof of Insurance on policy"))
			Assert.fail("Activity Escalated on "+ monthDate +" - No Prior Proof of Insurance on policy Activity is not triggered");

	}

}




