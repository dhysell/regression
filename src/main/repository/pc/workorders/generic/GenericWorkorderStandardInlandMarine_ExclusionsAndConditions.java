package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;
import repository.pc.sidemenu.SideMenuPC;

public class GenericWorkorderStandardInlandMarine_ExclusionsAndConditions extends BasePage {

    private WebDriver driver;

    public GenericWorkorderStandardInlandMarine_ExclusionsAndConditions(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }


    public void fillOutExclusionsAndConditions(GeneratePolicy policy) throws Exception {
        new SideMenuPC(driver).clickSideMenuIMExclusionsAndConditions();

    }


}
