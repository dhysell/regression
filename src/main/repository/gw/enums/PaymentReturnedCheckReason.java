package repository.gw.enums;

public enum PaymentReturnedCheckReason {
	None("<none>"),
	Account_Closed("Account Closed"),
	Account_Frozen("Account Frozen"),
	Authorization_Revoked("Authorization Revoked"),
	Insufficient_Funds("Insufficient Funds"),
	Invalid_Account("Invalid Account"),
	Other_Did_Not_Honor("Other: Did not honor - please contact bank for details"),
	Payment_Stopped("Payment Stopped");
	String value;
	
	private PaymentReturnedCheckReason(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
}
