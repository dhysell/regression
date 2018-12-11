package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderSquirePACoverages_Coverages extends BasePage {

    public GenericWorkorderSquirePACoverages_Coverages(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public void fillOutCoverages_QQ(GeneratePolicy policy) {

    }

    public void fillOutCoverages_FA(GeneratePolicy policy) {

    }

    public void fillOutCoverages(GeneratePolicy policy) {

    }


}
