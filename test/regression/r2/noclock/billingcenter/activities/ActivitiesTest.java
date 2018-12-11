/**
 * Test functionality of Activities
 * By: Jessica 
 * 
 * **/
package regression.r2.noclock.billingcenter.activities;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.desktop.BCDesktopMenu;
import repository.bc.desktop.actions.DesktopActionsActivityRequest;
import repository.bc.search.BCSearchActivities;
import repository.bc.search.BCSearchMenu;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
public class ActivitiesTest extends BaseTest {
	private WebDriver driver;
	private String username="sbrunson";
	private String password="gw";
	private String assignedToUser, assignedToUserName;
	private Date openDate;
	BCTopMenu menu;
	
	@Test
	public void createActivities() throws Exception {
		boolean user= false;
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		openDate= DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		new Login(driver).login(username, password);
		menu = new BCTopMenu(driver);
		menu.clickDesktopTab();
		BCDesktopMenu myDesktopMenu = new BCDesktopMenu(driver);
		myDesktopMenu.clickDesktopMenuActionsActivityRequest();
		DesktopActionsActivityRequest myActReq = new DesktopActionsActivityRequest(driver);
		while (!user) {
			myActReq.randomSetAssignedToCombobox();
			assignedToUser = myActReq.getCurrentTextInAssignedToCombobox();
			if(assignedToUser.contains("(")){
				user=true;
			}			
		}
		assignedToUserName=assignedToUser.substring(0,assignedToUser.indexOf("(")-1);
		myActReq.clickActivityRequestUpdate();

	}
	
	@Test(dependsOnMethods = { "createActivities" })
	public void searchActivities() throws Exception {
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		new Login(driver).login(username, password);
		menu = new BCTopMenu(driver);
		menu.clickSearchTab();
		BCSearchMenu mySchMenu = new BCSearchMenu(driver);
		mySchMenu.clickSearchMenuActivities();
		BCSearchActivities mySchAct = new BCSearchActivities(driver);
		mySchAct.setSearchActivitiesActivityPattern("Request");
		mySchAct.setSearchActivitiesAssignedToUser(assignedToUserName);
		mySchAct.clickSearch();
		if(!mySchAct.bcSearchActivitiesSearchResultsTableExist()){
			Assert.fail("Could not find the Search Activity results table.\n");
		}
		else {
			boolean existOrNot= mySchAct.verifyActivitiesTable(openDate,assignedToUserName);			
			if(!existOrNot){
				Assert.fail("Could not find the Activity.\n");
			}
		}		
	}	
}
