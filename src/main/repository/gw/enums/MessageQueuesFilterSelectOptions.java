package repository.gw.enums;

public enum MessageQueuesFilterSelectOptions {
	Failed_Messages("failed messages"),
	Messages_Needing_Retry("messages needing retry"),
	Unfinished_Messages("any unfinished messages");
	String value;
		
	private MessageQueuesFilterSelectOptions(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
