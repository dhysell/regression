package previousProgramIncrement.pi1_041918_062718.maintenanceDefects.achievers;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.Contact;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.desktop.DesktopProofOfMailPC;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuPC;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.rewrite.StartRewrite;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE LIABILITY CARDS ARE PRINTED
* @DATE Sep 20, 2018
*/
public class DE6600_RewriteFullTermMissingLiabilityCards extends BaseTest {
	private WebDriver driver;
	GeneratePolicy myPolicyObject;


	@Test(enabled = true)
	public void rewriteFullTermMissingLiabilityCards() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		Contact excludedDriver = new Contact();
		excludedDriver.setAge(driver, 34);

		SquirePersonalAuto myPersonalAuto = new SquirePersonalAuto();
		myPersonalAuto.addToDriversList(excludedDriver);


		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = myPersonalAuto;

		myPolicyObject = new GeneratePolicy.Builder(driver)
				.withSquire(mySquire)
				.withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL)
				.withPolOrgType(OrganizationType.Individual)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
		myPolicyObject.cancelPolicy(CancellationSourceReasonExplanation.OtherPolicyRewrittenOrReplaced, "Correct Commisions", null, true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).login("hhill", "gw");
		new TopMenuPC(driver).clickDesktopTab();
		new DesktopSideMenuPC(driver).clickProofOfMail();
		new DesktopProofOfMailPC(driver).checkAccountDocuments(myPolicyObject.accountNumber);
		new DesktopProofOfMailPC(driver).clickDesktopProofOfMailDelete();
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObject);
		new StartRewrite(driver).rewriteFullTerm(myPolicyObject);
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new PolicySummary(driver).clickCompletedTransactionByType(TransactionType.Rewrite_Full_Term);
		new SideMenuPC(driver).clickSideMenuForms();
		GenericWorkorderForms formspage = new GenericWorkorderForms(driver);
		List<String> rewriteForms = formspage.getFormDescriptionsFromTable();
		Assert.assertTrue(rewriteForms.contains("Certificate of Liability Insurance"), "Certificate of Liability Insurance: WAS NOT INFERED ON REWITE FULL TERM");
		
	}
}
