package repository.gw.enums;

public enum NotRatedReason {
	
	ArmedForces("Armed Forces"),
	ExchangeStudent("Exchange Student"),
	LackOfUnderwritingInformation("Lack Of Underwriting Information"),
	Missionary("Missionary"),
	NonDriver("Non Driver"),
	Other("Other"),
	Permit("Permit"),
	RatedOnOtherPolicy("Rated on Other Policy");
	String value;
	
	private NotRatedReason(String reason){
		value = reason;
	}
	
	public String getValue(){
		return value;
	}

}
