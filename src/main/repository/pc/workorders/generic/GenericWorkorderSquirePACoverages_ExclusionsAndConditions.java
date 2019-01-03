package repository.pc.workorders.generic;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderSquirePACoverages_ExclusionsAndConditions extends BasePage {

    public GenericWorkorderSquirePACoverages_ExclusionsAndConditions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }


    public void fillOutExclusionsAndConditions_QQ(GeneratePolicy policy) {

    }

    public void fillOutExclusionsAndConditions_FA(GeneratePolicy policy) {

    }

    public void fillOutExclusionsAndConditions(GeneratePolicy policy) {

    }

    public void fillOutExclusions_QQ(GeneratePolicy policy) {

    }

    public void fillOutConditions_QQ(GeneratePolicy policy) {

    }


    public void fillOutExclusions(GeneratePolicy policy) {

    }

    public void fillOutConditions(GeneratePolicy policy) {

    }


}
