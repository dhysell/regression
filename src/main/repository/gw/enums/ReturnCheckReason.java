package repository.gw.enums;

public enum ReturnCheckReason {	
	CheckAmountDoNotAgree("Check Amount do not Agree"),
	DuplicatePayment("Duplicate Payment"),
	Other("Other"),
	SentToUsInError("Sent to us in Error"),	
	YourAccountIsInBalance("Your Account is in Balance");
	
	String value;
	
	private ReturnCheckReason(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
}

