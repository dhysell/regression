package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;
import java.util.HashMap;
//for Desktop->Actions->New Transaction -> Charge Reversal and Account -> Actions ->New Transaction -> Charge Reversal
public class ChargeReversalWizard extends BasePage{
	private WebDriver driver;
	private TableUtils tableUtils;

    public ChargeReversalWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    // -----------------------------------------------
    // "Recorded Elements" and their XPaths
    // -----------------------------------------------

    @FindBy(xpath = "//input[@label='Minimum Amount' or contains(@id, ':MinAmountCriterion-inputEl')]")
	private WebElement editBox_BCChargeReversalMinimumAmount;

	@FindBy(xpath = "//input[@label='Maximum Amount' or contains(@id, ':MaxAmountCriterion-inputEl')]")
	private WebElement editBox_BCChargeReversalMaximumAmount;

	@FindBy(xpath = "//input[@label='Earliest Date' or contains(@id, ':EarliestDateCriterion-inputEl')]")
	private WebElement editBox_BCChargeReversalEarliestDate;

	@FindBy(xpath = "//input[@label='Latest Date' or contains(@id, ':LatestDateCriterion-inputEl')]")
	private WebElement editBox_BCChargeReversalLatestDate;
	
    @FindBy(xpath = "//div[@id='NewChargeReversalWizard:NewChargeReversalChargeSearchScreen:NewChargeReversalChargesLV']/div/table[starts-with(@id,'gridview-')]")
    public WebElement table_DesktopActionsChargeReversal;

    @FindBy(xpath = "//a[@id='NewChargeReversalWizard:NewChargeReversalChargeSearchScreen:ChargeSearchDV:AccountCriterion:AccountPicker']")
    public WebElement link_DesktopActionsChargeReversalAccountPicker;

    @FindBy(xpath = "//a[@id='NewChargeReversalWizard:NewChargeReversalChargeSearchScreen:ChargeSearchDV:PolicyPeriodCriterion:PolicyPicker']")
    public WebElement link_DesktopActionsChargeReversalPolicyPicker;

    @FindBy(css = "input[id$='AccountCriterion-inputEl']")
    private WebElement input_AccountNumber;

    @FindBy(css = "input[id$='PolicyPeriodCriterion-inputEl']")
    private WebElement input_PolicyNumber;

    @FindBy(xpath = "//textarea[contains(@id,':NewReversalConfirmationDV:ReversalDescription-inputEl')]")
    public WebElement editBox_ReversalDescription;

    public Guidewire8Select comboBox_ChargeType() {
        return new Guidewire8Select(driver, "//table[contains(@id,':ChargeSearchDV:ChargePatternCriterion-triggerWrap')]");
    }

    public Guidewire8Select comboBox_ReversalReason() {
        return new Guidewire8Select(driver, "//table[contains(@id,':NewReversalConfirmationDV:Reason-triggerWrap')]");
    }    
    
    @FindBy(xpath = "//div[contains(@id,':NewChargeReversalChargesLV')]")
	public WebElement table_ChargeReversalTable;

    // -------------------------------------------------------
    // Helper Methods for Above Elements
    // -------------------------------------------------------

    public WebElement getChargeReversalTableRow(Date date, String description, String owner, Double amount) {
		HashMap<String, String> columnRowKeyValuePairs = new HashMap<String, String>();
		if (date != null) {
			columnRowKeyValuePairs.put("Date", DateUtils.dateFormatAsString("MM/dd/yyyy", date));
		}
		
		if (description != null) {
			columnRowKeyValuePairs.put("Description", description);
		}
		if (owner != null) {
			columnRowKeyValuePairs.put("Owner", owner);
		}
		
		if (amount != null) {
			columnRowKeyValuePairs.put("Amount", StringsUtils.currencyRepresentationOfNumber(amount));
		}
		
        return tableUtils.getRowInTableByColumnsAndValues(table_ChargeReversalTable, columnRowKeyValuePairs);
	}

    public void setChargeReversalDescription(String description) {
        clickWhenClickable(editBox_ReversalDescription);
        editBox_ReversalDescription.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editBox_ReversalDescription.sendKeys(description);
    }

    public void clickSelectButtonInChargeReversalTable(Date date, String description, String owner, Double amount){
    	WebElement theRow = getChargeReversalTableRow(date, description, owner, amount);
    	tableUtils.clickSelectLinkInTable(table_ChargeReversalTable, tableUtils.getRowNumberFromWebElementRow(theRow));
    }


    
    public void clickChargeReversalSearch() {
        clickSearch();
    }


    public void clickChargeReversalReset() {
        clickReset();
    }


    public void clickChargeReversalNext() {
        clickNext();
    }


    public void clickChargeReversalCancel() {
        clickCancel();
    }


    public void clickChargeReversalFinish() {
        clickFinish();
    }


    public void setChargeReversalChargeType(String typeToSelect) {
        comboBox_ChargeType().selectByVisibleText(typeToSelect);
    }


    public void setChargeReversalReason(String reasonToSelect) {
        comboBox_ReversalReason().selectByVisibleText(reasonToSelect);
    }


    public void setChargeReversalMinimiumAmount(double amountToFill) {
    	clickWhenVisible(editBox_BCChargeReversalMinimumAmount);
		editBox_BCChargeReversalMinimumAmount.sendKeys(Keys.HOME);
		editBox_BCChargeReversalMinimumAmount.sendKeys(String.valueOf(amountToFill));		
    }


    public void setChargeReversalMaximumAmount(double amountToFill) {
    	clickWhenVisible(editBox_BCChargeReversalMaximumAmount);
		editBox_BCChargeReversalMaximumAmount.sendKeys(Keys.HOME);
		editBox_BCChargeReversalMaximumAmount.sendKeys(String.valueOf(amountToFill));	
    }


    public void setChargeReversalEarliestDate(Date earliestDate) {
    	clickWhenVisible(editBox_BCChargeReversalEarliestDate);
		editBox_BCChargeReversalEarliestDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_BCChargeReversalEarliestDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", earliestDate));		
    }


    public void setChargeReversalLatestDate(Date latestDate) {
    	clickWhenVisible(editBox_BCChargeReversalLatestDate);
		editBox_BCChargeReversalLatestDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_BCChargeReversalLatestDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", latestDate));		
    }


    public void setChargeReversalAccount(String numToFill) {
        setText(input_AccountNumber, numToFill);
    }


    public void setChargeReversalPolicy(String numToFill) {
        setText(input_PolicyNumber, numToFill);
    }


    public void clickChargeReversalPolicyPicker() {
        clickWhenVisible(link_DesktopActionsChargeReversalPolicyPicker);
    }


    public void clickChargeReversalAccountPicker() {
        clickWhenVisible(link_DesktopActionsChargeReversalAccountPicker);
    }


    public WebElement getChargeReversalTable() {
        waitUntilElementIsVisible(table_DesktopActionsChargeReversal);
        return table_DesktopActionsChargeReversal;
    }


    public void clickReversalChargesSelectButton(int rowNumber) {
        WebElement selectButton = find(By.xpath("//a[@id='NewChargeReversalWizard:NewChargeReversalChargeSearchScreen:NewChargeReversalChargesLV:" + (rowNumber - 1) + ":Select']"));
        clickWhenClickable(selectButton);
    }
}
