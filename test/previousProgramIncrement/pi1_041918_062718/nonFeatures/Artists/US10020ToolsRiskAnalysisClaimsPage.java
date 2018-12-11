	package previousProgramIncrement.pi1_041918_062718.nonFeatures.Artists;

	import com.idfbins.driver.BaseTest;
	import repository.driverConfiguration.Config;
	import repository.gw.activity.ActivityPopup;
	import repository.gw.enums.BusinessownersLine.SmallBusinessType;
	import repository.gw.enums.ContactSubType;
	import repository.gw.enums.CreateNew;
	import repository.gw.enums.GeneratePolicyType;
	import repository.gw.enums.LineSelection;
	import repository.gw.enums.OrganizationType;
	import repository.gw.enums.PaymentPlanType;
	import repository.gw.enums.PaymentType;
	import repository.gw.generate.GeneratePolicy;
	import repository.gw.generate.custom.PolicyBusinessownersLine;
	import repository.gw.login.Login;
	import gwclockhelpers.ApplicationOrCenter;
	import org.openqa.selenium.WebDriver;
	import org.testng.Assert;
	import org.testng.annotations.Test;
	import repository.pc.account.AccountSummaryPC;
	import repository.pc.sidemenu.SideMenuPC;
	import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
	import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_Claims;

/**
* @Author nvadlamudi
* @Requirement : US10020: Standardize the appearance of the Risk Analysis Claims search
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764d/detail/userstory/80419948992"></a>
* @Description: validating Riskanalysis - Claims Search policy from job and Tools -> risk Analysis 
* @DATE Jun 11, 2018
*/
public class US10020ToolsRiskAnalysisClaimsPage extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myQQPolObj;

	@Test
	public void testCheckToolsRiskAnalysisClaimsTab() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myQQPolObj = new GeneratePolicy.Builder(driver).withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
				.withCreateNew(CreateNew.Create_New_Always).withInsCompanyName("US10020 BOP")
				.withPaymentPlanType(PaymentPlanType.getRandom()).withDownPaymentType(PaymentType.getRandom())
				.withLineSelection(LineSelection.Businessowners).build(GeneratePolicyType.PolicySubmitted);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(myQQPolObj.underwriterInfo.getUnderwriterUserName(),myQQPolObj.underwriterInfo.getUnderwriterPassword(), myQQPolObj.accountNumber);
		AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickActivitySubject("Submitted Full Application");
		ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.setUWIssuanceActivity();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.clickClaimsCardTab();	
		GenericWorkorderRiskAnalysis_Claims riskClaims = new GenericWorkorderRiskAnalysis_Claims(driver);
		
		Assert.assertTrue(riskClaims.checkClaimsSearchByRelatedPolicy(), "Risk Analysis - Claims - Search By Related Policy field is not displayed.");
		riskClaims.setClaimsSearchByRelatedPolicy(myQQPolObj.busOwnLine.getPolicyNumber());
		riskClaims.clickGenericWorkorderSaveDraft();
		
		sideMenu.clickSideMenuToolsRiskAnalysis();
		risk.clickClaimsCardTab();	
		Assert.assertTrue(riskClaims.checkClaimsSearchByRelatedPolicy(), "Risk Analysis - Claims - Search By Related Policy field is not displayed.");
		riskClaims.setClaimsSearchByRelatedPolicy(myQQPolObj.busOwnLine.getPolicyNumber());
		riskClaims.clickGenericWorkorderSaveDraft();
		
		
	}
}
