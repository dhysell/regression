package regression.r2.noclock.contactmanager.other;


import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.idfbins.enums.CountyIdaho;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AddressType;
import repository.gw.enums.GenerateContactType;
import repository.gw.exception.GuidewireNavigationException;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.DBA;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement The field of Brokerage Agent shall exist in the Contact Details Basics Page.
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Apr 4, 2016
*/
public class BrokerageAgent extends BaseTest {
	private WebDriver driver;
	private GenerateContact myContactObj;
	private String brokerageAgent = "Terry Sivertson";
	private String brokerageAgentNum = "799";
	private AbUsers user;
	private String firstName = "Stor";
	private String middleName = "Broderman";
	private String lastName = "Andan";
	private DBA newDBA = new DBA("theScripter");
	private ArrayList<DBA> myDBAs = new ArrayList<DBA>();
	private AddressInfo myAddress = new AddressInfo("261 Collins St", "", "Blackfoot", State.Idaho, "83221",
			CountyIdaho.Bingham, "United States", AddressType.Work);

    @BeforeMethod
    public void beforeMethod() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
    }
    
	@Test
	public void makeContact() throws Exception{ 
		
		this.user = AbUserHelper.getRandomDeptUser("Policy Services");
		
		myDBAs.add(newDBA);

        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName(this.firstName, this.middleName, this.lastName)
				.withDba(this.myDBAs)
				.withPrimaryAddress(myAddress)
				.build(GenerateContactType.Person);
	
		System.out.println("accountNumber: " + myContactObj.accountNumber);
	}
	
	@Test(dependsOnMethods={"makeContact"})
    public void testBrokerageAgent() throws GuidewireNavigationException {

        Login logMeIn = new Login(driver);
		logMeIn.login(this.user.getUserName(), this.user.getUserPassword());

        TopMenuAB menu = new TopMenuAB(driver);
        AdvancedSearchAB search = new AdvancedSearchAB(driver);

		menu.clickSearchTab();
		
		search.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName, myContactObj.addresses.get(0).getLine1());

        ContactDetailsBasicsAB policyContact = new ContactDetailsBasicsAB(driver);
		String contactName = policyContact.getContactPageTitle();
		if (!contactName.equals(myContactObj.firstName + " " + myContactObj.lastName)) {
			Assert.fail("Unable to find " + myContactObj.firstName + " " + myContactObj.lastName);
		}
		policyContact.clickContactDetailsBasicsEditLink();
		policyContact.addBrokerageAgent(brokerageAgent, brokerageAgentNum);
		String uiBrokerAgent = policyContact.getBrokerageAgent();
		System.out.println(uiBrokerAgent);
		System.out.println(brokerageAgent);
		if(!(uiBrokerAgent.contains(brokerageAgent))){
			Assert.fail("The Brokeragent in the UI doesn't match the BrokerAgent that was intended to be added to the contact.");
		}
		new GuidewireHelpers(driver).logout();
	}
	
	public void getBrokerageAgent(){
		
	}
}
