package previousProgramIncrement.pi2_062818_090518.f97_QuickerQuickQuote_ContactChanges;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.search.AdvancedSearchAB;
import repository.ab.topmenu.TopMenuAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class US15902_EditNewANI extends BaseTest{
	private WebDriver driver;
	GeneratePolicy myPolicy;
	
	/**
	* @Author sbroderick
	* @Requirement 
	* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/242169756452">Editability of ANI on Quote when ANI is new.</a>
	* @Description 
	* As an Agent or PA I want the ability to edit an ANI's or AI name and contact info if the ANI is a newly created contact and my submission is still in quote status. If a pre-existing contact is chosen the permission to edit the ANI contact is not an option.

	Acceptance criteria:
	Ensure that a newly created ANI's or any policy member contact info can be altered on quote
	Ensure that if a pre-existing contact is chosen as the ANI that the information cannot be altered 
	Ensure that the ANI and or policy member contact is not going back to CM until submit button is clicked
	
	* @DATE Aug 21, 2018
	* @throws GuidewireException
	* @throws Exception
	*/
	@Test
	public void testEditNewAni() throws GuidewireException, Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    driver = buildDriver(cf);
	    
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
				.withInsFirstLastName("Edit", "Ani")
				.withANIList(aniList)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
		driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.ContactManager).getUrl());
		Assert.assertFalse(testNewAniInAB(), "The ani should not be found in ContactManager, when the policy is unsubmitted. See the requirements listed above.");
		
		driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.PolicyCenter).getUrl());
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuPolicyInfo();
        
        polInfo = new GenericWorkorderPolicyInfo(driver);
        polInfo.clickANI();
        
        GenericWorkorderPolicyInfoAdditionalNamedInsured aniPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);       
		aniPage.setEditAdditionalNamedInsuredTIN("987654654");
		aniPage.setName("Verywild", "Animal");
		myPolicy.aniList.get(0).setUnique(false);
		myPolicy.aniList.get(0).setPersonLastName("Animal");
		myPolicy.aniList.get(0).setPersonFirstName("Verywild");
		AddressInfo aniNewAddress = new AddressInfo(true);
		GenericWorkorder wo = new GenericWorkorder(driver);
		wo.setNewAddress(aniNewAddress);
		aniPage.clickUpdate();
		wo = new GenericWorkorder(driver);
		wo.clickGenericWorkorderQuote();
		GuidewireHelpers help = new GuidewireHelpers(driver);
		help.logout();
		this.myPolicy.convertTo(driver, GeneratePolicyType.PolicyIssued);
		driver.get(cf.getEnvironmentsMap().get(ApplicationOrCenter.ContactManager).getUrl());
		Assert.assertTrue(testNewAniInAB(), "The ani should be found in ContactManager after the policy has been issued. /r/n Check event messages and the consider filing a defect. \r\n Search "+driver.getCurrentUrl()+" ContactManager for "+myPolicy.aniList.get(0).getPersonFullName() +".");
		driver.quit();
	}
	
	@Test
	public void testExistingAniCanNotBeEdited() throws Exception {
		
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
	    driver = buildDriver(cf);

	    AbUsers user = AbUserHelper.getRandomDeptUser("Policy Serv");
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(user)
				.withFirstLastName("Existing", "", "Contact")
				.build(GenerateContactType.Person);
		
        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
	    
	    PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, myContactObj.firstName, myContactObj.lastName, AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
	    ani.setNewContact(CreateNew.Do_Not_Create_New);
	    ani.setUnique(false);
	    ani.setPersonLastName(myContactObj.lastName);
	    ani.setAddress(myContactObj.addresses.get(0));
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
				.withInsFirstLastName("Noedit", "Ani")
				.withANIList(aniList)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.QuickQuote);	
		
//		driver.quit();
//		
//		cf = new Config(ApplicationOrCenter.PolicyCenter);
//		driver = buildDriver(cf);
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		polInfo.clickANI();
		
		GenericWorkorderPolicyInfoAdditionalNamedInsured aniPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		Assert.assertFalse(aniPage.checkIfNameEditable(), "The when adding an Additional Named Insured from ContactManager, the name should not be editable.");
	}
	
	public boolean testNewAniInAB() throws Exception {
		 
         AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy");
         Login logMeIn = new Login(driver);
         logMeIn.login(abUser.getUserName(), abUser.getUserPassword());

         TopMenuAB getToSearch = new TopMenuAB(driver);
         getToSearch.clickSearchTab();

         AdvancedSearchAB searchMe = new AdvancedSearchAB(driver);
         AdvancedSearchResults searchResult = searchMe.searchByFirstNameLastNameAnyAddress(this.myPolicy.aniList.get(0).getPersonFirstName(), this.myPolicy.aniList.get(0).getPersonLastName());
         if (searchResult != null) {
        	 new GuidewireHelpers(driver).logout();
             return true;
         } else {
        	 new GuidewireHelpers(driver).logout();
        	 return false;
         } 
	}	
}
