package scratchpad.steve.holding;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.contact.ContactDetailsPC;
import repository.pc.contact.ContactEditPC;
import repository.pc.search.SearchContactsPC;
import repository.pc.search.SearchSidebarPC;
import repository.pc.topmenu.TopMenuPC;
import persistence.globaldatarepo.entities.PC_Users;
import persistence.globaldatarepo.helpers.PCUsersHelper;

import java.util.ArrayList;

public class DE7804_ContactsNotDeletingInPCWithSolr extends BaseTest{
	
	@Test
	public void testDeleteContact() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);
        
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
        PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
        locOnePropertyList.add(locationOneProperty);
        locationsList.add(new PolicyLocation(locOnePropertyList));

        SquireLiability liabilitySection = new SquireLiability();      
        
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;
        
        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Delete", "Me")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);
        
        PC_Users user = PCUsersHelper.getPCUsersByJob("ContactManager").get(0);
		new Login(driver).login(user.getUserName(), "gw");
		SearchSidebarPC searchSideMenu = new SearchSidebarPC(driver);
		searchSideMenu.clickContacts();
		SearchContactsPC searchContacts = new SearchContactsPC(driver);
		searchContacts.searchPerson(myPolicy.pniContact.getFirstName(), myPolicy.pniContact.getLastName(), myPolicy.pniContact.getAddress());
		ContactDetailsPC details = new ContactDetailsPC(driver);
		details.clickEdit();
		ContactEditPC editContactPage = new ContactEditPC(driver);
		editContactPage.clickDelete();
		TopMenuPC topMenu = new TopMenuPC(driver);
		topMenu.clickSearchTab();
		searchSideMenu = new SearchSidebarPC(driver);
		searchSideMenu.clickContacts();
		searchContacts = new SearchContactsPC(driver);
		Assert.assertFalse(searchContacts.searchPerson(myPolicy.pniContact.getFirstName(), myPolicy.pniContact.getLastName(), myPolicy.pniContact.getAddress()), "After deleting a contact, the contact was found in PC. \r\n Account: "+ myPolicy.accountNumber + "\r\n The contacts name is: "+myPolicy.pniContact.getFullName());
	}
	
	
}
