package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.SquireUmbrellaIncreasedLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquireUmbrellaInfo;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
* @Author nvadlamudi
* @Requirement : US15614: Enable auto-issue on Umbrella
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Personal%20Lines%20-%20Common/PC8%20-%20Personal%20Lines%20-%20Auto%20Issuance.xlsx">PC8 - Personal Lines - Auto Issuance</a>
* @Description: Validate agent can auto issue a umbrella policy 
* @DATE Aug 17, 2018
*/
public class US15614UmbrellaAgentAutoIssueNewBusiness extends BaseTest {
	private String activityName = "Auto-Issued Submission for UW Review";
	private WebDriver driver;
	
	@Test
	public void testCheckingUmbrellaAgentAutoIssueInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		SquireLiability liabilitySection = new SquireLiability();
		liabilitySection.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
		
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.CSL300K,
				MedicalLimit.TenK, true, UninsuredLimit.CSL300K, false, UnderinsuredLimit.CSL300K);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		
		Squire mySquire = new Squire(new GuidewireHelpers(driver).getRandomEligibility());
		mySquire.propertyAndLiability.liabilitySection = liabilitySection;
		mySquire.squirePA = squirePersonalAuto;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.Membership)
				.withInsFirstLastName("Umbrella", "Rules")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		SquireUmbrellaInfo squireUmbrellaInfo = new SquireUmbrellaInfo();
		squireUmbrellaInfo.setIncreasedLimit(SquireUmbrellaIncreasedLimit.Limit_1000000);

		myPolicyObjPL.squireUmbrellaInfo = squireUmbrellaInfo;
		myPolicyObjPL.addLineOfBusiness(ProductLineType.PersonalUmbrella, GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolicyObjPL);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(),"Umbrella - Auto Issuance - Agent can't have Submit Options to submit a policy..");
		Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(),"Umbrella - Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
		boolean checkActivity =  uwIssues.getInformationalList().size() > 0;
		//Adding the below loop for checking the ClaimCenter Down - UW Issues -- Test failed in REGR environments because of this
		for(UnderwriterIssue currentUW :uwIssues.getInformationalList()){
			if(currentUW.getShortDescription().equalsIgnoreCase(PLUWIssues.ClaimCenterDown.getShortDesc())){
				checkActivity =  uwIssues.getInformationalList().size() > 1;
			}
		}
		payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
			
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance),
				"Umbrella - Agent Auto Issue  - completed Issuance job not found.");

		if(checkActivity){
			Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed.");
		}
	}
}
