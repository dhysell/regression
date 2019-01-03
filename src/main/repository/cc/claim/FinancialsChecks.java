package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

public class FinancialsChecks extends BasePage {

    private WaitUtils waitUtils;

    public FinancialsChecks(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "div[id$='ChecksLV']")
    private WebElement table_Checks;

//    private String cssSelector = "div[id$='ChecksLV']";

    public boolean pickFirstCheckVerifyNoErrorMessages() {

        boolean noErrors = true;
        clickWhenClickable(find(By.cssSelector("a[id$='GrossAmount']")));
        noErrors = find(By.cssSelector("span[id$='ttlBar']")).getText().equalsIgnoreCase("check details")
                && finds(By.cssSelector("div.message")).size() == 0;

        return noErrors;

    }

    public repository.cc.claim.CheckDetails clickCheckByNumber(String checkNumber) {

        String xpathString = "//a[contains(text(),'"+checkNumber+"')]";
        waitUtils.waitUntilElementIsClickable(find(By.xpath(xpathString)));
        find(By.xpath(xpathString)).click();

        return new CheckDetails(getDriver());
}

}
