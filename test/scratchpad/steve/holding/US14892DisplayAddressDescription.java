package scratchpad.steve.holding;

import static org.testng.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.pagelinks.PageLinks;
import repository.ab.search.AdvancedSearchAB;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TaxReportingOption;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.AdvancedSearchResults;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.gw.helpers.StringsUtils;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.helpers.AbUserHelper;

/**
* @Author sbroderick
* @Requirement When searching a Lienholder, the address description will display in the search results.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/213532553876">Display address description to PC Users.</a>
* @Description When searching a Lienholder, the address description will display in the search results.
* @DATE Apr 24, 2018
*/
public class US14892DisplayAddressDescription extends BaseTest{
	private WebDriver driver;
	@Test
	public void testLienAddressDescriptionIsDisplayedInSearchResults() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
		driver = buildDriver(cf);
		
        AdvancedSearchAB searchLien = new AdvancedSearchAB(driver);
		searchLien.loginAndGetToSearch(AbUserHelper.getRandomDeptUser("Policy"));
		AdvancedSearchResults lienSearchResults = searchLien.getRandomLienholderByName(ContactSubType.Company, "Blue");
        ContactDetailsBasicsAB contactPage = new ContactDetailsBasicsAB(driver);
		String tin = contactPage.getTin();
		AddressInfo lienAddress = StringsUtils.contactDetailsAddressStringParser(contactPage.getContactDetailsBasicsContactAddress());
		if(tin.length() < 9) {
			tin = NumberUtils.generateRandomNumberDigits(9)+"";
			contactPage.setContactDetailsBasicsTIN(tin);
			contactPage.clickContactDetailsBasicsUpdateLink();
		} else {
			contactPage.clickContactDetailsBasicsCancel();
		}

        PageLinks tabLinks = new PageLinks(driver);
		tabLinks.clickContactDetailsBasicsAddressLink();

        ContactDetailsAddressesAB addressTab = new ContactDetailsAddressesAB(driver);
		addressTab.clickEdit();
		// Date stamp taken from https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html  "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
		// explanation:  "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
		String uniqueId = "Unique Identifier is " + DateUtils.dateFormatAsString("yyyy.MM.dd 'at' HH:mm:ss", DateUtils.getCenterDate(driver, ApplicationOrCenter.ContactManager));
		addressTab.setContactDetailsAddressesDescription(uniqueId);
		addressTab.clickUpdate();
		
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
				.withInsFirstLastName("Squire", "AdditionalInterest")
				.build(GeneratePolicyType.FullApp);
		
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
        
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(lienSearchResults.getContactSubType());
		loc1Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Bldg1AddInterest.setCompanyName(lienSearchResults.getLastNameOrCompanyName());
		loc1Bldg1AddInterest.setFirstMortgage(true);
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
        
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg1AdditionalInterests);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        searchPC.searchAddressBook(true, tin, TaxReportingOption.TIN, null, null, loc1Bldg1AddInterest.getCompanyName(), loc1Bldg1AddInterest.getAddress().getLine1(),  null, null, null, CreateNew.Do_Not_Create_New);
        if(!searchPC.getSearchResultDescription(uniqueId).equals(uniqueId)) {
        	Assert.fail("When searching a Lienholder, the Description should show the Address Description in ContactManager.");
        }
        searchPC.searchAddressBook(true, tin, TaxReportingOption.TIN, null, null, loc1Bldg1AddInterest.getCompanyName(), loc1Bldg1AddInterest.getAddress().getLine1(), lienAddress.getCity(), lienAddress.getState(), lienAddress.getZip(), CreateNew.Do_Not_Create_New);
        assertFalse(searchPC.checkIfCreateNewButtonExists(), "After search a Lienholder the create new was found on the page. The Create New button should not exist.");
    }
}
