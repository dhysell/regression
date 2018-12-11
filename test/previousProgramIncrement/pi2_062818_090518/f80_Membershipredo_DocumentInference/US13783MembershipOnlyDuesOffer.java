package previousProgramIncrement.pi2_062818_090518.f80_Membershipredo_DocumentInference;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
* @Author nvadlamudi
* @Requirement :US13783: Inference - Send Membership dues offer 60 days after insurance policy cancels midterm or at renewal
* @RequirementsLink <a href="http://projects.idfbins.com/thunderhead/Documents/R2%20Thunderhead%20Project/Federation/Renewal%20Documents/Membership%20Renewal%20Requirement.docx">Document Requirements</a>
* @Description: Validating the Membership Renewal document after insurance policy cancels and renewals.
* @DATE Jun 28, 2018
*/
public class US13783MembershipOnlyDuesOffer extends BaseTest{
	private static final String membershipRenew = "Membership Renewal";
	private WebDriver driver;
	private GeneratePolicy mySqPolObj;
  
	/**Description: If a Membership Only policy is canceled the document must not infer.
	    * If a Membership Only policy is not renewed/not taken (regardless of when it is done), the document must not infer. 
	    */

	@Test
	public void testMemberShipOnlyCancelsRenewal() throws Exception {
		  Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	      driver = buildDriver(cf);
	      Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -360);

	      mySqPolObj = new GeneratePolicy.Builder(driver)
	    		  .withProductType(ProductLineType.Membership)
	    		  .withInsFirstLastName("Membership", "Dues")
	    		  .withPolEffectiveDate(newEff)
	              .withPaymentPlanType(PaymentPlanType.Annual)
	              .withDownPaymentType(PaymentType.Cash)
	              .build(GeneratePolicyType.PolicyIssued);
	      
	      cf = new Config(ApplicationOrCenter.PolicyCenter);
	      driver = buildDriver(cf);
	      Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
	      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.accountNumber);

	      StartCancellation cancelPol = new StartCancellation(driver);
	      cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null, mySqPolObj.squire.getEffectiveDate(), true, 43.00);
	      GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
	      new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());	  
	      submittedPage.clickViewYourPolicy();      
	      checkDocument("Cancel", false);
	      new GuidewireHelpers(driver).logout();
	      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.accountNumber);
	      PolicyMenu policyMenu = new PolicyMenu(driver);
	      policyMenu.clickMenuActions();
	      policyMenu.clickReinstatePolicy();
	      StartReinstate reinstatePolicy = new StartReinstate(driver);
		  reinstatePolicy.setReinstateReason("Payment received");
	      cancelPol = new StartCancellation(driver);
	      reinstatePolicy.quoteAndIssue();
	      submittedPage = new GenericWorkorderComplete(driver);
		  new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
		  submittedPage.clickPolicyNumber();      
	      StartRenewal renewal = new StartRenewal(driver);
	      renewal.startRenewal();    
	      InfoBar infoBar = new InfoBar(driver);
	      infoBar.clickInfoBarPolicyNumber();
	      cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null, mySqPolObj.squire.getEffectiveDate(), true, 43.00);
	      submittedPage = new GenericWorkorderComplete(driver);
	      new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());	  
	      submittedPage.clickViewYourPolicy();      
	      checkDocument("Renew", false);
	      new GuidewireHelpers(driver).logout();  
	  }
	  
	  private void checkDocument(String job, boolean documentFound) throws Exception{	 
			SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuToolsDocuments();
			PolicyDocuments docs = new PolicyDocuments(driver);
			docs.selectRelatedTo(job);
			docs.setDocumentDescription(membershipRenew);
			docs.clickSearch();
			ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
			boolean docFound = false;
			for (String desc : descriptions) {
				if (desc.equals(membershipRenew)) {
					docFound = true;
					break;
				}
			}
		
			if(documentFound){
				Assert.assertTrue(docFound, "Policy Number: '" + mySqPolObj.squire.getPolicyNumber() + "' - Document '"
						+ membershipRenew + "' is not displayed for Job :" +job);
			}else{
				Assert.assertTrue(!docFound, "Policy Number: '" + mySqPolObj.squire.getPolicyNumber() + "' - Document '"
						+ membershipRenew + "' is displayed for Job :" +job);
			}		
	  }
}
