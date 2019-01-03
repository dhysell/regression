package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class Snapshot {
    public Snapshot(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    // Elements
    @FindBy(xpath = "//div[contains(@id, ':ClaimStatusInputSet:CreateUserPhone-inputEl')]")
    public WebElement div_CreatorUserPhone;

    // Helpers
    public String getCreatorPhoneNumber() {
        return div_CreatorUserPhone.getText();
    }

}
