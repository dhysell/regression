package repository.bc.wizards;

import com.idfbins.enums.State;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Checkbox;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.BankAccountType;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.generate.custom.BankAccountInfo;
import repository.gw.helpers.StringsUtils;

import java.util.Scanner;

public class NewPaymentInstrumentWizard extends BasePage{
	 private WebDriver driver;

	    public NewPaymentInstrumentWizard(WebDriver driver) {
	        super(driver);
	        this.driver = driver;
	        PageFactory.initElements(driver, this);
	    }
	    private Guidewire8Select select_PaymentMethod() {
	        return new Guidewire8Select(driver, "//table[contains(@id,':PaymentMethod-triggerWrap')]");
	    }
	    private Guidewire8Checkbox checkBox_CopyPrimaryContactDetails() {
	        return new Guidewire8Checkbox(driver, "//table[contains(@id,'PaymentInstrumentContactInputSet:CopyPrimaryContact')]");
	    }

	    private Guidewire8Select select_BankAccountType() {
	        return new Guidewire8Select(driver, "//table[(contains(@id,'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:BankAccountType-triggerWrap')) or (contains(@id, 'NewPaymentInfoPopup:PaymentInfoInputSet:BankAccountType-triggerWrap')) or (contains(@id, 'NewPaymentInstrumentACHInputSet:accountTypeDropdown-triggerWrap'))]");
	    }

	    @FindBy(xpath = "//input[(contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:BankABANumber-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:PaymentInfoInputSet:BankABANumber-inputEl')) or (contains(@id, 'routingNumberInput-inputEl'))]")
	    private WebElement editBox_BankRoutingNumber;
	    
	    @FindBy(xpath = "//input[contains(@id, 'AccountNumber-inputEl') or contains(@id, ':AccountCriterion-inputEl') or contains(@id, ':AccountNumberCriterion-inputEl') or contains(@id, 'accountNumberInput-inputEl') or contains(@id, ':AccountNumber-inputEl')]")
	    private WebElement editBox_BankAccountNumber;
	    
	    @FindBy(xpath = "//input[contains(@id, 'bankAccountHolderInput-inputEl')]")
	    private WebElement BankAccountHolderName;
	    
	    @FindBy(xpath = "//*[(contains(@id, 'NewDownPaymentMethodPopup:DownPaymentMethodInputSet:BankInfo-inputEl')) or (contains(@id, 'NewPaymentInfoPopup:PaymentInfoInputSet:BankInfo-inputEl')) or (contains(@id, 'BankInfo-inputEl'))]")
	    private WebElement text_BankNameAddress;
	  
	    public void selectPaymentMethod(PaymentInstrumentEnum paymentMethod) {
	        Guidewire8Select mySelect = select_PaymentMethod();
	        mySelect.selectByVisibleText(paymentMethod.getValue());
	    }
	    public void selectAccountType(BankAccountType accountType) {
	        Guidewire8Select mySelect = select_BankAccountType();
	        mySelect.selectByVisibleText(accountType.getValue());
	    }
	    public void setBankAccountHolderName(String bankAccountHolderName) {
	        clickWhenClickable(BankAccountHolderName);
	        BankAccountHolderName.sendKeys(Keys.chord(Keys.CONTROL + "a"));
	        BankAccountHolderName.sendKeys(bankAccountHolderName);
	        clickProductLogo();
	    }
	    
	    public void setRoutingNumber(String routingNumber){
	    	waitUntilElementIsVisible(editBox_BankRoutingNumber);
	        editBox_BankRoutingNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
	        editBox_BankRoutingNumber.sendKeys(routingNumber);
	        clickProductLogo();
	    }
	    public void setAccountNumber(String accountNumber){
	    	waitUntilElementIsVisible(editBox_BankAccountNumber);
	    	editBox_BankAccountNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
	    	editBox_BankAccountNumber.sendKeys(accountNumber);
	        clickProductLogo();
	    }
	    public void setCopyPrimaryContactDetailsCheckBox(boolean trueOrFalse) {
	    	checkBox_CopyPrimaryContactDetails().select(trueOrFalse);
	    }
	    
	    public void clickOK(){
	    	super.clickOK();
	    }
	    public void clickCancel(){
	    	super.clickCancel();
	    }	
	    public String getBankNameAddress(){
	    	return text_BankNameAddress.getText();
	    }	
	    
	    public void setPaymentInfo(BankAccountInfo bankAccountInfo) {
	        String bankCityStateZip = null;

	        Guidewire8Select mySelect = select_BankAccountType();
	        mySelect.selectByVisibleText(bankAccountInfo.getBankAccountType().getValue());

	        waitUntilElementIsVisible(editBox_BankRoutingNumber);
	        editBox_BankRoutingNumber.sendKeys(Keys.chord(Keys.CONTROL + "a"));
	        editBox_BankRoutingNumber.sendKeys(bankAccountInfo.getRoutingNumber());
	        clickProductLogo();

	        waitUntilElementIsVisible(text_BankNameAddress);

	        int i = 1;
	        Scanner scanner = new Scanner(text_BankNameAddress.getText());
	        while (scanner.hasNextLine()) {
	            String line = scanner.nextLine();
	            if (i == 1) {
	                bankAccountInfo.setBankName(line);
	            } else if (i == 2) {
	                bankAccountInfo.setBankAddress(line);
	            } else if (i == 3) {
	                bankCityStateZip = line;
	            }
	            i++;
	        }
	        scanner.close();

	        String[] cityStateZipSplit = StringsUtils.cityStateZipParser(bankCityStateZip);
	        bankAccountInfo.setBankCity(cityStateZipSplit[0]);
	        bankAccountInfo.setBankState(State.valueOfAbbreviation(cityStateZipSplit[1]));
	        bankAccountInfo.setBankZip(cityStateZipSplit[2]);

	        editBox_BankAccountNumber.sendKeys(bankAccountInfo.getAccountNumber());
	        clickProductLogo();
	    }
}
