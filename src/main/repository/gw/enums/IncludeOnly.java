package repository.gw.enums;

public enum IncludeOnly {
	Up_to_amount_billed("Up to amount billed"), 
	Up_to_amount_past_due("Up to amount past due"),
	Up_to_amount_under_contract("Up to amount under contract"),
	Up_to_next_invoice("Up to next invoice");
	String value;
	
	private IncludeOnly(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
