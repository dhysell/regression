package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

import java.util.ArrayList;
import java.util.List;

public class CPPInlandMarineContractorsEquipment {

	// Coverages
	private InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_ContractorType contractorType = InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_ContractorType.SmallContractor;
	private InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Deductible deductible = InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Deductible.TwoHundredFifty;
	private InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance coinsurance = InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance.EightlyPercent;
	
	private boolean contractorsRentedEquipment = true;
	private String contractorsRentedEquipmentLimit = "1000";
	
	private boolean miscellaneousItemsBlanketCoverage = true;
	private String miscellaneousItemsLimit = "1000";
	private InlandMarineCPP.MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible miscellaneousItemsDeductible = InlandMarineCPP.MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible.OneHundred;
	
	private boolean toolsAndClothing = true;
	private String toolsAndClothingLimit = "1000";
	private InlandMarineCPP.ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible toolsAndClothingDeductible = InlandMarineCPP.ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible.OneHundred;
	
	private List<CPPInlandMarineContractorsEquipment_ScheduledItem> scheduledItems = new ArrayList<>();
	
	public CPPInlandMarineContractorsEquipment() {
		scheduledItems.add(new CPPInlandMarineContractorsEquipment_ScheduledItem());
	}

	public InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_ContractorType getContractorType() {
		return contractorType;
	}

	public void setContractorType(InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_ContractorType contractorType) {
		this.contractorType = contractorType;
	}

	public InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Deductible getDeductible() {
		return deductible;
	}

	public void setDeductible(InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Deductible deductible) {
		this.deductible = deductible;
	}

	public InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance getCoinsurance() {
		return coinsurance;
	}

	public void setCoinsurance(InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_Coinsurance coinsurance) {
		this.coinsurance = coinsurance;
	}

	public boolean isContractorsRentedEquipment() {
		return contractorsRentedEquipment;
	}

	public void setContractorsRentedEquipment(boolean contractorsRentedEquipment) {
		this.contractorsRentedEquipment = contractorsRentedEquipment;
	}

	public String getContractorsRentedEquipmentLimit() {
		return contractorsRentedEquipmentLimit;
	}

	public void setContractorsRentedEquipmentLimit(String contractorsRentedEquipmentLimit) {
		this.contractorsRentedEquipmentLimit = contractorsRentedEquipmentLimit;
	}

	public boolean isMiscellaneousItemsBlanketCoverage() {
		return miscellaneousItemsBlanketCoverage;
	}

	public void setMiscellaneousItemsBlanketCoverage(boolean miscellaneousItemsBlanketCoverage) {
		this.miscellaneousItemsBlanketCoverage = miscellaneousItemsBlanketCoverage;
	}

	public String getMiscellaneousItemsLimit() {
		return miscellaneousItemsLimit;
	}

	public void setMiscellaneousItemsLimit(String miscellaneousItemsLimit) {
		this.miscellaneousItemsLimit = miscellaneousItemsLimit;
	}

	public InlandMarineCPP.MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible getMiscellaneousItemsDeductible() {
		return miscellaneousItemsDeductible;
	}

	public void setMiscellaneousItemsDeductible(
			InlandMarineCPP.MiscellaneousItemsBlanketCoverage_IH_68_02_Deductible miscellaneousItemsDeductible) {
		this.miscellaneousItemsDeductible = miscellaneousItemsDeductible;
	}

	public boolean isToolsAndClothing() {
		return toolsAndClothing;
	}

	public void setToolsAndClothing(boolean toolsAndClothing) {
		this.toolsAndClothing = toolsAndClothing;
	}

	public String getToolsAndClothingLimit() {
		return toolsAndClothingLimit;
	}

	public void setToolsAndClothingLimit(String toolsAndClothingLimit) {
		this.toolsAndClothingLimit = toolsAndClothingLimit;
	}

	public InlandMarineCPP.ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible getToolsAndClothingDeductible() {
		return toolsAndClothingDeductible;
	}

	public void setToolsAndClothingDeductible(
			InlandMarineCPP.ToolsAndClothingBelongingToYourEmployees_IH_68_01_Deductible toolsAndClothingDeductible) {
		this.toolsAndClothingDeductible = toolsAndClothingDeductible;
	}

	public List<CPPInlandMarineContractorsEquipment_ScheduledItem> getScheduledItems() {
		return scheduledItems;
	}

	public void setScheduledItems(List<CPPInlandMarineContractorsEquipment_ScheduledItem> scheduledItems) {
		this.scheduledItems = scheduledItems;
	}

}
