package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.AddressType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquireEligibility;
import repository.gw.enums.TransactionType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.Squire;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePropertyAndLiability;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderAdditionalNamedInsured;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;

/**
* @Author nvadlamudi
* @Requirement :DE7914: Auto-Issued Submission activity not triggering on SQ016, SQ042, & SQ051
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/Product%20Model%20Spreadsheets/Squire%20Product%20Model%20Spreadhseets/2018%20NEW%20PL%20PRODUCT%20MODEL/Personal%20Lines%20Product%20Model.xlsx">Personal Lines Product Model</a>
* @Description : Validating The Auto-Issued Submission Activity should trigger if either of these UW Issues (SQ016 and SQ042)
* @DATE Sep 27, 2018
*/
public class DE7914AutoIssuedSubmissionActivityNotShowing extends BaseTest {
	private String activityName = "Auto-Issued Submission for UW Review";

    @Test
	public void testCheckSQ016UWIssuesInSubmission() throws Exception {
    	Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);

		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "ANI" + StringsUtils.generateRandomNumberDigits(8),
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolLevel", "Block").withANIList(listOfANIs)
				.withSocialSecurityNumber("4" + StringsUtils.generateRandomNumberDigits(8)).isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolObjPL.agentInfo.getAgentUserName(),
				myPolObjPL.agentInfo.getAgentPassword(), myPolObjPL.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		// SQ016 - Prior losses
		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		risk.clickPriorLossesCardTab();
		risk.setLossHistoryType("Manually Entered");
		risk.setManualLossHistory(1, "Section III - Auto Line",
				DateUtils.dateFormatAsString("MM/dd/yyyy",
						DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter)),
				"Testing", "2000", "2000", "2000", "Open");

		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.clickUWIssuesTab();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
			risk.performRiskAnalysisAndQuote(myPolObjPL);
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
		boolean checkUWInfo =  uwIssues.getInformationalList().size() > 0;
		
		//Adding the below loop for checking the ClaimCenter Down - UW Issues -- Test failed in REGR environments because of this
		for(UnderwriterIssue currentUW :uwIssues.getInformationalList()){
			if(currentUW.getShortDescription().equalsIgnoreCase(PLUWIssues.ClaimCenterDown.getShortDesc())){
				checkUWInfo =  uwIssues.getInformationalList().size() > 1;
			}
		}
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolObjPL);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(), "Auto Issuance - Agent can't have Submit Options to submit a policy..");
		new GuidewireHelpers(driver).clickProductLogo();
		Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(), "Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
		sideMenu.clickSideMenuForms();
		payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance), "Agent Auto Issue  - completed Issuance job not found.");
		
		if(checkUWInfo){
			Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed for UW issue: SQ042.");
		}
	
    }
    
    @Test
   	public void testCheckSQ042UWIssuesInSubmission() throws Exception {      
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		locOnePropertyList.add(new PLPolicyLocationProperty(PropertyTypePL.ResidencePremises));
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumAcres(11);
		locationsList.add(locToAdd);
		SquirePropertyAndLiability myPropertyAndLiability = new SquirePropertyAndLiability();
		myPropertyAndLiability.locationList = locationsList;
		Squire mySquire = new Squire(SquireEligibility.City);
		mySquire.squirePA = new SquirePersonalAuto();
		mySquire.propertyAndLiability = myPropertyAndLiability;

		GeneratePolicy myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolLevel", "Block")
				.withSocialSecurityNumber("4" + StringsUtils.generateRandomNumberDigits(8)).isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ042 - Designated Out-of-State
		policyInfo.clickPolicyInfoPrimaryNamedInsured();
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.setAddressListing("New...");
		GenericWorkorderAdditionalInsured additionalInsured = new GenericWorkorderAdditionalInsured(driver);
		additionalInsured.setAddressLine1("3696 S 6800 W");
		additionalInsured.setContactEditAddressCity("West Valley City");

		// additionalInsured.setState(State.Utah);
		additionalInsured.setZipCode("84128");
		additionalInsured.selectAddressType(AddressType.Home);
		GenericWorkorderAdditionalNamedInsured namedInsured = new GenericWorkorderAdditionalNamedInsured(driver);
		namedInsured.clickUpdate();

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.clickUWIssuesTab();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
			risk.performRiskAnalysisAndQuote(myPolicyObjPL);
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
		boolean checkUWInfo =  uwIssues.getInformationalList().size() > 0;
		
		//Adding the below loop for checking the ClaimCenter Down - UW Issues -- Test failed in REGR environments because of this
		for(UnderwriterIssue currentUW :uwIssues.getInformationalList()){
			if(currentUW.getShortDescription().equalsIgnoreCase(PLUWIssues.ClaimCenterDown.getShortDesc())){
				checkUWInfo =  uwIssues.getInformationalList().size() > 1;
			}
		}
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(myPolicyObjPL);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(), "Auto Issuance - Agent can't have Submit Options to submit a policy..");
		new GuidewireHelpers(driver).clickProductLogo();
		Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(), "Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
		sideMenu.clickSideMenuForms();
		payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		myPolicyObjPL.squire.setPolicyNumber(completePage.getPolicyNumber());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance), "Agent Auto Issue  - completed Issuance job not found.");
		
		if(checkUWInfo){
			Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed for UW issue: SQ042.");
		}
	
	}

}
