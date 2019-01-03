package repository.cc.claim;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.WaitUtils;

public class CropHailCalculator extends BasePage {

    private WaitUtils waitUtils;

    public CropHailCalculator(WebDriver driver) {
        super(driver);
        this.waitUtils = new WaitUtils(getDriver());
        PageFactory.initElements(driver, this);
    }

    // Crop Hail Elements.

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:RealTotalFieldAcresExt-inputEl']")
    public WebElement realTotalFieldAcres;

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:FieldNameRefExt-inputEl']")
    public WebElement fieldName;

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:BreakOutAcresExt-inputEl']")
    public WebElement breakOutAcres;

    @FindBy(xpath = "//input[@id='EditCropCalculatorPopup:NewCropIncidentScreen:CheckIfStubbleExt_true-inputEl']")
    public WebElement radio_YesStubble;

    @FindBy(xpath = "//input[@id='EditCropCalculatorPopup:NewCropIncidentScreen:CheckIfStubbleExt_false-inputEl']")
    public WebElement radio_NoStubble;

    @FindBy(xpath = "//input[@id='EditCropCalculatorPopup:NewCropIncidentScreen:InsurancePerAcreExt-inputEl']")
    public WebElement insurancePerAcre;

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:PercentNetExt-inputEl']")
    public WebElement percentGross;

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:PercentTotalExt-inputEl']")
    public WebElement percentTotal;

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:Comments-inputEl']")
    public WebElement paymentAmount;

    @FindBy(xpath = "//input[@id='NewCropCalculatorPopup:NewCropIncidentScreen:PaymentAmountExt-inputEl']")
    public WebElement comments;

    @FindBy(xpath = "//a[@id='NewCropCalculatorPopup:NewCropIncidentScreen:Update']")
    public WebElement button_Update;

    @FindBy(xpath = "//a[@id='EditCropIncidentPopup:EditCropIncidentScreen:Update']")
    public WebElement button_OkCrop;

    @FindBy(xpath = "//a[@id='EditCropCalculatorPopup:NewCropIncidentScreen:Cancel']")
    public WebElement button_Cancel;

    private String paymentAmt;

    // Helpers

    public void inputBreakOutAcres() {
        int totalAcres;

        if (realTotalFieldAcres.getAttribute("value").contains(".")) {
            totalAcres = (int) Math.floor(Double.parseDouble(realTotalFieldAcres.getAttribute("value")));
        } else {
            totalAcres = Integer.parseInt(realTotalFieldAcres.getAttribute("value"));
        }
        int randomNum = NumberUtils.generateRandomNumberInt(5, totalAcres);
        breakOutAcres.sendKeys(Integer.toString(randomNum));
        clickProductLogo();
    }

    public void inputSpecificPercentGross(String gross) {
        percentGross.sendKeys(gross);
    }

    public void inputRandomPercentGross() {

        find(By.xpath("//span[@id='NewCropCalculatorPopup:NewCropIncidentScreen:ttlBar']")).click();
        String randomPercent = Integer.toString(NumberUtils.generateRandomNumberInt(0, 100));
        waitUtils.waitUntilElementIsClickable(percentGross, 5000);
        percentGross.sendKeys(randomPercent);

    }

    public void inputPercentTotal(String total) {
        percentTotal.sendKeys(total + Keys.TAB);
    }

    public String getPaymentAmount() {
        this.paymentAmt = paymentAmount.getAttribute("value");
        return paymentAmt;
    }

    public void clickUpdateButton() {
        clickWhenClickable(button_Update);
    }

    public void clickIncidentOk() {
        waitUntilElementIsClickable(By.cssSelector("a[id$='EditCropIncidentScreen:Update']"));
        clickWhenClickable(button_OkCrop);
    }

}
