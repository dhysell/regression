package previousProgramIncrement.pi1_041918_062718.f106_SelectionOfLienHoldersInPolicycenter;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;


/**
* @Author sbroderick
* @Requirement The state is not required when searching a lienholder.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/212651066044">Lien Search Remove Requirement to Search by State</a>
* @Description Entering a state is no longer required when searching a lienholder.
* @DATE Apr 24, 2018
*/
public class US14780RemoveStateRequirementForLienSearch extends BaseTest {
	private WebDriver driver;
	@Test
	public void testLienSearchDoesNotNeedState() throws Exception {
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
				.withInsFirstLastName("Squire", "AdditionalInterest")
				.build(GeneratePolicyType.FullApp);
		
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicyObjPL.agentInfo.getAgentUserName(), myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg1AddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		loc1Bldg1AddInterest.setCompanyName("Wells Fargo Bank");
		loc1Bldg1AddInterest.setFirstMortgage(true);
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
        
		loc1Bldg1.setPolicyLocationBuildingAdditionalInterestArrayList(loc1Bldg1AdditionalInterests);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        assertTrue(searchPC.searchAddressBook(true, null, null, null, null, loc1Bldg1AddInterest.getCompanyName(), loc1Bldg1AddInterest.getAddress().getLine1(),  null, null, null, CreateNew.Do_Not_Create_New).isFound(), "When Searching for a lienholder, the state should not be required.  See Account number: " + myPolicyObjPL.accountNumber);
    }
	
	
	
}
