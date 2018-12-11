package scratchpad.steve;


import java.util.Date;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @author Steve Broderick
 * @Requirement - When IRS Lien set to Yes, a backup withholding date is required.
 * @Link - <a href="https://rally1.rallydev.com/#/10075774537d/detail/userstory/47093521415">Rally Link</a>
 * @Description - As a ClaimsCenter User of ContactManager or as Sales User of ContactManager, when the IRS Lien radiobutton is set to Yes, a backup withholding date is required.
 * @DATE - 11/30/2015
 */
public class RequiredBackupWitholdingDate extends BaseTest {
    private String userName = "swerth";
    private String password = "gw";
    private Agents agent;
    private Date currentDate;
    private WebDriver driver;

    @Test
    public void backUpWithHolding() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        this.driver = buildDriver(cf);
        this.currentDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager);
        new Login(driver).login(userName, password);

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        AdvancedSearchAB bankSearch = new AdvancedSearchAB(driver);
        bankSearch.setRoles("Vendor");
        bankSearch.setCompanyName("abc");
        bankSearch.clickRandomSearchResult();
        verifyIRSVerified();
        resetBackupWithholding();

    }

    @Test(dependsOnMethods = {"backUpWithHolding"})
    public void navigateToAgent() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        agent = AgentsHelper.getRandomAgent();
        new Login(driver).login(userName, password);

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        AdvancedSearchAB bankSearch = new AdvancedSearchAB(driver);
        bankSearch.setRoles("Agent");
        bankSearch.setCompanyName(agent.getAgentLastName());
        bankSearch.clickRandomSearchResult();

        ContactDetailsBasicsAB agentPage = new ContactDetailsBasicsAB(driver);
        String text = agentPage.getContactPageTitle();
        if (!text.equals(this.agent.getAgentFirstName() + " " + this.agent.getAgentLastName())) {
            throw new Exception("The search for " + this.agent.getAgentFirstName() + " " + this.agent.getAgentLastName() + " brought up the " + text + " page.");
        }
        verifyIRSVerified();
        resetBackupWithholding();

    }

    private void verifyIRSVerified() {
        ContactDetailsBasicsAB vendorPage = new ContactDetailsBasicsAB(driver);
        vendorPage.clickContactDetailsBasicsEditLink();
        vendorPage.setIRSLien(true);
        vendorPage.clickContactDetailsBasicsUpdateLink();
        if (!new GuidewireHelpers(driver).containsErrorMessage("Backup Withholding 1 :")) {
            Assert.fail(driver.getCurrentUrl() + userName + " set the IRS Lien? to Yes. No Backup Withholding 1 date was entered. The validation message was not generated.");
        }
        vendorPage = new ContactDetailsBasicsAB(driver);
        vendorPage.setBackupWithholdingDate1(currentDate);
        vendorPage.clickContactDetailsBasicsUpdateLink();
    }

    private void resetBackupWithholding() {
        ContactDetailsBasicsAB vendorPage = new ContactDetailsBasicsAB(driver);
        vendorPage.clickContactDetailsBasicsEditLink();
        vendorPage.clearBackupWithholdingDate1();
        vendorPage.setIRSLien(false);
        vendorPage.clickContactDetailsBasicsUpdateLink();

    }
}
