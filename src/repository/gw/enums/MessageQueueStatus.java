package repository.gw.enums;

public enum MessageQueueStatus {
	Started("Started"),
	Suspended("Suspended");
	String value;
		
	private MessageQueueStatus(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
