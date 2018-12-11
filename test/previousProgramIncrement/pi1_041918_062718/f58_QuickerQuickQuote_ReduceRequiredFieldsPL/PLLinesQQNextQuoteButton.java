package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.QuoteType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderSquireEligibility;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_CoverageSelection;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderStandardIMCoverageSelection;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

import java.util.Date;

/**
* @Author nvadlamudi
* @Requirement: US14798: "Next" button should skip unnecessary screens - PL QQ 
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/212669380472">US14798 - "Next" button should skip unnecessary screens - PL QQ</a>
* @Description : Acceptance Criteria : Ensure that when you click next that the next screen you are taken to has required fields.  
*									   Ensure that the quote button is available once QQ required screens have been visited.
*									   Validations should still trigger from missed required fields. 
* @DATE Jun 4, 2018
*/
public class PLLinesQQNextQuoteButton extends BaseTest {
	private WebDriver driver;
	private String insLastName = "NextPage" + StringsUtils.generateRandomNumberDigits(9);
	private AddressInfo address;
	private GenericWorkorderSquireEligibility eligibilityPage;

	@Test
	public void testCheckNextButtonInSquireQQ() throws Exception {
		createAccountAndSelectLine(ProductLineType.Squire);
		eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.chooseCity();
		eligibilityPage.clickNext();
		checkNextForPolicyInfo(ProductLineType.Squire);
		checkNextForSectionIScreens(PropertyTypePL.ResidencePremises);
		checkNextForSectionIIIScreens();
		checkNextForSectionIVScreens();
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - RISK ANALYSIS PAGE");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}
	@Test
	public void testCheckNextButtonInStdFireQQ() throws Exception {
		createAccountAndSelectLine(ProductLineType.StandardFire);
		eligibilityPage = new GenericWorkorderSquireEligibility(driver);

		eligibilityPage.setPolicySelection(true);
		checkNextForPolicyInfo(ProductLineType.StandardFire);
		checkNextForSectionIScreens(PropertyTypePL.DwellingPremises);
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - RISK ANALYSIS PAGE");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}
	@Test
	public void testCheckNextButtonInStdIMQQ() throws Exception {
		createAccountAndSelectLine(ProductLineType.StandardIM);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		policyInfoPage.setPolicyInfoRatingCounty("Bannock");
		policyInfoPage.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
		eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.clickNext();
		Assert.assertTrue(
				new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverage Selection')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - COVERAGE SELECTION PAGE");
		
		GenericWorkorderStandardIMCoverageSelection imSelection  = new GenericWorkorderStandardIMCoverageSelection(driver);
		imSelection.checkCoverage(true, InlandMarine.FarmEquipment.getValue());
		imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Farm Equipment')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - FARM EQUIPMENT PAGE");
	
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Personal Property')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - PERSONAL PROPERTY PAGE");
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - RISK ANALYSIS PAGE");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
	}

	@Test
	public void testCheckNextButtonInStdLiabQQ() throws Exception {
			createAccountAndSelectLine(ProductLineType.StandardLiability);
            GenericWorkorderSquireEligibility eligibilityPage = new GenericWorkorderSquireEligibility(driver);

			eligibilityPage.setPolicySelection(false);	
			checkNextForPolicyInfo(ProductLineType.StandardLiability);
			eligibilityPage.clickNext();
			Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Locations')]"),
					"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO LOCATIONS PAGE");
			GenericWorkorderSquirePropertyAndLiabilityLocation pLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
			PolicyLocation location = new PolicyLocation();
			location.setAddress(address);
			pLocations.addNewOrSelectExistingLocationQQ(location);
			eligibilityPage.clickNext();	
			Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverages')]"),
					"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION 1 - COVERAGES PAGE");
			eligibilityPage.clickNext();
			Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
					"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - RISK ANALYSIS PAGE");
			new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();	
	}
	
	public void createAccountAndSelectLine(ProductLineType lineType) throws Exception {
		Agents agent = AgentsHelper.getRandomAgent();
		address = new AddressInfo();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());

		TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
		menuPolicy.clickNewSubmission();

		SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
		newSubmissionPage.selectAdvancedSearchSubmission();
		newSubmissionPage.fillOutFormSearchCreateNewWithoutStamp(true, ContactSubType.Person, insLastName,
				"QQ" + StringsUtils.generateRandomNumberDigits(7), null, null, address.getCity(), address.getState(),
				address.getZip());
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		createAccountPage.fillOutPrimaryAddressFields(address);
		createAccountPage.setSubmissionCreateAccountBasicsAltID(StringsUtils.generateRandomNumberDigits(12));
		createAccountPage.setDOB(DateUtils.dateAddSubtract(new Date(), DateAddSubtractOptions.Year, -35));
		createAccountPage.clickCreateAccountUpdate();
		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, lineType);
	}

    private void checkNextForPolicyInfo(ProductLineType product) {
		eligibilityPage = new GenericWorkorderSquireEligibility(driver);
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Policy Info')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO POLICY INFO PAGE");

		GenericWorkorderPolicyInfo policyInfoPage = new GenericWorkorderPolicyInfo(driver);
		policyInfoPage.setPolicyInfoRatingCounty("Bannock");
		if(product.equals(ProductLineType.StandardFire) || product.equals(ProductLineType.StandardLiability)){
			policyInfoPage.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
		}
	}

	private void checkNextForSectionIScreens(PropertyTypePL propertyType) throws Exception {
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Locations')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO LOCATIONS PAGE");
		GenericWorkorderSquirePropertyAndLiabilityLocation pLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		PolicyLocation location = new PolicyLocation();
		location.setAddress(address);
		pLocations.addNewOrSelectExistingLocationQQ(location);
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Property Detail')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION 1 - PROPERTY DETAILS PAGE");
				GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		PLPolicyLocationProperty property = new PLPolicyLocationProperty();
		property.setpropertyType(propertyType);
		propertyDetail.fillOutPLPropertyQQ(true, property, SectionIDeductible.OneThousand, location);
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverages')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION 1 - COVERAGES PAGE");
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.setCoverageALimit(120000);
		
		if(propertyType.equals(PropertyTypePL.DwellingPremises)){
			coverages.setCoverageCLimit(120000);
		}
	}

	private void checkNextForSectionIIIScreens() throws Exception {
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Drivers')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION III - DRIVERS PAGE");
		GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.addExistingDriver(insLastName);
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Male);
		paDrivers.clickOk();
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverages')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION III - COVERAGES PAGE");

		// PA Coverages
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Vehicles')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION III - VEHICLES PAGE");
		GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
		vehiclePage.createVehicleManually();
		Vehicle vehicle = new Vehicle();
		vehicle.setVehicleTypePL(VehicleTypePL.PrivatePassenger);
		Contact driverPL = new Contact();
		driverPL.setLastName(insLastName);
		vehicle.setDriverPL(driverPL);
		new GenericWorkorderVehicles_Details(driver).fillOutVehicleDetails_QQ(true, vehicle);
		new GenericWorkorderVehicles_CoverageDetails(driver).fillOutCoverageDetails_QQ(vehicle);
		new GenericWorkorderVehicles_ExclusionsAndConditions(driver).fillOutExclusionsAndConditions_QQ(vehicle);
		vehiclePage.clickOK();

	}

    private void checkNextForSectionIVScreens() {
		eligibilityPage.clickNext();
		Assert.assertTrue(
				new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Coverage Selection')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - COVERAGE SELECTION PAGE");
		GenericWorkorderSquireInlandMarine_CoverageSelection imSelection = new GenericWorkorderSquireInlandMarine_CoverageSelection(driver);
		imSelection.checkCoverage(true, InlandMarine.FarmEquipment.getValue());
		imSelection.checkCoverage(true, InlandMarine.PersonalProperty.getValue());
		imSelection.checkCoverage(true, InlandMarine.RecreationalEquipment.getValue());
		imSelection.checkCoverage(true, InlandMarine.Watercraft.getValue());
		imSelection.checkCoverage(true, InlandMarine.Cargo.getValue());
		imSelection.checkCoverage(true, InlandMarine.Livestock.getValue());
		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Recreational Equipment')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - RECREATIONAL EQUIPMENT PAGE");

		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Watercraft')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - WATERCRAFT PAGE");

		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Farm Equipment')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - FARM EQUIPMENT PAGE");

		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Personal Property')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - PERSONAL PROPERTY PAGE");

		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Cargo')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - CARGO PAGE");

		eligibilityPage.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Livestock')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO SECTION IV - LIVESTOCK PAGE");

	}

}
