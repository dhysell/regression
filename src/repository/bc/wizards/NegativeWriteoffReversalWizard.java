package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class NegativeWriteoffReversalWizard extends BasePage {

	private TableUtils tableUtils;
	
	public NegativeWriteoffReversalWizard(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	@FindBy(xpath = "//div[@id='LienholderPaymentNegativeWriteOffReversalWizard:LienholderPaymentSelectNegativeWriteoffScreen:NegativeWriteoffsLV']")
	public WebElement table_NegativeWriteOffReversal;

	public WebElement getNegativeWriteoffReversalTableRow(Date createDate, Double totalAmount) {
		StringBuilder xpathBuilder = new StringBuilder();
		if (!(createDate == null)) {
			String createDateGridColumnID = tableUtils.getGridColumnFromTable(table_NegativeWriteOffReversal, "Date");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + createDateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", createDate) + "')]");
		}
		if (!(totalAmount == null)) {
			String totalAmountGridColumnID = tableUtils.getGridColumnFromTable(table_NegativeWriteOffReversal, "Amount");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + totalAmountGridColumnID + "') and contains(.,'" + StringsUtils.currencyRepresentationOfNumber(totalAmount) + "')]");
		}

		xpathBuilder.replace(0, 9, ".//");
		WebElement tableRow = table_NegativeWriteOffReversal.findElement(By.xpath(xpathBuilder.toString()));
		return tableRow;
	}

	public void clickNegativeWriteoffReversalLinkByDateAndAmount(Date createDate, Double totalAamount) {
		WebElement negativeWriteOffTableRow = getNegativeWriteoffReversalTableRow(createDate, totalAamount);
		tableUtils.clickSelectLinkInTable(table_NegativeWriteOffReversal, Integer.valueOf(negativeWriteOffTableRow.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1);
	}
}
