package repository.bc.wizards;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentInstrumentEnum;
import repository.gw.helpers.DateUtils;

import java.util.Date;

public class PaymentRequestWizard extends BasePage {
    private WebDriver driver;

    public PaymentRequestWizard(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public Guidewire8Select comboBox_PolicyNumber() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PaymentRequestDV:Policy-triggerWrap')]");
    }

    @FindBy(xpath = "//input[contains(@id,':PaymentRequestDV:amount-inputEl')]")
    public WebElement editbox_Amount;

    @FindBy(xpath = "//input[contains(@id,':PaymentRequestDV:paymentDate-inputEl')]")
    public WebElement editbox_DraftDate;    
    
    @FindBy(xpath = "//label[contains(@id,':PaymentRequestDV:PaymentInstrument-labelEl')]")
    public WebElement label_PaymentInstrument;

    public Guidewire8Select comboBox_PaymentInstrument() {
        return new Guidewire8Select(driver, "//table[contains(@id,':PaymentRequestDV:PaymentInstrument-triggerWrap')]");
    }

    public void setPolicyNumber(String policyNumber) {
        comboBox_PolicyNumber().selectByVisibleText(policyNumber);
    }

    public void setDraftDate(Date date) {
        editbox_DraftDate.sendKeys(Keys.CONTROL + "a");
        editbox_DraftDate.sendKeys(DateUtils.dateFormatAsString("MM/dd/yyyy", date));
    }

    public void setPaymentInstrument(PaymentInstrumentEnum paymentInstrument) {
        comboBox_PaymentInstrument().selectByVisibleText(paymentInstrument.getValue());
    }
    
    public String getPaymentInstrument() {
    	waitUntilElementIsVisible(label_PaymentInstrument);
        return comboBox_PaymentInstrument().getText();
    }


}
