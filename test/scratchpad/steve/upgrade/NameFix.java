package scratchpad.steve.upgrade;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.ab.contact.ContactNameCasingAB;
import repository.ab.sidebar.SidebarAB;
import repository.driverConfiguration.Config;
import repository.gw.generate.custom.ContactName;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class NameFix extends BaseTest {
    private AbUsers user;
    private int row;
    private ContactName contactName;
    private WebDriver driver;

    public void getToContactNameCasingPage() throws Exception {
        Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.user = AbUserHelper.getRandomDeptUser("Policy Services");
        new Login(driver).login(this.user.getUserName(), this.user.getUserPassword());
        SidebarAB sidemenu = new SidebarAB(driver);
        sidemenu.clickNames();        
    }


    @Test
    public void fixName() throws Exception {
        getToContactNameCasingPage();
        ContactNameCasingAB nameCasingPage = new ContactNameCasingAB(driver);
        nameCasingPage.fixName(this.contactName.getNewName(), this.contactName.getNewName() + " Whoot!");
        if (nameCasingPage.checkIfNameExistsCurrentPage(contactName.getNewName())) {
            Assert.fail(driver.getCurrentUrl() + this.contactName.getNewName() + " was found after updating the contact.");
        }
    }

    @Test(dependsOnMethods = {"fixName"})
    public void ignoreContactCase() throws Exception {
        getToContactNameCasingPage();
        ContactNameCasingAB nameCasingPage = new ContactNameCasingAB(driver);
        ContactName contactName = nameCasingPage.ignoreContactCase();
        if (nameCasingPage.checkIfNameExistsCurrentPage(contactName.getCurrentName())) {
            Assert.fail(driver.getCurrentUrl() + "Clicking the Ignore button functionality did not complete for " + contactName.getNewName() + ". Please investigate.");
        }
    }

    @Test(dependsOnMethods = {"ignoreContactCase"})
    public void manualChangeRevert() throws Exception {
        getToContactNameCasingPage();
        ContactNameCasingAB nameCasingPage = new ContactNameCasingAB(driver);
        int row = nameCasingPage.revertNameFix();
        this.contactName = nameCasingPage.getNameData(row);
        nameCasingPage.systemOut("Name after Revert: " + nameCasingPage.getNameData(row).getNewName());
        nameCasingPage.systemOut("Name before Revert: " + contactName.getNewName());
        if (!(this.contactName.getNewName().equals(nameCasingPage.getNameData(row).getNewName()))) {
            Assert.fail(driver.getCurrentUrl() + "Reverting the change functionality did not complete for " + contactName.getNewName() + ". Please investigate.");
        }
    }


}
