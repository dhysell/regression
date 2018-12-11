package previousProgramIncrement.pi2_062818_090518.f298_EmploymentPracticeLiabilityCoverageBOP;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.Forms_BOP;
import repository.gw.enums.GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.infobar.InfoBar;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import repository.pc.policy.PolicyDocuments;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.forms.BOP_Forms;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import scratchpad.evan.SideMenuPC;

import java.util.ArrayList;
import java.util.List;
/**
* @Author jlarsen
* @Requirement 
* @RequirementsLink <a href="http:// ">Link Text</a>
* @Description ENSURE EPLI DOCUMENTS GENERATE CORRECTLY
* @DATE Sep 20, 2018
*/
@Test(groups = {"ClockMove"})
public class EPLI_DocumentGeneration extends BaseTest {
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
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityWarrantyStatement);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		
		
		GeneratePolicy formsPolicy = new BOP_Forms(driver).createFormsPolicyObject(formsList);

		myPolicyObj = new GeneratePolicy.Builder(driver)
				.withBusinessownersLine(formsPolicy.busOwnLine)
				.build(GeneratePolicyType.PolicyIssued);
		
		new Login(driver).loginAndSearchPolicy_asUW(myPolicyObj);
		
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		List<String> documentList = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		for(Forms_BOP form : formsList) {
			softAssert.assertTrue(documentList.contains(form.getName()), form.getName() + " DID NOT GENERATE IN POLICY DOCUMENTS");
		}
		String changeNumber = new StartPolicyChange(driver).startPolicyChange("101 Employees", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(101);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneHundredThousand);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new StartPolicyChange(driver).quoteAndIssue();
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Policy Change", changeNumber);
		new PolicyDocuments(driver).clickSearch();
		
		formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.BusinessownersPolicyDeclarations);
//		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		
		
		documentList = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		for(Forms_BOP form : formsList) {
			softAssert.assertTrue(documentList.contains(form.getName()), form.getName() + " DID NOT GENERATE IN POLICY DOCUMENTS ON 101 Employees CHANGE");
		}
		


		
		
		
		changeNumber = new StartPolicyChange(driver).startPolicyChange("Five Hundred thousand", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new StartPolicyChange(driver).quoteAndIssue();
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Policy Change", changeNumber);
		new PolicyDocuments(driver).clickSearch();
		
		formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.BusinessownersPolicyDeclarations);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		
		
		documentList = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		for(Forms_BOP form : formsList) {
			softAssert.assertTrue(documentList.contains(form.getName()), form.getName() + " DID NOT GENERATE IN POLICY DOCUMENTS ON Five Hundred thousand CHANGE");
		}
		
		
		
		
		changeNumber = new StartPolicyChange(driver).startPolicyChange("one Million Dollars", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.OneMillion);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new StartPolicyChange(driver).quoteAndIssue();
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Policy Change", changeNumber);
		new PolicyDocuments(driver).clickSearch();
		
		formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.BusinessownersPolicyDeclarations);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		
		
		documentList = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		for(Forms_BOP form : formsList) {
			softAssert.assertTrue(documentList.contains(form.getName()), form.getName() + " DID NOT GENERATE IN POLICY DOCUMENTS ON one Million Dollars CHANGE");
		}
		
		
		
		changeNumber = new StartPolicyChange(driver).startPolicyChange("Five Hundred thousand....again", null);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(1);
		myPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmploymentPracticesLiabilityInsurance_AggregateLimit(EmploymentPracticesLiabilityInsurance_AggregateLimit.FiveHundredThousand);
		new GenericWorkorderBusinessownersLineAdditionalCoverages(driver).fillOutOtherLiabilityCovergaes(myPolicyObj.busOwnLine);
		new StartPolicyChange(driver).quoteAndIssue();
		new InfoBar(driver).clickInfoBarPolicyNumber();
		new SideMenuPC(driver).clickSideMenuToolsDocuments();
		new PolicyDocuments(driver).selectRelatedTo("Policy Change", changeNumber);
		new PolicyDocuments(driver).clickSearch();
		
		formsList = new ArrayList<Forms_BOP>();
		formsList.add(Forms_BOP.EmploymentPracticesLiabilityInsuranceSupplementalApplication);
		formsList.add(Forms_BOP.BusinessownersPolicyDeclarations);
		formsList.add(Forms_BOP.CommercialEmploymentPracticesLiabilityInsuranceCoverageSupplementalDeclarations);
		
		
		documentList = new PolicyDocuments(driver).getDocumentsDescriptionsFromTable();
		for(Forms_BOP form : formsList) {
			softAssert.assertTrue(documentList.contains(form.getName()), form.getName() + " DID NOT GENERATE IN POLICY DOCUMENTS ON Five Hundred thousand....again CHANGE");
		}
		softAssert.assertAll();
	}
}
