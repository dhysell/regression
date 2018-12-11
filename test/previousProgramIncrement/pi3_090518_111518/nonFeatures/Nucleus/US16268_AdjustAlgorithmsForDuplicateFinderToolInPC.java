package previousProgramIncrement.pi3_090518_111518.nonFeatures.Nucleus;


import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.topmenu.TopMenuPolicyPC;
import repository.pc.workorders.generic.GenericWorkorderMatchingContacts;
import repository.pc.workorders.submission.SubmissionCreateAccount;
import repository.pc.workorders.submission.SubmissionNewSubmission;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

public class US16268_AdjustAlgorithmsForDuplicateFinderToolInPC extends BaseTest{
	
	private String existingContactFirstName = "Albert";
	private String existingContactLastName = "Ramos";
	
	
	@Test
	public void testSSNExactMatch() throws Exception {
		String ssn = StringsUtils.getValidSSN();
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
		
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				
				.withFirstLastName(this.existingContactFirstName, null, this.existingContactLastName)
				.withPrimaryAddress(new AddressInfo(true))
				.withAgent(AgentsHelper.getRandomAgent().getAgentUserName())
				.withUniqueName(true)
				.withSSN(ssn)
				.build(GenerateContactType.Person);
				
		driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.PolicyCenter).getUrl());
		
		Contact pniContact = new Contact(myContactObj.firstName, myContactObj.lastName);
		pniContact.setFirstName(myContactObj.firstName);
		pniContact.setLastName(myContactObj.lastName);
		pniContact.setSocialSecurityNumber(myContactObj.ssn);
		pniContact.setAddress(myContactObj.addresses.get(0));
		pniContact.setIsDupSSN(true);
			
	    Agents agent = AgentsHelper.getRandomAgent();
	    
		new Login(driver).login(agent.getAgentUserName(), agent.getAgentPassword());
		if (new Login(driver).accountLocked()) {
			agent = new Login(driver).loginAsRandomAgent();
		}
		new TopMenuPolicyPC(driver).clickNewSubmission();
		new SubmissionNewSubmission(driver).fillOutFormSearchAndEitherCreateNewOrUseExisting(true, CreateNew.Create_New_Always, ContactSubType.Person, myContactObj.lastName, myContactObj.firstName, null, null, myContactObj.addresses.get(0).getCity(), myContactObj.addresses.get(0).getState(), myContactObj.addresses.get(0).getZip());
		SubmissionCreateAccount createAcctPage = new SubmissionCreateAccount(driver);
		createAcctPage.createNewContact(pniContact);
		GenericWorkorderMatchingContacts matchingPage = new GenericWorkorderMatchingContacts(driver);
		Assert.assertTrue(matchingPage.exactMatch(myContactObj.ssn.substring(5)) >0, "At least once contact with the same ssn exist on the matching contacts page.");
		matchingPage.clickReturnToCreateAccount();
		createAcctPage = new SubmissionCreateAccount(driver);
		new GuidewireHelpers(driver).isOnPage("//span[contains(@id,'CreateAccount:CreateAccountScreen:ttlBar')]", 500, "After clicking the Return To Create Account, you should be directed to the Create Account Page.");	
	}
}
