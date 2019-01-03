package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.TransferReason;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

public class NewTransferWizard extends BasePage {

    private TableUtils tableUtils;
    private WebDriver driver;

    public NewTransferWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.tableUtils = new TableUtils(driver);
        PageFactory.initElements(driver, this);
    }
    // -----------------------------------------------
    // "Recorded Elements" and their XPaths
    // -----------------------------------------------

    @FindBy(xpath = "//input[contains(@id,':TransferDetailsDV:Target_option1-inputEl')]")
    public WebElement radio_Target;

    @FindBy(xpath = "//div[contains(@id, 'Screen:TransferDetailsDV:AccountInfo:SourcePolicyNumber-inputEl')]")
    public WebElement label_NewTransferWizardPolicyNumber;

    @FindBy(xpath = "//div[contains(@id, 'Screen:TransferDetailsDV:AccountInfo:SourceLoanNumber-inputEl')]")
    public WebElement label_NewTransferWizardLoanNumber;

    @FindBy(xpath = "//div[contains(@id, 'Screen:TransferDetailsDV:AccountInfo:bucketUnappliedAmount-inputEl') or contains(@id, 'Screen:TransferDetailsDV:AccountInfo:UnappliedAmount-inputEl')]")
    public WebElement label_NewTransferWizardAvailableTransferAmount;

    // this table displays after clicking Target radio button
    @FindBy(xpath = "//div[@id='LienholderPaymentTransferWizard:LienholderPaymentTransferDetailsScreen:TransferDetailsLV' or @id='AccountNewTransferWizard:TransferDetailsScreen:TransferDetailsDV:TransferDetailsLV']")
    public WebElement table_TransferDetails;

    public Guidewire8Select select_UnappliedFund() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountInfo:UnappliedFunds-triggerWrap')]");
    }

    public Guidewire8Select select_PolicyNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountInfo:SourcePolicyNumber-triggerWrap')]");
    }

    public Guidewire8Select select_LoanNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':AccountInfo:SourceLoanNumber-triggerWrap')]");
    }

    @FindBy(xpath = "//a[@id='TransferReversePopup:ToolbarButton']")
    public WebElement button_ReverseTransfer;

    // -------------------------------------------------------
    // Helper Methods for Above Elements
    // -------------------------------------------------------

    public void clickTarget() {
        radio_Target.click();
    }

    public WebElement getTransferDetailsTable() {
        return table_TransferDetails;
    }

    public String getNewTransferWizardPolicyNumber() {
        waitUntilElementIsVisible(label_NewTransferWizardPolicyNumber);
        String policyNumber = label_NewTransferWizardPolicyNumber.getText();
        return policyNumber;
    }

    public String getNewTransferWizardLoanNumber() {
        waitUntilElementIsVisible(label_NewTransferWizardLoanNumber);
        String loanNumber = label_NewTransferWizardLoanNumber.getText();
        return loanNumber;
    }

    public double getNewTransferWizardAvailableTransferAmount() {
        waitUntilElementIsVisible(label_NewTransferWizardAvailableTransferAmount);
        double availableTransferAmount = NumberUtils.getCurrencyValueFromElement(label_NewTransferWizardAvailableTransferAmount.getText());
        return availableTransferAmount;
    }

    public void setTransferTableTargetAccount(int tableRowNumber, String targetAccount) {
        String targetAccountGridColumnID = tableUtils.getGridColumnFromTable(table_TransferDetails, "Target Account");
        WebElement editBox_NewTransferWizardTargetAccountNumber = table_TransferDetails.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + targetAccountGridColumnID + "')]/div"));
        clickWhenClickable(editBox_NewTransferWizardTargetAccountNumber);
        if (targetAccount.equalsIgnoreCase("randomLienholder") || targetAccount.equalsIgnoreCase("randomInsured")) {
            WebElement button_NewTransferWizardTargetAccountPicker = table_TransferDetails.findElement(By.xpath("//div[contains(@id,'TransferDetailsScreen:TransferDetailsDV:TransferDetailsLV:" + (tableRowNumber - 1) + ":TargetAccount:SelectTargetAccount')]"));
            clickWhenClickable(button_NewTransferWizardTargetAccountPicker);
            repository.bc.search.BCSearchAccounts accountSearch = new BCSearchAccounts(getDriver());
            if (targetAccount.equalsIgnoreCase("randomLienholder")) {
                accountSearch.pickRandomAccount("98");
            } else if (targetAccount.equalsIgnoreCase("randomInsured")) {
                accountSearch.pickRandomAccount("2");
            }
        } else {
            tableUtils.setValueForCellInsideTable(table_TransferDetails, "TargetAccount", targetAccount);
        }
    }

    public void setTransferTableAmount(int tableRowNumber, double transferAmount) {
        String amountGridColumnID = tableUtils.getGridColumnFromTable(table_TransferDetails, "Amount");
        WebElement editBox_NewTransferWizardAmount = table_TransferDetails.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + amountGridColumnID + "')]/div"));
        clickWhenClickable(editBox_NewTransferWizardAmount);
        tableUtils.setValueForCellInsideTable(table_TransferDetails, "Amount", String.valueOf(transferAmount));
    }

    public void selectTransferTableReason(int tableRowNumber, TransferReason transferReason) {
        tableUtils.selectValueForSelectInTable(table_TransferDetails, tableRowNumber, "Reason", transferReason.getValue());
    }

    public void selectTransferTableUnappliedFund(int tableRowNumber, String unappliedfund) {
        tableUtils.selectValueForSelectInTable(table_TransferDetails, tableRowNumber, "Unapplied Fund", unappliedfund);
    }

    public void selectTransferTableTargetPolicyNumber(int tableRowNumber, String targetPolicyNumber) {
        if (targetPolicyNumber.equals("random")) {
            tableUtils.selectRandomValueForSelectInTable(table_TransferDetails, tableRowNumber, "Target Policy #");
        } else {
            tableUtils.selectValueForSelectInTable(table_TransferDetails, tableRowNumber, "Target Policy #", targetPolicyNumber);
        }
    }

    public void selectTransferTableTargetLoanNumber(int tableRowNumber, String targetLoanNumber) {
        if (targetLoanNumber.equals("random")) {
            tableUtils.selectRandomValueForSelectInTable(table_TransferDetails, tableRowNumber, "Target Loan #");
        } else {
            tableUtils.selectValueForSelectInTable(table_TransferDetails, tableRowNumber, "Target Loan #", targetLoanNumber);
        }
    }

    public void createNewTransfer(int rowNum, String targetAccount, String unappliedFund, Double transferAmount, String targetPolicyNumber, String targetLoanNumber, TransferReason transferReason) {
        if (targetAccount.equals("random")) {
            Assert.fail("The Target Account option chosen was \"random\". However, you must be more specific. Please choose either \"randomLienholder\" or \"randomInsured\".");
        }
        if ((targetAccount.equals("randomLienholder") || targetAccount.startsWith("98")) && targetPolicyNumber == null) {
            Assert.fail("The Target Account was a lienholder account, but the Target Policy Number was null. If you choose a lienholder for a transfer, a target Policy Number must be selected.");
        }
		clickAdd();
		
        setTransferTableTargetAccount(rowNum, targetAccount);
        
        if (unappliedFund != null) {
            selectTransferTableUnappliedFund(rowNum, unappliedFund);
            
        }
        if (transferAmount != null) {
            setTransferTableAmount(rowNum, transferAmount);
        } else {
            setTransferTableAmount(rowNum, getNewTransferWizardAvailableTransferAmount());
        }
        
        if (targetPolicyNumber != null) {
            selectTransferTableTargetPolicyNumber(rowNum, targetPolicyNumber);
            
        }
        if (targetLoanNumber != null) {
            selectTransferTableTargetLoanNumber(rowNum, targetLoanNumber);
            
        }
        selectTransferTableReason(rowNum, transferReason);
        clickNext();
        
        clickFinish();
        
    }

    public void createNewTransfer(String targetAccount, String unappliedFund, Double transferAmount, String targetPolicyNumber, String targetLoanNumber, TransferReason transferReason) {
        clickAdd();
        
        int newestRow = tableUtils.getNextAvailableLineInTable(table_TransferDetails);
        if (targetAccount.equals("random")) {
            Assert.fail("The Target Account option chosen was \"random\". However, you must be more specific. Please choose either \"randomLienholder\" or \"randomInsured\".");
        }
        if ((targetAccount.equals("randomLienholder") || targetAccount.startsWith("98")) && targetPolicyNumber == null) {
            Assert.fail("The Target Account was a lienholder account, but the Target Policy Number was null. If you choose a lienholder for a transfer, a target Policy Number must be selected.");
        }
        setTransferTableTargetAccount(newestRow, targetAccount);
        
        if (unappliedFund != null) {
            selectTransferTableUnappliedFund(newestRow, unappliedFund);
            
        }
        if (transferAmount != null) {
            setTransferTableAmount(newestRow, transferAmount);
        } else {
            setTransferTableAmount(newestRow, getNewTransferWizardAvailableTransferAmount());
        }
        
        if (targetPolicyNumber != null) {
            selectTransferTableTargetPolicyNumber(newestRow, targetPolicyNumber);
            
        }
        if (targetLoanNumber != null) {
            selectTransferTableTargetLoanNumber(newestRow, targetLoanNumber);
            
        }
        selectTransferTableReason(newestRow, transferReason);
        clickNext();
        
        clickFinish();
        
    }

    public void selectUnappliedFund(String policyNumber) {
        select_UnappliedFund().selectByVisibleTextPartial(policyNumber);
    }

    public void selectPolicyNumber(String policyNumber) {
        select_PolicyNumber().selectByVisibleTextPartial(policyNumber);
        
    }

    public void selectLoanNumber(String loanNumber) {
        select_LoanNumber().selectByVisibleTextPartial(loanNumber);
        
    }

    public void clickReverseTransferButton() {
        clickWhenVisible(button_ReverseTransfer);
    }
}
