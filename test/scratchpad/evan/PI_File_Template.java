package scratchpad.evan;

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
 * @RequirementsLink <a href=""></a>
 * @Description ~~RALLY TEXT HERE~~
 * @DATE August 2, 2018
 */

//@Test(groups= {"ClockMove"})
public class PI_File_Template extends BaseTest {

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
    public void INSERTDESCRIPTIVETESTNAMEHERE() throws Exception {

            generatePolicy();


        new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
                myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);
        
//      @Testing config
//    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//      driver = buildDriver(cf);
//      new Login(driver).loginAndSearchPolicyByAccountNumber("su", "gw", "277512");
        
        

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