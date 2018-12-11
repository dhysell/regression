package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.PolicyTermStatus;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.gw.helpers.DateUtils;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBlockBind;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author ecoleman - scope creeps
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/222491139920">US15201</a>
 * @Description Production 162745
 * <p>
 * Requirements: Squire - Section III - Product Model (Refer Bus Rules & UW Issues tab - Rule # AU042)
 * <p>
 * Steps to get there:
 * <p>
 * As agent do a F&R policy with a semi-trailer on section III but no farm truck.
 * UW issue AU042 should trigger.
 * <p>
 * Acceptance Criteria:
 * <p>
 * AU042 should be a Special Approve for UW not a hard stop on screen
 * @DATE May 22, 2018
 */

public class US15201_SemiTrailerNoFarmTruck extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObject = null;
	
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
	    myPolicyObject = new GeneratePolicy.Builder(driver)
			.withProductType(ProductLineType.Squire)
			.withSquireEligibility(SquireEligibility.FarmAndRanch)
			.withDownPaymentType(PaymentType.Cash)
			.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
			.withPolOrgType(OrganizationType.Individual)
			.withInsFirstLastName("Scope", "Creeps")
			.build(GeneratePolicyType.PolicyIssued);
	    driver.quit();
	}

@Test(enabled = true)
	public void VerifyAU042ValidationOnNewPolicy() throws Exception {
	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	driver = buildDriver(cf);

    myPolicyObject = new GeneratePolicy.Builder(driver)
		.withProductType(ProductLineType.Squire)
		.withSquireEligibility(SquireEligibility.FarmAndRanch)
		.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
		.withPolOrgType(OrganizationType.Individual)
		.withDownPaymentType(PaymentType.Cash)
		.withInsFirstLastName("Scope", "Creeps")
		.isDraft()
		.build(GeneratePolicyType.FullApp);
    driver.quit();
	
    cf = new Config(ApplicationOrCenter.PolicyCenter);
	driver = buildDriver(cf);
	
	new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

    SideMenuPC pcSideMenu = new SideMenuPC(driver);
    GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
    AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
	GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
    GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
    GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);
	Vehicle myVehicle = new Vehicle();
	SoftAssert softAssert = new SoftAssert();	
	GenericWorkorderBlockBind pcBlockBindPage = new GenericWorkorderBlockBind(driver);

	pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
	pcSideMenu.clickSideMenuPAVehicles();
					
	myVehicle.setVehicleTypePL(VehicleTypePL.SemiTrailer);
	myVehicle.setTrailerType(TrailerTypePL.Semi);
	pcVehicleWorkorder.createVehicleManually();
	pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
    pcWorkorderVehicleDetails.clickOK();		
	
	pcSideMenu.clickSideMenuRiskAnalysis();
	pcWorkOrder.clickGenericWorkorderQuote();
	pcBlockBindPage.clickDetails();
	
//	Acceptance criteria: AU042 should be special approve, not hard stop 
	softAssert.assertTrue(pcRiskUnderwriterIssuePage.checkIfSpecialApproveButtonsExist(true), "Special Approve button was not visible and active, it should be there for No farm truck when you have a semi-trailer (AU042)");
	
	List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
	softAssert.assertTrue(uwIssues.contains("AUTO: A farm truck must exist in policy to add a semi-trailer. Please add farm truck. (AU042)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	
	softAssert.assertAll();
}


@Test(enabled = true)
public void VerifyAU042ValidationOnPolicyChange() throws Exception {
	generatePolicy();
	
	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	driver = buildDriver(cf);
	
	new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

    SideMenuPC pcSideMenu = new SideMenuPC(driver);
    GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
    AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
	GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
    GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
    GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);
	Vehicle myVehicle = new Vehicle();
	SoftAssert softAssert = new SoftAssert();
    StartPolicyChange policyChangePage = new StartPolicyChange(driver);
    GenericWorkorderBlockBind pcBlockBindPage = new GenericWorkorderBlockBind(driver);
	
	
	pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
    policyChangePage.startPolicyChange("Add Semi-Trailer without Farm Truck", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
    
    pcSideMenu.clickSideMenuPAVehicles();					
	myVehicle.setVehicleTypePL(VehicleTypePL.SemiTrailer);
	myVehicle.setTrailerType(TrailerTypePL.Semi);
	pcVehicleWorkorder.createVehicleManually();
	pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
    pcWorkorderVehicleDetails.clickOK();
	pcWorkOrder.clickGenericWorkorderQuote();
    pcSideMenu.clickSideMenuRiskAnalysis();
//	Acceptance criteria: AU042 should be special approve, not hard stop 
	softAssert.assertTrue(pcRiskUnderwriterIssuePage.checkIfSpecialApproveButtonsExist(true), "Special Approve button was not visible and active, it should be there for No farm truck when you have a semi-trailer (AU042)");
	
	List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
	softAssert.assertTrue(uwIssues.contains("AUTO: A farm truck must exist in policy to add a semi-trailer. Please add farm truck. (AU042)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	
	softAssert.assertAll();
}


@Test(enabled = true)
public void VerifyAU042ValidationOnRewriteFullTerm() throws Exception {
	generatePolicy();
	
	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	driver = buildDriver(cf);
	
	new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

    SideMenuPC pcSideMenu = new SideMenuPC(driver);
    GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
    AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
	GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
    GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
    GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);
	Vehicle myVehicle = new Vehicle();
	SoftAssert softAssert = new SoftAssert();
    GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
    PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
    StartCancellation pcCancellationPage = new StartCancellation(driver);
    StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
    GenericWorkorderBlockBind pcBlockBindPage = new GenericWorkorderBlockBind(driver);
	
	
	pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
	pcPolicyMenu.clickMenuActions();
	pcPolicyMenu.clickCancelPolicy();
	pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.Rewritten);
    pcCancellationPage.clickStartCancellation();
	pcCancellationPage.clickSubmitOptionsCancelNow();
	pcWorkorderCompletePage.clickViewYourPolicy();
	
	pcPolicyMenu.clickMenuActions();
	pcPolicyMenu.clickRewriteFullTerm();
	
	pcSideMenu.clickSideMenuPAVehicles(); 
	myVehicle.setVehicleTypePL(VehicleTypePL.SemiTrailer);
	myVehicle.setTrailerType(TrailerTypePL.Semi);
	pcVehicleWorkorder.createVehicleManually();
	pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
    pcWorkorderVehicleDetails.clickOK();
    
    pcRewriteWorkOrder.visitAllPages(myPolicyObject); // 5/23 CHECK THIS STEP!
	pcWorkOrder.clickGenericWorkorderQuote();
    pcSideMenu.clickSideMenuRiskAnalysis();
//	Acceptance criteria: AU042 should be special approve, not hard stop 
	softAssert.assertTrue(pcRiskUnderwriterIssuePage.checkIfSpecialApproveButtonsExist(true), "Special Approve button was not visible and active, it should be there for No farm truck when you have a semi-trailer (AU042)");
	
	List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
	softAssert.assertTrue(uwIssues.contains("AUTO: A farm truck must exist in policy to add a semi-trailer. Please add farm truck. (AU042)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	
	softAssert.assertAll();
}

@Test(enabled = true)
public void VerifyAU042ValidationOnRewriteNewTerm() throws Exception {
	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	driver = buildDriver(cf);
    myPolicyObject = new GeneratePolicy.Builder(driver)
		.withProductType(ProductLineType.Squire)
//		RNT needs to be >= 30 days, so moving inception date 40 days back here
		.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -40))
		.withSquireEligibility(SquireEligibility.FarmAndRanch)
		.withDownPaymentType(PaymentType.Cash)
		.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
		.withPolOrgType(OrganizationType.Individual)
		.withInsFirstLastName("Scope", "Creeps")
		.build(GeneratePolicyType.PolicyIssued);
    driver.quit();
	
    cf = new Config(ApplicationOrCenter.PolicyCenter);
	driver = buildDriver(cf);
	
	new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

    SideMenuPC pcSideMenu = new SideMenuPC(driver);
    GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
    AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
	GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
    GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
    GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);
	Vehicle myVehicle = new Vehicle();
	SoftAssert softAssert = new SoftAssert();
    GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
    PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
    StartCancellation pcCancellationPage = new StartCancellation(driver);
	GenericWorkorderQualification pcQualificationPage = new GenericWorkorderQualification(driver);
    StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
    GenericWorkorderBlockBind pcBlockBindPage = new GenericWorkorderBlockBind(driver);
		
	
	pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
	pcPolicyMenu.clickMenuActions();
	pcPolicyMenu.clickCancelPolicy();
	pcCancellationPage.setSourceReasonAndExplanation(Cancellation.CancellationSourceReasonExplanation.NameChange);
    pcCancellationPage.clickStartCancellation();
	pcCancellationPage.clickSubmitOptionsCancelNow();
	pcWorkorderCompletePage.clickViewYourPolicy();
	
	pcPolicyMenu.clickMenuActions();
	pcPolicyMenu.clickRewriteNewTerm();
	pcRewriteWorkOrder.clickNext();
	
	pcSideMenu.clickSideMenuQualification();
	pcQualificationPage.fillOutFullAppQualifications(myPolicyObject);
	
	pcSideMenu.clickSideMenuPAVehicles(); 
	myVehicle.setVehicleTypePL(VehicleTypePL.SemiTrailer);
	myVehicle.setTrailerType(TrailerTypePL.Semi);
	pcVehicleWorkorder.createVehicleManually();
	pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
    pcWorkorderVehicleDetails.clickOK();
	
	pcRewriteWorkOrder.visitAllPages(myPolicyObject);
	pcWorkOrder.clickGenericWorkorderQuote();
    pcSideMenu.clickSideMenuRiskAnalysis();
//	Acceptance criteria: AU042 should be special approve, not hard stop 
	softAssert.assertTrue(pcRiskUnderwriterIssuePage.checkIfSpecialApproveButtonsExist(true), "Special Approve button was not visible and active, it should be there for No farm truck when you have a semi-trailer (AU042)");
	
	List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
	softAssert.assertTrue(uwIssues.contains("AUTO: A farm truck must exist in policy to add a semi-trailer. Please add farm truck. (AU042)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	
	softAssert.assertAll();
}
}

