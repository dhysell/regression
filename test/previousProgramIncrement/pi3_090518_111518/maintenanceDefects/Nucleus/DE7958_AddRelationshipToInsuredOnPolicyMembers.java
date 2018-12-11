package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.Gender;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipToInsured;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
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


public class DE7958_AddRelationshipToInsuredOnPolicyMembers extends BaseTest{

	private GeneratePolicy mySQPolicyObjPL = null;
	
	@Test()
	public void testIssueSquirePolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

        SquirePropertyAndLiability myProeprty = new SquirePropertyAndLiability();
        myProeprty.locationList = locationsList;


        Squire mySquire = new Squire(SquireEligibility.City);

        Agents agent = AgentsHelper.getAgentWithPA();

        this.mySQPolicyObjPL = new GeneratePolicy.Builder(driver)
                .withSquire(mySquire)
                .withAgent(agent)
                .withProductType(ProductLineType.Squire)
                .withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
                .withInsFirstLastName("Modifier", "Change")
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	
        Contact abContact = new Contact("Buddy", mySQPolicyObjPL.pniContact.getLastName(), Gender.Male, DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -30));
        abContact.setAddress(mySQPolicyObjPL.pniContact.getAddress());
        abContact.setRelationToInsured(RelationshipToInsured.Child);
        mySQPolicyObjPL.additionalMembersToAddToMembershipList.add(abContact);
        mySquire.policyMembers.add(abContact);
        
        PAs pa = PAsHelper.getPAInfoByAgent(mySQPolicyObjPL.agentInfo.getAgentLastName());
        
        Login login = new Login(driver);
		login.loginAndSearchPolicyByAccountNumber(pa.getPauserName(), "gw", mySQPolicyObjPL.accountNumber);

		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("Add PolicyMember",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		
		SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuHouseholdMembers();
        
        GenericWorkorderPolicyMembers policyMembersPage = new GenericWorkorderPolicyMembers(driver);
        policyMembersPage.fillOutPLHouseholdMembersQQ(mySQPolicyObjPL);
        if(policyMembersPage.getPolicyHouseholdMembersTableRow(abContact.getLastName())<=0) {
        	Assert.fail("The newly added policy member should be found in the Policy Members table, please ensure that the Policy Member was able to be added properly.");
        }
	}
}
