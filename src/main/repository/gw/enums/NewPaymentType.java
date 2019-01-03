package repository.gw.enums;

public enum NewPaymentType {
	NewDirectBillPayment("New Direct Bill Payment"), 
	NewDirectBillCreditDistribution("New Direct Bill Credit Distribution"), 
	PaymentRequest("Payment Request");
	String value;
	
	private NewPaymentType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}