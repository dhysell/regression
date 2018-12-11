package currentProgramIncrement.f273_ActivitiesCleanUp_Triton;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.bc.account.BCAccountMenu;
import repository.bc.common.BCCommonActivities;
import repository.bc.common.BCCommonDesktopMyActivityAndQueue;
import repository.bc.desktop.BCDesktopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.OpenClosed;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import helpers.DateUtils;

/**
* @Author JQU
* @Requirement 	US8423 -- Allow users to assign activities at account and policy screens.* 				
				
				Acceptance criteria:
					Ensure that when you click on the activity and the pop up appears for the activity we need to be able to edit the field "Assigned to". (Req 02-11-01)
					Ensure that when the drop down appears that the BillingCenter AR users appear in that list.(roles)
				Steps to get there:
					Click on "My Activities" in the side bar.
					Click on the activity "Subject" to open up the pop-up.
					Click on the drop down "Assigned to" located on the activity tab.
* @DATE 	November 15th, 2018
* 
* */
public class US8423AllowUsersToAssignActivitiesAtAccountAndPolicyScreens extends BaseTest{
	private WebDriver driver;
	private String accountNumber;
	private String policyPeriod;
	private String subject;
	private Date openDate;
	private String assignedToUsername;
	
	@Test
	public void testActivityAssignTo() throws Exception {	
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
    	driver = buildDriver(cf);
		new Login(driver).login("skane", "gw");
		BCDesktopMenu desktopMenu = new BCDesktopMenu(driver);
		desktopMenu.clickDesktopMenuMyQueues();
		BCCommonDesktopMyActivityAndQueue myQueues = new BCCommonDesktopMyActivityAndQueue(driver);
		//sort twice so the latest activities are on first page
		myQueues.sortTableByTile("Opened");
		myQueues.sortTableByTile("Opened");
		//find an activity whose subject doesn't contain Disbursement or LH Over payment
		int pageNumber=1;
		int rowNumber =1;
		int queuesTableRowCount= myQueues.getTableRowCount();
		boolean foundRow = false;
		while(pageNumber<myQueues.getNumberPages() && !foundRow){
			try{
				while(rowNumber < queuesTableRowCount && !foundRow){
					
						if(!myQueues.getSubject(queuesTableRowCount).contains("Disbursement") && !myQueues.getSubject(queuesTableRowCount).contains("Overpayment") )	{					
							foundRow = true;
						}else{
							rowNumber ++;
						}					
				}
			}catch (Exception e) {
				getQALogger().error("Didn't find the suspense payment on page #"+pageNumber);
				pageNumber++;
				myQueues.goToNextPage();
			}	
		}
		accountNumber = myQueues.getAccountNumber(rowNumber);
		policyPeriod = myQueues.getPolicyPeriod(rowNumber);
		subject = myQueues.getSubject(rowNumber);
		openDate = myQueues.getOpenDate(rowNumber);
		System.out.println("it is row #" + rowNumber + " on page "+ pageNumber);
		System.out.println("account # "+ myQueues.getAccountNumber(rowNumber));
		System.out.println("policy period is  "+ policyPeriod);
		
		System.out.println("Open date is  "+ DateUtils.dateFormatAsString("MM/dd/yyyy", openDate));
		myQueues.clickAccountNumber(rowNumber);
		
		BCAccountMenu accountMenu = new BCAccountMenu(driver);
		accountMenu.clickBCMenuActivities();
		BCCommonActivities activity = new BCCommonActivities(driver);
		
		activity.clickActivityTableSubject(openDate, OpenClosed.Open, subject);
		int count =0;
		String newAssignedTo = activity.setAssignedToRandomly();
		while(newAssignedTo.contains("-") && count <20){
			newAssignedTo = activity.setAssignedToRandomly();
			System.out.println("new assigned to is  "+ newAssignedTo);
			count ++;
			}
		System.out.println("newAssignedTo is  "+ newAssignedTo);
		String userWholeName = newAssignedTo.substring(0, newAssignedTo.indexOf("(") -1);
		System.out.println("user whole name  "+ userWholeName);
	
		assignedToUsername = newAssignedTo.substring(0, 1).concat(userWholeName.substring(userWholeName.indexOf(" ")+1));
		System.out.println("new assigned user name  "+ assignedToUsername);
		activity.clickUpdate();
		}
	 @Test(dependsOnMethods = { "testActivityAssignTo" })
		public void verifyActiviyOnNewUser() throws Exception {
			Config cf = new Config(ApplicationOrCenter.BillingCenter);
			driver = buildDriver(cf);

	        new Login(driver).login(assignedToUsername, "gw");
	        BCCommonDesktopMyActivityAndQueue myActivity = new BCCommonDesktopMyActivityAndQueue(driver);
			//sort twice so the latest activities are on first page
	        myActivity.sortTableByTile("Opened");
	        myActivity.sortTableByTile("Opened");
	        try{
	        myActivity.getMyActivityOrMyQueueTableRow(openDate, null, null, null, subject, null, accountNumber, policyPeriod);
	        }catch (Exception e) {
				Assert.fail("The acvity with Subject of '" +subject+"' is not assigned to User '"+assignedToUsername+"'");
			}			
	 }	
}
