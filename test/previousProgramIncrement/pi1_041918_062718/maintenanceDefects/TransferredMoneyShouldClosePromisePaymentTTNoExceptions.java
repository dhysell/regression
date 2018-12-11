package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.common.BCCommonTroubleTicket;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchPolicies;
import repository.bc.wizards.NewTransferWizard;
import repository.driverConfiguration.Config;
import repository.gw.enums.AccountType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.TransferReason;
import repository.gw.enums.TroubleTicketFilter;
import repository.gw.enums.TroubleTicketType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GeneratePolicyHelper;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;


/**
 * @Author sgunda
 * @Requirement Transferred money not closing "promise payment trouble ticket"
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558469364d/detail/defect/198277898084">DE7196 Transferred money not closing "promise payment trouble ticket"</a>
 * @DATE Jun 05, 2018
 */

public class TransferredMoneyShouldClosePromisePaymentTTNoExceptions extends BaseTest {
	private WebDriver driver;
    private GeneratePolicy myPolicyObj ;
    private ARUsers arUser = new ARUsers();
    private String accountNumber;
    private BCSearchAccounts bcSearchAccounts;
    private BCAccountMenu bcAccountMenu;

    @Test
    public void generatePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicyHelper(driver).generatePLSectionIIIPersonalAutoLinePLPolicy("TT Close For","Transfer",null,PaymentPlanType.Annual,PaymentType.Cash);

     }

    @Test(dependsOnMethods = {"generatePolicy"})
    public void transferMoneyAndSeeIfTTCloses() throws Exception {

        this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(arUser.getUserName(), arUser.getPassword(),myPolicyObj.squire.getPolicyNumber());
        BCCommonTroubleTickets bcCommonTroubleTickets =new BCCommonTroubleTickets(driver);
        Assert.assertTrue(bcCommonTroubleTickets.waitForTroubleTicketsToArrive(120),"Trouble ticket did not arrive after waiting for 2 min , test can not continue");

        bcSearchAccounts = new BCSearchAccounts(driver);
        accountNumber = bcSearchAccounts.findRecentAccountInGoodStanding("01-275" ,AccountType.Insured);
        bcSearchAccounts.searchAccountByAccountNumber(accountNumber);

        bcAccountMenu =  new BCAccountMenu(driver);
        bcAccountMenu.clickAccountMenuActionsNewDirectBillPayment();
        new NewDirectBillPayment(driver).makeDirectBillPaymentExecuteWithoutDistribution(5000);

        bcAccountMenu.clickActionsNewTransactionTransfer();
        new NewTransferWizard(driver).createNewTransfer(1, myPolicyObj.accountNumber, myPolicyObj.squire.getPolicyNumber(),myPolicyObj.squire.getPremium().getDownPaymentAmount(), myPolicyObj.squire.getPolicyNumber(), null, TransferReason.Other);
        new BCSearchPolicies(driver).searchPolicyByPolicyNumber(myPolicyObj.squire.getPolicyNumber());
        new BCPolicyMenu(driver).clickBCMenuTroubleTickets();

        bcCommonTroubleTickets.setTroubleTicketFilter(TroubleTicketFilter.All);
        bcCommonTroubleTickets.clickTroubleTicketNumberBySubjectOrType(TroubleTicketType.PromisePayment);
        Assert.assertTrue(new BCCommonTroubleTicket(driver).verifyIfTroubleTicketIsInClosedStatus(), "TT did not close after making transfer");

    }
}