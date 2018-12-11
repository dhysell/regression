package previousProgramIncrement.pi1_041918_062718.f58_QuickerQuickQuote_ReduceRequiredFieldsPL;

import com.idfbins.driver.BaseTest;
import com.idfbins.enums.State;
import repository.driverConfiguration.Config;
import repository.gw.enums.AdditionalInsuredRole;
import repository.gw.enums.Building.BusinessIncomeOrdinaryPayroll;
import repository.gw.enums.Building.CauseOfLoss;
import repository.gw.enums.Building.RoofCondition;
import repository.gw.enums.Building.ValuationMethod;
import repository.gw.enums.BusinessownersLine.EmpDishonestyLimit;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency;
import repository.gw.enums.BusinessownersLine.EmployeeDishonestyAuditsPerformedBy;
import repository.gw.enums.BusinessownersLine.LiabilityLimits;
import repository.gw.enums.BusinessownersLine.SmallBusinessType;
import repository.gw.enums.ContactSubType;
import repository.gw.enums.CreateNew;
import repository.gw.enums.DateAddSubtractOptions;
import repository.gw.enums.GeneratePolicyType;
import repository.gw.enums.LineSelection;
import repository.gw.enums.Location.AutoIncreaseBlgLimitPercentage;
import repository.gw.enums.OrganizationType;
import repository.gw.enums.ProductLineType;
import repository.gw.enums.SpoilageLimit;
import repository.gw.generate.GeneratePolicy;
import repository.gw.generate.custom.AddressInfo;
import repository.gw.generate.custom.FullUnderWriterIssues;
import repository.gw.generate.custom.PolicyBusinessownersLine;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalCoverages;
import repository.gw.generate.custom.PolicyBusinessownersLineAdditionalInsured;
import repository.gw.generate.custom.PolicyLocation;
import repository.gw.generate.custom.PolicyLocationAdditionalCoverages;
import repository.gw.generate.custom.PolicyLocationBuilding;
import repository.gw.generate.custom.PolicyLocationBuildingAdditionalCoverages;
import repository.gw.helpers.DateUtils;
import repository.gw.login.Login;
import gwclockhelpers.ApplicationOrCenter;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import repository.pc.sidemenu.SideMenuPC;
import repository.pc.workorders.generic.GenericWorkorder;
import repository.pc.workorders.generic.GenericWorkorderBuildings;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineAdditionalInsured;
import repository.pc.workorders.generic.GenericWorkorderBusinessownersLineIncludedCoverages;
import repository.pc.workorders.generic.GenericWorkorderQuote;
import repository.pc.workorders.generic.GenericWorkorderRiskAnalysis;

import java.util.ArrayList;
import java.util.Date;

/**
* @Author nvadlamudi
* @Requirement : US14797: Eliminate UW issues @ QQ, BOP
* @RequirementsLink <a href="https://fbmicoi.sharepoint.com/:x:/s/ARTists2/EaHilEOT6e1EoP3Xi2gDicoB7Cuv10lVY5pz_x21dcw7aw?e=AnhJGb">US14797 BOP QQ UW Issues to Disable</a>
* @Description : Validate QQ not showing any specified UW Issues
* @DATE Jun 10, 2018
*/
public class US14797EliminateBOPQQUWIssues extends BaseTest {
	private WebDriver driver;
	private GeneratePolicy myQQPolObj;

	@Test
	public void testCheckBOPQQUWIssues() throws Exception {
		Config cf = new Config(ApplicationOrCenter.PolicyCenter);
		driver = buildDriver(cf);
		// LOCATIONS
		ArrayList<PolicyLocation> locationsList = new ArrayList<PolicyLocation>();
		PolicyLocation location1 = new PolicyLocation(new AddressInfo(true), false);
		//Locations -- Details	Is the operation seasonal-closed for more than 30 consecutive days?
		//Locations -- Location Details	If agent selects 8%  or 10% on Auto Increase Blg. Limit %.
		//Locations -- Location Details	If value exceeds $6,000,000 on "Annual Gross Receipts at this Location?" for the pool.
		location1.setAutoIncrease(AutoIncreaseBlgLimitPercentage.AutoInc10Perc);
		location1.setPlaygroundYes(true);
		location1.setSeasonalYes(true);
		location1.setAnnualGrossReceipts(7000000);
		
		//Locations -- Location Add.Coverages	If Outdoor Sign Limits is over $50,000.
		//Locations -- Location Add.Coverages	If On Premise or Off Premis is over $30,000.
		PolicyLocationAdditionalCoverages addCoverages = new PolicyLocationAdditionalCoverages();
		addCoverages.setOutdoorSignsCoverage(true);
		addCoverages.setOutdoorSignsLimit(60000);
		addCoverages.setMoneyAndSecuritiesCoverage(true);
		addCoverages.setMoneySecOnPremisesLimit(40000);
		addCoverages.setMoneySecOffPremisesLimit(40000);		
		location1.setAdditionalCoveragesStuff(addCoverages);
		// END LOCATIONS

		// BUILDINGS
		ArrayList<PolicyLocationBuilding> locOneBuildingList = new ArrayList<PolicyLocationBuilding>();
		PolicyLocationBuilding myBuilding = new PolicyLocationBuilding();
		myBuilding.setBuildingCauseOfLoss(CauseOfLoss.NamedPerils);
		myBuilding.setBuildingValuationMethod(ValuationMethod.ActualCashValue);
		myBuilding.setBuildingLimit(600000);
		myBuilding.setBppLimit(200000);
		myBuilding.setRoofCondition(RoofCondition.HasMajorDamage);
		myBuilding.setProtectionClassCode(9);
		myBuilding.setWoodBurningStove(true);
		myBuilding.setNumStories(4);
		PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff = new PolicyLocationBuildingAdditionalCoverages();
		additionalCoveragesStuff.setSpoilageCoverage(true);
		additionalCoveragesStuff.setSpoilageLimit(SpoilageLimit.Fifty000);
		additionalCoveragesStuff.setSpoilageBreakdownContamination(true);
		additionalCoveragesStuff.setSpoilagePowerOutage(false);
		additionalCoveragesStuff.setSpoilageRefrigerationMaintAgreement(false);
		additionalCoveragesStuff.setFoodContaminationCoverage(true);
		additionalCoveragesStuff.setFoodContaminationLimit(45000);
		additionalCoveragesStuff.setOrdinanceOrLawCoverage(false);
		additionalCoveragesStuff.setOrdinanceOrLawDemoCostLimit(120000);
		additionalCoveragesStuff.setOrdinanceOrLawIncrCostConstructionLimit(60000);
		additionalCoveragesStuff.setOrdinanceOrLawDemoAndIncrCostConstructionCombLimit(200000);
		additionalCoveragesStuff.setValuablePapersOptionalCoverage(true);
		additionalCoveragesStuff.setValuablePapersOptionalOnPremisesLimit(200000);
		additionalCoveragesStuff.setAccountsReceivableOptionalCoverage(true);
		additionalCoveragesStuff.setAccountsReceivableOptionalOnPremisesLimit(200000);
		additionalCoveragesStuff.setBusinessIncomeOrdinaryPayrollCoverage(true);
		additionalCoveragesStuff.setBusinessIncomeOrdinaryPayrollType(BusinessIncomeOrdinaryPayroll.Days60);
		additionalCoveragesStuff.setCondoCommercialUnitOwnersOptionalCoverage(true);
		additionalCoveragesStuff.setCondoCommercialUnitOwnersOptionalMiscRealProperty(60000);
		myBuilding.setAdditionalCoveragesStuff(additionalCoveragesStuff);
		
		
		PolicyLocationBuilding myBuilding1 = new PolicyLocationBuilding();
		myBuilding1.setRoofCondition(RoofCondition.HasSomeWearAndTear);	
		PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff1 = new PolicyLocationBuildingAdditionalCoverages();
		additionalCoveragesStuff1.setDiscretionaryPayrollExpenseCoverage(true);
		additionalCoveragesStuff1.setDiscretionaryPayrollExpenseAmount(120000);
		myBuilding1.setAdditionalCoveragesStuff(additionalCoveragesStuff1);
		
		PolicyLocationBuilding myBuilding2 = new PolicyLocationBuilding();
		myBuilding2.setRoofCondition(RoofCondition.Other);
		PolicyLocationBuildingAdditionalCoverages additionalCoveragesStuff2 = new PolicyLocationBuildingAdditionalCoverages();
		additionalCoveragesStuff2.setBusinessIncomeOrdinaryPayrollCoverage(true);
		additionalCoveragesStuff2.setBusinessIncomeOrdinaryPayrollType(BusinessIncomeOrdinaryPayroll.Days360);
		myBuilding2.setAdditionalCoveragesStuff(additionalCoveragesStuff2);
		
		
		// END BUILDING
		locOneBuildingList.add(myBuilding);
		locOneBuildingList.add(myBuilding1);
		locOneBuildingList.add(myBuilding2);
		location1.setBuildingList(locOneBuildingList);
		locationsList.add(location1);
		PolicyBusinessownersLine boLine = new PolicyBusinessownersLine(SmallBusinessType.Apartments) {
			{
				this.setLiabilityLimits(LiabilityLimits.One000_2000_2000);
				this.locationList = locationsList;
				this.setAdditionalCoverageStuff(new PolicyBusinessownersLineAdditionalCoverages(false, true) {
					{
						// Businessowners Line On the Employee Dishonesty  Coverages
						this.setEmployeeDishonestyCoverage(true);
						this.setEmpDisLimit(EmpDishonestyLimit.Dishonest25000);
						this.setEmpDisNumCoveredEmployees(20);
						this.setEmpDisReferencesChecked(true);
						this.setEmpDisHowOftenAudits(EmployeeDishonestyAuditsConductedFrequency.Monthly);
						this.setEmpDisAuditsPerformedBy(EmployeeDishonestyAuditsPerformedBy.CPA);
						this.setEmpDisDiffWriteThanReconcile(true);
						this.setEmpDisLargeCheckProcedures(true);
						// Businessowners Line -- Additional Coverages If the Electrnoic Data Limit is over $50,000.
						this.setElectronicDataCoverage(true);
						this.setElectronicDataLimit(60000);
						// Busiessowmers Line -- Additional Coverages If endorsement Insurance to Value is selected.
						this.setInsuranceToValueCoverage(true);
						// YES to "Is Applicant in the business of selling or charging for liquor?" and BP 04 89 is not selected.
						this.setSellingOrChargingLiquor(true);
						this.setHiredAutoCoverage(false);
						this.setHiredAutoOwnedAuto(false);
						this.setNonOwnedAutoLiabilityCoverage(true);
						this.setNonOwnedAutoNonCompanyVehicle(true);
					}
				}); // END ADDITIONAL COVERAGES
			}
		}; // END BUSINESS OWNERS LINE

		 Date newEff = DateUtils.dateAddSubtract(DateUtils.getCenterDate(cf, ApplicationOrCenter.PolicyCenter), DateAddSubtractOptions.Day, -11);

		myQQPolObj = new GeneratePolicy.Builder(driver).withInsPersonOrCompany(ContactSubType.Company)
				.withPolOrgType(OrganizationType.Partnership).withProductType(ProductLineType.Businessowners).withPolEffectiveDate(newEff)
				.withPolicyLocations(locationsList).withBusinessownersLine(boLine)
				.withCreateNew(CreateNew.Create_New_Always).withInsCompanyName("BOPQQUW")
				.withLineSelection(LineSelection.Businessowners).isDraft().build(GeneratePolicyType.QuickQuote);


        new Login(driver).loginAndSearchSubmission(myQQPolObj);
		SideMenuPC sideMenu = new SideMenuPC(driver);
		sideMenu.clickSideMenuBusinessownersLine();
		// Businessowners "Where does the Insured perform the activities described above?"
		// Businessowners Line Special Wording requires UW approval Special
		// select YES to any type under "Does the Certificate Holder or Additional Insured operate in any of the following industries?"
		GenericWorkorderBusinessownersLineIncludedCoverages pcBusinessOwnersLineIncludedCoveragePage = new GenericWorkorderBusinessownersLineIncludedCoverages(driver);
		PolicyBusinessownersLineAdditionalInsured policyBusinessOwnersLineAdditionalInsured = new PolicyBusinessownersLineAdditionalInsured();
		policyBusinessOwnersLineAdditionalInsured.setAiRole(AdditionalInsuredRole.CertificateHolderOnly);
		policyBusinessOwnersLineAdditionalInsured.setNewContact(CreateNew.Create_New_Always);
		policyBusinessOwnersLineAdditionalInsured.setSpecialWording(true);
		policyBusinessOwnersLineAdditionalInsured.setSpecialWordingDesc("Testing Purpose Added");	
		pcBusinessOwnersLineIncludedCoveragePage.addAdditionalInsureds(true, policyBusinessOwnersLineAdditionalInsured);
		pcBusinessOwnersLineIncludedCoveragePage.clickAdditionalInsuredByRow(1);
		GenericWorkorderBusinessownersLineAdditionalInsured aiInsured = new GenericWorkorderBusinessownersLineAdditionalInsured(driver);
		aiInsured.setEditAdditionalInsuredBOLineIndustryAll(true);
		aiInsured.setEditAdditionalInsuredBOLineWherePerformActivities(State.California, true);
		aiInsured.setEditAdditionalInsuredBOLineWherePerformActivities(State.Idaho, true);
		aiInsured.clickEditAdditionalInsuredBOLineOK();
		
		
		sideMenu.clickSideMenuBuildings();
		GenericWorkorderBuildings buildings = new GenericWorkorderBuildings(driver);         
		 for (PolicyLocationBuilding bldg : myQQPolObj.busOwnLine.locationList.get(0).getBuildingList()) {
			 buildings.clickBuildingsBuildingEdit(bldg.getNumber());
			 buildings.clickBuildingDetails();
			 buildings.setRoofCondition(bldg.getRoofCondition());
			 buildings.setBuildingValuationMethod(ValuationMethod.PlActualCashValue.getValue());
			 buildings.setBuildingCauseOfLoss(bldg.getBuildingCauseOfLoss().getValue());
			 buildings.setBuildingAdditionalCoverages(bldg);
			 buildings.clickOK();
	         new GenericWorkorder(driver).clickGenericWorkorderSaveDraft();// to mainly run error handling if needed
	     }
		 
		 sideMenu.clickSideMenuRiskAnalysis();
		 GenericWorkorderRiskAnalysis risk = new GenericWorkorderRiskAnalysis(driver);
		 risk.Quote();
		 boolean testPassed = true;
		 String errorMessage = "";
		 GenericWorkorderQuote quote = new GenericWorkorderQuote(driver);
		if (quote.isPreQuoteDisplayed()) {
			testPassed = false;
			errorMessage = errorMessage + "Unexpected Pre-Quote UW Issues message in QQ. \n";
			quote.clickPreQuoteDetails();
		}

		sideMenu.clickSideMenuRiskAnalysis();
		FullUnderWriterIssues allUWIssues = risk.getUnderwriterIssues();
		if (allUWIssues.getBlockQuoteList().size() > 0 || allUWIssues.getBlockSubmitList().size() > 0) {
			testPassed = false;
			errorMessage = errorMessage + "Unexpected Block Quote, Block Submit UW Issues displayed in QQ. \n";
		}

		Assert.assertTrue(testPassed, errorMessage);
			

	}

}
