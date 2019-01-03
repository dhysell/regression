package repository.gw.enums;

public enum PaymentType {
	Cash("Cash"),
	Cash_Equivalent("Cash Equivalent"),
	Credit_Debit("Credit / Debit"),
	ACH_EFT("ACH / EFT"),
	Check("Check"),
	Title_Company("Title Company"),
	Intercompany_Transfer("Intercompany Transfer"),
	Website("Website");
	String value;

	private PaymentType(String type){
		value = type;
	}

	public String getValue(){
		return value;
	}

	public static PaymentType getRandom() {
		return values()[(int) (Math.random() * values().length)];
	}
}
