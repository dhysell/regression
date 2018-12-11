package regression.r2.noclock.policycenter.rolespermissions;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.bc.account.AccountCharges;
import repository.bc.account.AccountInvoices;
import repository.bc.account.BCAccountMenu;
import repository.bc.account.actions.NewDirectBillPayment;
import repository.bc.account.summary.BCAccountSummary;
import repository.bc.common.BCCommonContacts;
import repository.bc.common.BCCommonDocuments;
import repository.bc.common.actions.BCCommonActionsNewNote;
import repository.bc.policy.BCPolicyMenu;
import repository.bc.policy.PolicyNotes;
import repository.bc.policy.summary.BCPolicySummary;
import repository.bc.search.BCSearchAccounts;
import repository.bc.search.BCSearchInvoices;
import repository.bc.search.BCSearchPolicies;
import repository.driverConfiguration.Config;
import repository.gw.enums.InvoiceStatus;
import repository.gw.enums.PolicyNotes.RelatedTo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyBilling;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.ARUsersHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author bhiltbrand
 * @Requirement In order to support Center Connect, a new user and role were made (user: pcuser role: pcviewonly) to allow PC users limited
 * access to BC information. This test verifies that the pcuser user only has the ability to see and use the functions declared.
 * @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Release%202%20Requirements/15%20-%20Roles%20Permissions/15-03%20PC%20View%20Only%20Role.docx">Business Requirements</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/47933317054"> Rally Story US6255</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/60459169539"> Rally Story US8762</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/60459168153"> Rally Story US8759</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/60459168494"> Rally Story US8760</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/userstory/60459168794"> Rally Story US8761</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/60646046173"> Rally Defect DE3858</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/41432362343d/detail/defect/60645433649"> Rally Defect DE3857</a>
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/68999021356d/detail/defect/82130750216"> Rally Defect DE4540</a>
 * @DATE Dec 29, 2015
 * Updated Sept 02, 2016
 */
@QuarantineClass
public class TestPCToBCCenterConnect extends BaseTest {
    public ARUsers arUser = new ARUsers();
    public Agents pcAgent = new Agents();
    public String accountNumber = "";
    public String truncatedAccountNumber = "";
    public String policyNumber = "";
    public String agentName = "";

    private WebDriver driver;

    @Test
    public void findAccountToUse() throws Exception {
        try {
            this.arUser = ARUsersHelper.getRandomARUserByRole(ARUserRole.Billing_Manager);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        new Login(driver).login(arUser.getUserName(), arUser.getPassword());

        BCSearchInvoices invoiceSearch = new BCSearchInvoices(driver);
        this.accountNumber = invoiceSearch.searchForAccountByInvoiceAndAmountRange("245", 150, 500, InvoiceStatus.Due);
        this.truncatedAccountNumber = this.accountNumber.substring(0, 6);

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        this.policyNumber = policySearch.searchPolicyByAccountNumber(this.truncatedAccountNumber);

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuActionsNewNote();


        BCCommonActionsNewNote newNote = new BCCommonActionsNewNote(driver);
        newNote.setNewNoteRelatedTo(RelatedTo.Trouble_Tickets);
        newNote.setNewNoteSubject("This is a Note");
        newNote.setNewNoteText("Here is a note to test.");
        newNote.clickUpdate();

        BCSearchAccounts accountSearch = new BCSearchAccounts(driver);
        accountSearch.searchAccountByAccountNumber(this.truncatedAccountNumber);


        BCAccountMenu menu = new BCAccountMenu(driver);
        menu.clickBCMenuContacts();


        BCCommonContacts accountContacts = new BCCommonContacts(driver);
        this.agentName = accountContacts.getAgentName();

        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuActionsNewDirectBillPayment();

        NewDirectBillPayment directPayment = new NewDirectBillPayment(driver);
        directPayment.makeDirectBillPaymentExecuteWithoutDistribution(120.00, this.policyNumber);
        new GuidewireHelpers(driver).logout();

    }

    @Test(dependsOnMethods = {"findAccountToUse"}, enabled = true)
    public void billingDetailsPCtoBCTest() throws Exception {
        String[] agentNameSplit = StringsUtils.firstLastNameParser(this.agentName);

        try {
            this.pcAgent = AgentsHelper.getAgentByName(agentNameSplit[0], agentNameSplit[1]);
        } catch (Exception e) {
            System.out.println(e);
            Assert.fail("While attempting to get the AR User, the hibernate connection failed.");
        }

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchPolicyByAccountNumber(pcAgent.getAgentUserName(), pcAgent.getAgentPassword(), this.truncatedAccountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsBilling();


        PolicyBilling billingPage = new PolicyBilling(driver);
        String mainWindowHandle = null;
        try {
            mainWindowHandle = billingPage.clickViewInBillingCenterButton();
        } catch (Exception e) {
            Assert.fail("The test was not able to switch to the pop-up window that should have been there. This most likely indicates that permissions for the agent role were not in place for the billing link to be clickable. Please verify.");
        }

        BCPolicyMenu policyMenu = new BCPolicyMenu(driver);
        try {
            policyMenu.clickPolicyNotes();
        } catch (Exception e) {
            Assert.fail("The attempt to click the policy notes side menu item failed. Test Failed");
        }
        PolicyNotes policyNotes = new PolicyNotes(driver);
        policyNotes.selectRelatedTo(RelatedTo.Trouble_Tickets);
        policyNotes.clickSearch();
        RelatedTo relatedToValue = policyNotes.getNotesRelatedTo(1);
        if (!relatedToValue.equals(RelatedTo.Trouble_Tickets)) {
            Assert.fail("The Policy Notes Search did not return an expected value. Test Failed.");
        }

        try {
            policyNotes.clickReset();
        } catch (Exception e) {
            Assert.fail("The attempt to reset the note search failed. Test Failed.");
        }

        try {
            policyNotes.clickViewInPolicyCenterButton();
            Assert.fail("The \"View in Policy Center\" Button was clickable, but should not have been. Test failed.");
        } catch (Exception e) {
            System.out.println("The \"View in Policy Center\" Button was not clickable. This is expected.");
        }


        policyMenu = new BCPolicyMenu(driver);
        try {
            policyMenu.clickBCMenuDocuments();
        } catch (Exception e) {
            Assert.fail("The attempt to click the policy documents side menu link failed. Test failed.");
        }


        BCCommonDocuments documents = new BCCommonDocuments(driver);
        try {
            documents.clickViewInPolicyCenterButton();
            Assert.fail("The \"View in Policy Center\" Button was clickable, but should not have been. Test failed.");
        } catch (Exception e) {
            System.out.println("The \"View in Policy Center\" Button was not clickable. This is expected.");
        }


        try {
            policyMenu.clickPaymentSchedule();
        } catch (Exception e) {
            Assert.fail("The attempt to click the policy Payment Schedule side menu link failed. Test failed.");
        }


        //This check is for a link to lienholder payment functionality that was inadvertently removed. It will be brought back, but is no longer in scope for the time being.
        //When it is brought back, please uncomment this code. - Brett Hiltbrand 09/02/2016
		/*try {
			policyMenu.clickLienholderPayments();
		} catch (Exception e) {
			Assert.fail("The attempt to click the policy Lienholder Payments side menu link failed. Test failed.");
		}

		*/

        try {
            policyMenu.clickInsuredPayments();
        } catch (Exception e) {
            Assert.fail("The attempt to click the policy Insured Payments side menu link failed. Test failed.");
        }


        /*
         * AccountPayments payments = AccountFactory.getAccountPaymentsPage();
         * try { payments.getPaymentsAndCreditDistributionsTableRow(DateUtils.
         * getCenterDate(Product.BillingCenter), null, null, null, null, null,
         * null, null, 120.00, null, null, null); } catch (Exception e) {
         * Assert.fail(
         * "The search for the insured payment made previously did not return any results. Test Failed."
         * ); }
         */

        policyMenu = new BCPolicyMenu(driver);
        try {
            policyMenu.clickBCMenuSummary();
        } catch (Exception e) {
            Assert.fail("The attempt to click the policy summary side menu link failed. Test failed.");
        }


        BCPolicySummary policySummary = new BCPolicySummary(driver);

        if (!policySummary.checkIfAccountNumberLinkIsActive()) {
            Assert.fail("The Account Number was not available to be clicked on the Policy Summary page. This should not be the case. Test Failed.");
        }

        if (!policySummary.checkIfAccountHolderNameLinkIsActive()) {
            Assert.fail("The Account Name was not available to be clicked on the Policy Summary page. This should not be the case. Test Failed.");
        }

        if (policySummary.checkIfPaymentPlanLinkIsActive()) {
            Assert.fail("The Payment Plan Link was available to be clicked on the Policy Summary page. This should not be the case. Test Failed.");
        }

        if (policySummary.checkIfDelinquencyPlanLinkIsActive()) {
            Assert.fail("The Delinquency Plan Link was available to be clicked on the Policy Summary page. This should not be the case. Test Failed.");
        }


        try {
            policySummary.clickEdit();
            Assert.fail("The attempt to click the policy summary edit link passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the policy summary edit link failed. This is expected.");
        }

        try {
            policySummary.clickInvoiceOverridesArrow();
            Assert.fail("The attempt to click the policy summary invoice override dropdown arrow passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the policy summary invoice override dropdown arrow failed. This is expected.");
        }

        try {
            policySummary.clickBillingMethodArrow();
            Assert.fail("The attempt to click the policy summary billing method dropdown arrow passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the policy summary billing method dropdown arrow failed. This is expected.");
        }

        try {
            policySummary.clickPaymentRestrictionArrow();
            Assert.fail("The attempt to click the policy summary payment restriction dropdown arrow passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the policy summary payment restriction dropdown arrow failed. This is expected.");
        }

        TableUtils tableUtils = new TableUtils(driver);
        int policyOpenActivitiesTableRowCount = tableUtils.getRowCount(policySummary.getOpenActivitiesTable());
        if (policyOpenActivitiesTableRowCount > 0) {
            try {
                tableUtils.clickLinkInSpecficRowInTable(policySummary.getOpenActivitiesTable(), 1);
                Assert.fail("The attempt to click in to the subject of the first activity on the open activites table passed. This should not happen. Test failed.");
            } catch (Exception e) {
                System.out.println("The attempt to click in to the subject of the first activity on the open activites table failed. This is expected.");
            }
        }


        //This is to verify that the actions menu is not available to open
        policyMenu = new BCPolicyMenu(driver);
        try {
            policyMenu.clickBCMenuActionsNewNote();
            Assert.fail("The attempt to click the policy actions menu new note link passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the policy actions menu new note link failed. This is expected.");
        }


        //Moving to Account Level
        policyMenu = new BCPolicyMenu(driver);
        policyMenu.clickBCMenuSummary();


        policySummary = new BCPolicySummary(driver);
        try {
            policySummary.clickAccountNumberLink();
            System.out.println("The attempt to click the Account Number Link Passed. This is expected, continuing test...");
        } catch (Exception e) {
            Assert.fail("The attempt to click the Account Number Link failed. This should not have happened. Test failed.");
        }


        BCAccountSummary accountSummary = new BCAccountSummary(driver);
        int accountOpenActivitiesTableRowCount = tableUtils.getRowCount(accountSummary.getOpenActivitiesTable());
        if (accountOpenActivitiesTableRowCount > 0) {
            try {
                tableUtils.clickLinkInSpecficRowInTable(accountSummary.getOpenActivitiesTable(), 1);
                Assert.fail("The attempt to click in to the subject of the first activity on the open activites table passed. This should not happen. Test failed.");
            } catch (Exception e) {
                System.out.println("The attempt to click in to the subject of the first activity on the open activites table failed. This is expected.");
            }
        }

        int accountRecentPaymentsTableRowCount = tableUtils.getRowCount(accountSummary.getAccountRecentPaymentsReceivedTable());
        if (accountRecentPaymentsTableRowCount > 0) {
            try {
                tableUtils.clickLinkInSpecficRowInTable(accountSummary.getAccountRecentPaymentsReceivedTable(), 1);
                Assert.fail("The attempt to click in on the actions drop-down menu of the first payment record on the payments received table passed. This should not happen. Test failed.");
            } catch (Exception e) {
                System.out.println("The attempt to click in on the actions drop-down menu of the first payment record on the payments received table failed. This is expected.");
            }
        }

        try {
            accountSummary.clickEdit();
            Assert.fail("The attempt to click the Account summary edit link passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the Account summary edit link failed. This is expected.");
        }

        //This is to verify that the actions menu is not available to open
        BCAccountMenu accountMenu = new BCAccountMenu(driver);
        try {
            accountMenu.clickAccountMenuActionsNewDirectBillPayment();
            Assert.fail("The attempt to click the account actions menu new direct bill payment link passed. This should not be allowed. Test failed.");
        } catch (Exception e) {
            System.out.println("The attempt to click the account actions menu new direct bill payment link failed. This is expected.");
        }

        if (!accountSummary.checkIfPolicyNumberLinkInOpenPolicyStatusTableIsActive()) {
            Assert.fail("The Policy Number Link was not available to be clicked on the Account Summary page. This should not be the case. Test Failed.");
        }

        if (accountSummary.checkIfPaymentAllocationPlanLinkIsActive()) {
            Assert.fail("The Payment Allocation Plan Link was available to be clicked on the Account Summary page. This should not be the case. Test Failed.");
        }

        if (!accountSummary.checkIfPolicyNumberLinkInOpenPolicyStatusTableIsActive()) {
            Assert.fail("The Delinquency Plan Link was not available to be clicked on the Account Summary page. This should not be the case. Test Failed.");
        }

        try {
            accountMenu = new BCAccountMenu(driver);
            accountMenu.clickAccountMenuFundsTracking();
            Assert.fail("The Funds Tracking link was available on the Account Menu and was clicked. Test Failed.");
        } catch (Exception e) {
            System.out.println("The Funds Tracking link was not available on the Account Menu. This was expected.");
        }

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickBCMenuCharges();


        AccountCharges charges = new AccountCharges(driver);

        List<WebElement> chargesTableRows = tableUtils.getAllTableRows(charges.getChargesOrChargeHoldsPopupTable());
        for (WebElement row : chargesTableRows) {
            if (!tableUtils.getCellTextInTableByRowAndColumnName(charges.getChargesOrChargeHoldsPopupTable(), tableUtils.getRowNumberFromWebElementRow(row), "Default Payer").contains(this.accountNumber)) {
                WebElement differentPayerLink = tableUtils.getCellWebElementInTableByRowAndColumnName(charges.getChargesOrChargeHoldsPopupTable(), tableUtils.getRowNumberFromWebElementRow(row), "Default Payer");
                if (new GuidewireHelpers(driver).checkIfLinkIsActive(differentPayerLink)) {
                    Assert.fail("The link for the different payer than the insured was available to be clicked. This should not happen. Test failed.");
                } else {
                    System.out.println("The link for the different payer than the insured was unavailable to be clicked. This is expected.");
                }
            } else {
                System.out.println("There were no default payers that were different than the insured on this random policy. Will attempt this test in subsequent runs.");
            }
        }

        if (charges.checkIfContextLinkIsActive()) {
            Assert.fail("The Charges Context Link was available to be clicked on the Account Charges page. This should not be the case. Test Failed.");
        }

        List<WebElement> chargesActionsLinks = charges.getChargesOrChargeHoldsPopupTable().findElements(By.xpath(".//descendant::a[contains(@id, ':ActionButton')]"));
        if (chargesActionsLinks.size() > 0) {
            Assert.fail("There was at least one actions button found in the Account Charges table. This should not be the case. Test failed.");
        }

        try {
            if (!charges.checkIfModifyInvoiceItemsButtonIsDisabled()) {
                Assert.fail("The Modify Invoice Items Button was clickable. This should not happen. Test Failed.");
            }
        } catch (Exception e) {
            System.out.println("The attempt to click on the Modify Invoice Items Button resulted in a caught exception. This most likely means that the element was not even on the page. This is expected.");
        }

        try {
            if (!charges.checkIfMoveInvoiceItemsButtonIsDisabled()) {
                Assert.fail("The Move Invoice Items Button was clickable. This should not happen. Test Failed.");
            }
        } catch (Exception e) {
            System.out.println("The attempt to click on the Move Invoice Items Button resulted in a caught exception. This most likely means that the element was not even on the page. This is expected.");
        }

        if (!charges.checkIfPolicyLinkIsActive()) {
            Assert.fail("The Charges Policy Number Link was not available to be clicked on the Account Charges page. This should not be the case. Test Failed.");
        }

        accountMenu = new BCAccountMenu(driver);
        accountMenu.clickAccountMenuInvoices();


        AccountInvoices invoices = new AccountInvoices(driver);

        try {
            if (!invoices.checkIfCreateNewInvoiceButtonIsDisabled()) {
                Assert.fail("The Create New Invoice Button was clickable. This should not happen. Test Failed.");
            }
        } catch (Exception e) {
            System.out.println("The attempt to click on the Create New Invoice Button resulted in a caught exception. This most likely means that the element was not even on the page. This is expected.");
        }

        try {
            if (!invoices.checkIfDeleteInvoiceButtonIsDisabled()) {
                Assert.fail("The Delete Invoice Button was clickable. This should not happen. Test Failed.");
            }
        } catch (Exception e) {
            System.out.println("The attempt to click on the Delete Invoice Button resulted in a caught exception. This most likely means that the element was not even on the page. This is expected.");
        }

        try {
            if (!invoices.checkIfChangeInvoiceDateButtonIsDisabled()) {
                Assert.fail("The Change Invoice Date Button was clickable. This should not happen. Test Failed.");
            }
        } catch (Exception e) {
            System.out.println("The attempt to click on the Change Invoice Date Button resulted in a caught exception. This most likely means that the element was not even on the page. This is expected.");
        }

        if (!invoices.checkIfPolicyLinkIsActive()) {
            Assert.fail("The Invoice Charges Policy Number Link was not available to be clicked on the Account Invoices page. This should not be the case. Test Failed.");
        }

        BCSearchPolicies policySearch = new BCSearchPolicies(driver);
        try {
            policySearch.searchPolicyByAccountNumber(this.accountNumber);
            Assert.fail("The attempt to search a new policy passed. This should not happen. Test Failed.");
        } catch (Exception e) {
            System.out.println("The attempt to search a new policy failed. This is expected.");
        }

        guidewireHelpers.logout();
        driver.switchTo().window(mainWindowHandle);
        guidewireHelpers.logout();
    }

    @Test(dependsOnMethods = {"findAccountToUse"})
    public void notesBCtoPCTest() throws Exception {
        Config cf = new Config(ApplicationOrCenter.BillingCenter);
        driver = buildDriver(cf);
        GuidewireHelpers guidewireHelpers = new GuidewireHelpers(driver);
        new Login(driver).loginAndSearchPolicyByAccountNumber(arUser.getUserName(), arUser.getPassword(), this.truncatedAccountNumber);

        BCPolicyMenu menu = new BCPolicyMenu(driver);
        menu.clickPolicyNotes();

        PolicyNotes policyNotes = new PolicyNotes(driver);
        String mainWindowHandle = null;
        try {
            mainWindowHandle = policyNotes.clickViewInPolicyCenterButton();
        } catch (Exception e) {
            Assert.fail(
                    "The test was not able to switch to the pop-up window that should have been there. This most likely indicates that permissions for the agent role were not in place for the billing link to be clickable. Please verify.");
        }

        // TODO: Validate the business rules tied around the permissions

        guidewireHelpers.logout();
        driver.switchTo().window(mainWindowHandle);
        guidewireHelpers.logout();
    }
}
