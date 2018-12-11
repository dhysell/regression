package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.Measurement;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;

import java.util.ArrayList;
import java.util.List;

/**
* @Author nvadlamudi
* @Requirement : US15545: Disable/change UW Issues - Section 1 & 2 (new activity)
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EfkD5tWiAwlAq85ITIpy3M8B9Hg5URkky9PGfOGjeHJQnA?e=u0CR2b">UW Issues to Change for Auto Issue</a>
* @Description : validate PGL009, PGL012, PGL014, PGL015, PR003, PR006, PR007, PR008, PR010, PR056, PR060, PR064, PR065, PR067, PR068, PR070, PR090
* @DATE Jul 31, 2018
*/
public class US15545DisableChangeSection1And2UWIssues extends BaseTest {
	SoftAssert softAssert = new SoftAssert();
	WebDriver driver;

	@Test
	public void testCheckSection1And2UWIssuesInCitySubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		softAssert = new SoftAssert();
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		property3.setConstructionType(ConstructionTypePL.Frame);
		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.Garage);
		property4.setConstructionType(ConstructionTypePL.Frame);
		property4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.BroadForm);

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		locOnePropertyList.add(property4);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		// PGL009 City acre over 10
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		Squire mySquire = new Squire(SquireEligibility.City);

		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Sec1N2", "UWChange")
				.withSocialSecurityNumber("4" + StringsUtils.generateRandomNumberDigits(8)).isDraft()
				.build(GeneratePolicyType.FullApp);

		// adding all the section I & Section II Coverages
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(
				driver);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPropertyLocations();
		propertyLocations.clickEditLocation(1);
		propertyLocations.setAcresAndResidents(12, 6);
		propertyLocations.clickOK();

		sideMenu.clickSideMenuSquirePropertyCoverages();
		propCoverages.clickSectionIICoveragesTab();

		// PGL014 Incidental occupancy coverage
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.IncidentalOccupancy, 0, 2);
		addCoverage.addCoverages(myCoverages);
		addCoverage.addIncidentalOccupancy("Testing Desc",
				myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getAddress().getCity());

		// PGL015 Watercraft length of over 40.
		addCoverage.addWatercraftLengthCoverage("Testing Desc", 42);

		// PR003 Only 4 Rental Units Allowed
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();

		// PR006 Cov A property prior to 1954
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
		propertyDetail.clickPropertyConstructionTab();
		constructionPage.setYearBuilt(1950);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();

		// PR007 Property over 20 years and Cov E
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.Garage);
		propertyDetail.clickPropertyConstructionTab();
		int yearField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy");
		constructionPage.setYearBuilt(yearField - 25);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();
		propertyDetail.clickNext();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Garage));
		coverages.setCoverageECoverageType(CoverageType.BroadForm);
		coverages.setCoverageELimit(850000);

		// PR010 Vacant property
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
		propertyDetail.setDwellingVacantRadio(true);

		// PR090 Defensible or not
		constructionPage.clickProtectionDetailsTab();
		protectionPage.setDefensibleSpace(false);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();		

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		@SuppressWarnings("serial")
		List<PLUWIssues> sectionIIIBlockInfo = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.CityAcreageOver10);
				this.add(PLUWIssues.IncidentalOccupancyCoverageExists);
				this.add(PLUWIssues.WatercraftLengthOver40FT);
				this.add(PLUWIssues.CovABuildEarlier1954);
				this.add(PLUWIssues.VacantProperty);
				this.add(PLUWIssues.DefensibleNot);
				this.add(PLUWIssues.PropertyOver20YearsAndCovE);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Expected UW Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();

	}

	@Test
	public void testCheckSection1And2UWIssuesInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		softAssert = new SoftAssert();
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		property3.setYearBuilt(1950); // PR006 Cov A property prior to 1954		
		property3.setConstructionType(ConstructionTypePL.Frame);

		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);

		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		// PGL012 Insufficient acres for this line of business
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.section1Deductible = SectionIDeductible.OneThousand;
		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Section1", "Change")
				.withSocialSecurityNumber("4" + StringsUtils.generateRandomNumberDigits(8))
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(
				driver);
		GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(
				driver);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		// PR056 ANI change to policy
		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickANIContact("Add");

		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Add", AdditionalNamedInsuredType.Affiliate,
				new AddressInfo(false));
		ani.setNewContact(CreateNew.Create_New_Always);
		GenericWorkorderPolicyInfoAdditionalNamedInsured editANIPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(
				driver);
		editANIPage.clicknPolicyInfoAdditionalNamedInsuredsSearch();
		editANIPage.createNewContact(true, ani);
		sideMenu.clickSideMenuHouseholdMembers();
		
		sideMenu.clickSideMenuPropertyLocations();
		propertyLocations.clickEditLocation(1);
		propertyLocations.setAcresAndResidents(6, 10);
		propertyLocations.clickOK();

		// PR060 New property added
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickAdd();
		PLPolicyLocationProperty property4 = new PLPolicyLocationProperty(PropertyTypePL.Garage);
		property4.setConstructionType(ConstructionTypePL.Frame);
		property4.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.BroadForm);

		propertyDetail.setPropertyType(property4.getpropertyType());
		propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot3);
		propertyDetail.clickPropertyConstructionTab();
		int yearField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy");
		constructionPage.setYearBuilt(yearField - 25);
		constructionPage.setConstructionType(ConstructionTypePL.Frame);
		constructionPage.setSquareFootage(1200);
		constructionPage.setMeasurement(Measurement.SQFT);
		constructionPage.setFoundationType(FoundationType.Foundation);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();
		propertyDetail.clickNext();
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.Garage));
		coverages.setCoverageEValuation(ValuationMethod.ActualCashValue);
		coverages.setCoverageECoverageType(CoverageType.BroadForm);
		// PR008 Add property over $1,500,000
		coverages.setCoverageELimit(1600000);

		// PR064 Property detail change
		// PGL014 Incidental occupancy coverage
		sideMenu.clickSideMenuSquirePropertyCoverages();
		propCoverages.clickSectionIICoveragesTab();
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.IncidentalOccupancy, 0, 2);
		addCoverage.addCoverages(myCoverages);
		addCoverage.addIncidentalOccupancy("Testing Desc",
				myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getAddress().getCity());

		// PGL015 Watercraft length of over 40.
		addCoverage.addWatercraftLengthCoverage("Testing Desc", 42);
		
		// PR067 Section I deductible increase
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.selectSectionIDeductible(SectionIDeductible.FiveHundred);
		
		// PR070 Section II coverage decrease		
		propCoverages.clickSectionIICoveragesTab();
		addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_25_50_25);
		
		// PR003 Only 4 Rental Units Allowed
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();

		
		// PR010 Vacant property
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
		propertyDetail.setDwellingVacantRadio(true);

		// PR090 Defensible or not
		constructionPage.clickProtectionDetailsTab();
		protectionPage.setDefensibleSpace(false);
		
		
		// PR065 Property Additional insured change
		propertyDetail.clickPropertyInformationDetailsTab();
		propertyDetail.addAddtionalInsured();	       
		propertyDetail.setAddtionalInsuredName("Testing Change");	
		propertyDetail.clickOK();
		propertyDetail.clickOkayIfMSPhotoYearValidationShows();			
				
		// PR068 Coverage change
		sideMenu.clickSideMenuSquirePropertyCoverages();
		 coverages.clickSpecificBuilding(1, 1);
		 coverages.setCoverageALimit(200000);

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
		
		@SuppressWarnings("serial")
		List<PLUWIssues> sectionIIIBlockInfo = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.InsufficientAcreageForLOB);
				this.add(PLUWIssues.IncidentalOccupancyCoverageExists);
				this.add(PLUWIssues.WatercraftLengthOver40FT);
				this.add(PLUWIssues.CovABuildEarlier1954);
				this.add(PLUWIssues.PropertyOver20YearsAndCovE);
				this.add(PLUWIssues.HighValuePropertyAdded);
				this.add(PLUWIssues.VacantProperty);
				this.add(PLUWIssues.ANIChange);
				this.add(PLUWIssues.PropertyDetailChange);
				this.add(PLUWIssues.PropertyAddInsuredchange);
				this.add(PLUWIssues.Coveragechange);
				this.add(PLUWIssues.SectionIICoverageDecrease);
				this.add(PLUWIssues.DefensibleNot);
				
			}
		};

		for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Policy Change - Expected UW Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();

	}
}
