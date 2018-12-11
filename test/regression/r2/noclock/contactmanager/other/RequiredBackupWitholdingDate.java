package regression.r2.noclock.contactmanager.other;


import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
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
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @author Steve Broderick
 * @Requirement - When IRS Lien set to Yes, a backup withholding date is required.
 * @Link - <a href="https://rally1.rallydev.com/#/10075774537d/detail/userstory/47093521415">Rally Link</a>
 * @Description - As a ClaimsCenter User of ContactManager or as Sales User of ContactManager, when the IRS Lien radiobutton is set to Yes, a backup withholding date is required.
 * @DATE - 11/30/2015
 */
public class RequiredBackupWitholdingDate extends BaseTest {
	private WebDriver driver;
    private AbUsers claimsUser;
    private AbUsers salesUser;
    private Date currentDate;

    @Test
    public void backUpWithHolding() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.currentDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager);
        this.claimsUser = AbUserHelper.getRandomDeptUser("Claims");
        new Login(driver).login(claimsUser.getUserName(), claimsUser.getUserPassword());

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
        Agents agent = AgentsHelper.getRandomAgent();
        this.salesUser = AbUserHelper.getRandomDeptUser("IS Programmers");
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        new Login(driver).login(salesUser.getUserName(), salesUser.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        menu.clickSearchTab();

        AdvancedSearchAB bankSearch = new AdvancedSearchAB(driver);
        bankSearch.setRoles("Agent");
        bankSearch.setCompanyName(agent.getAgentLastName());
        bankSearch.clickRandomSearchResult();

        verifyIRSVerified();
        resetBackupWithholding();
    }

    public void verifyIRSVerified() {
        ContactDetailsBasicsAB vendorPage = new ContactDetailsBasicsAB(driver);
        vendorPage.clickContactDetailsBasicsEditLink();
        vendorPage.setIRSLien(true);
        vendorPage.clickContactDetailsBasicsUpdateLink();
        if (!new GuidewireHelpers(driver).containsErrorMessage("Backup Withholding 1 :")) {
            Assert.fail(driver.getCurrentUrl() + claimsUser.getUserName() + " set the IRS Lien? to Yes. No Backup Withholding 1 date was entered. The validation message was not generated.");
        }
        vendorPage = new ContactDetailsBasicsAB(driver);
        vendorPage.setBackupWithholdingDate1(currentDate);
        vendorPage.clickContactDetailsBasicsUpdateLink();
    }

    public void resetBackupWithholding() {
        ContactDetailsBasicsAB vendorPage = new ContactDetailsBasicsAB(driver);
        vendorPage.clickContactDetailsBasicsEditLink();
        vendorPage.clearBackupWithholdingDate1();
        vendorPage.setIRSLien(false);
        vendorPage.clickContactDetailsBasicsUpdateLink();

    }
}
