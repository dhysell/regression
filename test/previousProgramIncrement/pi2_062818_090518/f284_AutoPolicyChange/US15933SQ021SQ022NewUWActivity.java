package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
* @Author nvadlamudi
* @Requirement :US15933:Attach SQ021 & SQ022 to new UW activity and update message wording
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/Squire%20Product%20Model%20Spreadhseets/2018%20NEW%20PL%20PRODUCT%20MODEL/Personal%20Lines%20Product%20Model.xlsx">Personal Lines Product Model</a>
* @Description : Validating SQ021 & SQ022 to new UW activity
* @DATE Aug 13, 2018
*/
public class US15933SQ021SQ022NewUWActivity extends BaseTest {
	SoftAssert softAssert = new SoftAssert();
	WebDriver driver;

	@Test
	public void testCheckSQ021SQ022UWIssuesInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property1 = new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises);
		property1.setSetMSPhotoYears(false);
		property1.setSetMSPhotoYears(false);
		locOnePropertyList.add(property1);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		// PGL009 City acre over 10
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		Squire mySquire = new Squire(SquireEligibility.random());

		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("MSPhoto", "MSYear").isDraft()
				.build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
	
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = risk.getUnderwriterIssues();

		@SuppressWarnings("serial")
		List<PLUWIssues> newUWIssues = new ArrayList<PLUWIssues>() {
			{
				this.add(PLUWIssues.MissingMSYear);
				this.add(PLUWIssues.MissingPhotoYear);
			}
		};
		
		
		for (PLUWIssues uwBlockBindExpected : newUWIssues) {
			softAssert.assertFalse(
					!uwIssues.isInList(uwBlockBindExpected.getLongDesc().replace("<<property number>>", String.valueOf(myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getPropertyList().size())).replace("<<location number>>", String.valueOf(myPolicyObjPL.squire.propertyAndLiability.locationList.get(0).getNumber()))).equals(UnderwriterIssueType.Informational),
					"Expected UW Informational : " + uwBlockBindExpected.getShortDesc() + " is not displayed");
		}

		softAssert.assertAll();	
		
	}	
	
}
