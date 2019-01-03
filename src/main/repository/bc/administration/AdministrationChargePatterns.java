package repository.bc.administration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class AdministrationChargePatterns extends BasePage {

	private TableUtils tableUtils;
	
	public AdministrationChargePatterns(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//div[@id='ChargePatterns:ChargePatternsScreen:ChargePatternsLV')]")
	public WebElement table_AdministrationChargePatterns;

	// -------------------------------------------------------
	// Helper Methods for Above Elements - Trouble Tickets Page
	// -------------------------------------------------------

	public WebElement getAdministrationChargePatternsTable() {
		waitUntilElementIsVisible(table_AdministrationChargePatterns);
		return table_AdministrationChargePatterns;
	}

	public String getChargePatternsTAccountOwnerByCode(String code) {
		return tableUtils.getCellTextInTableByRowAndColumnName(table_AdministrationChargePatterns, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AdministrationChargePatterns, "Code", code)), "Code");
	}
	
	public void clickActivityPatternsTableCheckBoxBySubject(String subject) {
		tableUtils.setCheckboxInTable(table_AdministrationChargePatterns, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(table_AdministrationChargePatterns, "Subject", subject)), true);
	}

	public boolean checkIfCellExistsInTableByLinkText(String linkText) {
		return tableUtils.checkIfLinkExistsInTable(table_AdministrationChargePatterns, linkText);
	}

}
