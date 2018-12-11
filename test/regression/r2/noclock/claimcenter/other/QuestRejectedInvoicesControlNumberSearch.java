package regression.r2.noclock.claimcenter.other;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.testng.listener.QuarantineClass;

import repository.cc.desktop.DesktopSidebarCC;
import repository.cc.desktop.QuestRejectedInvoicesCC;
import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.ClaimsUsers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
@QuarantineClass
public class QuestRejectedInvoicesControlNumberSearch extends BaseTest {
	private WebDriver driver;
    // Login Info
    private ClaimsUsers userName = ClaimsUsers.abatts;
    private String password = "gw";

    @Test()
    public void controlNumberSearch() throws Exception {

        Config cf = new Config(ApplicationOrCenter.ClaimCenter);
        driver = buildDriver(cf);
        new Login(driver).login(userName.toString(), password);

        TopMenu topMenu = new TopMenu(driver);
        topMenu.clickDesktopTab();

        DesktopSidebarCC sideBar = new DesktopSidebarCC(driver);
        sideBar.clickQuestRejectedInvoices();

        QuestRejectedInvoicesCC qri = new QuestRejectedInvoicesCC(driver);
        qri.searchByControlNumberRandom();
        qri.testCommentsField();


    }
}
	
