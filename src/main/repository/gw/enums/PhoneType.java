package repository.gw.enums;

public enum PhoneType {
	None("<none>"), 
	Business("Business"), 
	Work("Work"), 
	Home("Home"), 
	Mobile("Mobile");
	String value;
	
	private PhoneType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
