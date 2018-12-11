package regression.r3.clock.billingcenter.cancel;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.BCCommonSummary;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Vehicle.BodyType;
import repository.gw.enums.Vehicle.VehicleType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.CPPCommercialAuto;
import repository.gw.generate.custom.CPPCommercialAutoLine;
import repository.gw.generate.custom.CPPCommercialAutoStateInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchPoliciesPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.change.StartPolicyChange;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author jqu
 * @Description: US6852 Partial Cancel UW Delinquency Process test
 * @Steps: Steps:
 * 1. Generate two policies on same account. One policy has the LH
 * 2. Pay the down payment and add coverage to both policies.
 * 3. Override one policy
 * 4. cancel the policy without LH
 * 5. Remove the insured from the other policy (which has the LH)
 * 6. verify delinquency, loop letters,...
 * @DATE June 15, 2016, written on developer's patch code
 * @Modified September 28, 2016
 */
public class PartialCancelUWDelinquencyProcessTest extends BaseTest {
    private Agents agent;
    private float buildingLimit = 50000;
    private GeneratePolicy cppPolicyObj = null;
    private GeneratePolicy bopPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private Date currentDate;
    private BCAccountMenu acctMenu;
    private String delinquencyAlert;
    private String bannerMessage = "Underwriting Partial Cancel";
    private AccountCharges charge;


    private WebDriver driver;

    private void waitForTransactionToCome(Date date, TransactionType transactionType, String policyNumber) {
        charge = new AccountCharges(driver);
        try {
            charge.waitUntilChargesFromPolicyContextArriveByTransactionDateAndPolicyNumber(120, date, transactionType, policyNumber);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " doesn't find the " + transactionType.getValue() + " for " + policyNumber);
        }
    }

    @SuppressWarnings("serial")
    @Test
    public void generatePolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        locOneBuildingList.add(new PolicyLocationBuilding());
        final ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

        final ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>() {{
            this.add(new Vehicle() {{
                this.setGaragedAt(locationsList.get(0).getAddress());
            }});
        }};

        final ArrayList<Contact> driverList = new ArrayList<Contact>() {{
            this.add(new Contact() {{
                this.setGender(Gender.Male);
            }});
        }};

        CPPCommercialAuto commercialAuto = new CPPCommercialAuto() {{
            this.setCommercialAutoLine(new CPPCommercialAutoLine() {{
            }});
            this.setVehicleList(vehicleList);
            this.setCPP_CAStateInfo(new CPPCommercialAutoStateInfo());
            this.setDriversList(driverList);
        }};

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        // GENERATE POLICY
        this.cppPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withProductType(ProductLineType.CPP)
                .withCPPCommercialAuto(commercialAuto)
                .withLineSelection(LineSelection.CommercialAutoLineCPP)
                .withPolicyLocations(locationsList)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("PartialCancelUWDelinquencyProcess")
                .withPolOrgType(OrganizationType.LLC)
                .withInsPrimaryAddress(locationsList.get(0).getAddress())
                .withPaymentPlanType(PaymentPlanType.Monthly)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void addPolicyToAccount() throws Exception {
        ArrayList<AdditionalInterest> lienholderBuildingAdditionalInterests = new ArrayList<AdditionalInterest>();
        PolicyLocationBuilding lienholderBuilding = new PolicyLocationBuilding();
        AdditionalInterest lienholderBuildingAddInterest = new AdditionalInterest(ContactSubType.Company);
        lienholderBuildingAddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
        lienholderBuildingAddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        lienholderBuildingAdditionalInterests.add(lienholderBuildingAddInterest);
        lienholderBuilding.setAdditionalInterestList(lienholderBuildingAdditionalInterests);

        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
        PolicyLocationBuilding insuredBldg = new PolicyLocationBuilding();
        insuredBldg.setBuildingLimit(buildingLimit);
        insuredBldg.setBppLimit(buildingLimit);
        locOneBuildingList.add(insuredBldg);
        locOneBuildingList.add(lienholderBuilding);
        locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(cppPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(cppPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setCounty(cppPolicyObj.pniContact.getAddress().getCounty());
        insuredAddress.setState(cppPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(cppPolicyObj.pniContact.getAddress().getZip());
        //uncheck equipment breakdown
        AddressInfo address = new AddressInfo();
        PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.SelfStorageFacilities);
        PolicyBusinessownersLineAdditionalCoverages additionalCoverageStuff = new PolicyBusinessownersLineAdditionalCoverages(false, false);
        boLine.setAdditionalCoverageStuff(additionalCoverageStuff);
        ArrayList<PolicyBusinessownersLineAdditionalInsured> additonalInsuredBOLineList = new ArrayList<PolicyBusinessownersLineAdditionalInsured>();
        additonalInsuredBOLineList.add(new PolicyBusinessownersLineAdditionalInsured(ContactSubType.Company, "dafigudhfiuhdafg", AdditionalInsuredRole.CertificateHolderOnly, address));

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        this.bopPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(agent)
                .withCreateNew(CreateNew.Do_Not_Create_New)
                .withInsPersonOrCompany(cppPolicyObj.pniContact.getPersonOrCompany())
                .withInsCompanyName(cppPolicyObj.pniContact.getCompanyName())
                .withInsPrimaryAddress(insuredAddress)
                .withPolOrgType(OrganizationType.LLC)
                .withBusinessownersLine(boLine)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(cppPolicyObj.paymentPlanType)
                .withDownPaymentType(cppPolicyObj.downPaymentType)
                .build(GeneratePolicyType.PolicyIssued);

        System.out.println(cppPolicyObj.toString());
        System.out.println(bopPolicyObj.toString());
    }

    //payoff down payment for CPP and BOP
    @Test(dependsOnMethods = {"addPolicyToAccount"})
    public void payDownPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();
        NewDirectBillPayment directPay = new NewDirectBillPayment(driver);
        directPay.makeInsuredDownpayment(cppPolicyObj, cppPolicyObj.commercialPackage.getPremium().getDownPaymentAmount(), cppPolicyObj.commercialPackage.getPolicyNumber());
        directPay.makeInsuredDownpayment(bopPolicyObj, bopPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), bopPolicyObj.busOwnLine.getPolicyNumber());
        currentDate = DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1);
        ClockUtils.setCurrentDates(driver, currentDate);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice_Due);
    }

    //add coverage to CPP and BOP
    @Test(dependsOnMethods = {"payDownPayment"})
    public void changePolicyToAddCoverage() throws Exception {
        //add a vehicle to CPP to increase its coverage
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        Vehicle newVehicle = new Vehicle();
        AddressInfo insuredAddress = new AddressInfo();
        insuredAddress.setLine1(cppPolicyObj.pniContact.getAddress().getLine1());
        insuredAddress.setCity(cppPolicyObj.pniContact.getAddress().getCity());
        insuredAddress.setState(cppPolicyObj.pniContact.getAddress().getState());
        insuredAddress.setZip(cppPolicyObj.pniContact.getAddress().getZip());
        newVehicle.setGaragedAt(insuredAddress);
        newVehicle.setVehicleType(VehicleType.Miscellaneous);
        newVehicle.setMake("Antique Auto1");
        newVehicle.setModel("Truck");
        newVehicle.setBodyType(BodyType.AntiqueAutos);
        newVehicle.setLeasedToOthers(true);
        policyChangePage.addVehicleToCPP(true, newVehicle, "add a vehicle", currentDate);

        //add coverage to BOP
        SearchPoliciesPC search = new SearchPoliciesPC(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        policyChangePage.changeBuildingCoverage(1, 200000, 200000);
    }

    @Test(dependsOnMethods = {"changePolicyToAddCoverage"})
    public void overrideBOP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        //wait for the policy changes to come
        waitForTransactionToCome(currentDate, TransactionType.Policy_Change, cppPolicyObj.commercialPackage.getPolicyNumber());
        waitForTransactionToCome(currentDate, TransactionType.Policy_Change, bopPolicyObj.busOwnLine.getPolicyNumber());
        //override BOP with CPP
        BCSearchPolicies search = new BCSearchPolicies(driver);
        search.searchPolicyByPolicyNumber(bopPolicyObj.busOwnLine.getPolicyNumber());
        BCPolicySummary summary = new BCPolicySummary(driver);
        summary.overrideInvoiceStream(cppPolicyObj.commercialPackage.getPolicyNumber());
        //move clock to couple of days before next Due date
        currentDate = DateUtils.dateAddSubtract(DateUtils.dateAddSubtract(cppPolicyObj.commercialPackage.getEffectiveDate(), DateAddSubtractOptions.Month, 1), DateAddSubtractOptions.Day, -2);
        ClockUtils.setCurrentDates(driver, currentDate);
        new BatchHelpers(driver).runBatchProcess(BatchProcess.Invoice);
    }

    @Test(dependsOnMethods = {"overrideBOP"})
    public void cancelCPP() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(cppPolicyObj.underwriterInfo.getUnderwriterUserName(), cppPolicyObj.underwriterInfo.getUnderwriterPassword(), cppPolicyObj.commercialPackage.getPolicyNumber());
        StartCancellation cancellation = new StartCancellation(driver);
        cancellation.cancelPolicy(CancellationSourceReasonExplanation.OtherInsuredRequest, "cancel cpp", currentDate, true);
    }

    //shouldn't trigger UW partial delinquency if not all policies in the invoice stream go delinquent
    @Test(dependsOnMethods = {"cancelCPP"})
    public void verifyUWDelinquencyAfterCancelOnePolicy() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        //wait for CPP cancellation to come
        acctMenu.clickBCMenuCharges();
        waitForTransactionToCome(currentDate, TransactionType.Cancellation, cppPolicyObj.commercialPackage.getPolicyNumber());
        //verify that the UW delinquency is NOT triggered (since BOP is not delinquent in the same invoice stream)
        acctMenu.clickBCMenuSummary();
        BCCommonSummary acctSummary = new BCCommonSummary(driver);
        try {
            delinquencyAlert = acctSummary.getDelinquencyAlertMessage();
            if (delinquencyAlert.contains(bannerMessage))
                Assert.fail(driver.getCurrentUrl() + "the UW partial cancel delinquency should not be triggered if not all policies in the same invoice stream are delinquent.");
        } catch (Exception e) {
            System.out.println("Doesn't find the Underwrithing Full cancel which is expected");
        }
    }

    @Test(dependsOnMethods = {"verifyUWDelinquencyAfterCancelOnePolicy"})
    public void removeBOPInsuredBuilding() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(), bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
        policyChangePage.removeBuildingByBldgNumber(1);
    }

    @Test(dependsOnMethods = {"verifyUWDelinquencyAfterCancelOnePolicy"})
    public void verifyTheUWPartialCancel() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), cppPolicyObj.accountNumber);
        acctMenu = new BCAccountMenu(driver);
        //wait for BOP policy change to come
        acctMenu.clickBCMenuCharges();
        waitForTransactionToCome(currentDate, TransactionType.Policy_Change, bopPolicyObj.busOwnLine.getPolicyNumber());
        //verify that the UW partial cancel delinquency is triggered
        acctMenu.clickBCMenuSummary();
        BCCommonSummary acctSummary = new BCCommonSummary(driver);
        try {
            delinquencyAlert = acctSummary.getDelinquencyAlertMessage();
            if (!delinquencyAlert.contains(bannerMessage))
                Assert.fail(driver.getCurrentUrl() + " the UW partial cancel delinquency should be triggered.");
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " Doesn't find the UW partial cancel delinquency which is expected.");
        }
    }

    @Test(dependsOnMethods = {"verifyTheUWPartialCancel"})
    public void verifyLoopLetters() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());
        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), DateAddSubtractOptions.Day, 1));
        new BatchHelpers(driver).runBatchProcess(BatchProcess.BC_Workflow);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuDocuments();
        BCCommonDocuments doc = new BCCommonDocuments(driver);
        try {
            doc.getDocumentsTableRow(DocumentType.Balance_Due_Partial_Cancel.getValue(), null, null, null, DateUtils.getCenterDate(driver, ApplicationOrCenter.BillingCenter), null);
        } catch (Exception e) {
            Assert.fail(driver.getCurrentUrl() + " Doesn't find the UW partial cancel delinquency loop letter.");
        }
    }
}
