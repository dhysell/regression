package repository.gw.generate.custom;

import java.util.ArrayList;

public class SquireFPP {

	private repository.gw.enums.FPP.FPPCoverageTypes coverageType = repository.gw.enums.FPP.FPPCoverageTypes.BlanketExclude;
    private repository.gw.enums.FPP.FPPDeductible deductible = repository.gw.enums.FPP.FPPDeductible.Ded_1000;
    private boolean overrideEmptyItemsList = false;
    private ArrayList<SquireFPPTypeItem> items = new ArrayList<SquireFPPTypeItem>();
    private ArrayList<String> additionalInsureds = new ArrayList<String>();
	private int borrowedEquipmentLimit;
	private int freezingOfLivestockLimit;
	private int nonOwnedEquipmentLimit;
	private boolean selectNonOwnerEquipment = false;
	private ArrayList<repository.gw.generate.custom.AdditionalInterest> additionalInterests;

    public SquireFPP() {

    }

    public SquireFPP(repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes... fppTypes) {
        for (repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes subtype : fppTypes) {
            items.add(new SquireFPPTypeItem(this, subtype));
        }
    }

	public int getTotalNumItemsPerSubType(repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes subType) {
		int numToReturn = 0;
		for(SquireFPPTypeItem item : getItems()) {
			if(item.getType() == subType) {
				numToReturn++;
			}
		}
		
		return numToReturn;
	}
	
	public int getTotalValuePerSubType(repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes subType) {
		int numToReturn = 0;
		for(SquireFPPTypeItem item : getItems()) {
			if(item.getType() == subType) {
				numToReturn = numToReturn + item.getValue();
			}
		}
		
		return numToReturn;
	}
	
	public repository.gw.enums.FPP.FPPCoverageTypes getCoverageType() {
		return coverageType;
	}
	
	public void setCoverageType(repository.gw.enums.FPP.FPPCoverageTypes coverageType) {
		this.coverageType = coverageType;
	}

	public repository.gw.enums.FPP.FPPDeductible getDeductible() {
		return deductible;
	}

	public void setDeductible(repository.gw.enums.FPP.FPPDeductible deductible) {
		this.deductible = deductible;
	}
	
	public ArrayList<SquireFPPTypeItem> getItems(repository.gw.enums.FPP.FarmPersonalPropertyTypes type) {
		ArrayList<SquireFPPTypeItem> toReturn = new ArrayList<SquireFPPTypeItem>();
		for(SquireFPPTypeItem item : getItems()) {
			repository.gw.enums.FPP.FPPFarmPersonalPropertySubTypes itemType = item.getType();
			repository.gw.enums.FPP.FarmPersonalPropertyTypes itemParentType = itemType.getParentType();
			if(itemParentType.equals(type)) {
				toReturn.add(item);
			}
		}
		return toReturn;
	}
	
	public ArrayList<SquireFPPTypeItem> getItemsWithAdditionalInterests(repository.gw.enums.FPP.FarmPersonalPropertyTypes type) {
		ArrayList<SquireFPPTypeItem> toReturn = new ArrayList<SquireFPPTypeItem>();
		for(SquireFPPTypeItem item : getItems()) {
			if(item.getType().getParentType().equals(type) && item.getAdditionalInterest() != null) {
				toReturn.add(item);
			}
		}
		return toReturn;
	}

	public ArrayList<SquireFPPTypeItem> getItems() {
		return items;
	}

	public void setItems(ArrayList<SquireFPPTypeItem> items) {
		this.items = items;		
	}

    public void setItems(SquireFPPTypeItem... items) {
        for (SquireFPPTypeItem fppTypeItem : items) {
            this.items.add(fppTypeItem);
        }
    }

	public ArrayList<String> getAdditionalInsureds() {
		return additionalInsureds;
	}

	public void setAdditionalInsureds(ArrayList<String> additionalInsureds) {
		this.additionalInsureds = additionalInsureds;
	}

	public int getBorrowedEquipmentLimit() {
		return borrowedEquipmentLimit;
	}

	public void setBorrowedEquipmentLimit(int borrowedEquipmentLimit) {
		this.borrowedEquipmentLimit = borrowedEquipmentLimit;
	}

	public int getFreezingOfLivestockLimit() {
		return freezingOfLivestockLimit;
	}

	public void setFreezingOfLivestockLimit(int freezingOfLivestockLimit) {
		this.freezingOfLivestockLimit = freezingOfLivestockLimit;
	}

	public int getNonOwnedEquipmentLimit() {
		return nonOwnedEquipmentLimit;
	}

	public void setNonOwnedEquipmentLimit(int nonOwnedEquipmentLimit) {
		this.selectNonOwnerEquipment = true;
		this.nonOwnedEquipmentLimit = nonOwnedEquipmentLimit;
	}
	
	public void addNonOwnedEquipment(boolean trueFalse){
		this.selectNonOwnerEquipment = trueFalse;
	}
	
	public boolean getNonOwnedEquipmentCheck(){
		return this.selectNonOwnerEquipment;
	}

	public ArrayList<repository.gw.generate.custom.AdditionalInterest> getAdditionalInterests() {
		return additionalInterests;
	}

	public void setAdditionalInterests(ArrayList<AdditionalInterest> additionalInterests) {
		this.additionalInterests = additionalInterests;
	}

    public boolean isOverrideEmptyItemsList() {
        return overrideEmptyItemsList;
    }

    public void setOverrideEmptyItemsList(boolean overrideEmptyItemsList) {
        this.overrideEmptyItemsList = overrideEmptyItemsList;
    }
	
	
	
}
