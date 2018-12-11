package previousProgramIncrement.pi3_090518_111518.f348_MVR_Verisk;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.RelationshipsAB;
import repository.gw.enums.SRPIncident;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssues_PL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers;
import persistence.globaldatarepo.entities.VeriskMvr;
import persistence.globaldatarepo.helpers.Verisk_MVRHelpers;

public class US16210_MVRUWIssues extends BaseTest {

	private GeneratePolicy myPolicyObject = null;
	private WebDriver driver;

	@Test(enabled=true)
	public void generatePolicyWithUMandUIMselected() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SoftAssert softassert = new SoftAssert();

		List<UnderwriterIssues_PL> uwIssuesList = new ArrayList<UnderwriterIssues_PL>();
		//        uwIssuesList.add(UnderwriterIssues_PL.MVRforeachdriver_AU048);
		//		uwIssuesList.add(UnderwriterIssues_PL.NotvalidlicenseonMVR_AU065);
		//        uwIssuesList.add(UnderwriterIssues_PL.MVRnotfound_AU066);
		//        uwIssuesList.add(UnderwriterIssues_PL.WaivedMVRIncident_AU69);
		//        uwIssuesList.add(UnderwriterIssues_PL.UnableToRetrieveInsuranceScore_SQ081);
		uwIssuesList.add(UnderwriterIssues_PL.HighSRPDriverlessVehicle_AU087);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;


		try {

			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskMVREnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup


			myPolicyObject = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PersonalAutoLinePL)
					.isDraft()
					.build(GeneratePolicyType.FullApp);

			new Login(driver).loginAndSearchSubmission(myPolicyObject);

			for(UnderwriterIssues_PL issue : uwIssuesList) {
				new GuidewireHelpers(driver).editPolicyTransaction();
				setupPolicy(issue);
				new GenericWorkorder(driver).clickGenericWorkorderQuote();
				new SideMenuPC(driver).clickSideMenuRiskAnalysis();
				FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis_UWIssues(driver).getUnderwriterIssues();
				softassert.assertTrue(uwIssues.isInList(getUWIssueString(issue)).equals(issue.getIssuetype()), issue.name() + "DID NOT INFER AS EXTECTED");
			}

			softassert.assertAll();
		} catch (Exception e) {
			throw e;
		} finally {
			//switch Verisk Score enabled back to false
			try {
				new GuidewireHelpers(driver).logout();
			} catch(Exception e) {
				//Already logged out.
			}
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskMVREnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
		}
	}

	private String getUWIssueString(UnderwriterIssues_PL issue) {
		switch(issue) {
		case MVRforeachdriver_AU048:
			for(Contact driver : myPolicyObject.squire.squirePA.getDriversList()) {
				if(driver.getAssignedUWIssue() != null || driver.getAssignedUWIssue().equals(issue)) {
					return issue.getLongDescription().replaceAll("${driver no. here}", driver.getFullName());
				}
			}
			break;
		case NotvalidlicenseonMVR_AU065:
		case MVRnotfound_AU066:
			for(Contact driver : myPolicyObject.squire.squirePA.getDriversList()) {
				if(driver.getAssignedUWIssue() != null || driver.getAssignedUWIssue().equals(issue)) {
					return issue.getLongDescription().replaceAll("${Driver name here}", driver.getFullName());
				}
			}
			break;
		case HighSRPDriverlessVehicle_AU087:
			break;
		case PassengerVehicleOlderThanTwentyYearsWithCompAndCollision_AU018:
			break;
		case UnableToRetrieveInsuranceScore_SQ081:
			break;
		case WaivedMVRIncident_AU69:
			break;
		}
		return null;
	}



	private void setupPolicy(UnderwriterIssues_PL uwIssue) throws Exception {
		switch(uwIssue) {
		case MVRforeachdriver_AU048:
			Contact driverNoMVR = new Contact();
			driverNoMVR.setOrderMVR(false);
			driverNoMVR.setRelationshipAB(RelationshipsAB.Friend);
			driverNoMVR.setAssignedUWIssue(uwIssue);
			myPolicyObject.squire.squirePA.addToDriversList(driverNoMVR);
			myPolicyObject.squire.policyMembers.add(driverNoMVR);

			new GenericWorkorderPolicyMembers(driver).addPolicyMember(driverNoMVR, myPolicyObject.pniContact);
			new GenericWorkorderSquireAutoDrivers(driver).addNewDrivers(myPolicyObject);
			break;
		case MVRnotfound_AU066:
			myPolicyObject.squire.squirePA.getDriversList().get(0).setAssignedUWIssue(uwIssue);
			//			License Status on MVR report is not valid, create Blocks Bind.
			break;
		case NotvalidlicenseonMVR_AU065:
			Contact invalidLicence = new Contact(Verisk_MVRHelpers.getVeriskTestCase("AB112123B"));
			new GenericWorkorderPolicyMembers(driver).addPolicyMember(invalidLicence, myPolicyObject.pniContact);
			new GenericWorkorderSquireAutoDrivers(driver).addNewDrivers(myPolicyObject);
			break;
		case HighSRPDriverlessVehicle_AU087:
			Contact highSRP = new Contact(Verisk_MVRHelpers.getVeriskTestCase("AB112101B"));
			for(VeriskMvr mvr : highSRP.getVeriskMVRReport()) {
				switch(mvr.getResults()) {
				case "Violation 05072015 Conviction 06072015 Driving Without Privileges":
					mvr.setAssignedSRPIncident(SRPIncident.DrivingWithoutLicense.getValue());
					break;
				case "Violation 06062014 Conviction 07062014 Limitations on Backing":
					mvr.setAssignedSRPIncident(SRPIncident.Other.getValue());
					break;
				case "Violation 12142012 Conviction 12282012 DUI":
				case "Violation 12252013 Conviction 12302013 DUI":
					mvr.setAssignedSRPIncident(SRPIncident.DUI.getValue());
					break;
				default:
					break;
				}
			}
			myPolicyObject.squire.squirePA.addToDriversList(highSRP);
			new GenericWorkorderPolicyMembers(driver).addPolicyMember(highSRP, myPolicyObject.pniContact);
			new GenericWorkorderSquireAutoDrivers(driver).addNewDrivers(myPolicyObject);
			break;
		case PassengerVehicleOlderThanTwentyYearsWithCompAndCollision_AU018:
			break;
		case UnableToRetrieveInsuranceScore_SQ081:
			break;
		case WaivedMVRIncident_AU69:
			break;
		}
	}










}
