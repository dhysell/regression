package production.thunderhead.repository.topMenu;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import repository.driverConfiguration.BasePage;

public class TopMenu extends BasePage {	
    public TopMenu(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

	// ------------------------------------
	// "Recorded Elements" and their XPaths
	// ------------------------------------
	@FindBy(xpath="//a[@href='/thunderhead/spring/admin.jsp?method=overview']")
	public WebElement tab_MenuOverview;
	
	@FindBy(xpath="//a[@href='/thunderhead/spring/search.jsp']")
	public WebElement tab_MenuSearch;
	
	@FindBy(xpath="//a[@href='/thunderhead/spring/admin.jsp?method=batches']")
	public WebElement tab_MenuBatches;
	
	@FindBy(xpath="//a[@href='/thunderhead/spring/admin.jsp?method=openSpools']")
	public WebElement tab_MenuSpools;
	
	@FindBy(xpath="//a[@href='/thunderhead/spring/admin.jsp?method=storedEnvelopes']")
	public WebElement tab_MenuStoredEnvelopes;
	
	@FindBy(xpath="//a[@href='/thunderhead/about.jsp']")
	public WebElement tab_MenuAbout;
	
	@FindBy(xpath="//a[@href='/thunderhead/logout.jsp']")
	public WebElement tab_MenuLogout;
	
	// ---------------------------------
	// Helper Methods for Above Elements
	// ---------------------------------
	public void clickMenuBatches() {
		clickWhenClickable(tab_MenuBatches);
	}
}

