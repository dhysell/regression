package previousProgramIncrement.pi2_062818_090518.nonFeatures.Nucleus;

import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.errorhandling.ErrorHandlingHelpers;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.cc.GenerateFNOL;
import repository.gw.generate.custom.AdvancedSearchResults;
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
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;
import regression.r2.noclock.contactmanager.contact.ClaimsQueue;

import java.util.ArrayList;

/**
 * @Author sbroderick
 * @Requirement The admin cannot delete contacts linked to other centers.
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/236930078296">Permission change for deleting linked contacts</a>
 * @Description Ensure that no user can delete a contact if a contact is linked to another center
 * Ensure that the OOTB warning prompt is given to the user when trying to delete contact.
 * @DATE Jul 23, 2018
 */
public class US15777PermissionChangeForDeletingLinkedContacts extends BaseTest {
	
	private GeneratePolicy mySquirePolicy = null;
	private GenerateFNOL unverifiedFNOL = null;
	
	@Test
	public void deleteSquirePolicyPNI() throws Exception {

    	Config cf = new Config(ApplicationOrCenter.PolicyCenter, "dev");
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

        this.mySquirePolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Cantdelete", "Me")
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);

//        driver.quit();
//
//        cf = new Config(ApplicationOrCenter.ContactManager, "qa");
//        driver = buildDriver(cf);
        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.ContactManager));
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        AdvancedSearchResults searchResults;
        int count = 0;
        do {
        	searchResults = searchMe.searchByFirstNameLastNameAnyAddress(this.mySquirePolicy.pniContact.getFirstName(), this.mySquirePolicy.pniContact.getLastName());
        	count++;
        } while (searchResults == null && count <= 5);
        
        if (searchResults == null) {
            Assert.fail("Uh oh, after issuing the Policy, the contact did not make it to AB so that the contact can be deleted. The test terminated.");
        }

        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
        contactPage.clickContactDetailsBasicsDeleteLink();
        ErrorHandlingHelpers errorHelp = new ErrorHandlingHelpers(driver);
        Assert.assertTrue(errorHelp.getErrorMessage().contains("Contact is not deletable because"), "When attempting to delete a contact that is tied to another center, an error message should show.");
//        driver.quit();
    }

	@Test
	public void testDeleteClaimCenterContact() throws Exception {
		AbUsers user = AbUserHelper.getRandomDeptUser("Admin");
		ClaimsQueue unverifiedFnolObj = new ClaimsQueue();
        unverifiedFnolObj.acceptUnverifiedFNOL();
        this.unverifiedFNOL = unverifiedFnolObj.getUnverifiedFNOL();
        Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        WebDriver driver = buildDriver(cf);
        AdvancedSearchAB searchContact = new AdvancedSearchAB(driver);
		searchContact.loginAndGetToSearch(user);
		AdvancedSearchResults searchResult = searchContact.searchByFirstNameLastNameAnyAddress(this.unverifiedFNOL.getPartiesInvolved().get(0).getFirstName(), this.unverifiedFNOL.getPartiesInvolved().get(0).getLastName());
		if(searchResult == null) {
			Assert.fail(this.unverifiedFNOL.getPartiesInvolved().get(0).getFirstName() + " "+this.unverifiedFNOL.getPartiesInvolved().get(0).getLastName()+" was not found in AB.");
		}
		ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
		detailsPage.clickContactDetailsBasicsDeleteLink();
		
		driver.quit();
        cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        driver = buildDriver(cf);
        
        searchContact = new AdvancedSearchAB(driver);
        searchContact.loginAndGetToSearch(user);
        AdvancedSearchResults deletedContactSearchResult = searchContact.searchByFirstNameLastNameAnyAddress(this.unverifiedFNOL.getPartiesInvolved().get(0).getFirstName(), this.unverifiedFNOL.getPartiesInvolved().get(0).getLastName());
        if(deletedContactSearchResult != null) {
			Assert.fail("After deletion, "+ this.unverifiedFNOL.getPartiesInvolved().get(0).getFirstName() + " "+this.unverifiedFNOL.getPartiesInvolved().get(0).getLastName()+" should not be found in AB.");
		}
	}

    @Test
    public void testDeleteABContactOnly() throws Exception {
        AbUsers user = AbUserHelper.getRandomDeptUser("Policy Service");

        Config cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        WebDriver driver = buildDriver(cf);

        GenerateContact myContactObj = new GenerateContact.Builder(driver)
                .withCreator(user)
                .withFirstLastName("Delete", "Me")
                .build(GenerateContactType.Person);

        ContactDetailsBasicsAB detailsPage = new ContactDetailsBasicsAB(driver);
        detailsPage.clickContactDetailsBasicsDeleteLink();
        AdvancedSearchAB searchAB = new AdvancedSearchAB(driver);
        Assert.assertTrue(searchAB.getAdvancedSearchPageTitle().contains("Search"), "When attempting to delete a contact that is not tied to another center, the user should not receive an error.");
        driver.quit();
        cf = new Config(ApplicationOrCenter.ContactManager, "dev");
        driver = buildDriver(cf);
        AdvancedSearchAB searchContact = new AdvancedSearchAB(driver);
        searchContact.loginAndGetToSearch(user);
        if (searchContact.searchByFirstLastName(myContactObj.firstName, myContactObj.lastName, myContactObj.addresses.get(0).getLine1())){
        	 Assert.fail("After attempting to confirm that the contact that was deleted does not exist, the contact was found in AB.");
        }
    }
}
