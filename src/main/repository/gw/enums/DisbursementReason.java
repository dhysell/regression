package repository.gw.enums;

public enum DisbursementReason {
	Cancellation("Cancellation"),
	ChangePayer("Change Payer"),
	IntercompanyTransfer("Intercompany Transfer"),
	Other("Other"),
	Overpayment("Overpayment"),
	ReturnPremiumAudit("Return Premium Audit"),
	ReturnPremiumPolicyChange("Return Premium PolicyChange"),
	WrongPayer("Wrong Payer");
	String value;
	
	private DisbursementReason(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
