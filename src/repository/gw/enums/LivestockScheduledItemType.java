package repository.gw.enums;

import java.util.ArrayList;

public enum LivestockScheduledItemType {
	
	Bull("Bull", "Cows", repository.gw.enums.LivestockType.Livestock),
	Cow("Cow", "Cows", repository.gw.enums.LivestockType.Livestock),
	Heifer("Heifer", "Cows", repository.gw.enums.LivestockType.Livestock),
	Horse("Horse", "Horses", repository.gw.enums.LivestockType.Livestock),
	Llama("Llama", "Horses", repository.gw.enums.LivestockType.Livestock),
	Alpaca("Alpaca", "Horses", repository.gw.enums.LivestockType.Livestock),
	Mule("Mule", "Horses", repository.gw.enums.LivestockType.Livestock),
	Swine("Swine", null, repository.gw.enums.LivestockType.Livestock),
	Poultry("Poultry", null, repository.gw.enums.LivestockType.Livestock),
	Donkey("Donkey", "Horses", repository.gw.enums.LivestockType.Livestock),
	Goat("Goat", null, repository.gw.enums.LivestockType.Livestock),
	SheepL("Sheep", null, repository.gw.enums.LivestockType.Livestock),
	Steer("Steer", "Cows", repository.gw.enums.LivestockType.Livestock),
	HogSwine("Hog/Swine", null, repository.gw.enums.LivestockType.Livestock),
	SheepD("Sheep", null, repository.gw.enums.LivestockType.DeathOfLivestock),
	HogSwineD("Hogs/Swine", null, repository.gw.enums.LivestockType.DeathOfLivestock),
	Other("Other", null, repository.gw.enums.LivestockType.DeathOfLivestock),
	Calf("Calf", "Cows", repository.gw.enums.LivestockType.FourH),
	Hog("Hog", null, repository.gw.enums.LivestockType.FourH),
	Sheep4("Sheep", null, repository.gw.enums.LivestockType.FourH);
	
	String section4Value;
	String section2Value;
	repository.gw.enums.LivestockType parentType;
	
	private LivestockScheduledItemType(String section4Value, String section2Value, repository.gw.enums.LivestockType parentType) {
		this.section4Value = section4Value;
		this.section2Value = section2Value;
		this.parentType = parentType;
	}
	
	public String getSection4Value() {
		return this.section4Value;
	}
	
	public String getSection2Value() {
		return this.section2Value;
	}
	
	public repository.gw.enums.LivestockType getParentType() {
		return this.parentType;
	}
	
	public static ArrayList<LivestockScheduledItemType> getAllWithSpecificParent(LivestockType parentType) {
		ArrayList<LivestockScheduledItemType> toReturn = new ArrayList<LivestockScheduledItemType>();
		for (LivestockScheduledItemType lsit : LivestockScheduledItemType.values()) {
			toReturn.add(lsit);
		}
		
		return toReturn;
	}
}
