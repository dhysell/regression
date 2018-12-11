package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class CollectExposureData extends BasePage {

    public CollectExposureData(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
