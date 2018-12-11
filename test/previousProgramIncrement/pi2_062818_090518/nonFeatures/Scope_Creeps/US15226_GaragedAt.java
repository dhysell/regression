package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.Vehicle;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.gw.helpers.DateUtils;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;

import java.util.ArrayList;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/222937008368">US15226 Change 'garaged at' on section III so doesn't have to be removed to delete location on section II</a>
 * @Description
 * As an agent/PA/underwriter I want to be able to NOT have to edit or remove the garaged at address on section III vehicles before remove that location from section II. 
 * Also want to see a validation reminding user that the primary location was removed and does the garaged at location on section III need updated.
 * 
 * Steps to get there:
 * 
 *     Have policy issued with sections I, II and III. The garaged at on section III should be the same as location on section II.
 *     Do a policy change removing the location and home from sections I and II.
 *     Should be able to issue policy change.
 * 
 * Acceptance criteria:
 * 
 *     Ensure that the garaged at location on section III does not need to be updated in order to remove the same location from section II
 *     Ensure that there is a reminder for user to check section III and update if necessary.  "Primary address has been changed/removed. Does section III 'garaged at' address need to be updated?"
 *     Ensure that everything also works for policy change and all editable rewrite jobs.
 * @DATE July 26, 2018
 */

public class US15226_GaragedAt extends BaseTest {

	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;

	@Test(enabled = true)
	public void RemoveGaragedAtLocation() throws Exception {
		Squire mySquire = new Squire();
		PolicyLocation myLocation = new PolicyLocation();
		mySquire.propertyAndLiability.locationList.add(myLocation);
		ArrayList<Vehicle> myVehicleList = new ArrayList<Vehicle>();
		Vehicle myVehicle = new Vehicle();    	
		myVehicle.setGaragedAt(myLocation.getAddress());
		myVehicleList.add(myVehicle);
		mySquire.squirePA.setVehicleList(myVehicleList);    	

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
				.withInsFirstLastName("ScopeCreeps", "NonFeature")
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObject.underwriterInfo.getUnderwriterUserName(),
				myPolicyObject.underwriterInfo.getUnderwriterPassword(), myPolicyObject.accountNumber);


		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		GenericWorkorder pcWorkOrder = new GenericWorkorder(driver);
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		GenericWorkorderComplete pcCompletePage = new GenericWorkorderComplete(driver);
		StartPolicyChange pcPolicyChange = new StartPolicyChange(driver);
		GenericWorkorderLocations pcLocationsPage = new GenericWorkorderLocations(driver);


		pcPolicyChange.startPolicyChange("This is a change", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

		pcSideMenu.clickSideMenuSquirePropertyDetail();

		pcPropertyDetailPage.clickLocation(2);
		pcPropertyDetailPage.removeBuilding("2");

		pcSideMenu.clickSideMenuLocations();

		pcLocationsPage.removeLocationByLocNumber(2);

		String popupText = pcLocationsPage.getPopupTextContents();
		Boolean popupTextContainsPR093 = popupText.contains("(PR093)");
		// Acceptance criteria: 
		Assert.assertFalse(popupTextContainsPR093, "PR093 displayed as a blocker, it should not have, it should just be a warning and the building should be removed.");

		pcWorkOrder.clickGenericWorkorderQuote();
		pcWorkOrder.clickIssuePolicyButton();
		pcCompletePage.clickViewYourPolicy();

		Assert.assertNotNull(pcPolicySummaryPage.getCompletedTransactionNumberByType(TransactionType.Policy_Change), "No completed policy change was found, there should be.");
	}


}




















