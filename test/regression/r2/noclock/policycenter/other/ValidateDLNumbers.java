package regression.r2.noclock.policycenter.other;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.State;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import persistence.globaldatarepo.entities.DriverLicenseNumbers;
import persistence.globaldatarepo.helpers.DriverLicenseNumbersHelpers;

@QuarantineClass
public class ValidateDLNumbers extends BaseTest {

    public GeneratePolicy myPolicyObjCPP = null;
    private WebDriver driver;

    // Create and Issue CPP policy
    @Test(enabled = true)
    public void generatePolicy() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // Coverages
        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow, MedicalLimit.TenK);
        coverages.setUnderinsured(false);

        // driver
        ArrayList<Contact> driversList = new ArrayList<Contact>();
        Contact person = new Contact();
        person.setFirstName("Steve");
        driversList.add(person);

        // Vehicle
        ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();
        Vehicle toAdd = new Vehicle();
        toAdd.setComprehensive(true);
        toAdd.setCostNew(10000.00);
        toAdd.setCollision(true);
        toAdd.setAdditionalLivingExpense(true);
        vehicleList.add(toAdd);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);
        squirePersonalAuto.setVehicleList(vehicleList);
        squirePersonalAuto.setDriversList(driversList);


        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;

        // GENERATE POLICY
        myPolicyObjCPP = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withCreateNew(CreateNew.Create_New_Always)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withPaymentPlanType(PaymentPlanType.getRandom())
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.QuickQuote);

        System.out.println(myPolicyObjCPP.accountNumber);
    }


    @Test(dependsOnMethods = {"generatePolicy"})
    public void validateLicenseNumbers() throws Exception {
        boolean failed = false;
        String failureString = "";


        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        List<DriverLicenseNumbers> numbers = new ArrayList<DriverLicenseNumbers>();

        //get 5 random license numbers
        for (int i = 0; i < 5; i++) {
            numbers.add(DriverLicenseNumbersHelpers.getRandomDLNumber());
            System.out.println(numbers.get(i).getDlnumber() + " | " + numbers.get(i).getState());
        }

        new Login(driver).loginAndSearchJob(myPolicyObjCPP.agentInfo.getAgentUserName(), myPolicyObjCPP.agentInfo.getAgentPassword(), myPolicyObjCPP.accountNumber);

        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickPolicyContractSectionIIIAutoLine();

        GenericWorkorderSquireAutoDrivers_ContactDetail driversPage = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
        int numOfDrivers = driversPage.getDriverTableRowsCount();


        for (int i = 0; i < numOfDrivers; i++) {
            //remove the first DL number to use
            DriverLicenseNumbers number = numbers.remove(0);

            System.out.println("Testing : " + number.getDlnumber() + " For State : " + number.getState());

            //edit current driver
            driversPage.clickEditButtonInDriverTable(i);

            driversPage.setLicenseNumber(number.getDlnumber());
            if (State.valueOfName(number.getState()) != null) {
                driversPage.selectLicenseState(State.valueOfName(number.getState()));
            } else {
                System.out.println("FAILED on state  :  " + number.getState());
            }


            driversPage.clickOk();


            if (driversPage.hasInvalidLicenseFormat()) {
                failed = true;
                failureString = failureString + "Drivers License Nubmer: " + number.getDlnumber() + "Failed to validate with: " + number.getState() + "\n";
            }
        }


        if (failed) {
            Assert.fail(driver.getCurrentUrl() + myPolicyObjCPP.accountNumber + "Failed to validate these Drivers License Nubmers:" + failureString);
        }

    }
}


















