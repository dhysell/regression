package scratchpad.rusty.misc;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.Test;

import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuAccountPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.topmenu.TopMenuPC;

public class GW8MenuTest extends BaseTest {

    private String username = "su";
    private String password = "gw";
    private WebDriver driver;

    @Test
    public void testTopMenu() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login(username, password);
        TopMenuAccountPC menu = new TopMenuAccountPC(driver);

        menu.clickDesktopArrow();

        menu.clickAccountArrow();

        menu.clickPolicyArrow();

        //menu.clickContactArrow();//GW8 only needs to be implemented

        menu.clickSearchArrow();

        menu.clickAdministrationArrow();

    }

    public void hoverOverElement(WebDriver driver, WebElement element) {
        Actions mouse = new Actions(driver);
        mouse.moveToElement(element);
        mouse.build().perform();
    }


    @Test
    public void testGW8PCMenuAdministrationWebElements() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login(username, password);
        TopMenuPC menu = new TopMenuPC(driver);


        //This isn't being used since the elements are being statically accessed.
        //ITopMenuAdministration menuAdministration = TopMenuFactory.getMenuAdministration();


        menu.clickAdministrationArrow();

        TopMenuAdministrationPC topMenuAdministrationPC = new TopMenuAdministrationPC(driver);

        hoverOverElement(driver, topMenuAdministrationPC.Users_and_Security);
        hoverOverElement(driver, topMenuAdministrationPC.Users);
        hoverOverElement(driver, topMenuAdministrationPC.Groups);
        hoverOverElement(driver, topMenuAdministrationPC.Roles);
        hoverOverElement(driver, topMenuAdministrationPC.Regions);
        hoverOverElement(driver, topMenuAdministrationPC.Organizations);
        hoverOverElement(driver, topMenuAdministrationPC.Producer_Codes);
        hoverOverElement(driver, topMenuAdministrationPC.Authority_Profiles);
        hoverOverElement(driver, topMenuAdministrationPC.Attributes);
        hoverOverElement(driver, topMenuAdministrationPC.Affinity_Groups);

        hoverOverElement(driver, topMenuAdministrationPC.Business_Settings);
        hoverOverElement(driver, topMenuAdministrationPC.Activity_Patterns);
        hoverOverElement(driver, topMenuAdministrationPC.Holidays);
        hoverOverElement(driver, topMenuAdministrationPC.Policy_Form_Patterns);
        hoverOverElement(driver, topMenuAdministrationPC.Policy_Holds);

        hoverOverElement(driver, topMenuAdministrationPC.Monitoring);
        hoverOverElement(driver, topMenuAdministrationPC.Messages);
        hoverOverElement(driver, topMenuAdministrationPC.Message_Queues);
        hoverOverElement(driver, topMenuAdministrationPC.Workflows);
        hoverOverElement(driver, topMenuAdministrationPC.Workflow_Statistics);

        hoverOverElement(driver, topMenuAdministrationPC.Utilities);
        hoverOverElement(driver, topMenuAdministrationPC.Import_Data);
        hoverOverElement(driver, topMenuAdministrationPC.Export_Data);
        hoverOverElement(driver, topMenuAdministrationPC.Script_Parameters);
        hoverOverElement(driver, topMenuAdministrationPC.Spreadsheet_Export_Formats);
        hoverOverElement(driver, topMenuAdministrationPC.Data_Change);
    }

    @Test
    public void testGW8PCMenuAccountWebElements() throws Exception {
        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);
        new Login(driver).login(username, password);
        TopMenuPC menu = new TopMenuPC(driver);

        //This isn't being used since the elements are being statically accessed.
        //ITopMenuAccount menuAccount = TopMenuFactory.getMenuAccount();


        menu.clickAccountArrow();

        TopMenuAccountPC topMenuAccountPC = new TopMenuAccountPC(driver);
        hoverOverElement(driver, topMenuAccountPC.New_Account);
        hoverOverElement(driver, topMenuAccountPC.Search_Input);
        hoverOverElement(driver, topMenuAccountPC.Search_Button);
        hoverOverElement(driver, topMenuAccountPC.First);
        hoverOverElement(driver, topMenuAccountPC.Second);
        hoverOverElement(driver, topMenuAccountPC.Third);
        hoverOverElement(driver, topMenuAccountPC.Fourth);
        hoverOverElement(driver, topMenuAccountPC.Fifth);
    }

}
