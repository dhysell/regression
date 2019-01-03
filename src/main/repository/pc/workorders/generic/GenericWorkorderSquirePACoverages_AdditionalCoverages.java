package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderSquirePACoverages_AdditionalCoverages extends BasePage {

    public GenericWorkorderSquirePACoverages_AdditionalCoverages(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillOutAdditionalCoverages_QQ(GeneratePolicy policy) {

    }

    public void fillOutAdditionalCoverages_FA(GeneratePolicy policy) {

    }

    public void fillOutAdditionalCoverages(GeneratePolicy policy) {

    }


}
