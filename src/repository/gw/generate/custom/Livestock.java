package repository.gw.generate.custom;

import repository.gw.enums.LivestockDeductible;
import repository.gw.enums.LivestockType;

import java.util.ArrayList;

public class Livestock {
	
	private repository.gw.enums.LivestockType type;
	private repository.gw.enums.LivestockDeductible deductible = repository.gw.enums.LivestockDeductible.Ded0;
	private String clubName;
	private int limit;
	private boolean animalsHaveDiseaseSickness;
	private boolean animalsWithDiseasesHaveBeenOnPremises;
	private boolean animalsAreSoundAndNormal;
	private ArrayList<String> additionalInsureds;
	private ArrayList<LivestockScheduledItem> scheduledItems;
	
	public repository.gw.enums.LivestockType getType() {
		return type;
	}
	
	public void setType(repository.gw.enums.LivestockType type) {
		this.type = type;
	}
	
	private void checkIfTypeIsSet() throws Exception {
		if(getType() == null) {
			throw new Exception("Type must be set before you can set this field");
		}
	}
	
	public repository.gw.enums.LivestockDeductible getDeductible() {
		return deductible;
	}
	
	public void setDeductible(LivestockDeductible deductible) throws Exception {
		checkIfTypeIsSet();
		if(getType() == repository.gw.enums.LivestockType.DeathOfLivestock) {
			throw new Exception("Value is not valid for Death of Livestock.");
		}
		this.deductible = deductible;
	}
	
	public String getClubName() {
		return clubName;
	}

	public void setClubName(String clubName) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.LivestockType.FourH) {
			throw new Exception("Value is only valid for 4H Type");
		}
		this.clubName = clubName;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.LivestockType.FourH) {
			throw new Exception("Value is only valid for 4H Type");
		}
		this.limit = limit;
	}

	public boolean isAnimalsHaveDiseaseSickness() {
		return animalsHaveDiseaseSickness;
	}

	public void setAnimalsHaveDiseaseSickness(boolean animalsHaveDiseaseSickness) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.LivestockType.FourH) {
			throw new Exception("Value is only valid for 4H Type");
		}
		this.animalsHaveDiseaseSickness = animalsHaveDiseaseSickness;
	}

	public boolean isAnimalsWithDiseasesHaveBeenOnPremises() {
		return animalsWithDiseasesHaveBeenOnPremises;
	}

	public void setAnimalsWithDiseasesHaveBeenOnPremises(boolean animalsWithDiseasesHaveBeenOnPremises) throws Exception {
		checkIfTypeIsSet();
		if(getType() != repository.gw.enums.LivestockType.FourH) {
			throw new Exception("Value is only valid for 4H Type");
		}
		this.animalsWithDiseasesHaveBeenOnPremises = animalsWithDiseasesHaveBeenOnPremises;
	}

	public boolean isAnimalsAreSoundAndNormal() {
		return animalsAreSoundAndNormal;
	}

	public void setAnimalsAreSoundAndNormal(boolean animalsAreSoundAndNormal) throws Exception {
		checkIfTypeIsSet();
		if(getType() != LivestockType.FourH) {
			throw new Exception("Value is only valid for 4H Type");
		}
		this.animalsAreSoundAndNormal = animalsAreSoundAndNormal;
	}

	public ArrayList<String> getAdditionalInsureds() {
		return additionalInsureds;
	}
	
	public void setAdditionalInsureds(ArrayList<String> additionalInsureds) {
		this.additionalInsureds = additionalInsureds;
	}
	
	public ArrayList<LivestockScheduledItem> getScheduledItems() {
		return scheduledItems;
	}
	
	public void setScheduledItems(ArrayList<LivestockScheduledItem> scheduledItems) {
		this.scheduledItems = scheduledItems;
	}

}
