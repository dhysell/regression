package repository.bc.wizards;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonCharges;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.ChargeCategory;
import repository.gw.enums.ChargeHoldStatus;
import repository.gw.enums.TransactionNumber;
import repository.gw.enums.TransactionType;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.TableUtils;

import java.util.Date;

public class EditChargeInformation extends BasePage {

	private TableUtils tableUtils;
	
	public EditChargeInformation(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath = "//div[@id='ChargeHoldsPopup:ChargeHoldsScreen:ChargesLV']")
	public WebElement table_ChargeHoldsPopup;

	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------

	public void selectChargeHoldStatus(WebElement tableRowNumber, ChargeHoldStatus status) {
		tableUtils.selectValueForSelectInTable(table_ChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableRowNumber), "Hold Status", status.getValue());
	}

	public void setHoldReleaseDate(WebElement tableRowNumber, Date holdReleaseDate) {
		tableUtils.setValueForCellInsideTable(table_ChargeHoldsPopup, tableUtils.getRowNumberFromWebElementRow(tableRowNumber), "Hold Release Date", "HoldReleaseDate", DateUtils.dateFormatAsString("MM/dd/yyyy", holdReleaseDate));
	}

    public WebElement getChargeHoldsPopupTableRow(Date chargeDate, String defaultPayer, TransactionNumber transactionNumber, ChargeCategory chargeType, TransactionType chargeContext, String chargeGroup, ChargeHoldStatus holdStatus, Date holdReleaseDate, String policyNumber, Double chargeAmount, String chargeDescription, Boolean partialCancel, String loanNumber, String usageDescription, String payerAddress, String deliveryOptions) {
		return new BCCommonCharges(getDriver()).getChargesOrChargeHoldsPopupTableRow(chargeDate, defaultPayer, transactionNumber, chargeType, chargeContext, chargeGroup, holdStatus, holdReleaseDate, policyNumber, chargeAmount, chargeDescription, partialCancel, loanNumber, usageDescription, payerAddress, deliveryOptions);
	}
}
