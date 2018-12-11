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
import repository.gw.enums.PLUWIssues;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.Property.PropertyTypePL;
import repository.gw.enums.Property.SectionIIGeneralLiabLimit;
import repository.gw.enums.TransactionType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.PLPolicyLocationProperty;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.SquireLiability;
import repository.gw.generate.custom.StandardFireAndLiability;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.generate.custom.UnderwriterIssue;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.policy.PolicySummary;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPayment;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

/**
* @Author nvadlamudi
* @Requirement :US15615: STRETCH:  Enable auto-issuance of agent-submitted new business policies - Standard Lines
* @RequirementsLink <a href="http://projects.idfbins.com/policycenter/Documents/Story%20Cards/01_PolicyCenter/01_User_Stories/PolicyCenter%208.0/Personal%20Lines%20-%20Common/PC8%20-%20Personal%20Lines%20-%20Auto%20Issuance.xlsx">PC8 - Personal Lines - Auto Issuance</a>
* @Description : validate agent can auto issue a standard lines (standard fire, standard liab and standard im)
* @DATE Aug 20, 2018
*/
public class US15615StandardAgentAutoIssueNewBusiness extends BaseTest {
	private String activityName = "Auto-Issued Submission for UW Review";
	private WebDriver driver;

	@Test
	public void testStdIMAgentAutoIssueNewBusiness() throws Exception {
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
				.withInsFirstLastName("Submi", "StndIM").withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.FullApp);
		issuePolicySubmissionAndCheckUWActivity(stdIMPolObj);
	}

	@Test
	public void testStdFireAgentAutoIssueNewBusiness() throws Exception {

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
				.withProductType(ProductLineType.StandardFire).withLineSelection(LineSelection.StandardFirePL)
				.withCreateNew(CreateNew.Create_New_Always).withInsPersonOrCompany(ContactSubType.Person)
				.withInsFirstLastName("Submit", "StdFire").withPolicyLocations(locationsList)
				.withPaymentPlanType(PaymentPlanType.Annual).withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.FullApp);
		
		issuePolicySubmissionAndCheckUWActivity(stdFirePolicyObj);
	}
	
	@Test
    public void testStdLiabAgentAutoIssueNewBusiness() throws Exception {

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
                .withInsFirstLastName("Submit", "StdLiab")
                .withPolOrgType(OrganizationType.Individual)
                .withPaymentPlanType(PaymentPlanType.Annual)
                .withDownPaymentType(PaymentType.Cash)
                .build(GeneratePolicyType.FullApp);
        
        issuePolicySubmissionAndCheckUWActivity(myStdLibobj);

    }


	public void issuePolicySubmissionAndCheckUWActivity(GeneratePolicy pol) throws Exception {
		new Login(driver).loginAndSearchSubmission(pol);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPayment();
		GenericWorkorderPayment payment = new GenericWorkorderPayment(driver);
		payment.fillOutPaymentPage(pol);
		sideMenu.clickSideMenuForms();
		Assert.assertTrue(payment.SubmitOptionsButtonExists(),
				"Auto Issuance - Agent can't have Submit Options to submit a policy..");
			Assert.assertTrue(payment.checkGenericWorkorderSubmitOptionsIssuePolicyOption(),
					"Auto Issuance - Agent can't see Submit Options -> Issue Policy option to issue a policy..");
			sideMenu.clickSideMenuForms();
			sideMenu.clickSideMenuRiskAnalysis();
			FullUnderWriterIssues uwIssues = new GenericWorkorderRiskAnalysis(driver).getUnderwriterIssues();
			boolean checkUWInfo =  uwIssues.getInformationalList().size() > 0;
			//Adding the below loop for checking the ClaimCenter Down - UW Issues -- Test failed in REGR environments because of this
			for(UnderwriterIssue currentUW :uwIssues.getInformationalList()){
				if(currentUW.getShortDescription().equalsIgnoreCase(PLUWIssues.ClaimCenterDown.getShortDesc())){
					checkUWInfo =  uwIssues.getInformationalList().size() > 1;
				}
			}
			
			payment.clickGenericWorkorderSubmitOptionsIssuePolicy();
			GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
			new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
			completePage.clickPolicyNumber();
			PolicySummary polSummary = new PolicySummary(driver);
			Assert.assertNotNull(polSummary.getCompletedTransactionNumberByType(TransactionType.Issuance),
					"Agent Auto Issue  - completed Issuance job not found.");
			
			if(checkUWInfo){
				Assert.assertTrue(polSummary.checkIfActivityExists(activityName),"Expected Activity : '" + activityName + "' is not displayed.");	
			}
					

	}

}
