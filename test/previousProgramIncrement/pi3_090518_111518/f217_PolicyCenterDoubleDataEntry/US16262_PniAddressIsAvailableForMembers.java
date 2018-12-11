package previousProgramIncrement.pi3_090518_111518.f217_PolicyCenterDoubleDataEntry;

import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.AdditionalNamedInsuredType;
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
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;

import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;

import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPayerAssignment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;


public class US16262_PniAddressIsAvailableForMembers extends BaseTest{
	
	private GeneratePolicy myPolicy;
	
	/**
	* @Author sbroderick
	* @Requirement As a PC user when I add a new address to the PNI I want that address to be available throughout the policy including the ANI, AI's, Drivers and members and on location addresses.
	* @RequirementsLink <a href="http:// ">US16262 - CM - Make PNI address available for all policy members</a>
	* @Description Addresses of policy contacts are available when creating new policy contacts.
	* @DATE Sep 27, 2018
	* @throws Exception
	*/
	@Test
	public void testUS16262AddressAvailableForANI() throws Exception {
		
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
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        
        polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickANI();
        
        GenericWorkorderPolicyInfoAdditionalNamedInsured aniPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);       
		ArrayList<String> aniAddresses = aniPage.getAddressListings();
		boolean found = false;
		for(String address : aniAddresses) {
			if(address.contains(this.myPolicy.pniContact.getAddressList().get(0).getLine1())){
				found = true;
				aniPage.setContactEditAddressListing(this.myPolicy.pniContact.getAddressList().get(0).getLine1());
			}
		}
		
		Assert.assertTrue(found, "The Primary Named insureds address was not found in the ANI's address List. Address line 1 is:" + this.myPolicy.pniContact.getAddress().getLine1());
		aniPage.clickUpdate();
		
		sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        
        Contact abContact = new Contact("Dave","Cyborg");
        List<AddressInfo> contactAddresses = new ArrayList<>();
        contactAddresses.add(new AddressInfo(true));
        abContact.setAddressList(contactAddresses);
        myPolicy.additionalMembersToAddToMembershipList.add(abContact);
                
        GenericWorkorderPolicyMembers policyMembersPage = new GenericWorkorderPolicyMembers(driver);
        policyMembersPage.clickSearch();
        
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        searchPC.searchForContact(true, abContact);
        
        policyMembersPage = new GenericWorkorderPolicyMembers(driver);        
        Assert.assertTrue(policyMembersPage.notNewAddressListed(myPolicy.pniContact.getAddress().getLine1()), "Unable to find the PNI's address in the address drop down. Please investigate.");
        
        policyMembersPage.clickCancel();
		GenericWorkorder wo = new GenericWorkorder(driver);
		wo.clickGenericWorkorderQuote();
		GuidewireHelpers help = new GuidewireHelpers(driver);
		help.logout();
		this.myPolicy.convertTo(driver, GeneratePolicyType.PolicyIssued);
		driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.ContactManager).getUrl());
		Assert.assertTrue(testNewAniInAB(driver), "The ani should be found in ContactManager after the policy has been issued. /r/n Check event messages and the consider filing a defect. \r\n Search "+driver.getCurrentUrl()+" ContactManager for "+myPolicy.aniList.get(0).getPersonFullName() +".");
		driver.quit();
	}
	
	public boolean testNewAniInAB(WebDriver driver) throws Exception {
		 
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
		
		boolean pniAddressFound = false;
		boolean aniAddressFound = false;
		
		for(String address : addresses) {
			if(address.contains(myPolicy.aniList.get(0).getAddress().getLine1())) {
				aniAddressFound = true;
			}
			if(address.contains(myPolicy.pniContact.getAddress().getLine1())) {
				pniAddressFound = true;
			}
		}
		if(addresses.size() > 2) {
			Assert.fail("Addresses for the ANI should include the PNI's address and the ANI's address.  Investigate from where the extra address came for "+this.myPolicy.aniList.get(0).getPersonFirstName() +" "+ this.myPolicy.aniList.get(0).getPersonLastName() +" on account "+ this.myPolicy.accountNumber + ".");
			
		} else if(!pniAddressFound) {
			Assert.fail("The PNI's address was not found on the ANI contact after setting this on the ANI page. The ANI is: "+this.myPolicy.aniList.get(0).getPersonFirstName() +" "+ this.myPolicy.aniList.get(0).getPersonLastName() +" on account "+ this.myPolicy.accountNumber + ".");
		} else if(!aniAddressFound) {
			Assert.fail("The ANI's address was not found on the ANI contact after setting this on the ANI page. The ANI is: "+this.myPolicy.aniList.get(0).getPersonFirstName() +" "+ this.myPolicy.aniList.get(0).getPersonLastName() +" on account "+ this.myPolicy.accountNumber + ".");
		} else {
			return true;
		}
		return true;
	}
	
	@Test
	public void userCanImportAddress() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
	    WebDriver driver = buildDriver(cf);
	    
	    AbUsers user = AbUserHelper.getRandomDeptUser("Policy");
	    
	    GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName("Import", "Address")
				//because the import address doesn't show the address line 1, I decided to make the imported address the default, or Cor's address.
				.withPrimaryAddress(new AddressInfo())
				.build(GenerateContactType.Person);
	    
	    driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
	    
	    PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Wild", "Bear", AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
	    ani.setNewContact(CreateNew.Create_New_Always);
	    ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<>();
	    aniList.add(ani);  
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		
		PolicyLocationBuilding loc1Bldg1 = new PolicyLocationBuilding();
		loc1Bldg1.setClassClassification("storage");
		loc1Bldg1.setUsageDescription("Insured Building");
	
		locOneBuildingList.add(loc1Bldg1);
		PolicyLocation location1 = new PolicyLocation(new AddressInfo(), locOneBuildingList);
		
		locationsList.add(location1);
		
		this.myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Mr", "Pni")
				.withANIList(aniList)
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
        polInfo.clickANI();
        																										
        Assert.assertTrue(new GenericWorkorderAdditionalNamedInsured(driver).importAddress(myPolicy.aniList.get(0).getPersonLastName(), myContactObj.firstName, myContactObj.lastName, myContactObj.addresses.get(0).getLine1(), myContactObj.addresses.get(0).getCity(), myContactObj.addresses.get(0).getState(), myContactObj.addresses.get(0).getZip()), "After importing an address, the address wasn't in the address dropdown available to select.");
	}
	
	@Test
	public void testPayerAssignment() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		
		AdditionalInterest propertyAdditionalInterest = new AdditionalInterest(ContactSubType.Company);
		propertyAdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		propertyAdditionalInterest.setNewContact(CreateNew.Do_Not_Create_New);
		propertyAdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locationOneProperty.setBuildingAdditionalInterest(propertyAdditionalInterest);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));
		
		SquireLiability liabilitySection = new SquireLiability();

        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;
        myPropertyAndLiability.liabilitySection = liabilitySection;

        Squire mySquire = new Squire();
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withInsFirstLastName("Payer", "Assign")
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);
        
        Login login = new Login(driver);
		login.loginAndSearchJob(myPolicyObj.agentInfo.getAgentUserName(), myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPayerAssignment();
        
        GenericWorkorderPayerAssignment payerAssign = new GenericWorkorderPayerAssignment(driver);
        ArrayList<String> insAddresses = payerAssign.getPayerAssignmentInsAddresses();
        if(!insAddresses.get(0).contains(locationOneProperty.getAddress().getLine1())) {
        	Assert.fail("The PNI address was not found on the Payer Assignment page.");
        }  
	}
}
