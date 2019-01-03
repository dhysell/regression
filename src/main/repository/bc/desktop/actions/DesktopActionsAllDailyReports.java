package repository.bc.desktop.actions;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class DesktopActionsAllDailyReports extends BasePage  {
	
	public DesktopActionsAllDailyReports(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//table[@id='AllDailyReports:AllDailyReports2Screen:allDailyReportsLV']")
	public WebElement table_DesktopActionsAllDailyReports;

	@FindBy(xpath = "//table[@id='PrintOptionPopup:PrintOptionPopupScreen:PrintOptionDV:CustomCsvColumnsLV']")
	public WebElement table_DesktopActionsAllDailyReportsCustomCSV;

	@FindBy(xpath = "//input[@id='AllDailyReports:AllDailyReports2Screen:startDate-inputEl']")
	public WebElement editbox_DesktopActionsAllDailyReportsStartDate;

	@FindBy(xpath = "//input[@id='AllDailyReports:AllDailyReports2Screen:endDate-inputEl']")
	public WebElement editbox_DesktopActionsAllDailyReportsEndDate;

	@FindBy(xpath = "//a[@id='AllDailyReports:AllDailyReports2Screen:allDailyReportsLV_tb:testprintbutton']")
	public WebElement link_DesktopActionsAllDailyReportExportResults;

	@FindBy(xpath = "//a[@id='AllDailyReports:AllDailyReports2Screen:allDailyReportsLV_tb:RefreshToolbarButton']")
	public WebElement link_DesktopActionsAllDailyReportRefresh;

	@FindBy(xpath = "//a[@id='AllDailyReports:AllDailyReports2Screen:allDailyReportsLV_tb:Cancel']")
	public WebElement link_DesktopActionsAllDailyReportPreviousScreen;

	@FindBy(xpath = "//input[@id='PrintOptionPopup:PrintOptionPopupScreen:PrintOptionDV:PrintChoice_Choice-inputEl']")
	public WebElement editbox_DesktopActionsAllDailyReportsPrintChoice;

	@FindBy(xpath = "//input[@id='PrintOptionPopup:PrintOptionPopupScreen:PrintOptionDV:ExportChoice_Choice-inputEl']")
	public WebElement editbox_DesktopActionsAllDailyReportsExport;

	@FindBy(xpath = "//input[@id='PrintOptionPopup:PrintOptionPopupScreen:PrintOptionDV:CustomExportChoice_Choice-inputEl']")
	public WebElement editbox_DesktopActionsAllDailyReportsCustomExport;

	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	
	public void clickAllDailyReportsExportResults() {
		clickWhenVisible(link_DesktopActionsAllDailyReportExportResults);
	}

	
	public void clickAllDailyReportsRefresh() {
		clickWhenVisible(link_DesktopActionsAllDailyReportRefresh);
	}

	
	public void clickAllDailyReportsPreviousScreen() {
		clickWhenVisible(link_DesktopActionsAllDailyReportPreviousScreen);
	}

	
	public void setAllDailyReportsStartDate(String dateToFill) {
		clickWhenVisible(editbox_DesktopActionsAllDailyReportsStartDate);
		editbox_DesktopActionsAllDailyReportsStartDate.sendKeys(Keys.CONTROL + "a");
		editbox_DesktopActionsAllDailyReportsStartDate.sendKeys(dateToFill);
	}

	
	public void setAllDailyReportsEndDate(String dateToFill) {
		clickWhenVisible(editbox_DesktopActionsAllDailyReportsEndDate);
		editbox_DesktopActionsAllDailyReportsEndDate.sendKeys(Keys.CONTROL + "a");
		editbox_DesktopActionsAllDailyReportsEndDate.sendKeys(dateToFill);
	}

	
	public void clickPrintExportOk() {
		super.clickOK();
	}

	
	public void clickPrintExportDone() {
		super.clickDone();
	}

	
	public void clickPrintOption() {
		clickWhenVisible(editbox_DesktopActionsAllDailyReportsPrintChoice);
	}

	
	public void clickExportOption() {
		clickWhenVisible(editbox_DesktopActionsAllDailyReportsExport);
	}

	
	public void clickCustomExportOption() {
		clickWhenVisible(editbox_DesktopActionsAllDailyReportsCustomExport);
	}

	
	public WebElement getCustomExportTable() {
		waitUntilElementIsVisible(table_DesktopActionsAllDailyReportsCustomCSV);
		return table_DesktopActionsAllDailyReportsCustomCSV;
	}

	
	public void clickCustomExportTableCheckbox(int rowNumber) {
		waitUntilElementIsVisible(table_DesktopActionsAllDailyReportsCustomCSV);
		table_DesktopActionsAllDailyReportsCustomCSV
				.findElement(By.id("PrintOptionPopup:PrintOptionPopupScreen:PrintOptionDV:CustomCsvColumnsLV:"
						+ (rowNumber - 1) + ":Select"))
				.click();
		;
	}

}
