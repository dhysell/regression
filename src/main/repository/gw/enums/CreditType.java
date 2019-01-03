package repository.gw.enums;

public enum CreditType {
	None("<none>"), 
	Bank_Fee("Bank Fee"),
	Reinstatement_Fee("Reinstatement Fee"),
	Other("Other");
	String value;
	
	private CreditType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
