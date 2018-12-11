package previousProgramIncrement.pi1_041918_062718.nonFeatures.Achievers;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.exception.GuidewireException;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Activity;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.BatchHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyPreRenewalDirection;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;

/**
* @Author jlarsen
* @Requirement Renewal Activities are not assigning to the same user
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558466972d/detail/userstory/212676134784">US14803</a>
* @Description User Statement: 
As the PL PC Underwriter, I want the system to assign the Pre-Renewal Directions, Quoting Failed, and Blocked-Pending Renewal activities to the same user so that multiple users won't work on the same renewal.
Steps to get there:
Need an existing policy that has a Pre-Renewal Direction activity triggered on it.
Acceptance Criteria:
Ensure at day 80 a Pre-Renewal Direction Activity is generated and assigned to a PL PC underwriter user.
Ensure at day 80 for regular renewal and at day 65 for transition, a Quoting Failed activity should be assigned to the same PL PC underwriter who has the Pre-Renewal Direction activity.
Make sure there is an open Pre-Renewal Direction Activity
Ensure and Blocked-Pending Renewal activity is generated and assigned to the same PL PC underwriter that has a Pre-Renewal Direction activity and/or Quoting Failed activity.
Get a Blocked-Pending Renewal activity by having a Renewal policy as Not Taken 
* @DATE May 1, 2018
*/
@Test(groups= {"ClockMove"})
public class US14803_RenewalActivityAssignment extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObj = null;
    String activityAssignment = "";
	
	@Test
	public void renewalActivityAssignment() throws Exception {
		//create a 65 day policy.
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();

		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		locOneBuildingList.add(new PolicyLocationBuilding());
		PolicyLocation myLocation = new PolicyLocation(new AddressInfo(true), locOneBuildingList);
		
		locationsList.add(myLocation);

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        myPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPolTermLengthDays(65)
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
        new PolicyPreRenewalDirection(driver).createAnyPreRenewalDirection(myPolicyObj);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Policy_Renewal_Start);
		new BatchHelpers(cf).runBatchProcess(BatchProcess.Jump_Start_Renewals);
        PolicySummary summary = new PolicySummary(driver);
		String quotingFailedActivityAssignment = "";
		boolean found = false;
		int count = 0;
		do {
			try {
				quotingFailedActivityAssignment = summary.getActivityAssignment("Quoting failed");
				found = true;
			} catch (GuidewireException g) {
				new SideMenuPC(driver).clickSideMenuToolsDocuments();
                new SideMenuPC(driver).clickSideMenuToolsSummary();
			}
			count++;
		} while(count < 10 && found == false);
		if(!found) {
			Assert.fail("WAS NOT ABLE TO GET RENWAL ACTIVITIES TO GENERATE IN TIME TO CONTINUE TEST.");
		}
		
		List<Activity> activityList = summary.getCurrentActivites_BasicInfo();
		
		boolean activityfound = false;
		for(Activity activity : activityList) {
			if(activity.getSubject().equals("Pre-Renewal Review for Businessowners")) {
				if(activity.getAssignedTo().equals(quotingFailedActivityAssignment)) {
					activityfound = true;
					break;
				}
			}
		}
		
		Assert.assertTrue(activityfound, "COMMON ACTIVITIES WERE NOT ASSIGNED TO THE SAME USER.\n" + "Quoting failed: assigned to - " + quotingFailedActivityAssignment);
	}
}


