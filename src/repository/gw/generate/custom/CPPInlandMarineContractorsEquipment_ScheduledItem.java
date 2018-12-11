package repository.gw.generate.custom;

import repository.gw.enums.InlandMarineCPP;

public class CPPInlandMarineContractorsEquipment_ScheduledItem {

	private InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType equipmentType = InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType.ATV_UTV;
	private String limit = "1000";
	private boolean rentedToOthers = false;
	private boolean rentedFromOthers = false;
	
	public CPPInlandMarineContractorsEquipment_ScheduledItem() {
		//default constructor
	}

	public InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(InlandMarineCPP.ContractorsEquipmentCoverageForm_IH_00_68_EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public boolean isRentedToOthers() {
		return rentedToOthers;
	}

	public void setRentedToOthers(boolean rentedToOthers) {
		this.rentedToOthers = rentedToOthers;
	}

	public boolean isRentedFromOthers() {
		return rentedFromOthers;
	}

	public void setRentedFromOthers(boolean rentedFromOthers) {
		this.rentedFromOthers = rentedFromOthers;
	}
	
}
