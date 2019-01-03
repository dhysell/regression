package repository.gw.enums;

public enum DrivingExp {
	MoreThan2Years("More than 2 years"), 
	OneToTwoYears("1 - 2 years"), 
	SixToElevenMonths("6 - 11 months"), 
	LessThan6Months("Less than 6 months");
	String value;
	
	private DrivingExp(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
