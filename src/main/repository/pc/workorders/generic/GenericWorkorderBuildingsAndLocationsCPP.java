package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderBuildingsAndLocationsCPP extends BasePage {

    public GenericWorkorderBuildingsAndLocationsCPP(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillOutBuildingsAndLocationsInlandMarine(GeneratePolicy policy) {

    }


    public void fillOutBuildingsAndLocationsCommercialProperty(GeneratePolicy policy) {

    }

}
