package repository.bc.wizards;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class ManualActivityWizard extends BasePage{
	private WebDriver driver;

    public ManualActivityWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /////////////////////
    // Recorded Elements//
    /////////////////////

    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV_TargetDate-inputEl')]")
    public static WebElement editbox_DueDate;
    
    @FindBy(xpath = "//input[contains(@id, ':ActivityDetailDV_EscalationDate-inputEl')]")
    public static WebElement editbox_EscalationDate;

    @FindBy(xpath = "//input[contains(@id, ':EdgeEntitiesInputSet:account-inputEl')]")
    public static WebElement editbox_Account;
    
    @FindBy(xpath = "//a[contains(@id, ':account:acctPicker')]")
    public static WebElement link_AccountPicker;
    
    @FindBy(xpath = "//a[contains(@id, ':policyPeriod:ppPicker')]")
    public static WebElement link_PolicyPeriodPicker;
  
    ///////////////////////////////
    // Helper Methods for Elements//
    ///////////////////////////////

  


	public void setDueDate(Date duedate) {
		editbox_DueDate.sendKeys(Keys.CONTROL + "a");
		editbox_DueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", duedate));
		
	}

    public void setEscalationDate(Date escalationDate) {
        editbox_EscalationDate.sendKeys(Keys.CONTROL + "a");
        editbox_EscalationDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", escalationDate));
        
    }

    public void setAccountNumber(String accountNumber) {
    	waitUntilElementIsVisible(editbox_Account);
    	editbox_Account.sendKeys(Keys.CONTROL + "a");
        editbox_Account.sendKeys(accountNumber);
        
    }
    
    public void clickAccountPicker() {
    	waitUntilElementIsVisible(link_AccountPicker);
        
    }

    public void clickPolicyPeriodPicker() {
    	waitUntilElementIsVisible(link_PolicyPeriodPicker);
    }

    public void setAccountNumberFromPicker(String accountNumber) {
        clickAccountPicker();
        repository.bc.search.BCSearchAccounts search = new BCSearchAccounts(getDriver());
        search.setBCSearchAccountsAccountNumber(accountNumber);
        super.clickSearch();
        search.clickSelectAccountsButton();
    }

    public void clickCancel() {
        super.clickCancel();
    }

    public void clickUpdate() {
        super.clickUpdate();
    }
}
