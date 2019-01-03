package repository.bc.desktop.actions;

import com.idfbins.enums.CountyIdaho;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class DesktopActionsCountyCashPayment extends BasePage  {
	
	private WebDriver driver;	
	private TableUtils tableUtils;
	
	public DesktopActionsCountyCashPayment(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
		this.driver = driver;
	}
	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------

	@FindBy(xpath = "//div[@id='CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:NewMultiPaymentLV-body']/div/table[starts-with(@id,'gridview-')]")
	public WebElement table_DesktopActionsCountyCashPmt;

	@FindBy(xpath = "//div[@id='CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:NewMultiPaymentLV']")
	public WebElement tablewrapper_DesktopActionsCountyCashPayment;

	public Guidewire8Select comboBox_CountyCashTopCountyCode() {
		return new Guidewire8Select(driver, "//table[contains(@id,'CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:CountySetter-triggerWrap')]");
	}

	public Guidewire8Select comboBox_CountyCashTopOfficeNum() {
		return new Guidewire8Select(driver, "//table[contains(@id,'CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:OfficeSetter-triggerWrap')]");
	}

	@FindBy(xpath = "//a[@id='CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:CountySetterBT']")
	public WebElement button_CountyCashPmtChangeSelectedCountyOffice;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------
	
	public void clickDesktopActionsCountyCashPmtAdd() {
		super.clickAdd();
	}

	
	public void clickCountyCashPmtChangesSelectedCountyOffice() {
		clickWhenVisible(button_CountyCashPmtChangeSelectedCountyOffice);
	}

	
	public void clickDesktopActionsCountyCashPmtRemove() {
		super.clickRemove();
	}

	
	public void clickDesktopActionsCountyCashPmtCancel() {
		super.clickCancel();
	}

	
	public void clickDesktopActionsCountyCashPmtNext() {
		super.clickNext();
	}

	
	public void clickDesktopActionsCountyCashPmtFinish() {
		super.clickFinish();
	}

	
	public void makeCountyCashPayment (String policyNumber, CountyIdaho county, String countyOffice) {
		int rowNumber = getNextAvailableLineInTable("Amount");
		setCountyCashPolicyNumber(rowNumber, policyNumber);
		setCountyCashPaymentAmount(rowNumber, 25.00);
		selectCountyCashTableCountyCode(rowNumber, county);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
		
		selectCountyCashTableOfficeNumber(rowNumber, countyOffice);
		clickDesktopActionsCountyCashPmtNext();
		
		clickDesktopActionsCountyCashPmtFinish();
		
	}
	
	
	public void setCountyCashPolicyNumber(int tableRowNumber, String policyNumber) {
		String policyNumberGridColumnID = tableUtils.getGridColumnFromTable(tablewrapper_DesktopActionsCountyCashPayment, "Policy #");
		WebElement editBox_DesktopActionsCountyPaymentsPolicyNumber = tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + policyNumberGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsCountyPaymentsPolicyNumber);
		if (policyNumber.equalsIgnoreCase("random")) {
			WebElement button_DesktopActionCountyPaymentsPolicyPicker = tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath("//div[@id='CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:NewMultiPaymentLV:" + (tableRowNumber - 1) + ":PolicyPeriod:SelectPolicyPeriod']"));
			clickWhenClickable(button_DesktopActionCountyPaymentsPolicyPicker);
			
			repository.bc.search.BCSearchAccounts accountSearch = new repository.bc.search.BCSearchAccounts(getDriver());
			accountSearch.pickRandomAccount("98");
			
		} else {
			tableUtils.setValueForCellInsideTable(tablewrapper_DesktopActionsCountyCashPayment, "PolicyPeriod", policyNumber);
		}
		
	}
	
	public void setCountyCashPolicyNumberFromPicker(int tableRowNumber, String policyNumber) {
		String policyNumberGridColumnID = tableUtils.getGridColumnFromTable(tablewrapper_DesktopActionsCountyCashPayment, "Policy #");
		WebElement editBox_DesktopActionsCountyPaymentsPolicyNumber = tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + policyNumberGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsCountyPaymentsPolicyNumber);
		
		WebElement button_DesktopActionCountyPaymentsPolicyPicker = tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath("//div[@id='CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:NewMultiPaymentLV:" + (tableRowNumber - 1) + ":PolicyPeriod:SelectPolicyPeriod']"));
		clickWhenClickable(button_DesktopActionCountyPaymentsPolicyPicker);
		waitForPostBack();
		repository.bc.search.BCSearchAccounts accountSearch = new BCSearchAccounts(getDriver());
		accountSearch.clickReset();
		accountSearch.setBCSearchAccountsAccountNumber(policyNumber);
		accountSearch.clickSearch();
		accountSearch.clickSelectAccountsButton();
		waitForPostBack();		
	}

	
	public void setCountyCashPaymentTopCounty(CountyIdaho county) {
		try {
			comboBox_CountyCashTopCountyCode().selectByVisibleText(county.getValue());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setCountyCashPaymentTopOffice(String officeToSelect) {
		try {
			comboBox_CountyCashTopOfficeNum().selectByVisibleText(officeToSelect);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setCountyCashPaymentType(int rowNumber, PaymentType paymentType) {
		table_DesktopActionsCountyCashPmt.findElement(By.xpath("//tr[" + rowNumber + "]/td[3]")).click(); // need to click the cell to trigger the generate of the input element
		try {
			String myXpath = "//input[contains(@id,'inputEl') and (@name='PaymentType')]/../../../../../table[contains(@id,'triggerWrap')]";
			WebElement myTable = find(By.xpath(myXpath)); // find the table which holds the combobox
			// System.out.println("the id is " + myTable.getAttribute("id"));
			Guidewire8Select mySelect = new Guidewire8Select(driver, "//table[(@id='" + myTable.getAttribute("id") + "')]");
			mySelect.sendKeys(paymentType.getValue());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void setCountyCashPaymentAmount(int rowNumber, double amountToFill) {
		String amountGridColumnID = tableUtils.getGridColumnFromTable(tablewrapper_DesktopActionsCountyCashPayment, "Amount");
		WebElement editBox_DesktopActionsCountyPaymentsAppliedAmount = tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (rowNumber - 1) + "')]/td[contains(@class,'" + amountGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsCountyPaymentsAppliedAmount);
		tableUtils.setValueForCellInsideTable(tablewrapper_DesktopActionsCountyCashPayment, "Amount", String.valueOf(amountToFill));
		
	}
	
	
	public void selectCountyCashTableCountyCode(int rowNumber, CountyIdaho county) {
		tableUtils.selectValueForSelectInTable(tablewrapper_DesktopActionsCountyCashPayment, rowNumber, "County Code", county.getValue());	
		
	}
	
	
	public void selectCountyCashTableOfficeNumber(int rowNumber, String office) {
		tableUtils.selectValueForSelectInTable(tablewrapper_DesktopActionsCountyCashPayment, rowNumber, "Office #", office);
		
	}
	
	public int getNextAvailableLineInTable(String headerColumn) {
		return tableUtils.getNextAvailableLineInTable(tablewrapper_DesktopActionsCountyCashPayment, headerColumn);
	}

	
	public WebElement getCountyCashPaymentNotesEditBox(int rowNumber) {
		waitUntilElementIsVisible(table_DesktopActionsCountyCashPmt);
		return table_DesktopActionsCountyCashPmt.findElement(By.id("CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:NewMultiPaymentLV:" + (rowNumber - 1) + ":Description"));
	}

	
	public void setCountyCashPaymentNotes(int rowNumber, String notesToFill) {
		getCountyCashPaymentNotesEditBox(rowNumber).sendKeys(notesToFill);
	}

	
	public void clickCountyCashPaymentCheckBox(int rowNumber) {
		waitUntilElementIsVisible(table_DesktopActionsCountyCashPmt);
		table_DesktopActionsCountyCashPmt.findElement(By.id("CountyMultiPaymentEntryWizard:NewCountyMultiPaymentScreen:NewMultiPaymentLV:" + (rowNumber - 1) + ":_Checkbox")).click();
	}

	
	public String getDate(int rowNumber) {
		String dateInstrumentGridColumnID = tableUtils.getGridColumnFromTable(tablewrapper_DesktopActionsCountyCashPayment, "Date");
		
		return tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath(".//tbody/tr[" + rowNumber + "]/td[contains(@class,'" + dateInstrumentGridColumnID + "')]/div")).getText();

	}

	
	public void setDate(int rowNumber, Date date) {
		String dateGridColumnID = tableUtils.getGridColumnFromTable(tablewrapper_DesktopActionsCountyCashPayment, "Date");
		WebElement editBox_DesktopActionsMultiplePaymentsAppliedAmount = tablewrapper_DesktopActionsCountyCashPayment.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (rowNumber - 1) + "')]/td[contains(@class,'" + dateGridColumnID + "')]/div"));
		clickWhenClickable(editBox_DesktopActionsMultiplePaymentsAppliedAmount);
		tableUtils.setValueForCellInsideTable(tablewrapper_DesktopActionsCountyCashPayment, "Date",	DateUtils.dateFormatAsString("MM/dd/yyyy", date));
		
	}	
}
