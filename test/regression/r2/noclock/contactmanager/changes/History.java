package regression.r2.noclock.contactmanager.changes;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.enums.State;
import com.idfbins.testng.helpers.DateUtils;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.contact.ContactDetailsHistoryAB;
import repository.ab.pagelinks.PageLinks;
import repository.driverConfiguration.Config;
import repository.gw.enums.DeliveryOptionType;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.MaritalStatus;
import repository.gw.generate.GenerateContact;

import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
public class History extends BaseTest {
	private WebDriver driver;
	//Originals
	@SuppressWarnings("unused")
	private GenerateContact myContactObj = null;
	private String originalMainEmail = "chofman@idfbins.com";
	private String originalComment = "First Comment";
	private String originalAltName = "Originalaltname";
	private String originalFormerName = "Originalformername";
	private MaritalStatus originalMaritalStatus = MaritalStatus.Single;
	private Gender originalGender = Gender.Male;
	private String originalAltEmail = "firstaltemail@gmail.com";
	private boolean originalMembershipOnly = false;
	private String website = "www.ohHamburgers.com";
	private String originalBDay = "06/02/1990";
	private String originalDL = "ID312465A";
	private State originalDLState = State.Idaho;
	private DeliveryOptionType originalDeliveryOption = DeliveryOptionType.Attention;
	private String originalDeliveryOptionDescription = "ATTN: Hotness";
	
	//Changed
	private String changedMainEmail = "jlarsen@idfbins.com";
	private String changedComment = "Second Comment";
	private String changedAltName = "Changedaltname";
	private String changedFormerName = "Changedformername";
	private MaritalStatus changedMaritalStatus = MaritalStatus.Married;
	private Gender changedGender = Gender.Female;
	private String changedAltEmail = "changedaltemail@gmail.com";
	private boolean changedMembershipOnly = true;
	private String changedWebsite = "www.changedsite.com";
	private String changedBDay = "06/07/1990";
	private String changedDL = "I165165A";
	private State changedDLState = State.Hawaii;
	private String changedDeliveryOptionDescription = "ATTN: Extreme Hotness";
	
	@Test
	public void newCompanyHistoryTest() throws Exception{
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withCompanyName("Hursts Hosers")
				.build(GenerateContactType.Company);
		//Created
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setMainEmail(originalMainEmail);
		basicsPage.setComments(originalComment);
		basicsPage.setWebsite(website);
		basicsPage.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.addDeliveryOption(originalDeliveryOption, originalDeliveryOptionDescription);
		addressPage.clickContactDetailsAddressesUpdate();

        PageLinks links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();

        ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
		//Comments
		ArrayList<String> messages = history.verifyHistory("Comments Added", "Comments Added", "Comments", " ", originalComment);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		//Email
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Added","Main Email Address Added" ,"Main Email"," ", originalMainEmail);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Website
        history = new ContactDetailsHistoryAB(driver);
		String message = history.verifyHistoryNoChangeDetail("Website Added", "Website "+ website+" Added", DateUtils.dateFormatAsString("ddd, MMM d, yyyy", repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		if(!message.contains("History item not found")){
			failTest(messages);
		}
		
		//Delivery Option
		messages = history.verifyHistory("Delivery Option New", "New", "Type", " ",originalDeliveryOption.getTypeValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
		messages = history.verifyHistory("Delivery Option New", "New", "Description", " ",originalDeliveryOptionDescription);
		if(!messages.isEmpty()){
			failTest(messages);
		}
        history = new ContactDetailsHistoryAB(driver);
		history.clickContactDetailsBasicsTab();
		
		//Changed
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setMainEmail(changedMainEmail);
		basicsPage.setComments(changedComment);
		basicsPage.setWebsite(changedWebsite);
		basicsPage.clickContactDetailsBasicsAddressLink();

        addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.updateDeliveryOption(originalDeliveryOption, changedDeliveryOptionDescription);
		addressPage.clickContactDetailsAddressesUpdate();

        links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();

        history = new ContactDetailsHistoryAB(driver);
		//Comments
		messages = history.verifyHistory("Comments Changed", "Comments Changed", "Comments", "First Comment", changedComment);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Website
        history = new ContactDetailsHistoryAB(driver);
		message = history.verifyHistoryNoChangeDetail("Website Changed", "Website "+ website+" updated to "+changedWebsite, DateUtils.dateFormatAsString("ddd, MMM d, yyyy", repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		if(!message.contains("History item not found")){
			failTest(messages);
		}
		
		//Email
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Changed","Main Email Address Changed" ,"Main Email","chofman@idfbins.com", changedMainEmail);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Delivery Option
		messages = history.verifyHistory("Delivery Option Updated", "Updated", "Type", originalDeliveryOption.getTypeValue(),originalDeliveryOption.getTypeValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
		messages = history.verifyHistory("Delivery Option Updated", "Updated", "Description", originalDeliveryOptionDescription,changedDeliveryOptionDescription);
		if(!messages.isEmpty()){
			failTest(messages);
		}
        history = new ContactDetailsHistoryAB(driver);
		history.clickContactDetailsBasicsTab();

		//Removed
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setMainEmail("");
		List<WebElement> errors = basicsPage.clickContactDetailsBasicsUpdateLink();
		if(errors.size() > 0) {
			Assert.fail("Errors were received. Check it out.");
		}
		basicsPage.clickContactDetailsBasicsEditLink();		
		basicsPage.setComments("");
		basicsPage.setWebsite("");
		basicsPage.clickContactDetailsBasicsAddressLink();

        addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.removeDeliveryOption(changedDeliveryOptionDescription);
		addressPage.clickContactDetailsAddressesUpdate();

        links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();

        history = new ContactDetailsHistoryAB(driver);
		//Comments
		messages = history.verifyHistory("Comments Removed", "Comments Removed", "Comments", "Second Comment", " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Website
        history = new ContactDetailsHistoryAB(driver);
		message = history.verifyHistoryNoChangeDetail("Website Removed", "Website "+ changedWebsite+" Removed", DateUtils.dateFormatAsString("ddd, MMM d, yyyy", repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		if(!message.contains("History item not found")){
			failTest(messages);
		}
		//Email
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Removed","Main Email Address Removed" ,"Main Email Address",changedMainEmail, " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Delivery Option
		messages = history.verifyHistory("Delivery Option Removed", "Removed", "DeliveryOption", changedDeliveryOptionDescription," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		/*
		messages = history.verifyHistory("Delivery Option Removed", "Removed", "DeliveryOption", changedDeliveryOptionDescription," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		*/

        history = new ContactDetailsHistoryAB(driver);
		history.clickContactDetailsBasicsTab();
	}
	
	@Test
	public void newPersonHistoryTest() throws Exception{
		AbUsers user = AbUserHelper.getRandomDeptUser("Policy Services");

		Config cf = new Config(ApplicationOrCenter.ContactManager);
        driver = buildDriver(cf);
        this.myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName("Test", "History")
				.build(GenerateContactType.Person);

        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsLink();
//Created
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setMainEmail(originalMainEmail);
		basicsPage.setComments(originalComment);
		basicsPage.setContactDetailsBasicsAlternateName(originalAltName);
		basicsPage.setContactDetailsBasicsFormerName(originalFormerName);
		basicsPage.setMaritalStatus(originalMaritalStatus.getValue());
		basicsPage.setGender(originalGender.getValue());
		basicsPage.setAltEmail(originalAltEmail);
		basicsPage.setContactDetailsBasicsMembershipOnly(originalMembershipOnly);
		basicsPage.setDateOfBirth(originalBDay);
		basicsPage.setDL(originalDL);
		basicsPage.setWebsite(website);
		basicsPage.setDLState(originalDLState.getName());
		
		basicsPage.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.addDeliveryOption(originalDeliveryOption, originalDeliveryOptionDescription);
		addressPage.clickContactDetailsAddressesUpdate();

        PageLinks links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();

        ContactDetailsHistoryAB history = new ContactDetailsHistoryAB(driver);
		//Comments
		ArrayList<String>messages = history.verifyHistory("Comments Added", "Comments Added", "Comments", " ", originalComment);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		//Email
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Added","Main Email Address Added" ,"Main Email"," ", originalMainEmail);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//AltName
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Alternate Name Added","Alternate Name Added","Alternate Name"," ", originalAltName);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//FormerName
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Former Name Added","Former Name Added" ,"Former name"," ", originalFormerName);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Website
        history = new ContactDetailsHistoryAB(driver);
		String message = history.verifyHistoryNoChangeDetail("Website Added", "Website "+ website+" Added", DateUtils.dateFormatAsString("ddd, MMM d, yyyy", repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		if(!message.contains("History item not found")){
			failTest(messages);
		}
				
		//MaritalStatus
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Marital Status Added","Marital Status Added" ,"Marital status"," ", originalMaritalStatus.getValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//Gender
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Gender Added","Gender Added","Gender"," ", originalGender.getValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//AltEmail
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Added","Alternate Email Address Added" ,"Alternate Email"," ", originalAltEmail);
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//Membership Only
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Membership Only Changed","Membership Only Changed" ,"Membership Only?"," ", originalMembershipOnly + "");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Date of Birth
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Date of Birth Added","Date of Birth Added" ,"Date of Birth"," ", originalBDay);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//DriversLicense
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("License Number New","License Number Added" ,"License Number"," ", originalDL);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		
		//Drivers Licence State
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("License State New","License State Added" ,"License State"," ", originalDLState.getName());
		if(!messages.isEmpty()){
			failTest(messages);
		}
						
		//Delivery Option
		messages = history.verifyHistory("Delivery Option New", "New", "Type", " ",originalDeliveryOption.getTypeValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
			
		messages = history.verifyHistory("Delivery Option New", "New", "Description", " ",originalDeliveryOptionDescription);
		if(!messages.isEmpty()){
			failTest(messages);
		}
        history = new ContactDetailsHistoryAB(driver);
		history.clickContactDetailsBasicsTab();
		
//Changed
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setMainEmail(changedMainEmail);
		basicsPage.setComments(changedComment);
		basicsPage.setContactDetailsBasicsAlternateName(changedAltName);
		basicsPage.setContactDetailsBasicsFormerName(changedFormerName);
		basicsPage.setMaritalStatus(changedMaritalStatus.getValue());
		basicsPage.setGender(changedGender.getValue());
		basicsPage.setAltEmail(changedAltEmail);
		basicsPage.setContactDetailsBasicsMembershipOnly(changedMembershipOnly);
		basicsPage.setDateOfBirth(changedBDay);
		basicsPage.setDL(changedDL);
		basicsPage.setWebsite(changedWebsite);
		basicsPage.setDLState(changedDLState.getName());
		basicsPage.clickContactDetailsBasicsAddressLink();

        addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.updateDeliveryOption(DeliveryOptionType.Attention, changedDeliveryOptionDescription);
		addressPage.clickContactDetailsAddressesUpdate();

        links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();

        history = new ContactDetailsHistoryAB(driver);
		//Comments
		messages = history.verifyHistory("Comments Changed", "Comments Changed", "Comments", originalComment, changedComment);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		//Email
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Changed","Main Email Address Changed" ,"Main Email",originalMainEmail, changedMainEmail);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//AltName
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Alternate Name Changed","Alternate Name Changed","Alternate Name",originalAltName, changedAltName);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//FormerName
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Former Name Changed","Former Name Changed" ,"Former name",originalFormerName, changedFormerName);
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//MaritalStatus
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Marital Status Changed","Marital Status Changed" ,"Marital status",originalMaritalStatus.getValue(), changedMaritalStatus.getValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//Gender
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Gender Changed","Gender Changed","Gender",originalGender.getValue(), changedGender.getValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//AltEmail
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Changed","Alternate Email Address Changed" ,"Alternate Email",originalAltEmail, changedAltEmail);
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//Membership Only
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Membership Only Changed","Membership Only Changed" ,"Membership Only?",originalMembershipOnly + "", changedMembershipOnly + "");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		//Website
        history = new ContactDetailsHistoryAB(driver);
		message = history.verifyHistoryNoChangeDetail("Website Changed", "Website "+ website+" updated to "+changedWebsite, DateUtils.dateFormatAsString("ddd, MMM d, yyyy", repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		if(!message.contains("History item not found")){
			failTest(messages);
		}
		//Date of Birth
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Date of Birth Changed","Date of Birth Changed" ,"Date of Birth",originalBDay, changedBDay);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//DriversLicense
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("License Number Changed","License Number Changed" ,"License Number",originalDL, changedDL);
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		
		//Drivers Licence State
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("License State Changed","License State Changed" ,"License State", originalDLState.getName(), changedDLState.getName());
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Delivery Option
		messages = history.verifyHistory("Delivery Option Updated", "Updated", "Type", originalDeliveryOption.getTypeValue(), originalDeliveryOption.getTypeValue());
		if(!messages.isEmpty()){
			failTest(messages);
		}
		messages = history.verifyHistory("Delivery Option Updated", "Updated", "Description", "ATTN: Hotness","ATTN: Extreme Hotness");
		if(!messages.isEmpty()){
			failTest(messages);
		}

        history = new ContactDetailsHistoryAB(driver);
		history.clickContactDetailsBasicsTab();

//Removed
        basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickContactDetailsBasicsEditLink();
		basicsPage.setMainEmail("");
		basicsPage.setComments("");
		basicsPage.setContactDetailsBasicsAlternateName("");
		basicsPage.setContactDetailsBasicsFormerName("");
		basicsPage.setMaritalStatus("<none>");
		basicsPage.setGender("<none>");
		basicsPage.setAltEmail("");
		basicsPage.setDL("");
		basicsPage.setWebsite("");
		basicsPage.setDLState("<none>");
		basicsPage.setDateOfBirth("");

		basicsPage.clickContactDetailsBasicsAddressLink();

        addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.removeDeliveryOption(changedDeliveryOptionDescription);
		addressPage.clickContactDetailsAddressesUpdate();

        links = new PageLinks(driver);
		links.clickContactDetailsBasicsHistoryLink();

        history = new ContactDetailsHistoryAB(driver);
		//Comments
		messages = history.verifyHistory("Comments Removed", "Comments Removed", "Comments", changedComment, " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		//Email
        history = new ContactDetailsHistoryAB(driver);
//		message = history.verifyHistory("Email Address Removed","Main Email Address Removed" ,"Web.ContactDetail.EmailAddress1","jlarsen@idfbins.com", " ");
		messages = history.verifyHistory("Email Address Removed","Main Email Address Removed" ,"Main Email Address", changedMainEmail, " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Delivery Option
		messages = history.verifyHistory("Delivery Option Removed", "Removed", "DeliveryOption", changedDeliveryOptionDescription," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//AltName
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Alternate Name Removed","Alternate Name Removed","Alternate Name", changedAltName, " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//FormerName
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Former Name Removed","Former Name Removed" ,"Former name",changedFormerName," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//MaritalStatus
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Marital Status Removed","Marital Status Removed" ,"Marital status",changedMaritalStatus.getValue()," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//Gender
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Gender Removed","Gender Removed","Gender",changedGender.getValue(), " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
				
		//AltEmail
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Email Address Removed","Alternate Email Address Removed" ,"Alternate Email Address", changedAltEmail," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
						
		//Date of Birth
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("Date of Birth Removed","Date of Birth Removed" ,"Date of Birth",changedBDay, " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//DriversLicense
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("License Number Removed","License Number Removed" ,"License Number",changedDL, " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		
		//Drivers Licence State
        history = new ContactDetailsHistoryAB(driver);
		messages = history.verifyHistory("License State Removed","License State Removed" ,"License State",changedDLState.getName(), " ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		
		//Website
        history = new ContactDetailsHistoryAB(driver);
		message = history.verifyHistoryNoChangeDetail("Website Removed", "Website "+ changedWebsite+" Removed", DateUtils.dateFormatAsString("ddd, MMM d, yyyy", repository.gw.helpers.DateUtils.getCenterDate(cf, ApplicationOrCenter.ContactManager)));
		if(!message.contains("History item not found")){
			failTest(messages);
		}
						
		/*
		//Delivery Option
		messages = history.verifyHistory("Delivery Option Removed", "Removed", "DeliveryOption", changedDeliveryOptionDescription," ");
		if(!messages.isEmpty()){
			failTest(messages);
		}
		*/

        history = new ContactDetailsHistoryAB(driver);
		history.clickContactDetailsBasicsTab();

	}
	
	public void failTest(ArrayList<String> errorMessages){
		if(!errorMessages.isEmpty()){
		String failureMessage = "";
			for(String message : errorMessages){
				failureMessage += message + "\n";
			}				
		Assert.fail(failureMessage);
		}
	}
}
