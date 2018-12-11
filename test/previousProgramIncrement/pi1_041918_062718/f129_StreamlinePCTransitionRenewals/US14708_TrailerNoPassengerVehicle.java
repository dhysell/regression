package previousProgramIncrement.pi1_041918_062718.f129_StreamlinePCTransitionRenewals;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.TrailerTypePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBlockBind;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author ecoleman - scope creeps
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/212400113788">US14708</a>
 * @Description As a transition team lead or underwriting supervisor, I need to be the one special approving a policy with no cars listed on section III, only an 80 class trailer provided the car is on a commercial policy
 * <p>
 * Requirements: Squire - Section III - Product Model (Refer Bus Rules & UW Issues tab - Rule # AU012)
 * <p>
 * Steps to get there:
 * <p>
 * Create a policy with section III
 * Add only a class 80 trailer, no passenger vehicles
 * <p>
 * Acceptance Criteria
 * <p>
 * Can quote with no car on the policy with no block quote triggering.
 * Block issuance requiring special approve by an underwriting supervisor or transition team lead. (AU012 needs to be special approval)
 * <p>
 * <p>
 * Policy: 01 112090 01
 * @DATE May 11, 2018
 */
public class US14708_TrailerNoPassengerVehicle extends BaseTest {

	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;
	
	public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();				
		Vehicle myVehicle = new Vehicle();
		myVehicle.setVehicleTypePL(VehicleTypePL.Trailer);
		myVehicle.setTrailerType(TrailerTypePL.Camper);
		vehicleList.add(myVehicle);
		SquirePersonalAuto peronalAutoSection = new SquirePersonalAuto();
		peronalAutoSection.setVehicleList(vehicleList);
		Squire mySquire = new Squire();
		mySquire.squirePA = peronalAutoSection;
	
	    myPolicyObject = new GeneratePolicy.Builder(driver)
			.withSquire(mySquire)
			.withProductType(ProductLineType.Squire)
			.withSquireEligibility(SquireEligibility.City)
			.withDownPaymentType(PaymentType.Cash)
			.withLineSelection(LineSelection.PersonalAutoLinePL)
			.withPolOrgType(OrganizationType.Individual)
			.withInsFirstLastName("Scope", "Creeps")
			.isDraft()
			.build(GeneratePolicyType.FullApp);
	    driver.quit();
	}

	@Test(enabled = true)
	public void scenario2VerifyLoyaltyDiscountNotChangedOnPolicyInceptionDatechange() throws Exception {

        generatePolicy();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		new Login(driver).loginAndSearchAccountByAccountNumber("msanchez", "gw", myPolicyObject.accountNumber);

        SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
        AccountSummaryPC pcAccountSummaryPage = new AccountSummaryPC(driver);
        GenericWorkorderVehicles_Details pcVehicleDetails = new GenericWorkorderVehicles_Details(driver);
		GenericWorkorderBlockBind pcQuoteBlockBindPage = new GenericWorkorderBlockBind(driver);
		GenericWorkorderRiskAnalysis_UWIssues pcRiskUnderwriterIssuePage = new GenericWorkorderRiskAnalysis_UWIssues(driver);
		
		pcAccountSummaryPage.clickAccountSummaryPendingTransactionByProduct(ProductLineType.Squire);
		pcSideMenu.clickSideMenuPAVehicles();
		pcWorkOrder.clickEdit();
		pcVehicleDetails.selectTrailer(TrailerTypePL.Camper);
		pcVehicleDetails.clickOK();
		pcSideMenu.clickSideMenuRiskAnalysis();
		pcWorkOrder.clickGenericWorkorderQuote();
		pcQuoteBlockBindPage.clickDetails();
		
		
		// @revision 7/26/18: button no longer special approve
		List<String> uwIssues = pcRiskUnderwriterIssuePage.getUWIssuesListLongDescription();		
		Assert.assertTrue(uwIssues.contains("AUTO: no Private Passenger or Passenger Pickup vehicles.  Underwriting approval is required to issue.(AU012)"), "Approve button was not there, it should be there for No passenger/pickup in AUTO");
	}
}

