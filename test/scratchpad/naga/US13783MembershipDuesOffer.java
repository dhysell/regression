package scratchpad.naga;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

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
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.reinstate.StartReinstate;
import repository.pc.workorders.renewal.NonRenewalDataEntry;
import repository.pc.workorders.renewal.StartRenewal;
import repository.pc.workorders.rewrite.StartRewrite;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :US13783: Inference - Send Membership dues offer 60 days after insurance policy cancels midterm or at renewal
* @RequirementsLink <a href="http://projects.idfbins.com/thunderhead/Documents/R2%20Thunderhead%20Project/Federation/Renewal%20Documents/Membership%20Renewal%20Requirement.docx">Document Requirements</a>
* @Description: Validating the Membership Renewal document after insurance policy cancels and renewals.
* @DATE Jun 28, 2018
*/
@Test(groups = {"ClockMove"})
public class US13783MembershipDuesOffer extends  BaseTest{
	private static final String membershipRenew = "Membership Renewal";
	private WebDriver driver;
	private GeneratePolicy mySqPolObj;
	
/**
* @Description: 1. If the document(s) infer and the policy reinstates, the document(s) is deleted.
* 				2. If an insurance policy is canceled midterm (UW or Non-pay): The document will infer immediately when the cancellation job is scheduled or completed.
* 				3. If there is a flat cancel on a membership on insurance policy after Submission, Rewrite, Reinstate or Renewal: 
*/
  @Test
  public void testCheckMidTermCancelMembershipRenewal() throws Exception {
	  Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
      SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
      squirePersonalAuto.setCoverages(coverages);

      Squire mySquire = new Squire(SquireEligibility.City);
      mySquire.squirePA = squirePersonalAuto;
     
      mySqPolObj = new GeneratePolicy.Builder(driver)
              .withSquire(mySquire)
              .withProductType(ProductLineType.Squire)
              .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
              .withInsPersonOrCompany(ContactSubType.Person)
              .withInsFirstLastName("MembRenewal", "MidTerm")
              .withPolOrgType(OrganizationType.Individual)
              .withPaymentPlanType(PaymentPlanType.Annual)
              .withDownPaymentType(PaymentType.Cash)
              .build(GeneratePolicyType.PolicyIssued);
      
      //Cancel Policy
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);    
      Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.accountNumber);
      StartCancellation cancelPol = new StartCancellation(driver);
      cancelPol.cancelPolicy(CancellationSourceReasonExplanation.NoPaymentReceived, null, mySqPolObj.squire.getEffectiveDate(), true, 200.00);
      GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
      new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());	  
      submittedPage.clickViewYourPolicy();
      checkDocument("Cancel", true);
      new GuidewireHelpers(driver).logout();
      //Reinstate
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
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
	  checkDocument("Cancel", false);
      new GuidewireHelpers(driver).logout();
  }
  /**
  * @Description: 1. If the insurance policy is rewritten (Rewrite Full Term, Rewrite New Term, Rewrite Remainder of Term, Rewrite New Account) before the document(s) is(are) printed the document(s) must be deleted.  
  *
  */
  @Test
  private void testRewritenewTermMembershipRenewal() throws Exception {
	  Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
      SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
      squirePersonalAuto.setCoverages(coverages);
	
      Squire mySquire = new Squire(SquireEligibility.City);
      mySquire.squirePA = squirePersonalAuto;
     
      Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -81);

      mySqPolObj = new GeneratePolicy.Builder(driver)
              .withSquire(mySquire)
              .withProductType(ProductLineType.Squire)
              .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
              .withInsPersonOrCompany(ContactSubType.Person)
              .withInsFirstLastName("MembRenewal", "NewTerm")
              .withPolEffectiveDate(newEff)
              .withPolOrgType(OrganizationType.Individual)
              .withPaymentPlanType(PaymentPlanType.Annual)
              .withDownPaymentType(PaymentType.Cash)
              .build(GeneratePolicyType.PolicyIssued);
      
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
      new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.squire.getPolicyNumber());

      StartCancellation cancelPol = new StartCancellation(driver);
      cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);
      GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
      new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());	  
      submittedPage.clickViewYourPolicy();      
      checkDocument("Cancel", true);
      new GuidewireHelpers(driver).logout();
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.squire.getPolicyNumber());
      new StartRewrite(driver).startRewriteNewTerm();
      new StartRewrite(driver).rewriteNewTermGuts(LineSelection.PersonalAutoLinePL);
      for(int i=0; i<6; i++){
    	  new StartRewrite(driver).clickNext();
      }
      SideMenuPC sideMenu = new SideMenuPC(driver);
      sideMenu.clickSideMenuRiskAnalysis();
      new StartRewrite(driver).clickGenericWorkorderQuote();
      new StartRewrite(driver).clickIssuePolicy();
      submittedPage = new GenericWorkorderComplete(driver);
	  new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
	  submittedPage.clickPolicyNumber();
      checkDocument("Cancel", false);
      new GuidewireHelpers(driver).logout();
      
      
      //Rewrite reminder of the Term
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);

      uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
              Underwriter.UnderwriterTitle.Underwriter);
      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.accountNumber);
      cancelPol = new StartCancellation(driver);

      cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose",
              DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), true);     
      submittedPage = new GenericWorkorderComplete(driver);
	  new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
	  submittedPage.clickPolicyNumber();
      new StartRewrite(driver).rewriteRemainderOfTerm(mySqPolObj);  
      submittedPage = new GenericWorkorderComplete(driver);
	  new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
	  submittedPage.clickPolicyNumber();
      checkDocument("Cancel", false);
      new GuidewireHelpers(driver).logout();
      
  }
  
  /**
  * @Description 1.If an insurance policy is not renewed:
  *               If it is done between day -80 to day -51, the form will be inferred on the current term policy expiration date
  * 			2. If it is done between day -80 to day -51, the form will be inferred on the current term policy expiration date
  */
  @Test
  private void testRenewalsMembershipsNonRenew() throws Exception {
	  Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
      SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
      squirePersonalAuto.setCoverages(coverages);

      Squire mySquire = new Squire(SquireEligibility.City);
      mySquire.squirePA = squirePersonalAuto;
    
      mySqPolObj = new GeneratePolicy.Builder(driver)
              .withSquire(mySquire)
              .withProductType(ProductLineType.Squire)
              .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
              .withInsPersonOrCompany(ContactSubType.Person)
              .withInsFirstLastName("MembRenewal", "Renewal")
              .withPolTermLengthDays(30)
              .withPolOrgType(OrganizationType.Individual)
              .withPaymentPlanType(PaymentPlanType.Annual)
              .withDownPaymentType(PaymentType.Cash)
              .build(GeneratePolicyType.PolicyIssued);
      
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
              Underwriter.UnderwriterTitle.Underwriter_Supervisor);
      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.accountNumber);
      StartRenewal renewal = new StartRenewal(driver);
      renewal.startRenewal();
      InfoBar infoBar = new InfoBar(driver);
      infoBar.clickInfoBarPolicyNumber();

      PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
      preRenewalPage.closePreRenewalExplanations(mySqPolObj);
      PolicySummary policySummary = new PolicySummary(driver);
      policySummary.clickPendingTransaction(TransactionType.Renewal);
      GenericWorkorder generic = new GenericWorkorder(driver);
      generic.clickNonRenew();      
      NonRenewalDataEntry renew = new NonRenewalDataEntry(driver);
      renew.setNonRenewReasonExplanation("Carrier", "non-payment","no payment received");
      GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
	  new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
	  submittedPage.clickPolicyNumber();
	  ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 30);		
	  BatchHelpers batchHelpers = new BatchHelpers(cf);
      batchHelpers.runBatchProcess(BatchProcess.PC_Workflow);	  
	  checkDocument("Renew", true);
      new GuidewireHelpers(driver).logout();
  }
  
  
  /**
  * @Description 1.If it is done on or after day -50, the form will be inferred on the cancellation job when it is scheduled or completed.
  *				If an insurance policy is not-taken:	
  *				2. If it is done on or after day -50, the form will be inferred on the cancellation job when it is scheduled or completed.
  */
  @Test
  private void testRenewalsMembershipsNotTaken() throws Exception {
	  Config cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
      SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
      squirePersonalAuto.setCoverages(coverages);

      Squire mySquire = new Squire(SquireEligibility.City);
      mySquire.squirePA = squirePersonalAuto;
     
      mySqPolObj = new GeneratePolicy.Builder(driver)
              .withSquire(mySquire)
              .withProductType(ProductLineType.Squire)
              .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.Membership)
              .withInsPersonOrCompany(ContactSubType.Person)
              .withInsFirstLastName("MembRenewal", "RenewalLess")
              .withPolTermLengthDays(30)
              .withPolOrgType(OrganizationType.Individual)
              .withPaymentPlanType(PaymentPlanType.Annual)
              .withDownPaymentType(PaymentType.Cash)
              .build(GeneratePolicyType.PolicyIssued);
      
      cf = new Config(ApplicationOrCenter.PolicyCenter);
      driver = buildDriver(cf);
      Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
              Underwriter.UnderwriterTitle.Underwriter_Supervisor);
      new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySqPolObj.accountNumber);
      StartRenewal renewal = new StartRenewal(driver);
      renewal.startRenewal();
      InfoBar infoBar = new InfoBar(driver);
      infoBar.clickInfoBarPolicyNumber();

      PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
      preRenewalPage.closePreRenewalExplanations(mySqPolObj);
      PolicySummary policySummary = new PolicySummary(driver);
      policySummary.clickPendingTransaction(TransactionType.Renewal);
      GenericWorkorder generic = new GenericWorkorder(driver);
      generic.clickNotTaken();
      generic.selectOKOrCancelFromPopup(OkCancel.OK);      
      GenericWorkorderComplete submittedPage = new GenericWorkorderComplete(driver);
	  new GuidewireHelpers(driver).waitUntilElementIsVisible(submittedPage.getJobCompleteTitleBar());
	  submittedPage.clickPolicyNumber();
	  ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 30);	      
	  BatchHelpers batchHelpers = new BatchHelpers(cf);
      batchHelpers.runBatchProcess(BatchProcess.PC_Workflow);
	  checkDocument("Renew", true);
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
