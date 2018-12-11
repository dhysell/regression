package repository.gw.generate;


import org.openqa.selenium.By;
import org.testng.Assert;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.*;

import java.util.ArrayList;

public class QuickBuildBOP {

	
	public void quickBuildBOP(GeneratePolicy policy) throws Exception {
		
		
		new FieldIntegrityAndDefaults().checkClassFieldIntegrityAndDefaults(policy);
		Underwriters underwriter = UnderwritersHelper.getRandomUnderwriter(UnderwriterLine.Commercial);
		new Login(policy.getWebDriver()).login(underwriter.underwriterUserName, underwriter.underwriterPassword);
		policy.startNewSubmission();
		policy.selectProductAndFillOutQualificationsPage();
		new GenericWorkorderQualification(policy.getWebDriver()).fillOutFullAppQualifications(policy);
		new GenericWorkorderPolicyInfo(policy.getWebDriver()).fillOutPolicyInfoPage(policy);
		new GenericWorkorderBusinessownersLine(policy.getWebDriver()).fillOutBusinessownersLinePages(false, policy.busOwnLine);
		if(!new GenericWorkorder(policy.getWebDriver()).finds(By.xpath("//span[contains(@id, 'WebMessageWorksheet:WebMessageWorksheetScreen:WebMessageWorksheet_ClearButton-btnEl')]")).isEmpty()) {
			new GenericWorkorder(policy.getWebDriver()).clickClear();
		}
		new GenericWorkorderLocations(policy.getWebDriver()).fillOutBOPLocation_QQ(policy);
	
		new GenericWorkorderBuildings(policy.getWebDriver()).fillOutBOPBuildings_QQ(policy);
	
		new GenericWorkorderSupplemental(policy.getWebDriver()).handleSupplementalQuestions2(policy.busOwnLine.locationList, false, false, policy);
		new SideMenuPC(policy.getWebDriver()).clickSideMenuModifiers();
		policy.fillOutPayerAssignment();
		new SideMenuPC(policy.getWebDriver()).clickSideMenuBuildings();
		new SideMenuPC(policy.getWebDriver()).clickSideMenuLocations();
		new GenericWorkorderRiskAnalysis(policy.getWebDriver()).performRiskAnalysisAndQuote(policy);
		new SideMenuPC(policy.getWebDriver()).clickSideMenuQuote();
		new SideMenuPC(policy.getWebDriver()).clickSideMenuForms();
		new SideMenuPC(policy.getWebDriver()).clickSideMenuPayment();
		GenericWorkorderPayment paymentPage = new GenericWorkorderPayment(policy.getWebDriver());
		new GuidewireHelpers(policy.getWebDriver()).getPolicyPremium(policy).setInsuredPremium(paymentPage.getTotalInsuredPremiumPortion());
		new GuidewireHelpers(policy.getWebDriver()).getPolicyPremium(policy).setTotalAdditionalInterestPremium(paymentPage.getAdditionalInterestPremiumPortion(policy.getLocationList(policy)));
		new GenericWorkorderPayment(policy.getWebDriver()).fillOutPaymentPage(policy);
		new GenericWorkorderPayment(policy.getWebDriver()).clickGenericWorkorderSubmitOptionsSubmitOnly();
		new InfoBar(policy.getWebDriver()).clickInfoBarAccountNumber();
		ArrayList<String> foo = new AccountSummaryPC(policy.getWebDriver()).getActivityAssignedTo("Submitted Full Application");
		if(foo.isEmpty()) {
			Assert.fail("getActivityAssignedTo() WAS NOT FOUND. THERE FOR CANNNOT GET ASSIGNED UNDERWRITER. **PRIOR ISSUES WAS THE POLICY DID NOT GET ISSUED**");
		}
		policy.underwriterInfo = UnderwritersHelper.getUnderwriterInfoByFullName(foo.get(0));
		new GuidewireHelpers(policy.getWebDriver()).logout();
		policy.policyIssuedGuts();
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
