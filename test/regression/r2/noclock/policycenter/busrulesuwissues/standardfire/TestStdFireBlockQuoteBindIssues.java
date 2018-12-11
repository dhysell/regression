package regression.r2.noclock.policycenter.busrulesuwissues.standardfire;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.NumberOfUnits;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIDeductible;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PLPropertyCoverages;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailConstruction;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
@QuarantineClass
public class TestStdFireBlockQuoteBindIssues extends BaseTest {
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
		PLPolicyLocationProperty property2 = new PLPolicyLocationProperty(PropertyTypePL.VacationHome);
		PLPolicyLocationProperty property3 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremisesCovE);
		property3.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		locOnePropertyList.add(property1);
		locOnePropertyList.add(property2);
		locOnePropertyList.add(property3);
		PolicyLocation propLoc = new PolicyLocation(locOnePropertyList);
		propLoc.setPlNumAcres(10);
		propLoc.setPlNumResidence(10);
		locationsList.add(propLoc);

		StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
		myStandardFire.setLocationList(locationsList);

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
				.withStandardFire(myStandardFire)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsFirstLastName("BusRules", "Stdfire")
				.withInsAge(26)
				.withPolOrgType(OrganizationType.Individual)
				.build(GeneratePolicyType.FullApp);
	}

	@Test(dependsOnMethods = { "testCreateStandardFireFA" })
	private void testAddStandardFireAvailabilityRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);


		//PR001	-	$10,000 deductible
		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.selectSectionIDeductible(SectionIDeductible.TenThousand);

		//PR002	-	Property value greater than $750,000
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.DwellingPremises));
		coverages.setCoverageALimit(751000);

		//PR003	-	Only 4 Rental Units Allowed
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);

		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremisesCovE);
        propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOk();

		//PR004	-	More than 1 rental unit per property
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
        propertyDetail.setUnits(NumberOfUnits.FourUnits);
		propertyDetail.clickOk();

		//PR006	-	Cov A property prior to 1954
		sideMenu.clickSideMenuSquirePropertyDetail();
        propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.VacationHome);
        propertyDetail.clickPropertyConstructionTab();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.setYearBuilt(1950);
		propertyDetail.clickOk();
		propertyDetail.clickNext();

		//PR008	-	Add property over $1,500,000
		coverages.clickSpecificBuilding(1, 2);
		coverages.setCoverageALimit(1700000);


		//PR090	-	Defensible or not
		/*sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
				propertyDetail.clickPropertyConstructionTab();
		constructionPage.clickProtectionDetails();
		GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
		protectionPage.setDefensibleSpace(false);
		protectionPage.clickOK();*/
		//PR095	-	CLUE Property not ordered

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockSubmit = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.SectionIDed10KHigh);
				this.add(PLUWIssues.HighPropLimitGreater750K);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MoreThan1RentalUnit);
				this.add(PLUWIssues.CovABuildEarlier1954);	
			}
		};

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		for (PLUWIssues uwBlockSubmitExpected : sqSectionIBlockSubmit) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
					"Expected error Bind Submit : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		for (PLUWIssues uwIssueExpected : sqSectionIBlockIssue) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwIssueExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected error Bind Issuance : " + uwIssueExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.CluePropertyNotOrder.getLongDesc()).equals(UnderwriterIssueType.AlreadyApproved),
				"Expected error Bind Issuance : " + PLUWIssues.CluePropertyNotOrder.getShortDesc() + " is not displayed");
		softAssert.assertAll();
	}

	@Test
	public void testPolChangeStandardFireValidations() throws Exception {
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
		propLoc.setPlNumResidence(10);
		locationsList.add(propLoc);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Add", AdditionalNamedInsuredType.Affiliate,
				new AddressInfo(false));
		ani.setNewContact(CreateNew.Create_New_Always);
		PolicyInfoAdditionalNamedInsured aniRemove = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Remove", AdditionalNamedInsuredType.Affiliate,
				new AddressInfo(false));
		aniRemove.setNewContact(CreateNew.Create_New_Always);

		listOfANIs.add(ani);
		listOfANIs.add(aniRemove);

		StandardFireAndLiability myStandardFire = new StandardFireAndLiability();
		myStandardFire.setLocationList(locationsList);

        GeneratePolicy myPoliChangeObjPL = new GeneratePolicy.Builder(driver)
				.withStandardFire(myStandardFire)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withANIList(listOfANIs)
				.withInsFirstLastName("BusRules", "Stdfire")
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
        driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPoliChangeObjPL.standardFire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyCoverages();

		//PR056	-	ANI change to policy
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickANIContact("Add");
		GenericWorkorderAdditionalNamedInsured additionalNamedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
		additionalNamedInsured.setAdditionalInsuredLastName("Add Edit");
		additionalNamedInsured.clickUpdate();

		//PR057	-	PNI or ANI removed
		polInfo.removeANI("Remove");

		//PR059	-	Policy member contact change
		sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		hmember.clickPolicyHolderMembersByName(myPoliChangeObjPL.pniContact.getFirstName());
		additionalNamedInsured.setAdditionalInsuredMiddleName("M");
        polInfo.setReasonForContactChange("Testing purpose");
        additionalNamedInsured.clickUpdate();

		//PR060	-	New property added
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		PLPolicyLocationProperty condoProperty = new PLPolicyLocationProperty(PropertyTypePL.CondominiumDwellingPremises);
		condoProperty.setOwner(true);
		condoProperty.setConstructionType(ConstructionTypePL.Frame);
		sideMenu.clickSideMenuSquirePropertyDetail();

		propertyDetail.fillOutPropertyDetails_FA(condoProperty);
        GenericWorkorderSquirePropertyDetailConstruction constructionPage = new GenericWorkorderSquirePropertyDetailConstruction(driver);
		constructionPage.setCoverageAPropertyDetailsFA(condoProperty);
		propertyDetail.clickPropertyInformationDetailsTab();
		propertyDetail.AddExistingOwner();
		propertyDetail.clickOk();
		propertyDetail.clickNext();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, coverages.getBuildingNumber(PropertyTypePL.CondominiumDwellingPremises));
		coverages.setCoverageALimit(251000);
		coverages.setCoverageCLimit(251000);
		coverages.setCoverageCValuation(condoProperty.getPropertyCoverages());
		coverages.selectCoverageCCoverageType(CoverageType.BroadForm);

		//PR061	-	Existing property removed
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.removeBuilding("2");

		//PR064	-	Property detail change
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingByPropertyType(PropertyTypePL.DwellingPremises);
		propertyDetail.setUnits(NumberOfUnits.TwoUnits);
		propertyDetail.clickOk();

		//PR065	-	Property Additional insured change
		sideMenu.clickSideMenuSquirePropertyDetail();
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.addAddtionalInsured();
		propertyDetail.setAddtionalInsuredName("TestJobs");
		propertyDetail.clickOk();

		// PR068 - Coverage change
		sideMenu.clickSideMenuSquirePropertyCoverages();
		coverages.clickSpecificBuilding(1, 1);
		coverages.setCoverageALimit(200200);

		// PR067 - Section I deductible increase
		coverages.selectSectionIDeductible(SectionIDeductible.TenThousand);

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockSubmit = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.SectionIDed10KHigh);
			}
		};

		@SuppressWarnings("serial")
		List<PLUWIssues> sqSectionIBlockIssue = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MoreThan1RentalUnit);
				this.add(PLUWIssues.ANIChange);	
				this.add(PLUWIssues.SectionIDedIncrease);	
				this.add(PLUWIssues.Coveragechange);				
				this.add(PLUWIssues.AContactChanged);
				this.add(PLUWIssues.PropertyAddInsuredchange);
				this.add(PLUWIssues.NewPropertyAdded);
				this.add(PLUWIssues.PropertyDetailChange);
				this.add(PLUWIssues.ExistingPropRemoved);

			}
		};

		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		for (PLUWIssues uwBlockSubmitExpected : sqSectionIBlockSubmit) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockSubmitExpected.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
					"Expected error Bind Bind : " + uwBlockSubmitExpected.getShortDesc() + " is not displayed");
		}

		for (PLUWIssues uwIssueExpected : sqSectionIBlockIssue) {			
			softAssert.assertFalse(
					!uwIssues.isInList(uwIssueExpected.getLongDesc()).equals(UnderwriterIssueType.BlockIssuance),
					"Expected error Bind Issuance : " + uwIssueExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();
	}


	@Test()
	private void testStdFireWithTrellisedHopsRules() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy stdFire_Squire_PolicyObj = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withPolOrgType(OrganizationType.Individual)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("SQ", "Hops")
				.withPaymentPlanType(PaymentPlanType.getRandom())
				.build(GeneratePolicyType.FullApp);

		//standard fire
		ArrayList<PolicyLocation> locationsList1 = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList1 = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty newProp = new PLPolicyLocationProperty(PropertyTypePL.GrainSeed);
		newProp.getPropertyCoverages().getCoverageE().setCoverageType(CoverageType.Peril_1);
		locOnePropertyList1.add(newProp);
		PolicyLocation locToAdd1 = new PolicyLocation(locOnePropertyList1);

		locToAdd1.setPlNumAcres(11);
		locationsList1.add(locToAdd1);		

		PLPropertyCoverages propertyCoverages = new PLPropertyCoverages();
		propertyCoverages.getCoverageA().setIncreasedReplacementCost(false);
		driver.quit();

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		StandardFireAndLiability myStandardfire = new StandardFireAndLiability();
		myStandardfire.setLocationList(locationsList1);
		myStandardfire.setStdFireCommodity(true);		
		stdFire_Squire_PolicyObj.standardFire = myStandardfire;
		stdFire_Squire_PolicyObj.addLineOfBusiness(ProductLineType.StandardFire, GeneratePolicyType.FullApp);
		driver.quit();

		//PR087	-	Block Bind Trellised Hops
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchSubmission(stdFire_Squire_PolicyObj.agentInfo.getAgentUserName(), stdFire_Squire_PolicyObj.agentInfo.getAgentPassword(), stdFire_Squire_PolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.clickViewOrEditBuildingButton(1);
		propertyDetail.setPropertyType(PropertyTypePL.TrellisedHops);
		propertyDetail.clickOk();

		sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.setCoverageELimit(200000);
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		softAssert.assertFalse(
				!uwIssues.isInList(PLUWIssues.TrellisedHopExists.getLongDesc()).equals(UnderwriterIssueType.BlockSubmit),
				"Expected error Bind Issuance : " + PLUWIssues.TrellisedHopExists.getShortDesc() + " is not displayed");

		softAssert.assertAll();
	}

}
