package repository.gw.enums;

public enum DelinquencyPlan {
	
	InsuredDelinquency("Insured Deliquency Plan"),
	PolicyLevelDelinquencyPlan ("Policy Level Delinquency Plan");
	String value;
		
	private DelinquencyPlan(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
