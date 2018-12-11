package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.NoExceptions;

import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonCharges;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.helpers.GuidewireHelpers;
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

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

@Test(groups = {"ClockMove", "BillingCenter"})
public class DE7383_PolicyShouldBeAddedToZeroDollarRenewalDownIfPlannedNoExceptions extends BaseTest {

    private GeneratePolicy generatePolicy;
    private WebDriver driver;

    private String lienHolderNumber = null;
    private String lienholderLoanNumber = null;
    private double lienHolderLoanPremiumAmount;
    private ARUsers arUser = new ARUsers();
    private String invoiceNumber = null;


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
        
        generatePolicy = new GeneratePolicyHelper(driver).generateFullyLienBilledSquirePLPolicy("DE7383","Test",null,null,null, generateContacts);

        this.lienHolderNumber = this.generatePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLienholderNumber();
        this.lienholderLoanNumber = this.generatePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getLoanContractNumber();
        this.lienHolderLoanPremiumAmount = this.generatePolicy.squire.propertyAndLiability.locationList.get(0).getPropertyList().get(0).getAdditionalInterestList().get(0).getAdditionalInterestPremiumAmount();


    }

    @Test(dependsOnMethods = { "testGenerateFullyLienBilledPLPolicy" })
    public void makeLienPayment() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.lienHolderNumber);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 15);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);

        new BCAccountMenu(driver).clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecute(generatePolicy.squire.getPolicyNumber(), this.lienholderLoanNumber, this.lienHolderLoanPremiumAmount);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 20);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"makeLienPayment"})
    public void issueRenewal() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.helpers.DateUtils.dateAddSubtract(generatePolicy.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day,-47));

        new StartRenewal(driver).loginAsUWAndIssueRenewal(generatePolicy);

    }


    @Test(dependsOnMethods = { "issueRenewal" })
    public void makeLienRenewalDownBilled() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.generatePolicy.accountNumber);

        new BCAccountMenu(driver).clickBCMenuCharges();
        BCCommonCharges charges = new BCCommonCharges(driver);
        Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Renewal), "Renewal charges didn't make it to BC, Test can not continue");

        repository.gw.helpers.ClockUtils.setCurrentDates(cf, repository.gw.enums.DateAddSubtractOptions.Day, 10);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice);
        new BatchHelpers(cf).runBatchProcess(repository.gw.enums.BatchProcess.Invoice_Due);

        new BCAccountMenu(driver).clickAccountMenuInvoices();
        try {
            invoiceNumber = new AccountInvoices(driver).getInvoiceNumber(null, null, 0.0, repository.gw.enums.InvoiceType.RenewalDownPayment);
        } catch (Exception e ){
            fail(" Either Zero $ invoice was not found or method failed to get the invoice number");
        }
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = { "makeLienRenewalDownBilled" })
    public void makePolicyChangeAndAddCoverage() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        repository.gw.helpers.ClockUtils.setCurrentDates(cf, DateUtils.dateAddSubtract(generatePolicy.squire.getExpirationDate(), repository.gw.enums.DateAddSubtractOptions.Day,-23));

        generatePolicy.updateDriver(driver);
        generatePolicy.addSquireLine(generatePolicy.squire.getExpirationDate(), repository.gw.enums.LineSelection.PersonalAutoLinePL);
    }

    @Test(dependsOnMethods = { "makePolicyChangeAndAddCoverage" })
    public void verifyPolicyChangeAndInvoices() throws Exception {

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);

        new Login(driver).loginAndSearchAccountByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.generatePolicy.accountNumber);

        new BCAccountMenu(driver).clickBCMenuCharges();
        BCCommonCharges charges = new BCCommonCharges(driver);
        Assert.assertTrue(charges.waitUntilChargesFromPolicyContextArrive(repository.gw.enums.TransactionType.Policy_Change), "Policy_Change didn't make it to BC, Test can not continue");

        new BCAccountMenu(driver).clickAccountMenuInvoices();
        assertTrue(new AccountInvoices(driver).getInvoiceAmountByInvoiceNumber(invoiceNumber)!=0.0, "Policy change premium has now been added to the renewal down , please investigate ");
        new GuidewireHelpers(driver).logout();
    }

}
