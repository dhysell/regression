package repository.gw.enums;

public enum CancellationEvent {
	SendNoticeOfIntentToCancel("Send Notice of Intent To Cancel"), 
	CancellationBillingInstructionReceived("Cancellation Billing Instruction Received"), 
	SendBalanceDue("Send Balance Due"), 
	SendSecondBalanceDue("Send Second Balance Due"), 
	ReviewTheDelinquency("Review The Delinquency"),
	Writeoff("Writeoff");
	String value;
	
	private CancellationEvent(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
