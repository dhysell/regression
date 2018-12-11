package repository.cc.desktop;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8RadioButton;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

import java.util.List;

public class ClaimSurveyEntry extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public ClaimSurveyEntry(WebDriver webDriver) {
        super(webDriver);
        this.driver = webDriver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "input[id*='ClaimSurveyPolicyNumber-inputEl']")
    private WebElement inputPolicyNumber;

    private void setPolicyNumber(String policyNumber) {
        waitUtils.waitUntilElementIsVisible(inputPolicyNumber);
        inputPolicyNumber.sendKeys(policyNumber);
    }

    @FindBy(css = "input[id*='ClaimSurveyLossDate-inputEl']")
    private WebElement inputDateOfLoss;

    private void setDateOfLoss(String dateOfLoss) {
        waitUtils.waitUntilElementIsVisible(inputDateOfLoss);
        inputDateOfLoss.clear();
        waitUtils.waitUntilElementIsClickable(inputDateOfLoss);
        inputDateOfLoss.sendKeys(dateOfLoss);
    }

    private Guidewire8Select selectLossType() {
        return new Guidewire8Select(driver, "//table[contains(@id,'ClaimSurveyLossType-triggerWrap')]");
    }

    private void setLossType(String selection) {
        selectLossType().selectByVisibleText(selection);
    }

    private Guidewire8Select selectClaimNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id,'DesktopClaimSurveyEntry:ClaimNumber-triggerWrap')]");
    }

    private void setClaimNumber(String selection) {
        selectClaimNumber().selectByVisibleText(selection);
    }

    @FindBy(css = "a[id*='DesktopClaimSurveyEntry:Search']")
    private WebElement buttonSearch;

    private void clickSearchButton() {
        waitUtils.waitUntilElementIsClickable(buttonSearch,5);
        buttonSearch.click();
    }

    @FindBy(css = "a[id*='DesktopClaimSurveyEntry:ClaimNumber:Retrieve']")
    private WebElement linkLoadQuestions;

    private void clickLoadQuestions() {
        waitUtils.waitUntilElementIsVisible(linkLoadQuestions);
        linkLoadQuestions.click();
    }

    @FindBy(css = "div[id*='DesktopClaimSurveyEntry:2-body']")
    private WebElement rows;

    Guidewire8RadioButton radio_Excluded() {
        return new Guidewire8RadioButton(driver,"//table[contains(@id, ':BADriversDV:excluded')]");
    }

    @FindBy(css = "div[class*='message']")
    private WebElement elementMessage;

    public ClaimSurveyListing populateClaimSurvey(String policyNumber, String dateOfLoss, String lossType, String claimNumber) {
        setPolicyNumber(policyNumber);
        setDateOfLoss(dateOfLoss);
        setLossType(lossType);
        clickSearchButton();

        if (checkIfElementExists("//input[contains(@id,'DesktopClaimSurveyEntry:ClaimNumber-inputEl')]", 2)) {
            setClaimNumber(claimNumber);
            clickLoadQuestions();
        }

        if(driver.findElements(By.cssSelector("div[class*='message']")).size() != 0){
            if (elementMessage.getText()
                    .equalsIgnoreCase("Claim survey already added...please view on the listing page!")) {
                System.out.println("Claims survey already successfully added.");
            } else {
                Assert.fail(elementMessage.getText());
            }
        } else {
            waitUtils.waitUntilElementIsVisible(By.xpath("//table[contains(@class,'gw-radio-group-cell')]"));

            List<WebElement> radioGroups = finds(By.xpath("//table[contains(@class,'gw-radio-group-cell')]"));

            radioGroups.forEach((radioGroup) -> {
                List<WebElement> radios = radioGroup.findElements(By.cssSelector("input"));
                radios.get(NumberUtils.generateRandomNumberInt(0, radios.size()-1)).click();
            });

            clickUpdate();
        }
        return new ClaimSurveyListing(driver);
    }
}
