package regression.r2.noclock.policycenter.submission_misc.subgroup4;

import java.util.ArrayList;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.FPP.FPPCoverageTypes;
import repository.gw.enums.FPP.FPPDeductible;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.FPP.FarmPersonalPropertyTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UnderinsuredLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.UninsuredLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
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
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages_Coverages;
import repository.pc.workorders.generic.GenericWorkorderSquireFPPAdditionalInterest;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author skandibanda
 * @Requirement : US9829 Update to Section III Medical limit, FPP Additional Interest, and Coverage C Limit
 * @Description : Checking for screen level validation (validation is per property) if the Coverage C limit < $15,000
 * Checking for  quote level validation if the additional interest = Lessor is not attached to at least one scheduled item
 * and checking for PGL013 will be removed for Underwriting rule
 * @DATE Jan 12, 2017
 */
public class TestSquireCoverageCLimitScreenValidationsUWIssues extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj;

    private WebDriver webDriver;

    @Test
    public void testGenerateSquirePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        webDriver = driver = buildDriver(cf);
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        String randFirst = StringsUtils.generateRandomNumberDigits(4);
        Contact driver = new Contact("driver-" + randFirst, "motorcycle", Gender.Female, DateUtils.convertStringtoDate("06/01/1980", "MM/dd/YYYY"));
        driversList.add(driver);

        Vin vin = VINHelper.getRandomVIN();
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle veh = new Vehicle();
        veh.setVehicleTypePL(VehicleTypePL.Motorcycle);
        veh.setVin(vin.getVin());
        veh.setDriverPL(driver);
        vehicleList.add(veh);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locToAdd.setPlNumResidence(6);
        locationsList.add(locToAdd);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK, true, UninsuredLimit.Fifty, true, UnderinsuredLimit.Fifty);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);
        squireFPP.setCoverageType(FPPCoverageTypes.BlanketInclude);
        squireFPP.setDeductible(FPPDeductible.Ded_1000);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;
        myPropertyAndLiability.squireFPP = squireFPP;


        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;

        myPolicyObj = new GeneratePolicy.Builder(webDriver)
                .withSquire(mySquire)
                .withInsFirstLastName("Guy", "IMEdit")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
                .build(GeneratePolicyType.FullApp);
    }

    @Test(dependsOnMethods = {"testGenerateSquirePolicy"})
    public void testValidateSecionIAndIIIBusinessRules() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        webDriver = driver = buildDriver(cf);
        new Login(webDriver).loginAndSearchSubmission(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);
        new GuidewireHelpers(webDriver).editPolicyTransaction();
        SideMenuPC sideMenu = new SideMenuPC(webDriver);
        sideMenu.clickSideMenuSquirePropertyDetail();

        GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages_PropertyDetailCoverages(webDriver);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(webDriver);
        propertyDetail.clickEdit();
        propertyDetail.AddExistingOwner();
        propertyDetail.clickOk();
        addNewProperty(PropertyTypePL.DwellingPremisesCovE);
        addNewProperty(PropertyTypePL.VacationHomeCovE);
        addNewProperty(PropertyTypePL.ResidencePremisesCovE);
        addNewProperty(PropertyTypePL.BunkHouse);
        addNewProperty(PropertyTypePL.FarmOffice);

        sideMenu.clickSideMenuSquirePropertyCoverages();
        int properties = (coverages.getPropertyTableRowCount());
        for (int i = 2; i <= properties; i++) {
            coverages.clickSpecificBuilding(1, i);
            coverages.setCoverageECoverageType(CoverageType.Peril_1);
            coverages.setCoverageELimit(100000);
            coverages.setCoverageCLimit(14000);
            coverages.selectOtherCoverageCValuation(ValuationMethod.ActualCashValue);
            coverages.clickNext();
            // System will generate a screen level validation (validation is per
            // property) if the Coverage C limit < $15,000 EXCEPT on Squire
            // policy and Coverage A exists on property
            if (!new ErrorHandlingHelpers(webDriver).getErrorMessage().contains("Limit : Coverage C limit must be greater or equal to $15,000. (PR016)"))
                Assert.fail("When Coverage C Limit less than 15000, Limit : Contents must have limit greater or equal to $15,000. validation message should be displayed.");

            coverages.setCoverageCLimit(20000);

        }
        sideMenu.clickSideMenuSquirePropertyCoverages();
        coverages.clickFarmPersonalProperty();
        coverages.clickFarmPersonalProperty();
        GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages fppCovs = new GenericWorkorderSquirePropertyAndLiabilityCoverages_AdditionalSectionICoverages(webDriver);
        fppCovs.clickFPPAdditionalInterests();

        AddressInfo bankAddress = new AddressInfo();
		AdditionalInterest loc2Bldg1AddInterest = new AdditionalInterest("Test"+StringsUtils.generateRandomNumberDigits(5), "Per"+ StringsUtils.generateRandomNumberDigits(8), bankAddress);
        loc2Bldg1AddInterest.setNewContact(CreateNew.Create_New_Always);
        loc2Bldg1AddInterest.setAddress(bankAddress);
        loc2Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LessorPL);

        GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(webDriver);
        additionalInterests.clickBuildingsPropertyAdditionalInterestsSearch();
        SearchAddressBookPC search = new SearchAddressBookPC(webDriver);
        search.searchForContact(myPolicyObj.basicSearch, loc2Bldg1AddInterest);
        additionalInterests.selectBuildingsPropertyAdditionalInterestsInterestType(loc2Bldg1AddInterest.getAdditionalInterestType().getValue());
        additionalInterests.setAddressListing("New...");
        additionalInterests.setContactEditAddressLine1(loc2Bldg1AddInterest.getAddress().getLine1());
        additionalInterests.setContactEditAddressCity(loc2Bldg1AddInterest.getAddress().getCity());
        additionalInterests.setContactEditAddressState(loc2Bldg1AddInterest.getAddress().getState());
        additionalInterests.setContactEditAddressZipCode(loc2Bldg1AddInterest.getAddress().getZip());
        additionalInterests.sendArbitraryKeys(Keys.TAB);
        additionalInterests.setContactEditAddressAddressType(loc2Bldg1AddInterest.getAddress().getType());
        additionalInterests.setAdditionalInterestsLoanNumber(loc2Bldg1AddInterest.getLoanContractNumber());
        additionalInterests.clickRelatedContactsTab();
        additionalInterests.clickBuildingsPropertyAdditionalInterestsUpdateButton();
        GenericWorkorderSquireFPPAdditionalInterest fppAddInterest = new GenericWorkorderSquireFPPAdditionalInterest(webDriver);
        fppAddInterest.clickAdd(FarmPersonalPropertyTypes.Machinery);
        fppAddInterest.selectItemType(FPPFarmPersonalPropertySubTypes.Tractors);
        fppAddInterest.setDescriptionValueSerialNum(FPPFarmPersonalPropertySubTypes.Tractors, 1, "Testing Purpose", 100, "13324435435");
        coverages.clickNext();

        //System will generate a quote level validation if the additional interest = Lessor is not attached to at least one scheduled item
        if (!new ErrorHandlingHelpers(webDriver).getErrorMessage().contains("On the Farm Personal Property coverages, make sure all details have at least 1 Additional Interest specified."))
            Assert.fail("On the Farm Personal Property coverages, make sure all details have at least 1 Additional Interest specified. Validation message should be displayed.");
        coverages.sendArbitraryKeys(Keys.TAB);
        coverages.clickGenericWorkorderSaveDraft();
        fppAddInterest.addAdditionalInterest(FarmPersonalPropertyTypes.Machinery, loc2Bldg1AddInterest);

        //PGL013 will be removed for Underwriting rule for Squire Auto
        sideMenu.clickSideMenuPACoverages();
        GenericWorkorderSquireAutoCoverages_Coverages coveragePage = new GenericWorkorderSquireAutoCoverages_Coverages(webDriver);
        coveragePage.setMedicalCoverage(MedicalLimit.TwentyFiveK);
        coveragePage.clickNext();
        sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(webDriver);
        riskAnalysis.Quote();

        sideMenu.clickSideMenuRiskAnalysis();
        FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();

        if (!uwIssues.isInList("Medical Limit at $25,000").equals(UnderwriterIssueType.NONE)) {
            Assert.fail("Unexpected error Bind Issue : Medical Limit at $25,000 is displayed when it shouldnt be");
        }

    }

    private void addNewProperty(PropertyTypePL propertyType) throws GuidewireNavigationException {

        PLPolicyLocationProperty property = new PLPolicyLocationProperty(propertyType);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details propertyDetail = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_Details(webDriver);
        propertyDetail.fillOutPropertyDetails_FA(property);
        GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction constructionPage = new GenericWorkorderSquirePropertyAndLiabilityPropertyDetail_PropertyConstruction(webDriver);
        constructionPage.fillOutPropertyConstrustion_FA(property);
        if (!property.getpropertyType().equals(PropertyTypePL.FarmOffice) && !property.getpropertyType().equals(PropertyTypePL.BunkHouse)) {
            constructionPage.setLargeShed(property.getLargeShed());
            propertyDetail.clickPropertyInformationDetailsTab();
            propertyDetail.AddExistingOwner();
        }
        propertyDetail.clickOk();
    }


}
