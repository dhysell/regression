package regression.r3.noclock.billingcenter.disbursements;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsMultiplePayment;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementStatus;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Vehicle.BusinessUseClass;
import repository.gw.enums.Vehicle.Radius;
import repository.gw.enums.Vehicle.SecondaryClass;
import repository.gw.enums.Vehicle.SecondaryClassType;
import repository.gw.enums.Vehicle.SizeClass;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test creates an account with 2 policies on it. It then over pays both policies and makes an unapplied payment to the default unapplied bucket. The resulting disbursements are then verified.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/11%20-%20Installment%20Scheduling/11-09%20Reinstatement%20Installment%20Scheduling.docx">Disbursements Requirements</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/41439116420">Rally Story</a>
 * @DATE Mar 1, 2016
 */
@QuarantineClass
public class TestInsuredDisbursements extends BaseTest {
    private GeneratePolicy myPolicyObj = null;
    private GeneratePolicy multiPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private Agents agent;
    private Double defaultUnappliedPayment = 25.00;
    private Double CPPPayment = 125.00;
    private Double BOPPayment = 150.00;

    private WebDriver driver;

    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);
        final Date youthDOB = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Year, -16);

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
                this.setVehicleType(VehicleType.Trucks);
                this.setMake("Light Truck");
                this.setModel("Truck");
                this.setSizeClass(SizeClass.LightTrucksGVWOf10000PoundsOrLess);
                this.setRadius(Radius.Local0To100Miles);
                this.setBusinessUseClass(BusinessUseClass.ServiceUse);
                this.setSecondaryClassType(SecondaryClassType.Truckers);
                this.setSecondaryClass(SecondaryClass.CommonCarriers);
            }});
        }};

        final ArrayList<Contact> personList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
                this.setDob(driver, youthDOB);
                this.setAge(driver, 16);
                this.setPrimaryVehicleDriven(vehicleList.get(0).getVin());
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{

            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{

            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(personList);
        }};

        // GENERATE POLICY
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withCreateNew(CreateNew.Create_New_Always)
                .withPolOrgType(OrganizationType.Individual)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Payment", "Schedule")
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(myPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(myPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(myPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(myPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(myPolicyObj.pniContact.getAddress().getZip());

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.multiPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsPersonOrCompany(myPolicyObj.pniContact.getPersonOrCompany())
                .withInsFirstLastName(myPolicyObj.pniContact.getFirstName(), myPolicyObj.pniContact.getLastName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(OrganizationType.Individual)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolicyLocations(locationsList)
                .withMembershipDuesOnAllInsureds()
                .withPaymentPlanType(myPolicyObj.paymentPlanType)
                .withDownPaymentType(myPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makeInsuredDownPaymentOverMinimum() throws Exception {
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(this.arUser.getUserName(), this.arUser.getPassword());
        BatchHelpers batchHelpers = new BatchHelpers(driver);
        batchHelpers.runBatchProcess(BatchProcess.Invoice_Due);
        batchHelpers.runBatchProcess(BatchProcess.Invoice);

        BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
        desktopMenu.clickDesktopMenuActionsMultiplePayment();


        DesktopActionsMultiplePayment multiPayment = new DesktopActionsMultiplePayment(driver);
        if (myPolicyObj.downPaymentType.equals(PaymentType.Cash)) {
            multiPayment.fillOutNextLineOnMultiPaymentTable(myPolicyObj.commercialPackage.getPolicyNumber(), PaymentInstrumentEnum.Cash, (myPolicyObj.commercialPackage.getPremium().getDownPaymentAmount() + CPPPayment));
        } else {
            multiPayment.fillOutNextLineOnMultiPaymentTable(myPolicyObj.commercialPackage.getPolicyNumber(), null, (myPolicyObj.commercialPackage.getPremium().getDownPaymentAmount() + CPPPayment));
        }
        if (multiPolicyObj.downPaymentType.equals(PaymentType.Cash)) {
            multiPayment.fillOutNextLineOnMultiPaymentTable(multiPolicyObj.busOwnLine.getPolicyNumber(), PaymentInstrumentEnum.Cash, (multiPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() + BOPPayment));
        } else {
            multiPayment.fillOutNextLineOnMultiPaymentTable(multiPolicyObj.busOwnLine.getPolicyNumber(), null, (multiPolicyObj.busOwnLine.getPremium().getDownPaymentAmount() + BOPPayment));
        }
        multiPayment.clickNext();
        multiPayment.clickFinish();

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
        accountSearch.searchAccountByAccountNumber(myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();


        NewDirectBillPayment lienholderPayment = new NewDirectBillPayment(driver);
        lienholderPayment.makeDirectBillPaymentExecuteWithoutDistribution(25.00);

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"makeInsuredDownPaymentOverMinimum"})
    public void verifyDisbursements() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        new BatchHelpers(driver).runBatchProcess(BatchProcess.Automatic_Disbursement);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewTransactionDisbursement();

        CreateAccountDisbursementWizard disbursementWizard = new CreateAccountDisbursementWizard(driver);
        String disbursementDefaultPolicyNumber = disbursementWizard.getCreateAccountDisbursementWizardPolicyNumber();

        if (!((disbursementDefaultPolicyNumber.contains(myPolicyObj.commercialPackage.getPolicyNumber())) || (disbursementDefaultPolicyNumber.contains(multiPolicyObj.busOwnLine.getPolicyNumber())))) {
            System.out.println(multiPolicyObj.busOwnLine.getPolicyNumber());
            System.out.println(myPolicyObj.commercialPackage.getPolicyNumber());
            Assert.fail("The Policy number for the default unapplied funds bucket listed on the disbursement creation screen did not match the first policy number. Test failed.");
        }

        disbursementWizard.clickCancel();
        new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuDisbursements();


        AccountDisbursements disbursements = new AccountDisbursements(driver);
        WebElement defaultUnappliedDisbursement = null;
        WebElement cppUnappliedDisbursement = null;
        WebElement bobUnappliedDisbursement = null;
        try {
            defaultUnappliedDisbursement = disbursements.getDisbursementsTableRow(null, null, null, "Default", DisbursementStatus.Awaiting_Approval, null, defaultUnappliedPayment, null, null, null);
        } catch (Exception e) {
            Assert.fail("The disbursement for the Default Unapplied Fund in the amount of " + StringsUtils.currencyRepresentationOfNumber(defaultUnappliedPayment) + " was not found in the disbursements table.");
        }

        defaultUnappliedDisbursement.click();
        disbursements.clickAccountDisbursementsEdit();
        try {
            disbursements.setDisbursementsPolicyNumber(myPolicyObj.commercialPackage.getPolicyNumber());
        } catch (Exception e) {
            Assert.fail("The Policy Number field on the Default Unapplied disbursement should have been editable, but was not.");
        }
        disbursements.clickAccountDisbursementsCancel();

        try {
            cppUnappliedDisbursement = disbursements.getDisbursementsTableRow(null, null, null, myPolicyObj.commercialPackage.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, null, CPPPayment, null, null, null);
        } catch (Exception e) {
            Assert.fail("The disbursement for the CPP Policy Unapplied Fund in the amount of " + StringsUtils.currencyRepresentationOfNumber(CPPPayment) + " was not found in the disbursements table.");
        }

        disbursements = new AccountDisbursements(driver);
        cppUnappliedDisbursement.click();
        disbursements.clickAccountDisbursementsEdit();
        try {
            disbursements.setDisbursementsPolicyNumber(multiPolicyObj.busOwnLine.getPolicyNumber());
            Assert.fail("The Policy Number field on the CPP disbursement should not have been editable, but it was.");
        } catch (Exception e) {
            System.out.println("The Policy Number field on the CPP Disbursement was not editable. This is expected.");
        }
        disbursements.clickAccountDisbursementsCancel();

        try {
            bobUnappliedDisbursement = disbursements.getDisbursementsTableRow(null, null, null, multiPolicyObj.busOwnLine.getPolicyNumber(), DisbursementStatus.Awaiting_Approval, null, BOPPayment, null, null, null);
        } catch (Exception e) {
            Assert.fail("The disbursement for the BOP Policy Unapplied Fund in the amount of " + StringsUtils.currencyRepresentationOfNumber(BOPPayment) + " was not found in the disbursements table.");
        }

        disbursements = new AccountDisbursements(driver);
        bobUnappliedDisbursement.click();
        disbursements.clickAccountDisbursementsEdit();
        try {
            disbursements.setDisbursementsPolicyNumber(myPolicyObj.commercialPackage.getPolicyNumber());
            Assert.fail("The Policy Number field on the BOP disbursement should not have been editable, but it was.");
        } catch (Exception e) {
            System.out.println("The Policy Number field on the BOP Disbursement was not editable. This is expected.");
        }
        disbursements.clickAccountDisbursementsCancel();

        new GuidewireHelpers(driver).logout();
    }
}