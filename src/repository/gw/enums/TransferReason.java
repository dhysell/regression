package repository.gw.enums;

public enum TransferReason {
	None("<none>"),
	Agent_Request("Agent Request"),
	Coverage_Transferred("Coverage Transferred"),
	Insured_Request("Insured Request"),
	Misapplied("Misapplied"),
	Other("Other"),
	Payer_Change("Payer Change"),
	Policy_Transfer("Policy Transfer");
	String value;
	
	private TransferReason(String value) {
		this.value = value;
	}
	
	public String getValue(){
		return value;
	}
}
