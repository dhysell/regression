package regression.r2.noclock.policycenter.submission_fire_im.subgroup3;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.Building.ConstructionTypePL;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.FoundationType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.DriverVehicleUsePL;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import repository.pc.workorders.generic.GenericWorkorderModifiers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US5540: Personal Lines Modifers
 * @RequirementsLink <a
 * href="http://rally1.rallydev.com/#/33274298124d/detail/userstory/41806882537</a>
 * @Description
 * @DATE May 22, 2016
 */
@QuarantineClass
public class TestSquireStandardFireModifiers extends BaseTest {

    private GeneratePolicy myPolicyObjPL = null;
    private GeneratePolicy standardfirePolicy = null;
    private GeneratePolicy mySQPolicyObjPL = null;
    private Underwriters uw;

    private WebDriver driver;

    /// issue and cancel policy 3 month old policy
    @Test
    private void testIssue3MonthOldCancelledPolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                DateAddSubtractOptions.Day, -120);

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = new SquirePersonalAuto();

        mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withPolEffectiveDate(newEff)
                .withInsFirstLastName("Modif", "Cancel")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter_Supervisor);
        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySQPolicyObjPL.accountNumber);

        Date currentDate = mySQPolicyObjPL.squire.getEffectiveDate();
        Date cancellationDate = DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, 20);
        StartCancellation cancelPol = new StartCancellation(driver);

        cancelPol.cancelPolicy(CancellationSourceReasonExplanation.WentWithAnotherCarrier, "Testing Purpose",
                cancellationDate, true);
        guidewireHelpers.logout();
    }

    @Test(dependsOnMethods = {"testIssue3MonthOldCancelledPolicy"})
    public void testCreateSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
                MedicalLimit.TenK);
        coverages.setUnderinsured(false);
        coverages.setAccidentalDeath(true);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();

        Vehicle secondVeh = new Vehicle();
        secondVeh.setEmergencyRoadside(true);
        vehicleList.add(secondVeh);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_50_100_25);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        // Inland Marine
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.FarmEquipment);

        // Farm Equipment
        IMFarmEquipmentScheduledItem farmThing = new IMFarmEquipmentScheduledItem("Farm Equipment",
                "Manly Farm Equipment", 1000);
        ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
        farmEquip.add(farmThing);
        FarmEquipment imFarmEquip = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
                IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
        ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
        allFarmEquip.add(imFarmEquip);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = myLiab;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.farmEquipment = allFarmEquip;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;


        myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withSquireEligibility(SquireEligibility.FarmAndRanch)
                .withInsFirstLastName("Test", "Modi")
                .build(GeneratePolicyType.QuickQuote);

    }

    /**
     * @throws GuidewireException
     * @throws Exception
     * @Author nvadlamudi
     * @Description :Validating the Squire policy
     * @DATE May 22, 2016
     */
    @Test(dependsOnMethods = {"testCreateSquirePolicy"})
    public void testValidateSquireModifiers() throws GuidewireException, Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login to the newly created account
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
        GenericWorkorderModifiers modify = new GenericWorkorderModifiers(driver);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuModifiers();

        // Edit Package and Prefered drodowns
        modify.setSquirePolicyModifiersEligibilityByCategory("Package", "Yes");
        modify.setSquirePolicyModifiersEligibilityByCategory("Preferred", "Yes");
        modify.clickPolicyChangeNext();

        // Changing to Full App
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();
        sideMenu.clickSideMenuQualification();
        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.setSquireGeneralFullTo(false);
        qualificationPage.setSquireAutoFullTo(false);
        qualificationPage.setSquireGLFullTo(false);
        qualificationPage.setSquireHOFullTo(false, "Testing purpose");
        qualificationPage.clickQualificationIMLosses(false);
        qualificationPage.clickQualificationGLCattle(false);

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.updatePLHouseholdMembersFA(myPolicyObjPL);
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReporting(myPolicyObjPL);
        sideMenu.clickSideMenuPADrivers();
        GenericWorkorderSquireAutoDrivers drivers = new GenericWorkorderSquireAutoDrivers(driver);
        drivers.fillOutDriversFA(myPolicyObjPL);

        // Adding another vehicle to see multi car discount
        sideMenu.clickSideMenuPAVehicles();

        GenericWorkorderVehicles_Details vehiclePage = new GenericWorkorderVehicles_Details(driver);
        vehiclePage.fillOutVehicles_FA(myPolicyObjPL);
        sideMenu.clickSideMenuPAVehicles();
        vehiclePage.createVehicleManually();
        vehiclePage.createGenericVehicle(VehicleTypePL.PrivatePassenger);
        vehiclePage.addDriver(myPolicyObjPL.squire.squirePA.getDriversList().get(0), DriverVehicleUsePL.Principal);
        vehiclePage.selectGaragedAt(myPolicyObjPL.pniContact.getAddress());
        vehiclePage.clickOK();

        // adding Loyalty in Policy Info.
        sideMenu.clickSideMenuPolicyInfo();
        polInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Individual);
        polInfo.setTransferedFromAnotherPolicy(true);
        boolean setInceptionDate = polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy, this.mySQPolicyObjPL.squire.getPolicyNumber());
        if (setInceptionDate) {
            Date newInceptionDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),
                    DateAddSubtractOptions.Year, -5);
            polInfo.setInceptionDateByRow(1, newInceptionDate);
        }
        // Reading the Insurance Score
        sideMenu.clickSideMenuPLInsuranceScore();

        GenericWorkorderInsuranceScore insScore = new GenericWorkorderInsuranceScore(driver);
        double eScore = insScore.getEnhancedInsuranceScore();
        String insFlag = insScore.getFlag();
        // Validating the Modifiers
        sideMenu.clickSideMenuModifiers();
        // validate insurance score
        boolean testFailed = false;
        String errorMessage = "";
        String insEligibility = modify.getSquirePolicyModififierEligibilityByRow(4);
        String insDiscount = modify.getSquirePolicyModifierDiscountSurchargeByRow(4);
        if (insFlag.contains("N")) {
            if (!insEligibility.trim().contains("0") && !insDiscount.trim().contains("0")) {
                testFailed = true;
                errorMessage = "Insurance flag = N and score is not zero and displayed as " + insEligibility + " "
                        + insDiscount;
            }
        } else if (insFlag.contains("I")) {
            if (!insDiscount.trim().contains("-5")) {
                testFailed = true;
                errorMessage = "Insurance flag = I and score is not zero and displayed as " + insDiscount;
            }
        } else {
            if (Double.parseDouble(insEligibility.trim()) != eScore) {
                testFailed = true;
                errorMessage = "Insurance flag = F and score is not zero and displayed as " + insEligibility + " "
                        + insDiscount;
            }
        }

        // validate special and additional discount with - and + values
        int totalDiscount = modify.getTotalPolicyDiscount();
        modify.enterSquireModifierAdditionalDiscount(5, "-5", "Testing note added for additional discount");
        if (modify.getTotalPolicyDiscount() != totalDiscount + (5)) {
            testFailed = true;
            errorMessage = "Additional discount is not added and total modifer count is not matched: "
                    + modify.getTotalPolicyDiscount();
        }

        if (modify.getSquireSectionModifierEligibility(1) != 2) {
            testFailed = true;
            errorMessage = "Multi car discount is not correct : " + modify.getSquireSectionModifierEligibility(1);
        }

        if (modify.getSquirePolicyModififierEligibilityByRow(1).contains("5")
                && !modify.getSquirePolicyModifierDiscountSurchargeByRow(1).contains("-3")) {
            testFailed = true;
            errorMessage = "Expected Loyalty and Loyalty Discount not displayed: "
                    + modify.getSquirePolicyModififierEligibilityByRow(1);
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description :Creating the Standard Fire policy
     * @DATE May 22, 2016
     */
    @Test
    public void testCreateStandardFirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty property = new PLPolicyLocationProperty();
        property.setpropertyType(PropertyTypePL.DwellingPremises);
        property.setConstructionType(ConstructionTypePL.Frame);
        property.setFoundationType(FoundationType.FullBasement);
        locOnePropertyList.add(property);
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locationsList.add(locToAdd);

        standardfirePolicy = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardFire)
                .withLineSelection(LineSelection.StandardFirePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Test", "Modif")
                .withInsAge(26)
                .withPolOrgType(OrganizationType.Individual)
                .withPolicyLocations(locationsList)
                .build(GeneratePolicyType.QuickQuote);
    }

    /**
     * @throws Exception
     * @Author nvadlamudi
     * @Description :Validation for Standard Fire
     * @DATE May 22, 2016
     */
    @Test(dependsOnMethods = {"testCreateStandardFirePolicy"})
    public void testValidateStandardFireModifiers() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // Login to the newly created account
        uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
                Underwriter.UnderwriterTitle.Underwriter);
        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                standardfirePolicy.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuModifiers();

        // Changing to Full App
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.clickGenericWorkorderFullApp();

        sideMenu.clickSideMenuHouseholdMembers();
        GenericWorkorderPolicyMembers household = new GenericWorkorderPolicyMembers(driver);
        household.updatePLHouseholdMembersFA(standardfirePolicy);
        GenericWorkorderInsuranceScore creditReport = new GenericWorkorderInsuranceScore(driver);
        creditReport.fillOutCreditReporting(standardfirePolicy);
        // Reading the Insurance Score
        sideMenu.clickSideMenuPLInsuranceScore();

        double eScore = creditReport.getEnhancedInsuranceScore();
        String insFlag = creditReport.getFlag();
        // Validating the Modifiers
        sideMenu.clickSideMenuPropertyLocations();
        sideMenu.clickSideMenuModifiers();

        // validate insurance score
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderModifiers modify = new GenericWorkorderModifiers(driver);

        String insEligibility = modify.getStandardFirePolicyModififierEligibilityByRow(2);
        String insDiscount = modify.getStandardFirePolicyModifierDiscountSurchargeByRow(2);
        if (insFlag.contains("N")) {
            if (!insEligibility.trim().contains("0") && insDiscount.trim().contains("0")) {
                testFailed = true;
                errorMessage = "Insurance flag = N and score is not zero and displayed as " + insEligibility + " "
                        + insDiscount;
            }
        } else if (insFlag.contains("I")) {
            if (insDiscount.trim() != "-5") {
                testFailed = true;
                errorMessage = "Insurance flag = I and score is not zero and displayed as " + insDiscount;
            }
        } else {
            if (Double.parseDouble(insEligibility.trim()) != eScore) {
                testFailed = true;
                errorMessage = "Insurance flag = F and score is not zero and displayed as " + insEligibility + " "
                        + insDiscount;
            }
        }

        if (testFailed)
            Assert.fail(errorMessage);
    }
}
