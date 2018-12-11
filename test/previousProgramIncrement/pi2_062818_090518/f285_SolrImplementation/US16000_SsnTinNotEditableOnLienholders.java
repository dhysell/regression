package previousProgramIncrement.pi2_062818_090518.f285_SolrImplementation;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

import java.util.ArrayList;

public class US16000_SsnTinNotEditableOnLienholders extends BaseTest{
	
	private GeneratePolicy paPolicy;
	private AdditionalInterest loc1Bldg1AddInterest = null;
	private WebDriver driver;
	
	@Test
	public void us16000SsnTinNotEditable() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		this.paPolicy = createPolicies(AgentsHelper.getAgentWithPA());
				
		Login login = new Login(driver);
		login.loginAndSearchJob(this.paPolicy.agentInfo.getAgentUserName(), this.paPolicy.agentInfo.getAgentPassword(), paPolicy.accountNumber);
		createNewCheck();
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.searchForContact(true, loc1Bldg1AddInterest);
		GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
		//The Asserts are in the method below.
		aiPage.checkIfLeinHolderPCRIsEditable();
		driver.quit();
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
		
		return myPolicyObjPL;
	}
	
	private boolean createNewCheck() throws Exception {

        new GuidewireHelpers(driver).editPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuSquirePropertyDetail();
        
        ArrayList<AdditionalInterest> loc1Bldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
        
		this.loc1Bldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1Bldg1AddInterest.setFirstMortgage(true);
		loc1Bldg1AdditionalInterests.add(loc1Bldg1AddInterest);

        GenericWorkorderBuildings building = new GenericWorkorderBuildings(driver);
        building.clickEdit();
        building.clickSearch();
        SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
        searchPC.searchAddressBook(true, null, null, null, null, loc1Bldg1AddInterest.getCompanyName(), loc1Bldg1AddInterest.getAddress().getLine1(),  loc1Bldg1AddInterest.getAddress().getCity(), loc1Bldg1AddInterest.getAddress().getState(), loc1Bldg1AddInterest.getAddress().getZip(), CreateNew.Do_Not_Create_New);
        return searchPC.checkIfCreateNewButtonExists();
	}

}
