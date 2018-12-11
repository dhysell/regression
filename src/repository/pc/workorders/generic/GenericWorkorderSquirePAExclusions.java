package repository.pc.workorders.generic;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class GenericWorkorderSquirePAExclusions extends GenericWorkorder {

    public GenericWorkorderSquirePAExclusions(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

}
