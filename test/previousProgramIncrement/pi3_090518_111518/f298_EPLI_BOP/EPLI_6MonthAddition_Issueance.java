package previousProgramIncrement.pi3_090518_111518.f298_EPLI_BOP;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.UnderwriterIssues_BOP;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSupplemental;
import scratchpad.evan.SideMenuPC;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE THAT USER IS BLOCKED WHEN TRYING TO ADD EPLI WITH LESS THEN 6 MONTHS LEFT ON THE POLICY
* @DATE Sep 20, 2018
*/
public class EPLI_6MonthAddition_Issueance extends BaseTest {
	public SoftAssert softAssert = new SoftAssert();
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;


	@Test
	public void extendedCoverageLetterDeletion() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine()
				.withPolTermLengthDays(160)
				.isDraft()
				.build(GeneratePolicyType.FullApp);

		new Login(driver).loginAndSearchSubmission(myPolicyObj);
		new SideMenuPC(driver).clickSideMenuRiskAnalysis();
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
			new GenericWorkorderQuote(driver).clickPreQuoteDetails();
		}
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
		softAssert.assertTrue(uwIssues.isInList_ShortDescription(UnderwriterIssues_BOP.BOPBR19.getShortDescription()).equals(UnderwriterIssues_BOP.BOPBR19.getIssuetype()), UnderwriterIssues_BOP.BOPBR19.getShortDescription() + " DID NOT FIRE AS EXPECTED ON A LESS THAN 6 MONTH POLICY SUBMISSION");

		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(false);
		new GuidewireHelpers(driver).editPolicyTransaction();
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();

		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(driver);
		new GuidewireHelpers(driver).getPolicyPremium(myPolicyObj).setInsuredPremium(paymentPage.getTotalInsuredPremiumPortion());
		new GuidewireHelpers(driver).getPolicyPremium(myPolicyObj).setTotalAdditionalInterestPremium(paymentPage.getAdditionalInterestPremiumPortion(myPolicyObj.getLocationList(myPolicyObj)));
		//This section is to get the membership dues amount for BOP policies only. This amount does not show up on the quote page as expected for the time being, but is here.
		//So, we need to get it from here.
		if (myPolicyObj.productType.equals(ProductLineType.Businessowners)) {
			new GuidewireHelpers(driver).getPolicyPremium(myPolicyObj).setMembershipDuesAmount(paymentPage.getMembershipDuesAmount());
		}
		sideMenu.clickSideMenuQuote();
		new GuidewireHelpers(driver).logout();
		myPolicyObj.convertTo(driver, GeneratePolicyType.PolicySubmitted);
		
		new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.underwriterInfo.getUnderwriterUserName(), myPolicyObj.underwriterInfo.underwriterPassword, myPolicyObj.accountNumber);
		new AccountSummaryPC(driver).clickActivitySubject("Submitted Full Application");
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance(true);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new SideMenuPC(driver).clickSideMenuSupplemental();
		new GenericWorkorderSupplemental(driver).handleSupplementalQuestions2(myPolicyObj.busOwnLine.locationList, false, false, myPolicyObj);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();
		if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
			new GenericWorkorderQuote(driver).clickPreQuoteDetails();
		}
		uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
		softAssert.assertTrue(uwIssues.isInList_ShortDescription(UnderwriterIssues_BOP.BOPBR19.getShortDescription()).equals(UnderwriterIssues_BOP.BOPBR19.getIssuetype()), UnderwriterIssues_BOP.BOPBR19.getShortDescription() + " DID NOT FIRE AS EXPECTED ON A LESS THAN 6 MONTH POLICY ISSUANCE");
//
//		new GenericWorkorder(driver).clickGenericWorkorderIssue(IssuanceType.NoActionRequired);
//		if(new GenericWorkorderQuote(driver).isPreQuoteDisplayed()) {
//			new GenericWorkorderQuote(driver).clickPreQuoteDetails();
//			new GuidewireHelpers(driver).logout();
//			new Login(driver).loginAndSearchAccountByAccountNumber("su", "gw", myPolicyObj.accountNumber);
//			new AccountSummaryPC(driver).clickAccountSummaryPendingTransactionByProduct(ProductLineType.Businessowners);
//			new SideMenuPC(driver).clickSideMenuRiskAnalysis();
//			new GenericWorkorderRiskAnalysis_UWIssues(driver).specialApproveAll();
//			new GuidewireHelpers(driver).logout();
//			new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObj.underwriterInfo.underwriterUserName, myPolicyObj.underwriterInfo.underwriterPassword, myPolicyObj.accountNumber);
//			new AccountSummaryPC(driver).clickAccountSummaryPendingTransactionByProduct(ProductLineType.Businessowners);
//			new GenericWorkorder(driver).clickGenericWorkorderIssue(IssuanceType.NoActionRequired);
//		}
//		
//		new GuidewireHelpers(driver).logout();
//		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
//		new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "MEH GO TO GEICO THEIR CHEAPER", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 2), true);
//		new GuidewireHelpers(driver).logout();
//		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
//		new StartPolicyChange(driver).startPolicyChange("add exteded coverage", DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, 1));
//		new SideMenuPC(driver).clickSideMenuBusinessownersLine();
//		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).clickBusinessownersLine_AdditionalCoverages();
//		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Supplemental Extended Reporting Period Endorsement IDCW 31 0002"), "Supplemental Extended Reporting Period Endorsement IDCW 31 0002 WAS NOT ELECTABLE ON A POLICY LESS THAN 6 MONTHS WHERE EPLI WAS ADDED.");
//		
//		
//		
//		
		
		softAssert.assertAll();

	}


}
