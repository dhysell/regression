package regression.r2.noclock.policycenter.busrulesuwissues.squiresection2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIICoveragesEnum;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SectionIICoverages;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

public class TestSectionTwoBlockBindIssues extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObjPL = null;
	SoftAssert softAssert = new SoftAssert();
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() {
		softAssert = new SoftAssert();
	}

	@Test
	public void testGenerateSquireWithSectionTwo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquireEligibility squireType = (new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.City
				: ((new GuidewireHelpers(driver).getRandBoolean()) ? SquireEligibility.Country : SquireEligibility.FarmAndRanch);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setOwner(true);
		property1.setConstructionType(ConstructionTypePL.Frame);
		property1.setNumberOfUnits(NumberOfUnits.OneUnit);

		locOnePropertyList.add(property1);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(8);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(squireType);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("BlockIssue", "Two")
                .build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testGenerateSquireWithSectionTwo" })
	private void testAddBlockBindQuoteIssues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(),
				myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickEditLocation(1);

//		if (this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
//			// PGL009 City acre over 10
//			propertyLocations.setAcres(12);
//		} else {
//			// PGL012 Insufficient acres for this line of business
//
//			propertyLocations.setAcres(8);
//		}
//		propertyLocations.clickOK();
//
//		// PGL008 State is not Idaho
//
//		sideMenu.clickSideMenuPropertyLocations();
//		propertyLocations.clickNewLocation();
//		propertyLocations.selectAddressType(PLLocationType.OutOfState.getName());
//        propertyLocations.selectState(State.Nevada);
//		propertyLocations.setAcres(2);
//		propertyLocations.setCounty("test");
//		propertyLocations.setAddressLegal("Testing Legal");
		propertyLocations.clickOK();

		// PGL005 Dairy confinement, UW approval
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		propCoverages.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        if (this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)) {
			addCoverage.clickPollutionLiabilityTableCheckbox(3, true);
			addCoverage.setPollutionLiabilityQuantity(3, 5);
		}
		// PGL014 Incidental occupancy coverage
		SectionIICoverages myCoverages = new SectionIICoverages(SectionIICoveragesEnum.IncidentalOccupancy, 0, 2);
		addCoverage.addCoverages(myCoverages);
		addCoverage.addIncidentalOccupancy("Testing Desc", myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getAddress().getCity());

		// PGL010 $1 million liability limit
		if (this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)) {
			addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);
		}

		// PGL015 Watercraft length of over 40.
		addCoverage.addWatercraftLengthCoverage("Testing Desc", 42);

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIBlockBind = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.DairyConfinementAdded);
				this.add(PLUWIssues.StateIsNotIdaho);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.InsufficientAcreageForLOB);
				this.add(PLUWIssues.IncidentalOccupancyCoverageExists);
				this.add(PLUWIssues.WatercraftLengthOver40FT);
				this.add(PLUWIssues.CityAcreageOver10);
			}
		};

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		for (PLUWIssues uwBlockBindExpected : sqSectionIIBlockBind) {
			if (uwBlockBindExpected.equals(PLUWIssues.DairyConfinementAdded) && !this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.FarmAndRanch)){
					continue;
			}

			softAssert.assertFalse(
						!uwIssues.isInList(uwBlockBindExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
						"Expected error Bind : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
			
		}

		for (PLUWIssues uwIssueExpected : sqSectionIIBlockIssue) {
			if (uwIssueExpected.getShortDesc().equals(PLUWIssues.InsufficientAcreageForLOB.getShortDesc()) && this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
				continue;
			}
			if (uwIssueExpected.getShortDesc().equals(PLUWIssues.CityAcreageOver10.getShortDesc()) && !this.myPolicyObjPL.squire.squireEligibility.equals(SquireEligibility.City)) {
				continue;
			}
			softAssert.assertFalse(!uwIssues.isInList(uwIssueExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
						"Expected error Issuance : " + uwIssueExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();
	}
	
	@Test
	public void testIssueSquireWithSectionTwo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setOwner(true);
		property1.setConstructionType(ConstructionTypePL.Frame);
		property1.setNumberOfUnits(NumberOfUnits.OneUnit);

		locOnePropertyList.add(property1);

		PolicyLocation location1 = new PolicyLocation(locOnePropertyList);
		location1.setPlNumAcres(10);
		location1.setPlNumResidence(8);
		locationsList.add(location1);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
		squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
		squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myChangePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("BlockIssue", "Two")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myChangePolicyObjPL.squire.getPolicyNumber());
		
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();
		GenericWorkorderSquirePropertyAndLiabilityCoverages propCoverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
		propCoverages.clickSectionIICoveragesTab();
		GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages addCoverage = new GenericWorkorderSquirePropertyAndLiabilityCoverages_SectionIICoverages(driver);
        addCoverage.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_1000000_CSL);
		
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		
		softAssert.assertFalse(!uwIssues.isInList(PLUWIssues.LiabilityLimitAtOneMillion.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Issuance : " + PLUWIssues.LiabilityLimitAtOneMillion.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}


}
