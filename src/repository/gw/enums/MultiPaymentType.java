package repository.gw.enums;

public enum MultiPaymentType {
    Suspense("Suspense"),
	Payment("Payment");
	String value;

    MultiPaymentType(String type) {
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
