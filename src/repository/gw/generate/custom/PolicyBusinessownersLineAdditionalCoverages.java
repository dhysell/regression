package repository.gw.generate.custom;

import org.testng.Assert;
import repository.gw.enums.BusinessownersLine;
import repository.gw.enums.EmploymentPracticesLiabilityInsuranceIfApplicantIsA;
import repository.gw.enums.GeneralLiability;


public class PolicyBusinessownersLineAdditionalCoverages {

    private boolean employeeDishonestyCoverage = false;
    private BusinessownersLine.EmpDishonestyLimit empDisLimit = BusinessownersLine.EmpDishonestyLimit.Dishonest10000;
    private int empDisNumCoveredEmployees = 2;
    private boolean empDisReferencesChecked = true;
    private BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency empDisHowOftenAudits = BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency.Quarterly;
    private BusinessownersLine.EmployeeDishonestyAuditsPerformedBy empDisAuditsPerformedBy = BusinessownersLine.EmployeeDishonestyAuditsPerformedBy.CPA;
    private boolean empDisDiffWriteThanReconcile = true;
    private boolean empDisLargeCheckProcedures = true;
    private boolean hiredAutoCoverage = false;
    private boolean hiredAutoOwnedAuto = true;
    private boolean nonOwnedAutoLiabilityCoverage = false;
    private boolean nonOwnedAutoNonCompanyVehicle = false;
    private boolean sellingOrChargingLiquor = false;
    private boolean liquorLiabilityCoverageBP0488 = false;
    private boolean liquorLiabilityCoverageBP0489 = false;
    private BusinessownersLine.LiquorSalesType liquorSalesType = BusinessownersLine.LiquorSalesType.PackageSalesConsumedOffPremises;
    private long grossLiquorSales = 2000;
    private boolean beautySalonBarberShopCoverage = false;
    private BusinessownersLine.SalonTypeOfOperation beautySalonTypeOfOperation = BusinessownersLine.SalonTypeOfOperation.IndependentContractor;
    private int beautySalonNumOperators = 1;
    private int beautySalonNumStations = 1;
    private boolean electronicDataCoverage = false;
    private long electronicDataLimit = 10000;
    private boolean equipmentBreakdownCoverage = true;
    private boolean insuranceToValueCoverage = false;
    private boolean additionalInsuredEngineersArchitectsSurveyorsCoverage = false;

    public boolean employmentPracticesLiabilityInsurance_exists = true;
    private boolean employmentPracticesLiabilityInsurance = true;
    private int employmentPracticesLiabilityInsurance_Handrated = 0;
    private int employmentPracticesLiabilityInsurance_NumberOfFulltimeEmployees = 1;
    private int employmentPracticesLiabilityInsurance_NumberOfPartTimeEmployees = 0;
    private GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit employmentPracticesLiabilityInsurance_AggregateLimit = GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit.OneHundredThousand;
    private GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible employmentPracticesLiabilityInsurance_deductible = GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible.FiveThousand;
    private String employmentPracticesLiabilityInsurance_RetroactiveDate = null;
    private boolean employmentPracticesLiabilityInsurance_ThirdPartyViolations = false;
    private int employmentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated = 0;
    private GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations employmentPracticesLiabilityInsurance_NumberOfLocations = GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations.ONE;
    private boolean employmentPracticesLiabilityInsuranceDoesApplicantHaveEmployees = false;
    private EmploymentPracticesLiabilityInsuranceIfApplicantIsA employmentPracticesLiabilityInsuranceIfApplicantIsA = EmploymentPracticesLiabilityInsuranceIfApplicantIsA.NONE_OF_THE_ABOVE;

    public boolean isEmploymentPracticesLiabilityInsurance() {
        return employmentPracticesLiabilityInsurance;
    }

    public void setEmploymentPracticesLiabilityInsurance(boolean employmentPracticesLiabilityInsurance) {
        this.employmentPracticesLiabilityInsurance = employmentPracticesLiabilityInsurance;
    }

    public int getEmploymentPracticesLiabilityInsurance_Handrated() {
        return employmentPracticesLiabilityInsurance_Handrated;
    }

    public void setEmploymentPracticesLiabilityInsurance_Handrated(int employmentPracticesLiabilityInsurance_Handrated) {
        this.employmentPracticesLiabilityInsurance_Handrated = employmentPracticesLiabilityInsurance_Handrated;
    }

    public int getEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees() {
        return employmentPracticesLiabilityInsurance_NumberOfFulltimeEmployees;
    }

    public void setEmploymentPracticesLiabilityInsurance_NumberOfFulltimeEmployees(
            int employmentPracticesLiabilityInsurance_NumberOfFulltimeEmployees) {
        this.employmentPracticesLiabilityInsurance_NumberOfFulltimeEmployees = employmentPracticesLiabilityInsurance_NumberOfFulltimeEmployees;
    }

    public int getEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees() {
        return employmentPracticesLiabilityInsurance_NumberOfPartTimeEmployees;
    }

    public void setEmploymentPracticesLiabilityInsurance_NumberOfPartTimeEmployees(
            int employmentPracticesLiabilityInsurance_NumberOfPartTimeEmployees) {
        this.employmentPracticesLiabilityInsurance_NumberOfPartTimeEmployees = employmentPracticesLiabilityInsurance_NumberOfPartTimeEmployees;
    }

    public GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit getEmploymentPracticesLiabilityInsurance_AggregateLimit() {
        return employmentPracticesLiabilityInsurance_AggregateLimit;
    }

    public void setEmploymentPracticesLiabilityInsurance_AggregateLimit(GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit employmentPracticesLiabilityInsurance_AggregateLimit) {
        if (employmentPracticesLiabilityInsurance_AggregateLimit.equals(GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_AggregateLimit.TenThousand)) {
            Assert.fail("BOP EPLI AGGREGATE LIMIT CANNOT SET TO TEN THOUSAND. SORRY");
        }
        this.employmentPracticesLiabilityInsurance_AggregateLimit = employmentPracticesLiabilityInsurance_AggregateLimit;
    }

    public GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible getEmploymentPracticesLiabilityInsurance_deductible() {
        return employmentPracticesLiabilityInsurance_deductible;
    }

    public void setEmploymentPracticesLiabilityInsurance_deductible(
            GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_Deductible employmentPracticesLiabilityInsurance_deductible) {
        this.employmentPracticesLiabilityInsurance_deductible = employmentPracticesLiabilityInsurance_deductible;
    }

    public String getEmploymentPracticesLiabilityInsurance_RetroactiveDate() {
        return employmentPracticesLiabilityInsurance_RetroactiveDate;
    }

    public void setEmploymentPracticesLiabilityInsurance_RetroactiveDate(
            String employmentPracticesLiabilityInsurance_RetroactiveDate) {
        this.employmentPracticesLiabilityInsurance_RetroactiveDate = employmentPracticesLiabilityInsurance_RetroactiveDate;
    }

    public boolean isEmploymentPracticesLiabilityInsurance_ThirdPartyViolations() {
        return employmentPracticesLiabilityInsurance_ThirdPartyViolations;
    }

    public void setEmploymentPracticesLiabilityInsurance_ThirdPartyViolations(
            boolean employmentPracticesLiabilityInsurance_ThirdPartyViolations) {
        this.employmentPracticesLiabilityInsurance_ThirdPartyViolations = employmentPracticesLiabilityInsurance_ThirdPartyViolations;
    }

    public GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations getEmploymentPracticesLiabilityInsurance_NumberOfLocations() {
        return employmentPracticesLiabilityInsurance_NumberOfLocations;
    }

    public void setEmploymentPracticesLiabilityInsurance_NumberOfLocations(
            GeneralLiability.AdditionalCoverages.EmploymentPracticesLiabilityInsurance_NumberLocations employmentPracticesLiabilityInsurance_NumberOfLocations) {
        this.employmentPracticesLiabilityInsurance_NumberOfLocations = employmentPracticesLiabilityInsurance_NumberOfLocations;
    }

    public PolicyBusinessownersLineAdditionalCoverages() {

    }

    public PolicyBusinessownersLineAdditionalCoverages(boolean sellingOrChargingLiquor,
                                                       boolean equipmentBreakdownCoverage) {
        this.sellingOrChargingLiquor = sellingOrChargingLiquor;
        this.setEquipmentBreakdownCoverage(equipmentBreakdownCoverage);
    }

    public boolean isEmployeeDishonestyCoverage() {
        return employeeDishonestyCoverage;
    }

    public void setEmployeeDishonestyCoverage(boolean employeeDishonestyCoverage) {
        this.employeeDishonestyCoverage = employeeDishonestyCoverage;
    }

    public BusinessownersLine.EmpDishonestyLimit getEmpDisLimit() {
        return empDisLimit;
    }

    public void setEmpDisLimit(BusinessownersLine.EmpDishonestyLimit empDisLimit) {
        this.empDisLimit = empDisLimit;
    }

    public int getEmpDisNumCoveredEmployees() {
        return empDisNumCoveredEmployees;
    }

    public void setEmpDisNumCoveredEmployees(int empDisNumCoveredEmployees) {
        this.empDisNumCoveredEmployees = empDisNumCoveredEmployees;
    }

    public boolean isEmpDisReferencesChecked() {
        return empDisReferencesChecked;
    }

    public void setEmpDisReferencesChecked(boolean empDisReferencesChecked) {
        this.empDisReferencesChecked = empDisReferencesChecked;
    }

    public BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency getEmpDisHowOftenAudits() {
        return empDisHowOftenAudits;
    }

    public void setEmpDisHowOftenAudits(BusinessownersLine.EmployeeDishonestyAuditsConductedFrequency empDisHowOftenAudits) {
        this.empDisHowOftenAudits = empDisHowOftenAudits;
    }

    public BusinessownersLine.EmployeeDishonestyAuditsPerformedBy getEmpDisAuditsPerformedBy() {
        return empDisAuditsPerformedBy;
    }

    public void setEmpDisAuditsPerformedBy(BusinessownersLine.EmployeeDishonestyAuditsPerformedBy empDisAuditsPerformedBy) {
        this.empDisAuditsPerformedBy = empDisAuditsPerformedBy;
    }

    public boolean isEmpDisDiffWriteThanReconcile() {
        return empDisDiffWriteThanReconcile;
    }

    public void setEmpDisDiffWriteThanReconcile(boolean empDisDiffWriteThanReconcile) {
        this.empDisDiffWriteThanReconcile = empDisDiffWriteThanReconcile;
    }

    public boolean isEmpDisLargeCheckProcedures() {
        return empDisLargeCheckProcedures;
    }

    public void setEmpDisLargeCheckProcedures(boolean empDisLargeCheckProcedures) {
        this.empDisLargeCheckProcedures = empDisLargeCheckProcedures;
    }

    public boolean isHiredAutoCoverage() {
        return hiredAutoCoverage;
    }

    public void setHiredAutoCoverage(boolean hiredAutoCoverage) {
        this.hiredAutoCoverage = hiredAutoCoverage;
    }

    public boolean isHiredAutoOwnedAuto() {
        return hiredAutoOwnedAuto;
    }

    public void setHiredAutoOwnedAuto(boolean hiredAutoOwnedAuto) {
        this.hiredAutoOwnedAuto = hiredAutoOwnedAuto;
    }

    public boolean isNonOwnedAutoLiabilityCoverage() {
        return nonOwnedAutoLiabilityCoverage;
    }

    public void setNonOwnedAutoLiabilityCoverage(boolean nonOwnedAutoLiabilityCoverage) {
        this.nonOwnedAutoLiabilityCoverage = nonOwnedAutoLiabilityCoverage;
    }

    public boolean isNonOwnedAutoNonCompanyVehicle() {
        return nonOwnedAutoNonCompanyVehicle;
    }

    public void setNonOwnedAutoNonCompanyVehicle(boolean nonOwnedAutoNonCompanyVehicle) {
        this.nonOwnedAutoNonCompanyVehicle = nonOwnedAutoNonCompanyVehicle;
    }

    public boolean isSellingOrChargingLiquor() {
        return sellingOrChargingLiquor;
    }

    public void setSellingOrChargingLiquor(boolean sellingOrChargingLiquor) {
        this.sellingOrChargingLiquor = sellingOrChargingLiquor;
    }

    public boolean isLiquorLiabilityCoverageBP0488() {
        return liquorLiabilityCoverageBP0488;
    }

    public void setLiquorLiabilityCoverageBP0488(boolean liquorLiabilityCoverageBP0488) {
        this.liquorLiabilityCoverageBP0488 = liquorLiabilityCoverageBP0488;
    }

    public boolean isLiquorLiabilityCoverageBP0489() {
        return liquorLiabilityCoverageBP0489;
    }

    public void setLiquorLiabilityCoverageBP0489(boolean liquorLiabilityCoverageBP0489) {
        this.liquorLiabilityCoverageBP0489 = liquorLiabilityCoverageBP0489;
    }

    public BusinessownersLine.LiquorSalesType getLiquorSalesType() {
        return liquorSalesType;
    }

    public void setLiquorSalesType(BusinessownersLine.LiquorSalesType liquorSalesType) {
        this.liquorSalesType = liquorSalesType;
    }

    public long getGrossLiquorSales() {
        return grossLiquorSales;
    }

    public void setGrossLiquorSales(long grossLiquorSales) {
        this.grossLiquorSales = grossLiquorSales;
    }

    public boolean isBeautySalonBarberShopCoverage() {
        return beautySalonBarberShopCoverage;
    }

    public void setBeautySalonBarberShopCoverage(boolean beautySalonBarberShopCoverage) {
        this.beautySalonBarberShopCoverage = beautySalonBarberShopCoverage;
    }

    public BusinessownersLine.SalonTypeOfOperation getBeautySalonTypeOfOperation() {
        return beautySalonTypeOfOperation;
    }

    public void setBeautySalonTypeOfOperation(BusinessownersLine.SalonTypeOfOperation beautySalonTypeOfOperation) {
        this.beautySalonTypeOfOperation = beautySalonTypeOfOperation;
    }

    public int getBeautySalonNumOperators() {
        return beautySalonNumOperators;
    }

    public void setBeautySalonNumOperators(int beautySalonNumOperators) {
        this.beautySalonNumOperators = beautySalonNumOperators;
    }

    public int getBeautySalonNumStations() {
        return beautySalonNumStations;
    }

    public void setBeautySalonNumStations(int beautySalonNumStations) {
        this.beautySalonNumStations = beautySalonNumStations;
    }

    public boolean isElectronicDataCoverage() {
        return electronicDataCoverage;
    }

    public void setElectronicDataCoverage(boolean electronicDataCoverage) {
        this.electronicDataCoverage = electronicDataCoverage;
    }

    public long getElectronicDataLimit() {
        return electronicDataLimit;
    }

    public void setElectronicDataLimit(long electronicDataLimit) {
        this.electronicDataLimit = electronicDataLimit;
    }

    public boolean isEquipmentBreakdownCoverage() {
        return equipmentBreakdownCoverage;
    }

    public void setEquipmentBreakdownCoverage(boolean equipmentBreakdownCoverage) {
        this.equipmentBreakdownCoverage = equipmentBreakdownCoverage;
    }

    public boolean isInsuranceToValueCoverage() {
        return insuranceToValueCoverage;
    }

    public void setInsuranceToValueCoverage(boolean insuranceToValueCoverage) {
        this.insuranceToValueCoverage = insuranceToValueCoverage;
    }

    public boolean isAdditionalInsuredEngineersArchitectsSurveyorsCoverage() {
        return additionalInsuredEngineersArchitectsSurveyorsCoverage;
    }

    public void setAdditionalInsuredEngineersArchitectsSurveyorsCoverage(
            boolean additionalInsuredEngineersArchitectsSurveyorsCoverage) {
        this.additionalInsuredEngineersArchitectsSurveyorsCoverage = additionalInsuredEngineersArchitectsSurveyorsCoverage;
    }

    public boolean isEmploymentPracticesLiabilityInsuranceDoesApplicantHaveEmployees() {
        return employmentPracticesLiabilityInsuranceDoesApplicantHaveEmployees;
    }

    public void setEmploymentPracticesLiabilityInsuranceDoesApplicantHaveEmployees(boolean employmentPracticesLiabilityInsuranceDoesApplicantHaveEmployees) {
        this.employmentPracticesLiabilityInsuranceDoesApplicantHaveEmployees = employmentPracticesLiabilityInsuranceDoesApplicantHaveEmployees;
    }

    public EmploymentPracticesLiabilityInsuranceIfApplicantIsA getEmploymentPracticesLiabilityInsuranceIfApplicantIsA() {
        return employmentPracticesLiabilityInsuranceIfApplicantIsA;
    }

    public void setEmploymentPracticesLiabilityInsuranceIfApplicantIsA(EmploymentPracticesLiabilityInsuranceIfApplicantIsA employmentPracticesLiabilityInsuranceIfApplicantIsA) {
        this.employmentPracticesLiabilityInsuranceIfApplicantIsA = employmentPracticesLiabilityInsuranceIfApplicantIsA;
    }

	public int getEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated() {
		return employmentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated;
	}

	public void setEmploymentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated(
			int employmentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated) {
		this.employmentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated = employmentPracticesLiabilityInsurance_ThirdPartyViolations_HandRated;
	}
}
