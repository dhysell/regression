package repository.cc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class AdminSideMenuCC extends BasePage {


    public AdminSideMenuCC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ELEMENTS
    // =============================================================================

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring']")
    public WebElement link_Monitoring;

    @FindBy(xpath = "//td[@id='Admin:MenuLinks:Admin_Monitoring:Monitoring_MessagingDestinationControlList']")
    public WebElement link_MessageQueues;

    // HELPER METHODS
    // =======================================================================

    public void clickMonitoringLink() {
        clickWhenClickable(link_Monitoring);
    }

    public void clickMessageQueues() {
        clickWhenClickable(link_MessageQueues);
    }

}
