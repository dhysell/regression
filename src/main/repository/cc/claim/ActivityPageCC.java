package repository.cc.claim;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.elements.Guidewire8Select;
import repository.gw.enums.ClaimsUsers;
import repository.gw.helpers.WaitUtils;

public class ActivityPageCC extends BasePage {

    private WebDriver driver;
    private WaitUtils waitUtils;

    public ActivityPageCC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        this.waitUtils = new WaitUtils(driver);
        PageFactory.initElements(driver, this);
    }

    /////////////////////////// Webdriver Elements ///////////////////////////

    @FindBy(xpath = "//a[contains(@id,':NewActivity_UpdateButton')]")
    private WebElement button_Update;

    @FindBy(xpath = "//input[contains(@id, ':Activity_Subject')]")
    private WebElement input_ActivitySubject;

    @FindBy(xpath = "//textarea[contains(@id,':Activity_Description-inputEl')]")
    private WebElement textarea_ActivityDescription;

    @FindBy(xpath = "//input[contains(@id,':Activity_RelatedTo')]")
    private WebElement input_ActivityRelatedTo;

    private Guidewire8Select select_ActivityAssignTo() {
        return new Guidewire8Select(driver,"//table[contains(@id,'Activity_AssignActivity-triggerWrap')]");
    }

    /////////////////////////// Helper Methods ///////////////////////////

    private void inputDescriptionText(String text) {
        textarea_ActivityDescription.sendKeys(text);
    }

    private void selectSpecific_AssignTo(String item) {
        Guidewire8Select mySelect = select_ActivityAssignTo();
        mySelect.selectByVisibleTextPartial(item);
    }

    public String sendVerifyCoverageActivityTo(ClaimsUsers user) {

        waitUtils.waitUntilElementIsClickable(input_ActivitySubject);

        String descriptionText = "This is text for the activity description";
        inputDescriptionText(descriptionText);

        selectSpecific_AssignTo(user.getName());
        String subject = input_ActivitySubject.getAttribute("value");

        clickWhenClickable(button_Update);

        return subject;
    }

}
