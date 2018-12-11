package scratchpad.steve.utilities;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.ContactRole;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InlandMarineTypes.CargoClass;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Cargo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireIMCargo;
import repository.gw.generate.custom.SquireInlandMarine;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Vin;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.VINHelper;

public class LienholderPolicies extends BaseTest {
    private GeneratePolicy myPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private WebDriver driver;

    @Test
    public void generatePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
        rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienObj = new GenerateContact.Builder(driver)
                .withCompanyName("Lienholder")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);


        new GuidewireHelpers(driver).logout();

        driver.quit();
        cf.setCenter(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        Vin vin = VINHelper.getRandomVIN();

        Date today = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        Date previousFive = DateUtils.dateAddSubtract(today, DateAddSubtractOptions.Day, -5);

        // Cargo
        Cargo cargoTrailer1 = new Cargo(VehicleType.Trailer, vin.getVin(), Integer.parseInt(vin.getYear()), vin.getMake(), vin.getModel());
        cargoTrailer1.setLimit("1000");
        ArrayList<Cargo> cargoTrailers = new ArrayList<Cargo>();
        cargoTrailers.add(cargoTrailer1);
        ArrayList<String> cargoAddInsured = new ArrayList<String>();
        cargoAddInsured.add("Cor Hofman");
        //SquireIMCargo(CargoClass _cargoClass, String _cargoDescription, ArrayList<Cargo> _scheduledItems)
        SquireIMCargo stuff = new SquireIMCargo(CargoClass.ClassII, "my garbage", cargoTrailers);
        SquireIMCargo cargo = new SquireIMCargo(CargoClass.ClassI, "my garbage", cargoTrailers);
        ArrayList<SquireIMCargo> cargoList = new ArrayList<SquireIMCargo>();
        cargoList.add(cargo);
        cargoList.add(stuff);

        ArrayList<InlandMarine> inlandMarine = new ArrayList<InlandMarine>();
        inlandMarine.add(InlandMarine.Cargo);

		AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(myContactLienObj.companyName, myContactLienObj.addresses.get(0));
		propertyAdditionalInterest.setLienholderNumber(myContactLienObj.lienNumber);
        propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        propertyAdditionalInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
//		locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();

        ArrayList<AdditionalInterest> vehicleInterests = new ArrayList<AdditionalInterest>();
        vehicleInterests.add(new AdditionalInterest());

        Vehicle vehicle = new Vehicle();
        vehicle.setAdditionalInterest(vehicleInterests);

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        vehicles.add(vehicle);

        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setVehicleList(vehicles);

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        SquireInlandMarine myInlandMarine = new SquireInlandMarine();
        myInlandMarine.inlandMarineCoverageSelection_PL_IM = inlandMarine;
        myInlandMarine.cargo_PL_IM = cargoList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.squirePA = squirePersonalAuto;
        mySquire.propertyAndLiability = myPropertyAndLiability;
        mySquire.inlandMarine = myInlandMarine;

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("ins", "Duesdefect")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL, LineSelection.InlandMarineLinePL)
                .withPolEffectiveDate(previousFive)
//				.withPolTermLengthDays(51)
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println("Account Number: " + myPolicyObj.accountNumber);

        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        guidewireHelpers.logout();


        new Login(driver).loginAndSearchJob(myPolicyObj.agentInfo.agentUserName, myPolicyObj.agentInfo.agentPassword, myPolicyObj.accountNumber);
        new GuidewireHelpers(driver).editPolicyTransaction();

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuRiskAnalysis();

        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
        FullUnderWriterIssues allUWIssues = riskAnalysis.getUnderwriterIssues();

        boolean uwIssueFound = false;
        for (UnderwriterIssue uwIssues : allUWIssues.getInformationalList()) {
            if (uwIssues.getLongDescription().contains("Policy: Cargo on City Squire Policy. (IM021)")) {
                uwIssueFound = true;
                break;
            } else {
                uwIssueFound = false;
            }
        }

        Assert.assertFalse("City Cargo should contain the message: Policy: Cargo on City Squire Policy. (IM021)", uwIssueFound);
    }

    //	@Test(dependsOnMethods = { "generatePolicy" })
    public void makePolicyCashDownPayment() throws Exception {
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber());

        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        new GuidewireHelpers(driver).logout();

        accountMenu.systemOut("Account is " + myPolicyObj.accountNumber);
    }
}
