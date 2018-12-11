package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BusinessownersLine;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.UnderwriterIssues_BOP;
import repository.gw.enums.ValidUntil;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.desktop.DesktopMyActivitiesPC;
import repository.pc.search.SearchAccountsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :US16230: Change "Valid Until" on 10 day binding authority UW issue
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Policy%20Level%20Forms%20Product%20Model%20Spreadsheet.xlsx">Policy Level Forms Product Model Spreadsheet </a>
* @Description: 10 day binding authority UW Issue is displayed in policy change 
* @DATE Sep 8, 2018
*/
public class US16230PolicyChangeBindingAuthorityUWIssue extends BaseTest {
	
	@Test
	public void testSquirePolicyChangeBindingAuthorityUWIssues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(SquireEligibility.Country);

		GeneratePolicy myPolObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -20))
				.withInsFirstLastName("Bind", "Authority")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.isDraft()
				.build(GeneratePolicyType.FullApp);
		
		
		
		new Login(driver).loginAndSearchSubmission(myPolObjPL);
		SideMenuPC sideMenu = new SideMenuPC(driver);	
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
	    if (quotePage.isPreQuoteDisplayed()) {
	          quotePage.clickPreQuoteDetails();
	   }
	   sideMenu.clickSideMenuRiskAnalysis();		
	   GenericWorkorderRiskAnalysis_UWIssues risk_UW = new GenericWorkorderRiskAnalysis_UWIssues(driver);
	   risk_UW.clickRequestApproval();
	   UWActivityPC activity = new UWActivityPC(driver);
		activity.setText("Please Approve this Stuff!!");
		activity.setNewNoteSubject("Please Approve this Stuff!!");
		activity.clickSendRequest();
		InfoBar infoBar = new InfoBar(driver);
		infoBar.clickInfoBarAccountNumber();

		AccountSummaryPC aSumm = new AccountSummaryPC(driver);
		ArrayList<String> activityOwners = new ArrayList<String>();
		activityOwners = aSumm.getActivityAssignedTo("Approval Requested");
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(activityOwners.get(activityOwners.size() - 1));

		TopInfo topInfoStuff = new TopInfo(driver);
		topInfoStuff.clickTopInfoLogout();

		Login loginPage = new Login(driver);
		loginPage.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
		SearchAccountsPC search = new SearchAccountsPC(driver);
		search.searchAccountByAccountNumber(myPolObjPL.accountNumber);
		AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickActivitySubject("Approval Requested");
		ActivityPopup actPop = new ActivityPopup(driver);
		actPop.clickCompleteButton();
		sideMenu.clickSideMenuRiskAnalysis();		   
		risk_UW.enterValidUntilForSpecificUWIssue(PLUWIssues.TenDaySubmittingAuthority.getShortDesc(), ValidUntil.NextChange.getValue());
		risk_UW.approveAll();
		risk_UW.clickReleaseLock();
		new GuidewireHelpers(driver).logout();

		loginPage = new Login(driver);
		loginPage.login(myPolObjPL.agentInfo.getAgentUserName(), myPolObjPL.agentInfo.getAgentPassword());
		DesktopMyActivitiesPC myActivities = new DesktopMyActivitiesPC(driver);
		myActivities.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Underwriter has reviewed this job", myPolObjPL.accountNumber, myPolObjPL.pniContact.getFirstName());
		sideMenu.clickSideMenuRiskAnalysis();
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolObjPL);
		sideMenu.clickSideMenuForms();
		payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickViewYourPolicy();
		
		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("first policy Change",myPolObjPL.squire.getEffectiveDate());
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
		quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
		Assert.assertTrue(uwIssues.isInList(PLUWIssues.TenDaySubmittingAuthority.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected Blocking Issuance : " + PLUWIssues.TenDaySubmittingAuthority.getShortDesc() + " is not displayed");
		
  }
  
  @Test()
  public void testBOPPolicyChangeBindingAuthorityUWIssues() throws Exception {
      Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      WebDriver driver = buildDriver(cf);

      GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
              .withInsPersonOrCompany(ContactSubType.Person)
              .withProductType(ProductLineType.Businessowners)
              .withBusinessownersLine(new PolicyBusinessownersLine(BusinessownersLine.SmallBusinessType.Apartments))
              .withLineSelection(LineSelection.Businessowners)
              .withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -20))
			 .build(GeneratePolicyType.PolicyIssued);
      
      new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(),
    		  myPolicyObj.underwriterInfo.getUnderwriterPassword(), myPolicyObj.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("first policy Change",myPolicyObj.squire.getEffectiveDate());
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
		Assert.assertTrue(uwIssues.isInList(UnderwriterIssues_BOP.BOPTenDayBindingAuthority.getLongDescription()).equals(UnderwriterIssueType.AlreadyApproved),
				"Expected Blocking Submit : " + UnderwriterIssues_BOP.BOPTenDayBindingAuthority.getShortDescription() + " is not displayed");
		
  }
}
