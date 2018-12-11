package regression.r2.noclock.policycenter.change.subgroup6;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
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
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation;

/**
 * @Author nvadlamudi
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 29, 2016
 */
public class TestSquirePolicyChangePropertyConstruction extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;

	@Test()
	public void testIssueSquireSectionIPol() throws Exception {
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
		locToAdd.setPlNumResidence(3);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);
		squirePersonalAuto.setDriversList(driversList);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Test", "PropCons")
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

		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingUnderConstruction);
		int yearField = DateUtils.getCenterDateAsInt(cf, ApplicationOrCenter.PolicyCenter, "yyyy");
		property1.setYearBuilt(yearField);

		propertyDetail.fillOutPropertyDetails_FA(property1);
		GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
		constructionPage.fillOutPropertyConstrustion_FA(property1);
		constructionPage.setLargeShed(false);

        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);

		propertyDetail.clickPropertyInformationDetailsTab();
        propertyDetail.setRisk("A");
		protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 2);
        coverages.setCoverageALimit(120000);
		coverages.setCoverageCValuation(property1.getPropertyCoverages());
        quote.clickSaveDraftButton();
		
		sideMenu.clickSideMenuPropertyLocations();
		GenericWorkorderSquirePropertyLocation propertyLocation = new GenericWorkorderSquirePropertyLocation(driver);
		propertyLocation.clickEditLocation(myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getNumber());
		propertyLocation.setNumberOfResidence(2);
		propertyLocation.clickOK();

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
}
