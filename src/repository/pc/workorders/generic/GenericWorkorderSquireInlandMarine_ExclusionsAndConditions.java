package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;


public class GenericWorkorderSquireInlandMarine_ExclusionsAndConditions extends BasePage {

    public GenericWorkorderSquireInlandMarine_ExclusionsAndConditions(WebDriver driver) {
        super(driver);

        PageFactory.initElements(driver, this);
    }


}
