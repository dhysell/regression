package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.ARTists;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ChangeReason;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.OccasionalDriverReason;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;

/**
 * @Author nvadlamudi
 * @Requirement :DE7779: ***HOT FIX*** Policy change moving occasional underage driver to new car not giving agent the access to vehicle use
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/245085927756">Link Text</a>
 * @Description : Validate vehicle Use is shown to agent and UW
 * @DATE Aug 10, 2018
 */
public class DE7779PolicyChangeOccasionalDriver  extends BaseTest{
	@Test
	public void testIssueUnderAgeDriverPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Squire mySquire = new Squire(SquireEligibility.City);

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL).withInsFirstLastName("Sub", "UWIssue").isDraft()
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash).withInsAge(23)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start first policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
		GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
		Vehicle newVehicle = new Vehicle();
		newVehicle.setVehicleTypePL(VehicleTypePL.PassengerPickup);
		vehiclePage.fillOutVehicleDetails_QQ(false, newVehicle);	
		vehiclePage.clickOK();
		vehiclePage.clickGenericWorkorderSaveDraft();

		sideMenu.clickSideMenuRiskAnalysis();
		policyChangePage.quoteAndSubmit(ChangeReason.AutoOnlyChange, "Testing Purpose");
//		GenericWorkorderComplete woComplete = new GenericWorkorderComplete(driver);
//		woComplete.clickAccountNumber();
//		AccountSummaryPC summary = new AccountSummaryPC(driver);
//		Underwriters uw = summary.getAssignedToUW("Submitted policy change");
//		new GuidewireHelpers(driver).logout();      
//
//		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
//		summary.clickActivitySubject("Submitted policy change");
//		GenericWorkorder woIssue = new GenericWorkorder(driver);
//		woIssue.clickIssuePolicyButton();
		new GuidewireHelpers(driver).logout();   


		//Login as Agent and adding new policy change 
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start second policy change as agent
		policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		sideMenu.clickSideMenuPAVehicles();
		vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.removeVehicleByRowNumber(1);
		vehiclePage.clickEdit();
		vehiclePage.setNoDriverAssigned(false);

		// validate second policy change with agent Login 
		vehiclePage.addDriver(myPolicyObjPL.pniContact, DriverVehicleUsePL.Principal);
		Assert.assertTrue(vehiclePage.checkDriverVehicleUse(), "Vehile Use is not displayed with Agent Login.");
		vehiclePage.clickOK();     
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote(); 
		new GuidewireHelpers(driver).logout();   


		// validate second policy change with uw Login 
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObjPL);
		PolicySummary policySummary = new PolicySummary(driver);
		policySummary.clickPendingTransaction(TransactionType.Policy_Change); 
		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuPAVehicles();
		vehiclePage.clickEdit();      
		vehiclePage.selectDriverVehicleUse(DriverVehicleUsePL.Occasional);
		vehiclePage.selectOcassionalDriverReason(OccasionalDriverReason.Permit);
		Assert.assertTrue(vehiclePage.checkDriverVehicleUse(), "Vehile Use is not displayed with UW Login.");      
//		vehiclePage.clickOK();
//
//		sideMenu.clickSideMenuRiskAnalysis();
//		risk = new GenericWorkorderRiskAnalysis(driver);
//		risk.Quote(); 
//		sideMenu.clickSideMenuRiskAnalysis();
//		risk.approveAll();
//		policyChangePage.clickIssuePolicy();
//		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
//		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());


	}
}
