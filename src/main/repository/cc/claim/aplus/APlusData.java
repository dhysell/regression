package repository.cc.claim.aplus;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class APlusData extends BasePage {

    public APlusData(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
