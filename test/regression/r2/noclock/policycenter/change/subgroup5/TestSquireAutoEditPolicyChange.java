package regression.r2.noclock.policycenter.change.subgroup5;

import java.util.Date;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.idfbins.enums.Gender;
import com.idfbins.testng.listener.QuarantineClass;

import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalNamedInsuredType;
import repository.gw.enums.CommuteType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.MaritalStatus;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SquirePersonalAutoCoverages.LiabilityLimit;
import repository.gw.enums.SquirePersonalAutoCoverages.MedicalLimit;
import repository.gw.enums.UnderwriterIssueType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyInfoAdditionalNamedInsured;
import repository.gw.generate.custom.SquirePersonalAuto;
import repository.gw.generate.custom.SquirePersonalAutoCoverages;
import repository.gw.generate.custom.Vehicle;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.StringsUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import gwclockhelpers.ApplicationOrCenter;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderPolicyInfo;
import repository.pc.workorders.generic.GenericWorkorderPolicyMembers;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoCoverages;
import repository.pc.workorders.generic.GenericWorkorderSquireAutoDrivers_ContactDetail;
import repository.pc.workorders.generic.GenericWorkorderVehicles_CoverageDetails;
import repository.pc.workorders.generic.GenericWorkorderVehicles_ExclusionsAndConditions;
import persistence.globaldatarepo.entities.Agents;
import persistence.globaldatarepo.helpers.AgentsHelper;

/**
 * @Author skandibanda
 * @Requirement : US7555 : [Part II] PL - Edit Policy Change - Section III
 * @RequirementsLink <a href="http:// "
 * rally1.rallydev.com/#/33274298124d/detail/userstory/
 * 54446240611</a>
 * @Description - Issue Squire Auto Policy and do policy changes and Perform UW
 * validations
 * @DATE June 21, 2016
 */
@QuarantineClass
public class TestSquireAutoEditPolicyChange extends BaseTest {
	private WebDriver driver;
	public GeneratePolicy myPolicyObj;
	private Agents agent;

	@Test()
	public void testGenerateSquireAutoIssuance() throws Exception {
		this.agent = AgentsHelper.getRandomAgent();

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);

		SquirePersonalAutoCoverages coverages = new SquirePersonalAutoCoverages(LiabilityLimit.FiftyHigh,
				MedicalLimit.TenK);
		SquirePersonalAuto squirePersonalAuto = new SquirePersonalAuto();
		squirePersonalAuto.setCoverages(coverages);

        myPolicyObj = new GeneratePolicy.Builder(driver).withAgent(agent).withProductType(ProductLineType.Squire)
				.withLineSelection(LineSelection.PersonalAutoLinePL).withCreateNew(CreateNew.Create_New_Always)
				.withInsPersonOrCompany(ContactSubType.Person).withInsFirstLastName("Guy", "Auto")
				.withSocialSecurityNumber(StringsUtils.generateRandomNumber(666000000, 666999999))
				.withPolOrgType(OrganizationType.Individual).withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash).build(GeneratePolicyType.PolicyIssued);

	}

	@Test(dependsOnMethods = { "testGenerateSquireAutoIssuance" })
	public void verifyEditPolicyChangesSquireAuto() throws Exception {

		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByPolicyNumber(myPolicyObj.agentInfo.getAgentUserName(),
				myPolicyObj.agentInfo.getAgentPassword(), myPolicyObj.squire.getPolicyNumber());

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);
        Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

        SideMenuPC sideMenu = new SideMenuPC(driver);

		// add ANI
		sideMenu.clickSideMenuPolicyInfo();
        GenericWorkorderPolicyInfo policyInfo = new GenericWorkorderPolicyInfo(driver);
		PolicyInfoAdditionalNamedInsured pANI = new PolicyInfoAdditionalNamedInsured(ContactSubType.Person,
				"Test" + StringsUtils.generateRandomNumberDigits(8), "Comp", AdditionalNamedInsuredType.Spouse,
				new AddressInfo(true));
		pANI.setNewContact(CreateNew.Create_New_Always);
		policyInfo.addAdditionalNamedInsured(myPolicyObj.basicSearch, pANI);
		new GuidewireHelpers(driver).clickNext();

        GenericWorkorderPolicyMembers hmember = new GenericWorkorderPolicyMembers(driver);
		int row = hmember.getPolicyHouseholdMembersTableRow("Test");
		hmember.clickPolicyHouseHoldTableCell(row, "Name");
        GenericWorkorderPolicyMembers householdMember = new GenericWorkorderPolicyMembers(driver);
		householdMember.setDateOfBirth("01/01/1980");
		householdMember.selectNotNewAddressListingIfNotExist(pANI.getAddress());
		householdMember.clickRelatedContactsTab();
        householdMember.clickOK();

		sideMenu.clickSideMenuPADrivers();

		// Change to SSN, Change Driver's info
        GenericWorkorderSquireAutoDrivers_ContactDetail paDrivers = new GenericWorkorderSquireAutoDrivers_ContactDetail(driver);
		paDrivers.clickEditButtonInDriverTable(1);
		paDrivers.selectMaritalStatus(MaritalStatus.Single);
		paDrivers.selectGender(Gender.Male);
		//paDrivers.setSSN("538-55-1986");
        paDrivers.selectCommuteType(CommuteType.ParkAndRide);
		paDrivers.clickOk();
		paDrivers.clickNext();

		// Line level coverage reduced or removed
		GenericWorkorderSquireAutoCoverages coverages = new GenericWorkorderSquireAutoCoverages(driver);
		myPolicyObj.squire.squirePA.getCoverages().setLiability(LiabilityLimit.FiftyLow);
		myPolicyObj.squire.squirePA.getCoverages().setMedical(MedicalLimit.TenK);
		coverages.fillOutSquireAutoCoverages_Coverages(myPolicyObj);
		coverages.fillOutSquireAutoCoverages_ExclusionsAndConditions(myPolicyObj);
		coverages.clickNext();

		// 310 added or removed
		Vehicle Vehicle = new Vehicle();
		Vehicle.setLocationOfDamage("Left Rear");
		Vehicle.setDamageItem("Tail Light");
		Vehicle.setDamageDescription("Missing");

		// added or removed condition 301
		GenericWorkorderVehicles_ExclusionsAndConditions Vehicles = new GenericWorkorderVehicles_ExclusionsAndConditions(driver);
		Vehicles.clickLinkInVehicleTable("Edit");
		Vehicles.fillOutExclusionsAndConditions(Vehicle);

		// Vehicle level coverage added for first time		
		GenericWorkorderVehicles_CoverageDetails vehicle_Coverages = new GenericWorkorderVehicles_CoverageDetails(driver);
		Vehicles.selectVehicleCoverageDetailsTab();
        vehicle_Coverages.setFireTheftCoverage(true);
		vehicle_Coverages.clickOK();
        vehicle_Coverages.clickNext();

		sideMenu.clickSideMenuRiskAnalysis();
        GenericWorkorderRiskAnalysis riskAnalysis = new GenericWorkorderRiskAnalysis(driver);
		riskAnalysis.Quote();
		sideMenu.clickSideMenuRiskAnalysis();

		// validate UW block bind and block issuance
		boolean testFailed = false;
		String errorMessage = "";

		String[] expectedUWMessages = { "Added or removed condition 301", "A Contact detail on the policy has changed",
				"Change to driver info", "310 added or removed", "ANI change to policy",
				"Line level coverage reduced or removed" };

		FullUnderWriterIssues uwIssues = riskAnalysis.getUnderwriterIssues();
		for (String uwIssue : expectedUWMessages) {
			if(uwIssues.isInList(uwIssue).equals(UnderwriterIssueType.NONE)) {
				testFailed = true;
				errorMessage = errorMessage + "Expected error Bind Issue : " + uwIssue + " is not displayed.";
			}
		}

		if (testFailed)
			Assert.fail(errorMessage);
	}
}
