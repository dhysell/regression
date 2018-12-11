package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;

import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.DateUtils;

import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;

import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.PAs;

import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.PAsHelper;


/**
* @Author sbroderick
* @Requirement Agents and PA's should be able to relate a new policy member with the PNI.
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:w:/r/sites/TeamNucleus/_layouts/15/Doc.aspx?sourcedoc=%7B08E885C9-F97A-4A4F-82F4-36E013DC2FC0%7D&file=DE8180%20-%20CM%20-%20HOTFIX%20PA%20or%20Agent%20Cannot%20add%20the%20relationship%20to%20PNI%20on%20a%20new%20member%20on%20a%20policy%20change.docx&action=default&mobileredirect=true">Documentation for DE8180</a>
* @DATE Nov 14, 2018
*/
public class DE8180_PaAgentCantAddRelationshipToPniOnNewMemberPolicyChange extends BaseTest{
		
	@Test
	public void testDE8180() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
	    WebDriver driver = buildDriver(cf);
	    		
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();	
		
		PLPolicyLocationProperty loc1Bldg1 = new PLPolicyLocationProperty();
		loc1Bldg1.setpropertyType(PropertyTypePL.ResidencePremises);
	
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		
		locOnePropertyList.add(loc1Bldg1);
		locationsList.add(new PolicyLocation(locOnePropertyList, new AddressInfo()));	
		Agents agentInfo = AgentsHelper.getAgentWithPA();
		
		GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withAgent(agentInfo)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Mr", "Pni")
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPolicyLocations(locationsList)
				.build(GeneratePolicyType.PolicyIssued);	
		
		
		PAs pa = PAsHelper.getPAInfoByAgent(myPolicy.agentInfo.getAgentFullName());
		Login login = new Login(driver);
		login.loginAndSearchPolicyByAccountNumber(pa.getPauserName(), "gw", myPolicy.accountNumber);
		
		StartPolicyChange policyChange = new StartPolicyChange(driver);
		policyChange.startPolicyChange("Add New Member.", DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter));
		  
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        
        Contact abContact = new Contact("Kid", myPolicy.pniContact.getLastName(), Gender.Male, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -17));
        abContact.setAddress(myPolicy.pniContact.getAddress());
        abContact.setRelationToInsured(RelationshipToInsured.Child);
        myPolicy.additionalMembersToAddToMembershipList.add(abContact);
        mySquire.policyMembers.add(abContact);
        
        GenericWorkorderPolicyMembers policyMembersPage = new GenericWorkorderPolicyMembers(driver);
        policyMembersPage.fillOutPLHouseholdMembersQQ(myPolicy);
        if(policyMembersPage.getPolicyHouseholdMembersTableRow(abContact.getLastName())<=0) {
        	Assert.fail("The newly added policy member should be found in the Policy Members table, please ensure that the Policy Member was able to be added properly.");
        }
        policyMembersPage.clickPolicyHolderMembersByName(abContact.getFirstName());
        boolean relationshipDropDownExists = policyMembersPage.selectRelationshipToInsured(RelationshipToInsured.Child);
        Assert.assertTrue(relationshipDropDownExists, "The Relationship dropdown should exist on the PolicyMember Page.");
	}
}
