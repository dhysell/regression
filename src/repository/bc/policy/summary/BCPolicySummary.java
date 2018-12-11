package repository.bc.policy.summary;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonSummary;
import repository.bc.wizards.NewPaymentInstrumentWizard;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.enums.PaymentRestriction;
import repository.gw.enums.SecurityZone;
import repository.gw.enums.TransactionFilter;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;

public class BCPolicySummary extends BCCommonSummary {

    private WebDriver driver;

    public BCPolicySummary(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    /////////////////////////
    // xPaths for Elements //
    /////////////////////////

    @FindBy(xpath = "//div[contains(@id,'PolicyDetailSummaryScreen:TransactionDetailsLV')]")
    private WebElement table_PolicyRecentTransactions;

    @FindBy(xpath = "//div[contains(@id,'PolicyDetailSummaryScreen:1')]")
    private WebElement table_PolicyOpenActivities;

    @FindBy(xpath = "//div[contains(@id, ':PolicyCharges-inputEl')]")
    public WebElement label_PremiumCharges;

    @FindBy(xpath = "//div[contains(@id,':PolicyDetailDV:PaymentRestriction-inputEl')]")
    public WebElement label_PaymentRestrictionValue;

    @FindBy(xpath = "//div[contains(@id, 'PolicyDetailDV:CancellationStatus-inputEl')]")
    public WebElement label_CancellationStatus;

    @FindBy(xpath = "//div[contains(@id, 'PolicyDetailSummary:PolicyDetailSummaryScreen:PolicyDetailDV:DefaultPaymentInstrument-inputEl') or contains(@id, 'AccountDetailSummary:AccountDetailSummaryScreen:AccountDetailDV:DefaultPaymentInstrument-inputEl')]")
    private WebElement labelOrLink_PaymentInstrumentWhenViewing;

    @FindBy(xpath = "//div[contains(@id, ':PaymentPlan-inputEl')]")
    public WebElement link_PaymentPlan;

    @FindBy(xpath = "//div[contains(@id,':PaymentRestriction:ChangePaymentRestrictionAction')]")
    public WebElement link_PaymentRestrictionUpdate;

    @FindBy(xpath = "//div[contains(@id, ':ChangeDirectBillInvoicingOverrides')]")
    public WebElement link_InvoiceOverridesUpdate;

    @FindBy(xpath = "//a[contains(@id,':DefaultPaymentInstrument:CreateNewPaymentInstrument')]")
    private WebElement link_NewPaymentInstrument;

    @FindBy(xpath = "//div[contains(@id, ':ChangeToAgencyBillAction')]")
    public WebElement link_ChangeToAgencyBill;

    @FindBy(xpath = "//div[contains(@id, ':BillingMethod:changeToListBill')]")
    public WebElement link_ChangeToListBill;

    @FindBy(xpath = "//span[contains(@id, ':StartDelinquencyButton-btnEl')]")
    public WebElement button_StartDeliquency;

    @FindBy(xpath = "//a[contains(@id, ':ChangeInvoivingOverridesMenuIcon')]")
    public WebElement button_InvoiceOverrideArrow;

    @FindBy(xpath = "//a[contains(@id, ':PaymentRestriction:PaymentRestrictionMenuIcon')]")
    public WebElement button_PaymentRestrictionArrow;

    @FindBy(xpath = "//a[contains(@id, ':BillingMethodMenuIcon')]")
    public WebElement button_BillingMethodArrow;

    @FindBy(xpath = "//span[@id = 'PolicyGroup:PolicyInfoBar:policyLeadFlagid-btnInnerEl']")
    public WebElement flag_PolicyLeadFlag;

    @FindBy(xpath = "//span[@id = 'PolicyGroup:PolicyInfoBar:policyOverrriddenFlagid-btnInnerEl']")
    public WebElement flag_PolicyOverridenFlag;

    @FindBy(xpath = "//span[@id = 'PolicyGroup:PolicyInfoBar:policyCashOnlyFlagid-btnInnerEl']")
    public WebElement flag_PolicyCashOnlyStatusFlag;

	@FindBy(xpath = "//div[@id='PolicyDetailSummary:PolicyDetailSummaryScreen:PolicyDetailDV:PolicyNumber-inputEl']")
	public WebElement label_PolicyNumber;
	
    Guidewire8Select select_RecentTransactionsShow() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':TransactionDetailsLV:Filter-triggerWrap')]");
    }

    Guidewire8Select select_SecurityZone() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':PolicyDetailDV:SecurityZone-triggerWrap')]");
    }

    Guidewire8Select select_PaymentInstrument() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PolicyDetailDV:DefaultPaymentInstrument-triggerWrap')]");
    }

    Guidewire8Select select_PaymentRestriction() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'PolicyDetailDV:PaymentRestriction-triggerWrap') or contains(@id, 'OverridingPaymentRestriction-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id, 'bankAccountHolderInput-inputEl')]")
    private WebElement editbox_BCPolicySummaryBankAccountHolderName;

    @FindBy(xpath = "//a[contains(@id, 'PaymentInstrument:CreateNewPaymentInstrument')]")
    private WebElement button_BCPolicySummaryNewPaymentIntrument;
      
    @FindBy(xpath = "//*[(contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:BankInfo-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:PaymentInfoInputSet:BankInfo-inputEl')) or (contains(@id, 'BankInfo-inputEl'))]")
    private WebElement text_BankNameAddress;

    ////////////////////
    // Helper Methods //
    ////////////////////

    public void clickNewPaymentInstrument() {
        clickWhenClickable(button_BCPolicySummaryNewPaymentIntrument);
    }

   
    public void setBankAccountHolderName(String bankAccountHolderName) {
        clickWhenClickable(editbox_BCPolicySummaryBankAccountHolderName);
        editbox_BCPolicySummaryBankAccountHolderName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
        editbox_BCPolicySummaryBankAccountHolderName.sendKeys(bankAccountHolderName);
        clickProductLogo();
    }

   public WebElement getPolicyRecentTransactionsTable() {
        waitUntilElementIsVisible(table_PolicyRecentTransactions);
        return table_PolicyRecentTransactions;
    }

    public double getPremiumCharges() {
        return NumberUtils.getCurrencyValueFromElement(label_PremiumCharges);
    }

    public void clickInvoiceOverridesArrow() {
        button_InvoiceOverrideArrow.click();
    }

    public void clickInvoiceOverridesUpdate() {
        link_InvoiceOverridesUpdate.click();
    }

    public void updateInvoicingOverride() {
        clickInvoiceOverridesArrow();
        
        clickInvoiceOverridesUpdate();
    }

    public void clickPaymentRestrictionArrow() {
        button_PaymentRestrictionArrow.click();
    }

    public void clickPaymentRestrictionUpdate() {
        link_PaymentRestrictionUpdate.click();
    }

    public void updatePaymentRestriction() {
        clickPaymentRestrictionArrow();
        
        clickPaymentRestrictionUpdate();
    }

    public PaymentRestriction getPaymentRestrictionValue() {
        return PaymentRestriction.valueOf(label_PaymentRestrictionValue.getText());
    }

    public void selectPaymentRestriction(PaymentRestriction paymentRestriction) {
        updatePaymentRestriction();
        repository.bc.policy.summary.PolicySummarySelectPaymentRestriction restriction = new PolicySummarySelectPaymentRestriction(getDriver());
        restriction.setPaymentRestriction(paymentRestriction);
        restriction.clickNext();
        restriction.clickFinish();
    }

    public void setPaymentRestriction(PaymentRestriction paymentRestriction) {
        Guidewire8Select mySelect = select_PaymentRestriction();
        mySelect.selectByVisibleText(paymentRestriction.getValue());
    }

    public void setPaymentInstrumentToResponsive() {
        Guidewire8Select mySelect = select_PaymentInstrument();
        mySelect.selectByVisibleText("Responsive");
    }

    public void clickPaymentInstrumentNew() {
        clickWhenVisible(link_NewPaymentInstrument);
    }

    public void selectPaymentInstrument(String paymentInstrument) {
        select_PaymentInstrument().selectByVisibleTextPartial(paymentInstrument);
    }

   

    public void setNewPaymentInstrument() {
        clickNewPaymentInstrument();
        NewPaymentInstrumentWizard wizard = new NewPaymentInstrumentWizard(driver);
        wizard.selectPaymentMethod(PaymentInstrumentEnum.ACH_EFT);
        wizard.setPaymentInfo(new BankAccountInfo());
        setBankAccountHolderName("Someone");
        wizard.setCopyPrimaryContactDetailsCheckBox(true);
        clickOK();
        waitForPostBack();
    }

    // This method only exists for a negative check. This button should no longer exist on the the policy summary page.

    public void clickStartDeliquency() {
        button_StartDeliquency.click();
    }

    public String getPolicyCancellationStatus() {
        return label_CancellationStatus.getText();
    }

    public String getPaymentInstrumentValue() {
        return waitUntilElementIsVisible(labelOrLink_PaymentInstrumentWhenViewing).getText();
    }

    public void clickBillingMethodArrow() {
        button_BillingMethodArrow.click();
    }

    public void clickChangeToAgencyBill() {
        link_ChangeToAgencyBill.click();
    }

    public void clickChangeToListBill() {
        link_ChangeToListBill.click();
    }

    public void setRecentTransactionsShowFilter(TransactionFilter filter) {
        Guidewire8Select mySelect = select_RecentTransactionsShow();
        mySelect.selectByVisibleText(filter.getValue());
    }

    public void setSecurityZone(SecurityZone zone) {
        Guidewire8Select mySelect = select_SecurityZone();
        mySelect.selectByVisibleText(zone.getValue());
    }

    // when a policy has other policies overridden to its invoice stream, a
    // green checkmark marker displays

    public boolean policyLeadFlagExist() {
        return super.checkIfElementExists(flag_PolicyLeadFlag, 1000);
    }

    // when a policy is overridden, a blue arrow marker displays

    public boolean policyOverridenFlagExist() {
        return super.checkIfElementExists(flag_PolicyOverridenFlag, 1000);
    }

    // when a policy's Policy Restriction is Cash Only, this flag displays

    public boolean policyCashOnlyStatusFlagExist() {
        return super.checkIfElementExists(flag_PolicyCashOnlyStatusFlag, 1000);
    }

    public boolean checkIfPaymentPlanLinkIsActive() {
        return new GuidewireHelpers(getDriver()).checkIfLinkIsActive(link_PaymentPlan);
    }

    public String getPaymentPlanType() {
        return link_PaymentPlan.getText();
    }

    public void overrideInvoiceStream(String leadPolicy) {
        updateInvoicingOverride();
        
        PolicySummaryInvoicingOverrides invoiceStreamOverride = new PolicySummaryInvoicingOverrides(getDriver());
        invoiceStreamOverride.selectOverridingInvoiceStream(leadPolicy);
        
        invoiceStreamOverride.clickNext();
        
        invoiceStreamOverride.clickFinish();
        
    }

    public void reverseInvoiceStreamOverride() {
        updateInvoicingOverride();
        
        PolicySummaryInvoicingOverrides invoicingStreamOverride = new PolicySummaryInvoicingOverrides(getDriver());
        invoicingStreamOverride.selectOverridingInvoiceStream("<none>");
        
        invoicingStreamOverride.clickNext();
        
        invoicingStreamOverride.clickFinish();
        
    }

	public String getPolicyNumber() {
		return waitUntilElementIsVisible(label_PolicyNumber).getText();
	}

}
