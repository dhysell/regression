package regression.r2.noclock.billingcenter.other;


import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountCharges;
import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonTroubleTickets;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.topmenu.BCTopMenuPolicy;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.Priority;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.Status;
import repository.gw.enums.TransactionType;
import repository.gw.enums.TroubleTicketType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.cancellation.StartCancellation;
import persistence.enums.ARCompany;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author bhiltbrand
 * @Requirement This test checks that when a policy is flat cancelled, that the promised payment trouble ticket holds are released and that the trouble ticket itself is closed.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/7832667974d/detail/defect/190849614852">Rally Defect DE7064</a>
 * @Description
 * @DATE Jan 25, 2018
 */
public class TestFlatCancelTroubleTicketHolds extends BaseTest {
	private WebDriver driver;
    public GeneratePolicy myPolicyObj, myNewTermPolicyObjPL;
    public Agents agent;
    public Underwriters underwriter;
    public ARUsers arUser;

    @Test
    public void generateAutoOnlyPolicy() throws Exception {
        this.agent = AgentsHelper.getRandomAgent();

        SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh, MedicalLimit.TenK);
        SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
        squirePersonalAuto.setCoverages(coverages);

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        myPolicyObj = new GeneratePolicy.Builder(driver)
                .withAgent(this.agent)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PersonalAutoLinePL)
                .withInsPersonOrCompany(ContactSubType.Person)
                .withInsFirstLastName("Rewrite", "Invoicing")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Semi_Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
    }

    @Test(dependsOnMethods = {"generateAutoOnlyPolicy"})
    public void waitForChargesInBC() throws Exception {
        this.arUser = ARUsersHelper.getRandomARUserByCompany(ARCompany.Personal);
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
        accountCharges.waitUntilIssuanceChargesArrive(120);

        BCTopMenuPolicy topMenu = new BCTopMenuPolicy(driver);
        topMenu.menuPolicySearchPolicyByPolicyNumber(this.myPolicyObj.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets troubleTickets = new BCCommonTroubleTickets(driver);
        troubleTickets.waitForTroubleTicketsToArrive(120);
        new GuidewireHelpers(driver).logout();
    }

    @Test(dependsOnMethods = {"waitForChargesInBC"}, enabled = true)
    public void flatCancelPolicy() throws Exception {
        this.underwriter = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).loginAndSearchPolicyByPolicyNumber(this.underwriter.getUnderwriterUserName(), this.underwriter.getUnderwriterPassword(), this.myPolicyObj.squire.getPolicyNumber());
        StartCancellation flatCancel = new StartCancellation(driver);
        flatCancel.cancelPolicy(CancellationSourceReasonExplanation.SubmittedOnAccident, "Flat Cancel", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), true);
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"flatCancelPolicy"}, enabled = true)
    public void verifyTroubleTicketInBC() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
        new Login(driver).loginAndSearchAccountByAccountNumber(this.arUser.getUserName(), this.arUser.getPassword(), this.myPolicyObj.accountNumber);
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickBCMenuCharges();

        AccountCharges accountCharges = new AccountCharges(driver);
        accountCharges.waitUntilChargesFromPolicyContextArrive(TransactionType.Cancellation);

        BCTopMenuPolicy topMenu = new BCTopMenuPolicy(driver);
        topMenu.menuPolicySearchPolicyByPolicyNumber(this.myPolicyObj.squire.getPolicyNumber());

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuTroubleTickets();

        BCCommonTroubleTickets troubleTickets = new BCCommonTroubleTickets(driver);
        if (new TableUtils(driver).getRowCount(troubleTickets.getTroubleTicketsTable()) > 0) {
            try {
                troubleTickets.getTroubleTicketTableRow(null, null, DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter), null, Priority.Normal, Status.Open, TroubleTicketType.PromisePayment);
                Assert.fail("The Promised Payment trouble ticket should have been closed, but was not. Test Failed.");
            } catch (Exception e) {
                //Test Passed
            }
        }
        new GuidewireHelpers(driver).logout();
    }
}
