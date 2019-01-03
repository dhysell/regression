package repository.gw.enums;

public enum BatchSource {
	ListOfPolicies("List of Policies");
	String value;
	
	private BatchSource(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
