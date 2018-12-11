package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactMembershipType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.contact.ContactEditPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderSquirePropertyLocation;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;


/**
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Oct 2, 2018
* 
* Acceptance criteria:
Ensure that I can edit a newly created address while my policy is in quote
Ensure that this edited address updates on contacts using this address on the policy 
Ensure that I cannot edit an address that has already gone to ContactManager even if the current policy you are creating isn't bound (we will assume that if the contact is in CM that the address is correct and being used elsewhere)
Ensure that once I submit the policy I am quoting the address is no longer editable
Ensure that when I edit an address that when the policy is submitted CM is only getting the final edited address
Ensure that when an address is edited on any policy member that does not change the location addresses as these are separate and the location address will need it's own update
* 
*/
public class US16293_AllowAddressToBeEditedWhileSubmissionIsInDraftStatus extends BaseTest{
	
	private GeneratePolicy myPolicy;
	private AddressInfo correctAddress;
	
	@Test
	public void checkEditedAddressChanged() throws Exception {
		
		this.correctAddress = new AddressInfo(true);
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    
	    PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Wild", "Bear", AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
	    ani.setNewContact(CreateNew.Create_New_Always);
	    ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<>();
	    aniList.add(ani);  
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();	
		
		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
	
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		
		locOnePropertyList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		
		
		this.myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Mr", "Pni")
				.withANIList(aniList)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        
        polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        new ContactEditPC(driver).addressLine1Exists();
        polInfo.changeAddress(correctAddress);
        polInfo.clickUpdate();
        
        polInfo.clickANI();
        
        GenericWorkorderPolicyInfoAdditionalNamedInsured aniPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);       
		ArrayList<String> aniAddresses = aniPage.getAddressListings();
		boolean found = false;
		for(String address : aniAddresses) {
			if(address.contains(correctAddress.getLine1())){
				found = true;
			}
		}
		Assert.assertTrue(found, "The PNI's changed address must be available for selection on the ANI Contact.");
        aniPage.inputNewAddress(correctAddress);
		aniPage.clickUpdate();
		
		sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPropertyLocations();
        
        GenericWorkorderSquirePropertyLocation locationPage = new GenericWorkorderSquirePropertyLocation(driver);
        ArrayList<String> addresses = locationPage.getAddresses();
        for(String address : addresses) {
        	Assert.assertFalse(address.contains(correctAddress.getLine1()), "The corrected address was found on the locations page, when this address should only exist for contacts.");
        }	

		GenericWorkorder wo = new GenericWorkorder(driver);
		wo.clickGenericWorkorderQuote();
		GuidewireHelpers help = new GuidewireHelpers(driver);
		help.logout();
		this.myPolicy.convertTo(driver, GeneratePolicyType.PolicyIssued);
		driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.ContactManager).getUrl());
		Assert.assertTrue(testNewAniInAB(driver, correctAddress), "The ani should be found in ContactManager after the policy has been issued. /r/n Check event messages and the consider filing a defect. \r\n Search "+driver.getCurrentUrl()+" ContactManager for "+myPolicy.aniList.get(0).getPersonFullName() +".");
		driver.quit();
	}
	
	public boolean testNewAniInAB(WebDriver driver, AddressInfo address) throws Exception {
		 
        AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy");
        Login logMeIn = new Login(driver);
        logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

        TopMenuAB getToSearch = new TopMenuAB(driver);
        getToSearch.clickSearchTab();

        AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
        AdvancedSearchResults searchResult = null;
        int i = 0;
        do{
        	searchResult = searchMe.searchByFirstNameLastNameAnyAddress(this.myPolicy.aniList.get(0).getPersonFirstName(), this.myPolicy.aniList.get(0).getPersonLastName());
        	if(searchResult == null) {
	        	new GuidewireHelpers(driver).logout();
	        	i++;
        	}
        	
        }while(searchResult == null && i < 4);
        if (searchResult != null) {
        	ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
        	basicsPage.clickContactDetailsBasicsAddressLink();
        	ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
        	boolean existsOnce = addressExistsOnce(addressPage.getAddresses());
       	 	new GuidewireHelpers(driver).logout();
            return existsOnce;
        } else {
	   		Assert.fail("The Additional Named Insured was not found in ContactManager.  Could be a stuck message or a different defect."); 
        }
        return false;
	}
	
	public boolean addressExistsOnce(ArrayList<String> addresses) {
		boolean found = false;
		for(String address : addresses) {
			if(address.contains(this.correctAddress.getLine1())) {
				found = true;
			}
		}
		if(addresses.size() > 1) {
			Assert.fail("Multiple addresses exist for "+this.myPolicy.aniList.get(0).getPersonFirstName() +" "+ this.myPolicy.aniList.get(0).getPersonLastName() +".");
			
		} else if(addresses.size() < 1) {
			Assert.fail("There isn't an addresses for "+this.myPolicy.aniList.get(0).getPersonFirstName() +" "+ this.myPolicy.aniList.get(0).getPersonLastName() +".");
		} 
		return found;
	}
	
	@Test(dependsOnMethods = {"checkEditedAddressChanged"})
	public void unableToChangeSubmittedPolicyAddresses() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
	    WebDriver driver = buildDriver(cf);
	    AdvancedSearchAB searchPage = new AdvancedSearchAB(driver);
	    searchPage.loginAndSearchContact(AbUserHelper.getRandomDeptUser("Policy Service"), myPolicy.pniContact.getFirstName(), myPolicy.pniContact.getLastName(), correctAddress.getLine1(), correctAddress.getState());
	    ContactDetailsBasicsAB details= new ContactDetailsBasicsAB(driver);
	    details.clickEdit();
	    details.setMembershipType(ContactMembershipType.Associate);
	    details.clickContactDetailsBasicsAddressLink();
	    ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
	    addressPage.setContactDetailsAddressesAddressLine1("261 Collins St");
	    addressPage.setContactDetailsAddressesCity("Blackfoot");
	    addressPage.setContactDetailsAddressesState(State.Idaho);
	    addressPage.setContactDetailsAddressesZipCode("83221");
	    addressPage.clickUpdate();
	    new GuidewireHelpers(driver).logout();
	    driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
	    Login login = new Login(driver);
		login.loginAndSearchPolicy_asAgent(myPolicy);
		PolicySummary pcSummaryPage = new PolicySummary(driver);
		Assert.assertTrue(pcSummaryPage.getAddress().contains(correctAddress.getLine1()), "The PNI's address on the policy Summary page should match the address created on the policy.");
		driver.quit();
	}
	
	@Test
	public void changePniAddressFromAB() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
		
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				
				.withFirstLastName("Contactmanager", null, "Add")
				.withUniqueName(true)
				.withPrimaryAddress(new AddressInfo())
				.build(GenerateContactType.Person);
        
        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
	    		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();	
		
		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
	
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		mySquire.setCreateNew(CreateNew.Do_Not_Create_New);
		
		locOnePropertyList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		
		
		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Do_Not_Create_New)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withContact(myContactObj)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        
        polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickPolicyInfoPrimaryNamedInsured();
        Assert.assertFalse(new ContactEditPC(driver).addressLine1Exists(),"The address should not be editable when the contact came from ContactManager.");
	}
}
