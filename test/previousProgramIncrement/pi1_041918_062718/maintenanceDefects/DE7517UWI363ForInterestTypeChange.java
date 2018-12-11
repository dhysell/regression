package previousProgramIncrement.pi1_041918_062718.maintenanceDefects;

import com.idfbins.driver.BaseTest;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestSubType;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.DocFormEvents;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.ActualExpectedDocumentDifferences;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.DocFormUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderForms;
import repository.pc.workorders.generic.GenericWorkorderVehicles_Details;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

import java.util.ArrayList;
import java.util.Date;

/**
 * 
* @Author nvadlamudi
* @Requirement :DE7517: Lessor Endorsement # UWI363 (1014) not inferring when the interest type is edited
* Steps: 	Have a lienholder.
*			Create a policy change and change the interest type to Lessor. (Endorsement did not infer)
* @Description:  Validating Forms and Document page for UWI363
* @DATE May 21, 2018
 */
public class DE7517UWI363ForInterestTypeChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myPolicyObj;
	private ArrayList<DocFormEvents.PolicyCenter> eventsHitDuringSubmissionCreationPlusLocal = new ArrayList<DocFormEvents.PolicyCenter>();
	private ArrayList<String> formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = new ArrayList<String>();
	private ArrayList<String> formsOrDocsActualFromUISubmissionPlusLocal = new ArrayList<String>();
	private ActualExpectedDocumentDifferences actualExpectedDocDifferencesSubmissionPlusLocal = new ActualExpectedDocumentDifferences();
	private Underwriters uw;
	private String LessorEndoDocument = "Insured Vehicle Leasing Endorsement";


	@Test
	public void testPolicyChangeWithAIInterestType() throws Exception {	
		testIssueSquireAuto();
		
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				myPolicyObj.accountNumber);
		Date currentSystemDate = DateUtils.getCenterDate(driver, ApplicationOrCenter.PolicyCenter);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", currentSystemDate);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPAVehicles();
        GenericWorkorderVehicles_Details vehicalTab = new GenericWorkorderVehicles_Details(driver);
		vehicalTab.selectDriverTableSpecificRowByText(1);
        vehicalTab.clickVehicleAdditionalInterestByName(
				myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getCompanyName());
        GenericWorkorderAdditionalInterests aInterest = new GenericWorkorderAdditionalInterests(driver);
		aInterest.selectBuildingsPropertyAdditionalInterestsInterestType(AdditionalInterestType.LessorPL.getValue());
		eventsHitDuringSubmissionCreationPlusLocal.add(DocFormEvents.PolicyCenter.Squire_Auto_InsuredVehicleLeasingEndorsement);

		aInterest.clickUpdate();
        vehicalTab.clickOK();
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorder genericWO = new GenericWorkorder(driver);
	    genericWO.clickGenericWorkorderQuote();
	    sideMenu.clickSideMenuForms();
        //Validating in Forms page
        GenericWorkorderForms formsPage = new GenericWorkorderForms(driver);
		formsOrDocsActualFromUISubmissionPlusLocal = formsPage.getFormDescriptionsFromTable();
		formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal = DocFormUtils
				.getListOfDocumentNamesForListOfEventNames(eventsHitDuringSubmissionCreationPlusLocal);
		actualExpectedDocDifferencesSubmissionPlusLocal = DocFormUtils.compareListsForDifferences(
				formsOrDocsActualFromUISubmissionPlusLocal,
				formsOrDocsExpectedBasedOnSubmissionEventsHitPlusLocal);	
		Assert.assertTrue(actualExpectedDocDifferencesSubmissionPlusLocal.getInExpectedNotInUserInterface().size() == 0, "Policy Number: '"+ myPolicyObj.squire.getPolicyNumber() +"' - Forms '"+ LessorEndoDocument + "' is not displayed for Additional Interest Address change.");
		
		//Issue policy change
		sideMenu.clickSideMenuQuote();
		policyChangePage.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		
		//validating document page
		sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Change");
		docs.setDocumentDescription(LessorEndoDocument);
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();
		boolean docFound = false;
		for (String desc : descriptions) {
			if (desc.equals(LessorEndoDocument)) {				
				docFound = true;
				break;
			}
		}	
		
		String insuredCopy = myPolicyObj.pniContact.getFirstName() + " "+ myPolicyObj.pniContact.getLastName();
		String aICopy = myPolicyObj.squire.squirePA.getVehicleList().get(0).getAdditionalInterest().get(0).getCompanyName();
		ArrayList<String> aiNames = docs.getDocumentNameAddress();
		boolean insuredFound = false;
		boolean aIFound = false;
		for (String aiName : aiNames) {
			if (aiName.contains(insuredCopy)){				
				insuredFound = true;
			}		
			
			if (aiName.contains(aICopy)){				
				aIFound = true;
		}		
		}		
		Assert.assertTrue(docFound, "Policy Number: '"+ myPolicyObj.squire.getPolicyNumber() +"' - Document '"+ LessorEndoDocument + "' is not displayed for Additional Interest Address change.");
		Assert.assertTrue(insuredFound, "Policy Number: '"+ myPolicyObj.squire.getPolicyNumber() +"' - Document page for '" +LessorEndoDocument +"' not showing Insured Name :" +insuredCopy+ ".");
		Assert.assertTrue(aIFound, "Policy Number: '"+ myPolicyObj.squire.getPolicyNumber() +"' - Document page for '" +LessorEndoDocument +"' not showing Additional Interest Name :" +aICopy+ ".");
		
	}
	
	
	public void testIssueSquireAuto() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyLow,
				MedicalLimit.TenK);
		coverages.setUnderinsured(false);		

		// Vehicle
		ArrayList<Vehicle> vehicleList = new ArrayList<Vehicle>();		
		AdditionalInterest autoAI = new AdditionalInterest(ContactSubType.Company);
		autoAI.setAdditionalInterestSubType(AdditionalInterestSubType.PLSectionIIIAuto);
		autoAI.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		ArrayList<AdditionalInterest> autoAIList = new ArrayList<AdditionalInterest>();
		autoAIList.add(autoAI);
		Vehicle toAdd = new Vehicle();
		toAdd.setAdditionalInterest(autoAIList);
		vehicleList.add(toAdd);

		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);
		squirePersonalAuto.setVehicleList(vehicleList);

		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = squirePersonalAuto;

        myPolicyObj = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL).withInsFirstLastName("DE7479", "LienAdd")
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);

	}

}
