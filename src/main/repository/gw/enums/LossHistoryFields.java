package repository.gw.enums;

public enum LossHistoryFields {
	OccuranceDate("Occurrence Date"),
	Description("Description"),
	TotalIncurred("Total Incurred"),
	AmountPaid("Amount Paid"),
	OpenReserve("Amount Reserved"),
	Status("Status");
	String value;
	
	private LossHistoryFields(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
