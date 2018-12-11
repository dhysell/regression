package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Cancellation.CancellationSourceReasonExplanation;
import repository.gw.enums.Forms_BOP;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.ClockUtils;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.policy.PolicyDocuments;
import repository.pc.workorders.forms.BOP_Forms;
import repository.pc.workorders.renewal.StartRenewal;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;
import java.util.List;

/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description VERIFY EPLI FORMS
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_FormInference extends BaseTest {


	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled = true)
	public void epli_Forms() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		List<Forms_BOP> formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsurance);
		formsList.add(Forms_BOP.BusinessownersPolicyDeclarations);
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityWarrantyStatement);

		GeneratePolicy formsPolicy = new BOP_Forms(driver).createFormsPolicyObject(formsList);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine(formsPolicy.busOwnLine)
				.build(GeneratePolicyType.PolicyIssued);

		if(myPolicyObj.busOwnLine.getCurrentPolicyType().equals(GeneratePolicyType.PolicyIssued)) {
			new Login(driver).loginAndSearchPolicy_asAgent(myPolicyObj);
		} else {
			new Login(driver).loginAndSearchSubmission(myPolicyObj);
		}
		new SideMenuPC(driver).clickSideMenuForms();
		List<Forms_BOP> expectedForms = new BOP_Forms(myPolicyObj, driver).getFormsList();
		for(Forms_BOP form : expectedForms) {
			switch(myPolicyObj.busOwnLine.getCurrentPolicyType()) {
			case FullApp:
			case PolicySubmitted:
			case QuickQuote:
				if(form.isSubmission()) {
					softAssert.assertTrue(myPolicyObj.busOwnLine.getSubmissionForms().contains(form.getName()), form.getName() + " DID NOT INFER AS EXPECTED ON SUBMISSION");
				}
				break;
			case PolicyIssued:
				if(form.isIssuance()) {
					softAssert.assertTrue(myPolicyObj.busOwnLine.getIssuanceForms().contains(form.getName()), form.getName() + " DID NOT INFER AS EXPECTED ON ISSUANCE");
				}
				break;
			}
		}
		softAssert.assertAll();
	}


	@Test
	public void epli_Forms_Cancelation() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		if(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter).before(DateUtils.convertStringtoDate("10/1/2018", "MM/dd/yyyy"))) {
			ClockUtils.setCurrentDates(driver, DateUtils.convertStringtoDate("10/1/2018", "MM/dd/yyyy"));
		}

		List<Forms_BOP> formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.OfferToPurchaseExtendedReportingPeriodLetter);

		GeneratePolicy formsPolicy = new BOP_Forms(driver).createFormsPolicyObject(formsList);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine(formsPolicy.busOwnLine)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		myPolicyObj.cancelPolicy(CancellationSourceReasonExplanation.Photos, "no policy for you", null, true);
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Cancellation");
		new PolicyDocuments(driver).clickSearch();
		List<String> generatedForms = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		boolean found = false;
		for(Forms_BOP bopForm : formsList) {
			for(String form : generatedForms) {
				if(form.equals(bopForm.getName())) {
					found = true;
					break;
				}
			}
			softAssert.assertTrue(found, bopForm.name() + " | " + bopForm.getNumber() + " DID NOT INFER ON CANCELATION");
			found = false;
		}
		softAssert.assertAll();
	}


	@Test
	public void epli_Forms_Renewal() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		if(DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter).before(DateUtils.convertStringtoDate("10/1/2018", "MM/dd/yyyy"))) {
			ClockUtils.setCurrentDates(driver, DateUtils.convertStringtoDate("10/1/2018", "MM/dd/yyyy"));
		}

		List<Forms_BOP> formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityWarrantyStatement);


		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine()
				.withPolTermLengthDays(79)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		new StartRenewal(driver).startRenewal();
		new GuidewireHelpers(driver).logout();
		new Login(driver).loginAndSearchPolicyByAccountNumber("hhill", "gw", myPolicyObj.accountNumber);
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Renewal");
		new PolicyDocuments(driver).clickSearch();
		List<String> generatedForms = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		boolean found = false;
		for(Forms_BOP bopForm : formsList) {
			for(String form : generatedForms) {
				if(form.equals(bopForm.getName())) {
					found = true;
					break;
				}
			}
			softAssert.assertFalse(found, bopForm.name() + " | " + bopForm.getNumber() + " INFERED ON RENEAL");
			found = false;
		}
		softAssert.assertAll();
	}











}
