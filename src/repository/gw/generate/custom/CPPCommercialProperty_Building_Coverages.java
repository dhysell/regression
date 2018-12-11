package repository.gw.generate.custom;

import repository.gw.enums.CommercialProperty;

import java.util.ArrayList;
import java.util.List;

public class CPPCommercialProperty_Building_Coverages {

	@SuppressWarnings("serial")
	List<CommercialProperty.PropertyCoverages> buildingCoverageList = new ArrayList<CommercialProperty.PropertyCoverages>() {{
		this.add(CommercialProperty.PropertyCoverages.BuildingCoverage);
	}};

	//BUILDING COVERAGE
//	private boolean buildingCoverage = true;
	private String buildingCoverage_Limit = "100";
	private CommercialProperty.BuildingCoverageCauseOfLoss buildingCoverage_CauseOfLoss = CommercialProperty.BuildingCoverageCauseOfLoss.Basic;
	private boolean buildingCoverage_VandalismExclusion_CP_10_55 = false;
	private CommercialProperty.BuildingCoverageDeductible buildingCoverage_Deductible = CommercialProperty.BuildingCoverageDeductible.FiveHundred;
	private CommercialProperty.BuildingCoverageValuationMethod buildingCoverage_ValuationMethod = CommercialProperty.BuildingCoverageValuationMethod.ACV;
	private CommercialProperty.BuildingCoverageCoinsurancePercent buildingCoverage_Coinsurance = CommercialProperty.BuildingCoverageCoinsurancePercent.EightyPercent;
	private CommercialProperty.BuildingCoverageAutoIncreasePercent buildingCoverage_AutoIncrease = CommercialProperty.BuildingCoverageAutoIncreasePercent.FourPercent;
	private String buildingCoverageBasicGroupIRate = "1";
	private String buildingCoverageBasicGroupIIRate = "2";
	private String buildingCoverageBasicGroupIISymbol = "b";
	private String buildingCoveragePrefixNumber = "2";


	//BUSINESS PERSONAL PROPERTY COVERAGE
//	private boolean businessPersonalPropertyCoverage = false;
	private String businessPersonalPropertyCoverage_Limit = "100";
	private CommercialProperty.BusinessPersonalPropertyCoverageCauseOfLoss businessPersonalPropertyCoverage_CauseOfLoss = CommercialProperty.BusinessPersonalPropertyCoverageCauseOfLoss.Basic;
	private CommercialProperty.BusinessPersonalPropertyCoverageDeductible businessPersonalPropertyCoverage_Deductible = CommercialProperty.BusinessPersonalPropertyCoverageDeductible.FiveHundred;
	private CommercialProperty.BusinessPersonalPropertyCoverageValuationMethod businessPersonalPropertyCoverage_ValuationMethod = CommercialProperty.BusinessPersonalPropertyCoverageValuationMethod.ACV;
	private CommercialProperty.BusinessPersonalPropertyCoverageCoinsurancePercent businessPersonalPropertyCoverage_Coinsurance = CommercialProperty.BusinessPersonalPropertyCoverageCoinsurancePercent.EightyPercent;
	private CommercialProperty.BusinessPersonalPropertyCoverageAutoIncreasePercent businessPersonalPropertyCoverage_AutoIncreasePercent = CommercialProperty.BusinessPersonalPropertyCoverageAutoIncreasePercent.FourPercent;
	private boolean businessPersonalPropertyCoverage_IsStockIncluded = false;
	private String businessPersonalPropertyCoverage_BasicGroupIRate = "1";
	private String businessPersonalPropertyCoverage_BasicGroupIIRate = "2";
	private String businessPersonalPropertyCoverage_BasicGroupIISymbol = "b";
	private String businessPersonalPropertyCoverage_PrefixNumber = "2";


	//BUILDERS RISK COVERAGE FORM CP 00 20
//	private boolean buildersRiskCoverageForm_CP_00_20 = false;
	private int buildersRiskCoverageForm_CP_00_20_Limit = 100;
	private CommercialProperty.BuildersRiskCoverageFormCP0020CauseOfLoss buildersRiskCoverageForm_CP_00_20_CauseOfLoss = CommercialProperty.BuildersRiskCoverageFormCP0020CauseOfLoss.Basic;
	private CommercialProperty.BuildersRiskCoverageFormCP0020Deductible buildersRiskCoverageForm_CP_00_20_Deductible = CommercialProperty.BuildersRiskCoverageFormCP0020Deductible.FiveHundred;
	private boolean constructionPastFoundationStage = false;
	private String buildersRiskCoverageForm_CP_00_20_BasicGroupIRate = "1";
	private String buildersRiskCoverageForm_CP_00_20_BasicGroupIIRate = "2";
	private String buildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol = "b";
	private String buildersRiskCoverageForm_CP_00_20_PrefixNumber = "2";
	private boolean buildingARenovation = false;

	//PROPERTY IN THE OPEN
//	private boolean propertyInTheOpen = false;
	private int propertyInTheOpen_Limit = 100;
	private CommercialProperty.PropertyInTheOpenCauseOfLoss propertyInTheOpen_CauseOfLoss = CommercialProperty.PropertyInTheOpenCauseOfLoss.Basic;
	private CommercialProperty.PropertyInTheOpenDeductible propertyInTheOpen_Deductible = CommercialProperty.PropertyInTheOpenDeductible.FiveHundred;
	private CommercialProperty.PropertyInTheOpenValuationMethod propertyInTheOpen_ValuationMethod = CommercialProperty.PropertyInTheOpenValuationMethod.ACV;
	private CommercialProperty.PropertyInTheOpenCoinsurancePercent propertyInTheOpen_Coinsurance = CommercialProperty.PropertyInTheOpenCoinsurancePercent.EightyPercent;
	private CommercialProperty.PropertyInTheOpenAutoIncreasePercent propertyInTheOpen_AutoIncrease = CommercialProperty.PropertyInTheOpenAutoIncreasePercent.FourPercent;
	private String propertyInTheOpen_UsageDescription = "Stuff and Things";


	//PROPERTY OF OTHERS
//	private boolean personalPropertyOfOthers = false;
	private int propertyOfOthers_Limit = 100;
	private CommercialProperty.PropertyOfOthersCauseOfLoss propertyOfOthers_CauseOfLoss = CommercialProperty.PropertyOfOthersCauseOfLoss.Basic;
	private CommercialProperty.PropertyOfOthersDeductible propertyOfOthers_Deductible = CommercialProperty.PropertyOfOthersDeductible.FiveHundred;
	private CommercialProperty.PropertyOfOthersCoinsurancePercent propertyOfOthers_Coinsurance = CommercialProperty.PropertyOfOthersCoinsurancePercent.EightyPercent;
	private String propertyOfOthers_CP_00_20_BasicGroupIRate = "1";
	private String propertyOfOthers_CP_00_20_BasicGroupIIRate = "2";
	private String propertyOfOthers_CP_00_20_BasicGroupIISymbol = "b";
	private String propertyOfOthers_CP_00_20_PrefixNumber = "2";

	//LOGAL LIABILITY COVERAGE FORM CP 00 40
//	private boolean legalLiabilityCoverageForm_CP_00_40 = false;
	private int legalLiabilityCoverageForm_CP_00_40_Limit = 1;
	private CommercialProperty.LegalLiabilityCoverageFormCP0040CauseOfLoss legalLiabilityCoverageForm_CP_00_40_CauseOfLoss = CommercialProperty.LegalLiabilityCoverageFormCP0040CauseOfLoss.Basic;


	public CPPCommercialProperty_Building_Coverages() {

	}
	
	@SuppressWarnings("serial")
	public CPPCommercialProperty_Building_Coverages(CommercialProperty.PropertyCoverages propertyCoverage) {
		setBuildingCoveragesList(new ArrayList<CommercialProperty.PropertyCoverages>() {{
			this.add(propertyCoverage);
		}});
	}
	
	@SuppressWarnings("serial")
	public CPPCommercialProperty_Building_Coverages(CommercialProperty.PropertyCoverages propertyCoverage1, CommercialProperty.PropertyCoverages propertyCoverage2) {
		setBuildingCoveragesList(new ArrayList<CommercialProperty.PropertyCoverages>() {{
			this.add(propertyCoverage1);
			this.add(propertyCoverage2);
		}});
	}
	
	@SuppressWarnings("serial")
	public CPPCommercialProperty_Building_Coverages(CommercialProperty.PropertyCoverages propertyCoverage1, CommercialProperty.PropertyCoverages propertyCoverage2, CommercialProperty.PropertyCoverages propertyCoverage3) {
		setBuildingCoveragesList(new ArrayList<CommercialProperty.PropertyCoverages>() {{
			this.add(propertyCoverage1);
			this.add(propertyCoverage2);
			this.add(propertyCoverage3);
		}});
	}
	
	public CPPCommercialProperty_Building_Coverages(List<CommercialProperty.PropertyCoverages> buildingCoverageList) {
		this.buildingCoverageList = buildingCoverageList;
	}


	public boolean isBuildingCoverage() {
		return buildingCoverageList.contains(CommercialProperty.PropertyCoverages.BuildingCoverage);
	}
//
//
//
//	public void setBuildingCoverage(boolean buildingCoverage) {
//		this.buildingCoverage = buildingCoverage;
//	}



	public String getBuildingCoverageBasicGroupIRate() {
		return buildingCoverageBasicGroupIRate;
	}



	public void setBuildingCoverageBasicGroupIRate(String buildingCoverageBasicGroupIRate) {
		this.buildingCoverageBasicGroupIRate = buildingCoverageBasicGroupIRate;
	}



	public String getBuildingCoverageBasicGroupIIRate() {
		return buildingCoverageBasicGroupIIRate;
	}



	public void setBuildingCoverageBasicGroupIIRate(String buildingCoverageBasicGroupIIRate) {
		this.buildingCoverageBasicGroupIIRate = buildingCoverageBasicGroupIIRate;
	}



	public String getBuildingCoverageBasicGroupIISymbol() {
		return buildingCoverageBasicGroupIISymbol;
	}



	public void setBuildingCoverageBasicGroupIISymbol(String buildingCoverageBasicGroupIISymbol) {
		this.buildingCoverageBasicGroupIISymbol = buildingCoverageBasicGroupIISymbol;
	}



	public boolean isBusinessPersonalPropertyCoverage() {
		return buildingCoverageList.contains(CommercialProperty.PropertyCoverages.BusinessPersonalPropertyCoverage);
	}
//
//
//
//	public void setBusinessPersonalPropertyCoverage(boolean businessPersonalPropertyCoverage) {
//		this.businessPersonalPropertyCoverage = businessPersonalPropertyCoverage;
//	}



	public String getBusinessPersonalPropertyCoverage_Limit() {
		return businessPersonalPropertyCoverage_Limit;
	}



	public void setBusinessPersonalPropertyCoverage_Limit(String businessPersonalPropertyCoverage_Limit) {
		this.businessPersonalPropertyCoverage_Limit = businessPersonalPropertyCoverage_Limit;
	}


	public boolean isBusinessPersonalPropertyCoverage_IsStockIncluded() {
		return businessPersonalPropertyCoverage_IsStockIncluded;
	}



	public void setBusinessPersonalPropertyCoverage_IsStockIncluded(boolean businessPersonalPropertyCoverage_IsStockIncluded) {
		this.businessPersonalPropertyCoverage_IsStockIncluded = businessPersonalPropertyCoverage_IsStockIncluded;
	}



	public String getBusinessPersonalPropertyCoverage_BasicGroupIRate() {
		return businessPersonalPropertyCoverage_BasicGroupIRate;
	}



	public void setBusinessPersonalPropertyCoverage_BasicGroupIRate(String businessPersonalPropertyCoverage_BasicGroupIRate) {
		this.businessPersonalPropertyCoverage_BasicGroupIRate = businessPersonalPropertyCoverage_BasicGroupIRate;
	}



	public String getBusinessPersonalPropertyCoverage_BasicGroupIIRate() {
		return businessPersonalPropertyCoverage_BasicGroupIIRate;
	}



	public void setBusinessPersonalPropertyCoverage_BasicGroupIIRate(String businessPersonalPropertyCoverage_BasicGroupIIRate) {
		this.businessPersonalPropertyCoverage_BasicGroupIIRate = businessPersonalPropertyCoverage_BasicGroupIIRate;
	}



	public String getBusinessPersonalPropertyCoverage_BasicGroupIISymbol() {
		return businessPersonalPropertyCoverage_BasicGroupIISymbol;
	}



	public void setBusinessPersonalPropertyCoverage_BasicGroupIISymbol(String businessPersonalPropertyCoverage_BasicGroupIISymbol) {
		this.businessPersonalPropertyCoverage_BasicGroupIISymbol = businessPersonalPropertyCoverage_BasicGroupIISymbol;
	}



	public boolean isBuildersRiskCoverageForm_CP_00_20() {
		return buildingCoverageList.contains(CommercialProperty.PropertyCoverages.BuildersRiskCoverageForm_CP_00_20);
	}
//
//
//
//	public void setBuildersRiskCoverageForm_CP_00_20(boolean buildersRiskCoverageForm_CP_00_20) {
//		this.buildersRiskCoverageForm_CP_00_20 = buildersRiskCoverageForm_CP_00_20;
//	}
//
//
//
	public boolean isPropertyInTheOpen() {
		return buildingCoverageList.contains(CommercialProperty.PropertyCoverages.PropertyInTheOpen);
	}
//
//
//
//	public void setPropertyInTheOpen(boolean propertyInTheOpen) {
//		this.propertyInTheOpen = propertyInTheOpen;
//	}
//
//
//
	public boolean isPersonalPropertyOfOthers() {
		return buildingCoverageList.contains(CommercialProperty.PropertyCoverages.PersonalPropertyOfOthers);
	}
//
//
//
//	public void setPersonalPropertyOfOthers(boolean propertyOfOthers) {
//		this.personalPropertyOfOthers = propertyOfOthers;
//	}
//
//
//
	public boolean isLegalLiabilityCoverageForm_CP_00_40() {
		return buildingCoverageList.contains(CommercialProperty.PropertyCoverages.LegalLiabilityCoverageForm_CP_00_40);
	}
//
//
//
//	public void setLegalLiabilityCoverageForm_CP_00_40(boolean legalLiabilityCoverageForm_CP_00_40) {
//		this.legalLiabilityCoverageForm_CP_00_40 = legalLiabilityCoverageForm_CP_00_40;
//	}



	public String getBuildingCoverage_Limit() {
		return buildingCoverage_Limit;
	}



	public void setBuildingCoverage_Limit(String buildingCoverage_Limit) {
		this.buildingCoverage_Limit = buildingCoverage_Limit;
	}


	public boolean isBuildingCoverage_VandalismExclusion_CP_10_55() {
		return buildingCoverage_VandalismExclusion_CP_10_55;
	}



	public void setBuildingCoverage_VandalismExclusion_CP_10_55(boolean buildingCoverage_VandalismExclusion_CP_10_55) {
		this.buildingCoverage_VandalismExclusion_CP_10_55 = buildingCoverage_VandalismExclusion_CP_10_55;
	}



	public CommercialProperty.BuildingCoverageCauseOfLoss getBuildingCoverage_CauseOfLoss() {
		return buildingCoverage_CauseOfLoss;
	}



	public void setBuildingCoverage_CauseOfLoss(CommercialProperty.BuildingCoverageCauseOfLoss buildingCoverage_CauseOfLoss) {
		this.buildingCoverage_CauseOfLoss = buildingCoverage_CauseOfLoss;
	}



	public CommercialProperty.BuildingCoverageDeductible getBuildingCoverage_Deductible() {
		return buildingCoverage_Deductible;
	}



	public void setBuildingCoverage_Deductible(CommercialProperty.BuildingCoverageDeductible buildingCoverage_Deductible) {
		this.buildingCoverage_Deductible = buildingCoverage_Deductible;
	}



	public CommercialProperty.BuildingCoverageValuationMethod getBuildingCoverage_ValuationMethod() {
		return buildingCoverage_ValuationMethod;
	}



	public void setBuildingCoverage_ValuationMethod(CommercialProperty.BuildingCoverageValuationMethod buildingCoverage_ValuationMethod) {
		this.buildingCoverage_ValuationMethod = buildingCoverage_ValuationMethod;
	}



	public CommercialProperty.BuildingCoverageCoinsurancePercent getBuildingCoverage_Coinsurance() {
		return buildingCoverage_Coinsurance;
	}



	public void setBuildingCoverage_Coinsurance(CommercialProperty.BuildingCoverageCoinsurancePercent buildingCoverage_Coinsurance) {
		this.buildingCoverage_Coinsurance = buildingCoverage_Coinsurance;
	}



	public CommercialProperty.BuildingCoverageAutoIncreasePercent getBuildingCoverage_AutoIncrease() {
		return buildingCoverage_AutoIncrease;
	}



	public void setBuildingCoverage_AutoIncrease(CommercialProperty.BuildingCoverageAutoIncreasePercent buildingCoverage_AutoIncrease) {
		this.buildingCoverage_AutoIncrease = buildingCoverage_AutoIncrease;
	}



	public CommercialProperty.BusinessPersonalPropertyCoverageCauseOfLoss getBusinessPersonalPropertyCoverage_CauseOfLoss() {
		return businessPersonalPropertyCoverage_CauseOfLoss;
	}



	public void setBusinessPersonalPropertyCoverage_CauseOfLoss(CommercialProperty.BusinessPersonalPropertyCoverageCauseOfLoss businessPersonalPropertyCoverage_CauseOfLoss) {
		this.businessPersonalPropertyCoverage_CauseOfLoss = businessPersonalPropertyCoverage_CauseOfLoss;
	}



	public CommercialProperty.BusinessPersonalPropertyCoverageDeductible getBusinessPersonalPropertyCoverage_Deductible() {
		return businessPersonalPropertyCoverage_Deductible;
	}



	public void setBusinessPersonalPropertyCoverage_Deductible(CommercialProperty.BusinessPersonalPropertyCoverageDeductible businessPersonalPropertyCoverage_Deductible) {
		this.businessPersonalPropertyCoverage_Deductible = businessPersonalPropertyCoverage_Deductible;
	}



	public CommercialProperty.BusinessPersonalPropertyCoverageValuationMethod getBusinessPersonalPropertyCoverage_ValuationMethod() {
		return businessPersonalPropertyCoverage_ValuationMethod;
	}



	public void setBusinessPersonalPropertyCoverage_ValuationMethod(CommercialProperty.BusinessPersonalPropertyCoverageValuationMethod businessPersonalPropertyCoverage_ValuationMethod) {
		this.businessPersonalPropertyCoverage_ValuationMethod = businessPersonalPropertyCoverage_ValuationMethod;
	}



	public CommercialProperty.BusinessPersonalPropertyCoverageCoinsurancePercent getBusinessPersonalPropertyCoverage_Coinsurance() {
		return businessPersonalPropertyCoverage_Coinsurance;
	}



	public void setBusinessPersonalPropertyCoverage_Coinsurance(CommercialProperty.BusinessPersonalPropertyCoverageCoinsurancePercent businessPersonalPropertyCoverage_Coinsurance) {
		this.businessPersonalPropertyCoverage_Coinsurance = businessPersonalPropertyCoverage_Coinsurance;
	}



	public CommercialProperty.BusinessPersonalPropertyCoverageAutoIncreasePercent getBusinessPersonalPropertyCoverage_AutoIncreasePercent() {
		return businessPersonalPropertyCoverage_AutoIncreasePercent;
	}



	public void setBusinessPersonalPropertyCoverage_AutoIncreasePercent(CommercialProperty.BusinessPersonalPropertyCoverageAutoIncreasePercent businessPersonalPropertyCoverage_AutoIncreasePercent) {
		this.businessPersonalPropertyCoverage_AutoIncreasePercent = businessPersonalPropertyCoverage_AutoIncreasePercent;
	}



	public int getBuildersRiskCoverageForm_CP_00_20_Limit() {
		return buildersRiskCoverageForm_CP_00_20_Limit;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_Limit(int buildersRiskCoverageForm_CP_00_20_Limit) {
		this.buildersRiskCoverageForm_CP_00_20_Limit = buildersRiskCoverageForm_CP_00_20_Limit;
	}



	public CommercialProperty.BuildersRiskCoverageFormCP0020CauseOfLoss getBuildersRiskCoverageForm_CP_00_20_CauseOfLoss() {
		return buildersRiskCoverageForm_CP_00_20_CauseOfLoss;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_CauseOfLoss(CommercialProperty.BuildersRiskCoverageFormCP0020CauseOfLoss buildersRiskCoverageForm_CP_00_20_CauseOfLoss) {
		this.buildersRiskCoverageForm_CP_00_20_CauseOfLoss = buildersRiskCoverageForm_CP_00_20_CauseOfLoss;
	}



	public CommercialProperty.BuildersRiskCoverageFormCP0020Deductible getBuildersRiskCoverageForm_CP_00_20_Deductible() {
		return buildersRiskCoverageForm_CP_00_20_Deductible;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_Deductible(CommercialProperty.BuildersRiskCoverageFormCP0020Deductible buildersRiskCoverageForm_CP_00_20_Deductible) {
		this.buildersRiskCoverageForm_CP_00_20_Deductible = buildersRiskCoverageForm_CP_00_20_Deductible;
	}



	public int getPropertyInTheOpen_Limit() {
		return propertyInTheOpen_Limit;
	}



	public void setPropertyInTheOpen_Limit(int propertyInTheOpen_Limit) {
		this.propertyInTheOpen_Limit = propertyInTheOpen_Limit;
	}



	public CommercialProperty.PropertyInTheOpenCauseOfLoss getPropertyInTheOpen_CauseOfLoss() {
		return propertyInTheOpen_CauseOfLoss;
	}



	public void setPropertyInTheOpen_CauseOfLoss(CommercialProperty.PropertyInTheOpenCauseOfLoss propertyInTheOpen_CauseOfLoss) {
		this.propertyInTheOpen_CauseOfLoss = propertyInTheOpen_CauseOfLoss;
	}



	public CommercialProperty.PropertyInTheOpenDeductible getPropertyInTheOpen_Deductible() {
		return propertyInTheOpen_Deductible;
	}



	public void setPropertyInTheOpen_Deductible(CommercialProperty.PropertyInTheOpenDeductible propertyInTheOpen_Deductible) {
		this.propertyInTheOpen_Deductible = propertyInTheOpen_Deductible;
	}



	public CommercialProperty.PropertyInTheOpenValuationMethod getPropertyInTheOpen_ValuationMethod() {
		return propertyInTheOpen_ValuationMethod;
	}



	public void setPropertyInTheOpen_ValuationMethod(CommercialProperty.PropertyInTheOpenValuationMethod propertyInTheOpen_ValuationMethod) {
		this.propertyInTheOpen_ValuationMethod = propertyInTheOpen_ValuationMethod;
	}



	public CommercialProperty.PropertyInTheOpenCoinsurancePercent getPropertyInTheOpen_Coinsurance() {
		return propertyInTheOpen_Coinsurance;
	}



	public void setPropertyInTheOpen_Coinsurance(CommercialProperty.PropertyInTheOpenCoinsurancePercent propertyInTheOpen_Coinsurance) {
		this.propertyInTheOpen_Coinsurance = propertyInTheOpen_Coinsurance;
	}



	public CommercialProperty.PropertyInTheOpenAutoIncreasePercent getPropertyInTheOpen_AutoIncrease() {
		return propertyInTheOpen_AutoIncrease;
	}



	public void setPropertyInTheOpen_AutoIncrease(CommercialProperty.PropertyInTheOpenAutoIncreasePercent propertyInTheOpen_AutoIncrease) {
		this.propertyInTheOpen_AutoIncrease = propertyInTheOpen_AutoIncrease;
	}



	public String getPropertyInTheOpen_UsageDescription() {
		return propertyInTheOpen_UsageDescription;
	}


	public void setPropertyInTheOpen_UsageDescription(String propertyInTheOpen_UsageDescription) {
		this.propertyInTheOpen_UsageDescription = propertyInTheOpen_UsageDescription;
	}



	public int getPropertyOfOthers_Limit() {
		return propertyOfOthers_Limit;
	}



	public void setPropertyOfOthers_Limit(int propertyOfOthers_Limit) {
		this.propertyOfOthers_Limit = propertyOfOthers_Limit;
	}



	public CommercialProperty.PropertyOfOthersCauseOfLoss getPropertyOfOthers_CauseOfLoss() {
		return propertyOfOthers_CauseOfLoss;
	}



	public void setPropertyOfOthers_CauseOfLoss(CommercialProperty.PropertyOfOthersCauseOfLoss propertyOfOthers_CauseOfLoss) {
		this.propertyOfOthers_CauseOfLoss = propertyOfOthers_CauseOfLoss;
	}



	public CommercialProperty.PropertyOfOthersDeductible getPropertyOfOthers_Deductible() {
		return propertyOfOthers_Deductible;
	}



	public void setPropertyOfOthers_Deductible(CommercialProperty.PropertyOfOthersDeductible propertyOfOthers_Deductible) {
		this.propertyOfOthers_Deductible = propertyOfOthers_Deductible;
	}


	public CommercialProperty.PropertyOfOthersCoinsurancePercent getPropertyOfOthers_Coinsurance() {
		return propertyOfOthers_Coinsurance;
	}



	public void setPropertyOfOthers_Coinsurance(CommercialProperty.PropertyOfOthersCoinsurancePercent propertyOfOthers_Coinsurance) {
		this.propertyOfOthers_Coinsurance = propertyOfOthers_Coinsurance;
	}



	public int getLegalLiabilityCoverageForm_CP_00_40_Limit() {
		return legalLiabilityCoverageForm_CP_00_40_Limit;
	}


	public void setLegalLiabilityCoverageForm_CP_00_40_Limit(int legalLiabilityCoverageForm_CP_00_40_Limit) {
		this.legalLiabilityCoverageForm_CP_00_40_Limit = legalLiabilityCoverageForm_CP_00_40_Limit;
	}



	public CommercialProperty.LegalLiabilityCoverageFormCP0040CauseOfLoss getLegalLiabilityCoverageForm_CP_00_40_CauseOfLoss() {
		return legalLiabilityCoverageForm_CP_00_40_CauseOfLoss;
	}



	public void setLegalLiabilityCoverageForm_CP_00_40_CauseOfLoss(CommercialProperty.LegalLiabilityCoverageFormCP0040CauseOfLoss legalLiabilityCoverageForm_CP_00_40_CauseOfLoss) {
		this.legalLiabilityCoverageForm_CP_00_40_CauseOfLoss = legalLiabilityCoverageForm_CP_00_40_CauseOfLoss;
	}



	public String getBuildingCoveragePrefixNumber() {
		return buildingCoveragePrefixNumber;
	}



	public void setBuildingCoveragePrefixNumber(String buildingCoveragePrefixNumber) {
		this.buildingCoveragePrefixNumber = buildingCoveragePrefixNumber;
	}



	public String getBusinessPersonalPropertyCoverage_PrefixNumber() {
		return businessPersonalPropertyCoverage_PrefixNumber;
	}



	public void setBusinessPersonalPropertyCoverage_PrefixNumber(String businessPersonalPropertyCoverage_PrefixNumber) {
		this.businessPersonalPropertyCoverage_PrefixNumber = businessPersonalPropertyCoverage_PrefixNumber;
	}



	public boolean isConstructionPastFoundationStage() {
		return constructionPastFoundationStage;
	}



	public void setConstructionPastFoundationStage(boolean constructionPastFoundationStage) {
		this.constructionPastFoundationStage = constructionPastFoundationStage;
	}



	public String getBuildersRiskCoverageForm_CP_00_20_BasicGroupIRate() {
		return buildersRiskCoverageForm_CP_00_20_BasicGroupIRate;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_BasicGroupIRate(String buildersRiskCoverageForm_CP_00_20_BasicGroupIRate) {
		this.buildersRiskCoverageForm_CP_00_20_BasicGroupIRate = buildersRiskCoverageForm_CP_00_20_BasicGroupIRate;
	}



	public String getBuildersRiskCoverageForm_CP_00_20_BasicGroupIIRate() {
		return buildersRiskCoverageForm_CP_00_20_BasicGroupIIRate;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_BasicGroupIIRate(String buildersRiskCoverageForm_CP_00_20_BasicGroupIIRate) {
		this.buildersRiskCoverageForm_CP_00_20_BasicGroupIIRate = buildersRiskCoverageForm_CP_00_20_BasicGroupIIRate;
	}



	public String getBuildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol() {
		return buildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol(String buildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol) {
		this.buildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol = buildersRiskCoverageForm_CP_00_20_BasicGroupIISymbol;
	}



	public String getBuildersRiskCoverageForm_CP_00_20_PrefixNumber() {
		return buildersRiskCoverageForm_CP_00_20_PrefixNumber;
	}



	public void setBuildersRiskCoverageForm_CP_00_20_PrefixNumber(String buildersRiskCoverageForm_CP_00_20_PrefixNumber) {
		this.buildersRiskCoverageForm_CP_00_20_PrefixNumber = buildersRiskCoverageForm_CP_00_20_PrefixNumber;
	}



	public String getPropertyOfOthers_CP_00_20_BasicGroupIRate() {
		return propertyOfOthers_CP_00_20_BasicGroupIRate;
	}



	public void setPropertyOfOthers_CP_00_20_BasicGroupIRate(String propertyOfOthers_CP_00_20_BasicGroupIRate) {
		this.propertyOfOthers_CP_00_20_BasicGroupIRate = propertyOfOthers_CP_00_20_BasicGroupIRate;
	}



	public String getPropertyOfOthers_CP_00_20_BasicGroupIIRate() {
		return propertyOfOthers_CP_00_20_BasicGroupIIRate;
	}



	public void setPropertyOfOthers_CP_00_20_BasicGroupIIRate(String propertyOfOthers_CP_00_20_BasicGroupIIRate) {
		this.propertyOfOthers_CP_00_20_BasicGroupIIRate = propertyOfOthers_CP_00_20_BasicGroupIIRate;
	}



	public String getPropertyOfOthers_CP_00_20_BasicGroupIISymbol() {
		return propertyOfOthers_CP_00_20_BasicGroupIISymbol;
	}



	public void setPropertyOfOthers_CP_00_20_BasicGroupIISymbol(String propertyOfOthers_CP_00_20_BasicGroupIISymbol) {
		this.propertyOfOthers_CP_00_20_BasicGroupIISymbol = propertyOfOthers_CP_00_20_BasicGroupIISymbol;
	}



	public String getPropertyOfOthers_CP_00_20_PrefixNumber() {
		return propertyOfOthers_CP_00_20_PrefixNumber;
	}



	public void setPropertyOfOthers_CP_00_20_PrefixNumber(String propertyOfOthers_CP_00_20_PrefixNumber) {
		this.propertyOfOthers_CP_00_20_PrefixNumber = propertyOfOthers_CP_00_20_PrefixNumber;
	}



	public boolean isBuildingARenovation() {
		return buildingARenovation;
	}



	public void setBuildingARenovation(boolean buildingARenovation) {
		this.buildingARenovation = buildingARenovation;
	}


	public List<CommercialProperty.PropertyCoverages> getBuildingCoverageList() {
		return buildingCoverageList;
	}


	public void setBuildingCoveragesList(List<CommercialProperty.PropertyCoverages> buildingCoverageList) {
		this.buildingCoverageList = buildingCoverageList;
	}














}







