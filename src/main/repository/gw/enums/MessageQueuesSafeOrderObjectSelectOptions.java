package repository.gw.enums;

public enum MessageQueuesSafeOrderObjectSelectOptions {
	Safe_Order_Object_With_Messages_In_Error("Safe order object with messages in error"),
	Safe_Order_Object_With_Messages_Needing_Retry("Safe order object with messages needing retry"),
	Safe_Order_Object_With_Any_Unfinished_Messages("Safe order object with any unfinished messages");
	String value;
		
	private MessageQueuesSafeOrderObjectSelectOptions(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
