package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.ValidUntil;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
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
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;


/**
* @Author nvadlamudi
* @Requirement :US16098: Make Submit Options unavailable after user has clicked Issue
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/249478554752">Link Text</a>
* @Description : check Submit options is still available after validations
* @DATE Sep 8, 2018
*/
public class US16098MakeSubmitOptionsUnavailable extends BaseTest {
	
	@Test
	public void testSquireAgentAutoIssueAfterUWApproval() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
						
		Squire mySquire = new Squire(SquireEligibility.City);

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Submit", "Change")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.isDraft()
				.build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		SideMenuPC sideMenu = new SideMenuPC(driver);	
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
	    if (quotePage.isPreQuoteDisplayed()) {
	          quotePage.clickPreQuoteDetails();
	   }	    
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
		search.searchAccountByAccountNumber(myPolicyObjPL.accountNumber);
		AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickActivitySubject("Approval Requested");
		ActivityPopup actPop = new ActivityPopup(driver);
		actPop.clickCompleteButton();
		sideMenu.clickSideMenuRiskAnalysis();		   
		risk_UW.enterValidUntilForSpecificUWIssue(PLUWIssues.CLUEAutoNotOrdered.getShortDesc(), ValidUntil.NextChange.getValue());
		risk_UW.approveAll();
		risk_UW.clickReleaseLock();
		new GuidewireHelpers(driver).logout();

		loginPage = new Login(driver);
		loginPage.login(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword());
		DesktopMyActivitiesPC myActivities = new DesktopMyActivitiesPC(driver);
		myActivities.searchMyActivitiesTableForSpecificSubjectLinkAndClick("Underwriter has reviewed this job", myPolicyObjPL.accountNumber, myPolicyObjPL.pniContact.getFirstName());
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolicyObjPL);
		sideMenu.clickSideMenuForms();
		payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
		if(new GuidewireHelpers(driver).errorMessagesExist()  &&  !new GuidewireHelpers(driver).getFirstErrorMessage().contains("You do not have the permission required to perform this action")){
			Assert.fail("After Policy Issuance, still submit options showing in submission job");
		}
		Assert.assertTrue(!payment.SubmitOptionsButtonExists(), "After selecting submit options , still submit options showing in submission job");
		
	}
}
