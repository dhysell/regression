package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.BuildingClassCode;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;

/**
 * @Author ecoleman
 * @Requirement WCIC Businessowners Product Model.xlsx
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/WCIC%20Businessowners%20Product%20Model.xlsx">BOP Requirements Spreadsheet</a>
 * @Description testing Business Owner Policy coverages.
 * @Description Coverages tested:
 * Apartment Building BP 07 75 - Property
 * Food Contamination Coverage BP 04 31
 * Loss of Rental Value - Landlord as Designated Payee BP 05 93
 * Equipment Breakdown Enhancement Endorsement IDBP 31 1002
 * Ordinance or Law BP 04 46
 * Outdoor Property
 * Spoilage BP 04 15
 * Utilities - Direct Damage BP 04 56
 * Valuable Papers - Optional Coverage
 * Discretionary Payroll Expense BP 14 30
 * Condominium Commercial Unit-Owners Optional Coverage BP 17 03
 * Business Income - Ordinary Payroll
 * Accounts Receivable - Optional Coverage
 * Apartment Building BP 07 75 - Liability
 * @DATE Mar 6, 2018
 */
public class BOP_CoveragesBuildings extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObject = null;

	public void generatePolicy() throws Exception {
        myPolicyObject = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withProductType(ProductLineType.Businessowners)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.Apartments))
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsCompanyName("MMM BOP")
				.withLineSelection(LineSelection.Businessowners)
				.isDraft()
				.build(GeneratePolicyType.QuickQuote);
	}

	/**
	 * @throws Exception 
	 * @Author ecoleman
	 * @Description - Testing Coverages within Buildings
	 * @DATE Mar 21, 2018
     */
	@Test(enabled=true)
    public void testBOPBuildingCoverages() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderBuildings pcBuildingList = new GenericWorkorderBuildings(driver);		
        GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
        
		pcSideMenu.clickSideMenuBuildings();
        pcBuildingList.changeBuildingClassCode(BuildingClassCode.ApartmentBuildings4FamiliesorFewerWithNoOfficeOccupancyIncludes3or4FamilyLessorsRiskOnly.getClassCode());
        // TODO: "Apartment Building BP 07 75 - Property" is Electable in REQ document
        softAssert.assertTrue(gwHelpers.isSuggested("Apartment Building BP 07 75 - Property"), "Apartment Building BP 07 75 - Property | Was not electable when building class code was set to apartment");
        pcBuildingList.clickAdditionalCoverages();
        softAssert.assertTrue(gwHelpers.isElectable("Food Contamination Coverage BP 04 31"), "Food Contamination Coverage BP 04 31 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Loss of Rental Value - Landlord as Designated Payee BP 05 93"), "Loss of Rental Value - Landlord as Designated Payee BP 05 93 | Was not electable");
        softAssert.assertTrue(gwHelpers.isSuggested("Equipment Breakdown Enhancement Endorsement IDBP 31 1002"), "Equipment Breakdown Enhancement Endorsement IDBP 31 1002 | Was not suggested");
        softAssert.assertTrue(gwHelpers.isElectable("Ordinance or Law BP 04 46"), "Ordinance or Law BP 04 46 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Outdoor Property"), "Outdoor Property | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Spoilage BP 04 15"), "Spoilage BP 04 15 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Utilities - Direct Damage BP 04 56"), "Utilities - Direct Damage BP 04 56 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Valuable Papers - Optional Coverage"), "Valuable Papers - Optional Coverage | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Discretionary Payroll Expense BP 14 30"), "Discretionary Payroll Expense BP 14 30 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Condominium Commercial Unit-Owners Optional Coverage BP 17 03"), "Condominium Commercial Unit-Owners Optional Coverage BP 17 03 | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Business Income - Ordinary Payroll"), "Business Income - Ordinary Payroll | Was not electable");
        softAssert.assertTrue(gwHelpers.isElectable("Accounts Receivable - Optional Coverage"), "Accounts Receivable - Optional Coverage | Was not electable");
        softAssert.assertTrue(gwHelpers.isRequired("Apartment Building BP 07 75 - Liability"), "Apartment Building BP 07 75 - Liability | Was not required when building class code was set to apartment");
        softAssert.assertAll();
	}
}
