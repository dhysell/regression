package repository.pc.workorders.reinstate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.OkCancel;

import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ReinstateReason;
import repository.pc.actions.ActionsPC;
import repository.pc.workorders.generic.GenericWorkorder;

public class StartReinstate extends repository.pc.workorders.generic.GenericWorkorder {
    private WebDriver driver;

    public StartReinstate(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    private Guidewire8Select select_Reason() {
        return new Guidewire8Select(getDriver(), "//table[contains(@id, 'ReinstatePolicyDV:ReasonCode-triggerWrap')]");
    }

    @FindBy(xpath = "//textarea[contains(@id, 'ReinstatePolicyDV:ReasonDescription-inputEl')]")
    private WebElement editbox_Description;

    @FindBy(xpath = "//a[contains(@id, 'JobWizardToolbarButtonSet:Reinstate')]")
    public WebElement button_Reinstate;

    @FindBy(xpath = "//span[contains(@id, ':JobWizardToolbarButtonSet:Reinstate-btnEl')]")
    public WebElement button_ReinstateAlt;

    @FindBy(xpath = "//div[contains(@id, ':TermType-inputEl')]")
    public WebElement input_TermType;

    @FindBy(xpath = "//div[contains(@id, ':EffectiveDate-inputEl')]")
    public WebElement input_EffectiveDate;

    @FindBy(xpath = "//div[contains(@id, ':ExpirationDate-inputEl')]")
    public WebElement input_ExpiryDate;

    @FindBy(xpath = "//div[contains(@id, ':RateAsOfDate-inputEl')]")
    public WebElement input_RateAsOfDate;


    public void quoteAndIssue() {
        repository.pc.workorders.generic.GenericWorkorder genericWO = new GenericWorkorder(driver);
        genericWO.clickGenericWorkorderQuote();

        try {
            clickReinstatePolicy();
        } catch (Exception e) {
            genericWO.requestApproval("Approve Policy Reinstatement");
        }
    }


    public void clickReinstatePolicy() {
        try {
        	clickWhenClickable(button_Reinstate);
        } catch (Exception e) {
        	clickWhenClickable(button_ReinstateAlt);
        }
        selectOKOrCancelFromPopup(OkCancel.OK);
    }


    public void setReinstateReason(String reasonToSelect) {
        Guidewire8Select mySelect = select_Reason();
        mySelect.selectByVisibleText(reasonToSelect);
    }


    public void setDescription(String description) {
        waitUntilElementIsVisible(editbox_Description);
        editbox_Description.sendKeys(description);
    }


    public void reinstatePolicy(ReinstateReason reinstateReason, String description) {
        repository.pc.actions.ActionsPC actions = new ActionsPC(driver);
        actions.click_Actions();
        actions.click_ReinstatePolicy();

        setReinstateReason(reinstateReason.getValue());

        if (!(description == null)) {
            setDescription(description);
        }

        quoteAndIssue();
    }


    public String getStartReinstatementTermType() {
        return input_TermType.getText();
    }


    public String getStartReinstatementEffectiveDate() {
        return input_EffectiveDate.getText();
    }


    public String getStartReinstatementExpirationDate() {
        return input_ExpiryDate.getText();
    }


    public String getStartReinstatementRateAsOfDate() {
        return input_RateAsOfDate.getText();
    }

}
