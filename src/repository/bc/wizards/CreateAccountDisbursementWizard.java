package repository.bc.wizards;

import com.idfbins.enums.State;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.DisbursementReason;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.helpers.DateUtils;
import repository.gw.infobar.InfoBar;

import java.util.Date;

public class CreateAccountDisbursementWizard extends BasePage {
	
	private WebDriver driver;

	public CreateAccountDisbursementWizard(WebDriver driver) {
		
		super(driver);
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	// -----------------------------------------------
	// "Recorded Elements" and their XPaths
	// -----------------------------------------------

	@FindBy(xpath = "//input[contains(@id, ':City-inputEl') or contains(@id, ':city-inputEl')]")
	private WebElement editBox_BCAccountDisbursementCity;
	
	private Guidewire8Select select_BCAccountDisbursementState() {
 		return new Guidewire8Select(driver, "//table[contains(@id, ':State-triggerWrap') or contains(@id, ':state-triggerWrap')]");
	}
	
	@FindBy(xpath = "//input[contains(@id, ':PostalCode-inputEl') or contains(@id, ':postalCode-inputEl')]")
	private WebElement editBox_BCAccountDisbursementZip;
	
	public Guidewire8Select select_CreateAccountDisbursementPolicyNumber() {
		return new Guidewire8Select(driver, "//table[contains(@id,':DisbursementUnappliedSelectionDV:policy-triggerWrap')]");
	}

	public Guidewire8Select select_CreateAccountDisbursementLoanNumber() {
		return new Guidewire8Select(driver, "//table[contains(@id,':DisbursementUnappliedSelectionDV:loannumber-triggerWrap')]");
	}

	public Guidewire8Select select_CreateAccountDisbursementUnappliedFund() {
		return new Guidewire8Select(driver, "//table[contains(@id,':DisbursementUnappliedSelectionDV:UnappliedFunds-triggerWrap')]");
	}

	public Guidewire8Select select_CreateAccountDisbursementReason() {
		return new Guidewire8Select(driver, "//table[contains(@id,':CreateDisbursementDetailDV:reason-triggerWrap')]");
	}

	@FindBy(xpath = "//div[contains(@id, 'Screen:DisbursementUnappliedSelectionDV:loannumber-inputEl')]")
	public WebElement label_CreateAccountDisbursementLoanNumber;

	@FindBy(xpath = "//input[contains(@id, 'Screen:DisbursementPage2DV:LienNumber-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementLienHolderNumber;

	@FindBy(xpath = "//input[contains(@id, 'Screen:DisbursementPage2DV:InsuredName-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementInsuredName;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:amount-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementAmount;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:Detail-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementDisbursementDetail;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:PolicyNumber-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementPolicyNumber;

	@FindBy(xpath = "//div[contains(@id, 'Screen:DisbursementUnappliedSelectionDV:policy-inputEl')]")
	public WebElement label_CreateAccountDisbursementPolicyNumber;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:addressLIne1-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementAddressLine1;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:effectiveDate-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementDueDate;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:payTo-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementPayTo;

	@FindBy(xpath = "//input[contains(@id, 'Screen:CreateDisbursementDetailDV:mailTo-inputEl')]")
	public WebElement editbox_CreateAccountDisbursementMailTo;

	@FindBy(xpath = "//span[contains(@id, 'AccountCreateDisbursementWizard:CreateDisbursementConfirmScreen:ApprovalActivityAlertBar')]")
	public WebElement banner_AccountDisbursementsAlertBar;

	// -------------------------------------------------------
	// Helper Methods for Above Elements
	// -------------------------------------------------------

	public boolean alertBarExists() {
		return banner_AccountDisbursementsAlertBar.isDisplayed();
	}

	public String getAlertBarBanner() {
		return banner_AccountDisbursementsAlertBar.getText();
	}

	public void setAddressLine1(String address) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementAddressLine1);
		editbox_CreateAccountDisbursementAddressLine1.sendKeys(address);
	}
	
	public void setCity(String cityToFill) {
		clickWhenClickable(editBox_BCAccountDisbursementCity);
		editBox_BCAccountDisbursementCity.sendKeys(Keys.chord(Keys.CONTROL + "a"), cityToFill);
	}
	
	public void setState(State stateToSelect) {
		Guidewire8Select commonState = select_BCAccountDisbursementState();
		commonState.selectByVisibleText(stateToSelect.getName());
	}
	
	public void setZip(String zipToFill) {
		clickWhenClickable(editBox_BCAccountDisbursementZip);
		editBox_BCAccountDisbursementZip.sendKeys(Keys.chord(Keys.CONTROL + "a"), zipToFill);
	}

	public void fillOutAddress(AddressInfo address) {
		setState(address.getState());
		
		setZip(address.getZip());
		
		setAddressLine1(address.getLine1());
		
		setCity(address.getCity());
	}

	public void setCreateAccountDisbursementWizardLoanNumber(String loanNumber) {
		select_CreateAccountDisbursementLoanNumber().selectByVisibleText(loanNumber);
		
	}

	public String getCreateAccountDisbursementWizardLoanNumber() {
		waitUntilElementIsVisible(label_CreateAccountDisbursementLoanNumber);
		String loanNumber = label_CreateAccountDisbursementLoanNumber.getText();
		return loanNumber;
	}

	public void setCreateAccountDisbursementLienHolderNumber(String lienHolderNumber) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementLienHolderNumber);
		editbox_CreateAccountDisbursementLienHolderNumber.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementLienHolderNumber.sendKeys(lienHolderNumber);
	}

	public void setCreateAccountDisbursementInsuredName(String insuredName) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementInsuredName);
		editbox_CreateAccountDisbursementInsuredName.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementInsuredName.sendKeys(insuredName);
	}

	public void setCreateAccountDisbursementWizardUnappliedFund(String unappliedFund) {
		select_CreateAccountDisbursementUnappliedFund().selectByVisibleText(unappliedFund);
		
	}

	public void setCreateAccountDisbursementWizardAmount(double amount) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementAmount);
		editbox_CreateAccountDisbursementAmount.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementAmount.sendKeys(String.valueOf(amount));
	}

	public void setCreateAccountDisbursementWizardDueDate(Date date) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementDueDate);
		editbox_CreateAccountDisbursementDueDate.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementDueDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
	}

	public void setCreateAccountDisbursementWizardReasonFor(DisbursementReason reason) {
		select_CreateAccountDisbursementReason().selectByVisibleText(reason.getValue());
	}

	public void setCreateAccountDisbursementWizardDisbursementDetail(String disbursementDetail) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementDisbursementDetail);
		editbox_CreateAccountDisbursementDisbursementDetail.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementDisbursementDetail.sendKeys(disbursementDetail);
	}

	public void setCreateAccountDisbursementWizardPolicyNumberDropDown(String policyNumber) {
		select_CreateAccountDisbursementPolicyNumber().selectByVisibleText(policyNumber);
	}

	public void setCreateAccountDisbursementWizardPolicyNumber(String policyNumber) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementPolicyNumber);
		editbox_CreateAccountDisbursementPolicyNumber.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementPolicyNumber.sendKeys(policyNumber);
	}

	public String getCreateAccountDisbursementWizardPolicyNumber() {
		String policyNumber = null;
		try {
			waitUntilElementIsVisible(editbox_CreateAccountDisbursementPolicyNumber, 3000);
			policyNumber = editbox_CreateAccountDisbursementPolicyNumber.getAttribute("value");
		} catch (Exception e) {
			waitUntilElementIsVisible(label_CreateAccountDisbursementPolicyNumber, 3000);
			policyNumber = label_CreateAccountDisbursementPolicyNumber.getText();
		}
		return policyNumber;
	}

	public void setCreateAccountDisbursementWizardPayTo(String payTo) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementPayTo);
		editbox_CreateAccountDisbursementPayTo.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementPayTo.sendKeys(String.valueOf(payTo));
	}

	public void setCreateAccountDisbursementWizardMailTo(String MailTo) {
		waitUntilElementIsVisible(editbox_CreateAccountDisbursementMailTo);
		editbox_CreateAccountDisbursementMailTo.sendKeys(Keys.CONTROL + "a");
		editbox_CreateAccountDisbursementMailTo.sendKeys(String.valueOf(MailTo));
	}

	public void createAccountDisbursement(double disbursementAmount, Date dueDate, DisbursementReason disbursementReason) {
		setCreateAccountDisbursementWizardAmount(disbursementAmount);
		setCreateAccountDisbursementWizardDueDate(dueDate);
		setCreateAccountDisbursementWizardReasonFor(disbursementReason);
		clickNext();
		
		clickFinish();
		
	}

	public void createAccountDisbursement(String policyNumber, String loanNumberOrUnapliedFund, double disbursementAmount, Date dueDate, DisbursementReason disbursementReason) {
		setCreateAccountDisbursementWizardPolicyNumberDropDown(policyNumber);
		InfoBar infoBar = new InfoBar(getDriver());
		if (infoBar.getInfoBarAccountNumber().startsWith("98")) {
			setCreateAccountDisbursementWizardLoanNumber(loanNumberOrUnapliedFund);
		} else {
			setCreateAccountDisbursementWizardUnappliedFund(loanNumberOrUnapliedFund);
		}
		setCreateAccountDisbursementWizardAmount(disbursementAmount);
		setCreateAccountDisbursementWizardDueDate(dueDate);
		setCreateAccountDisbursementWizardReasonFor(disbursementReason);
		sendArbitraryKeys(Keys.TAB);
		waitForPostBack();
		
		clickNext();
		
		clickFinish();
		
	}

}
