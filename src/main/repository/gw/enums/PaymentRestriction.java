package repository.gw.enums;

public enum PaymentRestriction {
	None("None"),
	Cash_Only("Cash Only"),
	Cash_Only_Warning("Cash Only Warning"),
	None_One_NSF("None - One NSF");
	String value;
	
	PaymentRestriction(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
