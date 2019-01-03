package repository.pc.policy;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;

public class PolicyBilling extends BasePage {

	WebDriver driver;
	
    public PolicyBilling(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    @FindBy(xpath = "//a[@id='PolicyFile_Billing:Policy_BillingScreen:ViewInBC']")
    public WebElement button_ViewInBillingCenter;


    public String clickViewInBillingCenterButton() {
        clickWhenClickable(button_ViewInBillingCenter);

        String mainPCWindow = driver.getWindowHandle();
        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
            if (driver.getTitle().contains("Guidewire BillingCenter (PC User)")) {
                systemOut("Switching to Billing Center Window");
                break;
            }
        }
        if (!checkIfElementExists("//img[contains(@class,'product-logo')]", 5000)) {
            Assert.fail("The Billing Center window failed to load completely within 5 seconds.");
        }
        return mainPCWindow;
    }

}
