package repository.gw.servertools;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import repository.driverConfiguration.BasePage;
import repository.gw.enums.StartablePlugin;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;

public class ServerToolsStartablePlugin extends BasePage{
	
	public ServerToolsStartablePlugin(WebDriver driver) {
		super(driver);
		PageFactory.initElements(driver, this);
	}
	
	@FindBy(xpath = "//div[@id = 'StartablePlugin:1']")
	private WebElement tableDiv_StartablePlugin;
	 
	private void openStartablePluginPageInNewWindow() {
		repository.gw.login.Login loginPage = new Login(getDriver());
	    loginPage.login("su", "gw");
	    goToStartablePluginPageAfterLogin();
	}

    private void goToStartablePluginPageAfterLogin() {
    	pressAltShiftT();
        new ServerToolsSideBar(getDriver()).clickStartablePlugin();
    }
	    
    public void stopStartablePlugin(StartablePlugin plugin) {
    	repository.gw.helpers.TableUtils tableUtils = new repository.gw.helpers.TableUtils(getDriver());
    	if (tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_StartablePlugin, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableDiv_StartablePlugin, "Name", plugin.getValue())), "Status").equals("Started")) {
    		tableUtils.clickButtonInTableByRowAndColumnName(tableDiv_StartablePlugin, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableDiv_StartablePlugin, "Name", plugin.getValue())), "Action", "Stop");
    	} else {
    		systemOut("The " + plugin.getValue() + " plugin was already stopped.");
    	}
	}
	 
	public void startStartablePlugin(StartablePlugin plugin) {
		repository.gw.helpers.TableUtils tableUtils = new TableUtils(getDriver());
    	if (tableUtils.getCellTextInTableByRowAndColumnName(tableDiv_StartablePlugin, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableDiv_StartablePlugin, "Name", plugin.getValue())), "Status").equals("Stopped")) {
    		tableUtils.clickButtonInTableByRowAndColumnName(tableDiv_StartablePlugin, tableUtils.getRowNumberFromWebElementRow(tableUtils.getRowInTableByColumnNameAndValue(tableDiv_StartablePlugin, "Name", plugin.getValue())), "Action", "Start");
    	} else {
    		systemOut("The " + plugin.getValue() + " plugin was already started.");
    	}
	}
	 
	public void loginAndStopStartablePlugin(StartablePlugin plugin) {
		openStartablePluginPageInNewWindow();
		stopStartablePlugin(plugin);
	}
	 
	public void loginAndStartStartablePlugin(StartablePlugin plugin) {
		openStartablePluginPageInNewWindow();
		startStartablePlugin(plugin);
	}
}
