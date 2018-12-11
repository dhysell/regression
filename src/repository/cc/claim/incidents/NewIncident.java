package repository.cc.claim.incidents;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class NewIncident extends BasePage {

    public NewIncident(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
