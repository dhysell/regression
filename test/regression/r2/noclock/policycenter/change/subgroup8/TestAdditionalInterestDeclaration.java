package regression.r2.noclock.policycenter.change.subgroup8;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.activity.ActivityPopup;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.IssuanceType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.desktop.DesktopProofOfMailPC;
import repository.pc.desktop.DesktopSideMenuPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderAdditionalInterests;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireInlandMarine_FarmEquipment;
import repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author skandibanda
 * @Requirement : DE5703:Additional Interest Cancel not generating for Farm Equipment section IV cancel
 * @Description -
 * @DATE June 14, 2017
 */
@QuarantineClass
public class TestAdditionalInterestDeclaration extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy stdIMPolObj;
	private Underwriters uw;

	@Test
	public void testGenerateStdIMFullApp() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
		imCoverages.add(InlandMarine.FarmEquipment);

		// Farm Equipment		
		IMFarmEquipmentScheduledItem scheduledItem1 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem1);			
		FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);	

		IMFarmEquipmentScheduledItem scheduledItem2 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip2 = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip2.add(scheduledItem2);			
		FarmEquipment imFarmEquip2 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);		

		IMFarmEquipmentScheduledItem scheduledItem3 = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);
		ArrayList<IMFarmEquipmentScheduledItem> farmEquip3 = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip3.add(scheduledItem3);			
		FarmEquipment imFarmEquip3 = new FarmEquipment(IMFarmEquipmentType.WheelSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);		

		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);
		allFarmEquip.add(imFarmEquip2);
		allFarmEquip.add(imFarmEquip3);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = imCoverages;

        stdIMPolObj = new GeneratePolicy.Builder(driver)
				.withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
				.withInsFirstLastName("Inference", "IMForm")
				.build(GeneratePolicyType.FullApp);	
	}

	@Test (dependsOnMethods = { "testGenerateStdIMFullApp" })
	public void testStdIMIssuance() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchJob(stdIMPolObj.agentInfo.getAgentUserName(), stdIMPolObj.agentInfo.getAgentPassword(), stdIMPolObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuPolicyInfo();

        GenericWorkorderPolicyInfo polInfo = new GenericWorkorderPolicyInfo(driver);
		polInfo.clickEditPolicyTransaction();
		sideMenu.clickSideMenuStandardIMFarmEquipment();
		GenericWorkorderSquireInlandMarine_FarmEquipment farmEquipment = new GenericWorkorderSquireInlandMarine_FarmEquipment(driver);
		GenericWorkorderStandardIMFarmEquipment farmEquipmentPage = new GenericWorkorderStandardIMFarmEquipment(driver);
		farmEquipment.clickEditButton();
		GenericWorkorderAdditionalInterests additionalInterests = new GenericWorkorderAdditionalInterests(driver);
		additionalInterests.searchAndAddExistingCompanyAsAdditionalInterest(stdIMPolObj.basicSearch, AdditionalInterestType.LessorPL);			
		farmEquipment.addFarmEquipmentScheduledItemExistingAdditionalInterest(additionalInterests.getAdditionalInterestsName(),AdditionalInterestType.LessorPL);
		farmEquipment.addScheduledItemAdditionalInsured("First Guy");	
		farmEquipmentPage.clickOk();
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();

		sideMenu.clickSideMenuForms();
		new GuidewireHelpers(driver).logout();

        stdIMPolObj.convertTo(driver, GeneratePolicyType.PolicySubmitted);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal, Underwriter.UnderwriterTitle.Underwriter);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(), stdIMPolObj.accountNumber);

        AccountSummaryPC accountSummaryPage = new AccountSummaryPC(driver);
		accountSummaryPage.clickActivitySubject("Submitted Full Application");
        ActivityPopup activityPopupPage = new ActivityPopup(driver);
		activityPopupPage.clickCloseWorkSheet();

        SideMenuPC sidemenu = new SideMenuPC(driver);
		sidemenu.clickSideMenuRiskAnalysis();
		risk.approveAll();	
		risk.Quote();		
		sidemenu.clickSideMenuQuote();
        GenericWorkorderQuote quotePage = new GenericWorkorderQuote(driver);
		quotePage.issuePolicy(IssuanceType.NoActionRequired);
		new GuidewireHelpers(driver).logout();

	}


	@Test (dependsOnMethods = { "testStdIMIssuance" })
	public void testStdIMAdditionalInterestCancellation() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,Underwriter.UnderwriterTitle.Underwriter_Supervisor);
		new Login(driver).loginAndSearchPolicyByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),stdIMPolObj.accountNumber);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);


		//add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);	

		//start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

		sideMenu.clickSideMenuStandardIMFarmEquipment();
		GenericWorkorderStandardIMFarmEquipment farmEquipmentPage = new GenericWorkorderStandardIMFarmEquipment(driver);
		farmEquipmentPage.clickFarmEqipmentTableSpecificCheckbox(1, true);
		farmEquipmentPage.ClickRemove();
        GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		risk.Quote();
		sideMenu.clickSideMenuRiskAnalysis();
		risk.approveAll();
		sideMenu.clickSideMenuQuote();
        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		completePage.clickPolicyNumber();

		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Change");
		docs.clickSearch();
        ArrayList<String> descriptions = docs.getDocumentsDescriptionsFromTable();

		String[] documents = {"Additional Interest Termination Notice"};

		boolean testFailed = false;
		String errorMessage = "";
		for (String document : documents) {	
			boolean documentFound = false;
			for(String desc: descriptions){
				if(desc.equals(document)){
					documentFound = true;
					break;
				}
			}
			if(!documentFound){
				testFailed = true;
				errorMessage = errorMessage + "Expected document : '"+document+ "' not available in documents page. \n";
			}
		}
		new GuidewireHelpers(driver).logout();
        Login lp = new Login(driver);
		lp.login(uw.getUnderwriterUserName(), uw.getUnderwriterPassword());
        DesktopSideMenuPC sidemenu = new DesktopSideMenuPC(driver);
		sidemenu.clickProofOfMail();
		DesktopProofOfMailPC ProofOfMail = new DesktopProofOfMailPC(driver);

        String description = ProofOfMail.getDescriptionFromTablebyPolicyNo(stdIMPolObj.standardInlandMarine.getPolicyNumber());
		if(!description.contains("Additional Interest Termination Notice")){
			testFailed = true;
			errorMessage = errorMessage + "Expected description 'Additional Interest Termination Notice' not displayed";			
		}
		if(testFailed)
			Assert.fail(errorMessage);
	}

}



