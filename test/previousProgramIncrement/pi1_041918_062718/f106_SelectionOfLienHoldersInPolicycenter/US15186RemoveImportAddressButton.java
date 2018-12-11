package previousProgramIncrement.pi1_041918_062718.f106_SelectionOfLienHoldersInPolicycenter;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DeliveryOptionType;
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
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

import java.util.ArrayList;

/**
* @Author sbroderick
* @Requirement Ensure that the button is no longer visible on lienholders
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/userstory/221853486180">Remove the import address button from the lienholder selection page in PC</a>
* @Description Ensure that the button is no longer visible on lienholders
* @DATE Jun 1, 2018
*/
public class US15186RemoveImportAddressButton extends BaseTest {
	
	GeneratePolicy paPolicy = null;
	AdditionalInterest loc1Bldg1AddInterest = null;
	ArrayList<DeliveryOptionType> deliveryOptions = new ArrayList<>();
	private WebDriver driver;

	@Test
	public void testImportAddressDoesNotExistOnAIPage() throws Exception {
		this.paPolicy = createPolicies(AgentsHelper.getRandomAgent());
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		Login login = new Login(driver);
		login.loginAndSearchJob(this.paPolicy.agentInfo.getAgentUserName(), this.paPolicy.agentInfo.getAgentPassword(), paPolicy.accountNumber);
		Assert.assertFalse(testimportAddressButtonDoesNotExist(), "The import address button should not be available for any users.");
	}
	
	private GeneratePolicy createPolicies(Agents agent) throws Exception {
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
                .withAgent(agent)
				.withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Squire", "TestAI")
				.build(GeneratePolicyType.QuickQuote);
		driver.quit();
		return myPolicyObjPL;
	}
	
	private boolean testimportAddressButtonDoesNotExist() throws Exception {
			
        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        
		AdditionalInterest loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg1AddInterest.setFirstMortgage(true);		
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);
		
		 GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
	     building.clickEdit();
	     building.clickSearch();

		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.searchForContact(true, loc1Bldg1AddInterest);
		GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
		
		return aiPage.importAddressbuttonExists();
		 
	}
}
