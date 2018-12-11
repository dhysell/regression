package repository.ab.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TopMenuSearchAB extends TopMenuAB {

    private WebDriver driver;

    public TopMenuSearchAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(@id, 'TabBar:SearchTab-btnEl') or contains(@id, 'TabBar:ContactsTab-btnInnerEl')]")
    public WebElement button_TopMenuSearchSearch;

    @FindBy(xpath = "//a[@id='TabBar:ContactsTab:ABContacts_MergeContacts-itemEl']")
    public WebElement Merge_Contacts;

    @FindBy(xpath = "//a[@id='TabBar:ContactsTab:ABContacts_PendingChanges-itemEl']")
    public WebElement Pending_Changes;

    @FindBy(xpath = "//a[@id='TabBar:ContactsTab:ABContacts_RetiredContacts-itemEl']")
    public WebElement Retired_Contacts;


    public void hoverOver(WebElement element) {
        waitUntilElementIsVisible(element);
        Actions mouse = new Actions(driver);
        mouse.moveToElement(element);
        mouse.build().perform();
    }

    public void clickSearch() {
        clickWhenClickable(button_TopMenuSearchSearch);
    }

    public void clickMergeContacts() {
//        clickSearchArrow();
        clickWhenVisible(Merge_Contacts);
    }

    public void clickRetiredContacts() {
 //       clickSearchArrow();
        clickWhenVisible(Retired_Contacts);
    }


    public void clickPendingChanges() {
        // TODO Auto-generated method stub

    }

}
