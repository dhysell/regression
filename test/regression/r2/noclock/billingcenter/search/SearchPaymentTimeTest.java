package regression.r2.noclock.billingcenter.search;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import repository.bc.search.BCSearchMenu;
import repository.bc.search.BCSearchPayment;
import repository.bc.topmenu.BCTopMenu;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.enums.ARCompany;
import persistence.enums.ARUserRole;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;
/**
* @Author jqu
* @Description When the calendar picker is used, the time should be defaulted to 7:00pm.(US6602)
* @RequirementsLink <a href="http://projects.idfbins.com/billingcenter/Documents/Requirements/MockUps/UI%20Mockup%20-%20US3215%20and%20US3253.docx">Payment Search Screen Requirements</a>*  
* @DATE Jan 20, 2016
*/
public class SearchPaymentTimeTest extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
	private String defaultTime="07:00 PM";
	
	@Test(enabled=true)
	public void testSearchPaymentDefaultTime() throws Exception {	
		this.arUser = ARUsersHelper.getRandomARUserByRoleAndCompany(ARUserRole.Billing_Manager, ARCompany.Commercial);
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		Date currentDate=DateUtils.getCenterDate(cf, ApplicationOrCenter.BillingCenter);
		new Login(driver).login(arUser.getUserName(), arUser.getPassword());
		BCTopMenu topMenu=new BCTopMenu(driver);
		topMenu.clickSearchTab();
		BCSearchMenu schMenu=new BCSearchMenu(driver);
		schMenu.clickSearchMenuPayments();
		BCSearchPayment schPayment = new BCSearchPayment(driver);
		String earliestCreateDate=schPayment.getBCSearchPaymentsEarliestCreatedDate();
		String latestCreateDate=schPayment.getBCSearchPaymentsLatestCreatedDate();
		if(!earliestCreateDate.contains(defaultTime) || !latestCreateDate.contains(defaultTime)){
			Assert.fail("default time is not 7:00 pm, which is incorrect.");
		}
		//change date and verify default time again
		schPayment.clickBCSearchPaymentsDateCriteriaPicker("Earliest Created Date");
		schPayment.selectBCSearchPaymentsDateFromDatePicker(DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, -3));
		earliestCreateDate=schPayment.getBCSearchPaymentsEarliestCreatedDate();
		
		driver.navigate().refresh();
		schPayment = new BCSearchPayment(driver);
		schPayment.clickBCSearchPaymentsDateCriteriaPicker("Latest Created Date");
		schPayment.selectBCSearchPaymentsDateFromDatePicker(DateUtils.dateAddSubtract(currentDate, DateAddSubtractOptions.Day, -2));
		latestCreateDate=schPayment.getBCSearchPaymentsEarliestCreatedDate();
		
		if(!earliestCreateDate.contains(defaultTime) || !latestCreateDate.contains(defaultTime)){
			Assert.fail("default time after date change is not 7:00 pm, which is incorrect.");
		}
	}
}
