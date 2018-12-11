package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Nucleus;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.search.SearchAddressBookPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;

import java.util.ArrayList;

/**
* @Author sbroderick
* @Requirement Users should be able to select person they already created or any other internal contact.
* @RequirementsLink <a href="https://rally1.rallydev.com/#/211690175148d/detail/defect/247707551544">No Select Button on Policy Members</a>
* @Description 
* @DATE Sep 6, 2018
*/
public class DE7825_NoSelectButtonOnPolicyMembers extends BaseTest{
	
	
	@Test
	public void DE7825_NoSelectButtonForFoundPolicyMembers() throws Exception {
		
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        WebDriver driver = buildDriver(cf);

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
        
        Contact member = new Contact();
        member.setRelationshipAB(RelationshipsAB.ChildWard);
        Contact member2 = new Contact();
        member2.setRelationshipAB(RelationshipsAB.ChildWard);
               
        mySquire.policyMembers.add(member);
        mySquire.policyMembers.add(member2);
        

        GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Pcmember", "Defects")
                .withProductType(ProductLineType.Squire)
                .withMembershipDuesForPrimaryNamedInsured(true)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);
        
        myPolicy.additionalMembersToAddToMembershipList.add(member);
        myPolicy.additionalMembersToAddToMembershipList.add(member2);

		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        
        GenericWorkorderPolicyMembers policyMembersPage = new GenericWorkorderPolicyMembers(driver);
        policyMembersPage.clickRemoveMember(member2.getLastName());
        policyMembersPage.clickSearch();
        SearchAddressBookPC addbookSearch = new SearchAddressBookPC(driver);        
        if(addbookSearch.searchAddressBook(myPolicy.basicSearch, null, null, member2.getFirstName(), member2.getLastName(), null, member2.getAddress().getLine1(), member2.getAddress().getCity(), member2.getAddress().getState(), member2.getAddress().getZip(), CreateNew.Create_New_Only_If_Does_Not_Exist).getSelectToClick() == null) {
        	Assert.fail("After searching the previously added Policy Member, the search was unable to find the Select to select the found contact.");
        }                   
	}
}
