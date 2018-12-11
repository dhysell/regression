package regression.r2.noclock.contactmanager.contact;


import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsRelatedContactsAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactRole;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.errorhandling.ErrorHandling;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
@QuarantineClass
public class LienholderContact extends BaseTest {
	private WebDriver driver;
	
	@Test
	public void testGenerateLienNumberButton() throws Exception {
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");

		// this.rolesToAdd.add(ContactRole.Vendor);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withRoles(rolesToAdd)
				.withGeneratedLienNumber(true)
				.withUniqueName(true)
				.build(GenerateContactType.Person);
		
		System.out.println("Lien Number: " + myContactObj.lienNumber);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		if(contactPage.getLienNumber().length()>1) {
			Assert.fail("Generate Lienholder Number button should exist for PolicyServices users that are working contacts that have the Lienholder Role.  See Account " + myContactObj.accountNumber+ ".");
		}	
		String lienNumber = contactPage.getLienNumber();
		new GuidewireHelpers(driver).logout();
		
		AdditionalInterest ai = new AdditionalInterest(myContactObj.companyName, myContactObj.addresses.get(0));
		ai.setNewContact(CreateNew.Do_Not_Create_New);
		ai.setLienholderNumber(lienNumber);
		//return ai;
	}
	
//	@Test
	public void testGenerateAccountNumber() throws Exception {	
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");
        GenerateContact contactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withCompanyName("Client Contact")
				.build(GenerateContactType.Company);

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		contactPage.clickContactDetailsBasicsEditLink();
		if(contactPage.clickContactDetailsBasicsAccountNumberGenerateLink().length()<1) {
			Assert.fail("Generate Lienholder Number button should not exist for contacts that do not have the Lienholder Role.  See Account " + contactObj.accountNumber+ ".");
		}	
		
	}
	
//	@Test
	public void testClaimsCannotGenerateLienholderAcct() throws Exception {
		AbUsers user = AbUserHelper.getRandomDeptUser("Claims");
        Login logMeIn = new Login(driver);
		logMeIn.login(user.getUserName(), user.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
		getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.getRandomLienholderByName(ContactSubType.Contact, "Red");

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		if(basicsPage.clickGenerateLienNumber().length()>1) {
			Assert.fail("The LienNumber Generator should not be available for Claims Users.");
		}
	}
	
	private RelationshipsAB getRelationshipOfCorrectType(ContactSubType contactType, ArrayList<RelationshipsAB> relationships) {
		for(int i = 0; i<100; i++) {
			RelationshipsAB toReturn = relationships.get(NumberUtils.generateRandomNumberInt(0, relationships.size()-1));
			for(int x = 0; x < toReturn.getType().size(); x++){
				if(toReturn.getType().get(x).equals(contactType)) {
					return toReturn;
				}
			}
		}
		return RelationshipsAB.Represents;
	}

    public ArrayList<RelationshipsAB> createArraylistsOfRelationshipsThatCanShareLienNumbers() {
		ArrayList<RelationshipsAB> canShareLiens = new ArrayList<>();
		canShareLiens.add(RelationshipsAB.Spouse);
		canShareLiens.add(RelationshipsAB.SpouseFor);
		canShareLiens.add(RelationshipsAB.Partner);
		canShareLiens.add(RelationshipsAB.PartnerTo);
		canShareLiens.add(RelationshipsAB.AffiliateTo);
		canShareLiens.add(RelationshipsAB.Affiliation);
		canShareLiens.add(RelationshipsAB.Trust);
		canShareLiens.add(RelationshipsAB.Trustee);
		canShareLiens.add(RelationshipsAB.Represents);
		canShareLiens.add(RelationshipsAB.RepresentsFor);
		canShareLiens.add(RelationshipsAB.Manager);
		return canShareLiens;
	}

    public ArrayList<RelationshipsAB> createArraylistsOfRelationshipsThatCantShareLienNumbers() {

		ArrayList<RelationshipsAB> canShareLiens = createArraylistsOfRelationshipsThatCanShareLienNumbers();
		ArrayList<RelationshipsAB> cantShareLiens = new ArrayList<>();
		boolean found = false;
		for(RelationshipsAB relationship : RelationshipsAB.values()) {
			found = false;
			for(RelationshipsAB canShareLien : canShareLiens) {
				if(relationship.equals(canShareLien)) {
					found = true;
					break;
				} 
			}
			if(!found && !relationship.equals(RelationshipsAB.Agent) && !relationship.equals(RelationshipsAB.Agent) &&!relationship.equals(RelationshipsAB.ManagerOf)) {  //Change the first relationship back to none before committing.
				cantShareLiens.add(relationship);
			}
		}
		return cantShareLiens;
	}
	
//	@Test
	public void testRelationshipsCanShareLienNumberPersonType() throws Exception {
		boolean errorMessageExistsAndShouldntExist = false;
		boolean errorMessagedoesntExistAndShould = false;
		RelationshipsAB relationship = getRelationshipOfCorrectType(ContactSubType.Person, createArraylistsOfRelationshipsThatCanShareLienNumbers());
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		GenerateContactType typeToGenerate = GenerateContactType.Person;
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Service"));
		AdvancedSearchResults lienResults = new AdvancedSearchResults();
		lienResults = searchMe.getRandomLienholderByName(ContactSubType.Person, "Smith");
		Contact lienContact = new Contact(lienResults.getFirstName(), lienResults.getLastNameOrCompanyName());
		lienContact = new Contact(lienResults.getFirstName(), lienResults.getLastNameOrCompanyName());		
		lienContact.setAddress(lienResults.getAddress());
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		String lienNum = basicsPage.getLienNumber();
		if(lienNum.length() != 6) {
            basicsPage = new ContactDetailsBasicsAB(driver);
			lienNum = basicsPage.clickGenerateLienNumber();
		}
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        ContactDetailsRelatedContactsAB relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.removeAllRelationships();		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        GenerateContact contactObj = new GenerateContact.Builder(driver)
				.withCreator(AbUserHelper.getRandomDeptUser("Policy Service"))
				.withGenerateAccountNumber(true)
				.build(GenerateContactType.Person);

        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.setRelationship(relationship, lienContact);
        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsLink();
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickEdit();
		basicsPage.addContactDetailsBasicsRole(ContactRole.Lienholder);
		basicsPage.setLienholderNumber(lienNum);
		basicsPage.clickUpdate();
        ErrorHandling errorMessages = new ErrorHandling(driver);
		if(errorMessages.validationMessageExists("Lienholder Number : Lienholder number " + lienNum + " already exists in ContactManager")) {
			errorMessageExistsAndShouldntExist = true;
		}		
		basicsPage.clickEdit();
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.changeRelationship(relationship, RelationshipsAB.Friend);
        errorMessages = new ErrorHandling(driver);
		if(!errorMessages.validationMessageExists("Lienholder Number : Lienholder number " + lienNum + " already exists in ContactManager")) {	
			errorMessagedoesntExistAndShould = true;
		}
        tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsLink();
		basicsPage.setLienholderNumber("");
		basicsPage.removeContactDetailsBasicsRole(ContactRole.Lienholder);
		basicsPage.clickUpdate();
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.removeRelationship(RelationshipsAB.Friend);
		
		if(errorMessageExistsAndShouldntExist) {
			Assert.fail("The Lienholder Message should not appear when a relationship that allows the sharing of a lien number exists.  Currently, the relationship was : "+relationship +".");
		}
		
		if(errorMessagedoesntExistAndShould) {
			Assert.fail("The Lienholder Message should appear when a relationship that allows the sharing of a lien number changes to a relationship that does not allow the sharing of the lien number.  Check the relationship "+ RelationshipsAB.Friend.getValue()+".");
		}
	
	}
	
//	@Test
	public void testRelationshipsCanShareLienNumberCompanyType() throws Exception {
		
		RelationshipsAB relationship = getRelationshipOfCorrectType(ContactSubType.Company, createArraylistsOfRelationshipsThatCanShareLienNumbers());	
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Service"));
		AdvancedSearchResults lienResults = new AdvancedSearchResults();
		lienResults = searchMe.getRandomLienholderByName(ContactSubType.Company, "Smith");
		Contact lienContact = new Contact(lienResults.getFirstName(), lienResults.getLastNameOrCompanyName());
		lienContact = new Contact(lienResults.getFirstName(), lienResults.getLastNameOrCompanyName());
		lienContact.setCompanyName(lienResults.getLastNameOrCompanyName());
		lienContact.setAddress(lienResults.getAddress());
		lienContact.setPersonOrCompany(ContactSubType.Company);
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		String lienNum = basicsPage.getLienNumber();
		if(lienNum.length() != 6) {
            basicsPage = new ContactDetailsBasicsAB(driver);
			lienNum = basicsPage.clickGenerateLienNumber();
		}
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        ContactDetailsRelatedContactsAB relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.removeAllRelationships();
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		if(relationship.equals(RelationshipsAB.Manager)){ //|| !canShareLien.equals(RelationshipsAB.Agentof) ) {
            GenerateContact contactObj = new GenerateContact.Builder(driver)
					.withCreator(AbUserHelper.getRandomDeptUser("Policy Service"))
					.withFirstLastName("New", "Lienholder")
					.withGenerateAccountNumber(true)
					.build(GenerateContactType.Person);
		} else {
            GenerateContact contactObj = new GenerateContact.Builder(driver)
					.withCreator(AbUserHelper.getRandomDeptUser("Policy Service"))
					.withGenerateAccountNumber(true)
					.build(GenerateContactType.Company);
		}

        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.setRelationship(relationship, lienContact);
        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsLink();
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickEdit();
		basicsPage.addContactDetailsBasicsRole(ContactRole.Lienholder);
		basicsPage.setLienholderNumber(lienNum);
		basicsPage.clickUpdate();
        ErrorHandling errorMessages = new ErrorHandling(driver);
		if(errorMessages.validationMessageExists("Lienholder Number : Lienholder number " + lienNum + " already exists in ContactManager")) {
			Assert.fail("The Lienholder Message should not appear when a relationship that allows the sharing of a lien number exists.");
		}
		basicsPage.clickEdit();
		basicsPage.setLienholderNumber("");
		basicsPage.clickUpdate();					
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.removeRelationship(relationship);
	}
		
//	@Test
	public void testRelationshipsThatCantShareLienNumber() throws Exception {
		
		RelationshipsAB relationship = getRelationshipOfCorrectType(ContactSubType.Person, createArraylistsOfRelationshipsThatCantShareLienNumbers());		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
		GenerateContactType typeToGenerate = GenerateContactType.Person;
        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
		searchMe.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy Service"));
		AdvancedSearchResults lienResults = new AdvancedSearchResults();
		lienResults = searchMe.getRandomLienholderByName(ContactSubType.Person, "Smith");
		Contact lienContact = new Contact(lienResults.getFirstName(), lienResults.getLastNameOrCompanyName());
		lienContact = new Contact(lienResults.getFirstName(), lienResults.getLastNameOrCompanyName());		
		lienContact.setAddress(lienResults.getAddress());
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		String lienNum = basicsPage.getLienNumber();
		if(lienNum.length() != 6) {
            basicsPage = new ContactDetailsBasicsAB(driver);
			lienNum = basicsPage.clickGenerateLienNumber();
		}
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        ContactDetailsRelatedContactsAB relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.removeAllRelationships();		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        GenerateContact contactObj = new GenerateContact.Builder(driver)
				.withCreator(AbUserHelper.getRandomDeptUser("Policy Service"))
				.withGenerateAccountNumber(true)
				.build(GenerateContactType.Person);

        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.setRelationship(relationship, lienContact);
        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsLink();
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickEdit();
		basicsPage.addContactDetailsBasicsRole(ContactRole.Lienholder);
		basicsPage.setLienholderNumber(lienNum);
		basicsPage.clickUpdate();
        ErrorHandling errorMessages = new ErrorHandling(driver);
		if(!errorMessages.validationMessageExists("Lienholder Number : Lienholder number " + lienNum + " already exists in ContactManager")) {
			Assert.fail("The Lienholder Message should not appear when a relationship that allows the sharing of a lien number exists.");
		}				
		basicsPage.setLienholderNumber("");
		basicsPage.clickUpdate();
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsRelatedContactsLink();
        relatedTab = new ContactDetailsRelatedContactsAB(driver);
		relatedTab.removeRelationship(relationship);
	}
}
