package repository.gw.generate.custom;

import repository.gw.enums.Building;
import repository.gw.enums.SpoilageLimit;

public class PolicyLocationBuildingAdditionalCoverages {

	private boolean businessIncomeOrdinaryPayrollCoverage = false;
	private Building.BusinessIncomeOrdinaryPayroll businessIncomeOrdinaryPayrollType = Building.BusinessIncomeOrdinaryPayroll.Days60;
	private boolean discretionaryPayrollExpenseCoverage = false;
	private Building.DiscretionaryPayrollExpense discretionaryPayrollExpenseType = Building.DiscretionaryPayrollExpense.Days90;
	private long discretionaryPayrollExpenseAmount = 10000;
	private String discretionaryPayrollExpenseJobClassOrNameEmployees = "Retail";
	private boolean lossOfRentalValueLandlordAsDesignatedPayeeCoverage = false;
	private long lossOfRentalValueLandlordAsDesignatedPayeeLimit = 5000;
	private String lossOfRentalValueLandlordAsDesignatedPayeePersonOrgName = "Joe Schmoe";
	private String lossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress = "234 S. 17th Ave";
	private String lossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip = "Pocatello, ID 83201";
	private boolean accountsReceivableOptionalCoverage = false;
	private long accountsReceivableOptionalOnPremisesLimit = 10000;
	private boolean condoCommercialUnitOwnersOptionalCoverage = false;
	private Building.CondoCommercialUnitOwnersLimit condoCommercialUnitOwnersOptionalLossAssessmentLimit = Building.CondoCommercialUnitOwnersLimit.Limit5000;
	private long condoCommercialUnitOwnersOptionalMiscRealProperty = 5000;
	private boolean foodContaminationCoverage = false;
	private long foodContaminationLimit = 10000;
	private long foodContaminationAdvLimit = 3000;
	private boolean ordinanceOrLawCoverage = false;
	private boolean ordinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage = false;
	private Building.CoverageOrNot ordinanceOrLawLossToUndamagedPartType = Building.CoverageOrNot.Covered;
	private long ordinanceOrLawDemoCostLimit = 0;
	private long ordinanceOrLawIncrCostConstructionLimit = 0;
	private long ordinanceOrLawDemoAndIncrCostConstructionCombLimit = 0;
	private boolean outdoorPropertyCoverage = false;
	private long outdoorPropertyInsuranceAmount = 2500;
	private boolean spoilageCoverage = false;
	private String spoilageProductDescription = "Food and stuff";
	private repository.gw.enums.SpoilageLimit spoilageLimit = repository.gw.enums.SpoilageLimit.Fifteen000;
	private Building.SpoilageDeductible spoilageDeductible = Building.SpoilageDeductible.Ded500;
	private boolean spoilageBreakdownContamination = false;
	private boolean spoilagePowerOutage = false;
	private boolean spoilageRefrigerationMaintAgreement = false;
	private Building.SpoilageRiskClass spoilageRiskDescriptionClass = Building.SpoilageRiskClass.BakeryGoods28;
	private boolean valuablePapersOptionalCoverage = false;
	private long valuablePapersOptionalOnPremisesLimit = 10000;
	private boolean utilitiesDirectDamageCoverage = false;
	private Building.UtilitiesCoverageAppliesTo utilitiesDirectDamageCoverageAppliesTo = Building.UtilitiesCoverageAppliesTo.Building;
	private Building.UtilitiesUtilityIs utilitiesDirectDamageUtilityIs = Building.UtilitiesUtilityIs.Public;
	private long utilitiesDirectDamageDirectLossLimit = 5000;
	private Building.CoverageOrNot utilitiesDirectDamageWaterSupply = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesDirectDamageCommunicationsNotOHLines = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesDirectDamageCommunicationsIncOHLines = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesDirectDamagePowerNotOHLines = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesDirectDamagePowerIncOHLines = Building.CoverageOrNot.NotCovered;
	private boolean utilitiesTimeElementCoverage = false;
	private Building.UtilitiesCoverageAppliesTo utilitiesTimeElementCoverageAppliesTo = Building.UtilitiesCoverageAppliesTo.Building;
	private Building.UtilitiesUtilityIs utilitiesTimeElementUtilityIs = Building.UtilitiesUtilityIs.Public;
	private long utilitiesTimeElementDirectLossLimit = 5000;
	private Building.CoverageOrNot utilitiesTimeElementWaterSupply = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesTimeElementCommunicationsNotOHLines = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesTimeElementCommunicationsIncOHLines = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesTimeElementPowerNotOHLines = Building.CoverageOrNot.NotCovered;
	private Building.CoverageOrNot utilitiesTimeElementPowerIncOHLines = Building.CoverageOrNot.NotCovered;
	
	private boolean equipmentBreakDown_IDBP_31_1002 = true;

	public PolicyLocationBuildingAdditionalCoverages() {

	}

	public boolean isBusinessIncomeOrdinaryPayrollCoverage() {
		return businessIncomeOrdinaryPayrollCoverage;
	}

	public void setBusinessIncomeOrdinaryPayrollCoverage(boolean businessIncomeOrdinaryPayrollCoverage) {
		this.businessIncomeOrdinaryPayrollCoverage = businessIncomeOrdinaryPayrollCoverage;
	}

	public Building.BusinessIncomeOrdinaryPayroll getBusinessIncomeOrdinaryPayrollType() {
		return businessIncomeOrdinaryPayrollType;
	}

	public void setBusinessIncomeOrdinaryPayrollType(Building.BusinessIncomeOrdinaryPayroll businessIncomeOrdinaryPayrollType) {
		this.businessIncomeOrdinaryPayrollType = businessIncomeOrdinaryPayrollType;
	}

	public boolean isDiscretionaryPayrollExpenseCoverage() {
		return discretionaryPayrollExpenseCoverage;
	}

	public void setDiscretionaryPayrollExpenseCoverage(boolean discretionaryPayrollExpenseCoverage) {
		this.discretionaryPayrollExpenseCoverage = discretionaryPayrollExpenseCoverage;
	}

	public Building.DiscretionaryPayrollExpense getDiscretionaryPayrollExpenseType() {
		return discretionaryPayrollExpenseType;
	}

	public void setDiscretionaryPayrollExpenseType(Building.DiscretionaryPayrollExpense discretionaryPayrollExpenseType) {
		this.discretionaryPayrollExpenseType = discretionaryPayrollExpenseType;
	}

	public long getDiscretionaryPayrollExpenseAmount() {
		return discretionaryPayrollExpenseAmount;
	}

	public void setDiscretionaryPayrollExpenseAmount(long discretionaryPayrollExpenseAmount) {
		this.discretionaryPayrollExpenseAmount = discretionaryPayrollExpenseAmount;
	}

	public String getDiscretionaryPayrollExpenseJobClassOrNameEmployees() {
		return discretionaryPayrollExpenseJobClassOrNameEmployees;
	}

	public void setDiscretionaryPayrollExpenseJobClassOrNameEmployees(
			String discretionaryPayrollExpenseJobClassOrNameEmployees) {
		this.discretionaryPayrollExpenseJobClassOrNameEmployees = discretionaryPayrollExpenseJobClassOrNameEmployees;
	}

	public boolean isLossOfRentalValueLandlordAsDesignatedPayeeCoverage() {
		return lossOfRentalValueLandlordAsDesignatedPayeeCoverage;
	}

	public void setLossOfRentalValueLandlordAsDesignatedPayeeCoverage(
			boolean lossOfRentalValueLandlordAsDesignatedPayeeCoverage) {
		this.lossOfRentalValueLandlordAsDesignatedPayeeCoverage = lossOfRentalValueLandlordAsDesignatedPayeeCoverage;
	}

	public long getLossOfRentalValueLandlordAsDesignatedPayeeLimit() {
		return lossOfRentalValueLandlordAsDesignatedPayeeLimit;
	}

	public void setLossOfRentalValueLandlordAsDesignatedPayeeLimit(
			long lossOfRentalValueLandlordAsDesignatedPayeeLimit) {
		this.lossOfRentalValueLandlordAsDesignatedPayeeLimit = lossOfRentalValueLandlordAsDesignatedPayeeLimit;
	}

	public String getLossOfRentalValueLandlordAsDesignatedPayeePersonOrgName() {
		return lossOfRentalValueLandlordAsDesignatedPayeePersonOrgName;
	}

	public void setLossOfRentalValueLandlordAsDesignatedPayeePersonOrgName(
			String lossOfRentalValueLandlordAsDesignatedPayeePersonOrgName) {
		this.lossOfRentalValueLandlordAsDesignatedPayeePersonOrgName = lossOfRentalValueLandlordAsDesignatedPayeePersonOrgName;
	}

	public String getLossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress() {
		return lossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress;
	}

	public void setLossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress(
			String lossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress) {
		this.lossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress = lossOfRentalValueLandlordAsDesignatedPayeePersonOrgAddress;
	}

	public String getLossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip() {
		return lossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip;
	}

	public void setLossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip(
			String lossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip) {
		this.lossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip = lossOfRentalValueLandlordAsDesignatedPayeePersonOrgCityStZip;
	}

	public boolean isAccountsReceivableOptionalCoverage() {
		return accountsReceivableOptionalCoverage;
	}

	public void setAccountsReceivableOptionalCoverage(boolean accountsReceivableOptionalCoverage) {
		this.accountsReceivableOptionalCoverage = accountsReceivableOptionalCoverage;
	}

	public long getAccountsReceivableOptionalOnPremisesLimit() {
		return accountsReceivableOptionalOnPremisesLimit;
	}

	public void setAccountsReceivableOptionalOnPremisesLimit(long accountsReceivableOptionalOnPremisesLimit) {
		this.accountsReceivableOptionalOnPremisesLimit = accountsReceivableOptionalOnPremisesLimit;
	}

	public boolean isCondoCommercialUnitOwnersOptionalCoverage() {
		return condoCommercialUnitOwnersOptionalCoverage;
	}

	public void setCondoCommercialUnitOwnersOptionalCoverage(boolean condoCommercialUnitOwnersOptionalCoverage) {
		this.condoCommercialUnitOwnersOptionalCoverage = condoCommercialUnitOwnersOptionalCoverage;
	}

	public Building.CondoCommercialUnitOwnersLimit getCondoCommercialUnitOwnersOptionalLossAssessmentLimit() {
		return condoCommercialUnitOwnersOptionalLossAssessmentLimit;
	}

	public void setCondoCommercialUnitOwnersOptionalLossAssessmentLimit(
			Building.CondoCommercialUnitOwnersLimit condoCommercialUnitOwnersOptionalLossAssessmentLimit) {
		this.condoCommercialUnitOwnersOptionalLossAssessmentLimit = condoCommercialUnitOwnersOptionalLossAssessmentLimit;
	}

	public long getCondoCommercialUnitOwnersOptionalMiscRealProperty() {
		return condoCommercialUnitOwnersOptionalMiscRealProperty;
	}

	public void setCondoCommercialUnitOwnersOptionalMiscRealProperty(
			long condoCommercialUnitOwnersOptionalMiscRealProperty) {
		this.condoCommercialUnitOwnersOptionalMiscRealProperty = condoCommercialUnitOwnersOptionalMiscRealProperty;
	}

	public boolean isFoodContaminationCoverage() {
		return foodContaminationCoverage;
	}

	public void setFoodContaminationCoverage(boolean foodContaminationCoverage) {
		this.foodContaminationCoverage = foodContaminationCoverage;
	}

	public long getFoodContaminationLimit() {
		return foodContaminationLimit;
	}

	public void setFoodContaminationLimit(long foodContaminationLimit) {
		this.foodContaminationLimit = foodContaminationLimit;
	}

	public long getFoodContaminationAdvLimit() {
		return foodContaminationAdvLimit;
	}

	public void setFoodContaminationAdvLimit(long foodContaminationAdvLimit) {
		this.foodContaminationAdvLimit = foodContaminationAdvLimit;
	}

	public boolean isOrdinanceOrLawCoverage() {
		return ordinanceOrLawCoverage;
	}

	public void setOrdinanceOrLawCoverage(boolean ordinanceOrLawCoverage) {
		this.ordinanceOrLawCoverage = ordinanceOrLawCoverage;
	}

	public boolean isOrdinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage() {
		return ordinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage;
	}

	public void setOrdinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage(
			boolean ordinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage) {
		this.ordinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage = ordinanceOrLawBusinessIncomeExtraExpenseOptionalCoverage;
	}

	public Building.CoverageOrNot getOrdinanceOrLawLossToUndamagedPartType() {
		return ordinanceOrLawLossToUndamagedPartType;
	}

	public void setOrdinanceOrLawLossToUndamagedPartType(Building.CoverageOrNot ordinanceOrLawLossToUndamagedPartType) {
		this.ordinanceOrLawLossToUndamagedPartType = ordinanceOrLawLossToUndamagedPartType;
	}

	public long getOrdinanceOrLawDemoCostLimit() {
		return ordinanceOrLawDemoCostLimit;
	}

	public void setOrdinanceOrLawDemoCostLimit(long ordinanceOrLawDemoCostLimit) {
		this.ordinanceOrLawDemoCostLimit = ordinanceOrLawDemoCostLimit;
	}

	public long getOrdinanceOrLawIncrCostConstructionLimit() {
		return ordinanceOrLawIncrCostConstructionLimit;
	}

	public void setOrdinanceOrLawIncrCostConstructionLimit(long ordinanceOrLawIncrCostConstructionLimit) {
		this.ordinanceOrLawIncrCostConstructionLimit = ordinanceOrLawIncrCostConstructionLimit;
	}

	public long getOrdinanceOrLawDemoAndIncrCostConstructionCombLimit() {
		return ordinanceOrLawDemoAndIncrCostConstructionCombLimit;
	}

	public void setOrdinanceOrLawDemoAndIncrCostConstructionCombLimit(
			long ordinanceOrLawDemoAndIncrCostConstructionCombLimit) {
		this.ordinanceOrLawDemoAndIncrCostConstructionCombLimit = ordinanceOrLawDemoAndIncrCostConstructionCombLimit;
	}

	public boolean isOutdoorPropertyCoverage() {
		return outdoorPropertyCoverage;
	}

	public void setOutdoorPropertyCoverage(boolean outdoorPropertyCoverage) {
		this.outdoorPropertyCoverage = outdoorPropertyCoverage;
	}

	public long getOutdoorPropertyInsuranceAmount() {
		return outdoorPropertyInsuranceAmount;
	}

	public void setOutdoorPropertyInsuranceAmount(long outdoorPropertyInsuranceAmount) {
		this.outdoorPropertyInsuranceAmount = outdoorPropertyInsuranceAmount;
	}

	public boolean isSpoilageCoverage() {
		return spoilageCoverage;
	}

	public void setSpoilageCoverage(boolean spoilageCoverage) {
		this.spoilageCoverage = spoilageCoverage;
	}

	public String getSpoilageProductDescription() {
		return spoilageProductDescription;
	}

	public void setSpoilageProductDescription(String spoilageProductDescription) {
		this.spoilageProductDescription = spoilageProductDescription;
	}

	public repository.gw.enums.SpoilageLimit getSpoilageLimit() {
		return spoilageLimit;
	}

	public void setSpoilageLimit(SpoilageLimit spoilageLimit) {
		this.spoilageLimit = spoilageLimit;
	}

	public Building.SpoilageDeductible getSpoilageDeductible() {
		return spoilageDeductible;
	}

	public void setSpoilageDeductible(Building.SpoilageDeductible spoilageDeductible) {
		this.spoilageDeductible = spoilageDeductible;
	}

	public boolean isSpoilageBreakdownContamination() {
		return spoilageBreakdownContamination;
	}

	public void setSpoilageBreakdownContamination(boolean spoilageBreakdownContamination) {
		this.spoilageBreakdownContamination = spoilageBreakdownContamination;
	}

	public boolean isSpoilagePowerOutage() {
		return spoilagePowerOutage;
	}

	public void setSpoilagePowerOutage(boolean spoilagePowerOutage) {
		this.spoilagePowerOutage = spoilagePowerOutage;
	}

	public boolean isSpoilageRefrigerationMaintAgreement() {
		return spoilageRefrigerationMaintAgreement;
	}

	public void setSpoilageRefrigerationMaintAgreement(boolean spoilageRefrigerationMaintAgreement) {
		this.spoilageRefrigerationMaintAgreement = spoilageRefrigerationMaintAgreement;
	}

	public Building.SpoilageRiskClass getSpoilageRiskDescriptionClass() {
		return spoilageRiskDescriptionClass;
	}

	public void setSpoilageRiskDescriptionClass(Building.SpoilageRiskClass spoilageRiskDescriptionClass) {
		this.spoilageRiskDescriptionClass = spoilageRiskDescriptionClass;
	}

	public boolean isValuablePapersOptionalCoverage() {
		return valuablePapersOptionalCoverage;
	}

	public void setValuablePapersOptionalCoverage(boolean valuablePapersOptionalCoverage) {
		this.valuablePapersOptionalCoverage = valuablePapersOptionalCoverage;
	}

	public long getValuablePapersOptionalOnPremisesLimit() {
		return valuablePapersOptionalOnPremisesLimit;
	}

	public void setValuablePapersOptionalOnPremisesLimit(long valuablePapersOptionalOnPremisesLimit) {
		this.valuablePapersOptionalOnPremisesLimit = valuablePapersOptionalOnPremisesLimit;
	}

	public boolean isUtilitiesDirectDamageCoverage() {
		return utilitiesDirectDamageCoverage;
	}

	public void setUtilitiesDirectDamageCoverage(boolean utilitiesDirectDamageCoverage) {
		this.utilitiesDirectDamageCoverage = utilitiesDirectDamageCoverage;
	}

	public Building.UtilitiesCoverageAppliesTo getUtilitiesDirectDamageCoverageAppliesTo() {
		return utilitiesDirectDamageCoverageAppliesTo;
	}

	public void setUtilitiesDirectDamageCoverageAppliesTo(
			Building.UtilitiesCoverageAppliesTo utilitiesDirectDamageCoverageAppliesTo) {
		this.utilitiesDirectDamageCoverageAppliesTo = utilitiesDirectDamageCoverageAppliesTo;
	}

	public Building.UtilitiesUtilityIs getUtilitiesDirectDamageUtilityIs() {
		return utilitiesDirectDamageUtilityIs;
	}

	public void setUtilitiesDirectDamageUtilityIs(Building.UtilitiesUtilityIs utilitiesDirectDamageUtilityIs) {
		this.utilitiesDirectDamageUtilityIs = utilitiesDirectDamageUtilityIs;
	}

	public long getUtilitiesDirectDamageDirectLossLimit() {
		return utilitiesDirectDamageDirectLossLimit;
	}

	public void setUtilitiesDirectDamageDirectLossLimit(long utilitiesDirectDamageDirectLossLimit) {
		this.utilitiesDirectDamageDirectLossLimit = utilitiesDirectDamageDirectLossLimit;
	}

	public Building.CoverageOrNot getUtilitiesDirectDamageWaterSupply() {
		return utilitiesDirectDamageWaterSupply;
	}

	public void setUtilitiesDirectDamageWaterSupply(Building.CoverageOrNot utilitiesDirectDamageWaterSupply) {
		this.utilitiesDirectDamageWaterSupply = utilitiesDirectDamageWaterSupply;
	}

	public Building.CoverageOrNot getUtilitiesDirectDamageCommunicationsNotOHLines() {
		return utilitiesDirectDamageCommunicationsNotOHLines;
	}

	public void setUtilitiesDirectDamageCommunicationsNotOHLines(
			Building.CoverageOrNot utilitiesDirectDamageCommunicationsNotOHLines) {
		this.utilitiesDirectDamageCommunicationsNotOHLines = utilitiesDirectDamageCommunicationsNotOHLines;
	}

	public Building.CoverageOrNot getUtilitiesDirectDamageCommunicationsIncOHLines() {
		return utilitiesDirectDamageCommunicationsIncOHLines;
	}

	public void setUtilitiesDirectDamageCommunicationsIncOHLines(
			Building.CoverageOrNot utilitiesDirectDamageCommunicationsIncOHLines) {
		this.utilitiesDirectDamageCommunicationsIncOHLines = utilitiesDirectDamageCommunicationsIncOHLines;
	}

	public Building.CoverageOrNot getUtilitiesDirectDamagePowerNotOHLines() {
		return utilitiesDirectDamagePowerNotOHLines;
	}

	public void setUtilitiesDirectDamagePowerNotOHLines(Building.CoverageOrNot utilitiesDirectDamagePowerNotOHLines) {
		this.utilitiesDirectDamagePowerNotOHLines = utilitiesDirectDamagePowerNotOHLines;
	}

	public Building.CoverageOrNot getUtilitiesDirectDamagePowerIncOHLines() {
		return utilitiesDirectDamagePowerIncOHLines;
	}

	public void setUtilitiesDirectDamagePowerIncOHLines(Building.CoverageOrNot utilitiesDirectDamagePowerIncOHLines) {
		this.utilitiesDirectDamagePowerIncOHLines = utilitiesDirectDamagePowerIncOHLines;
	}

	public boolean isUtilitiesTimeElementCoverage() {
		return utilitiesTimeElementCoverage;
	}

	public void setUtilitiesTimeElementCoverage(boolean utilitiesTimeElementCoverage) {
		this.utilitiesTimeElementCoverage = utilitiesTimeElementCoverage;
	}

	public Building.UtilitiesCoverageAppliesTo getUtilitiesTimeElementCoverageAppliesTo() {
		return utilitiesTimeElementCoverageAppliesTo;
	}

	public void setUtilitiesTimeElementCoverageAppliesTo(
			Building.UtilitiesCoverageAppliesTo utilitiesTimeElementCoverageAppliesTo) {
		this.utilitiesTimeElementCoverageAppliesTo = utilitiesTimeElementCoverageAppliesTo;
	}

	public Building.UtilitiesUtilityIs getUtilitiesTimeElementUtilityIs() {
		return utilitiesTimeElementUtilityIs;
	}

	public void setUtilitiesTimeElementUtilityIs(Building.UtilitiesUtilityIs utilitiesTimeElementUtilityIs) {
		this.utilitiesTimeElementUtilityIs = utilitiesTimeElementUtilityIs;
	}

	public long getUtilitiesTimeElementDirectLossLimit() {
		return utilitiesTimeElementDirectLossLimit;
	}

	public void setUtilitiesTimeElementDirectLossLimit(long utilitiesTimeElementDirectLossLimit) {
		this.utilitiesTimeElementDirectLossLimit = utilitiesTimeElementDirectLossLimit;
	}

	public Building.CoverageOrNot getUtilitiesTimeElementWaterSupply() {
		return utilitiesTimeElementWaterSupply;
	}

	public void setUtilitiesTimeElementWaterSupply(Building.CoverageOrNot utilitiesTimeElementWaterSupply) {
		this.utilitiesTimeElementWaterSupply = utilitiesTimeElementWaterSupply;
	}

	public Building.CoverageOrNot getUtilitiesTimeElementCommunicationsNotOHLines() {
		return utilitiesTimeElementCommunicationsNotOHLines;
	}

	public void setUtilitiesTimeElementCommunicationsNotOHLines(
			Building.CoverageOrNot utilitiesTimeElementCommunicationsNotOHLines) {
		this.utilitiesTimeElementCommunicationsNotOHLines = utilitiesTimeElementCommunicationsNotOHLines;
	}

	public Building.CoverageOrNot getUtilitiesTimeElementCommunicationsIncOHLines() {
		return utilitiesTimeElementCommunicationsIncOHLines;
	}

	public void setUtilitiesTimeElementCommunicationsIncOHLines(
			Building.CoverageOrNot utilitiesTimeElementCommunicationsIncOHLines) {
		this.utilitiesTimeElementCommunicationsIncOHLines = utilitiesTimeElementCommunicationsIncOHLines;
	}

	public Building.CoverageOrNot getUtilitiesTimeElementPowerNotOHLines() {
		return utilitiesTimeElementPowerNotOHLines;
	}

	public void setUtilitiesTimeElementPowerNotOHLines(Building.CoverageOrNot utilitiesTimeElementPowerNotOHLines) {
		this.utilitiesTimeElementPowerNotOHLines = utilitiesTimeElementPowerNotOHLines;
	}

	public Building.CoverageOrNot getUtilitiesTimeElementPowerIncOHLines() {
		return utilitiesTimeElementPowerIncOHLines;
	}

	public void setUtilitiesTimeElementPowerIncOHLines(Building.CoverageOrNot utilitiesTimeElementPowerIncOHLines) {
		this.utilitiesTimeElementPowerIncOHLines = utilitiesTimeElementPowerIncOHLines;
	}

	public boolean getEquipmentBreakDown_IDBP_31_1002() {
		return equipmentBreakDown_IDBP_31_1002;
	}

	public void setEquipmentBreakDown_IDBP_31_1002(boolean equipmentBreakDown_IDBP_31_1002) {
		this.equipmentBreakDown_IDBP_31_1002 = equipmentBreakDown_IDBP_31_1002;
	}

}
