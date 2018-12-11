package repository.bc.desktop;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.PolicyCompany;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.helpers.StringsUtils;
import repository.gw.helpers.TableUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DesktopLienholderMultiPayments extends BasePage {
	
	private WebDriver driver;
	private TableUtils tableUtils;
	public DesktopLienholderMultiPayments(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
		this.driver = driver;
	}

	////////////////////////
	// Recorded WebElements//
	////////////////////////

	@FindBy(xpath = "//div[@id='LienholderMultiPayments:2']")
	public WebElement table_DesktopLienholderMultiPaymentsPicker;

	@FindBy(xpath = "//div[@id='LienholderMultiPayments:3']")
	public WebElement table_DesktopLienholderMultiPaymentsBreakdown;
	
	@FindBy(xpath = "//input[@label='Earliest Date' or contains(@id, ':EarliestDateCriterion-inputEl')]")
	private WebElement editBox_BCLHMultiPaymentsEarliestDate;

	@FindBy(xpath = "//input[@label='Latest Date' or contains(@id, ':LatestDateCriterion-inputEl')]")
	private WebElement editBox_BCLHMultiPaymentsLatestDate;
	
	Guidewire8Select select_BCLHMultiPaymentsCompany() {
		return new Guidewire8Select(driver, "//table[contains(@id, 'CompanyCriterion-triggerWrap')]");
	}

	//////////////////
	// Helper Methods//
	//////////////////
	
	public void selectPolicyCompany(PolicyCompany policyCompany) {
		Guidewire8Select mySelect = select_BCLHMultiPaymentsCompany();
		mySelect.selectByVisibleText(policyCompany.getValue());
	}
	
	public void selectLienholderMultiPaymentsCompany(PolicyCompany policyCompany) {
		Guidewire8Select mySelect = select_BCLHMultiPaymentsCompany();
		mySelect.selectByVisibleText(policyCompany.getValue());
	}

	public void setLienholderMultipPaymentsEarliestCreatedDate(Date earliestCreatedDate) {
		clickWhenVisible(editBox_BCLHMultiPaymentsEarliestDate);
		editBox_BCLHMultiPaymentsEarliestDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_BCLHMultiPaymentsEarliestDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", earliestCreatedDate));
		clickProductLogo();
	}

	public Date getLienholderMultipPaymentsEarliestCreatedDate() {
		waitUntilElementIsVisible(editBox_BCLHMultiPaymentsEarliestDate);
		Date earliestDate = null;
		try {
			earliestDate = DateUtils.convertStringtoDate(editBox_BCLHMultiPaymentsEarliestDate.getText(), "MM/dd/yyyy");
		} catch (ParseException e) {
			Assert.fail("There was an error attempting to parse the String date to a Date format.");
		}
		return earliestDate;
	}

	public void setLienholderMultipPaymentsLatestCreatedDate(Date latestCreatedDate) {
		clickWhenVisible(editBox_BCLHMultiPaymentsLatestDate);
		editBox_BCLHMultiPaymentsLatestDate.sendKeys(Keys.chord(Keys.CONTROL + "a"));
		editBox_BCLHMultiPaymentsLatestDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", latestCreatedDate));
		clickProductLogo();
	}

	public Date getLienholderMultipPaymentsLatestCreatedDate() {
		waitUntilElementIsVisible(editBox_BCLHMultiPaymentsLatestDate);
		Date latestDate = null;
		try {
			latestDate = DateUtils.convertStringtoDate(editBox_BCLHMultiPaymentsLatestDate.getText(), "MM/dd/yyyy");
		} catch (ParseException e) {
			Assert.fail("There was an error attempting to parse the String date to a Date format.");
		}
		return latestDate;
	}

	public WebElement getLienholderMultiPaymentsTableRow(Date createDate, Double totalAmount) {
		StringBuilder xpathBuilder = new StringBuilder();
		if (!(createDate == null)) {
			String createDateGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopLienholderMultiPaymentsPicker, "Create Date");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + createDateGridColumnID + "') and contains(.,'" + DateUtils.dateFormatAsString("MM/dd/yyyy", createDate) + "')]");
		}
		if (!(totalAmount == null)) {
			String totalAmountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopLienholderMultiPaymentsPicker, "Total");
			xpathBuilder.append("/parent::tr/td[contains(@class,'" + totalAmountGridColumnID + "') and contains(.,'" + StringsUtils.currencyRepresentationOfNumber(totalAmount) + "')]");
		}

		xpathBuilder.replace(0, 9, ".//");
		WebElement tableRow = table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(xpathBuilder.toString()));
		return tableRow;
	}

	public void clickLienholderMultiPaymentsTableRow(Date createDate, Double totalAmount) {
		WebElement rowResult = getLienholderMultiPaymentsTableRow(createDate, totalAmount);
		clickWhenClickable(rowResult);
	}

	public int getLienholderMultiPaymentsTableRowCount() {
		return tableUtils.getRowCount(table_DesktopLienholderMultiPaymentsPicker);
	}

	private void getPaymentList() {
		Date earliestDate = null;
		int i = 0;
		while (getLienholderMultiPaymentsTableRowCount() < 0) {
			earliestDate = getLienholderMultipPaymentsEarliestCreatedDate();
			DateUtils.dateAddSubtract(earliestDate, DateAddSubtractOptions.Month, -1);
			setLienholderMultipPaymentsEarliestCreatedDate(earliestDate);
			sendArbitraryKeys(Keys.TAB);
			waitForPostBack();
			i++;
			if (i > 6) {
				Assert.fail("The date range for the payment search went back 6 months from the default date, but no payments were found.");
			}
		}
	}

	private List<WebElement> getViableLienholdersList() {
		List<WebElement> viableLienholdersList = new ArrayList<>();
		for (int i = 0; i < (getLienholderMultiPaymentsBreakdownTableRowCount() - 1); i++) {
			String policyGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopLienholderMultiPaymentsBreakdown, "Policy");
			WebElement editBox_PolicyEditBox = table_DesktopLienholderMultiPaymentsBreakdown.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]/td[contains(@class,'" + policyGridColumnID + "')]/div"));
			if (!editBox_PolicyEditBox.getText().equalsIgnoreCase("None")) {
				viableLienholdersList.add(editBox_PolicyEditBox);
			}
		}
		return viableLienholdersList;
	}

	public Date clickRandomLienholderMultiPaymentsTableRow() {
		getPaymentList();

		int randomRow = NumberUtils.generateRandomNumberInt(0, getLienholderMultiPaymentsTableRowCount() - 1);
		String createTimeGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopLienholderMultiPaymentsPicker, "Create Time");
		Date createDate = new Date();
		try {
			createDate = DateUtils.convertStringtoDate((table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + randomRow + "')]/td[contains(@class,'" + createTimeGridColumnID + "')]/div")).getText()), "MM/dd/yyyy");
		} catch (ParseException e) {
			Assert.fail("The clickRandomLienholderMultiPaymentsTableRow method was unable to correctly parse the 'Create Time' argument from the page.");
		}
		table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + randomRow + "')]")).click();
		return createDate;
	}

	public Date clickRandomLienholderMultiPaymentsTableRowWithViablePaymentInBreakdownTable() {
		getPaymentList();

		int randomRow = NumberUtils.generateRandomNumberInt(0, getLienholderMultiPaymentsTableRowCount() - 1);
		String createDateGridColumn = tableUtils.getGridColumnFromTable(table_DesktopLienholderMultiPaymentsPicker, "Create Date");
		Date createDate = new Date();
		try {
			createDate = DateUtils.convertStringtoDate((table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + randomRow + "')]/td[contains(@class,'" + createDateGridColumn + "')]/div")).getText()), "MM/dd/yyyy");
		} catch (ParseException e) {
			Assert.fail("The clickRandomLienholderMultiPaymentsTableRow method was unable to correctly parse the 'Create Time' argument from the page.");
		}
		table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + randomRow + "')]")).click();

		int i = 0;
		while (tableUtils.getRowCount(table_DesktopLienholderMultiPaymentsBreakdown) < 2) {
			try {
				createDate = DateUtils.convertStringtoDate((table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]/td[contains(@class,'" + createDateGridColumn + "')]/div")).getText()), "MM/dd/yyyy");
			} catch (ParseException e) {
				Assert.fail("The clickRandomLienholderMultiPaymentsTableRow method was unable to correctly parse the 'Create Time' argument from the page.");
			} catch (NoSuchElementException e2) {
				Assert.fail("Checked all rows for a viable payment, but none were found.");
			}
			table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]")).click();
			i++;
		}

		List<WebElement> viableLienholdersList = getViableLienholdersList();
		while (viableLienholdersList.size() < 1) {
			try {
				createDate = DateUtils.convertStringtoDate((table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]/td[contains(@class,'" + createDateGridColumn + "')]/div")).getText()), "MM/dd/yyyy");
			} catch (ParseException e) {
				Assert.fail("The clickRandomLienholderMultiPaymentsTableRow method was unable to correctly parse the 'Create Date' argument from the page.");
			} catch (NoSuchElementException e2) {
				Assert.fail("Checked all rows for a viable payment, but none were found.");
			}
			table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + i + "')]")).click();
			viableLienholdersList = getViableLienholdersList();
			i++;
		}
		return createDate;
	}

	/**
	 * This method returns the payment total of the physically selected row in the MultiPaymentsPicker table. The row must be highlighted first in order to return the correct value.
	 * 
	 * @return paymentTotal - The total of the selected payment row, expressed as a double.
	 */

	public double getPaymentTotal() {
		String totalAmountGridColumnID = tableUtils.getGridColumnFromTable(table_DesktopLienholderMultiPaymentsPicker, "Total");
		double paymentTotal = NumberUtils.getCurrencyValueFromElement(table_DesktopLienholderMultiPaymentsPicker.findElement(By.xpath(".//tbody/tr[contains(@class, 'x-grid-row-focused')]/td[contains(@class,'" + totalAmountGridColumnID + "')]/div")).getText());
		return paymentTotal;
	}

	public int getLienholderMultiPaymentsBreakdownTableRowCount() {
		return tableUtils.getRowCount(table_DesktopLienholderMultiPaymentsBreakdown);
	}

	public void clickRandomLienholderMultiPaymentsBreakdownTableRow() {
		int randomRow = NumberUtils.generateRandomNumberInt(0, getLienholderMultiPaymentsBreakdownTableRowCount() - 1);
		table_DesktopLienholderMultiPaymentsBreakdown.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + randomRow + "')]")).click();
	}

	public String getLienholderMultipPaymentsBreakdownLienholderAccountNumber(int rowNumber) {
		return table_DesktopLienholderMultiPaymentsBreakdown.findElement(By.xpath(".//a[contains(@id,'LienholderMultiPayments:payment:" + (rowNumber - 1) + ":Account')]")).getText();
	}

	public void clickLienholderMultipPaymentsBreakdownLienholderAccountNumber(int rowNumber) {
		table_DesktopLienholderMultiPaymentsBreakdown.findElement(By.xpath(".//a[contains(@id,'LienholderMultiPayments:payment:" + (rowNumber - 1) + ":Account')]")).click();
	}

	public String getLienholderMultipPaymentsBreakdownLienholderPaymentPolicyNumber(int rowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopLienholderMultiPaymentsBreakdown, rowNumber, "Policy");
	}

	public String getLienholderMultipPaymentsBreakdownLienholderPaymentLoanNumber(int rowNumber) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_DesktopLienholderMultiPaymentsBreakdown, rowNumber, "Loan Number");
	}

	public List<String> clickRandomLienholderMultiPaymentsBreakdownLienholderAccountNumber() {
		List<String> paymentInformation = new ArrayList<String>();
		List<WebElement> viableLienholdersList = getViableLienholdersList();

		int randomRow = NumberUtils.generateRandomNumberInt(0, (viableLienholdersList.size() - 1));
		WebElement randomLienholderAccount = viableLienholdersList.get(randomRow);
		int chosenRowNumber = Integer.valueOf(randomLienholderAccount.findElement(By.xpath(".//parent::td/parent::tr")).getAttribute("data-recordindex")) + 1;
		paymentInformation.add(getLienholderMultipPaymentsBreakdownLienholderAccountNumber(chosenRowNumber));
		paymentInformation.add(getLienholderMultipPaymentsBreakdownLienholderPaymentPolicyNumber(chosenRowNumber));
		paymentInformation.add(getLienholderMultipPaymentsBreakdownLienholderPaymentLoanNumber(chosenRowNumber));
		clickLienholderMultipPaymentsBreakdownLienholderAccountNumber(chosenRowNumber);
		return paymentInformation;
	}
}
