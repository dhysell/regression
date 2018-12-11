package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.QuoteType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
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
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import repository.pc.workorders.submission.SubmissionProductSelection;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
* @Author nvadlamudi
* @Requirement : US15075: "Next" button should skip unnecessary screens - BOP QQ
* 				 US15333: Unrequire 3 groups of questions on BOP QQ	
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/218293384284"> US15075 - "Next" button should skip unnecessary screens - BOP QQ</a>
* @Description : validate clicking next going to right page in QQ and supplementary questions are not mandatory for BOP QQ
* @DATE Jun 6, 2018
*/
public class BOPQQNextButton extends BaseTest {
	private WebDriver driver;
	private AddressInfo address;
	//JLARSEN 10/2/2018
	//DISABLED TEST INSTEAD OF FIXING BECAUSE THIS IS A PRETTY LOW RISK TEST. 
	//FIX IT NEEDS IS TO FILL OUT EPLI COVERAGE ON BUSINESS OWNERS LINE BEFORE TRYING TO MOVE ON.
	@Test(enabled = false)
	public void testCheckBOPNextButtonInQQ() throws Exception {
		Agents agent = AgentsHelper.getRandomAgent();
		address = new AddressInfo();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
		menuPolicy.clickNewSubmission();

		SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
		newSubmissionPage.selectAdvancedSearchSubmission();
		newSubmissionPage.fillOutFormSearchCreateNewWithoutStamp(true, ContactSubType.Company, null, null, null,
				"BOPNEXT " + StringsUtils.generateRandomNumberDigits(7), address.getCity(), address.getState(),
				address.getZip());
		SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		createAccountPage.fillOutPrimaryAddressFields(address);
		createAccountPage.setSubmissionCreateAccountBasicsAltID(StringsUtils.generateRandomNumberDigits(12));
		createAccountPage.clickCreateAccountUpdate();
		SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote,
				ProductLineType.Businessowners);
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		//adding BOP - Policy Info required fields - once defect fixed, we will remove them
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		polInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Partnership);
		polInfo.setPolicyInfoYearBusinessStarted("2012");
		polInfo.setPolicyInfoDescriptionOfOperations(null);
		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Businessowners Line')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO BUSINESSOWNERS LINE PAGE");

		GenericWorkorderBusinessownersLineIncludedCoverages boLineInclCovPage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);

		boLineInclCovPage.setSmallBusinessType(SmallBusinessType.Apartments);
		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'title') and contains(text(), 'Location Information')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO LOCATION INFORMATION PAGE");

		GenericWorkorderLocations location = new GenericWorkorderLocations(driver);
		PolicyLocation locToAdd = new PolicyLocation();
		locToAdd.setPlNumAcres(12);
		locToAdd.setAddress(address);
		location.addNewLocationAndBuildings(true, locToAdd, true, true);
		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@id, ':ttlBar') and contains(text(), 'Buildings')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO BUILDINGS PAGE");

		GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		buildings.addBuildingOnLocation(true, 1, loc1Bldg1);

		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO RISK ANALYSIS PAGE");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.clickGenericWorkorderQuote();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Quote')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO QUOTE PAGE");
		
		
		//US15333 : Unrequire 3 groups of questions on BOP QQ
		//Ensure that questions tied to Salon class code 71952(1) are not required.
		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuBuildings();
		buildings.clickEdit();
		buildings.selectFirstBuildingCodeResultClassCode("71952");
		buildings.setInsuredOperate(false);
		buildings.clickOK();
		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO RISK ANALYSIS PAGE");
		
		//Ensure that questions tied to Barber Shop class code 71332 are not required.
		sideMenu.clickSideMenuBuildings();
		buildings.clickEdit();
		buildings.selectFirstBuildingCodeResultClassCode("71332");
		buildings.setInsuredOperate(false);
		buildings.clickOK();
		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO RISK ANALYSIS PAGE");
		//Ensure that questions tied to Motel class codes 69151, 69161, and 69171 are not required.
		sideMenu.clickSideMenuBuildings();
		buildings.clickEdit();
		buildings.selectFirstBuildingCodeResultClassCode("71952");
		buildings.setInsuredOperate(false);
		buildings.clickOK();
		polInfo.clickNext();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Risk Analysis')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO RISK ANALYSIS PAGE");
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.clickGenericWorkorderQuote();
		Assert.assertTrue(new GuidewireHelpers(driver).isOnPage("//span[contains(@class, 'g-title') and (text()='Quote')]"),
				"NEXT BUTTON NOT SKIPPING UNNECESSARY SCREENS - UNABLE TO GET TO QUOTE PAGE");
		
		

	}

}
