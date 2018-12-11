package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions2;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonDelinquencies;
import repository.bc.search.BCSearchAccounts;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

import java.util.ArrayList;
import java.util.Date;


@Test(groups = {"ClockMove", "BillingCenter"})
public class DE7822NullPointerOnExitingLienDelinquencyNoExceptions extends BaseTest {

    GeneratePolicy generatePolicy;
    private WebDriver driver;

    private String lienHolderNumber = null;
    private double lienHolderLoanPremiumAmount;
    private ARUsers arUser = new ARUsers();


    @Test
    public void testGenerateFullyLienBilledPLPolicy() throws  Exception {

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

        generatePolicy = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy("DE7822","PDLienCancel",null,null ,null, generateContacts);

        this.lienHolderNumber = this.generatePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienHolderLoanPremiumAmount = this.generatePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();
    }

    @Test(dependsOnMethods = {"testGenerateFullyLienBilledPLPolicy"})
    public void makePaymentsAndReverseItToMakePolicyCashOnly() throws Exception {

        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienHolderNumber);

        new BCAccountMenu(driver).clickAccountMenuInvoices();
        Date invoiceDate = new AccountInvoices(driver).invoiceDate(null,null, repository.gw.enums.InvoiceType.NewBusinessDownPayment,null,null,lienHolderLoanPremiumAmount,null);
        Date dayPassedDueDate = DateUtils.dateAddSubtract(new AccountInvoices(driver).dueDate(null,null, repository.gw.enums.InvoiceType.NewBusinessDownPayment,null,null,lienHolderLoanPremiumAmount,null), repository.gw.enums.DateAddSubtractOptions.Day,1);

        ClockUtils.setCurrentDates(driver, invoiceDate);
        new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        ClockUtils.setCurrentDates(driver, dayPassedDueDate);
        new BatchHelpers(driver).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        new BCSearchAccounts(driver).searchAccountByAccountNumber(generatePolicy.accountNumber);

        new BCAccountMenu(driver).clickBCMenuDelinquencies();
        Assert.assertTrue(new BCCommonDelinquencies(driver).verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Open, repository.gw.enums.DelinquencyReason.PastDueLienPartialCancel,null,dayPassedDueDate));
        new BCCommonDelinquencies(driver).clickExitDelinquencyButton();
        new BCAccountMenu(driver).clickBCMenuSummary();
        new BCAccountMenu(driver).clickBCMenuDelinquencies();
        Assert.assertTrue(new BCCommonDelinquencies(driver).verifyDelinquencyByReason(repository.gw.enums.OpenClosed.Closed, repository.gw.enums.DelinquencyReason.PastDueLienPartialCancel,null,dayPassedDueDate),"Unable to exit Lien delinquency");
    }
}
