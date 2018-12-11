package repository.gw.enums;

public enum AccountType {
	None("<none>"),
	Insured("Insured"),
	List_Bill("List Bill"),
	Collection_Agency("Collection Agency"),
	Lienholder("Lienholder");
	String value;
		
	private AccountType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
