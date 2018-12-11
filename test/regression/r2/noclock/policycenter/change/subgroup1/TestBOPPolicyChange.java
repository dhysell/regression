package regression.r2.noclock.policycenter.change.subgroup1;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import repository.gw.enums.AdditionalInterestBilling;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.PremisesMedicalExpense;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.PaymentPlanType;
import repository.gw.enums.PaymentType;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AdditionalInterest;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.helpers.DateUtils;
import repository.gw.helpers.GuidewireHelpers;
import repository.gw.helpers.NumberUtils;
import repository.gw.login.Login;
import com.idfbins.driver.BaseTest;

import repository.driverConfiguration.Config;
import gwclockhelpers.ApplicationOrCenter;
import repository.pc.search.SearchOtherJobsPC;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.change.StartPolicyChange;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalCoverages;
import repository.pc.workorders.generic.GenericWorkorderComplete;
import repository.pc.workorders.generic.GenericWorkorderQualification;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis_UWIssues;

/**
 * @Author nvadlamudi
 * @Requirement :DE4276: BOP invalid quote - prorated quote not rounding
 * @RequirementsLink <a href="https://rally1.rallydev.com/#/33274298124d/detail/defect/73039634864">Link Text</a>
 * @Description 
 * @DATE Mar 16, 2017
 */
public class TestBOPPolicyChange extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy bopPolicyObj;

	@Test()
	private void testIssueBOPPolicy() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		ArrayList<AdditionalInterest> loc1LNBldg1AdditionalInterests = new ArrayList<AdditionalInterest>();
		AdditionalInterest loc1LNBldg1AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg1AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg1AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg1AddInterest.setLoanContractNumber("LN12345");
		loc1LNBldg1AdditionalInterests.add(loc1LNBldg1AddInterest);		
		PolicyLocationBuilding polBuilding = new PolicyLocationBuilding();
		polBuilding.setAdditionalInterestList(loc1LNBldg1AdditionalInterests);
		locOneBuildingList.add(polBuilding);


		ArrayList<AdditionalInterest> loc1LNBldg2AdditionalInterests = new ArrayList<AdditionalInterest>();		
		AdditionalInterest loc1LNBldg2AddInterest = new AdditionalInterest(ContactSubType.Company);
		loc1LNBldg2AddInterest.setAdditionalInterestBilling(AdditionalInterestBilling.Bill_Lienholder);
		loc1LNBldg2AddInterest.setNewContact(CreateNew.Create_New_Only_If_Does_Not_Exist);
		loc1LNBldg2AddInterest.setLoanContractNumber("LN12345");
		loc1LNBldg2AdditionalInterests.add(loc1LNBldg2AddInterest);		
		PolicyLocationBuilding polBuilding2 = new PolicyLocationBuilding();
		polBuilding2.setAdditionalInterestList(loc1LNBldg2AdditionalInterests);
		locOneBuildingList.add(polBuilding2);		

		PolicyLocationBuilding polBuilding3 = new PolicyLocationBuilding();
		locOneBuildingList.add(polBuilding3);	
		locationsList.add(new PolicyLocation(new AddressInfo(true), locOneBuildingList));

		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(smallBusiness.get(NumberUtils.generateRandomNumberInt(0, (smallBusiness.size() - 1)))) {{
			this.setLiabilityLimits(liabilityLimits.get(NumberUtils.generateRandomNumberInt(0, (liabilityLimits.size() - 1))));
			this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {{
				this.setEmployeeDishonestyCoverage(true);
				this.setEmpDisLimit(EmpDishonestyLimit.Dishonest25000);
				this.setEmpDisNumCoveredEmployees(20);
				this.setEmpDisReferencesChecked(true);
				this.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Monthly);
				this.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.CPA);
				this.setEmpDisDiffWriteThanReconcile(true);
				this.setEmpDisLargeCheckProcedures(true);
				this.setHiredAutoCoverage(false);
				this.setHiredAutoOwnedAuto(false);
				this.setNonOwnedAutoLiabilityCoverage(true);
				this.setNonOwnedAutoNonCompanyVehicle(true);
			}}); // END ADDITIONAL COVERAGES
		}}; // END BUSINESS OWNERS LINE
		boLine.locationList = locationsList;

        bopPolicyObj = new GeneratePolicy.Builder(driver)
				.withInsFirstLastName("BOP", "PolChange")
				.withBusinessownersLine(boLine)
				.withPaymentPlanType(PaymentPlanType.Annual)
				.withDownPaymentType(PaymentType.Cash)
				.build(GeneratePolicyType.PolicyIssued);
	}

	private List<SmallBusinessType> smallBusiness = new ArrayList<SmallBusinessType>() {
		private static final long serialVersionUID = 1L;

		{
			this.add(SmallBusinessType.Apartments);
			this.add(SmallBusinessType.Offices);
			this.add(SmallBusinessType.Condominium);
			this.add(SmallBusinessType.SelfStorageFacilities);
		}
	};

	private List<LiabilityLimits> liabilityLimits = new ArrayList<LiabilityLimits>() {
		private static final long serialVersionUID = 1L;

		{
			this.add(LiabilityLimits.Three00_600_600);
			this.add(LiabilityLimits.Five00_1000_1000);
			this.add(LiabilityLimits.One000_2000_2000);
			this.add(LiabilityLimits.Two000_4000_4000);
		}
	};

	@Test(dependsOnMethods = { "testIssueBOPPolicy" })
	private void testValidateCostChangeDetailsBOP() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		new Login(driver).loginAndSearchPolicyByAccountNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(),
				bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.accountNumber);

		Date currentSystemDate = DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter);

		// add 10 days to current date
		Date changeDate = DateUtils.dateAddSubtract(currentSystemDate, DateAddSubtractOptions.Day, 10);

		// start policy change
        StartPolicyChange policyChangePage = new StartPolicyChange(driver);
		policyChangePage.startPolicyChange("First policy Change", changeDate);

		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBuildings();
        GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);
		buildings.removeBuildingByBldgNumber(2);
		buildings.removeBuildingByBldgNumber(3);

		buildings.clickBuildingsBuildingEdit(1);
		buildings.setBuildingLimit(this.bopPolicyObj.busOwnLine.locationList.get(0).getBuildingList().get(0).getBuildingLimit() + 5000);
		buildings.clickAdditionalCoverages();
		buildings.clickOK();

		sideMenu.clickSideMenuBusinessownersLine();

        GenericWorkorderBusinessownersLineAdditionalCoverages bopAddCov = new GenericWorkorderBusinessownersLineAdditionalCoverages(driver);

        bopPolicyObj.busOwnLine.setLiabilityLimits(LiabilityLimits.Two000_4000_4000);
        bopPolicyObj.busOwnLine.setMedicalLimit(PremisesMedicalExpense.Exp10000);
        bopPolicyObj.busOwnLine.getAdditionalCoverageStuff().setHiredAutoCoverage(true);
        bopPolicyObj.busOwnLine.getAdditionalCoverageStuff().setEmpDisLimit(EmpDishonestyLimit.Dishonest25000);
        bopAddCov.fillOutBusinessownersLinePages(bopPolicyObj.basicSearch, bopPolicyObj.busOwnLine);

		GenericWorkorderQualification qualificationPage = new GenericWorkorderQualification(driver);
		qualificationPage.clickQuote();
        GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if(quote.isPreQuoteDisplayed()) {
			quote.clickPreQuoteDetails();
            GenericWorkorderRiskAnalysis_UWIssues riskAnaysis = new GenericWorkorderRiskAnalysis_UWIssues(driver);
			riskAnaysis.handleBlockSubmit(bopPolicyObj);
			qualificationPage.clickQuote();
			new GuidewireHelpers(driver).logout();
            new Login(driver).loginAndSearchPolicyByPolicyNumber(bopPolicyObj.underwriterInfo.getUnderwriterUserName(), bopPolicyObj.underwriterInfo.getUnderwriterPassword(), bopPolicyObj.busOwnLine.getPolicyNumber());
			SearchOtherJobsPC searchJob = new SearchOtherJobsPC(driver);
			searchJob.searchJobByAccountNumber(bopPolicyObj.accountNumber, "003");
			SideMenuPC sideMenuStuff = new SideMenuPC(driver);
			sideMenuStuff.clickSideMenuQuote();
		}
        sideMenu.clickSideMenuQuote();
		sideMenu.clickSideMenuPayment();
        StartPolicyChange change = new StartPolicyChange(driver);
        change = new StartPolicyChange(driver);
		change.clickIssuePolicy();
        GenericWorkorderComplete completePage = new GenericWorkorderComplete(driver);
		new GuidewireHelpers(driver).waitUntilElementIsVisible(completePage.getJobCompleteTitleBar());

	}
}
