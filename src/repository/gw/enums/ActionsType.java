package repository.gw.enums;

public enum ActionsType {
	AddPolicy("Add Policy"), 
	NewPayment("New Payment"), 
	Reports("Reports"), 
	NewTransaction("New Transaction"), 
	NewNote("New Note");
	String value;
	
	private ActionsType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
