package coreBusiness.delinquencies.renewaltermornottakenlienholderfullcancel;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.payments.AccountPayments;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.*;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.workorders.renewal.StartRenewal;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @Author sgunda
 * @Requirement DE3956 - Delinquency not closed even if delinquent amount is zero or less than 10 %
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/62536628605">Link Text<DE3956 - Delinquency not closed even if delinquent amount is zero or less than 10 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-01%20Insured%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-01 Insured Non-Pay Full Cancel Delinquency: See requirement 06-01-09 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-02%20Lienholder%20Non-Pay%20Full%20Cancel%20Delinquency.docx">Link Text<06-02 Lienholder Non-Pay Full Cancel Delinquency: See requirement 06-02-04 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-03%20Insured%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-03 Insured Non-Pay Partial Cancel Delinquency: See requirement 06-03-08 %/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-04%20Lienholder%20Non-Pay%20Partial%20Cancel%20Delinquency.docx">Link Text<06-04 Lienholder Non-Pay Partial Cancel Delinquency: See requirement 06-04-06/a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/06%20-%20Delinquency%20Cancel/06-07%20Not%20Taken%20Cancel%20Delinquency.docx">Link Text<06-07 Not Taken Cancel Delinquency: See requirement 06-07-06/a>
 * @Description This test covers payments made for 90% of original delinquent amount and check if delinquency exits or not
 * @DATE May 12, 2017
 */
public class NotTakenLHFullCancelThresholdTest extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj;
    private ARUsers arUser = new ARUsers();
    private ArrayList<AdditionalInterest> loc1Bldg2AdditionalInterests = new ArrayList<AdditionalInterest>();
    private ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
    private ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

    private String lienholderNumber = null;
    private String lienholderLoanNumber = null;
    private double lienholderLoanPremiumAmount;

    private BCAccountMenu acctMenu;
    private BCCommonDelinquencies acctDelinquency;
    private AccountPayments payment;
    private NewDirectBillPayment directPayment;
    private AccountInvoices acctInvoice;
    private double orgDelinquentAmount, renewalDownPaymentDueAmountLien;
    private Date reversalDate;

    private repository.gw.enums.OpenClosed openOrClosed;
    private String errorMsg;
    private HashMap<String, Object> getValues;
    private double payAmount;


    private void gotoAccount(String accountNumber) {
    	BCSearchAccounts search = new BCSearchAccounts(driver);
        search.searchAccountByAccountNumber(accountNumber);
    }


    @Test
    public void generate() throws Exception {
        ArrayList<GenerateContact> generateContacts= new ArrayList<>();
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

        ArrayList<repository.gw.enums.ContactRole> rolesToAdd = new ArrayList<repository.gw.enums.ContactRole>();
        rolesToAdd.add(repository.gw.enums.ContactRole.Lienholder);

        GenerateContact generateContact = new GenerateContact.Builder(driver)
                .withCompanyName("LH FullLienBilled")
                .withRoles(rolesToAdd)
                .withGeneratedLienNumber(true)
                .withUniqueName(true)
                .build(GenerateContactType.Company);

        generateContacts.add(generateContact);
        driver.quit();

        cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicyHelper(driver).generateFullyLienBilledBOPPolicy("LH Threshold NTkn",120, repository.gw.enums.PaymentPlanType.Annual, repository.gw.enums.PaymentType.Cash , generateContacts);

        Assert.assertNotNull(myPolicyObj,"Generate failed, please investigate from logs");

        this.lienholderNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienholderLoanNumber = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
        this.lienholderLoanPremiumAmount = this.myPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();

    }


    @Test(dependsOnMethods = {"generate"})
    public void payLienholderAmount() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 25);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        NewDirectBillPayment newPayment = new NewDirectBillPayment(driver);
        newPayment.makeDirectBillPaymentExecute(myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber, this.lienholderLoanPremiumAmount);

        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"payLienholderAmount"})
    public void runInvoiceDue() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -50));

    }

    @Test(dependsOnMethods = {"runInvoiceDue"})
    public void renewlPolicyInPolicyCenter() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
    	driver = buildDriver(cf);
        new StartRenewal(driver).loginAsUWAndIssueRenewal(myPolicyObj);

    }

    @Test(dependsOnMethods = {"renewlPolicyInPolicyCenter"})
    public void runBatches() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
    }

    @Test(dependsOnMethods = {"runBatches"})
    public void makeRenewalDownPaymentOnLien() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -2));
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);


        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuInvoices();

        //Get Invoice Date and amount for second
        acctInvoice = new AccountInvoices(driver);
        renewalDownPaymentDueAmountLien = acctInvoice.getInvoiceDueAmountByDueDate(repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, -1));

        acctMenu.clickAccountMenuActionsNewDirectBillPayment();
        directPayment = new NewDirectBillPayment(driver);
        directPayment.makeLienHolderPaymentExecute(renewalDownPaymentDueAmountLien, this.myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(myPolicyObj.busOwnLine.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day, 2));

        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);
    }


    @Test(dependsOnMethods = {"makeRenewalDownPaymentOnLien"})
    public void reversePaymentMadeAndCheckForDelinquency() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);
        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuPayments();
        payment = new AccountPayments(driver);
        payment = new AccountPayments(driver);
        payment.reversePayment(renewalDownPaymentDueAmountLien, null, null, repository.gw.enums.PaymentReversalReason.Payment_Moved);

        reversalDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);

        // letting account to go Delinquent in this search time
        gotoAccount(this.myPolicyObj.accountNumber);

        acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);

        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(repository.gw.enums.OpenClosed.Open, this.lienholderNumber, this.myPolicyObj.busOwnLine.getPolicyNumber(), reversalDate), "Delinquency does not exist Which is not Expected");
        orgDelinquentAmount = acctDelinquency.getOriginalDelinquentAmount(repository.gw.enums.OpenClosed.Open, this.lienholderNumber, this.myPolicyObj.busOwnLine.getPolicyNumber(), reversalDate);

    }

    @Test(dependsOnMethods = {"reversePaymentMadeAndCheckForDelinquency"})
    public void payDelinquentAmountAndCheckDelinquencyOnInsured() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienholderNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);

        acctMenu = new BCAccountMenu(driver);
        acctMenu.clickAccountMenuActionsNewDirectBillPayment();

        getValues = new BillingCenterHelper(driver).getVariablesToRunDelinquencyTestScenarios(orgDelinquentAmount);
        //getting values from HashMap
        payAmount = (double) getValues.get("payAmount");
        openOrClosed = (repository.gw.enums.OpenClosed) getValues.get("openOrClosed");
        errorMsg = (String) getValues.get("errorMsg");

        directPayment = new NewDirectBillPayment(driver);
        directPayment.makeLienHolderPaymentExecute(payAmount, this.myPolicyObj.busOwnLine.getPolicyNumber(), this.lienholderLoanNumber);

        //letting account to exit Delinquent in this search time
        gotoAccount(this.myPolicyObj.accountNumber);

        acctMenu.clickBCMenuDelinquencies();
        acctDelinquency = new BCCommonDelinquencies(driver);
        Assert.assertTrue(acctDelinquency.verifyDelinquencyStatus(openOrClosed, this.lienholderNumber, myPolicyObj.busOwnLine.getPolicyNumber(), reversalDate), errorMsg);

    }

}