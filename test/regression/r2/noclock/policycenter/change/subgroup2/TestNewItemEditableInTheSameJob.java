package regression.r2.noclock.policycenter.change.subgroup2;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyMenu;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityLocation;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyDetailProtectionDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : US8387 : New Item Information must be editable within that same job
 * @Description -  Issue Squire Policy, add a new item (vehicle, property, section 4 coverable, etc) within a Renewal and policy change job.
 * also be able to edit the new item until the job is bound/issued.
 * @DATE Aug 15, 2016
 */
public class TestNewItemEditableInTheSameJob extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObjPL;	
	private Underwriters uw;
	
	@Test()
	public void createPLAutoPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -285);
	
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);
	
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        myPolicyObjPL = new GeneratePolicy.Builder(driver)
			.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL)
			.withSquireEligibility(SquireEligibility.City)
			.withInsFirstLastName("Test", "Renew")					
			.withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
			.withPolicyLocations(locationsList)
			.withPolEffectiveDate(newEff)					
			.withPaymentPlanType(PaymentPlanType.Annual)
			.withDownPaymentType(PaymentType.Cash)
			.build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = {"createPLAutoPolicy"})
	public void verifyPolicyRenewEditPolicyNewItem() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		// Login with UW
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.squire.getPolicyNumber());

        PolicyMenu policyMenu = new PolicyMenu(driver);
		policyMenu.clickRenewPolicy();
        PolicyPreRenewalDirection preRenewalPage = new PolicyPreRenewalDirection(driver);
		preRenewalPage.closePreRenewalExplanations(myPolicyObjPL);

        SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
		searchJob.searchJobByAccountNumber(myPolicyObjPL.accountNumber, "003");
        SideMenuPC sideMenu = new SideMenuPC(driver);
		
		new GuidewireHelpers(driver).editPolicyTransaction();
		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		// Adding Vehicle
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.setNoDriverAssigned(true);
		vehiclePage.setVIN("DCBA4543543");
		vehiclePage.setModelYear(2000);
		vehiclePage.setMake("Honda");
		vehiclePage.setModel("Accord");
		vehiclePage.setFactoryCostNew(20000);
		vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setRadioLiabilityCoverage(false);
        vehicleCoverages.setComprehensive(true);
		vehicleCoverages.clickOK();	

		//Locations
		sideMenu.clickSideMenuSquirePropertyDetail();
		sideMenu.clickSideMenuLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation plProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		plProperty.clickEditLocation(1);
//		plProperty.setNumberOfResidence(3);
		plProperty.clickOK();
		//Property
		sideMenu.clickSideMenuSquirePropertyDetail();

		//Adding property 
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		propertyDetail.fillOutPropertyDetails_FA(property1);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property1);
		constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property1);
		protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 3);
		coverages.setCoverageALimit(40000);
        //		coverages.setCoverageCCoverageType("Broad Form");
		coverages.setCoverageAIncreasedReplacementCost(property1.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
		coverages.setCoverageCValuation(property1.getPropertyCoverages());
		coverages.clickQuote();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
			riskAnaysis.handleBlockSubmit(myPolicyObjPL);
            SideMenuPC sideMenuStuff = new SideMenuPC(driver);
			sideMenuStuff.clickSideMenuQuote();
		}
        sideMenu.clickSideMenuForms();
        sideMenu.clickSideMenuPayment();
	}

	@Test(dependsOnMethods = { "createPLAutoPolicy" })
	public void verifyPolicyChangeEditPolicyNewItem() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		quote.clickQuote();
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);

		polInfo.clickEditPolicyTransaction();

		sideMenu.clickSideMenuPolicyInfo();

		sideMenu.clickSideMenuPADrivers();
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        GenericWorkorderVehicles_CoverageDetails vehicleCoverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		// Adding Vehicle
		vehiclePage.createVehicleManually();
		vehiclePage.createGenericVehicle(VehicleTypePL.PassengerPickup);
		vehiclePage.setNoDriverAssigned(true);
		vehiclePage.setVIN("ABCD4543543");
		vehiclePage.setModelYear(2000);
		vehiclePage.setMake("Honda");
		vehiclePage.setModel("Accord");
		vehiclePage.setFactoryCostNew(20000);
		vehiclePage.selectGaragedAtZip(myPolicyObjPL.pniContact.getAddress().getCity());
		vehicleCoverages.selectVehicleCoverageDetailsTab();
        vehicleCoverages.setRadioLiabilityCoverage(false);
        vehicleCoverages.setComprehensive(true);
		vehicleCoverages.clickOK();

		//Locations
		sideMenu.clickSideMenuSquirePropertyDetail();
		sideMenu.clickSideMenuLocations();
        GenericWorkorderSquirePropertyAndLiabilityLocation plProperty = new GenericWorkorderSquirePropertyAndLiabilityLocation(driver);
		plProperty.clickEditLocation(1);
//		plProperty.setNumberOfResidence(3);
		plProperty.clickOK();
		//Property
		sideMenu.clickSideMenuSquirePropertyDetail();
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(driver);
		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.DwellingPremises);
		propertyDetail.fillOutPropertyDetails_FA(property1);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(driver);
        constructionPage.fillOutPropertyConstrustion_FA(property1);
		constructionPage.setLargeShed(false);
        constructionPage.clickProtectionDetailsTab();
        GenericWorkorderSquirePropertyDetailProtectionDetails protectionPage = new GenericWorkorderSquirePropertyDetailProtectionDetails(driver);
        new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_ProtectionDetails(driver).fillOutPropertyProtectionDetails(property1);
		protectionPage.clickOK();
        sideMenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(driver);
		coverages.clickSpecificBuilding(1, 2);
		coverages.setCoverageALimit(40000);
        //		coverages.setCoverageCCoverageType("Broad Form");
		coverages.setCoverageAIncreasedReplacementCost(property1.getPropertyCoverages().getCoverageA().isIncreasedReplacementCost());
		coverages.setCoverageCValuation(property1.getPropertyCoverages());
        quote.clickSaveDraftButton();
		quote.clickQuote();
		sideMenu.clickSideMenuForms();
		sideMenu.clickSideMenuPayment();
        GenericWorkorderPayment Payment = new GenericWorkorderPayment(driver);
		Payment.SubmitOnly();

	}	
}
