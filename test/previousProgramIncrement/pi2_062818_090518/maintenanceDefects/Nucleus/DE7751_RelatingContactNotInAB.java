package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Nucleus;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipsAB;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfoAdditionalNamedInsured;

import java.util.ArrayList;

public class DE7751_RelatingContactNotInAB extends BaseTest{


	@Test
	public void testRelateContactsNotInAB() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		PolicyInfoAdditionalNamedInsured ani = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person, "Wild", "Bear", AdditionalNamedInsuredType.Spouse, new AddressInfo(true));
		ani.setNewContact(CreateNew.Create_New_Always);
		ArrayList<PolicyInfoAdditionalNamedInsured> aniList = new ArrayList<>();
		aniList.add(ani);  

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		PLPolicyLocationProperty locationOneProperty = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		locOnePropertyList.add(locationOneProperty);
		locationsList.add(new PolicyLocation(locOnePropertyList));

		SquireLiability liabilitySection = new SquireLiability();      

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;

		Squire mySquire = new Squire();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withInsFirstLastName("Related", "Contact")
				.withProductType(ProductLineType.Squire)
				.withANIList(aniList)
				.withMembershipDuesForPrimaryNamedInsured(true)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.build(GeneratePolicyType.QuickQuote);


		driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

		polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickANI();

		GenericWorkorderPolicyInfoAdditionalNamedInsured aniPage = new GenericWorkorderPolicyInfoAdditionalNamedInsured(driver);
		aniPage.clickRelatedContactsTab();
		aniPage.addRelatedContact(myPolicy.pniContact, RelationshipsAB.SpouseFor);
		GuidewireHelpers gwHelpers = new GuidewireHelpers(driver);
		Assert.assertFalse(gwHelpers.errorMessagesExist(), "An Error occurred while adding a related contact on the related contacts tab.");

	}
}
