package previousProgramIncrement.pi3_090518_111518.f298_EPLI_BOP;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.desktop.DesktopProofOfMailPC;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.policy.PolicySummary;
import repository.pc.workorders.cancellation.StartCancellation;
import repository.pc.workorders.generic.GenericWorkorder;
import scratchpad.evan.SideMenuPC;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE THAT THE OFFER TO PURCHACE EXTENDED REPORTING PERIOD LETER DOES NOT SHOW IN POM IF CANCELATION IF RECINDED.
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})//IS IN CLOCK MOVE CUS I NEED DOCUMENT TO PROCESS MORE QUICKLY
public class US16079_EPLI_LetterCancelation extends BaseTest {
	public SoftAssert softAssert = new SoftAssert();
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;


	@Test
	public void extendedCoverageLetterDeletion() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine()
				.withPolEffectiveDate(DateUtils.dateAddSubtract(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -15))
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new StartCancellation(driver).cancelPolicy(CancellationSourceReasonExplanation.Photos, "HAHHAAH YOUR GONA GET CANCELED", null, false);
		new StartCancellation(driver).clickViewPolicyLink();
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Cancellation");
		new PolicyDocuments(driver).clickSearch();
		Assert.assertTrue(new PolicyDocuments(driver).getDocumentsDescriptionsFromTable().contains("Offer To Purchase Extended Reporting Period Letter"), "Offer To Purchase Extended Reporting Period Letter DID NOT INFER ON POLICY CANCELATION");
		new SideMenuPC(driver).clickSideMenuToolsSummary();
		new PolicySummary(driver).clickPendingTransaction(TransactionType.Cancellation);
		new StartCancellation(driver).clickCloseOptionsRescindCancellation();
		new GuidewireHelpers(driver).logout();
		new Login(driver).login("hhill", "gw");
		new DesktopSideMenuPC(driver).clickProofOfMail();
		new DesktopProofOfMailPC(driver).clickCommercialLinesProofOfMailTab();
		List<WebElement> pomDocuemnts = new GenericWorkorder(driver).finds(By.xpath("//a[contains(text(), '" + myPolicyObj.accountNumber + "')]/ancestor::tr/child::td/div[contains(text(), 'Offer To Purchase Extended Reporting Period Letter')]"));
		Assert.assertTrue((pomDocuemnts.isEmpty()), "Offer To Purchase Extended Reporting Period Letter, WAS FOUND ON POM AFTER THE CANCELATION WAS RECINDED. SHOULD NOT BE THERE!!!!! BAD GUGU");
		

	}

















}
