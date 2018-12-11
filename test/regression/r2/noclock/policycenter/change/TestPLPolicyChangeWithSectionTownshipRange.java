package regression.r2.noclock.policycenter.change;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.ProtectionClassCode;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * This test might fail due to location number sequential issue.
 */

/**
 * @Author nvadlamudi
 * @Requirement : DE4207 : Charge Group Apprearing as Section, Township and
 *              Range
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 29, 2016
 */
public class TestPLPolicyChangeWithSectionTownshipRange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;
	String[] sectionsArray = { "01", "02", "03", "04", "05", "06", "07" };
	ArrayList<String> sections = new ArrayList<String>();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass(){
		sections.add(sectionsArray[1]);	
	}

	@Test()
	public void testIssueSquireSectionIPol() throws Exception {
		for(int i = 0; i<sectionsArray.length; i++){
			sections.add(sectionsArray[i]);
		}
				
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// Coverages
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
				MedicalLimit.TenK);
		coverages.setUnderinsured(false);
		coverages.setAccidentalDeath(true);

		// driver
		ArrayList<Contact> driversList = new ArrayList<Contact>();
		Contact person = new Contact("Test" + StringsUtils.generateRandomNumberDigits(6), "Auto", Gender.Male,
				DateUtils.convertStringtoDate("01/01/1979", "MM/dd/YYYY"));
		person.setMaritalStatus(MaritalStatus.Married);
		person.setRelationToInsured(RelationshipToInsured.Insured);
		person.setOccupation("Software");
		driversList.add(person);

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
		Vehicle toAdd = new Vehicle();
		toAdd.setEmergencyRoadside(true);
		vehicleList.add(toAdd);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.squireFPP = squireFPP;
        property.liabilitySection = myLiab;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.propertyAndLiability = property;
        mySquire.squirePA = squirePersonalAuto;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "ChargeGroup")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testIssueSquireSectionIPol" })
	public void testPolicyChangeWithSectionTownshipRange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.underwriterInfo.getUnderwriterUserName(),
                myPolicyObjPL.underwriterInfo.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 1);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

		// Adding new location
		sideMenu.clickSideMenuPropertyLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation propertyLocations = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		propertyLocations.clickNewLocation();
//        propertyLocations.selectCounty("Bannock");
//
//		propertyLocations.addSection(sections);
//		propertyLocations.selectTownshipNumber("05");
//		propertyLocations.selectTownshipDirection("S");
//		propertyLocations.selectRangeNumber("32");
//		propertyLocations.selectRangeDirection("E");
//		propertyLocations.setAcres(2);
//		propertyLocations.setNumberOfResidence(2);
        propertyLocations.clickOK();

		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		propertyDetail.highLightPropertyLocationByNumber(3);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		propertyDetail.fillOutPropertyDetails_FA(property1);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.fillOutPropertyConstrustion_FA(property1);
		constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property1);
		propertyDetail.clickPropertyInformationDetailsTab();
        propertyDetail.setManualPublicProtectionClassCode(ProtectionClassCode.Prot5);
        propertyDetail.addExistingAdditionalInterest(this.myPolicyObjPL.squire.squirePA.getDriversList().get(0).getLastName());
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
		additionalInterests.checkBuildingsPropertyAdditionalInterestsFirstMortgage(true);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(3, 2);
        coverages.setCoverageALimit(120000);
        coverages.setCoverageAIncreasedReplacementCost(false);
		coverages.setCoverageCValuation(property1.getPropertyCoverages());
        quote.clickSaveDraftButton();

		sideMenu.clickSideMenuPayerAssignment();

		GenericWorkorderPayerAssignment payerAssignment = new GenericWorkorderPayerAssignment(driver);
		payerAssignment.setPayerAssignmentBillAllBuildingOrPropertyCoverages(3, 2,
				this.myPolicyObjPL.squire.squirePA.getDriversList().get(0).getLastName(), true, true);
		payerAssignment.setPayerAssignmentVerificationConfirmationCheckbox(true);

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

        if (quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
        }
		sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis riskAnaysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnaysis.approveAll_IncludingSpecial();

        sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();

        StartPolicyChange change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

	@Test(dependsOnMethods = { "testPolicyChangeWithSectionTownshipRange" })
	public void testValidateBCChargeGroup() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		ARUsers arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObjPL.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuCharges();
		String expectedString = "";
		for (int i = 0; i < sections.size(); i++) {
			expectedString = expectedString + sections.get(i) + "-";
		}
		expectedString = StringUtils.substring(expectedString, 0, (expectedString.length() - 1));
        AccountCharges charges = new AccountCharges(driver);
		if (!new TableUtils(driver).getCellTextInTableByRowAndColumnName(charges.getChargesOrChargeHoldsPopupTable(), new TableUtils(driver).getRowNumberInTableByText(charges.getChargesOrChargeHoldsPopupTable(), "001-01"), "Charge Group").contains(expectedString)) {
			Assert.fail("Expected Section Township Range Location is not displayed in change group");
		}
	}
}
