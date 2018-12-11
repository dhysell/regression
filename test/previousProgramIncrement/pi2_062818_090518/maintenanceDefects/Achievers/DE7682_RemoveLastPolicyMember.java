package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Achievers;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE THE USER CAN REMOVE ALL THE PERSONS ON THE MEMBERSHIP POLICY
* @DATE Sep 20, 2018
*/
public class DE7682_RemoveLastPolicyMember extends BaseTest {
	GeneratePolicy myPolObj;
		
	@Test
	public void removeMemberShipMember() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		myPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.Membership)
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asAgent(myPolObj);
		new StartPolicyChange(driver).startPolicyChange("Remove Only Member", null);
		new SideMenuPC(driver).clickSideMenuMembershipMembers();
		new GenericWorkorderMembershipMembers(driver).removeMembershipMember(myPolObj.pniContact.getLastName());
		Assert.assertTrue(new GuidewireHelpers(driver).finds(By.xpath("//*[contains(text(), 'NullPointerException')]")).isEmpty(), "RECIEVED A NULL POINTER AFTER REMOVING THE ONLY MEMBER");
		
		
	}
}
