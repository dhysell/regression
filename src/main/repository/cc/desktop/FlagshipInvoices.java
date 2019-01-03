package repository.cc.desktop;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

public class FlagshipInvoices extends BasePage {

    WaitUtils waitUtils;
    WebDriver driver;

    public FlagshipInvoices(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    
}
