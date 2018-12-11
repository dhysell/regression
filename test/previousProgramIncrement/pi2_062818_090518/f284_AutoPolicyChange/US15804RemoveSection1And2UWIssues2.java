package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.SquirePropertyLiabilityExclusionsConditions;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderLineSelection;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderVehicles;

import java.util.ArrayList;
import java.util.List;

/**
* @Author nvadlamudi
* @Requirement :US15804: Disable/change UW Issues - Section 1 & 2 (remove completely)
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/r/sites/ARTists2/_layouts/15/Doc.aspx?sourcedoc=%7B45AD1E27-B290-4BBD-AD76-BC67A17BD898%7D&file=UW%20Issues%20to%20Change%20for%20Auto%20Issue.xlsx&action=default&mobileredirect=true">UW Issues to Change for Auto Issue</a>
* @Description : Validate PR004, PR005, PR009, PR053, PR054, PR061, PR071 UW Issues changes part of this user story
* @DATE Aug 1, 2018
*/
public class US15804RemoveSection1And2UWIssues2 extends BaseTest {
	SoftAssert softAssert = new SoftAssert();
	WebDriver driver;

//	@Test
//	public void testRemoveSection1And2UWIssuesInSubmission() throws Exception {
//		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
//		driver = buildDriver(cf);
//		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
//		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
//
//		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
//		locOnePropertyList.add(property1);
//		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
//		locToAdd.setPlNumAcres(11);
//		locationsList.add(locToAdd);
//
//		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
//		myPropertyAndLiability.locationList = locationsList;
//		Squire mySquire = new Squire(SquireEligibility.Country);
//
//		mySquire.squirePA = new SquirePersonalAuto();
//		mySquire.propertyAndLiability = myPropertyAndLiability;
//
//		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
//				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
//				.withInsFirstLastName("Sec1N2", "Remove").isDraft()
//				.build(GeneratePolicyType.FullApp);
//
//		// adding all the section I & Section II Coverages
//		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
//				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
//
//		// PR004 - More than 1 rental unit per property
//		SideMenuPC sideMenu = new SideMenuPC(driver);
//		sideMenu.clickSideMenuSquirePropertyDetail();
//		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
//				driver);
//		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.ResidencePremises);
//		propertyDetail.setUnits(NumberOfUnits.FourUnits);
//		propertyDetail.clickOk();
//		propertyDetail.clickOkayIfMSPhotoYearValidationShows();
//
//		// PR005 - Additional gun coverage high limit
//		sideMenu.clickSideMenuSquirePropertyCoverages();
//		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
//				driver);
//		coverages.setGunIncreasedTheftLimit(20200);
//
//		// PR009 - Adding Endorsement 209
//		coverages.clickCoveragesExclusionsAndConditions();
//		coverages.clickAccessYesEndorsement209();
//		
//		sideMenu.clickSideMenuPropertyLocations();		
//		GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(
//				driver);
//		propertyLocations.clickEditLocation(1);
//		propertyLocations.setAcresAndResidents(1, 10);
//		propertyLocations.clickOK();
//		
//		sideMenu.clickSideMenuRiskAnalysis();
//		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
//
//		risk.Quote();
//		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
//		if (quotePage.isPreQuoteDisplayed()) {
//			quotePage.clickPreQuoteDetails();
//		}
//
//		sideMenu.clickSideMenuRiskAnalysis();
//		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();
//		@SuppressWarnings("serial")
//		List<PLUWIssues> sectionIIIBlockInfo = new ArrayList<PLUWIssues>() {
//			{
//				this.add(PLUWIssues.MoreThan1RentalUnit);
//				this.add(PLUWIssues.GunCoverage20KMore);
//				this.add(PLUWIssues.AccessYesEndoAdded);
//			}
//		};
//
//		for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
//			softAssert.assertFalse(uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
//					"Unexpected UW Informational : " + uwBlockBindExpected.getShortDesc() + " is displayed");
//		}
//
//		softAssert.assertAll();
//
//	}

	@Test
	public void testRemoveSection1And2UWIssuesInPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);

		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyLiabilityExclusionsConditions myPropLiabExcCond = new SquirePropertyLiabilityExclusionsConditions();
		myPropLiabExcCond.setConditionAccessYesEndorsement209(true);
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.propLiabExclusionsConditions = myPropLiabExcCond;

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Section1", "Remove")
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
	
		// PR054 Removing section
		sideMenu.clickSideMenuPAVehicles();
		GenericWorkorderVehicles vehicalTab = new GenericWorkorderVehicles(driver);
		vehicalTab.removeAllVehicles();
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderSquireAutoDrivers paDrivers = new GenericWorkorderSquireAutoDrivers(driver);
		paDrivers.setCheckBoxInDriverTable(1);
		paDrivers.clickRemoveButton();
		sideMenu.clickSideMenuLineSelection();
		GenericWorkorderLineSelection lineSelection = new GenericWorkorderLineSelection(driver);
		lineSelection.checkPersonalAutoLine(false);

		// PR061 Existing property removed
		sideMenu.clickSideMenuSquirePropertyDetail();
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(
				driver);
		propertyDetail.setCheckBoxByRowInPropertiesTable(2, true);
		propertyDetail.clickRemoveProperty();

		// PR071 Access Yes removed
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(
				driver);
		coverages.clickCoveragesExclusionsAndConditions();
		coverages.clickAccessYesEndorsement209();
		
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
				this.add(PLUWIssues.RemoveSection);
				this.add(PLUWIssues.ExistingPropRemoved);
				this.add(PLUWIssues.AccessYesEndoAdded);
			}
		};

		for (PLUWIssues uwBlockBindExpected : sectionIIIBlockInfo) {
			softAssert.assertFalse(
					uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.Informational),
					"Unexpected UW Informational : " + uwBlockBindExpected.getShortDesc() + " is displayed");
		}

		softAssert.assertAll();

	}
}
