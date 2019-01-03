package repository.pc.workorders.generic;


import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.LossHistoryFields;
import repository.gw.enums.LossHistoryType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

public class GenericWorkorderRiskAnalysis_PriorLosses extends GenericWorkorderRiskAnalysis {
	
	private WebDriver driver;
	private TableUtils tableUtils;

    public GenericWorkorderRiskAnalysis_PriorLosses(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
        this.driver = driver;
    }


    Guidewire8Select select_LossHistoryType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':LossHistoryType-triggerWrap')]");
    }

    private Guidewire8Select LossHistoryType() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':Job_RiskAnalysisScreen:RiskAnalysisCV:LossHistoryTypeSelectDV:LossHistoryType-triggerWrap')]");
    }

    public void setLossHistoryType(String value) {
        Guidewire8Select mySelect = LossHistoryType();
        mySelect.selectByVisibleTextPartial(value);
    }

    @FindBy(xpath = "//div[contains(@id, ':Job_RiskAnalysisScreen:RiskAnalysisCV:LossHistoryInputSet:ManualLossHistoryLV')]")
    private WebElement table_ManualLossHistory;

    public void setManualLossHistory(int row, String policyLine, String occuranceDate, String description, String totalIncurred, String amountPaid, String openReserve, String status) {
        tableUtils.selectValueForSelectInTable(table_ManualLossHistory, row, "Policy Line", policyLine);
        tableUtils.clickCellInTableByRowAndColumnName(table_ManualLossHistory, row, "Occurrence Date");
        WebElement text_occuranceDate = table_ManualLossHistory.findElement(By.xpath(".//input[contains(@name,'OccurrenceDate')]"));
        text_occuranceDate.sendKeys(occuranceDate);
        //added below two lines if there is a validation on occurance date then
        sendArbitraryKeys(Keys.TAB);
        waitForPostBack();
        tableUtils.clickCellInTableByRowAndColumnName(table_ManualLossHistory, row, "Occurrence Date");
        tableUtils.setValueForCellInsideTable(table_ManualLossHistory, row, "Description", "Description", description);
        tableUtils.setValueForCellInsideTable(table_ManualLossHistory, row, "Total Incurred", "TotalIncurred", totalIncurred);
        tableUtils.setValueForCellInsideTable(table_ManualLossHistory, row, "Amount Paid", "AmountPaid", amountPaid);
        tableUtils.setValueForCellInsideTable(table_ManualLossHistory, row, "Open Reserve", "AmountReserved", openReserve);
        //tableUtils.selectValueForSelectInTable(table_ManualLossHistory, row, "Status", status);
    }

    public void setLossHistory(LossHistoryType lossType) {
        Guidewire8Select mySelect = select_LossHistoryType();
        mySelect.selectByVisibleText(lossType.getValue());
    }

    @FindBy(xpath = "//div[contains(@id, ':LossHistoryInputSet:ManualLossHistoryLV-body')]/div/table")
    private WebElement table_LossHistory;

    private void clickElement(int tableRow, LossHistoryFields element) {
        List<WebElement> myList = table_LossHistory.findElements(By.xpath(".//child::tr"));
        switch (element.getValue()) {
            case "Occurrence Date":
                myList.get(tableRow).findElement(By.xpath(".//child::td[2]")).click();
                break;
            case "Description":
                myList.get(tableRow).findElement(By.xpath(".//child::td[3]")).click();
                break;
            case "Total Incurred":
                myList.get(tableRow).findElement(By.xpath(".//child::td[4]")).click();
                break;
            case "Amount Paid":
                myList.get(tableRow).findElement(By.xpath(".//child::td[5]")).click();
                break;
            case "Amount Reserved":
                myList.get(tableRow).findElement(By.xpath(".//child::td[6]")).click();
                break;
            case "Status":
                myList.get(tableRow).findElement(By.xpath(".//child::td[7]")).click();
                break;
        }
    }

    public void removeLossHistory(int tableRow) {
        List<WebElement> myList = table_LossHistory.findElements(By.xpath(".//child::tr"));
        myList.get(tableRow).findElement(By.xpath(".//child::td[1]/div/img")).click();
        clickRemove();
    }

    public void addLossHistory(Date occuranceDate, String Description, int totalIncurred, int amountPaid,
                               int openReserve, String status) {
        List<WebElement> myList = table_LossHistory.findElements(By.xpath(".//tbody/tr"));
        myList.get(myList.size() - 1);
        setOccuranceDate(myList.size() - 1, occuranceDate);
        setDescription(myList.size() - 1, Description);
        setTotalIncurred(myList.size() - 1, totalIncurred);
        setAmountPaid(myList.size() - 1, amountPaid);
        setOpenReserve(myList.size() - 1, openReserve);
        setStatus(myList.size() - 1, status);
    }

    @FindBy(xpath = "//span[contains(@id, ':Unlock-btnEl')]")
    private WebElement checkbox_LossHistoryTR;

    @FindBy(xpath = "//input[contains(@name, 'OccurrenceDate')]")
    private WebElement edixbox_OccuranceDate;

    @FindBy(xpath = "//input[contains(@name, 'Description')]")
    private WebElement editbox_Description;

    @FindBy(xpath = "//input[contains(@name, 'TotalIncurred')]")
    private WebElement editbox_TotalIncurred;

    @FindBy(xpath = "//input[contains(@name, 'AmountPaid')]")
    private WebElement editbox_AmountPaid;

    @FindBy(xpath = "//input[contains(@name, 'AmountReserved')]")
    private WebElement editbox_OpenReserve;
    @FindBy(xpath = "//input[contains(@name, 'Status')]")
    private WebElement editbox_Status;


    public void setOccuranceDate(int tableRow, Date occuranceDate) {
        clickElement(tableRow, LossHistoryFields.OccuranceDate);
        edixbox_OccuranceDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        edixbox_OccuranceDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", occuranceDate));
        clickProductLogo();
    }


    public void setDescription(int tableRow, String desc) {
        clickElement(tableRow, LossHistoryFields.Description);
        editbox_Description.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Description.sendKeys(desc);
        clickProductLogo();
    }


    public void setTotalIncurred(int tableRow, int totalIncurred) {
        clickElement(tableRow, LossHistoryFields.TotalIncurred);
        editbox_TotalIncurred.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_TotalIncurred.sendKeys(String.valueOf(totalIncurred));
        clickProductLogo();
    }


    public void setAmountPaid(int tableRow, int amountPaid) {
        clickElement(tableRow, LossHistoryFields.AmountPaid);
        editbox_AmountPaid.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_AmountPaid.sendKeys(String.valueOf(amountPaid));
        clickProductLogo();
    }


    public void setOpenReserve(int tableRow, int openReserve) {
        clickElement(tableRow, LossHistoryFields.OpenReserve);
        editbox_OpenReserve.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_OpenReserve.sendKeys(String.valueOf(openReserve));
        clickProductLogo();
    }


    public void setStatus(int tableRow, String status) {
        clickElement(tableRow, LossHistoryFields.Status);
        editbox_Status.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_Status.sendKeys(status);
        clickProductLogo();
    }


}










