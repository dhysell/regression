package previousProgramIncrement.pi1_041918_062718.f106_SelectionOfLienHoldersInPolicycenter;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
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
* @Requirement Make lienholder related contacts tab read only.  We don't want anyone to add relationships through this tab. Relationships are added in ContactManager.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/225379118676">Make lienholder related contacts tab read only</a>
* @Description 
* @DATE Jun 1, 2018
* @throws Exception
*/

public class US15262MakeLienholderRelatedContactTabReadOnly extends BaseTest {
	
	GeneratePolicy policy = null;
	private WebDriver driver;
	
	@Test
	public void testRelatedContactTabIsNowReadOnly() throws Exception {
		this.policy = createPolicies();
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.loginAndSearchJob(policy.agentInfo.getAgentUserName(), policy.agentInfo.getAgentPassword(), policy.accountNumber);
		Assert.assertFalse(addAdditionalInterest(), "The RelatedTab on the add AdditionalInterest page should be read only. "+policy.accountNumber+".");
	}
	
	private GeneratePolicy createPolicies() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
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
				.withInsFirstLastName("Squire", "TestAI")
				.build(GeneratePolicyType.QuickQuote);
		driver.quit();
		return myPolicyObjPL;
	}
	
	private boolean addAdditionalInterest() throws Exception {
		
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Person);
		loc1Bldg1AddInterest.setFirstMortgage(true);		
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
		
		 GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
	     building.clickEdit();
	     building.clickSearch();

		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.searchForContact(true, loc1Bldg1AddInterest);
		GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
		aiPage.setAdditionalInterestsLoanNumber("LH1234");
		aiPage.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
		aiPage = new GenericWorkorderAdditionalInterests(driver);
		aiPage.checkBuildingsPropertyAdditionalInterestsFirstMortgage();
		aiPage.clickRelatedContactsTab();
		return aiPage.checkIfRelatedContactsAddButtonExists();
   }
}
