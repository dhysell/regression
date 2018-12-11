package previousProgramIncrement.pi2_062818_090518.maintenanceDefects.Nucleus;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
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
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderMembershipMembers;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;

import java.util.ArrayList;

//This also tests DE7796
public class DE7755_MakeContactsOnMembershipSearchSelectable extends BaseTest{
	
	
	
	@Test
	public void searchContactwithoutABUIDForMembership() throws Exception {
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
        Contact member2 = new Contact();
               
        mySquire.policyMembers.add(member);
        mySquire.policyMembers.add(member2);

        GeneratePolicy myPolicy = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withInsFirstLastName("Member", "Defects")
                .withProductType(ProductLineType.Squire)
                .withMembershipDuesForPrimaryNamedInsured(true)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .build(GeneratePolicyType.QuickQuote);

        driver.quit();
        
        myPolicy.additionalMembersToAddToMembershipList.add(member);
        myPolicy.additionalMembersToAddToMembershipList.add(member2);
        
		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		Login login = new Login(driver);
		login.loginAndSearchJob(myPolicy.agentInfo.getAgentUserName(), myPolicy.agentInfo.getAgentPassword(), myPolicy.accountNumber);

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuMembershipMembers();
        
        GenericWorkorderMembershipMembers membershipPage = new GenericWorkorderMembershipMembers(driver);
        membershipPage.setMembershipMembers(myPolicy, true);
        boolean found = membershipPage.checkIfMembershipMemberExistsInTable(member);
        boolean found1 = membershipPage.checkIfMembershipMemberExistsInTable(member2); 
        if(!found && found1) {
        	Assert.fail("Two members were attempted to be added to the Members screen. Please investigate DE7796 or DE7755.");
        }		
	}
}
