package persistence.enums;

public enum FinanceUserRole {
	Finance_Clerical("FinanceClerical"), 
	Finance_Manager("FinanceManager");
	String value;
	
	private FinanceUserRole(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
