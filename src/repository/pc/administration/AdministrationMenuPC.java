package repository.pc.administration;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class AdministrationMenuPC extends BasePage {

    public AdministrationMenuPC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring:Monitoring_MessagingDestinationControlList']")
    private WebElement MessageQueues;


    public void clickMessageQueues() {
        clickWhenClickable(MessageQueues);
    }

}
