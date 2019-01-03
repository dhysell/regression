package repository.bc.policy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.bc.common.BCCommonNotes;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.PolicyNotes.Topic;
import repository.gw.helpers.GuidewireHelpers;

public class PolicyNotes extends BCCommonNotes {

    private WebDriver driver;

    public PolicyNotes(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //////////////////////////////////////////
    // "Recorded Elements" and their XPaths //
    //////////////////////////////////////////

    Guidewire8Select select_Topic() {
        return new Guidewire8Select(driver, "//table[contains(@id, ':NoteSearchDV:Topic-triggerWrap')]");
    }

    @FindBy(xpath = "//a[contains(@id,':SearchLinksNotesInputSet:ViewPCNotes')]")
    public WebElement button_ViewInPolicyCenter;


    //////////////////////////////////////
    // Helper Methods for Above Elements//
    //////////////////////////////////////

    public void selectTopic(Topic topic) {
        select_Topic().selectByVisibleText(topic.getValue());
    }

    public String clickViewInPolicyCenterButton() {
        clickWhenClickable(button_ViewInPolicyCenter, 1000);
        GuidewireHelpers guideWireHelpers = new GuidewireHelpers(driver);
        return guideWireHelpers.switcWebDriverWindow("Guidewire PolicyCenter (Service Account)");
    }
}
