package previousProgramIncrement.pi3_090518_111518.f350_InsuranceScore_Verisk_TransUnion;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.administration.AdminScriptParameters;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.ScriptParameter;
import repository.gw.enums.SquireEligibility;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.Squire;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.topmenu.TopMenuAdministrationPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderInsuranceScore;
import persistence.enums.Underwriter.UnderwriterLine;
import persistence.enums.Underwriter.UnderwriterTitle;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement :US16499: Make the new Verisk screen
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/userstory/254803293932">Link Text</a>
 * @Description : validating the Verisk screen/tab is displayed under Insurance score page
 * @DATE Sep 25, 2018
 */
@Test(groups= {"ClockMove"})
public class US16499VeriskInsuranceScoreScreen extends BaseTest {
	private GeneratePolicy myPolicyObjPL;

	@Test
	public void testPolicyChangeUWIssuesInformational() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Insu" + StringsUtils.generateRandomNumberDigits(8),
				"ANI" + StringsUtils.generateRandomNumberDigits(8), AdditionalNamedInsuredType.Spouse,
				new AddressInfo(true)) {{
				this.setNewContact(CreateNew.Create_New_Always);
			}});

		Squire mySquire = new Squire(SquireEligibility.City);

		try {

			//Need to set Verisk Credit Score Enabled Script Parameter to true
			new Login(driver).login("su", "gw");
			new TopMenuAdministrationPC(driver).clickScriptParameters();
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, true);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup


			myPolicyObjPL = new GeneratePolicy.Builder(driver)
					.withSquire(mySquire)
					.withANIList(listOfANIs)
					.withProductType(ProductLineType.Squire)
					.withLineSelection(LineSelection.PropertyAndLiabilityLinePL)
					.withInsFirstLastName("Verisk", "Screen")
					.build(GeneratePolicyType.QuickQuote);

			new Login(driver).loginAndSearchSubmission(myPolicyObjPL);
			new GuidewireHelpers(driver).editPolicyTransaction();
			SideMenuPC sideMenu = new SideMenuPC(driver);
			sideMenu.clickSideMenuPLInsuranceScore();
			GenericWorkorderInsuranceScore pcInsuranceScorePage = new GenericWorkorderInsuranceScore(driver);
			pcInsuranceScorePage.fillOutCreditReport(myPolicyObjPL);
			new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();

			//Ensure that we can toggle which screen (Verisk vs LexisNexis) is visible to users.
//			pcInsuranceScorePage.clickCurrentInsuranceScoreGeneral();
//			pcInsuranceScorePage.clickCurrentReportDetails();
//			pcInsuranceScorePage.clickInsuranceScoreVerisk();
//			pcInsuranceScorePage.clickGenericWorkorderSaveDraft();		
			new GuidewireHelpers(driver).logout(); 

			//Ensure that the re-order button follows current funtionality. (it's available)
			Underwriters uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(UnderwriterLine.Personal,
					UnderwriterTitle.Underwriter_Supervisor);

			new Login(driver).loginAndSearchSubmission(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), myPolicyObjPL.accountNumber);
			sideMenu.clickSideMenuPLInsuranceScore();
			Assert.assertTrue(pcInsuranceScorePage.checkOrderInsuranceReportButton(), "Reorder Insurance Score button does not exists for logged in UW user");

			//Ensure that the dropdown for choosing who the score is being ordered for is still available and mimics the LexisNexis functionality
//			pcInsuranceScorePage.clickEditInsuranceReport();
			pcInsuranceScorePage.selectCreditReportIndividual(myPolicyObjPL.aniList.get(0).getPersonFirstName());
			pcInsuranceScorePage.clickOrderReport();
			sideMenu.clickSideMenuHouseholdMembers();
			sideMenu.clickSideMenuPLInsuranceScore();
			pcInsuranceScorePage.clickOrderReport();
			pcInsuranceScorePage.clickGenericWorkorderSaveDraft();		 
			pcInsuranceScorePage.clickInsuranceScoreVerisk();		 
			Assert.assertTrue(pcInsuranceScorePage.getInsuranceScoreVeriskDetailsTableRowCount() > 0, "Verisk Insurance Score details are not displayed.");
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
			new AdminScriptParameters(driver).editScriptParameter(ScriptParameter.VeriskCreditScoreEnabled, false);
			new GuidewireHelpers(driver).logout();
			//End Script Parameter Setup
		}

	}
}
