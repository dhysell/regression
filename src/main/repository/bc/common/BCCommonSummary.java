package repository.bc.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.account.BCAccountMenu;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DelinquencyPlan;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;

public class BCCommonSummary extends BasePage {

    private WebDriver driver;

    public BCCommonSummary(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths
    // -----------------------------------------------
    @FindBy(xpath = "//div[contains(@id,'SummaryScreen:3')]")
    private WebElement table_OpenActivitiesTable;

    @FindBy(xpath = "//div[contains(@id, ':CollectionsWriteoffAmount-inputEl')]")
    public WebElement label_TotalCollectionsWriteOff;

    @FindBy(xpath = "//div[contains(@id, ':PremiumoffAmount-inputEl')]")
    public WebElement label_TotalPremiumWriteOff;

    @FindBy(xpath = "//div[contains(@id, ':negWriteOffamt-inputEl')]")
    public WebElement label_TotalNegativeWriteOff;

    @FindBy(xpath = "//div[contains(@id, ':WriteoffExpense-inputEl')]")
    public WebElement label_TotalWriteOff;

    @FindBy(xpath = "//div[contains(@id,':UnappliedAmount-inputEl') or contains(@id,':UnappliedFunds-inputEl')]")
    public WebElement label_UnappliedFundsAmount;

    @FindBy(xpath = "//div[contains(@id,':PrimaryContact-inputEl') or contains(@id,':PrimaryInsured_Name-inputEl')]")
    public WebElement label_NameOnBillingAddressOrInsuredInfo;

	@FindBy(xpath = "//div[contains(@id,':BillingAddress-inputEl')]")
	public WebElement label_BillingAddress;
	
    @FindBy(xpath = "//div[contains(@id, ':delinquentAmount-inputEl')]")
    public WebElement label_DelinquentAmount;

    @FindBy(xpath = "//div[contains(@id,':AccountName-inputEl')]")
    public WebElement labelOrLink_AccountName;

    @FindBy(xpath = "//div[contains(@id, ':AccountNumber-inputEl')]")
    public WebElement labelOrLink_AccountNumber;

    @FindBy(xpath = "//a[contains(@id,'DelinquencyAlertAlertBar')]")
    private WebElement link_DelinquencyAlertMessage;

    @FindBy(xpath = "//a[contains (@id, 'TroubleTicketAlertAlertBar')]")
    private WebElement link_TroubleTicketAlertMessage;

    @FindBy(xpath = "//div[contains (@id, 'DetailDV:DelinquencyPlan-inputEl')]")
    public WebElement link_DelinquencyPlan;

    Guidewire8Select select_DeliquencyPlan() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':DelinquencyPlan-triggerWrap')]");
    }

    // -------------------------------------------------------
    // Helper Methods for Above Elements
    // -------------------------------------------------------

    public WebElement getOpenActivitiesTable() {
        waitUntilElementIsVisible(table_OpenActivitiesTable);
        return table_OpenActivitiesTable;
    }

    public boolean verifyWriteOffAmountsMatch() {
        double collectionsWriteOff = NumberUtils.getCurrencyValueFromElement(label_TotalCollectionsWriteOff);
        double premiumWriteOff = NumberUtils.getCurrencyValueFromElement(label_TotalPremiumWriteOff);
        double negativeWriteOff = NumberUtils.getCurrencyValueFromElement(label_TotalNegativeWriteOff);
        if (collectionsWriteOff + premiumWriteOff + negativeWriteOff == getTotalWriteOffAmount()) {
            return true;
        } else {
            return false;
        }
    }

    public double getTotalWriteOffAmount() {
        return NumberUtils.getCurrencyValueFromElement(label_TotalWriteOff);
    }

    public double getTotalNegativeWriteOffAmount() {
        return NumberUtils.getCurrencyValueFromElement(label_TotalNegativeWriteOff);
    }

    public String getDelinquencyAlertMessage() {
        return link_DelinquencyAlertMessage.getText();
    }

    public void clickDelinquencyAlertMessage() {
        clickWhenVisible(link_DelinquencyAlertMessage);
    }

    public String getTroubleTicketAlertMessage() {
        return link_TroubleTicketAlertMessage.getText();
    }

    public boolean waitUntilTroubleTicketMessageArrives(int secondsToWait) {
        boolean troubleticketArrived;
        try {
            troubleticketArrived = link_TroubleTicketAlertMessage.isDisplayed();
        } catch (Exception e) {
            troubleticketArrived = false;
        }
        long secondsRemaining = secondsToWait;
        int delayInterval = 5;
        while (!troubleticketArrived && (secondsRemaining > 0)) {
            sleep(delayInterval); //Used to wait for the amount of time specified in the delayInterval variable before attempting to check for trouble tickets again.
            secondsRemaining -= delayInterval;
            repository.bc.account.BCAccountMenu acctMenuStuff = new BCAccountMenu(getDriver());
            acctMenuStuff.clickBCMenuCharges();
            acctMenuStuff.clickBCMenuSummary();
            try {
                troubleticketArrived = link_TroubleTicketAlertMessage.isDisplayed();
            } catch (Exception e) {
                troubleticketArrived = false;
            }
        }
        return troubleticketArrived;
    }

    public double getDefaultUnappliedFundsAmount() {
        return NumberUtils.getCurrencyValueFromElement(label_UnappliedFundsAmount);
    }

    public DelinquencyPlan getDelinquencyPlan() {
        return DelinquencyPlan.valueOf(link_DelinquencyPlan.getText());
    }

    public boolean checkIfDelinquencyPlanLinkIsActive() {
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(link_DelinquencyPlan);
    }

    public String getAccountHolderName() {
        return labelOrLink_AccountName.getText();
    }

    public boolean checkIfAccountHolderNameLinkIsActive() {
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(labelOrLink_AccountName);
    }

    public void clickAccountHolderName() {
        labelOrLink_AccountName.click();
    }

    public String getNameOnBillingAddressOrInsuredInfo() {
        return label_NameOnBillingAddressOrInsuredInfo.getText();
    }

	public String getBillingAddress() {
		return label_BillingAddress.getText();
	}
	
    public String getAccountNumber() {
        return labelOrLink_AccountNumber.getText();
    }

    public boolean checkIfAccountNumberLinkIsActive() {
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(labelOrLink_AccountNumber);
    }

    public void clickAccountNumberLink() {       
        clickWhenVisible(labelOrLink_AccountNumber, 15);
    }

    public void setDeliquencyPlan(DelinquencyPlan plan) {
        Guidewire8Select mySelect = select_DeliquencyPlan();
        mySelect.selectByVisibleText(plan.getValue());
    }

    public double getDelinquentAmount() {
        return NumberUtils.getCurrencyValueFromElement(label_DelinquentAmount);
    }
}
