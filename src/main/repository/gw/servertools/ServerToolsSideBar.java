package repository.gw.servertools;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;

public class ServerToolsSideBar extends BasePage {

    public ServerToolsSideBar(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    // ------------------------------------
    // "Recorded Elements" and their XPaths
    // ------------------------------------

    /////////////////////////////////////
    //Elements for the Actions Dropdown//
    /////////////////////////////////////

    @FindBy(xpath = "//span[contains(@id, 'ServerTools:InternalToolsMenuActions-btnInnerEl')]")
    public WebElement button_ServerToolsSideMenuActions;

    @FindBy(xpath = "//div[contains(@id, 'ServerTools:InternalToolsMenuActions:ReturnToApp')]")
    public WebElement link_ServerToolsSideMenuActionsReturnToApplication;

    @FindBy(xpath = "//div[contains(@id, 'ServerTools:InternalToolsMenuActions:InternalToolsMenuActions_Logout')]")
    public WebElement link_ServerToolsSideMenuActionsLogOut;

    //////////////////////////////////////////
    //Elements for the rest of the side menu//
    //////////////////////////////////////////

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_BatchProcessInfo')]/parent::tr")
    public WebElement link_ServerToolsSideMenuBatchProcessInfo;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_WorkQueueInfo')]/parent::tr")
    public WebElement link_ServerToolsSideMenuWorkQueueInfo;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_SetLogLevel')]/parent::tr")
    public WebElement link_ServerToolsSideMenuSetLogLevel;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_ViewLogs')]/parent::tr")
    public WebElement link_ServerToolsSideMenuViewLogs;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_InfoPages')]/parent::tr")
    public WebElement link_ServerToolsSideMenuInfoPages;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_MBeans')]/parent::tr")
    public WebElement link_ServerToolsSideMenuManagementBeans;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_StartablePlugin')]/parent::tr")
    public WebElement link_ServerToolsSideMenuStartablePlugin;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_CentipedeCacheInfoLG')]/parent::tr")
    public WebElement link_ServerToolsSideMenuCacheInfo;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_Profiler')]/parent::tr")
    public WebElement link_ServerToolsSideMenuGuidewireProfiler;

    @FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_ProductModelInfo')]/parent::tr")
    public WebElement link_ServerToolsSideMenuProductModelInfo;

	@FindBy(xpath = "//td[contains(@id,'ServerTools:MenuLinks:ServerTools_PLEntityBuilder')]/parent::tr")
	public WebElement link_ServerToolsSideMenuEntityBuilder;
	
    // ---------------------------------
    // Helper Methods for Above Elements
    // ---------------------------------


    public void clickServerToolsSideMenuActions() {
    	clickWhenClickable(button_ServerToolsSideMenuActions);
    }


    public void returnToApplication() {
        clickServerToolsSideMenuActions();
        clickWhenClickable(link_ServerToolsSideMenuActionsReturnToApplication);
    }


    public void logOutOfApplication() {
        clickServerToolsSideMenuActions();
        clickWhenClickable(link_ServerToolsSideMenuActionsLogOut);
    }


    public void clickBatchProcessInfo() {
    	clickWhenClickable(link_ServerToolsSideMenuBatchProcessInfo);
    }


    public void clickWorkQueueInfo() {
        clickWhenClickable(link_ServerToolsSideMenuWorkQueueInfo);
    }


    public void clickSetLogLevel() {
    	clickWhenClickable(link_ServerToolsSideMenuSetLogLevel);
    }


    public void clickViewLogs() {
    	clickWhenClickable(link_ServerToolsSideMenuViewLogs);
    }


    public void clickInfoPages() {
    	clickWhenClickable(link_ServerToolsSideMenuInfoPages);
    }


    public void clickManagementBeans() {
    	clickWhenClickable(link_ServerToolsSideMenuManagementBeans);
    }


    public void clickStartablePlugin() {
    	clickWhenClickable(link_ServerToolsSideMenuStartablePlugin);
    }


    public void clickCacheInfo() {
    	clickWhenClickable(link_ServerToolsSideMenuCacheInfo);
    }


    public void clickGuidewireProfiler() {
    	clickWhenClickable(link_ServerToolsSideMenuGuidewireProfiler);
    }


    public void clickProductModelInfo() {
    	clickWhenClickable(link_ServerToolsSideMenuProductModelInfo);
    }
	
	public void clickEntityBuilder() {
		clickWhenClickable(link_ServerToolsSideMenuEntityBuilder);
}
}
