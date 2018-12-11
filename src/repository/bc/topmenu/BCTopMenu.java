package repository.bc.topmenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class BCTopMenu extends BasePage {

	public BCTopMenu(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}

	// These are supposed to open drop down menus
	@FindBy(xpath = "//span[starts-with(@id,'TabBar:DesktopTab-btnWrap')]")
	public WebElement desktopTabArrow;

	@FindBy(xpath = "//span[starts-with(@id,'TabBar:SearchTab-btnWrap')]")
	public WebElement searchTabArrow;

	@FindBy(xpath = "//span[starts-with(@id,'TabBar:AdministrationTab-btnWrap')]")
	public WebElement administrationTabArrow;

	@FindBy(xpath = "//span[starts-with(@id,'TabBar:AccountsTab-btnWrap')]")
	public WebElement accountTabArrow;

	@FindBy(xpath = "//span[starts-with(@id,'TabBar:PoliciesTab-btnWrap')]")
	public WebElement policyTabArrow;

	@FindBy(xpath = "//a[starts-with(@id,'TabBar:DesktopTab')]")
	public WebElement desktopTab;

	@FindBy(xpath = "//a[starts-with(@id,'TabBar:SearchTab')]")
	public WebElement searchTab;

	@FindBy(xpath = "//a[starts-with(@id,'TabBar:AdministrationTab')]")
	public WebElement administrationTab;

	@FindBy(xpath = "//a[starts-with(@id,'TabBar:AccountsTab')]")
	public WebElement accountTab;

	@FindBy(xpath = "//a[starts-with(@id,'TabBar:PoliciesTab')]")
	public WebElement policyTab;

	@FindBy(xpath = "//div[@id=':tabs-innerCt']/div")
	public WebElement div_TopMenuBar;

	@FindBy(xpath = "//a[contains(@id,':notesflagid')]")
	public WebElement link_NotesFlag;

	public void clickDesktopArrow() {
		waitUntilElementIsClickable(desktopTabArrow);
		Actions mouse = new Actions(getDriver());
		mouse.moveToElement(desktopTabArrow, 80, 0);
		mouse.click();
		mouse.build().perform();
	}

	public void clickSearchArrow() {
		waitUntilElementIsClickable(searchTabArrow);
		Actions mouse = new Actions(getDriver());
		mouse.moveToElement(searchTabArrow, 70, 0);
		mouse.click();
		mouse.build().perform();
	}

	public void clickAdministrationArrow() {
		waitUntilElementIsClickable(administrationTabArrow);
		Actions mouse = new Actions(getDriver());
		mouse.moveToElement(administrationTabArrow, 110, 0);
		mouse.click();
		mouse.build().perform();
	}

	public void clickAccountArrow() {
		waitUntilElementIsClickable(accountTabArrow);
		Actions mouse = new Actions(getDriver());
		mouse.moveToElement(accountTabArrow, 80, 0);
		mouse.click();
		mouse.build().perform();
	}

	public void clickPolicyArrow() {
		waitUntilElementIsClickable(policyTabArrow);
		Actions mouse = new Actions(getDriver());
		mouse.moveToElement(policyTabArrow, 75, 0);
		mouse.click();
		mouse.build().perform();
	}

	public void clickDesktopTab() {
		clickWhenClickable(desktopTab);
		waitForPageLoad();
	}

	public void clickSearchTab() {
		clickWhenClickable(searchTab);
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

	public WebElement getTopMenuBar() {
		return div_TopMenuBar;
	}

	public boolean notesFlagExist() {
		try {
			link_NotesFlag.isDisplayed();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
