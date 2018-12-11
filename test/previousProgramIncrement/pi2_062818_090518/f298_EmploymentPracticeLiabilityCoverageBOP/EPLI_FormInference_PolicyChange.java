package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Forms_BOP;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.helpers.NumberUtils;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.forms.BOP_Forms;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderForms;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;
import java.util.List;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description 
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_FormInference_PolicyChange extends BaseTest {
	public GeneratePolicy myPolicyObj = null;
	private WebDriver driver;

	@Test(enabled = true)
	public void epli_Forms() throws Exception {
		SoftAssert softAssert = new SoftAssert();
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		
		List<Forms_BOP> formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsurance);

		GeneratePolicy formsPolicy = new BOP_Forms(driver).createFormsPolicyObject(formsList);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine(formsPolicy.busOwnLine)
				.build(GeneratePolicyType.PolicyIssued);

		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);

		new StartPolicyChange(driver).startPolicyChange("EPLI STUFFS", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneHundredThousand);
		int numberOfFulltimeEmployees = NumberUtils.generateRandomNumberInt(0, 110);
		int numberOfPartTimeEmployees = (110 - numberOfFulltimeEmployees)*2;
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(numberOfFulltimeEmployees);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(numberOfPartTimeEmployees);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();

		new SideMenuPC(driver).clickSideMenuForms();
		myPolicyObj.busOwnLine.setChangeForms(new GenericWorkorderForms(driver).getFormDescriptionsFromTable());

		List<Forms_BOP> expectedForms = new ArrayList<Forms_BOP>();
		expectedForms.add(Forms_BOP.BusinessownersPolicyDeclarations);
		expectedForms.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		for(Forms_BOP form : expectedForms) {
			if(form.isPolicyChange()) {
				softAssert.assertTrue(myPolicyObj.busOwnLine.getChangeForms().contains(form.getName()), form.getName() + " DID NOT INFER AS EXPECTED");
			} else {
				softAssert.assertFalse(myPolicyObj.busOwnLine.getChangeForms().contains(form.getName()), form.getName() + " INFERED WHEN IT SHOULD NOT HAVE");
			}
		}

		new InfoBar(driver).clickInfoBarPolicyNumber();




		new StartPolicyChange(driver).startPolicyChange("EPLI STUFFS", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();

		new SideMenuPC(driver).clickSideMenuForms();
		myPolicyObj.busOwnLine.setChangeForms(new GenericWorkorderForms(driver).getFormDescriptionsFromTable());

		expectedForms = new ArrayList<Forms_BOP>();
		expectedForms.add(Forms_BOP.BusinessownersPolicyDeclarations);
		expectedForms.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		for(Forms_BOP form : expectedForms) {
			if(form.isPolicyChange()) {
				softAssert.assertTrue(myPolicyObj.busOwnLine.getChangeForms().contains(form.getName()), form.getName() + " DID NOT INFER AS EXPECTED");
			} else {
				softAssert.assertFalse(myPolicyObj.busOwnLine.getChangeForms().contains(form.getName()), form.getName() + " INFERED WHEN IT SHOULD NOT HAVE");
			}
		}

		new InfoBar(driver).clickInfoBarPolicyNumber();
		
		new StartPolicyChange(driver).startPolicyChange("EPLI STUFFS", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneMillion);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new GenericWorkorder(driver).clickGenericWorkorderQuote();

		new SideMenuPC(driver).clickSideMenuForms();
		myPolicyObj.busOwnLine.setChangeForms(new GenericWorkorderForms(driver).getFormDescriptionsFromTable());

		expectedForms = new ArrayList<Forms_BOP>();
		expectedForms.add(Forms_BOP.BusinessownersPolicyDeclarations);
		expectedForms.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		for(Forms_BOP form : expectedForms) {
			if(form.isPolicyChange()) {
				softAssert.assertTrue(myPolicyObj.busOwnLine.getChangeForms().contains(form.getName()), form.getName() + " DID NOT INFER AS EXPECTED");
			} else {
				softAssert.assertFalse(myPolicyObj.busOwnLine.getChangeForms().contains(form.getName()), form.getName() + " INFERED WHEN IT SHOULD NOT HAVE");
			}
		}





		softAssert.assertAll();
	}















}





























