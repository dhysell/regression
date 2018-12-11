package regression.r2.noclock.policycenter.change.subgroup5;

import java.util.ArrayList;
import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInterestType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CoverageType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentDeductible;
import repository.gw.enums.IMFarmEquipment.IMFarmEquipmentType;
import repository.gw.enums.InlandMarineTypes.InlandMarine;
import repository.gw.enums.LineSelection;
import repository.gw.enums.ProductLineType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FarmEquipment;
import repository.gw.generate.custom.IMFarmEquipmentScheduledItem;
import repository.gw.generate.custom.StandardInlandMarine;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.account.AccountSummaryPC;
import repository.pc.policy.PolicyDocuments;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderStandardIMFarmEquipment;
import persistence.enums.Underwriter;
import persistence.globaldatarepo.entities.Underwriters;
import persistence.globaldatarepo.helpers.UnderwritersHelper;

/**
 * @Author nvadlamudi
 * @Requirement : DE5696:Inland Marine/Section IV Farm Equipment Interest Dec and Endorsements going to same interest
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/120828077060"></a>
 * @Description : Test to validate that additional interest document and farm equipment endorsement document are not going for same additional interest contact.
 * @DATE Jun 5, 2017
 */
@QuarantineClass
public class TestStandardIMRemoveCoverages extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy stdIMPolObj;
	private Underwriters uw;

	@Test
	public void testCreateStandardIMPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<InlandMarine> imCoverages = new ArrayList<InlandMarine>();
		imCoverages.add(InlandMarine.FarmEquipment);

		// Farm Equipment

		ArrayList<AdditionalInterest> CircleSprinklerAdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest CircleSprinklerAddInterest = new AdditionalInterest(ContactSubType.Company);
		CircleSprinklerAddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		CircleSprinklerAddInterest.setCompanyName("Wells Fargo");
		CircleSprinklerAddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		CircleSprinklerAdditionalInterests.add(CircleSprinklerAddInterest);

		ArrayList<AdditionalInterest> farmEquipmentAdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest farmEquipmentAddInterest = new AdditionalInterest(ContactSubType.Company);
		farmEquipmentAddInterest.setAdditionalInterestType(AdditionalInterestType.LienholderPL);
		farmEquipmentAddInterest.setCompanyName("Wells Fargo");
		farmEquipmentAddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		farmEquipmentAdditionalInterests.add(farmEquipmentAddInterest);

        IMFarmEquipmentScheduledItem scheduledItem = new IMFarmEquipmentScheduledItem("Circle Sprinkler", "Manly Farm Equipment", 1000);

		ArrayList<IMFarmEquipmentScheduledItem> farmEquip = new ArrayList<IMFarmEquipmentScheduledItem>();
		farmEquip.add(scheduledItem);

        FarmEquipment imFarmEquip1 = new FarmEquipment(IMFarmEquipmentType.CircleSprinkler, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		imFarmEquip1.setAdditionalInterests(CircleSprinklerAdditionalInterests);

        FarmEquipment imFarmEquip2 = new FarmEquipment(IMFarmEquipmentType.FarmEquipment, CoverageType.BroadForm, IMFarmEquipmentDeductible.FiveHundred, true, false, "Cor Hofman", farmEquip);
		imFarmEquip2.setAdditionalInterests(farmEquipmentAdditionalInterests);

		ArrayList<FarmEquipment> allFarmEquip = new ArrayList<FarmEquipment>();
		allFarmEquip.add(imFarmEquip1);
		allFarmEquip.add(imFarmEquip2);

        StandardInlandMarine myStandardInlandMarine = new StandardInlandMarine();
        myStandardInlandMarine.farmEquipment = allFarmEquip;
        myStandardInlandMarine.inlandMarineCoverageSelection_Standard_IM = imCoverages;

        stdIMPolObj = new GeneratePolicy.Builder(driver)
                .withProductType(ProductLineType.StandardIM)
                .withLineSelection(LineSelection.StandardInlandMarine)
                .withStandardInlandMarine(myStandardInlandMarine)
                .withInsFirstLastName("Remove", "IMCov")
                .build(GeneratePolicyType.PolicyIssued);
	}

	@Test(dependsOnMethods = { "testCreateStandardIMPolicy" })
	private void testStandardIMRemoveFarmEquipment() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(stdIMPolObj.agentInfo.getAgentUserName(),
                stdIMPolObj.agentInfo.getAgentPassword(), stdIMPolObj.standardInlandMarine.getPolicyNumber());
		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuStandardIMCoverageSelection();
		sideMenu.clickSideMenuStandardIMFarmEquipment();
		GenericWorkorderStandardIMFarmEquipment farmEquipmentPage = new GenericWorkorderStandardIMFarmEquipment(driver);
		farmEquipmentPage.clickFarmEqipmentTableSpecificCheckbox(2, true);
		farmEquipmentPage.ClickRemove();

		
		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.performRiskAnalysisAndQuote(stdIMPolObj);

        policyChangePage = new StartPolicyChange(driver);
		policyChangePage.clickSubmitPolicyChange();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

		new GuidewireHelpers(driver).logout();
		driver.quit();
		
		uw = UnderwritersHelper.getUnderwriterInfoByLineAndTitle(Underwriter.UnderwriterLine.Personal,
				Underwriter.UnderwriterTitle.Underwriter_Supervisor);

		cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchAccountByAccountNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
				stdIMPolObj.accountNumber);

        AccountSummaryPC acct = new AccountSummaryPC(driver);
		acct.clickActivitySubject("Submitted policy change");

        sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuRiskAnalysis();
        riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.approveAll_IncludingSpecial();
		sideMenu.clickSideMenuQuote();
        completePage = new GenericWorkorderComplete(driver);

        policyChangePage = new StartPolicyChange(driver);
		policyChangePage.clickIssuePolicy();
        completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());
    }

	@Test(dependsOnMethods = { "testStandardIMRemoveFarmEquipment" })
	private void testStandardIMDocuments() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(uw.getUnderwriterUserName(), uw.getUnderwriterPassword(),
                stdIMPolObj.standardInlandMarine.getPolicyNumber());

		AddressInfo circlerAddress = stdIMPolObj.squire.inlandMarine.farmEquipment.get(0).getAdditionalInterests().get(0).getAddress();
		String circlerFarmAdditionalInterestName = stdIMPolObj.squire.inlandMarine.farmEquipment.get(0).getAdditionalInterests().get(0)
				.getCompanyName() + "\n" 
				+ circlerAddress.getLine1() + "\n" 
				+ circlerAddress.getCity() + ", " +circlerAddress.getState().getAbbreviation() + " " +circlerAddress.getZip();
		
		AddressInfo farmAddress = stdIMPolObj.squire.inlandMarine.farmEquipment.get(1).getAdditionalInterests().get(0).getAddress();
		String farmEquipAdditionalInterestName = stdIMPolObj.squire.inlandMarine.farmEquipment.get(1).getAdditionalInterests().get(0)
				.getCompanyName() + "\n"
						+ farmAddress.getLine1() + "\n" 
						+ farmAddress.getCity() + ", " +farmAddress.getState().getAbbreviation() + " " +farmAddress.getZip();

        SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuToolsDocuments();
        PolicyDocuments docs = new PolicyDocuments(driver);
		docs.selectRelatedTo("Issuance");
		docs.setDocumentDescription("Farm Equipment Endorsement");
		docs.clickSearch();

		String errorMessage = "";
		if (checkDocumentWithAddress(circlerFarmAdditionalInterestName) != 1) {
			errorMessage = errorMessage + "Additional Interest Name and Address displayed : "
					+ circlerFarmAdditionalInterestName
					+ "displayed twice for document Desc : Farm Equipment Endorsement \n";
		}

		if (checkDocumentWithAddress(farmEquipAdditionalInterestName) != 1) {
			errorMessage = errorMessage + "Additional Interest Name and Address displayed : "
					+ farmEquipAdditionalInterestName
					+ "displayed twice for document Desc : Farm Equipment Endorsement \n";
		}

		docs.setDocumentDescription("Additional Interest Declarations");
		docs.clickSearch();

		if (checkDocumentWithAddress(circlerFarmAdditionalInterestName) != 1) {
			errorMessage = errorMessage + "Additional Interest Name and Address displayed : "
					+ circlerFarmAdditionalInterestName
					+ "displayed twice for document Desc : Additional Interest Declarations \n";
		}

		if (checkDocumentWithAddress(farmEquipAdditionalInterestName) != 1) {
			errorMessage = errorMessage + "Additional Interest Name and Address displayed : "
					+ farmEquipAdditionalInterestName
					+ "displayed twice for document Desc : Additional Interest Declarations \n";
		}

				
		if(errorMessage !=""){
			Assert.fail(errorMessage);
		}

	}

	private int checkDocumentWithAddress(String nameaddress) {
        PolicyDocuments docs = new PolicyDocuments(driver);
		ArrayList<String> allTableElements = docs.getDocumentNameAddress();
		int rowAddress = 0;
		for (String current : allTableElements) {
			if (current.contains(nameaddress)) {
				rowAddress = rowAddress + 1;
			}
		}

		return rowAddress;
	}
}
