package previousProgramIncrement.pi2_062818_090518.f285_SolrImplementation;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactRole;
import repository.gw.enums.CreateNew;
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
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

/**
* @Author sbroderick
* @Requirement Display Address line one on the lienholder search results
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/245110701240">US15999 Display Address Line on Lienholder Search</a>
* @Description 
* @DATE Aug 13, 2018
* 
* 
*/


/**
 * This test class also tests defect 7646.   * 
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B90C55B2C-E558-4E03-8F3D-975DE3F78CE1%7D&file=DE7646%20-%20CM%20-%20Wells%20Fargo%20Bank%20NA%20office%20%23.docx&action=default&mobileredirect=true">DE7646 Requirements Documentation.</a>
* @Description 
* @DATE Sep 6, 2018
*/
public class US15999_LienholderSearchAddressLineOne extends BaseTest {
	private WebDriver driver;
	private String env = "uat";
	
	@Test
	public void lienSearchResultsShowActiveAddressLineOne() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager, env);
		driver = buildDriver(cf);
		
		ArrayList<AddressInfo> addresses = new ArrayList<AddressInfo>();
		addresses.add(new AddressInfo(true));
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
				
		AdditionalInterest loc1Property1AdditionalInterest = new AdditionalInterest(myContactLienLoc1Obj.companyName, myContactLienLoc1Obj.addresses.get(0));
		loc1Property1AdditionalInterest.setLienholderNumber(myContactLienLoc1Obj.lienNumber);
		loc1Property1AdditionalInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1Property1AdditionalInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		
		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
			
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
      	searchPC.searchAddressBook(true, null, null, null, null, loc1Property1AdditionalInterest.getCompanyName(), addresses.get(2).getLine1(),  null, null, null, CreateNew.Do_Not_Create_New);
        assertTrue(searchPC.searchAddressBookByCompanyName(true, loc1Property1AdditionalInterest.getCompanyName(), addresses.get(2).getLine1(), addresses.get(2).getCity(), addresses.get(2).getState(), addresses.get(2).getZip(), CreateNew.Do_Not_Create_New), "The Lienholder address at "+addresses.get(2).getLine1()+ " should be found in the search results.");
        GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver); 
        aiPage.setAdditionalInterestsLoanNumber("LN"+ myPolicyObjPL.accountNumber);
        aiPage.checkBuildingsPropertyAdditionalInterestsFirstMortgage();
        aiPage.selectBuildingsPropertyAdditionalInterestsInterestType(AdditionalInterestType.LienholderPL.getValue());
        aiPage.clickUpdate();
        building = new GenericWorkorderBuildings(driver);
        aiPage = new GenericWorkorderAdditionalInterests(driver);
        aiPage.clickBuildingsPropertyAdditionalInterestsLink(loc1Property1AdditionalInterest.getCompanyName());
        //Assert to test that the address is the same address that was selected when selecting the lienholder. For Hotfix defect 7646.
        if(!aiPage.getBuildingsPropertyAdditionalInterestsLienHolderAddressLine1().equals(addresses.get(2).getLine1())){
        	Assert.fail("The Lienholder address changed from the selected address to a different address of the Lienholder. File a defect.");
        } 
	}	
}
