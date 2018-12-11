package repository.gw.enums;

public enum ReturnMailReason {
	AttemptedNotKnown("Attempted - Not Known"),
	ForwardingOrderExpired("Forwarding Order Expired"),
	InsufficientAddress("Insufficient Address"),
	Moved("Moved - Left No Address"),
	NoMailReceptacle("No Mail Receptacle"),
	NoSuchNumber("No Such Number"),
	NotAtThisAddress("Not At This Address"),
	NotDeliverableAsAddressed("Not Deliverable As Addressed"),
	Other("Other"),
	TemporarilyAway("Temporarily Away"),
	UnableToForward("Unable to Forward"),
	Vacant("Vacant");
	
	String value;
	
	private ReturnMailReason(String type){
		this.value = type;
	}
	
	public String getValue(){
		return this.value;
	}
}
