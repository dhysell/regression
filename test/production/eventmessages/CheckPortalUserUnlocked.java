package production.eventmessages;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.helpers.EmailUtils;

import repository.cc.topmenu.TopMenu;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
public class CheckPortalUserUnlocked extends BaseTest {
	private WebDriver driver;
	
    @Test
    public void checkPortalUser() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ClaimCenter, "PRD");
        driver = buildDriver(cf);
        new Login(driver).login("emessaging", "gwqa");

        TopMenu tM = new TopMenu(driver);

        boolean isAccountLocked = tM.clickAdministrationTab().clickUsersAndSecurity().searchForUserCheckIfLocked("pu");


        if (isAccountLocked) {
            ArrayList<String> emailsToSendTo = new ArrayList<String>();
            emailsToSendTo.add("ktennant@idfbins.com");
            emailsToSendTo.add("cjoslin@idfbins.com");
            emailsToSendTo.add("iclouser@idfbins.com");
            emailsToSendTo.add("shokanson@idfbins.com");
            emailsToSendTo.add("ryoung@idfbins.com");
            emailsToSendTo.add("jjuarez@idfbins.com");

            String emailContents = "To Whom it May Concern,<br/><br/><p>We just ran a check against the PRD ClaimCenter and found that the portal user has become locked. This will effect our Insureds ability to file/use the Claims Portal</p>"
                    + "<p>Please unlock the portal user in production.</p>";

            new EmailUtils().sendEmail(emailsToSendTo, "Portal User Locked in Production CC", emailContents, null);

        }

        Assert.assertFalse(isAccountLocked, "ERROR Portal User Appears to be locked this will effect PROD claims Portal!!!!!!");

    }
}
