package regression.businessowners.noclock.policycenter;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.Building.BoxType;
import repository.gw.enums.Building.HouseKeepingMaint;
import repository.gw.enums.Building.RoofCondition;
import repository.gw.enums.Building.RoofingType;
import repository.gw.enums.Building.WiringType;
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
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderSupplemental;

/**
 * @Author ecoleman
 * @Requirement WCIC Businessowners Product Model.xlsx
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/WCIC%20Businessowners%20Product%20Model.xlsx">BOP Requirements Spreadsheet</a>
 * @Description testing Business Owner Policy Normal Questions.
 * @Description Questions tested:
 * Is trash stored in a steel covered container?
 * Do any loss control features apply? If yes, check all that apply.
 * Are dishes chipped?
 * Is furniture in good repair and sturdy?
 * Does applicant have catering services?
 * Does the applicant have liquor sales?
 * At least 1 question associated with \"Do any loss control features apply? If yes, check all that apply.\" is required
 * BOP Schedule Credits
 * Play Area
 * Flaming Dish
 * Entertainment
 * Stage
 * Valet Parking
 * Grill
 * Deep Fat Fryer
 * None of the Above
 * Must Select at Least 1 Parking Lot and Sidewalk Characteristics. Loc# 1/Bld# 1/09621
 * Must Select at Least 1 Safety Equipment. Loc# 1/Bld# 1/09621
 * Must Select at Least 1 Exposure
 * @DATE Mar 29, 2018
 */
public class BOP_NormalQuestionsRestaurant extends BaseTest {
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
	 * @Author ecoleman
	 * @Description - Testing Liquor Liability Coverage Question Set within Business Owners Line
	 * TODO: Eventually expand this, and add Full App checks
	 * @DATE Apr 02, 2018
	 * @throws Exception
	 */
	@Test(enabled=true)
	public void testBusinessOwnersRestaurantQuestions() throws Exception  {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		generatePolicy();
		
		new Login(driver).loginAndSearchSubmission(myPolicyObject.agentInfo.getAgentUserName(), myPolicyObject.agentInfo.getAgentPassword(), myPolicyObject.accountNumber);
		
		SoftAssert softAssert = new SoftAssert();
		SideMenuPC pcSideMenu = new SideMenuPC(driver);
        GenericWorkorderBuildings pcBuildingListPage = new GenericWorkorderBuildings(driver);
		GenericWorkorderSupplemental pcWorkorderSupplementalPage = new GenericWorkorderSupplemental(driver);
		GenericWorkorderQualification pcWorkorderQualificationPage = new GenericWorkorderQualification(driver);
		GenericWorkorder gwo = new GenericWorkorder(driver);


//		GenericWorkorderBusinessownersLineIncludedCoverages boLineInclCovPage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
//		boLineInclCovPage.clickBusinessownersLine_AdditionalCoverages();
//		GenericWorkorderBusinessownersLineAdditionalCoverages boLineAddtlCovPage = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);
//		boLineAddtlCovPage.addLiquorLiability(true);
		
		pcSideMenu.clickSideMenuBuildings();
        
        pcBuildingListPage.changeBuildingClassCode(BuildingClassCode.CasualDiningRestaurantsBistrosBrasseriesandCafesWithSalesofAlcoholicBeveragesUpto50ofTotalSales.getClassCode());
        
        pcBuildingListPage.setInsuredOperate(true);
        pcBuildingListPage.setBasisAmount(50000);
        pcBuildingListPage.clickOK();
        pcSideMenu.clickSideMenuSupplemental();
		// The following asserts come from requirement doc " 1.0-2.0 - Quick Quote and Full App.pptx " vvv START
        // Complete Building Questions for Restaurants
        // Found at: " http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Quick%20Quote%20Full%20App/1.0-2.0%20-%20Quick%20Quote%20and%20Full%20App.pptx "
        // Slide #: 553 - 555 ; 1.4.4.BQ18 - 1.4.4.BQ20
        // ADDENDUM 5/21/18
        // Supplemental page questions removed from QQ
        // (US14791) https://rally1.rallydev.com/#/203558458764d/detail/userstory/212662651676
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Is trash stored in a steel covered container?"), "Is trash stored in a steel covered container? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Do any loss control features apply? If yes, check all that apply."), "Do any loss control features apply? If yes, check all that apply. | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Are dishes chipped?"), "Are dishes chipped? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Is furniture in good repair and sturdy?"), "Is furniture in good repair and sturdy? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does applicant have catering services?"), "Does applicant have catering services? | Was not required");
		softAssert.assertFalse(new GuidewireHelpers(driver).isRequired("Does the applicant have liquor sales?"), "Does the applicant have liquor sales? | Was not required");
		// The preceding asserts come from requirement doc " 1.0-2.0 - Quick Quote and Full App.pptx " ^^^ END
		
		softAssert.assertAll();
		

				
		// FA Section
		gwo.clickGenericWorkorderFullApp();
		
		pcWorkorderQualificationPage.setFullAppAllTo(false);
		
		pcSideMenu.clickSideMenuSupplemental();
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Is trash stored in a steel covered container?"), "Is trash stored in a steel covered container? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Do any loss control features apply? If yes, check all that apply."), "Do any loss control features apply? If yes, check all that apply. | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Are dishes chipped?"), "Are dishes chipped? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Is furniture in good repair and sturdy?"), "Is furniture in good repair and sturdy? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does applicant have catering services?"), "Does applicant have catering services? | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Does the applicant have liquor sales?"), "Does the applicant have liquor sales? | Was not required");
		
		pcWorkorderSupplementalPage.setTrashInSteelContainerRadio(true);
		pcWorkorderSupplementalPage.setLossControlFeaturesRadio(true);
		
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("Please fill in all required fields."), "Please fill in all required fields. | Was not on page");
		
		pcWorkorderSupplementalPage.setDishesChippedRadio(false);
		pcWorkorderSupplementalPage.setSturdyFurnitureRadio(true);
		pcWorkorderSupplementalPage.setCateringServicesRadio(false);
		pcWorkorderSupplementalPage.setLiquorSalesRadio(false);
		pcWorkorderSupplementalPage.setTrashStoredAwayFromFoodRadio(true);
		pcWorkorderSupplementalPage.setMajorhealthViolationsRadio(false);
		
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("At least 1 question associated with \"Do any loss control features apply? If yes, check all that apply.\" is required"), "At least 1 question associated with \"Do any loss control features apply? If yes, check all that apply.\" is required |  Was not on page");
		
		pcWorkorderSupplementalPage.setFirstAidKitsCheckBox(true);
		
		pcWorkorderSupplementalPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isRequired("BOP Schedule Credits"), "BOP Schedule Credits |  Was not on page");
		pcWorkorderSupplementalPage.clickBack();
		
		//Assert.assertTrue(new GuidewireHelpers(driver).isRequired(""), " |  Was not on page");
		//softAssert.assertTrue(new GuidewireHelpers(driver).isRequired(""), " | Was not required");
		
		pcSideMenu.clickSideMenuBuildings();
		pcBuildingListPage.clickEdit();
		
		// The following asserts come from requirement doc " 1.0-2.0 - Quick Quote and Full App.pptx " vvv START
        // Complete Building Questions for Restaurants
        // Found at: " http://projects.idfbins.com/policycenter/To%20Be%20Process/Requirements%20Documentation/Quick%20Quote%20Full%20App/1.0-2.0%20-%20Quick%20Quote%20and%20Full%20App.pptx "
        // Slide #: 617 ; 2.4.4.BQ.18
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Play Area"), "Play Area | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Flaming Dish"), "Table Side Service of Flaming Dishes | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Entertainment"), "Entertainment | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Stage"), "Stage | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Valet Parking"), "Valet Parking | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Grill"), "Grill | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("Deep Fat Fryer"), "Deep Fat Fryer | Was not on page");
		softAssert.assertTrue(new GuidewireHelpers(driver).isElectable("None of the Above"), "None of the Above | Was not on page");
		// The following asserts come from requirement doc " 1.0-2.0 - Quick Quote and Full App.pptx " ^^^ END
		
		// TODO: Build out  helper method to do all this
		pcBuildingListPage.setNumOfStories(1);
		pcBuildingListPage.setNumOfBasements(1);
		pcBuildingListPage.setTotalArea("1500");
		pcBuildingListPage.setRoofingType(RoofingType.Aluminum);
		pcBuildingListPage.setFlatRoof(false);
		pcBuildingListPage.setRoofCondition(RoofCondition.NoIssues);
		pcBuildingListPage.setWiringType(WiringType.Romex);
		pcBuildingListPage.setBoxType(BoxType.CircuitBreaker);
		pcBuildingListPage.setExistingDamage(false);
		pcBuildingListPage.setInsuredPropertyWithin100Feet(false);
		pcBuildingListPage.setExteriorHousekeeping(HouseKeepingMaint.Superior);
		pcBuildingListPage.setInteriorHousekeeping(HouseKeepingMaint.Superior);
		pcBuildingListPage.setExitsMarked(true);
		pcBuildingListPage.setNumberOfFireExtinguishers(10);
		pcBuildingListPage.setExposureToFlammables(false);
		pcBuildingListPage.setLastUpdateRoofing(2018);
		pcBuildingListPage.setLastUpdateWiring(2017);
		pcBuildingListPage.setLastUpdateHeating(2018);
		pcBuildingListPage.setWiringUpdateDescription("stuffandstuff");
		pcBuildingListPage.setHeatingUpdateDescription("asddasdasdasdasd");
		pcBuildingListPage.setPlumbingUpdateDescription("asdasdadsdasdasd");
		pcBuildingListPage.setLastUpdatePlumbing(2018);
		
		pcBuildingListPage.clickOK();

		// TODO: Research business rules on these 3 asserts
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Must Select at Least 1 Parking Lot and Sidewalk Characteristics. Loc# 1/Bld# 1/09621"), "Must Select at Least 1 Parking Lot and Sidewalk Characteristics. Loc# 1/Bld# 1/09621 - Casual Dining Restaurants - Bistros, Brasseries and Cafes - With Sales of Alcoholic Beverages - Up to 50% of Total Sales | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Must Select at Least 1 Safety Equipment. Loc# 1/Bld# 1/09621"), "Must Select at Least 1 Safety Equipment. Loc# 1/Bld# 1/09621 - Casual Dining Restaurants - Bistros, Brasseries and Cafes - With Sales of Alcoholic Beverages - Up to 50% of Total Sales | Was not required");
		softAssert.assertTrue(new GuidewireHelpers(driver).isRequired("Must Select at Least 1 Exposure"), "Must Select at Least 1 Exposure. Loc# 1/Bld# 1/09621 - Casual Dining Restaurants - Bistros, Brasseries and Cafes - With Sales of Alcoholic Beverages - Up to 50% of Total Sales | Was not required");
		
		softAssert.assertAll();
	}
}