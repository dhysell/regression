package previousProgramIncrement.pi1_041918_062718.nonFeatures.Scope_Creeps;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

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
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderVehicles;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.rewrite.StartRewrite;

/**
 * @Author ecoleman
 * @Requirement 
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/221148922668">US15158</a>
 * @Description 
As a transition team lead or underwriting supervisor, I need to be the one special approving a policy with no cars listed on section III, only an 80 class trailer provided the car is on a commercial policy or other underwriter approval, on more job types than this currently works on so that this functionality is consistent. 

Requirements:

Steps to get there:

    Create a policy with section III 
     Add only a class 80 trailer, no passenger vehicles 

Acceptance criteria:

    Ensure that a special approve(AU012) for UW triggers when quoted on new submission
    Ensure that AU012 triggers at Renewal, Rewrite New Term & Rewrite Full Term when the only vehicle on policy already is just one trailer.
    Ensure that AU012 triggers at Renewal, Rewrite New Term & Rewrite Full Term when on the job removing an existing passenger vehicle leaving only a trailer (class 088).
    Ensure that AU012 triggers Renewal, Rewrite New Term & Rewrite Full Term. Before jobs have policy with passenger auto, trailer and a farm truck. On the job remove all vehicles except the trailer. Should still see AU012 trigger.
    Ensure that AU012 triggers on a policy change when change leaves only a trailer inforce on the policy
    Test all LOB applicable (city, country, F&R)

See also: US14708

rewrite new term
rewrite full term 

Note: ARTists team disabled all UW issues for Transition Renewals, so TRs won't be testable
 * @DATE May 31, 2018
 */

public class US15158_AU012TrailerExpanded extends BaseTest {
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
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
		GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);	
		Vehicle myVehicle = new Vehicle();
		GenericWorkorderQuote pcQuoteWorkorder = new GenericWorkorderQuote(driver);

		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuPAVehicles();
//		myVehicle.setComprehensive(true);
		myVehicle.setVehicleTypePL(VehicleTypePL.Trailer);
		myVehicle.setTrailerType(TrailerTypePL.Camper);		
		pcVehicleWorkorder.editVehicleByType(VehicleTypePL.PrivatePassenger);//.createVehicleManually();
		pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
		pcWorkorderVehicleDetails.fillOutVehicleDetails_FA(myVehicle);
	    pcWorkorderVehicleDetails.clickOK();		
		
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcQuoteWorkorder.clickPreQuoteDetails();
		
		// @revision 7/26/18: button no longer special approve
		List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
		Assert.assertTrue(uwIssues.contains("AUTO: no Private Passenger or Passenger Pickup vehicles.  Underwriting approval is required to issue.(AU012)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	}
	
	@Test(enabled = true)
	public void VerifyAU042ValidationOnPolicyChangeRemovePassengerVehicleOnJob() throws Exception {
		
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
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
		GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);	
		StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
		GenericWorkorderQuote pcQuoteWorkorder = new GenericWorkorderQuote(driver);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		Vehicle myVehicle = new Vehicle();
		
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
		policyChangePage.startPolicyChange("Change#1 Remove location", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		
		pcSideMenu.clickSideMenuPAVehicles(); 
		pcVehicleWorkorder.removeVehicleByRowNumber(1);
		myVehicle.setVehicleTypePL(VehicleTypePL.Trailer);
		myVehicle.setTrailerType(TrailerTypePL.Camper);		
		pcVehicleWorkorder.createVehicleManually();
		pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
		pcWorkorderVehicleDetails.fillOutVehicleDetails_FA(myVehicle);
	    pcWorkorderVehicleDetails.clickOK();
	    
	    pcRewriteWorkOrder.visitAllPages(myPolicyObject); // 5/23 CHECK THIS STEP!
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcQuoteWorkorder.clickPreQuoteDetails();
		
		// @revision 7/26/18: button no longer special approve
		List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
		Assert.assertTrue(uwIssues.contains("AUTO: no Private Passenger or Passenger Pickup vehicles.  Underwriting approval is required to issue.(AU012)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	}

	@Test(enabled = true)
	public void VerifyAU042ValidationOnRenewal() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		myPolicyObject = new GeneratePolicy.Builder(driver)
			.withProductType(ProductLineType.Squire)
			.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -300))
			.withSquireEligibility(SquireEligibility.FarmAndRanch)
			.withDownPaymentType(PaymentType.Cash)
			.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
			.withPolOrgType(OrganizationType.Individual)
			.withInsFirstLastName("Scope", "Creeps")
			.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
		GenericWorkorderVehicles_Details pcWorkorderVehicleDetails = new GenericWorkorderVehicles_Details(driver);	
		Vehicle myVehicle = new Vehicle();
		PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
			
		
		pcAccountSummaryPage.clickAccountSummaryPolicyTermByStatus(PolicyTermStatus.InForce);
		pcPolicyMenu.clickRenewPolicy();
		
		new PolicyPreRenewalDirection(driver).closePreRenewalExplanations(myPolicyObject);
		new PolicySummary(driver).clickPendingTransaction(TransactionType.Renewal);
		pcSideMenu.clickSideMenuPolicyInfo();
		
		pcSideMenu.clickPolicyContractSectionIIIAutoLine();
		pcSideMenu.clickSideMenuPACoverages();
		GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
		gwHelpers.editPolicyTransaction();
		
		//To handle the strange exception we see.
		if (gwHelpers.errorMessagesExist()) {
			if (gwHelpers.getFirstErrorMessage().contains("The object you are trying to update was changed by another user.")) {
				gwHelpers.clickDiscardUnsavedChangesLink();
			} else {
				Assert.fail("There was an unexpected error. The error was: " + gwHelpers.getFirstErrorMessage());
			}
		}

		pcSideMenu.clickSideMenuPAVehicles();
		pcVehicleWorkorder.removeVehicleByRowNumber(1);
		myVehicle.setVehicleTypePL(VehicleTypePL.Trailer);
		myVehicle.setTrailerType(TrailerTypePL.Camper);
		pcVehicleWorkorder.createVehicleManually();
		pcWorkorderVehicleDetails.fillOutVehicleDetails_QQ(true, myVehicle);
		pcWorkorderVehicleDetails.fillOutVehicleDetails_FA(myVehicle);
	    pcWorkorderVehicleDetails.clickOK();
//		pcRewriteWorkOrder.visitAllPages(myPolicyObject);
		pcWorkOrder.clickGenericWorkorderQuote();
		pcSideMenu.clickSideMenuRiskAnalysis();

		// @revision 7/26/18: button no longer special approve
		List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
		Assert.assertTrue(uwIssues.contains("AUTO: no Private Passenger or Passenger Pickup vehicles.  Underwriting approval is required to issue.(AU012)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	}
	
	@Test(enabled = true)
	public void VerifyAU042ValidationOnRewriteFullTermRemovePassengerVehicleOnJob() throws Exception {
		
		Vehicle myVehicle = new Vehicle();
		Squire mySquire = new Squire();
		
		myVehicle.setVehicleTypePL(VehicleTypePL.Trailer);
		myVehicle.setTrailerType(TrailerTypePL.Camper);			
		mySquire.squirePA.addToVehiclesList(myVehicle);
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObject = new GeneratePolicy.Builder(driver)
			.withProductType(ProductLineType.Squire)
			.withSquire(mySquire)
			.withSquireEligibility(SquireEligibility.FarmAndRanch)
			.withDownPaymentType(PaymentType.Cash)
			.withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
			.withPolOrgType(OrganizationType.Individual)
			.withInsFirstLastName("Scope", "Creeps")
			.build(GeneratePolicyType.PolicyIssued);	
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);
		
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		GenericWorkorderVehicles pcVehicleWorkorder = new GenericWorkorderVehicles(driver);
		GenericWorkorderComplete pcWorkorderCompletePage = new GenericWorkorderComplete(driver);
		PolicyMenu pcPolicyMenu = new PolicyMenu(driver);
		StartCancellation pcCancellationPage = new StartCancellation(driver);
		StartRewrite pcRewriteWorkOrder = new StartRewrite(driver);
		GenericWorkorderQuote pcQuoteWorkorder = new GenericWorkorderQuote(driver);
		
		
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
		pcVehicleWorkorder.removeVehicleByRowNumber(1);
	    
	    pcRewriteWorkOrder.visitAllPages(myPolicyObject); // 5/23 CHECK THIS STEP!
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcQuoteWorkorder.clickPreQuoteDetails();
		
		// @revision 7/26/18: button no longer special approve
		List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
		Assert.assertTrue(uwIssues.contains("AUTO: no Private Passenger or Passenger Pickup vehicles.  Underwriting approval is required to issue.(AU012)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	}
}











