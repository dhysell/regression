package regression.r2.noclock.policycenter.busrulesuwissues.standardfire;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
/**
* @Author nvadlamudi
* @Requirement :Squire-Section I-Product-Model
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">Squire-Section I-Product-Model</a>
* @Description :Standard Fire - Availability rules are added 
* @DATE Oct 3, 2017
*/
public class TestStdFireAvailabilityRules extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();

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
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome);
		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumDwellingPremises);
		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.CondoVacationHomeCovE);
		property4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(6);
		locationsList.add(propLoc);

        myPolicyObjPL = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("BusRules", "Stdfire")
				.withInsAge(26).withPolOrgType(OrganizationType.Individual).withPolicyLocations(locationsList)
				.build(GeneratePolicyType.FullApp);
	}
	
	@Test(dependsOnMethods = {"testCreateStandardFireFA"})
	private void testAddStandardFireAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);
		String errorMessage = "";
		
		//PR040	-	Condo vacation construction type
		//PR083	-	Any Condo Property construction type
		sideMenu.clickSideMenuSquirePropertyDetail();

		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumVacationHome);
        propertyDetail.clickPropertyConstructionTab();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			if(!constu.equals(ConstructionTypePL.Frame.getValue()) && !constu.equals(ConstructionTypePL.NonFrame.getValue())){
					errorMessage = errorMessage + "UnExpected construction type : "+constu + "is displayed. \n";
			}
		}
		propertyDetail.clickOk();
		
		//PR042	-	Dwelling premises construction type
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			if(!constu.equals(ConstructionTypePL.Frame.getValue()) && !constu.equals(ConstructionTypePL.NonFrame.getValue()) && !constu.equals(ConstructionTypePL.ModularManufactured.getValue())){
				errorMessage = errorMessage + "UnExpected construction type : "+constu + "is displayed. \n";
			}
		}
		propertyDetail.clickOk();
		//PR045	-	Condo Dwelling Construction type
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumDwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			if(!constu.equals(ConstructionTypePL.Frame.getValue()) && !constu.equals(ConstructionTypePL.NonFrame.getValue())){
				errorMessage = errorMessage + "UnExpected construction type : "+constu + "is displayed. \n";
			}
		}
		propertyDetail.clickOk();
		
		//PR074	-	Mobile home on Cov A
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			if(constu.equals(ConstructionTypePL.MobileHome.getValue())){
				errorMessage = errorMessage + "UnExpected construction type : "+constu + "is displayed. \n";
			}
		}
		propertyDetail.clickOk();
		
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumDwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			if(constu.equals(ConstructionTypePL.MobileHome.getValue())){
				errorMessage = errorMessage + "UnExpected construction type : "+constu + "is displayed. \n";
			}
		}
		propertyDetail.clickOk();
		
		//PR077	-	if Property type = (Condo Vacation Home Cov E), then Construction Type = Mobile Home or Modular/Manufactured not available.
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondoVacationHomeCovE);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			if(constu.equals(ConstructionTypePL.MobileHome.getValue()) || constu.equals(ConstructionTypePL.ModularManufactured.getValue())){
				errorMessage = errorMessage + "UnExpected construction type : "+constu + "is displayed. \n";
			}
		}
		propertyDetail.clickOk();		
		
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		
		softAssert.assertFalse(errorMessage !="", errorMessage);
		softAssert.assertAll();
	}
}
