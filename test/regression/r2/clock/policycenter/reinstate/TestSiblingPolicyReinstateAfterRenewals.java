package regression.r2.clock.policycenter.reinstate;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RenewalCode;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4985: Should not receive validation on Sibling policy
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/94107312488"</a>
 * @Description
 * @DATE May 22, 2017
 */
@QuarantineClass
public class TestSiblingPolicyReinstateAfterRenewals extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy squirePolicy, siblingSquirePolicy;
	private Underwriters uw;

	@Test()
	private void testIssueSquirePol() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -286);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        squirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("SUSAN", "REDWINE")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquirePol" })
	private void testIssueSiblingPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Day, -286);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        siblingSquirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .withPolOrgType(OrganizationType.Sibling)
                .withInsFirstLastName("Test", "Sibling")
                .build(GeneratePolicyType.QuickQuote);
        driver.quit();
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchSubmission(siblingSquirePolicy.agentInfo.getAgentUserName(),
				siblingSquirePolicy.agentInfo.getAgentPassword(), siblingSquirePolicy.accountNumber);

		Date dob = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter),
				DateAddSubtractOptions.Year, -24);
		Date Dob = DateUtils.dateAddSubtract(dob, DateAddSubtractOptions.Day, 2);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		policyInfo.clickEditPolicyTransaction();

		sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTableByDriverName(siblingSquirePolicy.pniContact.getFirstName());
        paDrivers.setAutoDriversDOB(Dob);
        paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.clickOk();

		sideMenu.clickSideMenuPolicyInfo();
		policyInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);
		new BasePage(driver).sendArbitraryKeys(Keys.TAB);

        policyInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, squirePolicy.squire.getPolicyNumber());
        new GuidewireHelpers(driver).clickNext();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
		new GuidewireHelpers(driver).logout();
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		siblingSquirePolicy.convertTo(driver, GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSiblingPolicy" })
	private void testInitialSiblingPolicyRenew() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				siblingSquirePolicy.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(siblingSquirePolicy);

		new GuidewireHelpers(driver).logout();
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				siblingSquirePolicy.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();

		sideMenu.clickSideMenuQuote();
        StartRenewal renewal = new StartRenewal(driver);
		renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();
	}

	@Test(dependsOnMethods = { "testInitialSiblingPolicyRenew" })
    private void testMoveClockToSecondRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 365);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.PC_Workflow);
        // delay required to create a claim
	}

	@Test(dependsOnMethods = { "testMoveClockToSecondRenewal" })
	private void testSiblingPolicySecondRenewal() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				siblingSquirePolicy.accountNumber);

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();

        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(siblingSquirePolicy);

		new GuidewireHelpers(driver).logout();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), siblingSquirePolicy.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
        String errorMessage = "";
		if (!polInfo.getOrganizationTypeCurrentOption().equals(OrganizationTypePL.Individual.getValue())) {
			errorMessage = errorMessage + "Organization Type is not changed to Individual \n";
		}

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll_IncludingSpecial();

		sideMenu.clickSideMenuQuote();
        StartRenewal renewal = new StartRenewal(driver);
		renewal.clickRenewPolicy(RenewalCode.Renew_Good_Risk);

        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        new GuidewireHelpers(driver).logout();

		if (errorMessage != "") {
			Assert.fail(errorMessage);
		}
	}

	@Test(dependsOnMethods = { "testSiblingPolicySecondRenewal" })
	private void testCancelReinstatePolicyTerm() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), siblingSquirePolicy.accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
		Date currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		String comment = "Testing Purpose";
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.LossRuns, comment, currentDate, true);

		// searching again
        SearchPoliciesPC policySearchPC = new SearchPoliciesPC(driver);
		policySearchPC.searchPolicyByAccountNumber(siblingSquirePolicy.accountNumber);
        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickMenuActions();
		policyMenu.clickReinstatePolicy();
        StartReinstate reinstatePolicy = new StartReinstate(driver);

		reinstatePolicy.setReinstateReason("Payment received");
        cancelPol.setDescription("Testing purpose");
        reinstatePolicy.quoteAndIssue();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

		new GuidewireHelpers(driver).logout();
	}
}
