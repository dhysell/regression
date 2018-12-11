package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderCommercialAutoVehiclesCPP extends BasePage {

    public GenericWorkorderCommercialAutoVehiclesCPP(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillOutCAVehicles(GeneratePolicy policy) {

    }
}
