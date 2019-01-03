package repository.gw.enums;

public enum PaymentReversalReason {
	None("<none>"),
	Associated_Disbursement_Reversed_or_Void("Associated Disbursement Reversed or Void"),
	Payment_Modification("Payment Modification"),
	Payment_Moved("Payment Moved"),
	Processing_Error_Did_Not_Go_To_Bank("Processing Error - Did not go to Bank"),
	Processing_Error_Went_To_Bank("Processing Error - Went to Bank"),
	Return_Payment("Return Payment");
	String value;
	
	private PaymentReversalReason(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
}
