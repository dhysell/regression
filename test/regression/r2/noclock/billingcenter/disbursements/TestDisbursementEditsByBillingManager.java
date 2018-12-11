package regression.r2.noclock.billingcenter.disbursements;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchInvoices;
import repository.bc.search.BCSearchPolicies;
import repository.bc.wizards.CreateAccountDisbursementWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DisbursementReason;
import repository.gw.enums.InvoiceStatus;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test verifies that the Billing manager has the authority to edit the due date on a disbursement after it was created.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/73867770028">DE4316</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-04%20Account%20Free-Form%20Disbursements.docx">Account Freeform Disbursements Requirements</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-02%20Insured%20Disbursement%20Requirements.docx">Insured Account Disbursements Requirements</a>
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/07%20-%20Disbursements/07-08%20LH%20Account%20Disbursement%20Requirements.docx">Lienholder Account Disbursements Requirements</a>
 * @Description
 * @DATE Feb 28, 2017
 */
public class TestDisbursementEditsByBillingManager extends BaseTest {
	private WebDriver driver;
    public ARUsers arUser = new ARUsers();
    public Agents pcAgent = new Agents();
    public String accountNumber = "";
    public String policyNumber = "";
    public double disbursementAmount = 50.00;

    @Test
    public void findAccountToUse() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRole(ARUserRole.Billing_Manager);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCSearchInvoices invoiceSearch = new BCSearchInvoices(driver);
        int randamNumber = NumberUtils.generateRandomNumberInt(270, 279);
        this.accountNumber = invoiceSearch.searchForAccountByInvoiceAndAmountRange(Integer.toString(randamNumber), 150, 500, InvoiceStatus.Due);
        this.accountNumber = this.accountNumber.substring(0, 6);

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        this.policyNumber = policySearch.searchPolicyByAccountNumber(this.accountNumber);

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickTopInfoBarAccountNumber();


        BCAccountSummary accountSummary = new BCAccountSummary(driver);
        double unappliedAmount = accountSummary.getUnappliedFundByPolicyNumber(this.policyNumber);

        if (unappliedAmount < this.disbursementAmount) {
            BCAccountMenu accountMenu = new BCAccountMenu(driver);
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();


            NewDirectBillPayment payment = new NewDirectBillPayment(driver);
            payment.makeDirectBillPaymentExecuteWithoutDistribution(500, this.policyNumber);
        }

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewTransactionDisbursement();


        Date todaysDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
        CreateAccountDisbursementWizard accountDisbursement = new CreateAccountDisbursementWizard(driver);
        accountDisbursement.createAccountDisbursement(this.policyNumber, this.policyNumber, this.disbursementAmount, todaysDate, DisbursementReason.Overpayment);

        accountMenu.clickAccountMenuDisbursements();

        AccountDisbursements accountDisbursements = new AccountDisbursements(driver);
        int rowCount = new TableUtils(driver).getRowCount(accountDisbursements.getDisbursementsTable());
        System.out.println("Number of Disbursements in table = " + rowCount);
        new TableUtils(driver).clickRowInTableByRowNumber(accountDisbursements.getDisbursementsTable(), rowCount);
        accountDisbursements.clickAccountDisbursementsEdit();
        try {
            accountDisbursements.setDisbursementsDueDate(DateUtils.dateAddSubtract(todaysDate, DateAddSubtractOptions.Day, 10));
        } catch (Exception e) {
            Assert.fail("The attempt to change the due date on the disbursement failed. Test Failed.");
        }

        new GuidewireHelpers(driver).logout();
    }
}
