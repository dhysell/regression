package repository.pc.policy;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.TableUtils;

import java.util.ArrayList;
import java.util.List;

public class PolicyChargesToBC extends BasePage {

	private TableUtils tableUtils;
	
    public PolicyChargesToBC(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        this.tableUtils = new TableUtils(driver);
    }

    @FindBy(xpath = "//div[@id='ChargesToBC']")
    public WebElement table_ChangesToBCTablesContainer;


    public double getAmount(String transactionNumber, String linkedPayer) {
        WebElement tableWrapperUnderTransactionNum = table_ChangesToBCTablesContainer.findElement(By.xpath(".//tr/td/label[contains(text(), '" + transactionNumber + "')]/../parent::tr/following-sibling::tr/td/div[contains(@id,'ChargesToBC:')]"));
        String amountGridColumn = tableUtils.getGridColumnFromTable(tableWrapperUnderTransactionNum, "Amount");
        String linkedPayerGridColumn = tableUtils.getGridColumnFromTable(tableWrapperUnderTransactionNum, "Linked Payer");
        String xpath = ".//tr/td[contains(@class,'" + linkedPayerGridColumn + "') and contains(.,'" + linkedPayer + "')]/parent::tr/td[contains(@class,'" + amountGridColumn + "')]";
        double amount = NumberUtils.getCurrencyValueFromElement(tableWrapperUnderTransactionNum.findElement(By.xpath(xpath)).getText());
        return amount;
    }


    public List<ChargeObject> getChargesToBC(String transactionNumber) {
    	waitForPostBack();
        WebElement tableWrapperUnderTransactionNum = table_ChangesToBCTablesContainer.findElement(By.xpath(".//tr/td/label[contains(text(), '" + transactionNumber + "')]/parent::td/parent::tr/following-sibling::tr/td/div[contains(@id,'ChargesToBC:')]"));
        List<WebElement> tableRows = tableUtils.getAllTableRows(tableWrapperUnderTransactionNum);
        List<ChargeObject> chargesObjectList = new ArrayList<ChargeObject>();

        for (WebElement row : tableRows) {
        	double lineAmount = NumberUtils.getCurrencyValueFromElement(tableUtils.getCellWebElementInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Amount"));
        	String payer = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Payer");
        	String loanNumber = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Loan Number");
        	String oldLoanNumber = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Old Loan Number");
            String linePayer = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Payer Account Number");
            String chargeGroup = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "ChargeGroup");
            String linkedPayer = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Linked Payer");
            String chargePattern = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Charge Pattern");
            String earned = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Earned");
            String unearned = tableUtils.getCellTextInTableByRowAndColumnName(tableWrapperUnderTransactionNum, row, "Unearned");
            chargesObjectList.add(new ChargeObject(lineAmount, payer, loanNumber, oldLoanNumber, linePayer, chargeGroup, linkedPayer, chargePattern, earned, unearned));
//            chargesObjectList.add(new ChargeObject(lineAmount, linePayer));
        }
        waitForPostBack();
        return chargesObjectList;
    }


    public double getAmountByChargeGroup(String chargeGroup) {
        WebElement tableRowByChargeGroup = tableUtils.getRowInTableByColumnNameAndValue(table_ChangesToBCTablesContainer, "ChargeGroup", chargeGroup);
        String locationAmount = tableUtils.getCellTextInTableByRowAndColumnName(table_ChangesToBCTablesContainer, tableRowByChargeGroup, "Amount");
        double amount = NumberUtils.getCurrencyValueFromElement(locationAmount);
        return amount;
    }
}
