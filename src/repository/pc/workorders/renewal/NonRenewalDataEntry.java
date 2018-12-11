package repository.pc.workorders.renewal;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import com.idfbins.enums.OkCancel;

import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;

public class NonRenewalDataEntry extends BasePage {


    private WebDriver driver;

    public NonRenewalDataEntry(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    private Guidewire8Select select_Source() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NonRenewSource-triggerWrap')]");
    }

    private Guidewire8Select select_Reason() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NonRenewReason-triggerWrap')]");
    }

    private Guidewire8Select select_Explanation() {
        return new Guidewire8Select(driver, "//table[contains(@id, 'NonRenewReasonExplanation-triggerWrap')]");
    }
    

    public void setNonRenewSource(String sourceToSelect) {
        Guidewire8Select source = select_Source();
        source.selectByVisibleText(sourceToSelect);
    }
    
    public void setNonRenewReason(String reasonToSelect) {
        Guidewire8Select reason = select_Reason();
        reason.selectByVisibleText(reasonToSelect);
    }

    public void setNonRenewExplanation(String explanationToSelect) {
        Guidewire8Select explanation = select_Explanation();
        explanation.selectByVisibleText(explanationToSelect);
    }
    
    public void setNonRenewReasonExplanation(String source, String reason, String explanation){
      setNonRenewSource(source);
      setNonRenewReason(reason);
      setNonRenewExplanation(explanation);
      clickOK();
      super.selectOKOrCancelFromPopup(OkCancel.OK);
    }
}