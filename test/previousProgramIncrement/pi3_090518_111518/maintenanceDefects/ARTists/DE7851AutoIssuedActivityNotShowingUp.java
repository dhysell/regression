package previousProgramIncrement.pi3_090518_111518.maintenanceDefects.ARTists;

import java.util.ArrayList;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.InceptionDateSections;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
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
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;

/**
* @Author nvadlamudi
* @Requirement :DE7851, DE7915 - Auto issue activity and Issue policy for sibling policy. DE7914 - Auto-Issued Submission activity not triggering on SQ016, SQ042, & SQ051
* @RequirementsLink <a href="https://rally1.rallydev.com/#/203558458764/detail/defect/249212201052">Link Text</a>
* @Description : Validating SQ004 and other Policy levle UW Issues with auto issue activity
* 				: validating SQ051 - Sibling ANI - auto issuance submission activity 
* @DATE Sep 14, 2018
*/
public class DE7851AutoIssuedActivityNotShowingUp extends BaseTest {
	private GeneratePolicy myPolicyObjPL;	
	private String activityName = "Auto-Issued Submission for UW Review";

    @Test
	public void testCheckPolicyLevelUWIssuesInSubmission() throws Exception {
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

		myPolicyObjPL = new GeneratePolicy.Builder(driver).withSquire(mySquire).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PropertyAndLiabilityLinePL, LineSelection.PersonalAutoLinePL)
				.withInsFirstLastName("PolLevel", "Block").withANIList(listOfANIs)
				.withSocialSecurityNumber("4" + StringsUtils.generateRandomNumberDigits(8)).isDraft()
				.build(GeneratePolicyType.FullApp);
		new Login(driver).loginAndSearchSubmission(myPolicyObjPL.agentInfo.getAgentUserName(),
				myPolicyObjPL.agentInfo.getAgentPassword(), myPolicyObjPL.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");

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
			Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed.");
		}
	
	}


    @Test(dependsOnMethods = {"testCheckPolicyLevelUWIssuesInSubmission"})
    public void testCheckPolicyLevelSiblingPolUWIssuesInSubmission() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		WebDriver driver = buildDriver(cf);
		ArrayList<PolicyInfoAdditionalNamedInsured> listOfANIs = new ArrayList<PolicyInfoAdditionalNamedInsured>();
		listOfANIs.add(new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "ANI" + StringsUtils.generateRandomNumberDigits(8),
				AdditionalNamedInsuredType.Spouse, new AddressInfo(true)) {
			{
				this.setNewContact(CreateNew.Create_New_Always);
			}
		});

		SquirePersonalAuto myAuto1 = new SquirePersonalAuto();
		Squire mySquire1 = new Squire(SquireEligibility.City);
		mySquire1.squirePA = myAuto1;

		GeneratePolicy mySiblingPolicy = new GeneratePolicy.Builder(driver).withSquire(mySquire1)
				.withProductType(ProductLineType.Squire).withANIList(listOfANIs).isDraft()
				.withLineSelection(LineSelection.PersonalAutoLinePL).build(GeneratePolicyType.FullApp);
		
		new Login(driver).loginAndSearchSubmission(mySiblingPolicy.agentInfo.getAgentUserName(),
				mySiblingPolicy.agentInfo.getAgentPassword(), mySiblingPolicy.accountNumber);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(
				driver);
		sideMenu.clickSideMenuPADrivers();
		GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);

		paDrivers.clickEditButtonInDriverTableByDriverName(mySiblingPolicy.pniContact.getFirstName());
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.setAutoDriversDOB(DateUtils.dateAddSubtract(
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Year, -21));
		new GuidewireHelpers(driver).clickProductLogo();
		paDrivers.clickOk();
		sideMenu.clickSideMenuPolicyInfo();

		GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.setPolicyInfoOrganizationType(OrganizationType.Sibling);
		polInfo.clickProductLogo();
		polInfo.setInceptionPolicyNumberBySelection(InceptionDateSections.Policy,
				myPolicyObjPL.squire.getPolicyNumber().replace("-", ""));

		new GuidewireHelpers(driver).clickNext();
		sideMenu.clickSideMenuHouseholdMembers();
		sideMenu.clickSideMenuRiskAnalysis();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
			risk.performRiskAnalysisAndQuote(mySiblingPolicy);
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
		payment.fillOutPaymentPage(mySiblingPolicy);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(), "Auto Issuance - Agent can't have Submit Options to submit a policy..");
		sideMenu.clickSideMenuPayment();
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
			Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed for UW issue: SQ051.");
		}
	}

}
