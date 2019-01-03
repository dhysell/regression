package repository.cc.claim.policypages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class PolicyAggregateLimits extends BasePage {

    public PolicyAggregateLimits(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
