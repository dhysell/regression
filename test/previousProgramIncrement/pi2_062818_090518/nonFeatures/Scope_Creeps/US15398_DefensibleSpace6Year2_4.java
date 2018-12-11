package previousProgramIncrement.pi2_062818_090518.nonFeatures.Scope_Creeps;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.actions.ActionsPC;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetail;

import java.util.ArrayList;

/**
 * @Author ecoleman
 * @Requirement
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558461772d/detail/userstory/229415681900">US15398 Defensible space should be required to answer only every 6 years matching photo year</a>
 * @Description
 * As an underwriter I want to see the Defensible space question on Coverage A buildings nulls out  at the first renewal and be again required for every building at the same time as the buildings photo year is required. Every 6 years. Needs to go 6 years from the photo year date per building. 
 * 
 * Steps to get there:
 * 
 * Have a policy that has a photo year on at least one building that is greater than or equal to 6 years old
 * Begin the renewal process.
 * 
 * Acceptance criteria:
 * 
 * Ensure that when buildings are added as new that the defensible space question is required
 * Ensure that if the photo year is greater than or equal to 6 years old, the defensible space protection question will null out and be required to be re-answered
 * Ensure that if the photo year is less than 6 years old, the defensible space protection question does NOT null out
 * Ensure that this question only nulls out on the dwelling that the photo year is greater than or equal to 6 years old, if there is another dwelling that is less than 6 years old it does NOT null out.
 * Ensure that this works per Coverage A building. Each building has it's own photo year and the defensible space question goes with the year for each building.
 * @DATE July 31, 2018
 */

//@Test(groups= {"ClockMove"})
public class US15398_DefensibleSpace6Year2_4 extends BaseTest {

	public GeneratePolicy myPolicyObject = null;
	private WebDriver driver;

	public void generatePolicy() throws Exception {    	
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		PLPolicyLocationProperty newProperty1 = new PLPolicyLocationProperty();
		newProperty1.setYearBuilt(DateUtils.getYearFromDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -15)));
		newProperty1.setPhotoYear2(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -7));

		PLPolicyLocationProperty newProperty2 = new PLPolicyLocationProperty();
		newProperty2.setpropertyType(PropertyTypePL.DwellingPremises);
		newProperty2.setYearBuilt(DateUtils.getYearFromDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -15)));
		newProperty2.setPhotoYear2(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));

		Squire mySquire = new Squire();
		PolicyLocation newLocation = new PolicyLocation();
		ArrayList<PLPolicyLocationProperty> propertyLocationList1 = new ArrayList<PLPolicyLocationProperty>();
		propertyLocationList1.add(newProperty1);
		newLocation.setPropertyList(propertyLocationList1);
		PolicyLocation newLocation2 = new PolicyLocation();
		ArrayList<PLPolicyLocationProperty> propertyLocationList2 = new ArrayList<PLPolicyLocationProperty>();
		propertyLocationList2.add(newProperty2);
		newLocation2.setPropertyList(propertyLocationList2);
		ArrayList<PolicyLocation> newLocationList = new ArrayList<PolicyLocation>();
		newLocationList.add(newLocation);
		newLocationList.add(newLocation2);
		mySquire.propertyAndLiability.locationList = newLocationList;


		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Squire)
				.withSquire(mySquire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withDownPaymentType(PaymentType.Cash)
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -360))
				.withInsFirstLastName("ScopeCreeps", "NonFeature")
				.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(enabled = true)
	public void DefensiveSpaceNullOutAfter6Years() throws Exception {

		generatePolicy();

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);


		SideMenuPC pcSideMenu = new SideMenuPC(driver);
		SoftAssert softAssert = new SoftAssert();
		PolicySummary pcPolicySummaryPage = new PolicySummary(driver);
		GenericWorkorderSquirePropertyDetail pcPropertyDetailPage = new GenericWorkorderSquirePropertyDetail(driver);
		ActionsPC pcActionsMenu = new ActionsPC(driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails pcProtectionDetailsPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver);
		PolicyPreRenewalDirection pcPreRewenalDirectionsPage = new PolicyPreRenewalDirection(driver);        		

		pcActionsMenu.click_Actions();
		pcActionsMenu.click_RenewPolicy();
		pcActionsMenu.clickOK();

		pcPreRewenalDirectionsPage.closePreRenewalExplanations(myPolicyObject);        

		pcPolicySummaryPage.clickPendingTransaction(TransactionType.Renewal);

		pcSideMenu.clickSideMenuSquirePropertyDetail();        
		pcPropertyDetailPage.clickProtectionDetailsTabSecondary();

		// Acceptance criteria: Photo older than 6 years should null out defensible space
		softAssert.assertEquals(pcProtectionDetailsPage.label_DefensibleSpaceMaintained.getText(), "", "Photo year is older than 6yr, defensible space should null out.");

		pcPropertyDetailPage.clickLocation(myPolicyObject.squire.propertyAndLiability.locationList.get(1));

		// Acceptance criteria: Photo not older than 6 years should not null out defensible space
		softAssert.assertEquals(pcProtectionDetailsPage.label_DefensibleSpaceMaintained.getText(), "Yes", "Photo year is not older than 6yr, defensible space should not null out.");

		softAssert.assertAll();
	}


}




















