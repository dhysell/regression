package regression.r2.noclock.policycenter.busrulesuwissues.standardfire;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IssuanceType;
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
import repository.pc.account.AccountSummaryPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderLocations;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;

/**
* @Author nvadlamudi
* @Requirement :Squire-Section I-Product-Model
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/squire%20product%20model%20spreadhseets/Squire-Section%20I-Product-Model.xlsx">Squire-Section I-Product-Model</a>
* @Description :Standard Fire - Validations rules are added 
* @DATE Oct 3, 2017
*/
public class TestStdFireValidationRules extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	String pr016ValMessage = "Limit : Coverage C limit must be greater or equal to $15,000. (PR016)";
	String pr030ValMessage = "the square feet must be greater or equal to 400. If not then please move the property to Coverage E. (PR030)";
	String pr036ValMessage = "Modular/Manufactured Vacation Home cannot be older than 1984.(PR036)";
	String pr043ValMessage = "Modular/Manufactured Dwelling Premises cannot be older than 1985. (PR043)";
	String pr079ValMessage = "This building does not qualify for Coverage A. Please change the property type to a Coverage E Premises.";
	String pr089ValMessage = "is a PNI and cannot be removed";
	String pr092ValMessage = "A property exists on location #1. Please remove it before removing the location. (PR092)";
	String pr080ValMessage = "Risk and Category must be entered on all properties and Farm and Personal Property (if added) to issue policy. (PR080)";
	
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
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
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

	@Test(dependsOnMethods = { "testCreateStandardFireFA" })
	private void testAddStandardFireAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(driver);

		// PR016 - Coverage C Min Value
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageCLimit(14000);
		coverages.clickNext();
        softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr016ValMessage)),
				"Expected page validation : '" + pr016ValMessage + "' is not displayed. /n");

		coverages.setCoverageCLimit(25000);

		// PR030 - Mobile or Modular min sq. ft.
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setSquareFootage(300);
		constructionPage.clickOK();
        GenericWorkorderRiskAnalysis_UWIssues risk = new GenericWorkorderRiskAnalysis_UWIssues(driver);

		softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr030ValMessage),
				"Expected page validation : '" + pr030ValMessage + "' is not displayed. /n");
		risk.clickClearButton();

		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setSquareFootage(900);
		constructionPage.clickOK();

		// PR036 - Vacation home modular min year
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setYearBuilt(1980);
		constructionPage.clickOK();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr036ValMessage),
				"Expected page validation : '" + pr036ValMessage + "' is not displayed. /n");
		risk.clickClearButton();

		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setYearBuilt(2013);
		constructionPage.clickOK();

		// PR043 - Dwelling premises modular min year
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setConstructionType(ConstructionTypePL.ModularManufactured);
		constructionPage.setYearBuilt(1980);
		constructionPage.clickOK();
        softAssert.assertFalse(!risk.getValidationMessagesText().contains(pr043ValMessage),
				"Expected page validation : '" + pr043ValMessage + "' is not displayed. /n");
		risk.clickClearButton();

		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setYearBuilt(2013);
		constructionPage.clickOK();

		// PR079 - Vacant Cov A
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.setDwellingVacantRadio(true);
        softAssert.assertFalse(!propertyDetail.getPropertyValidationMessage().contains(pr079ValMessage),
				"Expected page validation : '" + pr079ValMessage + "' is not displayed. /n");
		propertyDetail.setDwellingVacantRadio(false);
		propertyDetail.clickOk();

		// PR089 - Cannot remove PNI or ANI
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers houseHoldMember = new GenericWorkorderPolicyMembers(driver);
		houseHoldMember.clickRemoveMember(this.myPolicyObjPL.pniContact.getLastName());
        softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr089ValMessage)),
				"Expected page validation : '" + pr089ValMessage + "' is not displayed. /n");

		// PR092 - Property on location
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderLocations myLoc = new GenericWorkorderLocations(driver);
		myLoc.removeAllLocations();
        softAssert.assertFalse(!new GuidewireHelpers(driver).errorMessagesExist() && (!new GuidewireHelpers(driver).getFirstErrorMessage().contains(pr092ValMessage)),
				"Expected page validation : '" + pr092ValMessage + "' is not displayed. /n");

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

		risk.performRiskAnalysisAndQuote(myPolicyObjPL);

        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.hasBlockBind()) {
			risk.handleBlockSubmit(myPolicyObjPL);
		}
		new GuidewireHelpers(driver).logout();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        new GuidewireHelpers(driver).setPolicyType(myPolicyObjPL, GeneratePolicyType.FullApp);
		myPolicyObjPL.convertTo(driver, GeneratePolicyType.PolicySubmitted);
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(myPolicyObjPL.underwriterInfo.getUnderwriterUserName(),
				myPolicyObjPL.underwriterInfo.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");
		// PR080 - No Risk on Property or FPP
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuQuote();
        quotePage = new GenericWorkorderQuote(driver);
		quotePage.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderRiskAnalysis riskPage = new GenericWorkorderRiskAnalysis(driver);

		softAssert.assertFalse(!riskPage.getValidationMessagesText().contains(pr080ValMessage),
				"Expected page validation : '" + pr080ValMessage + "' is not displayed. /n");
		riskPage.clickClearButton();
		
		softAssert.assertAll();

	}
	
}
