package repository.gw.generate.custom;

import repository.gw.enums.LivestockScheduledItemType;

public class SquireLiablityCoverageLivestockItem {
	
	private repository.gw.enums.LivestockScheduledItemType type = repository.gw.enums.LivestockScheduledItemType.Cow;
	private int quantity = 1;
	
	public repository.gw.enums.LivestockScheduledItemType getType() {
		return type;
	}
	
	public void setType(LivestockScheduledItemType type) {
		this.type = type;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

}
