package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.RelatedContacts;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class AddDependentsPopup extends BaseTest {
	private WebDriver driver;
	//Instance Data
	private AbUsers abUser;
	private String firstName = "Kenneth";
	private String lastName = "Tennant";
	private Contact spouse = null;
	private AddressInfo kensAddress = new AddressInfo("1357 Lakeview Dr", "","Pocatello",State.Idaho,"83201-3115",CountyIdaho.Bannock,"United States",AddressType.Home);
	private AddressInfo otherTennantAddress = new AddressInfo("4319 Canyon View Ln", "","Buhl",State.Idaho,"83316-5257",CountyIdaho.Bannock,"United States",AddressType.Home);
	ArrayList<RelatedContacts> kensFam = new ArrayList<RelatedContacts>();
	
	@BeforeMethod
    public void beforeMethod() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
	}
	
	/**
	 * @Author sbroderick
	* @Requirement 
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/10075774537ud/detail/userstory/50893730610">Add Dependents Popup</a>
	* @Description This test ensures Dependents can be added via the Dependents popup.
	* @DATE Apr 22, 2016
	*/
	
	@Test
    public void testAddDependents() throws Exception {
		this.abUser = AbUserHelper.getRandomDeptUser("Policy Services");
		this.spouse = new Contact();
		spouse.setFirstName("Shani");
		spouse.setLastName("Tennant");
		spouse.setAddress(kensAddress);

        new AdvancedSearchAB(driver).loginAndSearchContact(abUser, firstName, lastName, kensAddress.getLine1(), State.Idaho);

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
		policyContact.clickContactDetailsBasicsRelatedContactsLink();

        ContactDetailsRelatedContactsAB addDependents = new ContactDetailsRelatedContactsAB(driver);
		addDependents.addSpouse(spouse);
		
		addDependents();
		resetContact();		
	}

    public void addDependents() throws Exception {
		
		//Spouse
		this.kensFam.add(new RelatedContacts("Shani","Tennant", repository.gw.enums.RelatedContacts.Spouse,kensAddress,true));
		//Kids
		this.kensFam.add(new RelatedContacts("Angela", "Tennant", repository.gw.enums.RelatedContacts.ChildWard, kensAddress, true));
		this.kensFam.add(new RelatedContacts("Brant", "C", "Tennant", repository.gw.enums.RelatedContacts.ChildWard, otherTennantAddress,false));

        ContactDetailsRelatedContactsAB addDependents = new ContactDetailsRelatedContactsAB(driver);
		addDependents.addAllDependents(kensFam);
	}
	
	//removes dependents for the next test.
	public void resetContact(){
        ContactDetailsRelatedContactsAB addDependents = new ContactDetailsRelatedContactsAB(driver);
		addDependents.removeDependants();
		
	}
	

}
