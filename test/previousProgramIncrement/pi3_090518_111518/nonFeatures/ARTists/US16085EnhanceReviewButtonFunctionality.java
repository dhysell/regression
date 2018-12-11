package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.ArrayList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement : US16085: Enhance Review Button Functionality
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/248759829148">Link Text</a>
* @Description :Validate Reopen is exists for Review buttons and expired status is showing after renewal started
* @DATE Sep 7, 2018
*/
public class US16085EnhanceReviewButtonFunctionality extends BaseTest {
	private String activityName = "Auto-Issued Policy Change for UW Review";
	private GeneratePolicy myPolicyObjPL;

	@Test
	public void testPolicyChangeUWIssuesInformational() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Delete" + StringsUtils.generateRandomNumberDigits(8),
				"ANI" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.Spouse,
				new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});
		
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());

		myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withANIList(listOfANIs)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Review", "Enhance")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
//		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.performRiskAnalysisAndQuote(myPolicyObjPL);
		policyChangePage.clickIssuePolicy();
		
//		risk.Quote();
//		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
//		if (quotePage.isPreQuoteDisplayed()) {
//			quotePage.clickPreQuoteDetails();
//		}
//		sideMenu.clickSideMenuRiskAnalysis();
//		policyChangePage.clickIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
//		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		
		PolicySummary polSummary = new PolicySummary(driver);		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(polSummary.getActivityAssignment(activityName));
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());
		polSummary.clickActivity(activityName);
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk_UWIssues.clickUWIssuesTab();
		risk_UWIssues.reviewAllInformationalUWIssues();
  }
	
	@Test(dependsOnMethods = {"testPolicyChangeUWIssuesInformational"})
	public void testCheckSecondPolicyChangeWithReopen() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Second policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		
		// PR057 PNI or ANI removed
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.removeANI(myPolicyObjPL.aniList.get(0).getPersonFirstName());

		// PR059 - Policy member contact change
		sideMenu.clickSideMenuHouseholdMembers();
		GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		hmember.clickPolicyHolderMembersByName(myPolicyObjPL.pniContact.getFirstName());
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(
				driver);
		editANIPage.setEditAdditionalNamedInsuredDateOfBirth(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -24));
		
		editANIPage.clickOK();
		hmember.clickRemoveMember(myPolicyObjPL.aniList.get(0).getPersonFirstName());

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		quotePage.clickPreQuoteDetails();
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorderRiskAnalysis(driver).requestApproval();		        
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(new PolicySummary(driver).getActivityAssignment("Approval Requested"));
		new GuidewireHelpers(driver).logout();
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        new PolicySummary(driver).clickActivity("Approval Requested");
        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk_UWIssues.clickUWIssuesTab();
		risk_UWIssues.approveAll();
		risk_UWIssues.reviewAllInformationalUWIssues();
		FullUnderWriterIssues uwIssueButtons = risk_UWIssues.getUnderwriterIssuesWithButtons();
		for(UnderwriterIssue currentUW: uwIssueButtons.getAlreadyApprovedList()){
			Assert.assertTrue(currentUW.isReopen(), "Reopen button does not exists for already Reviewed UW Issue: "+currentUW.getShortDescription());
		}
		
		//Reopen  all the uw Issues and approve them again
		risk_UWIssues.reOpenAllInformationalUWIssues();
		risk_UWIssues.approveAll();
		risk_UWIssues.reviewAllInformationalUWIssues();
		
	}
	
	
	@Test
	public void testRenewalUWIssuesInformational() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(SquireEligibility.Country);

		GeneratePolicy myPolObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolTermLengthDays(78)
				.withInsFirstLastName("Renew", "Enhance")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolObjPL.agentInfo.getAgentUserName(),
				myPolObjPL.agentInfo.getAgentPassword(), myPolObjPL.accountNumber);
		
		PolicySummary polSummary = new PolicySummary(driver);		
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(polSummary.getActivityAssignment("Auto-Issued Submission for UW Review"));
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolObjPL.squire.getPolicyNumber());
		polSummary.clickActivity("Auto-Issued Submission for UW Review");
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_UWIssues risk_UWIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk_UWIssues.clickUWIssuesTab();
		risk_UWIssues.reviewAllInformationalUWIssues();
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolObjPL.accountNumber);
		StartRenewal renewal = new StartRenewal(driver);
		renewal.startRenewal();
		InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarPolicyNumber();
		infoBar.clickProductLogo();
		PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(myPolObjPL);
		
		PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Renewal);
		sideMenu.clickSideMenuRiskAnalysis();
		risk_UWIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		risk_UWIssues.clickUWIssuesTab();
		Assert.assertTrue(risk_UWIssues.checkIfReviewButtonsExist(), "Review button is not exists in Renewal Transaction" );
		
		
		FullUnderWriterIssues allUWIssues = risk_UWIssues.getUnderwriterIssues();

		for (UnderwriterIssue currentIUW : allUWIssues.getAlreadyApprovedList()) {
			new GenericWorkorder(driver).clickWhenClickable(By.xpath("//a[text()='" + currentIUW.getShortDescription() + "']"));
			Assert.assertTrue(risk_UWIssues.checkUWHistoryName("Expired") > 0, "Expired status is not shown in UW Issues History....");
		}
	}
}
