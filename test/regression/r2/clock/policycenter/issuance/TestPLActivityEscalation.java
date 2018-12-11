package regression.r2.clock.policycenter.issuance;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.BasePage;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireFPP;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;

import com.idfbins.driver.BaseTest;

import repository.gw.topinfo.TopInfo;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.desktop.DesktopMyActivitiesPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyAndLiabilityCoverages;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.UnderwritersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

/**
 * @Author nvadlamudi
 * @Requirement :DE4242: FA activity escalation stay open
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Nov 28, 2016
 */
@QuarantineClass
public class TestPLActivityEscalation extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy mySquirePolicyObjPL;
    private Underwriters uw;

    @Test()
    public void testBindSquirePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
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

        SquireFPP squireFPP = new SquireFPP(FPPFarmPersonalPropertySubTypes.Tractors);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);

        // Inland Marine
        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.Cargo);

        // Cargo
        Vin vin = VINHelper.getRandomVIN();
        Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("3123");
        ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");

        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, 100, 1000, "Brenda Swindle", false, cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.cargo_PL_IM = cargoList;
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;

        SquirePropertyAndLiability property = new SquirePropertyAndLiability();
        property.locationList = locationsList;
        property.liabilitySection = myLiab;
        property.squireFPP = squireFPP;

        Squire mySquire = new Squire(SquireEligibility.FarmAndRanch);
        mySquire.propertyAndLiability = property;
        mySquire.squirePA = squirePersonalAuto;

        mySquirePolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL, LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL)
                .withInsFirstLastName("Test", "ActivityEsc")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicySubmitted);
    }

    @Test(dependsOnMethods = {"testBindSquirePolicy"})
    private void testActivityAssignClockMove() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login(mySquirePolicyObjPL.underwriterInfo.getUnderwriterUserName(), mySquirePolicyObjPL.underwriterInfo.getUnderwriterPassword());
        this.uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        DesktopMyActivitiesPC myActivities = new DesktopMyActivitiesPC(driver);
        myActivities.AssignActivityBySearchingWithInsuredName(mySquirePolicyObjPL.pniContact.getFirstName() + " " + mySquirePolicyObjPL.pniContact.getLastName(), uw.getUnderwriterUserName());

        new GuidewireHelpers(driver).logout();
        driver.quit();
        
        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), mySquirePolicyObjPL.accountNumber);
        AccountSummaryPC summary = new AccountSummaryPC(driver);
        Date currenActivitySubject = summary.getActivityDueDate("Submitted Full Application");

        int days = DateUtils.getDifferenceBetweenDates(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), currenActivitySubject, DateDifferenceOptions.Day);

        days = days + 4;
        new GuidewireHelpers(driver).logout();
        testClockMove(days);

    }

    @Test(dependsOnMethods = {"testActivityAssignClockMove"})
    public void testValidateEscalatedActivity() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login("tstronks", "gw");

        DesktopMyActivitiesPC myActivities = new DesktopMyActivitiesPC(driver);
        if (!myActivities.findDesktopMyActivitiesResultsSubjectSpecific("Submitted Full Application", this.mySquirePolicyObjPL.accountNumber, mySquirePolicyObjPL.pniContact.getFirstName() + " " + mySquirePolicyObjPL.pniContact.getLastName(), this.mySquirePolicyObjPL.productType.getValue())) {
            Assert.fail("Expected Escalation activity is not shown");
        }
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"testValidateEscalatedActivity"})
    public void testIssuePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.uw.getUnderwriterUserName(), this.uw.getUnderwriterPassword(), this.mySquirePolicyObjPL.accountNumber);
        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
        accountSummaryPage.clickActivitySubject("Submitted Full Application");

        ActivityPopup activityPopupPage = new ActivityPopup(driver);
        try {
            activityPopupPage.setUWIssuanceActivity();
        } catch (Exception e) {
            activityPopupPage.clickCloseWorkSheet();
        }

        SideMenuPC sidemenu = new SideMenuPC(driver);
        sidemenu.clickSideMenuSquirePropertyCoverages();
        GenericWorkorderSquirePropertyAndLiabilityCoverages coverages = new GenericWorkorderSquirePropertyAndLiabilityCoverages(driver);
        coverages.clickFarmPersonalProperty();
        coverages.setFarmPersonalPropertyRisk("A");
        sidemenu.clickSideMenuQualification();

        GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
        qualificationPage.clickQuote();
        sidemenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
        risk.approveAll_IncludingSpecial();

        sidemenu.clickSideMenuQuote();
        sidemenu.clickSideMenuForms();
        sidemenu.clickSideMenuPayment();

        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
        quote.issuePolicy(IssuanceType.NoActionRequired);
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
        new BasePage(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
        TopInfo topInfoStuff = new TopInfo(driver);
        topInfoStuff.clickTopInfoLogout();
    }

    @Test(dependsOnMethods = {"testIssuePolicy"})
    public void testCheckEscalatedActivityClosed() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login("tstronks", "gw");

        DesktopMyActivitiesPC myActivities = new DesktopMyActivitiesPC(driver);
        if (myActivities.findDesktopMyActivitiesResultsSubjectSpecific("Submitted Full Application", this.mySquirePolicyObjPL.accountNumber, mySquirePolicyObjPL.pniContact.getFirstName() + " " + mySquirePolicyObjPL.pniContact.getLastName(), this.mySquirePolicyObjPL.productType.getValue())) {
            Assert.fail("Expected Escalation activity is not closed");
        }
        new GuidewireHelpers(driver).logout();
    }

    private void testClockMove(int days) throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Activity_Escalation);
        // delay required
    }
}
