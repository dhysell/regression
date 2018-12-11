package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class RecoveryDetails extends BasePage {

    WebDriver driver;

    public RecoveryDetails(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void voidRecovery() {
        clickVoidButton();
        clickSecondaryVoidButton();
        confimVoid();
    }

    @FindBy(css = "a[id*='ClaimFinancialsTransactionsDetail:ClaimFinancialsTransactionsDetailScreen:" +
            "TransactionDetailToolbarButtonSet:TransactionDetailToolbarButtons_VoidButton']")
    private WebElement buttonVoid;

    private void clickVoidButton() {
        waitUntilElementIsClickable(buttonVoid);
        buttonVoid.click();
    }

    @FindBy(css = "a[id*='VoidRecovery:VoidRecoveryScreen:VoidRecovery_VoidButton']")
    private WebElement buttonSecondaryVoid;

    private void clickSecondaryVoidButton() {
        waitUntilElementIsClickable(buttonSecondaryVoid);
        buttonSecondaryVoid.click();
    }

    @FindBy(css = "div[id*='messagebox-'] a")
    private WebElement okayPopup;

    private void confimVoid() {
        waitUntilElementIsClickable(okayPopup);
        okayPopup.click();
    }

    @FindBy(css = "div[id*=':TransactionTrackingInputSet:Status-inputEl']")
    private WebElement transactionStatus;

    public boolean isSuccessfullVoid() {
        waitUntilElementIsVisible(transactionStatus);
        return transactionStatus.getText().equalsIgnoreCase("Pending Void");
    }
}
