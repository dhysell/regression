package repository.pc.account;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class AccountClaimsPC extends BasePage {
	
	private WebDriver driver;

    public AccountClaimsPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[contains(@id, ':DateRangeForClaimSearchRangeChoice_Choice-inputEl')]")
    public WebElement radio_Since;

    @FindBy(xpath = "//input[contains(@id, ':DateRangeForClaimSearchDirectChoice_Choice-inputEl')]")
    public WebElement radio_From;

    public Guidewire8Select select_SinceDays() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Claims:AccountFile_ClaimScreen:DateRangeForClaimSearch:DateRangeForClaimSearchRangeValue-triggerWrap']");
    }

    @FindBy(xpath = "//input[contains(@id, ':DateRangeForClaimSearchStartDate-inputEl')]")
    public WebElement editbox_DateFrom;

    @FindBy(xpath = "//input[contains(@id, ':DateRangeForClaimSearchEndDate-inputEl')]")
    public WebElement editbox_DateTo;

    public Guidewire8Select select_PolicyPeriod() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Claims:AccountFile_ClaimScreen:AccountClaimsLV:policyPeriodFilter-triggerWrap']");
    }

    public Guidewire8Select select_Product() {
        return new Guidewire8Select(driver, "//*[@id='AccountFile_Claims:AccountFile_ClaimScreen:AccountClaimsLV:productCodeFilter-triggerWrap']");
    }


    public void clickRadioSince() {
        waitUntilElementIsVisible(radio_Since);
        radio_Since.click();
    }


    public void clickRadioFrom() {
        waitUntilElementIsVisible(radio_From);
        radio_From.click();
    }


    public void selectDaysSince(String timeFrame) {
        select_SinceDays().selectByVisibleText(timeFrame);
    }


    public void setDateFrom(Date mmddyyyy) {
        waitUntilElementIsVisible(editbox_DateFrom);
        editbox_DateFrom.click();
        editbox_DateFrom.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateFrom.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", mmddyyyy));
    }


    public void setDateTo(Date mmddyyyy) {
        waitUntilElementIsVisible(editbox_DateTo);
        editbox_DateTo.click();
        editbox_DateTo.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_DateTo.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", mmddyyyy));
    }


    public void clickSearch() {
        super.clickSearch();
    }


    public void clickReset() {
        super.clickReset();
    }


    public void setPolicyPeriod(String period) {
        select_PolicyPeriod().selectByVisibleText(period);
    }


    public void setProduct(String product) {
        select_Product().selectByVisibleText(product);
    }

}
