package regression.r2.noclock.billingcenter.disbursements;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.bc.account.AccountDisbursements;
import repository.bc.account.BCAccountMenu;
import repository.bc.search.BCSearchAccounts;
import repository.driverConfiguration.Config;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.ARUsers;
import persistence.globaldatarepo.helpers.ARUsersHelper;

/**
 * @Author sreddy
 * @Requirement
 * @RequirementsLink <a href="http:// ">Link Text</a>
 * @Description
 * @DATE Dec 23, 2016
 */
public class DisbursementsScreenValidations extends BaseTest {
	private WebDriver driver;
	private ARUsers arUser = new ARUsers();
		
	private void loginBC() throws Exception{		
		this.arUser = ARUsersHelper.getRandomARUser();
		Config cf = new Config(ApplicationOrCenter.BillingCenter);
		driver = buildDriver(cf);
		System.out.println("The role is "+arUser.getUserRole());
		new Login(driver).login(arUser.getUserName(),arUser.getPassword());	
	}
	
	String US5250ExpectedClosedInDaysList = "<none>;All;Closed in last 30 days;Closed in last 60 days;Closed in last 90 days;Closed in previous 12 monthsXX";
	String US5250ExpectedStatusList = "<none>;All;All but closed;Approved Only;Awaiting Approval Only;Reapplied Only;Rejected Only;Sent Only;Voided Only";
	
	public void compareListAndString(List<String> Ls,String expstr)
	{
		 
		 String[] tmpArr = expstr.split(";");
		 if(tmpArr.length == Ls.size())
		 {
			for(int i=0;i<tmpArr.length;i++)
			{
				String strExpeted = "NotFound";
				for(int j=0;j<Ls.size();j++)
				{
					if(tmpArr[i].equals(Ls.get(j)))
					{
						strExpeted = "Found";
					}
				}
				if(strExpeted == "NotFound")
				{
					System.out.println("Not found on list: " + tmpArr[i]);
				}
				
			}						
		 }
	}
	
	@Test 
	public void tvalidateInsuredAccount() throws Exception {		
		//login as a clerical		
		//loginBC(ARUserRole.View_Only);
		loginBC();
		BCSearchAccounts insuredAccount = new BCSearchAccounts(driver);
		String strInsAccount = insuredAccount.findAccountInGoodStanding();
		System.out.println("---------->>InsuredAccount ->" + strInsAccount);
		//setAccountAccountName
        BCAccountMenu objAccountMenu = new BCAccountMenu(driver);
		objAccountMenu.clickAccountMenuDisbursements();

		AccountDisbursements objAccountDisbursements = new AccountDisbursements(driver);
				
		// 1st List
		System.out.println("--> CLOSED IN FITER");
		List<String> availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsDateRangeFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}
		
		compareListAndString(availableOptions,US5250ExpectedClosedInDaysList);
		// 2nd List
		System.out.println("--> STATUS FILTER");
		availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsStatusFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}
		compareListAndString(availableOptions,US5250ExpectedClosedInDaysList);
		
		// 3rd List
		System.out.println("--> POLICY FILTER");
		availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsPolicyFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}
//*********************		
		String strlienholderAccount = insuredAccount.findRandomLienholder();
		System.out.println("---------->>lienholderAccount ->" + strlienholderAccount);
		
		objAccountMenu.clickAccountMenuDisbursements();
		// 1st List
		System.out.println("--> CLOSED IN FITER");
		availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsDateRangeFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}
		
		// 2nd List
		System.out.println("--> STATUS FILTER");
		availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsStatusFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}
		
		// 3rd List
		System.out.println("--> POLICY FILTER");
		availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsPolicyFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}		
		
		// 4th List
		System.out.println("--> LOANNUMBER FILTER");
		availableOptions = objAccountDisbursements.getAllOptionsOnDisbursementsLoanNumberFilter();
		for(int i=0;i < availableOptions.size();i++)
		{
			System.out.println(availableOptions.get(i));
		}			
		
	}

}
