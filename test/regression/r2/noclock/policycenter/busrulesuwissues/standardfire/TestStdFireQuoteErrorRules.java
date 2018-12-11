package regression.r2.noclock.policycenter.busrulesuwissues.standardfire;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
@QuarantineClass
public class TestStdFireQuoteErrorRules extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	String pr018ValMessage = "Please correct the number of residence for this location. (PR018)";
	String pr050ValMessage = "Acres : Must enter at least 1 acre. (PR050)";
	String pr075ValMessage = "Section I: Limit on Property #1 on location #1 must be at least $40,000. (PR075)";
	String pr076ValMessage = "on location #1 must be at least $15,000. (PR076)";

	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testCreateStandardFireFA() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(2);
		locationsList.add(propLoc);

        myPolicyObjPL = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("BusRules", "Stdfire")
				.withInsAge(26).withPolOrgType(OrganizationType.Individual).withPolicyLocations(locationsList)
				.build(GeneratePolicyType.FullApp);
	}
	
	@Test(dependsOnMethods = { "testCreateStandardFireFA" })
	private void testAddStandardFireAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		
		//PR018	-	No. of Residence and properties
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOk();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr018ValMessage),
				"Expected page validation : '" + pr018ValMessage + "' is not displayed. /n");
		risk.clickClearButton();

		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.setUnits(NumberOfUnits.OneUnit);
		propertyDetail.clickOk();

		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(1);
		propertyLocations.clickOK();
        risk.Quote();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr018ValMessage),
				"Expected page validation : '" + pr018ValMessage + "' is not displayed. /n");
		risk.clickClearButton();
        sideMenu.clickSideMenuPropertyLocations();
		propertyLocations.clickEditLocation(1);
//        propertyLocations.setNumberOfResidence(15);
		
		//PR050	-	Property and acres
//		propertyLocations.setAcres(0);
		propertyLocations.clickOK();

		softAssert.assertFalse((!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr050ValMessage))), "Expected page validation : '"+pr050ValMessage + "' is not displayed." );
//		propertyLocations.setAcres(10);
		propertyLocations.clickOK();

		//PR075	-	Cov A min on Fire
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(14000);
		risk.Quote();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr075ValMessage),
				"Expected page validation : '" + pr075ValMessage + "' is not displayed. /n");
		risk.clickClearButton();
        sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1,1);
        coverages.setCoverageALimit(45000);
		
		//PR076	-	Cov E habitable min limit
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingPremisesCovE));
        coverages.setCoverageELimit(12000);
		risk.Quote();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr076ValMessage),
				"Expected page validation : '" + pr076ValMessage + "' is not displayed. /n");
		risk.clickClearButton();
        sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingPremisesCovE));
        coverages.setCoverageELimit(102000);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		softAssert.assertAll();
	}
}
