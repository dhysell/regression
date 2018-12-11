package repository.pc.administration;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

/**
 * @author jlarsen
 * This class is to hold the Giant Switch statement of how to test each PC permission.
 */
public class Permissions extends BasePage {

    public Permissions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
