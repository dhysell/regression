package repository.bc.policy.summary;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PaymentRestriction;

public class PolicySummarySelectPaymentRestriction extends BasePage {


    private WebDriver driver;

    public PolicySummarySelectPaymentRestriction(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////

    Guidewire8Select select_PaymentRestriction() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':OverridingPaymentRestriction-triggerWrap')]");
    }

    //////////////////////////////////////
    // Helper Methods for Above Elements//
    //////////////////////////////////////

    public void setPaymentRestriction(PaymentRestriction paymentRestriction) {
        Guidewire8Select mySelect = select_PaymentRestriction();
        mySelect.selectByVisibleText(paymentRestriction.getValue());
    }
}
