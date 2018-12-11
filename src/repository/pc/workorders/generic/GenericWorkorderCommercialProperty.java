package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class GenericWorkorderCommercialProperty extends BasePage {

    public GenericWorkorderCommercialProperty(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
