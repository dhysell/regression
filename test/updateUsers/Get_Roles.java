package updateUsers;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.TableUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.administration.RolesPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import persistence.globaldatarepo.entities.UsersAuthorityProfile;
import persistence.globaldatarepo.entities.UsersInfo;
import persistence.globaldatarepo.entities.UsersRole;

public class Get_Roles extends BaseTest {

	String env = "DEV";

	@Test
	public void getuserInfo() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		WebDriver driver = buildDriver(cf);

		new Login(driver).login("su", "gw");

		new TopMenuAdministrationPC(driver).clickUserSearch();
		
		List<String> usernamesList = new UpdateUsersCommon(driver).getALLUsers_UserName();   
		
		for(String user : usernamesList) {
			new UpdateUsersCommon(driver).userSearch_ByUserName(user);
			UsersInfo info = new UpdateUsersCommon(driver).getUserInfo();
			try {
				UsersInfo.updateUsersInfoTable(info);
			} catch (Exception e) {
				e.printStackTrace();
			}
			new TopMenuAdministrationPC(driver).clickUserSearch();
		}
	}
	

	@Test(enabled = false)
	public void getRolesInEnvironment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		WebDriver driver = buildDriver(cf);

		new Login(driver).login("su", "gw");

		new TopMenuAdministrationPC(driver).clickRoles();


		List<UsersRole> rolesList = new RolesPC(driver).getRoleTypeDescription();

		UsersRole.updateRolesTable(rolesList);
	}

	@Test(enabled = false)
	public void getAthorityProfileGrants() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter, env);
		WebDriver driver = buildDriver(cf);

		new Login(driver).login("su", "gw");

		new TopMenuAdministrationPC(driver).clickAuthorityProfiles();
		List<UsersAuthorityProfile> commitList = new ArrayList<UsersAuthorityProfile>();
		List<WebElement> authorityProfiles = new GuidewireHelpers(driver).finds(By.xpath("//div[contains(@id, 'UWAuthorityProfiles:ProfilesScreen:ProfilesLV-body')]/div/table/tbody/child::tr"));
		for(int i = 0; i<authorityProfiles.size(); i++) {
			authorityProfiles = new GuidewireHelpers(driver).finds(By.xpath("//div[contains(@id, 'UWAuthorityProfiles:ProfilesScreen:ProfilesLV-body')]/div/table/tbody/child::tr"));
			new GuidewireHelpers(driver).clickWhenClickable(authorityProfiles.get(i).findElement(By.xpath(".//child::td[2]/div/a")));

			if (new TableUtils(driver).hasMultiplePages(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':ProfileDetailDV:AuthorityGrantsLV')]")))) {
				int numPages = new TableUtils(driver).getNumberOfTablePages(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':ProfileDetailDV:AuthorityGrantsLV')]")));
				int pageOn = 1;
				while (pageOn <= numPages) {
					List<WebElement> grantsList = new GuidewireHelpers(driver).finds(By.xpath("//div[contains(@id, ':AuthorityGrantsLV-body')]/div/table/tbody/child::tr"));
					for(WebElement grant : grantsList) {
						UsersAuthorityProfile foo = new UsersAuthorityProfile();
						foo.setType(grant.findElement(By.xpath(".//child::td[2]/div")).getText());
						foo.setComparison(grant.findElement(By.xpath(".//child::td[3]/div")).getText());
						foo.setValue(grant.findElement(By.xpath(".//child::td[4]/div")).getText());
						foo.setDescription(grant.findElement(By.xpath(".//child::td[5]/div")).getText());
						commitList.add(foo);
					}
					new TableUtils(driver).clickNextPageButton(new GuidewireHelpers(driver).find(By.xpath("//div[contains(@id, ':ProfileDetailDV:AuthorityGrantsLV')]")));
					pageOn++;
				}
			} else {
				List<WebElement> grantsList = new GuidewireHelpers(driver).finds(By.xpath("//div[contains(@id, ':AuthorityGrantsLV-body')]/div/table/tbody/child::tr"));
				for(WebElement grant : grantsList) {
					UsersAuthorityProfile foo = new UsersAuthorityProfile();
					foo.setType(grant.findElement(By.xpath(".//child::td[2]/div")).getText());
					foo.setComparison(grant.findElement(By.xpath(".//child::td[3]/div")).getText());
					foo.setValue(grant.findElement(By.xpath(".//child::td[4]/div")).getText());
					foo.setDescription(grant.findElement(By.xpath(".//child::td[5]/div")).getText());
					commitList.add(foo);
				}
			}
			
			new GuidewireHelpers(driver).waitForPostBack();
			new GuidewireHelpers(driver).clickWhenClickable(By.xpath("//a[contains(@id, '__crumb__')]"));

		}
		
		for(UsersAuthorityProfile profile : commitList) {
			try {
				UsersAuthorityProfile.updateUsersAuthorityProfileTable(profile);
			} catch (Exception e) {
				
			}
			
			
		}
		
		
	}



}
