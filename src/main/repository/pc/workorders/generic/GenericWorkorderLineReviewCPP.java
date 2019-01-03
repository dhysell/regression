package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.generate.GeneratePolicy;

public class GenericWorkorderLineReviewCPP extends BasePage {

    public GenericWorkorderLineReviewCPP(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    public GeneratePolicy getGLLineReviewInfo(GeneratePolicy policy) {


        return policy;
    }


    public void getCALineReviewInfo(GeneratePolicy policy) {

    }


    public void getCPLineReviewInfo(GeneratePolicy policy) {

    }


    public void getIMLineReviewInfo(GeneratePolicy policy) {

    }


}
