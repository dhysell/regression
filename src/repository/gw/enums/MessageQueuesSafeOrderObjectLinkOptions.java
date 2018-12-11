package repository.gw.enums;

public enum MessageQueuesSafeOrderObjectLinkOptions {
	Non_Safe_Ordered_Messages("Non-safe-ordered messages"),
	Outbound_Email("Outbound E-mail");
	String value;
		
	private MessageQueuesSafeOrderObjectLinkOptions(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
