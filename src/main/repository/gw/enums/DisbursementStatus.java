package repository.gw.enums;

public enum DisbursementStatus {
	Approved("Approved"),
	Awaiting_Approval("Awaiting Approval"),
	Rejected("Rejected"),
	Sent("Sent"),
	Reapplied("Reapplied"),
	Pending("Pending");
	String value;
	
	private DisbursementStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
