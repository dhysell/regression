package repository.pc.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.pc.search.SearchSubmissionsPC;

public class TopMenuPC extends BasePage {

    private WebDriver driver;

    public TopMenuPC(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // These are supposed to open drop down menus
    @FindBy(xpath = "//span[starts-with(@id,'TabBar:DesktopTab-btnWrap')]")
    public WebElement desktopTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:SearchTab-btnWrap')]")
    public WebElement searchTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:AdminTab-btnWrap')]")
    public WebElement administrationTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:AccountTab-btnWrap')]")
    public WebElement accountTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:PolicyTab-btnWrap')]")
    public WebElement policyTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:ContactTab-btnWrap')]")
    public WebElement contactTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:ReinsuranceTab-btnWrap')]")
    public WebElement reinsuranceTabArrow;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:DesktopTab')]")
    public WebElement desktopTab;

    @FindBy(xpath = "//span[@id='TabBar:SearchTab-btnEl']")
    public WebElement searchTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:TeamTab')]")
    public WebElement teamTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab')]")
    public WebElement administrationTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AccountTab')]")
    public WebElement accountTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:PolicyTab')]")
    public WebElement policyTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:PoliciesTab')]")
    public WebElement policiesTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:ContactTab')]")
    public WebElement contactTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:ReinsuranceTab')]")
    public WebElement reinsuranceTab;

    @FindBy(css = "a[id*='SubmissionSearch']")
    private WebElement submissionSearch;


    public void clickDesktopArrow() {
    	clickElementByCordinates(desktopTabArrow, 80, 0);
    }


    public void clickSearchArrow() {
    	clickElementByCordinates(searchTabArrow, 80, 0);
    }


    public void clickAdministrationArrow() {
    	clickElementByCordinates(administrationTabArrow, 110, 0);
    }


    public void clickAccountArrow() {
    	clickElementByCordinates(accountTabArrow, 80, 0);
    }


    public void clickPolicyArrow() {
    	clickElementByCordinates(policyTabArrow, 80, 0);
    }


    public void clickContactArrow() {
    	clickElementByCordinates(contactTabArrow, 80, 0);
    }


    public void clickReinsuranceArrow() {
    	clickElementByCordinates(reinsuranceTabArrow, 80, 0);
    }


    public void clickDesktopTab() {
        clickWhenClickable(desktopTab);
    }


    public void clickSearchTab() {
        clickWhenClickable(searchTab);
    }


    public void clickTeamTab() {
        clickWhenClickable(teamTab);
    }


    public void clickAdministrationTab() {
        clickWhenClickable(administrationTab);
    }


    public void clickAccountTab() {
        clickWhenClickable(accountTab);
    }


    public void clickPolicyTab() {
        clickWhenClickable(policyTab);
    }

    public void clickPoliciesTab() {
        clickWhenClickable(policiesTab);
    }


    public void clickContactTab() {
        clickWhenClickable(contactTab);
    }


    public void clickReinsuranceTab() {
        clickWhenClickable(reinsuranceTab);
    }


    public repository.pc.search.SearchSubmissionsPC searchSubmission() {
        clickSearchArrow();
        clickWhenClickable(submissionSearch);
        waitForPostBack();
        return new SearchSubmissionsPC(getDriver());
    }
}
