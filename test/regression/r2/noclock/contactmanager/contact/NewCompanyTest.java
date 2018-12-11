// Steve Broderick
package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class NewCompanyTest extends BaseTest {
	private WebDriver driver;
	private ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
	public GenerateContact myContactObj = null;

	@Test
	public void testNewCompany() throws Exception {
		
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);

		// this.rolesToAdd.add(ContactRole.Vendor);
		// this.rolesToAdd.add(ContactRole.Lienholder);

        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withRoles(this.rolesToAdd)
				.build(GenerateContactType.Company);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsAddressLink();
		String popupText = contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		if(!popupText.contains("Are you sure you want to cancel?")){
			Assert.fail("No popup on Address Tab.");
		}
		contactPage.clickContactDetailsBasicsRelatedContactsLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsAccountsLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsDBAsLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);
		contactPage.clickContactDetailsBasicsRoutingLink();
		contactPage.clickContactDetailsBasicsCancel(OkCancel.Cancel);		
	}
		
	@Test(dependsOnMethods = {"testNewCompany"})
	public void testAssociateRemovedFromCompanyType() throws Exception{
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");
        Login logMeIn = new Login(driver);
		logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.searchCompanyByName(myContactObj.companyName, myContactObj.addresses.get(0).getLine1(), myContactObj.addresses.get(0).getState());

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		
		ArrayList<String> control = new ArrayList<String>();
		
		control.add("Regular");
		control.add("Other");
		
		List<String> types = contactPage.checkMembershipTypeForCompany();
		
		boolean found = false;
		// iterate through enums
		for (String type : types) {
		    // reset to false for each value
		    found = false;
		    // iterate over the list
			for (String listValue : control) {
			    // compare enum value vs string value
				if (type.equals(listValue)) {
					found = true;
					break;
				} // break out of inner for Loop
			}
			if (found) {
				continue;
			}
			Assert.assertFalse(!found, type + " should not be found in the list named " + "Membership Type drop down");	
		}

		if (types.size() == control.size()) {
			Assert.assertTrue(found, "Verify that the appropriate MembershipTypes are in the Membership Type drop down.");	
		}
		else {
		    found = true;
		    System.out.println("all the values expected were found");
		}		
	}
}
