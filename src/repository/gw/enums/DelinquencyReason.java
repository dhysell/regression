package repository.gw.enums;

public enum DelinquencyReason {	
	PastDueFullCancel("Past Due Full Cancel"),
	PastDuePartialCancel("Past Due Partial Cancel"),
	PastDueLienPartialCancel("Past Due Lien Partial Cancel"),
	UnderwritingPartialCancel("Underwriting Partial Cancel"),	
	UnderwritingFullCancel("Underwriting Full Cancel"),
	NotTaken("Not Taken"),
	NotTakenPartial("Not Taken Partial"),
	NonPremiumDelinquency("Non-Premium Delinquency"),
	LienOnlyChargesPastDue("Lien only charges past due");
	
	String value;
		
	private DelinquencyReason(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
