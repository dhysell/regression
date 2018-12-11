package regression.r2.noclock.policycenter.submission_fire_im.subgroup1;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.InlandMarineTypes.RecreationalEquipmentType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Vehicle.PAComprehensive_CollisionDeductible;
import repository.gw.enums.Vehicle.VehicleTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.RecreationalEquipment;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_RecreationalEquipment;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : US9379: Wagons / Carriages - requirements change
 * @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/InlandMarine/PC8%20-%20IM%20-%20QuoteApplication%20-%20Recreational%20section.xlsx">Link Text</a>
 * @Description - Tet validated and verified for adding recreation equipment and wagons/carriages - RY 5/5/2017
 * @DATE Oct 11, 2016
 */
public class TestSquireIMRecEquipWagonsCarriages extends BaseTest {

    private GeneratePolicy myPolicyObjPL;

    private WebDriver driver;

    @Test()
    public void testBoundSqIMRecEquipmentWagonsCarriages() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

        locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
        PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
        locToAdd.setPlNumAcres(11);
        locationsList.add(locToAdd);


        // Rec Equip
        ArrayList<RecreationalEquipment> recVehicle = new ArrayList<RecreationalEquipment>();
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.WagonsCarriages, "500", PAComprehensive_CollisionDeductible.Fifty50, "Brett Hiltbrand"));
        recVehicle.add(new RecreationalEquipment(RecreationalEquipmentType.WagonsCarriages, "500", PAComprehensive_CollisionDeductible.Fifty50, "Cor Hofman"));

        SquireLiability liability = new SquireLiability();

        ArrayList<InlandMarine> imTypes = new ArrayList<InlandMarine>();
        imTypes.add(InlandMarine.RecreationalEquipment);

        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        driversList.add(person);
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle(VehicleTypePL.PrivatePassenger, "make new vin", 2003, "Oldie", "But Goodie");
        vehicleList.add(vehicle);

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto(driversList, vehicleList, coverages);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liability;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = imTypes;
        myInlandMarine.recEquipment_PL_IM = recVehicle;

        Squire mySquire = new Squire(SquireEligibility.Country);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.InlandMarineLinePL, LineSelection.PersonalAutoLinePL)
                .withInsFirstLastName("IM", "Forms")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
    }


    @Test(dependsOnMethods = {"testBoundSqIMRecEquipmentWagonsCarriages"})
    private void testValidateWagonsCarriagesPhoto() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);

        new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), this.myPolicyObjPL.accountNumber);
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
        policyInfo.clickEditPolicyTransaction();

        sideMenu.clickSideMenuIMRecreationVehicle();
        GenericWorkorderSquireInlandMarine_RecreationalEquipment recreationalEquipment = new GenericWorkorderSquireInlandMarine_RecreationalEquipment(driver);
        recreationalEquipment.clickLinkInRecreationalEquipmentTable(1);
        recreationalEquipment.addAdditionalInsured("Testing Purpose");
        recreationalEquipment.addExistingAdditionalInterest(this.myPolicyObjPL.squire.squirePA.getDriversList().get(0).getFirstName(), AdditionalInterestType.LienholderPL);
        int yearField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy");
        int monthField = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "MM");
        recreationalEquipment.setPhotoYear((monthField - 1) + "/" + (yearField - 7));
        recreationalEquipment.clickOK();
        String[] newReqFurtherUWMessages = {"Missing Wagon/Carriage Photo year"};
        validateRiskAnalysisApprovals(newReqFurtherUWMessages);
    }

    private void validateRiskAnalysisApprovals(String[] expectedUWMessages) {
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();
        boolean testFailed = false;
        String errorMessage = "";
        GenericWorkorderRiskAnalysis_UWIssues riskAnalysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
        riskAnalysis.Quote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);

        if (quote.isPreQuoteDisplayed()) {
            quote.clickPreQuoteDetails();
        } else {
            sideMenu.clickSideMenuRiskAnalysis();
        }

        for (String uwIssue : expectedUWMessages) {
            boolean messageFound = false;
            for (int i = 0; i < riskAnalysis.getUWIssuesList().size(); i++) {
                String currentUWIssueText = riskAnalysis.getUWIssuesList().get(i).getText();
                if (!currentUWIssueText.contains("Blocking Bind") && (!currentUWIssueText.contains("Blocking Quote"))) {

                    if (currentUWIssueText.contains(uwIssue)) {
                        messageFound = true;
                        break;
                    }
                }
            }
            if (!messageFound) {
                testFailed = true;
                errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
            }
        }

        if (testFailed) {
            Assert.fail(driver.getCurrentUrl() + errorMessage);
        }

    }


}
