package regression.r2.noclock.policycenter.busrulesuwissues.squiresection1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.CoverageType;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;

/**
* @Author nvadlamudi
* @Requirement :Squire-Section I-Product-Model
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">Squire-Section I-Product-Model</a>
* @Description :Section I - Avalability rules are added 
* @DATE Sep 6, 2017
*/
public class TestSectionOneAvailability extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	public SoftAssert softAssert = new SoftAssert();

	@Test
	public void testGenerateSquireWithSectionOne() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireEligibility squireType = (new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.City
				: ((new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setOwner(true);
		property1.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumResidencePremise);
		property2.setOwner(true);
		property2.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		property3.setOwner(true);
		property3.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumVacationHome);
		property4.setOwner(true);
		property4.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property5 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		property5.setOwner(true);
		property5.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property6 = new PLPolicyLocationProperty(PropertyTypePL.CondominiumDwellingPremises);
		property6.setOwner(true);
		property6.setConstructionType(ConstructionTypePL.Frame);

		PLPolicyLocationProperty property7 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstruction);
		property7.setOwner(true);
		property7.setConstructionType(ConstructionTypePL.Frame);
		property7.setYearBuilt(DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy"));

		PLPolicyLocationProperty property8 = new PLPolicyLocationProperty(PropertyTypePL.CondoVacationHomeCovE);
		property8.setOwner(true);
		property8.setConstructionType(ConstructionTypePL.Frame);
		property8.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		locOnePropertyList.add(property5);
		locOnePropertyList.add(property6);
		locOnePropertyList.add(property7);
		locOnePropertyList.add(property8);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(18);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;
        property.squireFPP = squireFPP;

        Squire mySquire = new Squire(squireType);
        mySquire.propertyAndLiability = property;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Availability", "One")
				.isDraft()
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSquireWithSectionOne" })
	private void testAddBlockBindQuoteIssues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();

		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail(driver);
		
		//PR033	-	Condo Residence and Frame/Non-Frame
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumResidencePremise);
        propertyDetail.clickPropertyConstructionTab();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		List<String> constructionValues = constructionPage.getConstructionTypeValues();
		propertyDetail.clickOk();
		for(String constu:   constructionValues ){
			softAssert.assertTrue(constu.equals(ConstructionTypePL.Frame.getValue()) || constu.equals(ConstructionTypePL.NonFrame.getValue()), "Condominium Residence Premises - UnExpected construction type : "+constu + " is displayed.");
		}
	
		//PR040	-	Condo vacation construction type
		//PR083	-	Any Condo Property construction type
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumVacationHome);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertTrue(constu.equals(ConstructionTypePL.Frame.getValue()) || constu.equals(ConstructionTypePL.NonFrame.getValue()), "Condominium Vacation Home - UnExpected construction type : "+constu + " is displayed.");
		}
		propertyDetail.clickOk();
		
		//PR042	-	Dwelling premises construction type		
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertTrue(constu.equals(ConstructionTypePL.Frame.getValue()) || constu.equals(ConstructionTypePL.NonFrame.getValue()) || constu.equals(ConstructionTypePL.ModularManufactured.getValue()), "Dwelling Premises - UnExpected construction type : "+constu + " is displayed.");
		}
		
		propertyDetail.clickOk();
		
		//PR045	-	Condo Dwelling Construction type
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumDwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertTrue(constu.equals(ConstructionTypePL.Frame.getValue()) || constu.equals(ConstructionTypePL.NonFrame.getValue()), "Condominium Dwelling Premises - UnExpected construction type : "+constu + "is displayed.");
		}
		propertyDetail.clickOk();
		
		
		//PR038	-	Vacation home mobile w/ foundation
		//PR047	-	Dwelling Under Construction  type
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingUnderConstruction);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertTrue(constu.equals(ConstructionTypePL.Frame.getValue()) || constu.equals(ConstructionTypePL.NonFrame.getValue()), "Dwelling Under Construction - UnExpected construction type : "+constu + " is displayed.");
		}
		propertyDetail.clickOk();
		
		//PR074	-	Mobile home on Cov A
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertFalse(constu.equals(ConstructionTypePL.MobileHome.getValue()), "Dwelling Premises - UnExpected construction type : "+constu + " is displayed.");
		}
		propertyDetail.clickOk();
		
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumDwellingPremises);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertFalse(constu.equals(ConstructionTypePL.MobileHome.getValue()), "Condominium Dwelling Premises - UnExpected construction type : "+constu + " is displayed.");
		}
		propertyDetail.clickOk();
		
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertFalse(constu.equals(ConstructionTypePL.MobileHome.getValue()), "Vacation Home - UnExpected construction type : "+constu + " is displayed.");
		}
		propertyDetail.clickOk();
		
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondominiumVacationHome);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertFalse(constu.equals(ConstructionTypePL.MobileHome.getValue()), "Condominium Vacation Home - UnExpected construction type : "+constu + " is displayed.");
		}
		propertyDetail.clickOk();
		
		//PR077	-	if Property type = (Condo Vacation Home Cov E), then Construction Type = Mobile Home or Modular/Manufactured not available.
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.CondoVacationHomeCovE);
        propertyDetail.clickPropertyConstructionTab();
		for(String constu:   constructionPage.getConstructionTypeValues() ){
			softAssert.assertFalse(constu.equals(ConstructionTypePL.MobileHome.getValue()) || constu.equals(ConstructionTypePL.ModularManufactured.getValue()), "Condo Vacation Home Cov E - UnExpected construction type : "+constu + " is displayed.");
		}
		
		softAssert.assertAll();
	}

}
