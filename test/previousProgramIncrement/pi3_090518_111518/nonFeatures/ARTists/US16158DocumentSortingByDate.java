package previousProgramIncrement.pi3_090518_111518.nonFeatures.ARTists;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;

/**
* @Author nvadlamudi
* @Requirement :US16158: Change the default Document sorting to Date column instead of Date Printed
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764d/detail/userstory/251004988868">Link Text</a>
* @Description : Verify that documents are showing in date sorting order
* @DATE Sep 12, 2018
*/
@Test(groups = {"ClockMove"})
public class US16158DocumentSortingByDate extends BaseTest {
	  
	@Test(enabled = false)///low risk
	public void testCheckDocumentDateSortingOptions() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = new SquirePersonalAuto();

		GeneratePolicy myPolicyObj = new GeneratePolicy.Builder(driver).withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("Activity", "Assigned")
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		// Move clock 1 day and issuing a policy change
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		new Login(driver).loginAndSearchPolicyByAccountNumber(myPolicyObj);
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		policyChangePage.quoteAndIssue();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();

		// Move clock 1 day and issuing a policy change
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 1);
		policyChangePage.startPolicyChange("First policy Change", DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));
		sideMenu.clickSideMenuRiskAnalysis();
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		policyChangePage.quoteAndIssue();
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		sideMenu.clickSideMenuToolsDocuments();
		PolicyDocuments docs = new PolicyDocuments(driver);
		ArrayList<String> docDates = docs.getDocumentDate();
		Date initialDate = DateUtils.getDateValueOfFormat(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter),"MM/dd/yyyy");
		for (String docDate : docDates) {
			softAssert.assertTrue(initialDate.compareTo(DateUtils.convertStringtoDate(docDate, "MM/dd/yyyy")) > 0, "Document Date Sorting is showing up correct between " + initialDate + " and " + DateUtils.convertStringtoDate(docDate, "MM/dd/yyyy"));
			initialDate = DateUtils.convertStringtoDate(docDate, "MM/dd/yyyy");
		}
		softAssert.assertAll();
	}
}
