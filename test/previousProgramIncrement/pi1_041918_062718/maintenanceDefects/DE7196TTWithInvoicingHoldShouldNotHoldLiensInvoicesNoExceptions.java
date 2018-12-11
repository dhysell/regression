package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.search.BCSearchAccounts;
import repository.bc.wizards.NewTroubleTicketWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.InvoiceType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.Priority;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TroubleTicketType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


/**
 * @Author sgunda
 * @Requirement DE7513 - TT with Invoicing hold is carrying forward lien's invoices
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/defect/220950205048">Link Text<DE7513 - TT with Invoicing hold is carrying forward lien's invoices/a>
 * @DATE May 17, 2018
 */

@Test(groups = {"ClockMove"})
public class DE7196TTWithInvoicingHoldShouldNotHoldLiensInvoicesNoExceptions extends BaseTest {
	private WebDriver driver;
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
    private GeneratePolicy myPolicyObj = null;
    private ARUsers arUser = new ARUsers();
    private String lienholderNumber = null;
    private String lienholderLoanNumber = null;
    private double lienholderLoanPremiumAmount;

    private BCAccountMenu acctMenu;

    @Test
    public void generatePolicy() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
        int yearTest = DateUtils.getCenterDateAsInt(driver, ApplicationOrCenter.PolicyCenter, "yyyy") - NumberUtils.generateRandomNumberInt(0, 50);
        PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
        loc1Bldg1.setYearBuilt(yearTest);
        loc1Bldg1.setClassClassification("storage");

        PolicyLocationBuilding loc1Bldg2 = new PolicyLocationBuilding();
        loc1Bldg2.setYearBuilt(2010);
        loc1Bldg2.setClassClassification("storage");
        
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("Lienholder")
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
        driver.quit();

        AdditionalInterest loc1Bld2AddInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
        loc1Bld2AddInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
        loc1Bld2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
        loc1Bldg2AdditionalInterests.add(loc1Bld2AddInterest);
        loc1Bldg2.setAdditionalInterestList(loc1Bldg2AdditionalInterests);

        this.locOneBuildingList.add(loc1Bldg1);
        this.locOneBuildingList.add(loc1Bldg2);
        this.locationsList.add(new PolicyLocation(new AddressInfo(), this.locOneBuildingList));

        cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
                .withInsPersonOrCompany(ContactSubType.Company)
                .withInsCompanyName("Renewal DE7513")
                .withPolTermLengthDays(120)
                .withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
                .withPolOrgType(OrganizationType.Partnership)
                .withPolicyLocations(locationsList)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

        this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getLoanContractNumber();
        this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(1).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
    }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void makeInsuredDownPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeInsuredDownpayment(myPolicyObj, myPolicyObj.busOwnLine.getPremium().getDownPaymentAmount(), myPolicyObj.busOwnLine.getPolicyNumber());

        new GuidewireHelpers(driver).logout();
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 25);
    }

//    @Test(dependsOnMethods = {"makeInsuredDownPayment"})
//    public void moveClocks() throws Exception {
//        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 25);
//    }

    @Test(dependsOnMethods = {"makeInsuredDownPayment"})
    public void payLienholderAmount() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"payLienholderAmount"})
    public void runInvoiceDue() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.myPolicyObj.accountNumber);
        ClockUtils.setCurrentDates(driver, DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice_Due);
        ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -50));
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"runInvoiceDue"})
    public void renewPolicyInPolicyCenter() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
        new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);

    }

    @Test(dependsOnMethods = {"renewPolicyInPolicyCenter"})
    public void mainTest() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), myPolicyObj.accountNumber);
        Date dateofTT = DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), DateAddSubtractOptions.Day, -48);
        ClockUtils.setCurrentDates(driver, dateofTT);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickBCMenuCharges();
        BCCommonCharges bcCommonCharges = new BCCommonCharges(driver);
        Assert.assertTrue(bcCommonCharges.waitUntilChargesFromPolicyContextArrive(TransactionType.Renewal), "Renewal Charges did not make to BC, Please investigate ");
        
        acctMenu.clickBCMenuTroubleTickets();
        BCCommonTroubleTickets bcCommonTroubleTickets = new BCCommonTroubleTickets(driver);
        bcCommonTroubleTickets.clickNewButton();
        NewTroubleTicketWizard newTroubleTicketWizard = new NewTroubleTicketWizard(driver);
        newTroubleTicketWizard.createNewTroubleTicket(TroubleTicketType.PromisedPayment, Priority.Normal, DateUtils.dateAddSubtract(dateofTT, DateAddSubtractOptions.Day, 60), myPolicyObj.accountNumber);
        
        new BCSearchAccounts(driver).searchAccountByAccountNumber(this.lienholderNumber);
        acctMenu.clickAccountMenuInvoices();
        AccountInvoices accountInvoices = new AccountInvoices(driver);
        Date lienInvoiceBillingDate = accountInvoices.getInvoiceDateByInvoiceType(InvoiceType.RenewalDownPayment); 
        ClockUtils.setCurrentDates(driver, lienInvoiceBillingDate);
        new BatchHelpers(cf).runBatchProcess(BatchProcess.Invoice);
        acctMenu.clickBCMenuCharges();
        acctMenu.clickAccountMenuInvoices();
        InvoiceStatus invoiceStatus = accountInvoices.getInvoiceStatusByInvoiceType(InvoiceType.RenewalDownPayment);
        
        Assert.assertTrue((invoiceStatus== InvoiceStatus.Billed), "Invoice status in still " + invoiceStatus.getValue() + " even after running the Invoice batch. LH invoice should go billed since TT should only hold Insured's invoice not on Lien, please investigate ");
    }
}