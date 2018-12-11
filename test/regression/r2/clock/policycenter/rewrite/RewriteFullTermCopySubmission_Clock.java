package regression.r2.clock.policycenter.rewrite;

import java.util.ArrayList;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import com.idfbins.enums.OkCancel;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.actions.ActionsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildingAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import persistence.globaldatarepo.entities.Underwriters;
@QuarantineClass
public class RewriteFullTermCopySubmission_Clock extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private String accountNumber;
	private String uwUserName;
	private String uwPassword;
    GenericWorkorderBuildings building;
    GenericWorkorderBuildingAdditionalCoverages buildAddtlCover;

	@Test
	public void generate() throws Exception {

		// customizing location and building
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding building = new PolicyLocationBuilding();
		locOneBuildingList.add(building);
		locationsList.add(new PolicyLocation(new AddressInfo(), locOneBuildingList));

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

        this.myPolicyObj = new GeneratePolicy.Builder(driver)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Company)
				//Was Company Name "Rewrite Full Term Copy Submission"
				.withInsCompanyName("Rewrite Full")
				.withPolOrgType(OrganizationType.Partnership)
				.withBusinessownersLine(new PolicyBusinessownersLine(SmallBusinessType.StoresRetail))
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Quarterly)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

		accountNumber = myPolicyObj.accountNumber;
		Underwriters underwriter = myPolicyObj.underwriterInfo;
		uwUserName = underwriter.getUnderwriterUserName();
		uwPassword = underwriter.getUnderwriterPassword();
	}

	/**
	 * @Author drichards
	 * @Requirement DE2709 and DE2833
	 * @Link <a href="http:// ">Link Text</a>
	 * @Description Tests to make sure Rewrite Full Term is available and Copy
	 *              Submission is not available before 180 days.
	 * @DATE Sep 24, 2015
	 * @throws Exception
	 */
	@Test(dependsOnMethods = { "generate" }, enabled = true)
	public void before180Days() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);
        StartCancellation cancelPol = new StartCancellation(driver);
		cancelPol.cancelPolicy(CancellationSourceReasonExplanation.Rewritten, "Just Because", null, true);

        GenericWorkorderComplete complete = new GenericWorkorderComplete(driver);
		complete.clickViewYourPolicy();

		ActionsPC actions = new ActionsPC(driver);
		// check presence of copy submission
		if(actions.copySubmission()) {
			Assert.fail("Copy Submission should not be available before 180 days after cancellation.");
		}

		try {
			actions.rewriteFullTerm();
            GenericWorkorder workorder = new GenericWorkorder(driver);
			workorder.clickWithdrawTransaction();
			new GuidewireHelpers(driver).selectOKOrCancelFromPopup(OkCancel.OK);
		} catch (Exception e) {
			Assert.fail("Rewrite Full Term should be available before 180 days after cancellation.");
		}

	}

	/**
	 * @throws Exception
	 * @Author drichards
	 * @Requirement DE2709 and DE2833
	 * @Link <a href="http:// ">Link Text</a>
	 * @Description Tests to make sure that Copy Submission is available and
	 *              Rewrite Full Term is not available after 180 days.
	 * @DATE Sep 24, 2015
	 */
	@Test(dependsOnMethods = { "before180Days" }, enabled = true)
	public void after180Days() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uwUserName, uwPassword, accountNumber);

		// move clock 180 days
		ClockUtils.setCurrentDates(cf, DateAddSubtractOptions.Day, 180);
        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsContacts();
		sideMenu.clickSideMenuToolsSummary();

		ActionsPC actions = new ActionsPC(driver);
		// check presence of copy submission
		boolean isFound = false;
		try {
			actions.rewriteFullTerm();
			isFound = true;
		} catch (Exception e) {
			actions.click_Actions();
		}
		if (isFound) {
			Assert.fail("Rewrite Full Term should not be available after 180 days after cancellation.");
		}

		if (!actions.copySubmission()) {
			Assert.fail("Copy Submission should be available after 180 days after cancellation.");
		}
	}

}
