package repository.gw.enums;

public enum Topic {

	BusinessDevelopment("Business Development"), 
	AccReceivableComments("Accounts Receivable Comments"), 
	AgentComments("Agent Comments"), 
	CRSComments("CSR Comments"), 
	Other("Other"), 
	UnderwritingComments("Underwriting Comments");	
	
	String value;
		
	private Topic(String topic){
		value = topic;
	}
	
	public String getValue(){
		return value;
	}

	public static Topic getEnumFromStringValue(String text){
		for(Topic status: Topic.values()){
			if(text.equalsIgnoreCase(status.value)){
				return status;
			}
		}
		return null;
	}
}
