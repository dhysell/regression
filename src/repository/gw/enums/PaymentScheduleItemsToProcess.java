package repository.gw.enums;

public enum PaymentScheduleItemsToProcess {
	All_Items("All Items"),
	Planned_Items("Planned Items"),
	Not_Fully_Paid_Items("Not Fully Paid Items"),
	No_Items("No Items"),
	Planned_And_Biled_Items("Planned and Biled Items");
	
	String value;
	
	private PaymentScheduleItemsToProcess(String value){
		this.value = value;
	}
	
	public String getValue(){
		return this.value;
	}
}
