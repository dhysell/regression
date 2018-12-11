package repository.ab.topmenu;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import persistence.globaldatarepo.entities.AbUsers;
import repository.driverConfiguration.BasePage;
import repository.gw.login.Login;

public class TopMenuAB extends BasePage {
    private WebDriver driver;
    
    public TopMenuAB(WebDriver driver) {
        super(driver);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // These are supposed to open drop down menus
    @FindBy(xpath = "//span[starts-with(@id,'TabBar:DesktopTab-btnWrap')]")
    private WebElement desktopTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:ContactsTab-btnWrap')]")
    private WebElement searchTabArrow;

    @FindBy(xpath = "//span[starts-with(@id,'TabBar:AdminTab-btnWrap')]")
    private WebElement administrationTabArrow;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:DesktopTab')]")
    private WebElement desktopTab;

    @FindBy(css = "span[id='TabBar:SearchTab-btnEl']")
    private WebElement searchTab;

    @FindBy(xpath = "//a[starts-with(@id,'TabBar:AdminTab')]")
    private WebElement administrationTab;

    private void login(AbUsers user) {
    	Login login = new Login(driver);
    	login.login(user.getUserName(), user.getUserPassword());
    }
    
    public void clickDesktopArrow(AbUsers user) {
    	login(user);
        waitUntilElementIsClickable(desktopTabArrow);
        Actions mouse = new Actions(driver);
        mouse.moveToElement(desktopTabArrow, 80, 0);
        mouse.click();
        mouse.build().perform();
    }
/* No longer exists.
    private void clickSearchArrow() {
        waitUntilElementIsClickable(searchTabArrow);
        Actions mouse = new Actions(driver);
        mouse.moveToElement(searchTabArrow, 80, 0);
        mouse.click();
        mouse.build().perform();
    }
*/
/*  No longer exists  
    public void clickAdministrationArrow() {
        waitUntilElementIsClickable(administrationTabArrow);
        Actions mouse = new Actions(driver);
        mouse.moveToElement(administrationTabArrow, 110, 0);
        mouse.click();
        mouse.build().perform();
    }
*/
    
    public void clickDesktopTab(AbUsers user) {
    	login(user);
        clickWhenClickable(desktopTab);
    }
    
    public void clickDesktopTab() {
        clickWhenClickable(desktopTab);
    }

    public void clickSearchTab() {
    	waitUntilElementIsClickable(searchTab);
        clickWhenClickable(searchTab);
    }

    public void clickSearchTab(AbUsers user) {
    	login(user);
    	waitUntilElementIsClickable(searchTab);
        clickWhenClickable(searchTab);
    }

    public void clickAdministrationTab(AbUsers user) {
    	login(user);
        clickWhenClickable(administrationTab);
    }
    
    public void clickAdministrationTab() {
        clickWhenClickable(administrationTab);
    }
}
