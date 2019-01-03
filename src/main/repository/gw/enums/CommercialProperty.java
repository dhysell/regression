package repository.gw.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CommercialProperty {

	//COVERAGES
	public enum PropertyCoverages {

		BuildingCoverage("Building Coverage"),
		BusinessPersonalPropertyCoverage("Business Personal Property Coverage"), 
		BuildersRiskCoverageForm_CP_00_20("Builders' Risk Coverage Form CP 00 20"), 
		PropertyInTheOpen("Property In The Open"),
		PersonalPropertyOfOthers("Personal Property Of Others"),
		LegalLiabilityCoverageForm_CP_00_40("Legal Liability Coverage Form CP 00 40"),
		CondominiumAssociationCoverageForm_CP_00_17("Condominium Association Coverage Form CP 00 17"),
		CondominiumCommercialUnit_OwnersCoverageForm_CP_00_18("Condominium Commercial Unit-Owners Coverage Form CP 00 18");

		String value;

		private PropertyCoverages(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		private static final List<PropertyCoverages> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
		public static PropertyCoverages random()  {
			return VALUES.get(new Random().nextInt(VALUES.size()));
		}
	}

	//BUILDING COVERAGE
	public enum BuildingCoverageCauseOfLoss {

		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private BuildingCoverageCauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BuildingCoverageDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;

		private BuildingCoverageDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}
	


	public enum BuildingCoverageValuationMethod {

		ACV("Actual Cash Value"),
		ReplacementCost("Replacement Cost"),
		AgreedValue("Agreed Value");

		String value;

		private BuildingCoverageValuationMethod(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BuildingCoverageCoinsurancePercent {

		NotApplicable("Not Applicable"),
		EightyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");


		String value;

		private BuildingCoverageCoinsurancePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}



	public enum BuildingCoverageAutoIncreasePercent {

		Decline("Decline"),
		TwoPercent("2%"),
		FourPercent("4%"),
		SixPercent("6%"),
		EightPercent("8%"),
		TenPercent("10%");

		String value;

		private BuildingCoverageAutoIncreasePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}





	//BUSINDESS PERSONAL PROPERTY COVERAGE
	public enum BusinessPersonalPropertyCoverageCauseOfLoss {

		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private BusinessPersonalPropertyCoverageCauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BusinessPersonalPropertyCoverageDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;

		private BusinessPersonalPropertyCoverageDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}


	public enum BusinessPersonalPropertyCoverageValuationMethod {

		ACV("Actual Cash Value"),
		ReplacementCost("Replacement Cost"),
		AgreedValue("Agreed Value");

		String value;

		private BusinessPersonalPropertyCoverageValuationMethod(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BusinessPersonalPropertyCoverageCoinsurancePercent {

		NotApplicable("Not Applicable"),
		EightyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");


		String value;

		private BusinessPersonalPropertyCoverageCoinsurancePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}



	public enum BusinessPersonalPropertyCoverageAutoIncreasePercent {

		Decline("Decline"),
		TwoPercent("2%"),
		FourPercent("4%"),
		SixPercent("6%"),
		EightPercent("8%"),
		TenPercent("10%");

		String value;

		private BusinessPersonalPropertyCoverageAutoIncreasePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BuildersRiskCoverageFormCP0020CauseOfLoss {
		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private BuildersRiskCoverageFormCP0020CauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BuildersRiskCoverageFormCP0020Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;

		private BuildersRiskCoverageFormCP0020Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum BuildersRiskCoverageFormCP0020ValuationMethod {
		ACV("Actual Cash Value");

		String value;

		private BuildersRiskCoverageFormCP0020ValuationMethod(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum BuildersRiskCoverageFormCP0020CoinsurancePercent {
		OneHundredPercent("100%");

		String value;

		private BuildersRiskCoverageFormCP0020CoinsurancePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}

	}


	public enum PropertyInTheOpenCauseOfLoss {
		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private PropertyInTheOpenCauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum PropertyInTheOpenDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;

		private PropertyInTheOpenDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum PropertyInTheOpenValuationMethod {

		ACV("Actual Cash Value"),
		ReplacementCost("Replacement Cost");

		String value;

		private PropertyInTheOpenValuationMethod(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum PropertyInTheOpenCoinsurancePercent {

		EightyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");

		String value;

		private PropertyInTheOpenCoinsurancePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}



	public enum PropertyInTheOpenAutoIncreasePercent {

		Decline("Decline"),
		TwoPercent("2%"),
		FourPercent("4%"),
		SixPercent("6%"),
		EightPercent("8%"),
		TenPercent("10%");

		String value;

		private PropertyInTheOpenAutoIncreasePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum PropertyOfOthersCauseOfLoss {
		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private PropertyOfOthersCauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum PropertyOfOthersDeductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;

		private PropertyOfOthersDeductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum PropertyOfOthersValuationMethod {

		ACV("Actual Cash Value");

		String value;

		private PropertyOfOthersValuationMethod(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum PropertyOfOthersCoinsurancePercent {

		EightyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");

		String value;

		private PropertyOfOthersCoinsurancePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum LegalLiabilityCoverageFormCP0040CauseOfLoss {
		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private LegalLiabilityCoverageFormCP0040CauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum LegalLiabilityCoverageFormCP0040AdditionalInsurableInterest {

		GeneralLesseesManagersOrOperatorsOfPremises("General lessees, managers or operators of premises."),
		EmployeesOtherThanExecutiveOfficersOrPartnersInPoliciesCoveringTheirEmployers("Employees other than executive officers or partners in policies covering their employers."),
		ContractorsorSub_ContractorsInPoliciesCoveringTenantsorLesseesOfPremises("Contractors or sub-contractors, in policies covering tenants or lessees of premises."),
		TenantsLesseesConcessionAiresorExhibitorsInPoliciesCoveringGeneralLesseesManagersOrOperatorsOfPremises("Tenants, lessees, concessionaires or exhibitors in policies covering general lessees, managers or operators of premises."),
		None("None");

		String value;

		private LegalLiabilityCoverageFormCP0040AdditionalInsurableInterest(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}







	public enum AdditionalCoverages {
		AdditionalBuildingProperty_CP_14_15("Additional Building Property CP 14 15"),
		AdditionalCoveredProperty_CP_14_10("Additional Covered Property CP 14 10"),
		AdditionalInsured_BuildingOwner_CP_12_19("Additional Insured � Building Owner CP 12 19"),
		BuildersRiskRenovations_CP_11_13("Builders� Risk Renovations CP 11 13"),
		BuildingGlass_TenantsPolicy_CP_14_70("Building Glass � Tenant�s Policy CP 14 70"),
		CondominiumCommercialUnit_OwnersOptionalCoverages_CP_04_18("Condominium Commercial Unit-Owners Optional Coverages CP 04 18"),
		DischargeFromSewerDrainOrSumpNotFlood_Related_CP_10_38("Discharge From Sewer, Drain Or Sump (Not Flood-Related) CP 10 38"),
		EquipmentBreakdownEnhancementEndorsementID_CP_31_1002("Equipment Breakdown Enhancement Endorsement IDCP 31 1002"),
		FunctionalBuildingValuation_CP_04_38("Functional Building Valuation CP 04 38"),
		LeasedProperty_CP_14_60("Leased Property CP 14 60"),
		LossPayableProvisions_CP_12_18("Loss Payable Provisions CP 12 18"),
		OrdinanceorLawCoverage_CP_04_05("Ordinance or Law Coverage CP 04 05"),
		OutdoorSigns_CP_14_40("Outdoor Signs CP 14 40"),
		PayrollLimitationOrExclusion_CP_15_10("Payroll Limitation Or Exclusion CP 15 10"),
		PeakSeasonLimitOfInsurance_CP_12_30("Peak Season Limit Of Insurance CP 12 30"),
		RadioOrTelevisionAntennas_CP_14_50("Radio Or Television Antennas CP 14 50"),
		SpoilageCoverage_CP_04_40("Spoilage Coverage CP 04 40"),
		TheftExclusion_CP_10_33("Theft Exclusion CP 10 33"),
		TheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRisk_CP_10_44("Theft Of Building Materials And Supplies (Other Than Builders Risk) CP 10 44"),
		UtilityServices_DirectDamage_CP_04_17("Utility Services � Direct Damage CP 04 17"),
		UtilityServices_TimeElements_CP_15_45("Utility Services � Time Elements CP 15 45"),
		BusinessIncomeCoverageForm("Business Income Coverage Form"),
		BusinessIncome_LandlordAsAdditionalInsuredRental_Value_CP_15_03("Business Income � Landlord As Additional Insured (Rental Value) CP 15 03"),
		DiscretionaryPayrollExpense_CP_15_04("Discretionary Payroll Expense CP 15 04"),
		ExtraExpenseCoverageForm_CP_00_50("Extra Expense Coverage Form CP 00 50"),
		FoodContaminationBusinessInterruptionAndExtraExpense_CP_15_05("Food Contamination (Business Interruption And Extra Expense) CP 15 05");


		String value;

		private AdditionalCoverages(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum UtilityServices_DirectDamageCP0417UtilityIs {

		Public("Public"),
		NotPublic("Not Public");
		String value;

		private UtilityServices_DirectDamageCP0417UtilityIs(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_DirectDamageCP0417WaterSupplyProperty {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_DirectDamageCP0417WaterSupplyProperty(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_DirectDamageCP0417Communication_notOHLines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_DirectDamageCP0417Communication_notOHLines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_DirectDamageCP0417Communication_IncOHLines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_DirectDamageCP0417Communication_IncOHLines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_DirectDamageCP0417Power_incOHLines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_DirectDamageCP0417Power_incOHLines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_DirectDamageCP0417Power_NotIncOHLines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_DirectDamageCP0417Power_NotIncOHLines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum CondominiumCommercialUnit_OwnersOptionalCoveragesCP0418LossAssessmentLimit {

		OneThousand("1,000"),
		TwoThousand("2,000"),
		ThreeThousand("3,000"),
		FourThousand("4,000"),
		FiveThousand("5,000"),
		SixThousand("6,000"),
		SevenThousand("7,000"),
		EightThousand("8,000"),
		NineThousand("9,000"),
		TenThousand("10,000");
		String value;

		private CondominiumCommercialUnit_OwnersOptionalCoveragesCP0418LossAssessmentLimit(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum CondominiumCommercialUnit_OwnersOptionalCoveragesCP0428DeductibleLossAssessment {

		FiveHundred("500");
		String value;

		private CondominiumCommercialUnit_OwnersOptionalCoveragesCP0428DeductibleLossAssessment(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum SpoilageCoverageCP0440TypeofProperty {

		BakeryGoods("Bakery Goods"),
		CheeseShops("Cheese Shops"),
		Delicatessens("Delicatessens"),
		FruitsAndVegetables("Fruits & Vegetables"),
		Restaurants("Restaurants"),
		DairyProductsexcludingIceCream("Dairy Products, excluding Ice Cream"),
		GroceryStores("Grocery Stores"),
		MeatAndPoultryMarkets("Meat & Poultry Markets"),
		Pharmaceuticals_Non_Manufacturing("Pharmaceuticals - Non-Manufacturing"),
		Supermarkets("Supermarkets"),
		DairyProductsIncludingIceCream("Dairy Products, Including Ice Cream"),
		Florists("Florists"),
		Greenhouses("Greenhouses"),
		Seafood("Seafood");
		String value;

		private SpoilageCoverageCP0440TypeofProperty(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum SpoilageCoverageCP0440Coverage {

		BreakdownContamination("Breakdown Contamination"),
		PowerOutage("Power Outage"),
		Both("Both");

		String value;
		private SpoilageCoverageCP0440Coverage(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum SpoilageCoverageCP0440Deductible {

		FiveHundred("500");

		String value;
		private SpoilageCoverageCP0440Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum DischargeFromSewerDrainOrSumpNotFlood_RelatedCP1038AnnualAggregateLimit {

		FiveThousand("5000");

		String value;
		private DischargeFromSewerDrainOrSumpNotFlood_RelatedCP1038AnnualAggregateLimit(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum DischargeFromSewerDrainOrSumpNotFlood_RelatedCP1038Coverage {

		PropertyDamage("Property Damage"),
		BusinessInterruption("Business Interruption"),
		Both("Both");

		String value;
		private DischargeFromSewerDrainOrSumpNotFlood_RelatedCP1038Coverage(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum DischargeFromSewerDrainOrSumpNotFlood_RelatedCP1038Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;
		private DischargeFromSewerDrainOrSumpNotFlood_RelatedCP1038Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum TheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRiskCP1044Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000"),
		TenThousand("10,000"),
		TwentyFiveThousand("25,000");

		String value;
		private TheftOfBuildingMaterialsAndSuppliesOtherThanBuildersRiskCP1044Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BuildingGlass_TenantsPolicyCP1470Deductible {

		None("None"),
		FiveHundred("500"),
		OneThousand("1,000");


		String value;
		private BuildingGlass_TenantsPolicyCP1470Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum BuildingGlass_TenantsPolicyCP1470CauseOfLoss {
		Basic("Basic"),
		Broad("Broad"),
		Special("Special");

		String value;

		private BuildingGlass_TenantsPolicyCP1470CauseOfLoss(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}



	public enum UtilityServices_TimeElementsCP1545WaterSupply {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_TimeElementsCP1545WaterSupply(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_TimeElementsCP1545WastewaterRemovalProperty {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_TimeElementsCP1545WastewaterRemovalProperty(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_TimeElementsCP1545CommunicationnotOHlines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_TimeElementsCP1545CommunicationnotOHlines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_TimeElementsCP1545CommunicationincOHlines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_TimeElementsCP1545CommunicationincOHlines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_TimeElementsCP1545PowernotOHlines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;

		private UtilityServices_TimeElementsCP1545PowernotOHlines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum UtilityServices_TimeElementsCP1545PowerincOHlines {

		Covered("Covered"),
		NotCovered("Not Covered");
		String value;
		private UtilityServices_TimeElementsCP1545PowerincOHlines(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum ExtraExpenseCoverageCP0050LimitOptions {

		OneHundred("100%/100%/100%"),
		FortyEightyOneHundred("40%/80%/100%"),
		ThirtyFiveSeventyOneHundred("35%/70%/100%");

		String value;
		private ExtraExpenseCoverageCP0050LimitOptions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum OutdoorSignsCP1440SignConstruction {

		EntirelyMetalIncludingFrameandSupports("Entirely Metal (Including Frame and Supports)"),
		Other("Other");

		String value;
		private OutdoorSignsCP1440SignConstruction(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}


	public enum OutdoorSignsCP1440CoinsurancePercent {

		EightyPercent("80%"),
		NinetyPercent("90%"),
		OneHundredPercent("100%");

		String value;
		private OutdoorSignsCP1440CoinsurancePercent(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum OrdinanceorLawCoverageCP0405CoverageA_CoverageForLossToTheUndamagedPortionOfTheBuilding {

		Covered("Covered"),
		NotCovered("Not Covered");


		String value;
		private OrdinanceorLawCoverageCP0405CoverageA_CoverageForLossToTheUndamagedPortionOfTheBuilding(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	public enum ClientsPropertyCR0401Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");


		String value;
		private ClientsPropertyCR0401Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	public enum InsideThePremises_TheftOfOtherPropertyCR0405Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");


		String value;
		private InsideThePremises_TheftOfOtherPropertyCR0405Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	public enum GuestsPropertyCR0411Deductible {

		FiveHundred("500"),
		OneThousand("1,000"),
		TwoThousandFiveHundred("2,500"),
		FiveThousand("5,000");


		String value;
		private GuestsPropertyCR0411Deductible(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	public enum GuestsPropertyCR0411SafeDepositBoxesLimit {

		TwentyFiveThousand("25,000"),
		FiftyThousand("50,000");


		String value;
		private GuestsPropertyCR0411SafeDepositBoxesLimit(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum GuestsPropertyCR0411ThePremisesLimit {

		OneThousandTwentyFiveThousand("1,000/25,000"),
		TwoThousandFiftyThousand("2,000/50,000");


		String value;
		private GuestsPropertyCR0411ThePremisesLimit(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum FunctionalBuildingValuationCP0438Limit {

		CP0438("See defined by script");


		String value;
		private FunctionalBuildingValuationCP0438Limit(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}





	public enum PayrollLimitationOrExclusion_CP_15_10_LimitationOrExclusion {

		PayrollExpenseLimitation("Limitation or Exclusion"),
		PayrollExpenseExclusion("Payroll Expense Limitation");

		String value;
		private PayrollLimitationOrExclusion_CP_15_10_LimitationOrExclusion(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}	


	public enum PayrollLimitationOrExclusion_CP_15_10_NumberOfDays{

		Zero("0"),
		Ninety("90"),
		OneHundredEighty("180");


		String value;
		private PayrollLimitationOrExclusion_CP_15_10_NumberOfDays(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	
	public enum PayrollLimitationOrExclusion_CP_15_10_Thefollowingaresubjecttotheprovisionsofthisendorsementifsoindicatedintheschedule{

		Including("All employees and job classification including officers, executives, management personnel and contract employees"),
		OtherThan("All employees and job classifications other than officers, executives, management personnel and contract employees"),
		EmployeesAndJobs("All employees and job classification (including officers, executives, management personnel and contract employees), except:"),
		Only("Only the following job classification and/or employees:");


		String value;
		private PayrollLimitationOrExclusion_CP_15_10_Thefollowingaresubjecttotheprovisionsofthisendorsementifsoindicatedintheschedule(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	

	public enum Condominium {
		Association("Association"),
		UnitOwner("Unit Owner"),
		NotACondominium("Not a Condominium");
		
		String value;
		private Condominium(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	
	public enum RateType {
		Class("Class"),
		Specific("Specific"),
		Tentative("Tentative");
		
		String value;
		private RateType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
	}
	
	
	
	public enum DiscretionaryPayrollExpense_CP_15_04_NumberOfDays {
		Ninty("90"),
		OneHundredTwenty("120"),
		OnedHundredFifty("150"),
		OnehundredEighty("180"),
		TwoHundredSeventy("270"),
		ThreeHundredSisty("360");
		String value;
		
		private DiscretionaryPayrollExpense_CP_15_04_NumberOfDays(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
	}
	
	
	
	
	
	public enum CommercialProperty_ExclusionsAndConditions {
		AdditionalPropertyNotCovered_CP_14_20("Additional Property Not Covered CP 14 20"),
		BrokenOrCrackedGlassExclusionForm_IDCP_31_1006("Broken Or Cracked Glass Exclusion Form IDCP 31 1006"),
		ExcludeCertainRisksInherentInInsuranceOperations_CR_25_23("Exclude Certain Risks Inherent In Insurance Operations CR 25 23"),
		ExcludeDesignatedPersonsOrClassesOfPersonsAsEmployees_CR_25_01("Exclude Designated Persons Or Classes Of Persons As Employees CR 25 01"),
		ExcludeDesignatedPremises_CR_35_13("Exclude Designated Premises CR 35 13"),
		ExcludeSpecifiedProperty_CR_35_01("Exclude Specified Property CR 35 01"),
		ExcludeUnauthorizedAdvancesRequireAnnualAudit_CR_25_25("Exclude Unauthorized Advances, Require Annual Audit CR 25 25"),
		RoofExclusionEndorsement_IDCP_31_1004("Roof Exclusion Endorsement IDCP 31 1004"),
		ExclusionOfLossDueToBy_ProductsOfProductionOrProcessingOperationsRentalProperties_CP_10_34("Exclusion Of Loss Due To By-Products Of Production Or Processing Operations (Rental Properties) CP 10 34"),
		ExclusionOfLossDueToVirusOrBacteria_CP_01_40("Exclusion Of Loss Due To Virus Or Bacteria CP 01 40"),
		SprinklerLeakageExclusion_CP_10_56("Sprinkler Leakage Exclusion CP 10 56"),
		TheftExclusion_CP_10_33("Theft Exclusion CP 10 33"),
		VandalismExclusion_CP_10_55("Vandalism Exclusion CP 10 55"),
		WindstormOrHailExclusion_CP_10_54("Windstorm Or Hail Exclusion CP 10 54"),
		BindingArbitration_CR_20_12("Binding Arbitration CR 20 12"),
		BurglaryAndRobberyProtectiveSafeguards_CP_12_11("Burglary And Robbery Protective Safeguards CP 12 11"),
		ChangeInControlOfTheInsured_NoticeToTheCompany_CR_20_29("Change In Control Of The Insured � Notice To The Company CR 20 29"),
		CommercialCrimeManuscriptEndorsement_IDCR_31_1001("Commercial Crime Manuscript Endorsement IDCR 31 1001"),
		CommercialPropertyConditions_CP_00_90("Commercial Property Conditions CP 00 90"),
		CommercialPropertyManuscriptEndorsement_IDCP_31_1005("Commercial Property Manuscript Endorsement IDCP 31 1005"),
		ConvertToAnAggregateLimitOfInsurance_CR_20_08("Convert To An Aggregate Limit Of Insurance CR 20 08"),
		IdahoChanges_CR_02_12("Idaho Changes CR 02 12"),
		LimitationsOnCoverageForRoofSurfacing_CP_10_36("Limitations On Coverage For Roof Surfacing CP 10 36"),
		MultipleDeductibleForm_IDCP_31_1001("Multiple Deductible Form IDCP 31 1001"),
		ProtectiveSafeguards_CP_04_11("Protective Safeguards CP 04 11"),
		TentativeRate_CP_99_93("Tentative Rate CP 99 93");

		String value;
		
		private CommercialProperty_ExclusionsAndConditions(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
		
		
		
		
		
	}
	
	
	
	
	
	






}













