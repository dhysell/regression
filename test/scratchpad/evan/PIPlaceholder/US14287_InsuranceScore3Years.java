package scratchpad.evan.PIPlaceholder;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import services.broker.objects.lexisnexis.cbr.response.actual.InquirySubjectType.Vehicle;
import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.activity.UWActivityPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/202227806928">US14287 MOVE TO F103? Order Insurance Score every 3 years</a>
 * @Description
*If hear from Shilynn that this will need to be done sooner than during PI 2 then the team will discuss when can get completed. NOT NECESSARY, CONTINUE WITH 3 YEAR PLAN. 
This has been moved into PI2 as there are issues with the production PolicyCenter renewals. They are creating an activity. This story will clean that up.

We will order Insurance Score every 3 years unless UW manually orders it. We need to look at the last ordered date to calculate the 3 years.

When testing this, we should make sure that the activity is only created every 3rd year and that the activity goes to the UW - what activity?          

Steps:

    Have inforce policy 
    Initiate Renewal job
    Enter all needed information 
    Go to insurance score wizard
    After verifying year one insurance score move policy through years 2 and 3 and 4.

Acceptance Criteria:

    Ensure that an activity is created for UW to order a new report if insurance score is older than 3 years.
    Ensure that only the underwriter is able to order insurance score manually. 
    Ensure that while renewal of the policy, insurance score remain static and is not enhanced (due to age, stability or legal adjustments). 
    Ensure that the policy modifier for the insurance score remains the same at renewal. unless new insurance score is ordered or fetched. 
    Ensure that years 1, 2 and 4 do not order a new score. Only year 3.

NOTE FOR BRENDA - find out from Darrel if we are going to continue using score by batch or individual order.
 

NOTE - BRENDA TO LOOK AT F103 - address this feature during 2.3 - might be able to complete this feature??  Cannot complete in PI2. Could definitely finish F103 in PI3
 * @DATE July 26, 2018
 */

//@Test(groups= {"ClockMove"})
public class US14287_InsuranceScore3Years extends BaseTest {

    public GeneratePolicy myPolicyObject = null;
    private WebDriver driver;


    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObject = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPolOrgType(OrganizationType.Individual)
					.withDownPaymentType(PaymentType.Cash)
                .withInsFirstLastName("ScopeCreeps", "NonFeature")
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(enabled = true)
    public void INSERTDESCRIPTIVETESTNAMEHERE() {

        try {
            generatePolicy();
        } catch (Exception e) {
            e.printStackTrace();
			Assert.fail("Unable to generate policy. Test cannot continue.");
        }

        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);

		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderModifiers pcModifiersPage = new GenericWorkorderModifiers(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		GenericWorkorderRiskAnalysis pcRiskPage = new GenericWorkorderRiskAnalysis(driver);		
		GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages pcPropertyDetailCoveragesPage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
		GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);	
		Vehicle myVehicle = new Vehicle();
		SoftAssert softAssert = new SoftAssert();
		PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
		StartCancellation pcCancellationPage = new StartCancellation(driver);
		StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		GenericWorkorderPolicyInfo pcPolicyInfoPage = new GenericWorkorderPolicyInfo(driver);
		GenericWorkorderSquirePropertyDetail pcPropertyPage = new GenericWorkorderSquirePropertyDetail(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails pcProtectionDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver);
		GenericWorkorderSquirePropertyCoverages pcSquirePropertyCoveragesPage = new GenericWorkorderSquirePropertyCoverages(driver);
		BasePage basePage = new BasePage(driver);
		UWActivityPC activity = new UWActivityPC(driver);
		GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
		
		
		
		// Acceptance criteria: 
        Assert.assertTrue(true, "Message");
    }


}




















