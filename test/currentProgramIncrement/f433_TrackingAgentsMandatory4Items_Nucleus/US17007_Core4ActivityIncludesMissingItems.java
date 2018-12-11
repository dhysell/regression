package currentProgramIncrement.f433_TrackingAgentsMandatory4Items_Nucleus;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ActivtyRequestType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.activity.GenericActivityPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.entities.PAs;
import persistence.globaldatarepo.helpers.AgentsHelper;
import persistence.globaldatarepo.helpers.PAsHelper;

/**
* @Author sbroderick
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Nov 29, 2018
* 
* Acceptance Criteria
make sure all four items (MS, Money, Photo's and Signature) are available to add or select within the activity 
make sure that the four items can later be tracked by reporting 
make sure the "mandatory" information isn't changeable

* 
*/
@Test(groups = {"ClockMove"})
public class US17007_Core4ActivityIncludesMissingItems extends BaseTest{
	
	private GeneratePolicy myPolicy = null;
	private String activityMandatory = "Yes";
	private String activityPriority = "Urgent";
	
	@Test
	public void core4Activity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		Agents agent = AgentsHelper.getAgentWithPA(); 
		// GENERATE POLICY
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		locationsList.add(new PolicyLocation(locOnePropertyList));
		SquireLiability liabilitySection = new SquireLiability();
		
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = liabilitySection;
		
		Squire mySquire = new Squire(SquireEligibility.Country);
		mySquire.propertyAndLiability = myPropertyAndLiability;
		
		this.myPolicy = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withAgent(agent)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		Login login = new Login(driver);
		login.loginAndSearchPolicyByAccountNumber(myPolicy.underwriterInfo.getUnderwriterUserName(), myPolicy.underwriterInfo.underwriterPassword, myPolicy.accountNumber);
		
		ActionsPC actions = new ActionsPC(driver);
        actions.requestActivity(ActivtyRequestType.GetMissingCore4Items);

        GenericActivityPC activityPage = new GenericActivityPC(driver);
        //Asserts that the activity is mandatory.
        Assert.assertTrue(activityPage.getMandatoryText().equals(activityMandatory), "The Core 4 Activity is not marked mandatory and should be according to requirements.");
        //Asserts that the activity has an urgent priority
        Assert.assertTrue(activityPage.getPriority().equals(activityPriority), "The Core 4 Activity should have the priority of Urgent and is not marked so.");
        activityPage.clickAddInformationToNotes();
        System.out.println(activityPage.getText());
        Assert.assertTrue(activityPage.getText().contains("The following items are missing:"),"The Core 4 Activity must contain a text area for freeform explanation.");

        //Assign to PA
        PAs pa = PAsHelper.getPAInfoByAgent(agent.getAgentFullName());
        activityPage.setAssignTo(pa.getPauserName());
        System.out.println(activityPage.getAssignTo() + "contains" + pa.getPalastName());
        Assert.assertTrue(activityPage.getAssignTo().contains(pa.getPalastName()), "The Core 4 Activity must be able to be assigned to a PA.");

        //Assign to Agent
        activityPage.setAssignTo(agent.agentUserName);
        Assert.assertTrue(activityPage.getAssignTo().contains(agent.getAgentLastName()), "The Core 4 Activity must be able to be assigned to an agent.");
        activityPage.setSubject("Get your $#$%^ together.");
        activityPage.clickOK();
        
        //verify activity was created
        SideMenuPC sideMenu = new SideMenuPC(driver);
        sideMenu.clickSideMenuToolsDocuments();
        sideMenu.clickSideMenuToolsSummary();
        
        PolicySummary policySummary = new PolicySummary(driver);
        String activityAssignedTo = policySummary.getActivityAssignment("Get Missing Core 4 Items");
        Assert.assertTrue(activityAssignedTo.contains(agent.getAgentLastName()), "The Core 4 activity was not assigned to the agent.");
	}
}
