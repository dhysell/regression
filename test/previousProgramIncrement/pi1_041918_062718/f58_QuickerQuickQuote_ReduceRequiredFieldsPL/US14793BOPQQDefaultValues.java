package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.QuoteType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
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

import java.util.ArrayList;

/**
* @Author nvadlamudi
* @Requirement : US14793: Eliminate non-essential BOP fields and add default values on QQ
* @RequirementsLink <a href="https://fbmicoi-my.sharepoint.com/:x:/g/personal/tharrild_idfbins_com/EQCwEbAXXqlEqzGnqJyMUkwB0auWz9WH-xNxDHPsQlMVOg?e=3RXpqC">US14793 BOP QQ Fields to UnRequire & Values to Default</a>
* @Description: Validate BOP - QQ default values and possible of non-essential fields check
* @DATE May 17, 2018
*/
public class US14793BOPQQDefaultValues extends BaseTest {
	private String accountNumber;
	private AddressInfo address;
	private WebDriver driver;

	//JLARSEN 10/2/2018
	//DISABLED TEST VS ADDING CODE FOR EPLI FOR IT IS A PRETTY LOW RISK SCENARIO
	@Test(enabled = false)
	public void testCheckDefaultValuesInQQ() throws Exception {
		createBOPAccountWithProductSelection();
		checkBOPIncludedCoverages();
	}
	
	
	private void checkBOPIncludedCoverages() throws Exception {
		boolean testPassed = true;
		String errorMessage = "";
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBusinessownersLine();
        GenericWorkorderBusinessownersLineIncludedCoverages boLineInclCovPage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
        boLineInclCovPage.setSmallBusinessType(SmallBusinessType.Apartments);
        
        if(!boLineInclCovPage.getBusinessownersLineIncludedCoveragesLiabilityLimits().equals(LiabilityLimits.One000_2000_2000.getValue())){
			testPassed = false;
			errorMessage = errorMessage + "Expected BOP - Included Coverages - Liability Limit - default value '"+LiabilityLimits.One000_2000_2000.getValue() + "' is not displayed in QQ. \n";
		}
        boLineInclCovPage.clickBusinessownersLine_AdditionalCoverages();
        boLineInclCovPage.clickBusinessownersLine_ExclusionsConditions();
        sideMenu.clickSideMenuLocations();
        GenericWorkorderLocations location = new GenericWorkorderLocations(driver);
        
    	ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		PolicyLocation locToAdd = new PolicyLocation();
		locToAdd.setPlNumAcres(12);
		locToAdd.setAddress(address);
		locationsList.add(locToAdd);        
		location.fillOutLocationsBuildingsSubmissionQuick(true, locationsList);
        sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		 buildings.addBuildingOnLocation(true, 1, loc1Bldg1);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();				
		Assert.assertTrue(testPassed, "Account Number: '"+ accountNumber +"' - Section I Failed Default Values:  "+errorMessage + ".");
		
	}
	private void createBOPAccountWithProductSelection() throws Exception {
		Agents agent = AgentsHelper.getRandomAgent();
		address = new AddressInfo();		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
        TopMenuPolicyPC menuPolicy = new TopMenuPolicyPC(driver);
		menuPolicy.clickNewSubmission();

        SubmissionNewSubmission newSubmissionPage = new SubmissionNewSubmission(driver);
    	newSubmissionPage.selectAdvancedSearchSubmission();
		newSubmissionPage.fillOutFormSearchCreateNewWithoutStamp(true, ContactSubType.Company,
				null ,null,null,
				"BOPQQ " + StringsUtils.generateRandomNumberDigits(7), address.getCity(), address.getState(),
				address.getZip());
        SubmissionCreateAccount createAccountPage = new SubmissionCreateAccount(driver);
		createAccountPage.fillOutPrimaryAddressFields(address);
		createAccountPage.setSubmissionCreateAccountBasicsAltID(StringsUtils.generateRandomNumberDigits(12));
		createAccountPage.clickCreateAccountUpdate();
        SubmissionProductSelection selectProductPage = new SubmissionProductSelection(driver);
		accountNumber = selectProductPage.startQuoteSelectProductAndGetAccountNumber(QuoteType.QuickQuote, ProductLineType.Businessowners);

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		//adding BOP - Policy Info required fields - once defect fixed, we will remove them
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		polInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Partnership);
		polInfo.setPolicyInfoYearBusinessStarted("2012");
		polInfo.setPolicyInfoDescriptionOfOperations(null);
		polInfo.clickNext();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
	}
}
