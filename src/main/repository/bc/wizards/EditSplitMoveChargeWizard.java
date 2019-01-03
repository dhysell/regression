package repository.bc.wizards;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.BasePage;
import repository.gw.helpers.TableUtils;

public class EditSplitMoveChargeWizard extends BasePage {

	private TableUtils tableUtils;
	
	public EditSplitMoveChargeWizard(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
		this.tableUtils = new TableUtils(driver);
	}

	///////////////////////
	// "Recorded Elements"//
	///////////////////////

	@FindBy(xpath = "//a[contains(@id,':splitChargeScreen:Update')]")
	public WebElement button_Execute;

	@FindBy(xpath = "//div[@id='SplitChargePopup:splitChargeScreen:1']")
	public WebElement table_EditSplitMoveCharge;

	//////////////////
	// Helper Methods//
	//////////////////

	public void clickExecute() {
		clickWhenClickable(button_Execute);
	}

	public void setPayer(int tableRowNumber, String payer) {
		String payerGridColumnID = tableUtils.getGridColumnFromTable(table_EditSplitMoveCharge, "Payer");		
		WebElement editBox_Payer = table_EditSplitMoveCharge.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + payerGridColumnID + "')]/div"));		
		clickWhenClickable(editBox_Payer);		
		tableUtils.setValueForCellInsideTable(table_EditSplitMoveCharge, "Payer", payer);
	}

	public void setPayerFromPicker(int tableRowNumber, String accountNumber) {

		WebElement accountPicker = table_EditSplitMoveCharge.findElement(By.xpath("//img[@id='SplitChargePopup:splitChargeScreen:" + (tableRowNumber - 1) + ":Payer:AccountPicker']"));
		clickWhenClickable(accountPicker);
		repository.bc.search.BCSearchAccounts search = new BCSearchAccounts(getDriver());
		search.setBCSearchAccountsAccountNumber(accountNumber);
		search.clickSearch();
		search.clickSelectAccountsButton();
	}

	public void setChargeGroup(int tableRowNumber, String chargeGroup) {
		String chargegroupGridColumnID = tableUtils.getGridColumnFromTable(table_EditSplitMoveCharge, "Charge Group");
		WebElement editBox_ChargeGroup = table_EditSplitMoveCharge.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + chargegroupGridColumnID + "')]/div"));
		clickWhenClickable(editBox_ChargeGroup);
		tableUtils.setValueForCellInsideTable(table_EditSplitMoveCharge, "ChargeGroup", chargeGroup);
	}

	public void setLoanNumber(int tableRowNumber, String loanNumber) {
		String loanNubmerGridColumnID = tableUtils.getGridColumnFromTable(table_EditSplitMoveCharge, "Loan Number");
		WebElement editBox_LoanNumber = table_EditSplitMoveCharge.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + loanNubmerGridColumnID + "')]/div"));
		clickWhenClickable(editBox_LoanNumber);
		tableUtils.setValueForCellInsideTable(table_EditSplitMoveCharge, "LoanNumber", loanNumber);
	}

	public void setCoverablePublicID(int tableRowNumber, String CoverablePublicID) {
		String CoverablePublicIDGridColumnID = tableUtils.getGridColumnFromTable(table_EditSplitMoveCharge, "Coverable Public ID");
		WebElement editBox_CoverablePublicID = table_EditSplitMoveCharge.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + CoverablePublicIDGridColumnID + "')]/div"));
		clickWhenClickable(editBox_CoverablePublicID);
		tableUtils.setValueForCellInsideTable(table_EditSplitMoveCharge, "covid", CoverablePublicID);
	}

	public void setAmount(int tableRowNumber, double amount) {
		String amountGridColumnID = tableUtils.getGridColumnFromTable(table_EditSplitMoveCharge, "Amount");
		WebElement editBox_amount = table_EditSplitMoveCharge.findElement(By.xpath(".//tbody/tr[contains(@data-recordindex,'" + (tableRowNumber - 1) + "')]/td[contains(@class,'" + amountGridColumnID + "')]/div"));
		clickWhenClickable(editBox_amount);
		tableUtils.setValueForCellInsideTable(table_EditSplitMoveCharge, "amtCell", String.valueOf(amount));
	}

	public void fillOutEditSplitMoveChargeTable(int tableRowNumber, String payer, String chargeGroup, String loanNumber, String CoverablePublicID, Double amount) {
		if (!(payer == null)) {			
			setPayerFromPicker(tableRowNumber, payer);
		}
		if (!(chargeGroup == null)) {
			clickProductLogo();
			setChargeGroup(tableRowNumber, chargeGroup);
		}
		
		if (!(loanNumber == null)) {
			clickProductLogo();
			setLoanNumber(tableRowNumber, loanNumber);
		}
		if (!(CoverablePublicID == null)) {
			clickProductLogo();
			setCoverablePublicID(tableRowNumber, CoverablePublicID);
		}
		if (!(amount == null)) {
			setAmount(tableRowNumber, amount);
		}
	}
	
	public String getAlert() {
        try {
            String alertMsg = find(By.xpath("//div[contains(@id, ':splitChargeScreen:_msgs')]/div")).getText();
            return alertMsg;
        } catch (NoSuchElementException e) {
            return "No Alert found.";
        }
    }

}
