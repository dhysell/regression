package previousProgramIncrement.pi2_062818_090518.f284_AutoPolicyChange;

import java.util.ArrayList;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.OrganizationTypePL;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_PriorLosses;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
* @Author nvadlamudi
* @Requirement :US15616: STRETCH:  Enable auto-issuance of agent-submitted policy changes - Standard Lines
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Personal%20Lines%20-%20Common/PC8%20-%20Personal%20Lines%20-%20Auto%20Issuance.xlsx">PC8 - Personal Lines - Auto Issuance</a>
* @Description : Test to validate agent can issue a policy change in Standard Lines 
* @DATE Aug 15, 2018
*/
public class US15616StandardAgentAutoSubmitPolicyChanges extends BaseTest {
	private String activityName = "Auto-Issued Policy Change for UW Review";
	private WebDriver driver;

	@Test
	public void testStdIMAgentAutoSubmitPolicyChange() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
		imCoverages.add(InlandMarine.FarmEquipment);

		// Farm Equipment
		IMFarmEquipmentScheduledItem scheduledItem = new IMFarmEquipmentScheduledItem("Circle Sprinkler",
				"Manly Farm Equipment", 1000);

		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem);

		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm,
				IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);

		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);

		StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
		myStandardInlandMarine.farmEquipment = allFarmEquip;
		myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = imCoverages;

		GeneratePolicy stdIMPolObj = new GeneratePolicy.Builder(driver).withProductType(ProductLineType.StandardIM)
				.withLineSelection(LineSelection.StandardInlandMarine).withStandardInlandMarine(myStandardInlandMarine)
				.withInsFirstLastName("Change", "StndIM").withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);
		issuePolicyChangeAndCheckUWActivity(stdIMPolObj, driver);
	}

	@Test
	public void testStdFireAgentAutoSubmitPolicyChange() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PLPolicyLocationProperty> locOnePropertyList = new ArrayList<PLPolicyLocationProperty>();

		PLPolicyLocationProperty property = new PLPolicyLocationProperty();
		property.setpropertyType(PropertyTypePL.DwellingPremises);

		locOnePropertyList.add(property);
		PolicyLocation locToAdd = new PolicyLocation(locOnePropertyList);
		locToAdd.setPlNumResidence(12);
		locToAdd.setPlNumAcres(12);
		locationsList.add(locToAdd);

		GeneratePolicy stdFirePolicyObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.StandardFire)
				.withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Change", "StdFire")
				.withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
		
		issuePolicyChangeAndCheckUWActivity(stdFirePolicyObj, driver);
	}
	
	@Test
    public void testStdLiabAgentAutoSubmitPolicyChange() throws Exception {

        Config cf = new Config(ApplicationOrCenter.PolicyCenter);
        driver = buildDriver(cf);

        // GENERATE POLICY
        ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
        PolicyLocation propLoc = new PolicyLocation();
        propLoc.setPlNumAcres(3);
        propLoc.setPlNumResidence(2);
        locationsList.add(propLoc);

        SquireLiability myLiab = new SquireLiability();
        myLiab.setGeneralLiabilityLimit(SectionIIGeneralLiabLimit.Limit_75000_CSL);

        StandardFireAndLiability myStandardLiability = new StandardFireAndLiability();
        myStandardLiability.liabilitySection = myLiab;
        myStandardLiability.setLocationList(locationsList);

        GeneratePolicy myStdLibobj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardLiability)
                .withLineSelection(LineSelection.StandardLiabilityPL)
                .withStandardLiability(myStandardLiability)
                .withInsFirstLastName("Change", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.PolicyIssued);
        
        issuePolicyChangeAndCheckUWActivity(myStdLibobj, driver);

    }


	public void issuePolicyChangeAndCheckUWActivity(GeneratePolicy pol, WebDriver cf) throws Exception {
		new Login(driver).loginAndSearchPolicyByAccountNumber(pol.agentInfo.getAgentUserName(),
				pol.agentInfo.getAgentPassword(), pol.accountNumber);

		// start policy change
		StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change",
				DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter));

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();
		GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);

		// SQ004 - Squire Organization type of other
		policyInfo.setPolicyInfoOrganizationType(OrganizationTypePL.Other);
		policyInfo.setPolicyInfoDescription("Testing Description");

		sideMenu.clickSideMenuRiskAnalysis();
		GenericWorkorderRiskAnalysis_PriorLosses risk = new GenericWorkorderRiskAnalysis_PriorLosses(driver);
		new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();
		risk.Quote();
		GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		if (quotePage.isPreQuoteDisplayed()) {
			quotePage.clickPreQuoteDetails();
		}
		sideMenu.clickSideMenuRiskAnalysis();
		
		boolean activityExists = risk.getUnderwriterIssues().getInformationalList().size() > 0 ;
		if(risk.getUnderwriterIssues().getBlockSubmitList().size() > 0 || risk.getUnderwriterIssues().getBlockIssuanceList().size() > 0 || risk.getUnderwriterIssues().getBlockQuoteList().size() > 0){
			 new GenericWorkorderRiskAnalysis(driver).requestApproval();
		        
		        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
				new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
				completePage.clickPolicyNumber();
     			Underwriters uw = UnderwritersHelper.getUnderwriterInfoByFullName(new PolicySummary(driver).getActivityAssignment("Approval Requested"));
				new GuidewireHelpers(driver).logout();
		        new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), pol.accountNumber);
		        new PolicySummary(driver).clickActivity("Approval Requested");
		        new SideMenuPC(driver).clickSideMenuRiskAnalysis();
		        new GenericWorkorderRiskAnalysis_UWIssues(driver).approveAll();
		        new GenericWorkorderRiskAnalysis(driver).clickReleaseLock();
		        new GuidewireHelpers(driver).logout();
		        new Login(driver).loginAndSearchPolicyByAccountNumber(pol.agentInfo.getAgentUserName(),
						pol.agentInfo.getAgentPassword(), pol.accountNumber);
		        new PolicySummary(driver).clickPendingTransaction(TransactionType.Policy_Change);
		        
		}
		sideMenu.clickSideMenuRiskAnalysis();
		Assert.assertTrue(policyChangePage.checkIssuePolicy(),
				"Agent Login - Expected Issue policy button does not exists.");
		policyChangePage.clickIssuePolicy();
		GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
		completePage.clickPolicyNumber();
		PolicySummary polSummary = new PolicySummary(driver);
		Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Policy_Change),
				"Agent Auto Issue  - completed policy change was found.");
		
		if(activityExists){
			Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed.");
		}
				
	}

}
