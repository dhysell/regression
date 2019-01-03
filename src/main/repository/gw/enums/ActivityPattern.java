package repository.gw.enums;

public enum ActivityPattern {
	All("All"),
	Notification("Notification"),
	Approval("Approval"),
	Reminder("reminder"),
	Request("request"),
	ReviewDelinquency("reviewdelinquency"),
	DisbursmentError("disbursementerror"),
	DocumentType("documenttype"),
	RevewBankingFOWC("reviewbankinfowc"),
	ReviewBankFOFB("reviewbankinfofb"),
	ReviewDeliquencyWC("reviewdelinquencywc"),
	ReviewDisbursementWC("reviewdisbursementwc"),
	ReviewDeliquencyFB("reviewdelinquencyfb"),
	ReviewDisbursementFB("reviewdisbursementfb");
	String value;
	
	private ActivityPattern(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
