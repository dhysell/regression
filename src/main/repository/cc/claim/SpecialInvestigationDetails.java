package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class SpecialInvestigationDetails extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public SpecialInvestigationDetails(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(getDriver());
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//a[@id='SIDetails:SIDetailsScreen:Edit']")
    private WebElement buttonEdit;

    private void clickEditButton() {
        clickWhenClickable(buttonEdit);
    }

    @FindBy(xpath = "//a[@id='SIDetails:SIDetailsScreen:Update']")
    private WebElement buttonUpdate;

    private void clickUpdateButton() {
        clickWhenClickable(buttonUpdate);
    }

    public void specialInvestiagionDetails() {

        WorkplanCC workplan = new WorkplanCC(this.driver);

        clickEditButton();

        List<WebElement> noRadio = finds(By.xpath("//label[contains(text(),'No')]/../input"));
        noRadio.get(0).click();

        for (WebElement radio : noRadio) {
            try {
                waitUtils.waitUntilElementIsClickable(radio);
                radio.click();
            } catch (Exception e) {
            	//This needs to do something or have a comment as to why it's not doing anything.
            }
        }


        try {
            clickUpdateButton();
        } catch (Exception e) {
            clickUpdateButton();
        }

        workplan.completeActivity("Complete Fraud Questionnaire");

    }
}
