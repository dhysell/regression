package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GenerateContactType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GenerateContact;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import persistence.globaldatarepo.helpers.AbUserHelper;

public class DE8172_CannotAddNewPersonWithPolicyMemberAddress extends BaseTest{

	/**
	* @Author sbroderick
	* @Requirement As a PC user, I need to be able to add new persons with policy member address.
	* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B2773EC2B-5D40-4584-9724-A2189AE3299C%7D&file=DE8172%20-%20CM%20-%20HOTFIX%20Cannot%20Add%20a%20New%20Person%20with%20Policy%20Member%20Address.docx&action=default&mobileredirect=true">DE8172 Documentation</a>
	* @DATE Nov 14, 2018
	* @throws Exception
	*/
	@Test
	public void testNewPerson() throws Exception {
		Config cf = new Config(ApplicationOrCenter.ContactManager);
        WebDriver driver = buildDriver(cf);
        
        GenerateContact myContactObj = new GenerateContact.Builder(driver)
				.withCreator(AbUserHelper.getRandomDeptUser("Policy"))
				.withFirstLastName("Sheldon", "", "Cooper")
				.withUniqueName(true)
				.withPrimaryAddress(new AddressInfo(true))
				.build(GenerateContactType.Person);

        driver.get(cf.getUrlOfCenter(ApplicationOrCenter.PolicyCenter));

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
                .withInsFirstLastName("Pcmember", "Defects")
                .withProductType(ProductLineType.Squire)
                .withMembershipDuesForPrimaryNamedInsured(true)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);
        
        Contact abContact = new Contact(myContactObj.firstName, myContactObj.lastName, myContactObj.gender, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -30));
        abContact.setAddress(myPolicy.pniContact.getAddress());
        abContact.setRelationToInsured(RelationshipToInsured.Child);
        myPolicy.additionalMembersToAddToMembershipList.add(abContact);
        mySquire.policyMembers.add(abContact);
        
        Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
        
        GenericWorkorderPolicyMembers policyMembersPage = new GenericWorkorderPolicyMembers(driver);
        policyMembersPage.fillOutPLHouseholdMembersQQ(myPolicy);
        if(policyMembersPage.getPolicyHouseholdMembersTableRow(abContact.getLastName())<=0) {
        	Assert.fail("The newly added policy member should be found in the Policy Members table, please ensure that the Policy Member was able to be added properly.");
        }
	}
}
