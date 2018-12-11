package previousProgramIncrement.pi2_062818_090518.nonFeatures.ARTists;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.BatchProcess;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.DateDifferenceOptions;
import repository.gw.enums.DocumentType;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.helpers.BatchHelpers;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.actions.ActionsPC;
import repository.pc.actions.NewDocumentPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * @Author nvadlamudi
 * @Requirement :US15766: Change to the Commodities Sheets Activity Pattern
 * @RequirementsLink <a href=
 *                   "http://projects.idfbins.com/policycenter/To%20Be%20Process/Admin%20Tab/Activity%20Patterns.xlsx">
 *                   Activity Patterns</a>
 * @Description : Validate Commodities Sheet Received created and escalated
 *              properly
 * @DATE Jul 30, 2018
 */
@Test(groups = { "ClockMove" })
public class TestCheckCommoditySheetActivity extends BaseTest {
	private String documentDirectoryPath = "\\\\FBMSQA11\\testing_data\\test-documents";

	@Test
	public void testCheckCommoditiesSheetActivity() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();
		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		SquireLiability myLiab = new SquireLiability();
		myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_300000_CSL);
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		myPropertyAndLiability.liabilitySection = myLiab;

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire).withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
				.withInsFirstLastName("Test", "SqForSib").withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);
		ActionsPC actions = new ActionsPC(driver);
		actions.click_Actions();
		actions.click_NewDocument();
		actions.click_LinkToExistingDoc();
		System.out.println("Uploading Doc Type: " + DocumentType.CommoditiesSheet);
		NewDocumentPC doc = new NewDocumentPC(driver);
		doc.setAttachment(documentDirectoryPath, "\\just_a_doc");
		// delay required to upload the documents
		doc.selectRelatedTo("Policy");
		doc.selectDocumentType(DocumentType.CommoditiesSheet);
		doc.sendArbitraryKeys(Keys.TAB);
		doc.clickUpdate();

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
		sideMenu.clickSideMenuToolsSummary();
		PolicySummary summaryPage = new PolicySummary(driver);

		if (!summaryPage.checkIfActivityExists("Commodities Sheet Received")) {
			Assert.fail("Expected Activity 'Commodities Sheet Received' is not displayed.");
		} else {
			summaryPage.clickActivity("Commodities Sheet Received");

			// No Prior Proof Of Insurance escalated activity
			ActivityPopup activityPopupPage = new ActivityPopup(driver);
			Date esclationDate = activityPopupPage.getActivityEscalationDate();

			int days = DateUtils.getDifferenceBetweenDates(
					DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), esclationDate,
					DateDifferenceOptions.Day);
			new GuidewireHelpers(driver).logout();

			ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, days + 2);
			new BatchHelpers(cf).runBatchProcess(BatchProcess.Activity_Escalation);
			// delay required

			Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
					Underwriter.UnderwriterTitle.Underwriter);

			new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(),
					uw.getUnderwriterPassword(),  myPolicyObjPL.accountNumber);
			String monthDate = DateUtils.dateFormatAsString("MM/dd",
					DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

			if (!summaryPage
					.checkIfActivityExists("Activity Escalated on " + monthDate + " - Commodities Sheet Received"))
				Assert.fail("Activity Escalated on "+ monthDate + "- Commodities Sheet Received Activity is not triggered");

		}
	}
}
