package scratchpad.steve.holding;

import repository.ab.contact.ContactDetailsAddressesAB;
import repository.ab.contact.ContactDetailsBasicsAB;
import repository.ab.search.AdvancedSearchAB;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.driverConfiguration.DriverBuilder;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
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
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import persistence.globaldatarepo.entities.AbUsers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AbUserHelper;
import persistence.globaldatarepo.helpers.AgentsHelper;

import java.util.ArrayList;
public class US15128EditableFieldsForAI extends BaseTest {

	GeneratePolicy paPolicy = null;
	AdditionalInterest loc1Bldg1AddInterest = null;
	SoftAssert softAssert = new SoftAssert();
	ArrayList<DeliveryOptionType> deliveryOptions = new ArrayList<>();
	WebDriver driver;

	@Test
	public void testCreateNewNotAvailableForFieldEmployees() throws Exception {	
		this.paPolicy = createPolicies(AgentsHelper.getAgentWithPA());


		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Login login = new Login(driver);
		login.loginAndSearchJob(paPolicy.agentInfo.getAgentUserName(), paPolicy.agentInfo.getAgentPassword(), paPolicy.accountNumber);
		softAssert.assertFalse(addAdditionalInterest(), "After adding an additional Interest to a lienholder on a submission, the delivery options should not show in AB. See Account "+paPolicy.accountNumber+".");
		softAssert.assertAll();
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

	private boolean addAdditionalInterest() throws Exception {
		this.deliveryOptions.add(DeliveryOptionType.PCA);
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
		aiPage.addDeliverionOptions(deliveryOptions);
		aiPage.setAdditionalInterestsLoanNumber("LH1234");
		aiPage.selectBuildingsPropertyAdditionalInterestsInterestType("Lienholder");
		aiPage = new GenericWorkorderAdditionalInterests(driver);
		aiPage.checkBuildingsPropertyAdditionalInterestsFirstMortgage();
		aiPage.clickUpdate();

		building = new GenericWorkorderBuildings(driver);
		building.clickOK();

		AbUsers abUser = AbUserHelper.getRandomDeptUser("Policy Services");

		WebDriver newDriver = new DriverBuilder().buildGWWebDriver(new Config(ApplicationOrCenter.ContactManager));

		AdvancedSearchAB advancedSearchAB = new AdvancedSearchAB(newDriver);
		advancedSearchAB.loginAndSearchContact(abUser, null, loc1Bldg1AddInterest.getCompanyName(), loc1Bldg1AddInterest.getAddress().getLine1(), loc1Bldg1AddInterest.getAddress().getState());
		ContactDetailsBasicsAB basicsPage = new ContactDetailsBasicsAB(newDriver);
		basicsPage.clickContactDetailsBasicsAddressLink();
		ContactDetailsAddressesAB addressPage = new ContactDetailsAddressesAB(newDriver);
		ArrayList<String> abDeliveryOptions = addressPage.getDeliveryOptions();
		for(String deliveryOption : abDeliveryOptions) {
			if(deliveryOption.equals(this.deliveryOptions.get(0).getTypeValue())) {
				newDriver.quit();
				return true;
			}
		}
		newDriver.quit();
		return false;
	}

	@Test(dependsOnMethods = {"testCreateNewNotAvailableForFieldEmployees"})
	public void testTransitionTeamAbleToEditAddress() throws Exception {
		if(this.paPolicy == null) {
			this.paPolicy = createPolicies(AgentsHelper.getAgentWithPA());
		}
		AbUsers abUser = AbUserHelper.getRandomUserByTitle("Transition Specialist");

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Login login = new Login(driver);
		login.loginAndSearchJob(abUser.getUserName(), abUser.getUserPassword(), paPolicy.accountNumber);
		createNewCheck();
		Assert.assertTrue(testAddressIsEditable(), "Transition Team should be able to change the Lienholder address.");
	}

	private boolean testAddressIsEditable() {
		SearchAddressBookPC searchPC = new SearchAddressBookPC(driver);
		searchPC.searchForContact(true, loc1Bldg1AddInterest);
		GenericWorkorderAdditionalInterests aiPage = new GenericWorkorderAdditionalInterests(driver);
		return aiPage.changeAddressSelectListExists();
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
