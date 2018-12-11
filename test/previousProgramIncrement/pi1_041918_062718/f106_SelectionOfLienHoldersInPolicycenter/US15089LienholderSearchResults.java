package previousProgramIncrement.pi1_041918_062718.f106_SelectionOfLienHoldersInPolicycenter;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.search.SearchResultsReturnPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;


/**
* @Author sbroderick
* @Requirement Only Active Lienholders and Active Lienholder addresses will appear in the lienholder search results.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/219079762144">US15089 Only show those with lienholder role and lien numnber when searching for lienholders.</a> 
* @DATE May 14, 2018
*/
@Test(groups= {"ClockMove"})
public class US15089LienholderSearchResults extends BaseTest {
	AdditionalInterest loc1Property1AdditionalInterest = null;
	WebDriver driver;
	@Test
	public void testLiensRequirementsToBeFoundInSearchResults() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact newLienholder = new GenerateContact.Builder(driver)
				.withCompanyName("TerminatedLienholder")
				.withRoles(rolesToAdd)
				.withUniqueName(true)
				.build(GenerateContactType.Company);

        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickEdit();
		basicsPage.setLienholderTerminationDate(DateUtils.dateFormatAsString("MM/dd/yyyy", DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Day, -1)));
		basicsPage.clickUpdate();
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(newLienholder.companyName, newLienholder.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(newLienholder.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
        GenerateContact myLienholderNoNumberObj = new GenerateContact.Builder(driver)
				.withCompanyName("LienholderNoNumber")
				.withRoles(rolesToAdd)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		
		AdditionalInterest lienholderNoNumber = new AdditionalInterest(myLienholderNoNumberObj.companyName, myLienholderNoNumberObj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber("");
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		

		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);		
		
		locOnePropertyList.add(loc1Bldg1);			
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		

		
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "Policyholder")
				.build(GeneratePolicyType.QuickQuote);
		
        driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		loc1Bldg1AdditionalInterests.add(loc1Property1AdditionalInterest);
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg1AdditionalInterests);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        SearchResultsReturnPC searchResults = searchPC.searchAddressBook(true, null, null, null, null, loc1Property1AdditionalInterest.getCompanyName(), loc1Property1AdditionalInterest.getAddress().getLine1(), loc1Property1AdditionalInterest.getAddress().getCity(), loc1Property1AdditionalInterest.getAddress().getState(), loc1Property1AdditionalInterest.getAddress().getZip(), CreateNew.Do_Not_Create_New);
        assertFalse(searchResults.isFound(), "The Lienholder should not be found in the search results because the Lienholder has no lienholder number.");

        searchPC = new SearchAddressBookPC(driver);
		searchResults = searchPC.searchAddressBook(true, null, null, null, null, lienholderNoNumber.getCompanyName(), lienholderNoNumber.getAddress().getLine1(),  lienholderNoNumber.getAddress().getCity(), lienholderNoNumber.getAddress().getState(), lienholderNoNumber.getAddress().getZip(), CreateNew.Do_Not_Create_New);
		assertFalse(searchResults.isFound(), "Terminated Lienholders should not be found in the Lienholder Search results.");
    }
	
	@Test
	public void lienSearchResultsShowActiveAddress() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
		ArrayList<AddressInfo> addresses = new ArrayList<AddressInfo>();
		addresses.add(new AddressInfo(true));
		addresses.add(new AddressInfo(true));
		ArrayList<ContactRole> rolesToAdd = new ArrayList<ContactRole>();
		rolesToAdd.add(ContactRole.Lienholder);

        GenerateContact myContactLienLoc1Obj = new GenerateContact.Builder(driver)
				.withCompanyName("ActiveLienholder")
				.withAddresses(addresses)
				.withGeneratedLienNumber(true)
				.withRoles(rolesToAdd)
				.withUniqueName(true)
				.build(GenerateContactType.Company);
		
		//retire one address.
        ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(driver);
		basicsPage.clickEdit();
		basicsPage.clickContactDetailsBasicsAddressLink();
        ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(driver);
		addressPage.retireAddress(addresses.get(1).getLine1());
		addressPage.clickUpdate();
		
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		ClockUtils.setCurrentDates(driver, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager), DateAddSubtractOptions.Day, 1));
		
		driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();		

		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();

		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);		
		
		locOnePropertyList.add(loc1Bldg1);			
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));		

		
        SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
        myPropertyAndLiability.locationList = locationsList;

        Squire mySquire = new Squire(SquireEligibility.City);
        mySquire.propertyAndLiability = myPropertyAndLiability;

        GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "Policyholder")
				.build(GeneratePolicyType.QuickQuote);
		
        driver.quit();
		
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		loc1Bldg1AdditionalInterests.add(loc1Property1AdditionalInterest);
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg1AdditionalInterests);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);        											
        SearchResultsReturnPC searchResults = searchPC.searchAddressBook(true, null, null, null, loc1Property1AdditionalInterest.getCompanyName(), addresses.get(1).getLine1(), null, null, null, CreateNew.Do_Not_Create_New);
        assertTrue(searchResults.isFound(), "The Lienholder address at "+addresses.get(1).getLine1()+" should be found in the search results because the Lienholder is active, and we changed this to show the retired addresses.");	
	}	
}
