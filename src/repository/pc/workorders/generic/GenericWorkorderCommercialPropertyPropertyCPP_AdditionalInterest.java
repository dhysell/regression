package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class GenericWorkorderCommercialPropertyPropertyCPP_AdditionalInterest extends BasePage {

    public GenericWorkorderCommercialPropertyPropertyCPP_AdditionalInterest(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }
}
